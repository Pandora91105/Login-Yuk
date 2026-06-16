package com.loginyuk.backend.controller;

import com.loginyuk.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * GET /api/report/laporan?userId=2&bulan=1&tahun=2026
     * bulan & tahun bersifat opsional, default ke bulan/tahun sekarang
     */
    @GetMapping("/laporan")
    public ResponseEntity<?> getLaporan(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer bulan,
            @RequestParam(required = false) Integer tahun) {
        try {
            int b = bulan != null ? bulan : LocalDate.now().getMonthValue();
            int t = tahun != null ? tahun : LocalDate.now().getYear();
            Map<String, Object> result = reportService.getLaporanBulanan(userId, b, t);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}