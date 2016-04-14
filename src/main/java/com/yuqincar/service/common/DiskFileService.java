package com.yuqincar.service.common;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import com.yuqincar.domain.common.DiskFile;

public interface DiskFileService {
	
	public DiskFile insertDiskFile(InputStream is, String fileName);

	public void deleteDiskFile(long id);

	public InputStream getInputStream(DiskFile diskFile);

	public DiskFile getDiskFileById(long id);

	public void updateDiskFileData(DiskFile diskFile, InputStream is,
			String fileName);
	
	public void downloadDiskFile(DiskFile diskFile, HttpServletResponse response);
}
