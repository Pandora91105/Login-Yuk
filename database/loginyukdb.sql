-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 05, 2026 at 01:33 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `loginyukdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `buyer`
--

CREATE TABLE `buyer` (
  `buyer_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `buyer`
--

INSERT INTO `buyer` (`buyer_id`) VALUES
(1);

-- --------------------------------------------------------

--
-- Table structure for table `chat_message`
--

CREATE TABLE `chat_message` (
  `message_id` bigint(20) NOT NULL,
  `room_id` bigint(20) NOT NULL,
  `sender_id` bigint(20) NOT NULL,
  `isi` text NOT NULL,
  `waktu` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `chat_room`
--

CREATE TABLE `chat_room` (
  `room_id` bigint(20) NOT NULL,
  `buyer_id` bigint(20) NOT NULL,
  `seller_id` bigint(20) NOT NULL,
  `game_id` bigint(20) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `game`
--

CREATE TABLE `game` (
  `game_id` bigint(20) NOT NULL,
  `seller_id` bigint(20) NOT NULL,
  `nama_game` enum('Mobile Legends','PUBG','Clash of Clans') NOT NULL,
  `judul` varchar(150) NOT NULL,
  `deskripsi` text DEFAULT NULL,
  `harga` decimal(12,2) NOT NULL,
  `status` enum('AVAILABLE','SOLD') DEFAULT 'AVAILABLE',
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `game`
--

INSERT INTO `game` (`game_id`, `seller_id`, `nama_game`, `judul`, `deskripsi`, `harga`, `status`, `created_at`) VALUES
(1, 2, 'Mobile Legends', 'Akun ML Rank Mythic — 40+ Hero', 'Rank: Epic 3 ★★★ (siap push lag)\nTotal Hero: 40+ hero\nSkin: 12 skin (termasuk skin M3s)\nWinrate Hlida: 65%+\nEmblem: Sudah tinggi & siap ranked\nBattle Point & Tiket masih tersedia.\n\nKekurangan: Cocok untuk player EXP Lane / Roamer. Tidak pernah banned.', 250000.00, 'AVAILABLE', '2026-06-05 18:23:54'),
(2, 2, 'Mobile Legends', 'Akun ML Rank Mythic Glory — Collector Skin', 'Rank: Mythic Glory (top 500 server)\nTotal Hero: 80+ hero\nSkin: 35+ skin termasuk Special & Limited\nWinrate: 58%\nAkun tua sejak season 5.\n\nKekurangan: Beberapa hero belum dikuasai.', 2745000.00, 'AVAILABLE', '2026-06-05 18:23:54'),
(3, 2, 'Clash of Clans', 'CoC Town Hall 15 — Full Upgrade', 'Town Hall: Level 15\nTroops: Hampir full upgrade\nHeroes: Barbarian King 75 / Archer Queen 75 / Grand Warden 50 / Royal Champion 40\nClan: Sudah punya clan sendiri rank Gold 1\nResources: Gold & Elixir penuh.', 1250000.00, 'SOLD', '2026-06-05 18:23:54');

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

CREATE TABLE `invoice` (
  `invoice_id` bigint(20) NOT NULL,
  `transaksi_id` bigint(20) NOT NULL,
  `detail` text DEFAULT NULL,
  `issued_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `invoice`
--

INSERT INTO `invoice` (`invoice_id`, `transaksi_id`, `detail`, `issued_at`) VALUES
(1, 1, 'Invoice #0001 | Produk: CoC Town Hall 15 — Full Upgrade | Pembeli: andi_buyer | Penjual: Rizky Game Store | Harga: Rp 1.250.000 | Metode: QRIS | Tanggal: 27 Mei 2026', '2026-05-27 14:32:05');

-- --------------------------------------------------------

--
-- Table structure for table `report`
--

CREATE TABLE `report` (
  `report_id` bigint(20) NOT NULL,
  `seller_id` bigint(20) NOT NULL,
  `bulan` tinyint(4) NOT NULL,
  `tahun` smallint(6) NOT NULL,
  `total_penjualan` int(11) DEFAULT 0,
  `total_pendapatan` decimal(14,2) DEFAULT 0.00,
  `rata_transaksi` decimal(14,2) DEFAULT 0.00,
  `generated_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `report`
--

INSERT INTO `report` (`report_id`, `seller_id`, `bulan`, `tahun`, `total_penjualan`, `total_pendapatan`, `rata_transaksi`, `generated_at`) VALUES
(1, 2, 5, 2026, 1, 1250000.00, 1250000.00, '2026-06-01 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `seller`
--

CREATE TABLE `seller` (
  `seller_id` bigint(20) NOT NULL,
  `nama_toko` varchar(100) NOT NULL,
  `deskripsi` text DEFAULT NULL,
  `last_active` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `seller`
--

INSERT INTO `seller` (`seller_id`, `nama_toko`, `deskripsi`, `last_active`) VALUES
(2, 'Rizky Game Store', 'Jual akun ML, PUBG, dan CoC berkualitas. Aman & terpercaya!', '2026-06-05 18:23:05');

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `transaksi_id` bigint(20) NOT NULL,
  `buyer_id` bigint(20) NOT NULL,
  `game_id` bigint(20) NOT NULL,
  `harga` decimal(12,2) NOT NULL,
  `metode_bayar` enum('QRIS','GOPAY','OVO','DANA','BCA','MANDIRI','BNI','BRI') NOT NULL,
  `status` enum('PENDING','SUCCESS','CANCELLED') DEFAULT 'PENDING',
  `waktu_transaksi` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transaksi`
--

INSERT INTO `transaksi` (`transaksi_id`, `buyer_id`, `game_id`, `harga`, `metode_bayar`, `status`, `waktu_transaksi`) VALUES
(1, 1, 3, 1250000.00, 'QRIS', 'SUCCESS', '2026-05-27 14:32:00');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('BUYER','SELLER') NOT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `email`, `password`, `role`, `created_at`) VALUES
(1, 'andi_buyer', 'andi@mail.com', 'pbojaya', 'BUYER', '2026-06-05 18:22:21'),
(2, 'toko_rizky', 'rizky@mail.com', 'ekigepeng', 'SELLER', '2026-06-05 18:22:21');

-- --------------------------------------------------------

--
-- Table structure for table `wishlist`
--

CREATE TABLE `wishlist` (
  `wishlist_id` bigint(20) NOT NULL,
  `buyer_id` bigint(20) NOT NULL,
  `game_id` bigint(20) NOT NULL,
  `added_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wishlist`
--

INSERT INTO `wishlist` (`wishlist_id`, `buyer_id`, `game_id`, `added_at`) VALUES
(1, 1, 1, '2026-06-05 18:24:11');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `buyer`
--
ALTER TABLE `buyer`
  ADD PRIMARY KEY (`buyer_id`);

--
-- Indexes for table `chat_message`
--
ALTER TABLE `chat_message`
  ADD PRIMARY KEY (`message_id`),
  ADD KEY `room_id` (`room_id`),
  ADD KEY `sender_id` (`sender_id`);

--
-- Indexes for table `chat_room`
--
ALTER TABLE `chat_room`
  ADD PRIMARY KEY (`room_id`),
  ADD UNIQUE KEY `uq_buyer_seller` (`buyer_id`,`seller_id`),
  ADD KEY `seller_id` (`seller_id`);

--
-- Indexes for table `game`
--
ALTER TABLE `game`
  ADD PRIMARY KEY (`game_id`),
  ADD KEY `seller_id` (`seller_id`);

--
-- Indexes for table `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`invoice_id`),
  ADD UNIQUE KEY `transaksi_id` (`transaksi_id`);

--
-- Indexes for table `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`report_id`),
  ADD KEY `seller_id` (`seller_id`);

--
-- Indexes for table `seller`
--
ALTER TABLE `seller`
  ADD PRIMARY KEY (`seller_id`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`transaksi_id`),
  ADD KEY `buyer_id` (`buyer_id`),
  ADD KEY `game_id` (`game_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `wishlist`
--
ALTER TABLE `wishlist`
  ADD PRIMARY KEY (`wishlist_id`),
  ADD UNIQUE KEY `uq_buyer_game` (`buyer_id`,`game_id`),
  ADD KEY `game_id` (`game_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `chat_message`
--
ALTER TABLE `chat_message`
  MODIFY `message_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `chat_room`
--
ALTER TABLE `chat_room`
  MODIFY `room_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `game`
--
ALTER TABLE `game`
  MODIFY `game_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `invoice`
--
ALTER TABLE `invoice`
  MODIFY `invoice_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `report`
--
ALTER TABLE `report`
  MODIFY `report_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `transaksi_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `wishlist`
--
ALTER TABLE `wishlist`
  MODIFY `wishlist_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `buyer`
--
ALTER TABLE `buyer`
  ADD CONSTRAINT `buyer_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `chat_message`
--
ALTER TABLE `chat_message`
  ADD CONSTRAINT `chat_message_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `chat_room` (`room_id`),
  ADD CONSTRAINT `chat_message_ibfk_2` FOREIGN KEY (`sender_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `chat_room`
--
ALTER TABLE `chat_room`
  ADD CONSTRAINT `chat_room_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `buyer` (`buyer_id`),
  ADD CONSTRAINT `chat_room_ibfk_2` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`seller_id`);

--
-- Constraints for table `game`
--
ALTER TABLE `game`
  ADD CONSTRAINT `game_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`seller_id`);

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`transaksi_id`) REFERENCES `transaksi` (`transaksi_id`);

--
-- Constraints for table `report`
--
ALTER TABLE `report`
  ADD CONSTRAINT `report_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `seller` (`seller_id`);

--
-- Constraints for table `seller`
--
ALTER TABLE `seller`
  ADD CONSTRAINT `seller_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `buyer` (`buyer_id`),
  ADD CONSTRAINT `transaksi_ibfk_2` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`);

--
-- Constraints for table `wishlist`
--
ALTER TABLE `wishlist`
  ADD CONSTRAINT `wishlist_ibfk_1` FOREIGN KEY (`buyer_id`) REFERENCES `buyer` (`buyer_id`),
  ADD CONSTRAINT `wishlist_ibfk_2` FOREIGN KEY (`game_id`) REFERENCES `game` (`game_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
