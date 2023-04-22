-- Create labels before
-- Need to change user id
-- Uncomment and execute if produce error
-- DELETE FROM pg_extension WHERE extname = 'uuid-ossp';
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

WITH random_title AS (
    SELECT CONCAT('Note Title ', FLOOR(RANDOM() * 100000)::integer) AS title, ROW_NUMBER() OVER () AS rn
    FROM generate_series(1, 10)
),
     random_content AS (
         SELECT (
                    SELECT STRING_AGG(substr('Lorem ipsum dolor sit amet, consectetur adipiscing elit. ', 1, FLOOR(RANDOM() * (40 - 5) + 5)::integer) || ' ', '' ORDER BY RANDOM())
                    FROM generate_series(1, r)
                ) AS content, ROW_NUMBER() OVER () AS rn
         FROM (
                  SELECT FLOOR(RANDOM() * (10 - 2) + 2)::integer AS r
                  FROM generate_series(1, 10)
              ) AS r
     )
INSERT INTO note (note_id, content, title, label_id, user_id, last_modified_date)
SELECT uuid_generate_v4(), rc.content, rt.title, lb.label_id, '057736c9-f836-40fc-9079-3bbe21421dba' AS user_id, CURRENT_TIMESTAMP AS last_modified_date
FROM random_title AS rt
         CROSS JOIN label AS lb
         INNER JOIN (
    SELECT rn, content FROM random_content
    WHERE LENGTH(content) BETWEEN 60 AND 800
    GROUP BY rn, content
) AS rc ON rc.rn = rt.rn
WHERE lb.user_id = '057736c9-f836-40fc-9079-3bbe21421dba';