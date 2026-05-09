INSERT INTO roles (id, name, description) VALUES
    ('1', 'ROLE_USER',  'Standard user'),
    ('2', 'ROLE_ADMIN', 'Administrator'),
    ('3', 'ROLE_MODERATOR', 'Moderator');

INSERT INTO permissions (id, name, description, resource, action) VALUES
    ('1', 'user:read',   'Read users',   'USER', 'READ'),
    ('2', 'user:create', 'Create users', 'USER', 'CREATE'),
    ('3', 'user:update', 'Update users', 'USER', 'UPDATE'),
    ('4', 'user:delete', 'Delete users', 'USER', 'DELETE'),
    ('5', 'role:read',   'Read roles',   'ROLE', 'READ'),
    ('6', 'role:manage', 'Manage roles', 'ROLE', 'MANAGE');

-- ADMIN a toutes les permissions
INSERT INTO role_permissions (role_id, permission_id) VALUES
    ('2', '1'), ('2', '2'), ('2', '3'),
    ('2', '4'), ('2', '5'), ('2', '6');

-- USER peut seulement lire
INSERT INTO role_permissions (role_id, permission_id) VALUES
    ('1', '1');