INSERT INTO db_almon.user(id, email, enabled, first_name, last_name, password, phone_number,
                          approved_by_id)
VALUES ('093d8814-4960-4da7-8337-fc89b8da69b1', 'ben.edwards2000@live.co.uk', b'1', 'Ben',
        'Edwards', '$2a$10$dvfWel1bC4L5r4Y5G8G32.hHF6AvyxKzSOSzB/gVorrYGHs9RhGPu', NULL,
        '093d8814-4960-4da7-8337-fc89b8da69b1'),
       ('881a85a9-47af-404e-b399-70b3f33f8db1', 'ben.edwards2000@live.co.uk2', b'1', 'Ben',
        'Edwards', '$2a$10$cgA1fxs/5RabyEm/jHVbmOF1SO5sNlRWS07vMaDeFfi8zAqx5u9b2', NULL,
        '093d8814-4960-4da7-8337-fc89b8da69b1');



INSERT INTO db_almon.role(name)
VALUES ('USER'),
       ('ADMIN')
;

INSERT INTO db_almon.user_roles(users_id, roles_name)
VALUES ('093d8814-4960-4da7-8337-fc89b8da69b1', 'USER'),
       ('093d8814-4960-4da7-8337-fc89b8da69b1', 'ADMIN'),
       ('881a85a9-47af-404e-b399-70b3f33f8db1', 'USER')
;

INSERT INTO db_almon.authority (authority)
VALUES ('ACCESS_CORE_PAGES'),
       ('ENABLE_DISABLE_ACCOUNTS'),
       ('ENABLE_DISABLE_SERVICES'),
       ('VIEW_ADMIN_PAGES'),
       ('VIEW_ALL_USERS'),
       ('DELETE_USERS'),
       ('UPDATE_USERS'),
       ('UPDATE_OTHER_USERS'),
       ('ASSIGN_ROLES'),
       ('ASSIGN_PERMISSIONS'),
       ('CREATE_SERVICE'),
       ('UPDATE_SERVICE'),
       ('CREATE_MONITORS'),
       ('UPDATE_MONITORS'),
       ('VIEW_ALL_MONITORS'),
       ('VIEW_ALL_SERVICES'),
       ('DELETE_SERVICES'),
       ('RUN_MONITORS'),
       ('DELETE_MONITORS'),
       ('ENABLE_DISABLE_MONITORS')
;


# INSERT INTO db_almon.apikey(api_key, owner_id)
# VALUES ('e5c9d432-d4e8-4859-9f4a-ef56f8a7854a','c5c86836-c38f-4f05-b9a7-164a96099413')
# ;
#


INSERT INTO db_almon.authority_roles (authorities_authority, roles_name)
VALUES ('ACCESS_CORE_PAGES', 'USER');

INSERT INTO db_almon.authority_roles (authorities_authority, roles_name)
VALUES ('VIEW_ADMIN_PAGES', 'ADMIN'),
       ('ENABLE_DISABLE_ACCOUNTS', 'ADMIN'),
       ('VIEW_ALL_USERS', 'ADMIN'),
       ('DELETE_USERS', 'ADMIN'),
       ('UPDATE_USERS', 'ADMIN'),
       ('UPDATE_OTHER_USERS', 'ADMIN'),
       ('ASSIGN_ROLES', 'ADMIN'),
       ('CREATE_SERVICE', 'ADMIN'),
       ('VIEW_ALL_SERVICES', 'ADMIN'),
       ('ASSIGN_PERMISSIONS', 'ADMIN'),
       ('ENABLE_DISABLE_SERVICES', 'ADMIN'),
       ('CREATE_MONITORS', 'ADMIN'),
       ('UPDATE_MONITORS', 'ADMIN'),
       ('DELETE_MONITORS', 'ADMIN'),
       ('RUN_MONITORS', 'ADMIN'),
       ('DELETE_SERVICES', 'ADMIN'),
       ('ENABLE_DISABLE_MONITORS', 'ADMIN'),
       ('UPDATE_SERVICE', 'ADMIN')
;

