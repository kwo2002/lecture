create table lecture.enrolled_lecture_employee
(
    id                 bigint auto_increment
        primary key,
    lecture_id         bigint               not null,
    employee_id        bigint               not null,
    use_flag           tinyint(1) default 1 not null,
    registered_at      datetime             not null,
    registered_user_id bigint               not null,
    modified_at        datetime             null,
    modified_user_id   bigint               null,
    constraint lecture_applicant_employee_id_fk
        foreign key (employee_id) references lecture.employee (id),
    constraint lecture_applicant_lecture_id_fk
        foreign key (lecture_id) references lecture.lecture (id)
);

