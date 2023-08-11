drop table if exists boards_members cascade;

drop table if exists attachments cascade;

drop table if exists cards_members cascade;

drop table if exists columns_members cascade;

drop table if exists comments cascade;

drop table if exists favorites cascade;

drop table if exists items cascade;

drop table if exists check_lists cascade;

drop table if exists labels_cards cascade;

drop table if exists labels cascade;

drop table if exists notifications_members cascade;

drop table if exists notifications cascade;

drop table if exists estimations cascade;

drop table if exists cards cascade;

drop table if exists columns cascade;

drop table if exists boards cascade;

drop table if exists user_work_space_roles cascade;

drop table if exists users_work_spaces cascade;

drop table if exists users cascade;

drop table if exists work_spaces cascade;

create sequence if not exists public.attachment_seq;

alter sequence public.attachment_seq owner to postgres;

create sequence if not exists public.board_seq;

alter sequence public.board_seq owner to postgres;

create sequence if not exists public.card_seq;

alter sequence public.card_seq owner to postgres;

create sequence if not exists public.check_list_seq;

alter sequence public.check_list_seq owner to postgres;

create sequence if not exists public.column_seq;

alter sequence public.column_seq owner to postgres;

create sequence if not exists public.comment_seq;

alter sequence public.comment_seq owner to postgres;

create sequence if not exists public.estimation_seq;

alter sequence public.estimation_seq owner to postgres;

create sequence if not exists public.favorite_seq;

alter sequence public.favorite_seq owner to postgres;

create sequence if not exists public.items_seq;

alter sequence public.items_seq owner to postgres;

create sequence if not exists public.label_seq;

alter sequence public.label_seq owner to postgres;

create sequence if not exists public.notification_seq;

alter sequence public.notification_seq owner to postgres;

create sequence if not exists public.users_seq;

alter sequence public.users_seq owner to postgres;

create sequence if not exists public.user_work_space_role_seq;

alter sequence public.user_work_space_role_seq owner to postgres;

create sequence if not exists public.work_space_seq;

alter sequence public.work_space_seq owner to postgres;


create table public.labels
(
    id         bigint not null
        primary key,
    color      varchar(255),
    label_name varchar(255)
);

alter table public.labels
    owner to postgres;

create table public.users
(
    id         bigint not null
        primary key,
    email      varchar(255),
    first_name varchar(255),
    image      varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    role       varchar(255)
        constraint users_role_check
            check ((role)::text = ANY ((ARRAY ['ADMIN'::character varying, 'MEMBER'::character varying])::text[]))
);

alter table public.users
    owner to postgres;

create table public.work_spaces
(
    admin_id     bigint,
    created_date timestamp(6) with time zone,
    id           bigint not null
        primary key,
    name         varchar(255)
);

alter table public.work_spaces
    owner to postgres;

create table public.boards
(
    id            bigint not null
        primary key,
    work_space_id bigint
        constraint fkrxhmv601gnnbh6dvivm1m41vg
            references public.work_spaces,
    back_ground   varchar(255),
    title         varchar(255)
);

alter table public.boards
    owner to postgres;

create table public.boards_members
(
    boards_id  bigint not null
        constraint fkq9up0wa41qtcle4h76b3xomam
            references public.boards,
    members_id bigint not null
        constraint fkqapk2hu8wqyos7uwf37vq3ccl
            references public.users
);

alter table public.boards_members
    owner to postgres;

create table public.columns
(
    is_archive boolean,
    board_id   bigint
        constraint fkiylg7oiwdt1tnoff75rkbihc0
            references public.boards,
    id         bigint not null
        primary key,
    title      varchar(255)
);

alter table public.columns
    owner to postgres;

create table public.cards
(
    is_archive   boolean,
    column_id    bigint
        constraint fklpqsumqub2x95veana5uak4gc
            references public.columns,
    created_date timestamp(6) with time zone,
    creator_id   bigint,
    id           bigint not null
        primary key,
    description  varchar(255),
    title        varchar(255)
);

alter table public.cards
    owner to postgres;

create table public.attachments
(
    card_id       bigint
        constraint fk8a70ieewfki0nbv4mjs3mof23
            references public.cards,
    created_at    timestamp(6) with time zone,
    id            bigint not null
        primary key,
    document_link varchar(255)
);

alter table public.attachments
    owner to postgres;

create table public.cards_members
(
    cards_id   bigint not null
        constraint fkcsxbshdyr1jf2em8ofn180gxv
            references public.cards,
    members_id bigint not null
        constraint fk89fiogrruk6qv7xchl9hoc6ke
            references public.users
);

alter table public.cards_members
    owner to postgres;

create table public.check_lists
(
    percent     integer not null,
    card_id     bigint
        constraint fkc7p77egiple50a3kke4v31iw0
            references public.cards,
    id          bigint  not null
        primary key,
    description varchar(255)
);

alter table public.check_lists
    owner to postgres;

create table public.columns_members
(
    columns_id bigint not null
        constraint fkbkh4a31w3q1eoc32trny8sbaf
            references public.columns,
    members_id bigint not null
        constraint fkrmiks421c23mwxx9l0nutvhpq
            references public.users
);

alter table public.columns_members
    owner to postgres;

create table public.comments
(
    card_id      bigint
        constraint fkq1d8ryms7bmgcdllfk7k50oe4
            references public.cards,
    created_date timestamp(6) with time zone,
    id           bigint not null
        primary key,
    member_id    bigint
        constraint fkhfwyjg46gq7vu9bptl65kd8fx
            references public.users,
    comment      varchar(255)
);

alter table public.comments
    owner to postgres;

create table public.estimations
(
    card_id         bigint
        unique
        constraint fkdvsissxwr5s1h3mryf46ikj72
            references public.cards,
    due_date        timestamp(6) with time zone,
    id              bigint not null
        primary key,
    notification_id bigint
        unique,
    start_date      timestamp(6) with time zone,
    time            timestamp(6) with time zone,
    reminder_type   varchar(255)
        constraint estimations_reminder_type_check
            check ((reminder_type)::text = ANY
                   ((ARRAY ['NONE'::character varying, 'FIVE_MINUTE'::character varying, 'TEN_MINUTE'::character varying, 'FIFTEEN_MINUTE'::character varying, 'THIRD_MINUTE'::character varying])::text[]))
);

alter table public.estimations
    owner to postgres;

create table public.favorites
(
    board_id      bigint
        constraint fkgpl0y89n3t9gkcmsro4n7eqm9
            references public.boards,
    id            bigint not null
        primary key,
    member_id     bigint
        constraint fkjh7l6s5jn75au12vc0mwn15jd
            references public.users,
    work_space_id bigint
        constraint fktehk18m97qq0ybpmx58k01kel
            references public.work_spaces
);

alter table public.favorites
    owner to postgres;

create table public.items
(
    is_done       boolean,
    check_list_id bigint
        constraint fkrlrys3jruy6x1ecouivn4idq7
            references public.check_lists,
    id            bigint not null
        primary key,
    title         varchar(255)
);

alter table public.items
    owner to postgres;

create table public.labels_cards
(
    cards_id  bigint not null
        constraint fk2x08s9bbm0p87exuu03cgnhvd
            references public.cards,
    labels_id bigint not null
        constraint fkr88x216bh9uhmauvmloyufsfw
            references public.labels
);

alter table public.labels_cards
    owner to postgres;

create table public.notifications
(
    is_read       boolean,
    card_id       bigint
        constraint fkaglsotkhmudvh71vtprbcusyf
            references public.cards,
    created_date  timestamp(6) with time zone,
    estimation_id bigint
        unique
        constraint fk25bftgxb70j9l9ql42q8vd1cs
            references public.estimations,
    id            bigint not null
        primary key,
    image         varchar(255),
    text          varchar(255),
    type          varchar(255)
        constraint notifications_type_check
            check ((type)::text = ANY
                   ((ARRAY ['REMINDER'::character varying, 'MOVE'::character varying, 'ASSIGN'::character varying])::text[]))
);

alter table public.notifications
    owner to postgres;

alter table public.estimations
    add constraint fkfo3raelhtwxmue4kbu2sfgnea
        foreign key (notification_id) references public.notifications;

create table public.notifications_members
(
    members_id       bigint not null
        constraint fko63r12e2lo568gl49ggi5xu22
            references public.users,
    notifications_id bigint not null
        constraint fkol3lroyla1310v2dfysdj8tb6
            references public.notifications
);

alter table public.notifications_members
    owner to postgres;

create table public.user_work_space_roles
(
    id            bigint not null
        primary key,
    member_id     bigint
        constraint fkm2wdpgh4udd9w9yatoqo14a3l
            references public.users,
    work_space_id bigint
        constraint fk1mrkt7ah82tsu96188f5mhlcq
            references public.work_spaces,
    role          varchar(255)
        constraint user_work_space_roles_role_check
            check ((role)::text = ANY ((ARRAY ['ADMIN'::character varying, 'MEMBER'::character varying])::text[]))
);

alter table public.user_work_space_roles
    owner to postgres;

create table public.users_work_spaces
(
    members_id     bigint not null
        constraint fkljecswpj6g81tc6ntuaabwfis
            references public.users,
    work_spaces_id bigint not null
        constraint fkdlmo61xy43a4kx8g4pajivb39
            references public.work_spaces
);

alter table public.users_work_spaces
    owner to postgres;

