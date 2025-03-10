create table customers
(
    id          numeric primary key,
    firstname   nvarchar2(15),
    lastname    nvarchar2(30),
    birthdate   date,
    national_id nvarchar2(10),
    phone_num   nvarchar2(11),
    sex         nvarchar2(6) check (sex in ('MALE', 'FEMALE')),
    is_deleted  numeric(1) default 0
);
create sequence customer_seq start with 1 increment by 1;

create table sellers
(
    id        numeric primary key,
    firstname nvarchar2(15),
    lastname  nvarchar2(30),
    username  nvarchar2(15) unique,
    password  nvarchar2(15)
);
create sequence seller_seq start with 1 increment by 1;

create table products
(
    id         numeric primary key,
    name       nvarchar2(20),
    price      numeric    default 0,
    is_deleted numeric(1) default 0
);
create sequence product_seq start with 1 increment by 1;

create table orders
(
    id          numeric primary key,
    customer_id numeric not null references customers (id),
    seller_id   numeric not null references sellers (id),
    branch_id   numeric not null references store_branch(id),
    order_date  date
);
create sequence order_seq start with 1 increment by 1;

create table order_items
(
    id         numeric primary key,
    order_id   numeric not null references orders (id),
    product_id numeric not null references products (id),
    quantity   numeric default 0
);
create sequence order_item_seq start with 1 increment by 1;

create table store_branch
(
    id          numeric primary key,
    branch_name nvarchar2(30),
    is_deleted  numeric default 0
);
create sequence store_branch_seq start with 1 increment by 1;

create table branch_seller
(
    id          numeric primary key,
    branch_id   numeric not null references store_branch (id),
    seller_id   numeric not null references sellers (id)
);
create sequence branch_seller_seq start with 1 increment by 1;


create table inventory
(
    id         numeric primary key,
    product_id numeric not null references products (id),
    quantity   numeric default 0,
    branch_id  numeric not null references store_branch (id)
);
create sequence inventory_seq start with 1 increment by 1;