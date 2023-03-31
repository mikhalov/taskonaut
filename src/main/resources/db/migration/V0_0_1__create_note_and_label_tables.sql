CREATE TABLE note (
    note_id VARCHAR(255) NOT NULL,
    content TEXT,
    creation_date TIMESTAMP(0) DEFAULT NOW(),
    last_modified_date TIMESTAMP(0),
    title VARCHAR(100),
    label_id VARCHAR(255),
    PRIMARY KEY (note_id)
);

CREATE TABLE label (
    label_id VARCHAR(255) NOT NULL,
    name VARCHAR(50) UNIQUE NOT NULL,
    PRIMARY KEY (label_id)
);

ALTER TABLE note
    ADD CONSTRAINT note_label_id_constraint
    FOREIGN KEY (label_id)
    REFERENCES label (label_id);


