package com.omar.api_rbac.rbac.controller;

import com.omar.api_rbac.common.response.ApiResponse;
import com.omar.api_rbac.rbac.dto.RoleRequest;
import com.omar.api_rbac.rbac.dto.RoleResponse;
import com.omar.api_rbac.rbac.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(roleService.findAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> findById(
            @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(roleService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> create(
            @Valid @RequestBody RoleRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Role created", roleService.create(request)));
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> assignPermission(
            @PathVariable String roleId,
            @PathVariable String permissionId) {
        return ResponseEntity.ok(ApiResponse.success(
            "Permission assigned",
            roleService.assignPermission(roleId, permissionId)
        ));
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoleResponse>> revokePermission(
            @PathVariable String roleId,
            @PathVariable String permissionId) {
        return ResponseEntity.ok(ApiResponse.success(
            "Permission revoked",
            roleService.revokePermission(roleId, permissionId)
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        roleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Role deleted", null));
    }
}