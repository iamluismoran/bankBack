package com.app.bankback.controller;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthController {

        private final EntityManager em;

        @GetMapping("/health")
        public ResponseEntity<Map<String, Object>> health() {
            Map<String, Object> body = new HashMap<>();
            body.put("status", "UP");
            try {
                em.createNativeQuery("SELECT 1").getSingleResult();
                body.put("db", "UP");
                return ResponseEntity.ok(body);
            } catch (Exception ex) {
                body.put("db", "DOWN");
                body.put("error", ex.getMessage());
                return ResponseEntity.status(503).body(body);
            }
        }
}
