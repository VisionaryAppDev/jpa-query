drop table person;
select * from person;
CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    age INTEGER,
    gender VARCHAR(10),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);


INSERT INTO person (first_name, last_name, email, age)
SELECT
    md5(random()::text || clock_timestamp()::text)::uuid,
    md5(random()::text || clock_timestamp()::text)::uuid,
    concat('person', i, '@example.com'),
    floor(random()*100)
FROM generate_series(1,100) s(i);

