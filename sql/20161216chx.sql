insert into user(loginName,name,password,phoneNumber,status,userType,department_id,company_id,visible)
			values('未知外派员工','未知外派员工','e10adc3949ba59abbe56e057f20f883e','13800000000',0,1,6,1,1);

update user set visible=1;
update user set visible=0 where loginName='admin' or loginName like '测试%' or loginName='未知外派员工';
