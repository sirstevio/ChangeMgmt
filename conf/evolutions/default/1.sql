# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table change (
  id                        bigint not null,
  initiated                 timestamp,
  summary                   varchar(255),
  description               varchar(255),
  business_impact           varchar(255),
  acceptance_criteria       varchar(255),
  test_description          varchar(255),
  test_plan                 varchar(255),
  roll_back_plan            varchar(255),
  iao_approved              boolean,
  sys_own_approved          boolean,
  test_approved             boolean,
  inf_assur_approved        boolean,
  status                    integer,
  environment               integer,
  change_type               integer,
  system_id                 bigint,
  initiator_userid          varchar(255),
  builder_userid            varchar(255),
  constraint ck_change_status check (status in (0,1,2,3,4,5,6,7,8)),
  constraint ck_change_environment check (environment in (0,1,2)),
  constraint ck_change_change_type check (change_type in (0,1,2)),
  constraint pk_change primary key (id))
;

create table ictsystem (
  id                        bigint not null,
  name                      varchar(255),
  iao_userid                varchar(255),
  system_owner_userid       varchar(255),
  constraint pk_ictsystem primary key (id))
;

create table outage (
  id                        bigint not null,
  length                    integer,
  description               varchar(255),
  is_approved               boolean,
  change_id                 bigint,
  system_id                 bigint,
  constraint pk_outage primary key (id))
;

create table user (
  userid                    varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (userid))
;

create sequence change_seq;

create sequence ictsystem_seq;

create sequence outage_seq;

create sequence user_seq;

alter table change add constraint fk_change_system_1 foreign key (system_id) references ictsystem (id) on delete restrict on update restrict;
create index ix_change_system_1 on change (system_id);
alter table change add constraint fk_change_initiator_2 foreign key (initiator_userid) references user (userid) on delete restrict on update restrict;
create index ix_change_initiator_2 on change (initiator_userid);
alter table change add constraint fk_change_builder_3 foreign key (builder_userid) references user (userid) on delete restrict on update restrict;
create index ix_change_builder_3 on change (builder_userid);
alter table ictsystem add constraint fk_ictsystem_iao_4 foreign key (iao_userid) references user (userid) on delete restrict on update restrict;
create index ix_ictsystem_iao_4 on ictsystem (iao_userid);
alter table ictsystem add constraint fk_ictsystem_systemOwner_5 foreign key (system_owner_userid) references user (userid) on delete restrict on update restrict;
create index ix_ictsystem_systemOwner_5 on ictsystem (system_owner_userid);
alter table outage add constraint fk_outage_change_6 foreign key (change_id) references change (id) on delete restrict on update restrict;
create index ix_outage_change_6 on outage (change_id);
alter table outage add constraint fk_outage_system_7 foreign key (system_id) references ictsystem (id) on delete restrict on update restrict;
create index ix_outage_system_7 on outage (system_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists change;

drop table if exists ictsystem;

drop table if exists outage;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists change_seq;

drop sequence if exists ictsystem_seq;

drop sequence if exists outage_seq;

drop sequence if exists user_seq;

