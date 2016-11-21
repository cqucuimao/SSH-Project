package com.yuqincar.domain.common;

import java.util.List;

import javax.persistence.Entity;

public class UploadedDiskFiles extends BaseEntity {


	private List<DiskFile> diskFiles;	//（包括扩展名）

	public List<DiskFile> getDiskFiles() {
		return diskFiles;
	}

	public void setDiskFiles(List<DiskFile> diskFiles) {
		this.diskFiles = diskFiles;
	}

}