package com.loginyuk.backend.service;

import com.loginyuk.backend.dto.TransaksiRequest;
import com.loginyuk.backend.model.*;
import com.loginyuk.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransaksiService {

    @Autowired
    private TransaksiRepository transaksiRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Game> getAvailableGames() {
        return gameRepository.findByStatus("AVAILABLE");
    }

    public Transaksi buatTransaksi(TransaksiRequest req) {
        User buyer = userRepository.findById(req.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer tidak ditemukan"));

        Game game = gameRepository.findById(req.getGameId())
                .orElseThrow(() -> new RuntimeException("Game tidak ditemukan"));

        Transaksi transaksi = new Transaksi();
        transaksi.setBuyer(buyer);
        transaksi.setGame(game);
        transaksi.setMetodePembayaran(req.getMetodePembayaran());
        transaksi.setStatus("PENDING");
        transaksi.setWaktuTransaksi(LocalDateTime.now());
        transaksi = transaksiRepository.save(transaksi);

        game.setStatus("SOLD");
        gameRepository.save(game);

        Invoice invoice = new Invoice();
        invoice.setTransaksi(transaksi);
        invoice.setTotalHarga(game.getHarga());
        invoice.setDetail("Pembelian " + game.getNamaGame());
        invoiceRepository.save(invoice);

        return transaksi;
    }

    public Transaksi konfirmasiPembayaran(Long transaksiId) {
        Transaksi transaksi = transaksiRepository.findById(transaksiId)
                .orElseThrow(() -> new RuntimeException("Transaksi tidak ditemukan"));
        transaksi.setStatus("SUCCESS");
        return transaksiRepository.save(transaksi);
    }

    public List<Transaksi> getRiwayat(Long buyerId) {
        return transaksiRepository.findByBuyerId(buyerId);
    }
}