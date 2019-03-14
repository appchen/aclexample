-- we don't know how to generate schema base (class Schema) :(
create table acl_class
(
	id bigint auto_increment
		primary key,
	class varchar(100) not null,
	constraint unique_uk_2
		unique (class)
)
;

create table acl_entry
(
	id bigint auto_increment
		primary key,
	acl_object_identity bigint not null,
	ace_order int not null,
	sid bigint not null,
	mask int not null,
	granting int not null,
	audit_success int not null,
	audit_failure int not null,
	constraint unique_uk_4
		unique (acl_object_identity, ace_order)
)
;

create index foreign_fk_5
	on acl_entry (sid)
;

create table acl_object_identity
(
	id bigint auto_increment
		primary key,
	object_id_class bigint not null,
	object_id_identity bigint not null comment '对应object_id_class类型的对象主键',
	parent_object bigint null,
	owner_sid bigint null,
	entries_inheriting tinyint(1) not null,
	constraint unique_uk_3
		unique (object_id_class, object_id_identity)
)
;

create index foreign_fk_1
	on acl_object_identity (parent_object)
;

create index foreign_fk_3
	on acl_object_identity (owner_sid)
;

create table acl_sid
(
	id bigint auto_increment
		primary key,
	principal int not null,
	sid varchar(100) not null,
	constraint unique_uk_1
		unique (sid, principal)
)
;

create table book
(
	id bigint auto_increment
		primary key,
	author varchar(255) null,
	book_name varchar(255) null,
	description varchar(255) null
)
engine=MyISAM
;

create table revchanges
(
	id int auto_increment
		primary key,
	timestamp bigint not null,
	rev int not null,
	entityname varchar(255) null
)
;

create table tb_resource
(
	id int auto_increment
		primary key,
	create_time datetime null,
	description varchar(255) null,
	icon varchar(255) null,
	is_hide int null,
	level int null,
	name varchar(255) null,
	sort int null,
	source_key varchar(255) null,
	source_url varchar(255) null,
	type int null,
	update_time datetime null,
	parent_id int null,
	constraint FKf5ra2gn0xedeida2op8097sr5
		foreign key (parent_id) references tb_resource (id)
)
;

create index FKf5ra2gn0xedeida2op8097sr5
	on tb_resource (parent_id)
;

create table tb_role
(
	id int auto_increment
		primary key,
	create_time datetime null,
	description varchar(255) null,
	name varchar(255) null,
	role_key varchar(255) null,
	status int null,
	update_time datetime null,
	authority varchar(255) not null
)
;

create table tb_role_resource
(
	role_id int not null,
	resource_id int not null,
	primary key (role_id, resource_id),
	constraint FK7ffc7h6obqxflhj1aq1mk20jk
		foreign key (role_id) references tb_role (id),
	constraint FK868kc8iic48ilv5npa80ut6qo
		foreign key (resource_id) references tb_resource (id)
)
;

create index FK868kc8iic48ilv5npa80ut6qo
	on tb_role_resource (resource_id)
;

create table tb_user
(
	id int auto_increment
		primary key,
	address varchar(255) null,
	birthday datetime null,
	create_time datetime null,
	delete_status int null,
	description varchar(255) null,
	email varchar(255) null,
	locked int null,
	nick_name varchar(255) null,
	password varchar(255) null,
	sex int null,
	telephone varchar(255) null,
	update_time datetime null,
	user_name varchar(255) null,
	first_name varchar(255) null,
	last_name varchar(255) null,
	mobile varchar(255) null,
	salutation varchar(255) null,
	enabled bit not null,
	username varchar(255) not null,
	constraint UK_4wv83hfajry5tdoamn8wsqa6x
		unique (username)
)
;

create table tb_user_role
(
	user_id int not null,
	role_id int not null,
	primary key (user_id, role_id),
	constraint FK7vn3h53d0tqdimm8cp45gc0kl
		foreign key (user_id) references tb_user (id),
	constraint FKea2ootw6b6bb0xt3ptl28bymv
		foreign key (role_id) references tb_role (id)
)
;

create index FKea2ootw6b6bb0xt3ptl28bymv
	on tb_user_role (role_id)
;

