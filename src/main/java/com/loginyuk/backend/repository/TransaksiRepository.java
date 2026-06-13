package com.loginyuk.backend.repository;

import com.loginyuk.backend.model.Transaksi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
    List<Transaksi> findByBuyerId(Long buyerId);
}