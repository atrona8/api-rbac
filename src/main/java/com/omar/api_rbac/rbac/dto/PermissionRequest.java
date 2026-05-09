package com.omar.api_rbac.rbac.dto;

import com.omar.api_rbac.rbac.model.ActionType;
import com.omar.api_rbac.rbac.model.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PermissionRequest {

    @NotBlank(message = "Permission name is required")
    @Size(max = 100)
    private String name;

    @Size(max = 200)
    private String description;

    @NotNull(message = "Resource is required")
    private ResourceType resource;

    @NotNull(message = "Action is required")
    private ActionType action;
} 
