INSERT INTO db_almon.authorities (authority)
VALUES ('ACCESS_CORE_PAGES'),
       ('APPROVE_REGISTRATIONS'),
       ('VIEW_ADMIN_PAGES'),
       ('VIEW_ALL_USERS'),
       ('UPDATE_OTHER_USERS'),,
       ('DISABLE_ACCOUNTS'),
       ('ASSIGN_ROLES'),
;

INSERT INTO db_almon.roles (name)
VALUES ('USER'),
       ('ADMIN'),
;


INSERT INTO db_almon.api_keys(api_key, owner_id)
VALUES ('e5c9d432-d4e8-4859-9f4a-ef56f8a7854a','c5c86836-c38f-4f05-b9a7-164a96099413')
;



INSERT INTO db_almon.authorities_roles (authorities_authority, roles_name)
VALUES ('ACCESS_CORE_PAGES', 'USER');

INSERT INTO db_almon.authorities_roles (authorities_authority, roles_name)
VALUES ('VIEW_ADMIN_PAGES', 'ADMIN'),
       ('APPROVE_REGISTRATIONS', 'ADMIN'),
       ('VIEW_ALL_USERS', 'ADMIN'),
       ('UPDATE_OTHER_USERS', 'ADMIN'),
       ('DISABLE_ACCOUNTS', 'ADMIN'),
       ('ASSIGN_ROLES', 'ADMIN'),
;