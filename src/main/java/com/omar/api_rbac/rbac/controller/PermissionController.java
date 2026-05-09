package com.omar.api_rbac.rbac.controller;

import com.omar.api_rbac.common.response.ApiResponse;
import com.omar.api_rbac.rbac.dto.PermissionRequest;
import com.omar.api_rbac.rbac.model.Permission;
import com.omar.api_rbac.rbac.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Permission>>> findAll() {
        return ResponseEntity.ok(
            ApiResponse.success(permissionService.findAll())
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Permission>> findById(@PathVariable String id) {
        return ResponseEntity.ok(
            ApiResponse.success(permissionService.findById(id))
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Permission>> create(
            @Valid @RequestBody PermissionRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                    "Permission created", permissionService.create(request)
                ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        permissionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Permission deleted", null));
    }
} 
