package com.omar.api_rbac.rbac.service;

import com.omar.api_rbac.rbac.dto.UserResponse;
import com.omar.api_rbac.rbac.model.Role;
import com.omar.api_rbac.rbac.model.User;
import com.omar.api_rbac.rbac.repository.RoleRepository;
import com.omar.api_rbac.rbac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse findById(String id) {
        return toResponse(getUserOrThrow(id));
    }

    @Transactional
    public UserResponse assignRole(String userId, String roleId) {
        User user = getUserOrThrow(userId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException(
                    "Role not found: " + roleId
                ));

        user.getRoles().add(role);
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse revokeRole(String userId, String roleId) {
        User user = getUserOrThrow(userId);
        user.getRoles().removeIf(r -> r.getId().equals(roleId));
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void toggleEnabled(String userId) {
        User user = getUserOrThrow(userId);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    private User getUserOrThrow(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                    "User not found: " + id
                ));
    }

    private UserResponse toResponse(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .roles(roles)
                .createdAt(user.getCreatedAt())
                .build();
    }
} 
