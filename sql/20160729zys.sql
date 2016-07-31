insert into privilege(id,name,url,parent_id) values(291,'导出加油信息月报表','/carRefuel_outPutOil_time',171);
insert into privilege(id,name,url,parent_id) values(292,'加油信息下载','/carRefuel_OilReport',171);
alter table carwash drop doEngineClean;
alter table carwash drop doInnerClean;
alter table carwash drop doPolishing;
insert into privilege(id,name,url,parent_id) values(311,'导入洗车信息','/carWash_excel',213);
insert into privilege(id,name,url,parent_id) values(312,'导入洗车信息','/carWash_importExcelFile',213);


