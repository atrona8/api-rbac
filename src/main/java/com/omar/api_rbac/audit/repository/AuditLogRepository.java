package com.omar.api_rbac.audit.repository;

import com.omar.api_rbac.audit.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {

    Page<AuditLog> findByUserId(String userId, Pageable pageable);

    Page<AuditLog> findByUsername(String username, Pageable pageable);

    Page<AuditLog> findByCreatedAtBetween(LocalDateTime from,
                                          LocalDateTime to,
                                          Pageable pageable);

    Page<AuditLog> findBySuccess(boolean success, Pageable pageable);
} 
