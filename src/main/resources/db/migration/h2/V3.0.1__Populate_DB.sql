INSERT INTO roles VALUES
('564716bf-315e-4dd4-85dc-56e1d682b2e1','user'),
('683b9562-dc45-4e73-9ac5-ddb5cfd9c4d6','admin');

INSERT INTO users VALUES
('5f9f027c-68c3-49cc-b2ca-a8b0ecc3646a','admin','{bcrypt}$2a$10$9JJNeyuLyhFFwVkkHXxfZ.WMU2kzadeWJGBG8PnG1gCA1UgQ22vMO');

INSERT INTO user_role VALUES
('5f9f027c-68c3-49cc-b2ca-a8b0ecc3646a','683b9562-dc45-4e73-9ac5-ddb5cfd9c4d6');