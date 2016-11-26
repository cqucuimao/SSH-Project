insert into privilege(id,name,url) values(405,'','/reserveCarApplyOrder_list');
insert into privilege(id,name,url) values(406,'','/reserveCarApplyOrder_addUI');
insert into privilege(id,name,url) values(407,'','/reserveCarApplyOrder_add');
insert into privilege(id,name,url) values(408,'','/reserveCarApplyOrder_editUI');
insert into privilege(id,name,url) values(409,'','/reserveCarApplyOrder_edit');
insert into privilege(id,name,url) values(411,'','/reserveCarApplyOrder_submitForNew');
insert into privilege(id,name,url) values(412,'','/reserveCarApplyOrder_submitForEdit');
insert into privilege(id,name,url) values(413,'','/reserveCarApplyOrder_view');
insert into privilege(id,name,url) values(414,'','/reserveCarApplyOrder_delete');
insert into privilege(id,name,url) values(415,'','/reserveCarApplyOrder_approve');
insert into privilege(id,name,url) values(416,'','/reserveCarApplyOrder_approveCar');
insert into privilege(id,name,url) values(417,'','/reserveCarApplyOrder_approveDriver');
insert into privilege(id,name,url) values(418,'','/reserveCarApplyOrder_approveUI');
insert into privilege(id,name,url) values(419,'','/reserveCarApplyOrder_approveCarUI');
insert into privilege(id,name,url) values(420,'','/reserveCarApplyOrder_approveDriverUI');

insert into role(id,name) values(22,'运营科领导');
insert into role(id,name) values(23,'技术保障科领导');
insert into role(id,name) values(24,'人力资源科领导');

insert into role_privilege(roles_id,privileges_id) values(17,405),(17,413),(17,415),17,418);
insert into role_privilege(roles_id,privileges_id) values(22,405),(22,406),(22,407),(22,408),
							 (22,409),(22,411),(22,412),(22,413),(22,414);
insert into role_privilege(roles_id,privileges_id) values(23,405),(23,413),(23,416),(23,419);
insert into role_privilege(roles_id,privileges_id) values(24,405),(24,413),(24,417),(24,420);

insert into role_privilege(roles_id,privileges_id) values (21,395);
insert into privilege(id,name,url,parent_id) values(421,'','/businessParameter_employeesFor4S',395);
insert into privilege(id,name,url,parent_id) values(422,'','/businessParameter_employeesForCarCare',395);
insert into privilege(id,name,url,parent_id) values(423,'','/businessParameter_reserveCarApplyOrderApplyUser',395);
insert into privilege(id,name,url,parent_id) values(424,'','/businessParameter_reserveCarApplyOrderCarApproveUser',395);
insert into privilege(id,name,url,parent_id) values(425,'','/businessParameter_reserveCarApplyOrderDriverApproveUser',395);
insert into privilege(id,name,url,parent_id) values(426,'','/businessParameter_addUI',395);
insert into privilege(id,name,url,parent_id) values(427,'','/businessParameter_addEmployeesFor4S',395);
insert into privilege(id,name,url,parent_id) values(428,'','/businessParameter_addEmployeesForCarCare',395);
insert into privilege(id,name,url,parent_id) values(429,'','/businessParameter_addReserveCarApplyOrderApplyUser',395);
insert into privilege(id,name,url,parent_id) values(430,'','/businessParameter_addReserveCarApplyOrderCarApproveUser',395);
insert into privilege(id,name,url,parent_id) values(431,'','/businessParameter_addReserveCarApplyOrderDriverApproveUser',395);
insert into privilege(id,name,url,parent_id) values(432,'','/businessParameter_reserveCarApplyOrderApproveUser',395);
insert into privilege(id,name,url,parent_id) values(433,'','/businessParameter_addReserveCarApplyOrderApproveUser',395);
insert into privilege(id,name,url,parent_id) values(434,'','/businessParameter_delete',395);

