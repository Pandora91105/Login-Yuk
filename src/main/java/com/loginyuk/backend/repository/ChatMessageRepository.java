package com.loginyuk.backend.repository;

import com.loginyuk.backend.model.ChatMessageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    /**
     * Ambil semua pesan dalam satu room, diurutkan dari yang terlama.
     * Dipakai untuk load history chat saat user buka room.
     */
    List<ChatMessageEntity> findByRoomIdOrderByWaktuAsc(Long roomId);
}
