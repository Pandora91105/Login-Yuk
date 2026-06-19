package com.loginyuk.backend.controller;

import com.loginyuk.backend.model.ChatRoom;
import com.loginyuk.backend.model.User;
import com.loginyuk.backend.repository.ChatRoomRepository;
import com.loginyuk.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getUserRooms(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        Long currentId = currentUser.getId();

        List<ChatRoom> rooms = chatRoomRepository
            .findByBuyerIdOrSellerId(currentId, currentId);
            

        List<Map<String, Object>> result = rooms.stream().map(room -> {
            Long opponentId = room.getBuyerId().equals(currentId)
                ? room.getSellerId()
                : room.getBuyerId();

            User opponent = userRepository.findById(opponentId).orElse(null);

            Map<String, Object> map = new HashMap<>();
            map.put("roomId", room.getId());
            map.put("opponentName", opponent != null ? opponent.getUsername() : "Unknown");
            map.put("opponentPhoto", opponent != null && opponent.getProfilePhoto() != null
                ? opponent.getProfilePhoto()
                : "assets/default-avatar.png");
            return map;
        }).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoomDetail(
            @PathVariable Long roomId,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        ChatRoom room = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new RuntimeException("Room not found"));

        Long opponentId = room.getBuyerId().equals(currentUser.getId())
            ? room.getSellerId()
            : room.getBuyerId();

        User opponent = userRepository.findById(opponentId)
            .orElseThrow(() -> new RuntimeException("Opponent not found"));

        Map<String, Object> map = new HashMap<>();
        map.put("roomId", room.getId());
        map.put("opponentName", opponent.getUsername());
        map.put("opponentPhoto", opponent.getProfilePhoto() != null
            ? opponent.getProfilePhoto()
            : "assets/default-avatar.png");

        return ResponseEntity.ok(map);
    }
}