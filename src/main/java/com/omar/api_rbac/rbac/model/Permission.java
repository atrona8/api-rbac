package com.omar.api_rbac.rbac.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource", nullable = false)
    private ResourceType resource;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ActionType action;
}