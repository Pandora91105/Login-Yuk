package com.loginyuk.backend.repository;

import com.loginyuk.backend.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findBySellerId(Long sellerId);
    Optional<Report> findBySellerIdAndBulanAndTahun(Long sellerId, Integer bulan, Integer tahun);
}
