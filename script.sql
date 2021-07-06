/*
    db: ressources_humaines
    user: haha
    pwd: 123456

    ALTER USER haha WITH SUPERUSER;


*/


DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
CREATE EXTENSION pgcrypto;

create type userType as  enum ('admin', 'user');
create table users (
    id serial primary key,
    name varchar(255) not null check (name <> ''),
    password char(60) not null check (password <>''),
    user_type userType not null,
    unique (name)
);

insert into users (name, password, user_type) values ('admin', crypt('FPO_12p)([]', gen_salt('bf')), 'admin');
insert into users (name, password, user_type) values ('user', crypt('FPO_12p)([]', gen_salt('bf')), 'user');

create table employee_categories (
    id serial primary key,
    name varchar(255) not null check (name <> ''),
    standard_hour_per_day int check (standard_hour_per_day > 0),
    day_week_start int not null check (day_week_start between 1 and 7),
    day_week_end int not null check (day_week_end between 1 and 7 and day_week_end > day_week_start),
    standard_salary double precision check (standard_salary > 0),
    indemnity_percent  double precision check (indemnity_percent between 0 and 1),
    unique (name)
);

create SEQUENCE registrationNumber increment by 1 minvalue 1 start 1;

create table employees (
    id serial primary key,
    first_name varchar(255) not null check (first_name <> ''),
    last_name varchar(255) not null check (last_name <> ''),
    id_category int not null,
    date_birth timestamp not null,
    date_begin_employment timestamp not null,
    date_end_employment timestamp,
    registration_number int not null check (registration_number > 0),
    foreign key (id_category) references employee_categories(id),
    unique (registration_number)
);

create type majorerType as enum ('nuit', 'ferier', 'dimanche');
create table majorer_config (
    id serial primary key,
    majorer_type majorerType not null,
    created_date timestamp not null default now(),
    code varchar(10) not null check (code <> ''),
    percentage double precision not null check (percentage between 0 and 1),
    unique (majorer_type),
    unique (code)
);

create type periodType as enum ('semaine');
-- CHECK SUM SUPPL_CONFIG == SUPPL CONFIG MAX
create table suppl_config (
    id serial primary key,
    code varchar(10) not null check (code <> ''),
    max_hour_per_period int not null check (max_hour_per_period >= 0),
    period_type periodType not null,
    created_date timestamp not null default now(),
    percentage double precision not null check (percentage between 0 and 1),
    unique (code)
);

create table suppl_config_max (
    id serial primary key,
    max_hour_supp int not null check (max_hour_supp > 0),
    created_date timestamp not null default now()
);

create table pointages (
    id serial primary key,
    id_employee int,
    id_semaine int not null check (id_semaine between 1 and 52),
    code varchar(255) not null check (code <> ''),
    hours double precision not null check ( hours >= 0),
    percentage double precision not null check (percentage between 0 and 1),
    foreign key (id_employee) references employees(id)
);

create table pointings_daily (
    id serial primary key,
    id_employee int,
    id_semaine int not null check (id_semaine between 1 and 52),
    weekOfDay int not null check (weekOfDay between 1 and 7),
    numberHoursDaily float not null check (numberHoursDaily >=0 ),
    numberHoursNightly float not null check (numberHoursNightly >=0 ),
    numberHoursFerier float not null check (numberHoursFerier >=0 ),
    foreign key (id_employee) references employees(id)
);

/* DATA */
insert into employee_categories (name, standard_hour_per_day, day_week_start, day_week_end, standard_salary, indemnity_percent) values ('normal', 6, 1, 7, 102500, 0.28);
insert into employee_categories (name, standard_hour_per_day, day_week_start, day_week_end, standard_salary, indemnity_percent) values ('gardien', 8, 1, 7, 105500, 0.28);
insert into employee_categories (name, standard_hour_per_day, day_week_start, day_week_end, standard_salary, indemnity_percent) values ('chauffeur', 8, 1, 6, 103200, 0.28);

-- insert into employees (last_name, first_name, id_category, date_birth, date_begin_employment, date_end_employment, registration_number) values ('rakoto', 'soa', 1, '1980-02-05', '2021-01-01', null,  nextval('registrationnumber'));
-- insert into employees (last_name, first_name, id_category, date_birth, date_begin_employment, date_end_employment, registration_number) values ('ramano', 'mickael', 1, '1985-02-05', '2021-02-01', null,  nextval('registrationnumber'));

-- insert into majorer_config (majorer_type, code, percentage) values ('nuit', 'hm130', 0.3);
-- insert into majorer_config (majorer_type, code, percentage) values ('dimanche', 'hm140', 0.4);
-- insert into majorer_config (majorer_type, code, percentage) values ('ferier', 'hm150', 0.5);

-- insert into suppl_config (code, max_hour_per_period, period_type, percentage) values ('hs30', 8,'semaine', 0.30);
-- insert into suppl_config (code, max_hour_per_period, period_type, percentage) values ('hs50', 12,'semaine', 0.5);

-- insert into suppl_config_max (max_hour_supp) values (20);

/* BASIC VIEWS */
create view all_employee_categories_with_weekly_hour as select *, ((day_week_end - day_week_start + 1) * standard_hour_per_day) as weekly_hour from employee_categories;

create view all_employees_with_categ as select employees.id as id_emp, employees.first_name, employees.last_name,employees.id_category, employees.date_birth, employees.date_begin_employment, employees.date_end_employment, employees.registration_number, all_employee_categories_with_weekly_hour.standard_hour_per_day, all_employee_categories_with_weekly_hour.day_week_start, all_employee_categories_with_weekly_hour.day_week_end, all_employee_categories_with_weekly_hour.standard_salary, all_employee_categories_with_weekly_hour.indemnity_percent, all_employee_categories_with_weekly_hour.weekly_hour,
all_employee_categories_with_weekly_hour.name from all_employee_categories_with_weekly_hour join employees on employees.id_category = all_employee_categories_with_weekly_hour.id order by employees.registration_number;

