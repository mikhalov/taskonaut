CREATE TABLE note (
    id VARCHAR(255) NOT NULL,
    content OID,
    creation_date TIMESTAMP,
    last_modified_date TIMESTAMP,
    title VARCHAR(255),
    notebook_id VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE notebook (
    id VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

ALTER TABLE note
    ADD CONSTRAINT FKa5rc3ktp8e452prs5e0ylluwj
    FOREIGN KEY (notebook_id)
    REFERENCES notebook (id);