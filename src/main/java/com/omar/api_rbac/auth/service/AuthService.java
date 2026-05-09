 
package com.omar.api_rbac.auth.service;

import com.omar.api_rbac.auth.dto.AuthRequest;
import com.omar.api_rbac.auth.dto.AuthResponse;
import com.omar.api_rbac.auth.dto.RegisterRequest;
import com.omar.api_rbac.common.exception.DuplicateResourceException;
import com.omar.api_rbac.rbac.model.Role;
import com.omar.api_rbac.rbac.model.User;
import com.omar.api_rbac.rbac.repository.RoleRepository;
import com.omar.api_rbac.rbac.repository.UserRepository;
import com.omar.api_rbac.audit.model.AuditEvent;
import com.omar.api_rbac.audit.model.AuditEventType;
import com.omar.api_rbac.audit.service.AuditService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditService auditService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
        	throw new DuplicateResourceException("username", request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
        	throw new DuplicateResourceException("email", request.getEmail());
        }

        // Assigne le rôle USER par défaut
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException(
                    "Default role ROLE_USER not found — check Flyway seed data"
                ));

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>(Set.of(userRole)))
                .build();

        userRepository.save(user);
        
        auditService.publish(new AuditEvent(
        	    this,
        	    user.getId(),
        	    user.getUsername(),
        	    AuditEventType.REGISTER,
        	    "/api/v1/auth/register",
        	    null,
        	    true,
        	    null
        	));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    public AuthResponse login(AuthRequest request) {

        // Lance l'authentification — lève une exception si échec
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByUsernameWithRolesAndPermissions(request.getUsername())
                .orElseThrow();

        auditService.publish(new AuditEvent(
        	    this,
        	    user.getId(),
        	    user.getUsername(),
        	    AuditEventType.LOGIN_SUCCESS,
        	    "/api/v1/auth/login",
        	    null,
        	    true,
        	    null
        	));
        
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    private AuthResponse buildAuthResponse(User user,
                                           String accessToken,
                                           String refreshToken) {
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .roles(roles)
                .build();
    }
}