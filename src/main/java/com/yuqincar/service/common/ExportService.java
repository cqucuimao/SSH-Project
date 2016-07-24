package com.yuqincar.service.common;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;


public interface ExportService {	

	public void exportExcel(String fileName, List<ExcelSheetContent> contents,
			HttpServletResponse response) throws IOException;
	
	public void exportExcel(String fileName, List<String> title,
			List<List<String>> lines, HttpServletResponse response) throws IOException;

}
