insert into privilege(id,name,url,parent_id) values(392,'������������','/carExamine_exportRemind',181);
insert into privilege(id,name,url,parent_id) values(393,'������������','/carInsurance_exportRemind',144);
insert into privilege(id,name,url,parent_id) values(394,'����·�ŷ�����','/tollCharge_exportRemind',195);

insert into role(id,name) values(21,'������Ա');
insert into privilege(id,name,url) values(395,'����','/businessParameter');
insert into privilege(id,name,url,parent_id) values(396,'','/businessParameter_employees',395);
insert into role_privilege(roles_id,privileges_id)
values (21,396);

insert into businessparameter(id,mileageForCarCareRemind,company_id) values(1,8000,1);
insert into businessparameter_employeesforcarcareappointmentsms_user(businessparameter,user) values(1,28);
insert into businessparameter_employeesin4sforsms_user(businessparameter,user) values(1,24);
