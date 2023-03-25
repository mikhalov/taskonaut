CREATE TABLE note (
    note_id VARCHAR(255) NOT NULL,
    content TEXT,
    creation_date TIMESTAMP(0) DEFAULT NOW(),
    last_modified_date TIMESTAMP(0),
    title VARCHAR(255),
    notebook_id VARCHAR(255),
    PRIMARY KEY (note_id)
);

CREATE TABLE notebook (
    notebook_id VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY (notebook_id)
);

ALTER TABLE note
    ADD CONSTRAINT FKa5rc3ktp8e452prs5e0ylluwj
    FOREIGN KEY (notebook_id)
    REFERENCES notebook (notebook_id);