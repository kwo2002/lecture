create table lecture.lecture
(
    id                 bigint auto_increment
        primary key,
    teacher            varchar(30)      not null,
    room               varchar(50)      not null,
    max_enrollment     int    default 0 not null,
    current_enrollment int    default 0 not null,
    lecture_at         date             not null,
    lecture_desc       longtext         null,
    use_flag           tinyint(1)       not null,
    registered_at      datetime         not null,
    registered_user_id bigint           not null,
    modified_at        datetime         null,
    modified_user_id   bigint           null,
    version            bigint default 0 not null
);

