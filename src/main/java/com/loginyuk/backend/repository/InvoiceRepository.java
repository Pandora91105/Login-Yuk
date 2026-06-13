package com.loginyuk.backend.repository;

import com.loginyuk.backend.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByTransaksiId(Long transaksiId);
}