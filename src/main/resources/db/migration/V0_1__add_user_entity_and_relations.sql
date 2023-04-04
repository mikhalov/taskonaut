CREATE TYPE user_role AS ENUM('ADMIN', 'USER');

CREATE TABLE users
(
    id       VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     user_role NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE label
    ADD user_id VARCHAR(255);

ALTER TABLE note
    ADD user_id VARCHAR(255);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE label
    ADD CONSTRAINT FK_LABEL_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE note
    ADD CONSTRAINT FK_NOTE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);