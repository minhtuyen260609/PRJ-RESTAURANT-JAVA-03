create table if not exists orders (
    id int primary key auto_increment,
    table_id int not null,
    user_id int not null,
    status varchar(30) not null default 'open'
);

create table if not exists order_details (
    id int primary key auto_increment,
    order_id int not null,
    menu_item_id int not null,
    quantity int not null,
    status varchar(30) not null default 'pending'
);

update tables
set status = 'empty'
where upper(status) = 'EMPTY';
