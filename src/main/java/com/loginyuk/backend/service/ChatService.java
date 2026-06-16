/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loginyuk.backend.service;

import com.loginyuk.backend.model.ChatMessageEntity;
import com.loginyuk.backend.model.ChatRoom;
import com.loginyuk.backend.repository.ChatMessageRepository;
import com.loginyuk.backend.repository.ChatRoomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    /**
     * Cari room antara buyer dan seller.
     * Kalau belum ada, buat baru (dengan gameId opsional).
     */
    public ChatRoom getOrCreateRoom(Long buyerId, Long sellerId, Long gameId) {
        return chatRoomRepository
                .findByBuyerIdAndSellerId(buyerId, sellerId)
                .orElseGet(() -> {
                    ChatRoom room = new ChatRoom();
                    room.setBuyerId(buyerId);
                    room.setSellerId(sellerId);
                    room.setGameId(gameId);
                    return chatRoomRepository.save(room);
                });
    }

    /**
     * Simpan pesan ke tabel chat_message.
     */
    public ChatMessageEntity saveMessage(Long roomId, Long senderId, String isi) {
        ChatMessageEntity msg = new ChatMessageEntity();
        msg.setRoomId(roomId);
        msg.setSenderId(senderId);
        msg.setIsi(isi);
        return chatMessageRepository.save(msg);
    }

    /**
     * Ambil history pesan dalam sebuah room.
     */
    public List<ChatMessageEntity> getHistory(Long roomId) {
        return chatMessageRepository.findByRoomIdOrderByWaktuAsc(roomId);
    }
}