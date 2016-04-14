package com.yuqincar.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class TestUtils {
	/**
	 * Export data for the table names by the given IDatabaseConnection into the
	 * resultFile.<br>
	 * The export data will be DBUnit format.
	 * 
	 * @param conn
	 *            数据库连接
	 * @param tableNameList
	 *            数据库表名列表
	 * @param resultFile
	 *            生成的文件地址
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void exportData(Connection connection, List<String> tableNameList, String resultFile)
			throws SQLException, DatabaseUnitException, FileNotFoundException, IOException {
		QueryDataSet dataSet = null;
		if (connection == null) {
			return;
		}
		if (tableNameList == null || tableNameList.size() == 0) {
			return;
		}
		try {

			dataSet = new QueryDataSet(new DatabaseConnection(connection));
			for (String tableName : tableNameList) {
				dataSet.addTable(tableName);
			}
		} finally {
			if (dataSet != null) {
				FlatXmlDataSet.write(dataSet, new FileOutputStream(resultFile));
			}
		}

	}	
}
