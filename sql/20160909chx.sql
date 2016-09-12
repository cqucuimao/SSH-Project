alter table device drop column PN;
insert into role_noprivilege(role_id,privilege_id) values(5,114);
insert into role_noprivilege(role_id,privilege_id) values(5,116);

alter table carcare drop column appointment;
alter table carcare drop column done;
alter table carcare drop column mileInterval;
delete from privilege where url='/carcare_appointment';
delete from role_privilege where privileges_id=139;
delete from privilege where url='/carcare_remind';
delete from privilege where url='/carcare_saveAppointment';

alter table carexamine drop column appointment;
alter table carexamine drop column done;
delete from privilege where url='/carexamine_appointment';
delete from privilege where url='/carexamine_saveAppointment';

alter table carrepair drop column appointment;
alter table carrepair drop column done;
delete from privilege where url='/carrepair_appointment';
delete from privilege where url='/carrepair_saveAppointment';