DROP TABLE IF EXISTS PERSON;
DROP TABLE IF EXISTS DEPARTMENT;

create table DEPARTMENT
(
    id   INTEGER not null ,
    name VARCHAR(32),
    primary key (id)
)