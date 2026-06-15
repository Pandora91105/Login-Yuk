package com.loginyuk.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private Integer bulan;
    private Integer tahun;
    private Integer totalPenjualan;
    private Double totalPendapatan;
    private Double rataTransaksi;
    private LocalDateTime generatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Seller getSeller() { return seller; }
    public void setSeller(Seller seller) { this.seller = seller; }
    public Integer getBulan() { return bulan; }
    public void setBulan(Integer bulan) { this.bulan = bulan; }
    public Integer getTahun() { return tahun; }
    public void setTahun(Integer tahun) { this.tahun = tahun; }
    public Integer getTotalPenjualan() { return totalPenjualan; }
    public void setTotalPenjualan(Integer totalPenjualan) { this.totalPenjualan = totalPenjualan; }
    public Double getTotalPendapatan() { return totalPendapatan; }
    public void setTotalPendapatan(Double totalPendapatan) { this.totalPendapatan = totalPendapatan; }
    public Double getRataTransaksi() { return rataTransaksi; }
    public void setRataTransaksi(Double rataTransaksi) { this.rataTransaksi = rataTransaksi; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
