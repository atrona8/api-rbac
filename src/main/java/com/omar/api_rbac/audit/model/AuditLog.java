package com.omar.api_rbac.audit.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs", indexes = {
    @Index(name = "idx_audit_user", columnList = "user_id"),
    @Index(name = "idx_audit_created", columnList = "created_at")
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "username")
    private String username;

    @Column(name = "action", nullable = false)
    private String action;  // ACCESS_GRANTED, ACCESS_DENIED, LOGIN, LOGOUT

    @Column(name = "resource")
    private String resource;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "success")
    private boolean success;

    @Column(name = "details", length = 500)
    private String details;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}