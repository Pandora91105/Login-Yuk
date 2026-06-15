package com.loginyuk.backend.service;

import com.loginyuk.backend.model.*;
import com.loginyuk.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getReportDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        if (!"SELLER".equals(user.getRole())) {
            throw new RuntimeException("Akses ditolak: hanya untuk penjual");
        }

        Long sellerId = userId;

        List<Transaksi> semuaTransaksi = transaksiRepository.findSuccessBySellerId(sellerId);

        int totalPenjualan = semuaTransaksi.size();
        double totalPendapatan = semuaTransaksi.stream()
                .mapToDouble(t -> t.getGame().getHarga())
                .sum();
        double rataTransaksi = totalPenjualan > 0 ? totalPendapatan / totalPenjualan : 0;

        Map<String, Map<String, Object>> perBulan = new LinkedHashMap<>();
        for (Transaksi t : semuaTransaksi) {
            int bulan = t.getWaktuTransaksi().getMonthValue();
            int tahun = t.getWaktuTransaksi().getYear();
            String key = tahun + "-" + String.format("%02d", bulan);

            perBulan.computeIfAbsent(key, k -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("bulan", bulan);
                m.put("tahun", tahun);
                m.put("label", getNamaBulan(bulan) + " " + tahun);
                m.put("totalPenjualan", 0);
                m.put("totalPendapatan", 0.0);
                m.put("invoices", new ArrayList<Map<String, Object>>());
                return m;
            });

            Map<String, Object> data = perBulan.get(key);
            data.put("totalPenjualan", (int) data.get("totalPenjualan") + 1);
            data.put("totalPendapatan", (double) data.get("totalPendapatan") + t.getGame().getHarga());

            Map<String, Object> invoiceDetail = new LinkedHashMap<>();
            invoiceDetail.put("transaksiId", t.getId());
            invoiceDetail.put("namaGame", t.getGame().getNamaGame());
            invoiceDetail.put("harga", t.getGame().getHarga());
            invoiceDetail.put("metodePembayaran", t.getMetodePembayaran());
            invoiceDetail.put("waktu", t.getWaktuTransaksi().toString());
            ((List<Map<String, Object>>) data.get("invoices")).add(invoiceDetail);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("namaToko", getNamaToko(sellerId));
        result.put("totalPenjualanKeseluruhan", totalPenjualan);
        result.put("totalPendapatanKeseluruhan", totalPendapatan);
        result.put("rataRataPerTransaksi", rataTransaksi);
        result.put("rangkumanPerBulan", new ArrayList<>(perBulan.values()));

        return result;
    }

    private String getNamaBulan(int bulan) {
        String[] bulanArr = {"", "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                             "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return bulanArr[bulan];
    }

    private String getNamaToko(Long sellerId) {
        return reportRepository.findBySellerId(sellerId).stream()
                .findFirst()
                .map(r -> r.getSeller().getNamaToko())
                .orElse("Toko Saya");
    }
}