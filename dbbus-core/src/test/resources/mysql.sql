-- Mysql
-- Create table
CREATE TABLE `DBBUS_EVENT`(
  `txn` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `table_name` VARCHAR(20) NOT NULL,
  `id` VARCHAR(20) NOT NULL,
  `action` INT(1) NOT NULL COMMENT '0-insert;1-update;2-delete;',
  `status` INT(1) NOT NULL DEFAULT 0 COMMENT '0-unread;1-readed;2-error;',
  `ts` BIGINT(14) NOT NULL ,
  PRIMARY KEY (`txn`)
);

-- Create　trigger for each table
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
