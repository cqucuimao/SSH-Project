package com.yuqincar.service.common;

import java.util.List;

public class ExcelSheetContent {
	private String sheetName;

	private List<String> title;

	private List<List<String>> contents;
	
	public ExcelSheetContent(){
		
	}

	public ExcelSheetContent(String sheetName, List<String> title,
			List<List<String>> contents) {
		this.sheetName = sheetName;
		this.title = title;
		this.contents = contents;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<String> getTitle() {
		return title;
	}

	public void setTitle(List<String> title) {
		this.title = title;
	}

	public List<List<String>> getContents() {
		return contents;
	}

	public void setContents(List<List<String>> contents) {
		this.contents = contents;
	}
	
}
