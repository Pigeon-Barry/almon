INSERT INTO db_almon.authorities (authority)
VALUES ('ACCESS_CORE_PAGES'),
       ('APPROVE_REGISTRATIONS'),
       ('ASSIGN_PERMISSIONS');

INSERT INTO db_almon.roles (role_name)
VALUES ('USER'),
       ('ADMIN');

INSERT INTO db_almon.authorities_roles (authorities_authority, roles_role_name)
VALUES ('ACCESS_CORE_PAGES', 'USER');

INSERT INTO db_almon.authorities_roles (authorities_authority, roles_role_name)
VALUES ('ASSIGN_PERMISSIONS', 'ADMIN'),
       ('APPROVE_REGISTRATIONS', 'ADMIN');