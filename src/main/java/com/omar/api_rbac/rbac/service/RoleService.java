package com.omar.api_rbac.rbac.service;

import com.omar.api_rbac.common.exception.DuplicateResourceException;
import com.omar.api_rbac.rbac.dto.RoleRequest;
import com.omar.api_rbac.rbac.dto.RoleResponse;
import com.omar.api_rbac.rbac.model.Permission;
import com.omar.api_rbac.rbac.model.Role;
import com.omar.api_rbac.rbac.repository.PermissionRepository;
import com.omar.api_rbac.rbac.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public List<RoleResponse> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public RoleResponse findById(String id) {
        return toResponse(getRoleOrThrow(id));
    }

    @Transactional
    public RoleResponse create(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("role", request.getName());
        }

        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        if (request.getParentRoleId() != null) {
            Role parent = getRoleOrThrow(request.getParentRoleId());
            role.setParentRole(parent);
        }

        return toResponse(roleRepository.save(role));
    }

    @Transactional
    public RoleResponse assignPermission(String roleId, String permissionId) {
        Role role = getRoleOrThrow(roleId);
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new NoSuchElementException(
                    "Permission not found: " + permissionId
                ));

        role.getPermissions().add(permission);
        return toResponse(roleRepository.save(role));
    }

    @Transactional
    public RoleResponse revokePermission(String roleId, String permissionId) {
        Role role = getRoleOrThrow(roleId);
        role.getPermissions().removeIf(p -> p.getId().equals(permissionId));
        return toResponse(roleRepository.save(role));
    }

    @Transactional
    public void delete(String id) {
        if (!roleRepository.existsById(id)) {
            throw new NoSuchElementException("Role not found: " + id);
        }
        roleRepository.deleteById(id);
    }

    private Role getRoleOrThrow(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                    "Role not found: " + id
                ));
    }

    private RoleResponse toResponse(Role role) {
        Set<String> permissions = role.getPermissions()
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .parentRoleName(role.getParentRole() != null
                    ? role.getParentRole().getName() : null)
                .permissions(permissions)
                .build();
    }
} 
