INSERT INTO db_almon.authorities (authority)
VALUES ('ACCESS_CORE_PAGES'),
       ('ENABLE_DISABLE_ACCOUNTS'),
       ('ENABLE_DISABLE_SERVICES'),
       ('VIEW_ADMIN_PAGES'),
       ('VIEW_ALL_USERS'),
       ('UPDATE_OTHER_USERS'),
       ('ASSIGN_ROLES'),
       ('ASSIGN_PERMISSIONS'),
       ('CREATE_SERVICE'),
       ('CREATE_ALERTTYPES'),
       ('VIEW_ALL_SERVICES'),
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
       ('ENABLE_DISABLE_ACCOUNTS', 'ADMIN'),
       ('VIEW_ALL_USERS', 'ADMIN'),
       ('UPDATE_OTHER_USERS', 'ADMIN'),
       ('ASSIGN_ROLES', 'ADMIN'),
       ('CREATE_SERVICE', 'ADMIN'),
       ('VIEW_ALL_SERVICES', 'ADMIN'),
       ('ASSIGN_PERMISSIONS','ADMIN'),
       ('ENABLE_DISABLE_SERVICES','ADMIN'),
       ('CREATE_ALERTTYPES','ADMIN'),
;