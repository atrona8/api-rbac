 
package com.omar.api_rbac.rbac.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleRequest {

    @NotBlank(message = "Role name is required")
    @Size(min = 3, max = 50)
    @Pattern(
        regexp = "^ROLE_[A-Z_]+$",
        message = "Role name must start with ROLE_ and contain only uppercase letters"
    )
    private String name;

    @Size(max = 200)
    private String description;

    private String parentRoleId;
}