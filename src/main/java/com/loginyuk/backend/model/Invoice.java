package com.loginyuk.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "transaksi_id")
    private Transaksi transaksi;

    private String detail;
    private Double totalHarga;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Transaksi getTransaksi() { return transaksi; }
    public void setTransaksi(Transaksi transaksi) { this.transaksi = transaksi; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
    public Double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(Double totalHarga) { this.totalHarga = totalHarga; }
}