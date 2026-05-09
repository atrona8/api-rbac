 
package com.omar.api_rbac.rbac.controller;

import com.omar.api_rbac.common.response.ApiResponse;
import com.omar.api_rbac.rbac.dto.UserResponse;
import com.omar.api_rbac.rbac.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(userService.findAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<ApiResponse<UserResponse>> findById(
            @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success(userService.findById(id)));
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> assignRole(
            @PathVariable String userId,
            @PathVariable String roleId) {
        return ResponseEntity.ok(ApiResponse.success(
            "Role assigned",
            userService.assignRole(userId, roleId)
        ));
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> revokeRole(
            @PathVariable String userId,
            @PathVariable String roleId) {
        return ResponseEntity.ok(ApiResponse.success(
            "Role revoked",
            userService.revokeRole(userId, roleId)
        ));
    }

    @PatchMapping("/{id}/toggle-enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> toggleEnabled(
            @PathVariable String id) {
        userService.toggleEnabled(id);
        return ResponseEntity.ok(ApiResponse.success("User status updated", null));
    }
}