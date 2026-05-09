package com.omar.api_rbac.rbac.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private boolean enabled;
    private Set<String> roles;
    private LocalDateTime createdAt;
} 
