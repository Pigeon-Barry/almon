INSERT INTO db_almon.authorities (authority)
VALUES ('ACCESS_CORE_PAGES'),
       ('APPROVE_REGISTRATIONS'),
       ('VIEW_ADMIN_PAGES'),
       ('VIEW_ALL_USERS'),
       ('UPDATE_OTHER_USERS'),
;

INSERT INTO db_almon.roles (role_name)
VALUES ('USER'),
       ('ADMIN'),
;

INSERT INTO db_almon.authorities_roles (authorities_authority, roles_role_name)
VALUES ('ACCESS_CORE_PAGES', 'USER');

INSERT INTO db_almon.authorities_roles (authorities_authority, roles_role_name)
VALUES ('VIEW_ADMIN_PAGES', 'ADMIN'),
       ('ASSIGN_PERMISSIONS', 'ADMIN'),
       ('APPROVE_REGISTRATIONS', 'ADMIN'),
       ('VIEW_ALL_USERS', 'ADMIN'),
       ('UPDATE_OTHER_USERS', 'ADMIN'),
;