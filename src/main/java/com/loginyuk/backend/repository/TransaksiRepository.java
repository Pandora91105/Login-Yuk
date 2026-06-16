package com.loginyuk.backend.repository;

import com.loginyuk.backend.model.Transaksi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
    List<Transaksi> findByBuyerId(Long buyerId);

    @Query("SELECT t FROM Transaksi t WHERE t.game.seller.id = :sellerId " +
           "AND MONTH(t.waktuTransaksi) = :bulan AND YEAR(t.waktuTransaksi) = :tahun")
    List<Transaksi> findBySellerIdAndBulanTahun(
        @Param("sellerId") Long sellerId, @Param("bulan") int bulan, @Param("tahun") int tahun);

    // Transaksi SUCCESS milik seller, sebelum/sampai batas waktu tertentu
    @Query("SELECT t FROM Transaksi t WHERE t.game.seller.id = :sellerId " +
           "AND t.status = 'SUCCESS' AND t.waktuTransaksi <= :batas")
    List<Transaksi> findSuccessBySellerIdBefore(
        @Param("sellerId") Long sellerId, @Param("batas") LocalDateTime batas);
}

