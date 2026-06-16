package com.loginyuk.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaksi")
public class Transaksi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaksi_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    private String metodePembayaran;
    private String status;
    private LocalDateTime waktuTransaksi;
    private Double harga;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getBuyer() { return buyer; }
    public void setBuyer(User buyer) { this.buyer = buyer; }
    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }
    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getWaktuTransaksi() { return waktuTransaksi; }
    public void setWaktuTransaksi(LocalDateTime waktuTransaksi) { this.waktuTransaksi = waktuTransaksi; }
    public Double getHarga() { return harga; }
    public void setHarga(Double harga) { this.harga = harga; }
}