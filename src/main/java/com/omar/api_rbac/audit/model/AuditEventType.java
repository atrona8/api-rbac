 
package com.omar.api_rbac.audit.model;

public enum AuditEventType {
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    LOGOUT,
    REGISTER,
    ACCESS_GRANTED,
    ACCESS_DENIED,
    ROLE_ASSIGNED,
    ROLE_REVOKED,
    PERMISSION_ASSIGNED,
    PERMISSION_REVOKED,
    USER_ENABLED,
    USER_DISABLED
}