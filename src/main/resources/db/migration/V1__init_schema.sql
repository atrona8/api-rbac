-- Users
CREATE TABLE users (
    id          VARCHAR(36) PRIMARY KEY,
    username    VARCHAR(50)  UNIQUE NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    enabled     BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL
);

-- Roles
CREATE TABLE roles (
    id             VARCHAR(36) PRIMARY KEY,
    name           VARCHAR(50) UNIQUE NOT NULL,
    description    VARCHAR(200),
    parent_role_id VARCHAR(36) REFERENCES roles(id)
);

-- Permissions
CREATE TABLE permissions (
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(200),
    resource    VARCHAR(50) NOT NULL,
    action      VARCHAR(50) NOT NULL
);

-- Relations
CREATE TABLE user_roles (
    user_id VARCHAR(36) REFERENCES users(id) ON DELETE CASCADE,
    role_id VARCHAR(36) REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE role_permissions (
    role_id       VARCHAR(36) REFERENCES roles(id) ON DELETE CASCADE,
    permission_id VARCHAR(36) REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- Audit
CREATE TABLE audit_logs (
    id         VARCHAR(36) PRIMARY KEY,
    user_id    VARCHAR(36),
    username   VARCHAR(50),
    action     VARCHAR(100) NOT NULL,
    resource   VARCHAR(200),
    ip_address VARCHAR(45),
    success    BOOLEAN,
    details    VARCHAR(500),
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_audit_user    ON audit_logs(user_id);
CREATE INDEX idx_audit_created ON audit_logs(created_at);