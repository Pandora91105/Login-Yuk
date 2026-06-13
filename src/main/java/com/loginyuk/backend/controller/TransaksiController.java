package com.loginyuk.backend.controller;

import com.loginyuk.backend.dto.TransaksiRequest;
import com.loginyuk.backend.model.Game;
import com.loginyuk.backend.model.Transaksi;
import com.loginyuk.backend.service.TransaksiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transaksi")
@CrossOrigin(origins = "http://localhost:3000")
public class TransaksiController {

    @Autowired
    private TransaksiService transaksiService;

    @GetMapping("/games")
    public List<Game> getGames() {
        return transaksiService.getAvailableGames();
    }

    @PostMapping("/buat")
    public Transaksi buatTransaksi(@RequestBody TransaksiRequest req) {
        return transaksiService.buatTransaksi(req);
    }

    @PutMapping("/konfirmasi/{id}")
    public Transaksi konfirmasi(@PathVariable Long id) {
        return transaksiService.konfirmasiPembayaran(id);
    }

    @GetMapping("/riwayat/{buyerId}")
    public List<Transaksi> getRiwayat(@PathVariable Long buyerId) {
        return transaksiService.getRiwayat(buyerId);
    }
}