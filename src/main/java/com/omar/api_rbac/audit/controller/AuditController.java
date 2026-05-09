package com.omar.api_rbac.audit.controller;

import com.omar.api_rbac.audit.model.AuditLog;
import com.omar.api_rbac.audit.service.AuditService;
import com.omar.api_rbac.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> findAll(
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(auditService.findAll(pageable)));
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.principal.username")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> findByUser(
            @PathVariable String username,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
            ApiResponse.success(auditService.findByUser(username, pageable))
        );
    }

    @GetMapping("/range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> findByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
            ApiResponse.success(auditService.findByDateRange(from, to, pageable))
        );
    }

    @GetMapping("/failures")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AuditLog>>> findFailures(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
            ApiResponse.success(auditService.findFailures(pageable))
        );
    }
} 
