
    drop table ACCOUNT if exists;

    drop table CATEGORY if exists;

    drop table CATEGORY_PRODUCT if exists;

    drop table COMMISSION if exists;

    drop table CONTACT if exists;

    drop table CUSTOMER if exists;

    drop table INVOICE if exists;

    drop table LINE_ITEM if exists;

    drop table PRODUCT if exists;

    drop table PURCHASE_ORDER if exists;

    drop table TEST if exists;

    drop table USER_PROFILE if exists;

    drop table WISH_LIST if exists;

    drop table WISH_LIST_PRODUCT if exists;

    drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 10000 increment by 10;

    create table ACCOUNT (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        ACCOUNT_NAME varchar(255) not null,
        ACTIVE boolean not null,
        BILLING_ADDRESS varchar(255) not null,
        CREDIT_CARD varchar(255),
        SHIPPING_ADDRESS varchar(255) not null,
        CONTACT_ID bigint,
        primary key (ID)
    );

    create table CATEGORY (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        NAME varchar(255),
        primary key (ID)
    );

    create table CATEGORY_PRODUCT (
       CATEGORY_ID bigint not null,
        PRODUCT_ID bigint not null,
        primary key (CATEGORY_ID, PRODUCT_ID)
    );

    create table COMMISSION (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        ORDER_IDENTIFIER varchar(255),
        RATE decimal(19,2),
        USER_EMAIL varchar(255),
        primary key (ID)
    );

    create table CONTACT (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        EMAIL varchar(80) not null,
        NAME varchar(80) not null,
        PHONE varchar(40) not null,
        primary key (ID)
    );

    create table CUSTOMER (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        CAN_CONTACT boolean,
        COMPANY varchar(255),
        DEPARTMENT varchar(255),
        FACEBOOK_IDENTIFIER varchar(255),
        FIRST_NAME varchar(255) not null,
        JOB_TITLE varchar(255),
        LAST_NAME varchar(255) not null,
        LINKEDIN_IDENTIFIER varchar(255),
        TWITTER_IDENTIFIER varchar(255),
        WORK_EMAIL varchar(255) not null,
        WORK_PHONE varchar(255) not null,
        ACCOUNT_ID bigint not null,
        primary key (ID)
    );

    create table INVOICE (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        DUE_DATE date,
        INVOICE_NBR varchar(255),
        STATUS varchar(255),
        ACCOUNT_ID bigint not null,
        primary key (ID)
    );

    create table LINE_ITEM (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        QUANTITY bigint not null,
        PRODUCT_ID bigint not null,
        PURCHASE_ORDER_ID bigint not null,
        primary key (ID)
    );

    create table PRODUCT (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        DESCRIPTION varchar(4096),
        NAME varchar(255),
        PRICE double,
        SKU varchar(255),
        UPC varchar(255),
        primary key (ID)
    );

    create table PURCHASE_ORDER (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        INVOICED boolean,
        ORDER_IDENTIFIER varchar(255) not null,
        PURCHASE_DTTM timestamp not null,
        STATUS varchar(255),
        ACCOUNT_ID bigint not null,
        primary key (ID)
    );

    create table TEST (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        VAR_BIG_DECIMAL DECIMAL(20,4),
        VAR_BIG_INTEGER decimal(19,2),
        VAR_BOOL boolean,
        VAR_BYTE tinyint,
        VAR_CH char(255),
        VAR_DATE date,
        VAR_DOUBLE double,
        VAR_DTTM timestamp,
        VAR_INT integer,
        VAR_LONG bigint,
        VAR_SHORT smallint,
        VAR_TIME time,
        primary key (ID)
    );

    create table USER_PROFILE (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        EMAIL varchar(255),
        FIRST_NAME varchar(255),
        LAST_NAME varchar(255),
        primary key (ID)
    );

    create table WISH_LIST (
       ID bigint not null,
        CREATED_BY varchar(255) not null,
        CREATED_DATE timestamp not null,
        MAINTAINED_BY varchar(255),
        MAINTAINED_DATE timestamp,
        VERSION bigint,
        NAME varchar(255),
        primary key (ID)
    );

    create table WISH_LIST_PRODUCT (
       WISH_LIST_ID bigint not null,
        PRODUCT_ID bigint not null
    );
create index IDX_ACCOUNT_NAME on ACCOUNT (ACCOUNT_NAME);

    alter table ACCOUNT 
       add constraint UQ_ACCOUNT_ACCOUNT_NAME unique (ACCOUNT_NAME);

    alter table CUSTOMER 
       add constraint UQ_CUSTOMER_ACCOUNT_ID unique (ACCOUNT_ID);
create index IDX_INVOICE_ACCOUNT_ID_FK on INVOICE (ACCOUNT_ID);
create index IDX_LINE_ITEM_PURCHASE_ORDER_ID_FK on LINE_ITEM (PURCHASE_ORDER_ID);
create index IDX_PURCHASE_ORDER_ACCOUNT_ID_FK on PURCHASE_ORDER (ACCOUNT_ID);

    alter table PURCHASE_ORDER 
       add constraint UQ_PURCHASE_ORDER_ORDER_IDENTIFIER unique (ORDER_IDENTIFIER);

    alter table ACCOUNT 
       add constraint FK_ACCOUNT_CONTACT 
       foreign key (CONTACT_ID) 
       references CONTACT;

    alter table CATEGORY_PRODUCT 
       add constraint FK_CATEGORY_PRODUCT 
       foreign key (PRODUCT_ID) 
       references PRODUCT;

    alter table CATEGORY_PRODUCT 
       add constraint FK_PRODUCT_CATEGORY 
       foreign key (CATEGORY_ID) 
       references CATEGORY;

    alter table CUSTOMER 
       add constraint FK_CUSTOMER_ACCOUNT 
       foreign key (ACCOUNT_ID) 
       references ACCOUNT;

    alter table INVOICE 
       add constraint FK_INVOICE_ACCOUNT 
       foreign key (ACCOUNT_ID) 
       references ACCOUNT;

    alter table LINE_ITEM 
       add constraint FK_LINE_ITEM_PRODUCT 
       foreign key (PRODUCT_ID) 
       references PRODUCT;

    alter table LINE_ITEM 
       add constraint FK_LINE_ITEM_PURCHASE_ORDER 
       foreign key (PURCHASE_ORDER_ID) 
       references PURCHASE_ORDER;

    alter table PURCHASE_ORDER 
       add constraint FK_PURCHASE_ORDER_ACCOUNT 
       foreign key (ACCOUNT_ID) 
       references ACCOUNT;

    alter table WISH_LIST_PRODUCT 
       add constraint FK_WISH_LIST_PRODUCT 
       foreign key (PRODUCT_ID) 
       references PRODUCT;

    alter table WISH_LIST_PRODUCT 
       add constraint FK_PRODUCT_WISH_LIST 
       foreign key (WISH_LIST_ID) 
       references WISH_LIST;
