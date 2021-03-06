
# An database data change listener. like [linkin-databus](https://github.com/linkedin/databus/)

# Build System
```
cd dbbus
gradle myTar
```

***

# Install Maven
```
cd dbbus
gradle uploadArchives
```

***

# Add Triggers Sequence Event-Table in database.

## oracle

```
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


-- Create sequence
create sequence SEQ_DBBUS_TXN minvalue 1 maxvalue 999999999999999 start with 1 increment by 1;

```

### for each table must add one trigger
```
-- Create　Trigger
CREATE OR REPLACE TRIGGER TRG_T_PERSON
  before insert or update or delete on T_PERSON
  referencing old as old new as new
  for each row
begin
  if (inserting) then
    INSERT INTO DBBUS_EVENT(txn, table_name, id, action, status) VALUES(seq_dbbus_txn.nextval, 't_person', :new.id, 0, 0);
  elsif(updating) then
    INSERT INTO DBBUS_EVENT(txn, table_name, id, action, status) VALUES(seq_dbbus_txn.nextval, 't_person', :old.id, 1, 0);
  else
    INSERT INTO DBBUS_EVENT(txn, table_name, id, action, status) VALUES(seq_dbbus_txn.nextval, 't_person', :old.id, 2, 0);
  end if;
end;
```

## mysql
```
CREATE TABLE `DBBUS_EVENT`(
  `txn` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `table_name` VARCHAR(20) NOT NULL,
  `id` VARCHAR(20) NOT NULL,
  `action` INT(1) NOT NULL COMMENT '0-insert;1-update;2-delete;',
  `status` INT(1) NOT NULL DEFAULT 0 COMMENT '0-unread;1-readed;2-error;3-no consumer;',
  `ts` BIGINT(14) NOT NULL ,
  PRIMARY KEY (`txn`)
);
```

### for each table must add three triggers
```
delimiter $

CREATE TRIGGER TRG_T_PERSON_INSERT
AFTER INSERT ON T_PERSON
FOR EACH ROW
BEGIN
  INSERT INTO DBBUS_EVENT(table_name, id, ACTION, ts) VALUES('t_person',new.id, 0, DATE_FORMAT(NOW(),'%Y%m%d%H%i%s'));
END$

CREATE TRIGGER TRG_T_PERSON_UPDATE
AFTER update ON T_PERSON
FOR EACH ROW
BEGIN
  INSERT INTO DBBUS_EVENT(table_name, id, ACTION, ts) VALUES('t_person',old.id, 1, DATE_FORMAT(NOW(),'%Y%m%d%H%i%s'));
END$

CREATE TRIGGER TRG_T_PERSON_DELETE
AFTER DELETE ON T_PERSON
FOR EACH ROW
BEGIN
  INSERT INTO DBBUS_EVENT(table_name, id, ACTION, ts) VALUES('t_person',old.id, 2, DATE_FORMAT(NOW(),'%Y%m%d%H%i%s'));
END$

delimiter ;
```

***

# cluster
### zookeeper
dbbus.properties
```
cluster.type=zookeeper
cluster.hostPort=127.0.0.1:2181
```

### redis
dbbus.properties
```
cluster.type=redis
cluster.hostPort=10.6.104.232:6379
```

***

# properties file
```
# jdbc config
jdbc.driver=oracle.jdbc.driver.OracleDriver
jdbc.url=jdbc:oracle:thin:@10.6.2.72:1521:orcl
jdbc.username=dbbus
jdbc.password=dbbus
jdbc.maxActive=10            #optional default is 100
jdbc.minIdle=2               #optional default is 10

# event config
event.maxRowNum=1000         #optional default is 100, not bigger than 1000
event.mergeUpdate=true       #optional default is true
event.queueCapacity=100000   #optional default is 10000, max is 100000
event.pullerPoolSize=10      #optional default is 10
event.pullerDelay=1000       #optional default is 1000, unit is milliseconds
event.clearDelay=-1          #optional default is -1, unit is days

# table config   select person_name,person_age from t_person where person_id = ?
table.t_person.enable=true                     #optional default is false
table.t_person.id=person_id                    #optional default is id
table.t_person.columns=person_name,person_age  #optional default is *

# cluser config
#cluster.type=zookeeper      #optional default is none
#cluster.hostPort=127.0.0.1:2181
#or
#cluster.type=redis
#cluster.hostPort=127.0.0.1:6379

```

***

# start
## use your customer properties file
* edit start.sh (or start.bat) add your properties file location. example
```
config_file_option="-config /home/yiwu/config/your.properties"
```
* the default properties file is   ' classpath*:dbbus.properties '

## add customer porperties in properties file. example
```
...
dubbo.application.name=yiwugou-dbbus
dubbo.registry.address=zookeeper://10.6.104.77:3191
...
```

## use application.getProperties() get the properties
```
public class DbbusMain {
    public static void main(String[] args) throws Exception {
        Application application = new Application(args);
        Properties properties = application.getProperties();
        String dubboName = properties.getProperty("dubbo.application.name");
        String dubboAddress = properties.getProperty("dubbo.registry.address");
        OrderEventConsumer orderEventConsumer = new OrderEventConsumer(dubboName, dubboAddress);

        Map<String, EventConsumer> eventConsumerMap = new HashMap<>();
        eventConsumerMap.put("t_order", orderEventConsumer);
        BeanCreater beanCreater = BeanCreater.builder().eventConsumerMap(eventConsumerMap).build();
        application.setBeanCreater(beanCreater);
        application.start();
    }
}
```



