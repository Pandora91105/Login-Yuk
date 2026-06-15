package com.loginyuk.backend.repository;

import com.loginyuk.backend.model.Transaksi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TransaksiRepository extends JpaRepository<Transaksi, Long> {
    List<Transaksi> findByBuyerId(Long buyerId);

    @Query("SELECT t FROM Transaksi t WHERE t.game.seller.id = :sellerId AND t.status = 'SUCCESS'")
    List<Transaksi> findSuccessBySellerId(@Param("sellerId") Long sellerId);

    @Query("SELECT t FROM Transaksi t WHERE t.game.seller.id = :sellerId " +
           "AND t.status = 'SUCCESS' " +
           "AND MONTH(t.waktuTransaksi) = :bulan " +
           "AND YEAR(t.waktuTransaksi) = :tahun")
    List<Transaksi> findSuccessBySellerIdAndBulanTahun(
        @Param("sellerId") Long sellerId,
        @Param("bulan") int bulan,
        @Param("tahun") int tahun
    );
}