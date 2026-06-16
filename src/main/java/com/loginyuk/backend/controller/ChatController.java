package com.loginyuk.backend.controller;

import com.loginyuk.backend.model.ChatMessage;
import com.loginyuk.backend.model.ChatMessageEntity;
import com.loginyuk.backend.service.ChatService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
        System.out.println("Received:");
        System.out.println("sender   = " + message.getSender());
        System.out.println("content  = " + message.getContent());
        System.out.println("roomId   = " + message.getRoomId());
 
        messagingTemplate.convertAndSend(
            "/topic/rooms/" + message.getRoomId(), message
        );

        // Simpan ke database
        // Catatan: message.getRoomId() adalah String dari ChatMessage DTO,
        // pastikan frontend mengirim roomId yang valid (angka).
        try {
            Long roomId   = Long.parseLong(message.getRoomId());
            Long senderId = message.getSenderId(); // lihat catatan di bawah
            chatService.saveMessage(roomId, senderId, message.getContent());
        } catch (NumberFormatException e) {
            System.err.println("roomId bukan angka valid: " + message.getRoomId());
        }

        // Broadcast ke semua subscriber room ini
        messagingTemplate.convertAndSend(
            "/topic/rooms/" + message.getRoomId(),
            message
        );
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
}