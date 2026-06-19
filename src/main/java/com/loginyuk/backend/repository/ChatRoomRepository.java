package com.loginyuk.backend.repository;

import com.loginyuk.backend.model.ChatRoom;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByBuyerIdAndSellerId(Long buyerId, Long sellerId);
    List<ChatRoom> findByBuyerIdOrSellerId(Long buyerId, Long sellerId);
}

