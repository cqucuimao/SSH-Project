package com.yuqincar.domain.common;

import javax.persistence.Entity;

import com.yuqincar.utils.Text;


/**
 * 代表服务器上存储在磁盘上的文件。
 *
 */

@Entity
public class DiskFile extends BaseEntity {

	@Text("文件名")
	private String fileName;	//（包括扩展名）

	@Text("文件类型")
	private String fileType;	//（不包含.）

	@Text("文件绝对路径")
	private String filePath;	//（包括文件名和扩展名）

	@Text("字节数")
	private long size;	//字节数
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}
