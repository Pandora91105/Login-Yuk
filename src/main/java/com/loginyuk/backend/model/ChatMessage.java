/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.loginyuk.backend.model;

/**
 * DTO (Data Transfer Object) untuk pesan WebSocket.
 * Class ini BUKAN entity DB — hanya dipakai untuk komunikasi
 * antara frontend dan backend via STOMP/WebSocket.
 *
 * Untuk persistensi ke DB, lihat ChatMessageEntity.java
 */
public class ChatMessage {

    private String sender;
    private String content;
    private String roomId;
    private Long senderId;   // ← TAMBAHAN: user_id dari tabel user
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE, SYSTEM
    }

    public String getSender() { 
        return sender; 
    }

    public void setSender(String sender) { 
        this.sender = sender; 
    }

    public String getContent() {
         return content; 
    }

    public void setContent(String content) {
         this.content = content; 
    }

    public String getRoomId() {
         return roomId; 
    }

    public void setRoomId(String roomId) {
         this.roomId = roomId; 
    }

    public Long getSenderId() {
         return senderId; 
    }
    
    public void setSenderId(Long senderId) {
         this.senderId = senderId; 
    }

    public MessageType getType() {
         return type; 
    }

    public void setType(MessageType type) {
         this.type = type; 
    }
}