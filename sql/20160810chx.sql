insert into privilege(id,name,url,parent_id) values(319,'司机APP','/driver_app',null);

insert into role(id,name) values(19,'驾驶员');

insert into role_privilege(roles_id,privileges_id) values (19,319);

CREATE UNIQUE INDEX licenseID ON driverlicense(licenseID);
ALTER TABLE car MODIFY VIN VARCHAR(255) NULL;
update car set VIN=null where VIN="none";
CREATE UNIQUE INDEX VIN ON car(VIN);
CREATE UNIQUE INDEX tollChargeSN ON car(tollChargeSN);
CREATE UNIQUE INDEX SN ON device(SN);
ALTER TABLE dayorderdetail MODIFY pathAbstract VARCHAR(1024) NULL;