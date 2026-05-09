package com.omar.api_rbac.audit.service;

import com.omar.api_rbac.audit.model.AuditEvent;
import com.omar.api_rbac.audit.model.AuditLog;
import com.omar.api_rbac.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ApplicationEventPublisher eventPublisher;

    // ===== PUBLICATION D'ÉVÉNEMENTS =====
    // Appelé depuis n'importe quel service sans couplage direct

    public void publish(AuditEvent event) {
        eventPublisher.publishEvent(event);
    }

    // ===== LISTENER ASYNCHRONE =====
    // Se déclenche automatiquement quand un AuditEvent est publié
    // @Async = ne bloque pas le thread de la requête HTTP

    @Async
    @EventListener
    @Transactional
    public void onAuditEvent(AuditEvent event) {
        try {
            AuditLog log = AuditLog.builder()
                    .userId(event.getUserId())
                    .username(event.getUsername())
                    .action(event.getEventType().name())
                    .resource(event.getResource())
                    .ipAddress(event.getIpAddress())
                    .success(event.isSuccess())
                    .details(event.getDetails())
                    .build();

            auditLogRepository.save(log);

        } catch (Exception e) {
            // Ne jamais laisser l'audit planter la requête principale
            log.error("Failed to save audit log: {}", e.getMessage());
        }
    }

    // ===== CONSULTATION =====

    public Page<AuditLog> findAll(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    public Page<AuditLog> findByUser(String username, Pageable pageable) {
        return auditLogRepository.findByUsername(username, pageable);
    }

    public Page<AuditLog> findByDateRange(LocalDateTime from,
                                          LocalDateTime to,
                                          Pageable pageable) {
        return auditLogRepository.findByCreatedAtBetween(from, to, pageable);
    }

    public Page<AuditLog> findFailures(Pageable pageable) {
        return auditLogRepository.findBySuccess(false, pageable);
    }
} 
