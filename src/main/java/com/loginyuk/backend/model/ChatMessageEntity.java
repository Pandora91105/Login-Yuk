package com.loginyuk.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity untuk tabel chat_message di database.
 * Berbeda dari ChatMessage.java (DTO untuk WebSocket),
 * class ini dipakai untuk persistensi ke DB.
 */
@Entity
@Table(name = "chat_message")
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "isi", nullable = false, columnDefinition = "TEXT")
    private String isi;

    @Column(name = "waktu")
    private LocalDateTime waktu = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getIsi() { return isi; }
    public void setIsi(String isi) { this.isi = isi; }

    public LocalDateTime getWaktu() { return waktu; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }
}
