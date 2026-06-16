package com.loginyuk.backend.repository;

import com.loginyuk.backend.model.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    /**
     * Cari room berdasarkan buyer dan seller.
     * Karena ada UNIQUE KEY uq_buyer_seller di DB,
     * dijamin maksimal 1 hasil.
     */
    Optional<ChatRoom> findByBuyerIdAndSellerId(Long buyerId, Long sellerId);
}
