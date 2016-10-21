package com.yuqincar.service.common.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;

import com.yuqincar.service.common.ExcelSheetContent;
import com.yuqincar.service.common.ExportService;
import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.ExcelUtil;

@Service
public class ExportServiceImpl implements ExportService {

	public void exportExcel(String exportName,
			List<ExcelSheetContent> sheetContents, HttpServletResponse response)
			throws IOException {
		System.out.println("in exportExcel");
		String fileName = URLEncoder.encode(exportName + ".xls", "UTF-8");
		fileName = fileName.replaceAll("\\+", "%20");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fileName);

		String tempFileName = Configuration.getWorkspaceFolder()
				//+ "\\ExportTemp\\"
				+ new Date().getTime()
				+ ".xls";
		for (ExcelSheetContent esc : sheetContents)
			ExcelUtil.writeLinesToExcel(tempFileName, esc.getSheetName(),
					esc.getTitle(), esc.getContents());

		File tempFile = new File(tempFileName);
		FileInputStream fi = new FileInputStream(tempFile);
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
		OutputStream os = response.getOutputStream();
		int n = 512;
		int count = 0;
		byte b[] = new byte[n];

		while ((count = fi.read(b, 0, n)) != -1)
			os.write(b, 0, count);
		fi.close();
		os.flush();
		os.close();

		tempFile.delete();
	}

	public void exportExcel(String fileName, List<String> title,
			List<List<String>> lines, HttpServletResponse response) throws IOException {
		List<ExcelSheetContent> sheetContents = new ArrayList<ExcelSheetContent>();
		sheetContents.add(new ExcelSheetContent(fileName, title, lines));
		exportExcel(fileName, sheetContents, response);
	}

}
