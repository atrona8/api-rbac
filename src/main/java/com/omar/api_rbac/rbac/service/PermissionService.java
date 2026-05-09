package com.omar.api_rbac.rbac.service;

import com.omar.api_rbac.common.exception.DuplicateResourceException;
import com.omar.api_rbac.rbac.dto.PermissionRequest;
import com.omar.api_rbac.rbac.model.Permission;
import com.omar.api_rbac.rbac.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findById(String id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                    "Permission not found: " + id
                ));
    }

    @Transactional
    public Permission create(PermissionRequest request) {
        if (permissionRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("permission", request.getName());
        }
        if (permissionRepository.existsByResourceAndAction(
                request.getResource(), request.getAction())) {
            throw new DuplicateResourceException(
                "resource:action",
                request.getResource() + ":" + request.getAction()
            );
        }

        Permission permission = Permission.builder()
                .name(request.getName())
                .description(request.getDescription())
                .resource(request.getResource())
                .action(request.getAction())
                .build();

        return permissionRepository.save(permission);
    }

    @Transactional
    public void delete(String id) {
        if (!permissionRepository.existsById(id)) {
            throw new NoSuchElementException("Permission not found: " + id);
        }
        permissionRepository.deleteById(id);
    }
} 
