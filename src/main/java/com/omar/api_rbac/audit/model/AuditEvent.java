package com.omar.api_rbac.audit.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AuditEvent extends ApplicationEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String userId;
    private final String username;
    private final AuditEventType eventType;
    private final String resource;
    private final String ipAddress;
    private final boolean success;
    private final String details;

    public AuditEvent(Object source,
                      String userId,
                      String username,
                      AuditEventType eventType,
                      String resource,
                      String ipAddress,
                      boolean success,
                      String details) {
        super(source);
        this.userId = userId;
        this.username = username;
        this.eventType = eventType;
        this.resource = resource;
        this.ipAddress = ipAddress;
        this.success = success;
        this.details = details;
    }

    // Le builder est statique pour faciliter la création
    public static AuditEvent of(Object source,
                                 String username,
                                 AuditEventType type,
                                 String resource,
                                 String ipAddress) {
        return new AuditEvent(source, null, username, type,
                              resource, ipAddress, true, null);
    }
} 
