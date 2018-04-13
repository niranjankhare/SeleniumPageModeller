create database IF NOT EXISTS automation;

use automation;

create table PAGES (
`PAGEID` int (10) NOT NULL AUTO_INCREMENT,
`PAGENAME` VARCHAR (50) UNIQUE, -- SELECT
`PARENTID` int (10), -- SELECT
`PAGEDESCRIPTION` VARCHAR (150), -- SELECT
PRIMARY KEY (`PAGEID`)
);
insert into PAGES (`PAGENAME`, `PARENTID`, `PAGEDESCRIPTION`)
values ('Login',null, 'Login page');
create table GUIMAP (
`GUIMAPID` int (10) NOT NULL AUTO_INCREMENT,
`PAGEID`  int (10) NOT NULL, 
`CONTROLNAME` VARCHAR (50), -- SELECT 
`CONTROLDESCRIPTION` VARCHAR (150), -- SELECT 
 PRIMARY KEY (`GUIMAPID`),
 CONSTRAINT fk_page FOREIGN KEY (`PAGEID`)
 REFERENCES PAGES(`PAGEID`) 
 ON DELETE CASCADE
 ON UPDATE CASCADE
 );



create table PROPERTIES (
`PROPERTYID` int (10) NOT NULL AUTO_INCREMENT,
`GUIMAPID` int (10) NOT NULL, 
`STANDARDCLASS`  VARCHAR (50), -- same as element type from guimap
`MAPPEDCLASS`  VARCHAR (50) DEFAULT '(No Maping)', 
`LOCATORVALUE` VARCHAR  (100) NOT NULL, -- Select
`LOCATORTYPE` VARCHAR (10) NOT NULL, -- Calculate based on value (xpath or id?)
PRIMARY KEY (`PROPERTYID`),
CONSTRAINT fk_guimap FOREIGN KEY (GUIMAPID)
  REFERENCES GUIMAP(`GUIMAPID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

create table TYPES (
`CLASSID` int (10) NOT NULL AUTO_INCREMENT,
`CLASS` VARCHAR (20) NOT NULL,
`TYPE` VARCHAR (20) NOT NULL, 
`ABRV` VARCHAR (10) NOT NULL,
`HASEXTENDEDPROPS` Boolean,
`PROPERTYMAP` VARCHAR  (1000),
PRIMARY KEY (`CLASSID`)
);

insert into TYPES (`CLASS`, `TYPE`, `ABRV`,`HASEXTENDEDPROPS`,`PROPERTYMAP`)
values  ('(No Maping)','CUSTOM','del',true, null),
('Button','STANDARD','btn',null, null),
('InputText','STANDARD','iTxt',null, null),
('Select','STANDARD','sel',true,'{"EXPROP1":"options"}'),
('ComplexSelect','CUSTOM','emSel',true, '{"EXPROP1":"options","EXPROP2":"showControlId"}');

create table EXTENDEDPROPS (
`EXPROPID` int (10) NOT NULL AUTO_INCREMENT,
`GUIMAPID` int (10) NOT NULL, 
`EXPROP1` VARCHAR  (100),
`EXPROP2` VARCHAR  (100),
`EXPROP3` VARCHAR  (100),
`EXPROP4` VARCHAR  (100),
`EXPROP5` VARCHAR  (100),
`EXPROP6` VARCHAR  (100),
`EXPROP7` VARCHAR  (100),
`EXPROP8` VARCHAR  (100),
`EXPROP9` VARCHAR  (100),
PRIMARY KEY (`EXPROPID`),
CONSTRAINT fk_guimap_ex 
FOREIGN KEY (GUIMAPID)
  REFERENCES GUIMAP(`GUIMAPID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

CREATE VIEW automation.PROPSVIEW AS
SELECT p.PAGENAME, g.GUIMAPID,r.MAPPEDCLASS, g.CONTROLNAME, g.CONTROLDESCRIPTION,
r.LOCATORVALUE, r.LOCATORTYPE,r.STANDARDCLASS
FROM
 automation.GUIMAP g INNER JOIN
 automation.PROPERTIES r ON
g.GUIMAPID = r.GUIMAPID INNER JOIN
automation.PAGES p ON
g.PAGEID = p.PAGEID;


CREATE VIEW automation.EXTENDEDPROPSVIEW AS
SELECT  
c.GUIMAPID, p.MAPPEDCLASS, c.EXPROP1, c.EXPROP2, c.EXPROP3, c.EXPROP4, c.EXPROP5,
c.EXPROP6, c.EXPROP7, c.EXPROP8, c.EXPROP9
FROM
automation.EXTENDEDPROPS c 
INNER JOIN automation.PROPERTIES p 
ON c.GUIMAPID=p.GUIMAPID;

/*
ALTER TABLE PROPERTIES
DROP FOREIGN KEY fk_guimap;
ALTER TABLE GUIMAP
DROP FOREIGN KEY fk_page;

/*
delimiter //
CREATE TRIGGER updateElements AFTER INSERT ON GUIMAP
FOR EACH ROW 
BEGIN 
INSERT INTO PROPERTIES  VALUES (new.idemployee.40);
   END//;
delimiter ;


insert into PAGES (`PAGENAME`, `PARENTID`, `PAGEDESCRIPTION`)
values ('Dashboard', null, 'Dashboard');
-- values ('LeftMenu',null, 'Left Menu Bar'),('TopMenu',null, 'Top Menu Bar');

update PAGES set PARENTID=3
WHERE 
PAGEID=5;

alter TABLE PAGES
DROP COLUMN `PARENTID`;

alter TABLE PAGES
ADD COLUMN `PARENTID` int(10) AFTER `PAGENAME`;

insert into PAGES (`PAGENAME`, `PARENTID`, `PAGEDESCRIPTION`)
values (
1,	'Login',	null,	'Login page'),
(3,	'LeftMenu',	4,	'Left Menu Bar');
(4,	'TopMenu',	null,	'Top Menu Bar'),
(5,	'Dashboard',	3,	'Dashboard');
*/
INSERT INTO PAGES
SET `pagename` = 'logon',
`parentid` = null,
`pagedescription` = "Login page";



