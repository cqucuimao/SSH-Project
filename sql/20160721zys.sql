insert into role(id,name) values(18,'技术保障科人员');
insert into role_privilege(roles_id,privileges_id)
values (18,228)
,(18,231)
,(18,172)
, (18,173)
, (18,196)
, (18,199)
, (18,201)
, (18,206)
, (18,209)
, (18,214)
, (18,221);


insert into privilege(id,name,url,parent_id) values(287,'导入保养信息','/carCare_excel',132);
insert into privilege(id,name,url,parent_id) values(288,'导入保养信息操作','/carCare_importExcelFile',132);

insert into privilege(id,name,url,parent_id) values(289,'导入维修信息','/carRepair_excel',160);
insert into privilege(id,name,url,parent_id) values(290,'导入维修信息操作','/carRepair_importExcelFile',160);


