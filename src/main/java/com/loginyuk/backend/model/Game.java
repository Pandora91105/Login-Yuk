package com.loginyuk.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;
    private String namaGame;
    private String deskripsi;
    private Double harga;
    private String status;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaGame() { return namaGame; }
    public void setNamaGame(String namaGame) { this.namaGame = namaGame; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public Double getHarga() { return harga; }
    public void setHarga(Double harga) { this.harga = harga; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Seller getSeller() { return seller; }
    public void setSeller(Seller seller) { this.seller = seller; }
}