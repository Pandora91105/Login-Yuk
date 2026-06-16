package com.loginyuk.backend.controller;

import com.loginyuk.backend.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // GET /api/report/bulanan?sellerId=2&bulan=6&tahun=2026
    @GetMapping("/bulanan")
    public ResponseEntity<?> getLaporanBulanan(
            @RequestParam Long sellerId,
            @RequestParam int bulan,
            @RequestParam int tahun) {
        try {
            Map<String, Object> result = reportService.getLaporanBulanan(sellerId, bulan, tahun);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}