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

    // GET /api/report/dashboard?userId=2
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(@RequestParam Long userId) {
        try {
            Map<String, Object> result = reportService.getReportDashboard(userId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
