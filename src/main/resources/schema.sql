drop table if exists book;
drop table if exists employee;
drop table if exists department;
drop
sequence if exists department_seq;
drop
sequence if exists employee_seq;

CREATE TABLE book
(
    id              serial primary key,
    title           varchar not null,
    author          varchar not null,
    publicationYear varchar not null
);

create
sequence department_seq start
2 increment by 1;
create
sequence employee_seq start
2 increment by 1;

create table department
(
    id   bigint not null,
    name varchar(255),
    primary key (id)
);

create table employee
(
    salary        float(53) not null,
    department_id bigint,
    id            bigint    not null,
    first_name    varchar(255),
    last_name     varchar(255),
    position      varchar(255),
    primary key (id)
);

alter table if exists employee add constraint FKbejtwvg9bxus2mffsm3swj3u9 foreign key (department_id) references department;




