package com.omar.api_rbac.rbac.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class RoleResponse {
    private String id;
    private String name;
    private String description;
    private String parentRoleName;
    private Set<String> permissions;
} 
