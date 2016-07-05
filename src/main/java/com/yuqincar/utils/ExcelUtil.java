package com.yuqincar.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {
	public static String MERGE_LEFT = "${L}";
	public static String MERGE_UP = "${U}";

	public static List<String> getStringFromExcel(String src, int rowFrom,
			int rowTo, int colFrom, int colTo, int sheetIndex) {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(new File(src));
			Sheet sheet = wb.getSheet(sheetIndex);
			return getStringFromExcel(sheet, rowFrom, rowTo, colFrom, colTo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (wb != null)
				wb.close();
		}
	}
	
	public static List<List<String>> getLinesFromExcel(String src, int rowFrom,int colFrom, int colTo, int sheetIndex) {
		try{
			InputStream is=new FileInputStream(new File(src));
			return getLinesFromExcel(is,rowFrom,colFrom,colTo,sheetIndex);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<List<String>> getLinesFromExcel(InputStream is, int rowFrom,int colFrom, int colTo, int sheetIndex){
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(sheetIndex);
			List<List<String>> lines=new ArrayList<List<String>>(sheet.getRows()-rowFrom+1);
			for(int i=rowFrom;i<=sheet.getRows();i++){
				List<String> line=getStringFromExcel(sheet, i, i, colFrom, colTo);
				if(line!=null && line.size()>0 && !StringUtils.isEmpty(line.get(0)))
					lines.add(line);
			}
			return lines;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (wb != null)
				wb.close();
		}
	}
	
	public static List<String> getStringFromExcel(InputStream is, int rowFrom,
			int rowTo, int colFrom, int colTo, int sheetIndex) {
		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(sheetIndex);
			if (rowTo == 0)
				rowTo = sheet.getRows();
			if (colTo == 0)
				colTo = sheet.getColumns();
			return getStringFromExcel(sheet, rowFrom, rowTo, colFrom, colTo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (wb != null)
				wb.close();
		}
	}

	public static List<String> getStringFromExcel(InputStream is, int rowFrom,
			int rowTo, int colFrom, int colTo) {
		return getStringFromExcel(is, rowFrom, rowTo, colFrom, colTo, 0);
	}

	public static List<String> getStringFromExcel(Sheet sheet, int rowFrom,
			int rowTo, int colFrom, int colTo) {
		List<String> list = new ArrayList<String>();
		for (int row = rowFrom - 1; row <= rowTo - 1; row++)
			for (int col = colFrom - 1; col <= colTo - 1; col++)
				list.add(sheet.getCell(col, row).getContents());
		return list;
	}

	public static void writeStringToExcel(String src, List<String> list,
			int rowFrom, int rowTo, int colFrom, int colTo, int sheetIndex) {
		WritableWorkbook wwb = null;
		try {
			wwb = Workbook.createWorkbook(new File(src));
			WritableSheet sheet = wwb.getSheet(sheetIndex);
			writeStringToExcel(sheet, list, rowFrom, rowTo, colFrom, colTo);
			wwb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeStringToExcel(String src, List<String> list,
			int rowFrom, int rowTo, int colFrom, int colTo) {
		writeStringToExcel(src, list, rowFrom, rowTo, colFrom, colTo, 0);
	}

	public static void writeStringToExcel(WritableSheet sheet,
			List<String> list, int rowFrom, int rowTo, int colFrom, int colTo) {
		int n = 0;
		for (int row = rowFrom - 1; row <= rowTo - 1; row++)
			for (int col = colFrom - 1; col <= colTo - 1; col++) {
				WritableCell label = null;
				if (n < list.size() && list.get(n) != null
						&& list.get(n).length() > 0) {
					// 数字内容
					if (isNumeric(list.get(n)))
						label = new jxl.write.Number(col, row,
								Float.valueOf(list.get(n)));
					// 公式内容
					else if (list.get(n).charAt(0) == '=')
						label = new jxl.write.Formula(col, row, list.get(n)
								.substring(1, list.get(n).length() - 1));
					else if (list.get(n).equals(MERGE_LEFT)
							|| list.get(n).equals(MERGE_UP))
						label = new Label(col, row, "");
					else
						label = new Label(col, row, list.get(n));
				} else
					label = new Label(col, row, "");
				try {
					sheet.addCell(label);
					if (n < list.size() && list.get(n) != null
							&& list.get(n).length() > 0) {
						if (list.get(n).equals(MERGE_LEFT)) {
							boolean merged = false;
							Cell topLeft = null;
							for (Range range : sheet.getMergedCells()) {
								if (range.getBottomRight().getRow() == row
										&& range.getBottomRight().getColumn() == col - 1) {
									topLeft = range.getTopLeft();
									sheet.unmergeCells(range);
									merged = true;
									break;
								}
							}
							if (merged)
								sheet.mergeCells(topLeft.getColumn(),
										topLeft.getRow(), col, row);
							else
								sheet.mergeCells(col - 1, row, col, row);
						}
						if (list.get(n).startsWith(MERGE_UP)) {
							boolean merged = false;
							Cell topLeft = null;
							for (Range range : sheet.getMergedCells()) {
								if (range.getBottomRight().getRow() == row - 1
										&& range.getBottomRight().getColumn() == col) {
									topLeft = range.getTopLeft();
									sheet.unmergeCells(range);
									merged = true;
									break;
								}
							}
							if (merged)
								sheet.mergeCells(topLeft.getColumn(),
										topLeft.getRow(), col, row);
							else
								sheet.mergeCells(col, row - 1, col, row);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				n++;
			}
	}

	// 往xls文件中追加多行数据，如果没有文件，则创建，如果没有sheet，则创建
	// fileName 目标文件
	// sheetName sheet名称
	// title 当新建sheet时写在头部的每列标题
	// lines 需要写入到excel文件的内容
	public static void writeLinesToExcel(String fileName, String sheetName,
			List<String> title, List<List<String>> lines) {
		File file = new File(fileName);
		Workbook wb = null;
		WritableWorkbook wwb = null;
		WritableSheet sheet = null;
		try {
			if (file.exists()) {
				wb = Workbook.getWorkbook(file);
				wwb = Workbook.createWorkbook(file, wb);
			} else {
				wwb = Workbook.createWorkbook(file);
			}
			sheet = wwb.getSheet(sheetName);
			if (sheet == null) {
				wwb.createSheet(sheetName, 0);
				sheet = wwb.getSheet(sheetName);
				if (title != null)
					writeStringToExcel(sheet, title, 1, 1, 1, title.size());
			}
			for (List<String> line : lines)
				writeStringToExcel(sheet, line, sheet.getRows() + 1,
						sheet.getRows() + 1, 1, line.size());

			wwb.write();
			wwb.close();
			if (wb != null)
				wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isNumeric(String str) {
		int begin = 0;
		boolean once = true;
		if (str == null || str.trim().equals("")) {
			return false;
		}
		str = str.trim();
		if (str.startsWith("+") || str.startsWith("-")) {
			if (str.length() == 1) {
				// "+" "-"
				return false;
			}
			begin = 1;
		}
		for (int i = begin; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				if (str.charAt(i) == '.' && once) {
					// '.' can only once
					once = false;
				} else {
					return false;
				}
			}
		}
		if (str.length() == (begin + 1) && !once) {
			// "." "+." "-."
			return false;
		}
		return true;
	}

}
