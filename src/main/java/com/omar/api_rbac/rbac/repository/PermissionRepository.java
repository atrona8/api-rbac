 package com.omar.api_rbac.rbac.repository;

import com.omar.api_rbac.rbac.model.Permission;
import com.omar.api_rbac.rbac.model.ActionType;
import com.omar.api_rbac.rbac.model.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Optional<Permission> findByName(String name);
    boolean existsByName(String name);
    boolean existsByResourceAndAction(ResourceType resource, ActionType action);
}
