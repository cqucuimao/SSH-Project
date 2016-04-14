package com.msober.xmldata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.msober.base.BaseTest;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.TestUtils;

public class CreateXmlData extends BaseTest {

	@Autowired
	private DataSource dataSource;

	@Test
	public void createXmlData() throws FileNotFoundException, SQLException,
			DatabaseUnitException, IOException {
		List<String> tables = new ArrayList<String>();

		tables.add("address");
		tables.add("car");
		tables.add("carcare");
		tables.add("carexamine");
		tables.add("carrepair");
		tables.add("carrefuel");
		tables.add("carservicetype");
		tables.add("customer");
		tables.add("customer_phones");
		tables.add("customerorganization");
		tables.add("department");
		tables.add("device");
		tables.add("diskfile");
		tables.add("driverlicense");
		tables.add("location");
		tables.add("order_");
		tables.add("servicepoint");
		tables.add("user");

		TestUtils.exportData(dataSource.getConnection(), tables,
				"D:\\yuqin.xml");
	}
}
