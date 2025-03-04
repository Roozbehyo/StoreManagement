create table customers
(
    id          numeric primary key,
    firstname   nvarchar2(15),
    lastname    nvarchar2(30),
    birthday    date,
    national_id nvarchar2(10) unique,
    phone_num   nvarchar2(11) unique,
    sex         CHAR(1) CHECK (sex IN ('M', 'F'))
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
    id    numeric primary key,
    name  nvarchar2(20),
    price numeric default 0,
    stock numeric default 0
);
create sequence product_seq start with 1 increment by 1;

create table orders
(
    id          numeric primary key,
    customer_id numeric not null references customers (id),
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
    seller_id   numeric not null references sellers (id),
    product_id  numeric not null references products (id)
);
create sequence store_branch_seq start with 1 increment by 1;