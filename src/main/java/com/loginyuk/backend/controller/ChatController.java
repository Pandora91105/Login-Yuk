package com.loginyuk.backend.controller;

import com.loginyuk.backend.model.ChatMessage;
import com.loginyuk.backend.model.ChatMessageEntity;
import com.loginyuk.backend.model.User;
import com.loginyuk.backend.repository.UserRepository;
import com.loginyuk.backend.service.ChatService;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    /**
     * Terima pesan dari client via WebSocket, simpan ke DB, lalu broadcast ke room.
     *
     * Payload ChatMessage harus berisi:
     *   - sender    : username pengirim
     *   - content   : isi pesan
     *   - roomId    : ID room (String, nanti di-parse ke Long)
     *   - senderId  : ID user pengirim (Long, tambahkan field ini di ChatMessage)
     */
    @MessageMapping("/chat.send")
    public void sendToRoom(@Payload ChatMessage message) {
        System.out.println("=== sendToRoom called ===");

        // Broadcast SEKALI saja
        messagingTemplate.convertAndSend(
            "/topic/rooms/" + message.getRoomId(), message
        );

        // Simpan ke DB
        try {
            Long roomId = Long.parseLong(message.getRoomId());
            Long senderId = message.getSenderId();
            if (senderId != null) {
                chatService.saveMessage(roomId, senderId, message.getContent());
            }
        } catch (NumberFormatException e) {
            System.err.println("roomId bukan angka valid: " + message.getRoomId());
        }
    }

    /**
     * REST endpoint untuk load history chat sebelumnya saat user buka room.
     * GET /api/chat/history/{roomId}
     */
    @GetMapping("/api/chat/history/{roomId}")
    @ResponseBody
    public List<ChatMessageEntity> getChatHistory(@PathVariable Long roomId) {
        return chatService.getHistory(roomId);
    }

    /**
     * REST endpoint untuk membuat atau mendapatkan room antara buyer dan seller.
     * GET /api/chat/room?buyerId=1&sellerId=2&gameId=3
     */
    @GetMapping("/api/chat/room")
    @ResponseBody
    public com.loginyuk.backend.model.ChatRoom getOrCreateRoom(
            @org.springframework.web.bind.annotation.RequestParam Long buyerId,
            @org.springframework.web.bind.annotation.RequestParam Long sellerId,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long gameId) {
        return chatService.getOrCreateRoom(buyerId, sellerId, gameId);
    }

    // ── Private message (tidak berubah) ──────────────────────────────────────
    @MessageMapping("/chat.private.{targetUser}")
    public void sendPrivate(@DestinationVariable @NonNull String targetUser,
                            @Payload ChatMessage message, Principal sender) {
        message.setSender(sender.getName());
        messagingTemplate.convertAndSendToUser(targetUser, "/queue/messages", message);
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/user/me")
    public ResponseEntity<?> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        User user = UserRepository.findByUsername(userDetails.getUsername());

        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("profilePhoto", user.getProfilePhoto());
        map.put("role", user.getRole());

        return ResponseEntity.ok(map);
    }
}