create table account
(
    id             int auto_increment
        primary key,
    city           varchar(255) null,
    country        varchar(255) null,
    employee_count int          not null,
    industry       varchar(255) null
);

create table contact
(
    id           int auto_increment
        primary key,
    company_name varchar(255) null,
    email        varchar(255) null,
    name         varchar(255) null,
    phone_number varchar(255) null,
    account_id   int          null,
    constraint FK3ctagodg5h629t8ltnam39l5w
        foreign key (account_id) references account (id)
);

create table opportunity
(
    id           int auto_increment
        primary key,
    product      varchar(255) null,
    quantity     int          not null,
    sales_rep_id int          null,
    status       varchar(255) null,
    account_id   int          null,
    contact_id   int          null,
    constraint FK9mvhqgny93la8u8k2gd531nex
        foreign key (account_id) references account (id),
    constraint FKo5iiy8cn6h1f2kae3s49yteot
        foreign key (contact_id) references contact (id)
);
