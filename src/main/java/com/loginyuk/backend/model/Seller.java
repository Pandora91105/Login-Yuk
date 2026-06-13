package com.loginyuk.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seller")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;
    private String namaToko;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaToko() { return namaToko; }
    public void setNamaToko(String namaToko) { this.namaToko = namaToko; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}