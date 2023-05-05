create table lecture.employee
(
    id                 bigint auto_increment
        primary key,
    employee_no        varchar(5) not null,
    use_flag           tinyint(1) not null,
    registered_at      datetime   not null,
    registered_user_id bigint     not null,
    modified_at        datetime   null,
    modified_user_id   bigint     null,
    constraint employee_no
        unique (employee_no)
);

create index employees_employee_no_index
    on lecture.employee (employee_no);

