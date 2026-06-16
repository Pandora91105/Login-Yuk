package com.loginyuk.backend.service;

import com.loginyuk.backend.model.*;
import com.loginyuk.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired private TransaksiRepository transaksiRepository;
    @Autowired private GameRepository gameRepository;
    @Autowired private UserRepository userRepository;

    private static final List<String> DAFTAR_GAME =
        List.of("Mobile Legends", "PUBG", "Clash of Clans");

    public Map<String, Object> getLaporanBulanan(Long sellerId, int bulan, int tahun) {
        User user = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        if (!"SELLER".equals(user.getRole())) {
            throw new RuntimeException("Akses ditolak: hanya untuk penjual");
        }

        List<Transaksi> transaksiBulanIni =
            transaksiRepository.findBySellerIdAndBulanTahun(sellerId, bulan, tahun);

        List<Transaksi> berhasil = transaksiBulanIni.stream()
                .filter(t -> "SUCCESS".equals(t.getStatus())).collect(Collectors.toList());
        List<Transaksi> batal = transaksiBulanIni.stream()
                .filter(t -> "CANCELLED".equals(t.getStatus())).collect(Collectors.toList());

        double totalPendapatan = berhasil.stream().mapToDouble(t -> t.getGame().getHarga()).sum();
        int totalProdukTerjual = berhasil.size();
        double rataTransaksi = totalProdukTerjual > 0 ? totalPendapatan / totalProdukTerjual : 0;

        Map<String, Long> produkTerjual = berhasil.stream()
                .collect(Collectors.groupingBy(t -> t.getGame().getNamaGame(), Collectors.counting()));

        List<Game> gameTersedia = gameRepository.findBySellerIdAndStatus(sellerId, "AVAILABLE");
        Map<String, Long> produkTidakTerjual = gameTersedia.stream()
                .collect(Collectors.groupingBy(Game::getNamaGame, Collectors.counting()));

        Map<String, int[]> stokBarang = hitungStokDinamis(sellerId, bulan, tahun);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("bulan", getNamaBulan(bulan));
        result.put("tahun", tahun);
        result.put("tanggalCetak", LocalDateTime.now().toString());

        Map<String, Object> ringkasanKeuangan = new LinkedHashMap<>();
        ringkasanKeuangan.put("totalPendapatan", totalPendapatan);
        ringkasanKeuangan.put("totalProdukTerjual", totalProdukTerjual);
        ringkasanKeuangan.put("rataTransaksi", rataTransaksi);
        result.put("ringkasanKeuangan", ringkasanKeuangan);

        Map<String, Object> detailPemesanan = new LinkedHashMap<>();
        detailPemesanan.put("berhasil", berhasil.size());
        detailPemesanan.put("batal", batal.size());
        result.put("detailPemesanan", detailPemesanan);

        result.put("produkTerjual", lengkapiSemuaGame(produkTerjual));
        result.put("produkTidakTerjual", lengkapiSemuaGame(produkTidakTerjual));
        result.put("stokBarang", stokBarang);

        return result;
    }

    // ===== Kalkulasi stok dinamis (opsi 2) =====
    private Map<String, int[]> hitungStokDinamis(Long sellerId, int bulan, int tahun) {
        YearMonth ym = YearMonth.of(tahun, bulan);
        LocalDateTime akhirBulan = ym.atEndOfMonth().atTime(23, 59, 59);
        LocalDateTime awalBulan = ym.atDay(1).atStartOfDay();

        // Semua game seller yang sudah dibuat sampai akhir bulan ini
        List<Game> gameSampaiAkhirBulan =
            gameRepository.findBySellerIdAndStatus(sellerId,"AVAILABLE");

        // Semua transaksi SUCCESS sampai akhir bulan ini
        List<Transaksi> terjualSampaiAkhirBulan =
            transaksiRepository.findSuccessBySellerIdBefore(sellerId, akhirBulan);

        // Transaksi SUCCESS yang terjadi SELAMA bulan ini saja
        List<Transaksi> terjualSelamaBulanIni = terjualSampaiAkhirBulan.stream()
                .filter(t -> !t.getWaktuTransaksi().isBefore(awalBulan))
                .collect(Collectors.toList());

        Map<String, int[]> hasil = new LinkedHashMap<>();
        for (String nama : DAFTAR_GAME) {
            long dibuat = gameSampaiAkhirBulan.stream()
                    .filter(g -> nama.equals(g.getNamaGame())).count();
            long terjualTotal = terjualSampaiAkhirBulan.stream()
                    .filter(t -> nama.equals(t.getGame().getNamaGame())).count();
            long terjualBulanIni = terjualSelamaBulanIni.stream()
                    .filter(t -> nama.equals(t.getGame().getNamaGame())).count();

            int stokAkhir = (int) (dibuat - terjualTotal);
            int stokAwal = stokAkhir + (int) terjualBulanIni;

            hasil.put(nama, new int[]{Math.max(stokAwal, 0), Math.max(stokAkhir, 0)});
        }
        return hasil;
    }

    private Map<String, Long> lengkapiSemuaGame(Map<String, Long> data) {
        Map<String, Long> hasil = new LinkedHashMap<>();
        for (String nama : DAFTAR_GAME) hasil.put(nama, data.getOrDefault(nama, 0L));
        return hasil;
    }

    private String getNamaBulan(int bulan) {
        String[] arr = {"", "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                        "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return arr[bulan];
    }
}