-- Oracle
-- Create table
create table DBBUS_EVENT
(
  txn        NUMBER not null,
  table_name VARCHAR2(20) not null,
  id         VARCHAR2(20) not null,
  action     NUMBER(1) not null,
  status     NUMBER(1) default 0 not null,
  ts         NUMBER(14) default to_char(sysdate,'yyyymmddhh24miss') not null
)

-- Add comments to the columns
comment on column DBBUS_EVENT.action is '0-insert;1-update;2-delete;';
comment on column DBBUS_EVENT.status is '0-unread;1-readed;2-error;3-no consumer;';
-- Create/Recreate primary, unique and foreign key constraints
alter table DBBUS_EVENT add constraint PK_DBBUS_EVENT primary key (TXN);
create index idx_dbbus_event_status on DBBUS_EVENT (status);

-- Create sequence
create sequence SEQ_DBBUS_TXN minvalue 1 maxvalue 999999999999999 start with 1 increment by 1;

-- Create trigger for each table
CREATE OR REPLACE TRIGGER TRG_T_PERSON
  before insert or update or delete on T_PERSON
  referencing old as old new as new
  for each row
begin
  if (inserting) then
    INSERT INTO DBBUS_EVENT(txn, table_name, id, action ,status) VALUES(seq_dbbus_txn.nextval,'t_person',:new.id,0,0);
  elsif(updating) then
    INSERT INTO DBBUS_EVENT(txn, table_name, id, action ,status) VALUES(seq_dbbus_txn.nextval,'t_person',:old.id,1,0);
  else
    INSERT INTO DBBUS_EVENT(txn, table_name, id, action ,status) VALUES(seq_dbbus_txn.nextval,'t_person',:old.id,2,0);
  end if;
end;

