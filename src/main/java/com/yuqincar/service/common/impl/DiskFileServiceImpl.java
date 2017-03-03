package com.yuqincar.service.common.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.yuqincar.dao.common.DiskFileDao;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.privilege.Contract;
import com.yuqincar.service.common.DiskFileService;
import com.yuqincar.utils.Configuration;

@Service
public class DiskFileServiceImpl implements DiskFileService {

	private final static String DiskFileFolder = "DiskFile";
	private static final int MAX_UNM_OF_FILES = 500;

	@Autowired
	private DiskFileDao diskFileDao;

	@Transactional
	public void deleteDiskFile(long id) {
		diskFileDao.delete(id);
	}

	@Transactional
	public DiskFile insertDiskFile(InputStream is, String fileName) {
		OutputStream os = null;
		DiskFile diskFile = null;
		try {
			diskFile = new DiskFile();
			diskFile.setFileName(fileName);
			if (fileName.lastIndexOf(".") >= 0)
				diskFile.setFileType(fileName.substring(
						fileName.lastIndexOf(".") + 1).toLowerCase());
			else
				diskFile.setFileType("");
			diskFile.setSize(is.available());
			diskFileDao.save(diskFile);

			String filePath = generateFilePath(diskFile.getId(),
					diskFile.getFileType(), is);
			os = new FileOutputStream(filePath);
			FileCopyUtils.copy(is, os);

			diskFile.setFilePath(filePath);
			diskFileDao.update(diskFile);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return diskFile;
	}

	private String generateFilePath(long id, String fileType, InputStream is) {
		String filePath = Configuration.getWorkspaceFolder();
		if (!filePath.endsWith("/"))
			filePath += "/";
		filePath += DiskFileFolder;
		filePath += "/";
		String subDirName = (new Integer((int) (id / MAX_UNM_OF_FILES) + 1))
				.toString();
		filePath += subDirName;
		filePath += "/";
		File subDir = new File(filePath);
		if (!subDir.exists())
			subDir.mkdirs();
		String fileName = id + "." + fileType;
		return filePath + fileName;
	}

	public InputStream getInputStream(DiskFile diskFile) {
		FileInputStream fis = null;
		try {
			File file = new File(diskFile.getFilePath());
			fis = new FileInputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fis;
	}

	public DiskFile getDiskFileById(long id) {
		return diskFileDao.getById(id);
	}

	@Transactional
	public void updateDiskFileData(DiskFile diskFile, InputStream is,
			String fileName) {
		OutputStream os = null;
		try {
			diskFile.setFileName(fileName);
			if (fileName.lastIndexOf(".") >= 0)
				diskFile.setFileType(fileName.substring(fileName
						.lastIndexOf(".") + 1));
			else
				diskFile.setFileType("");
			diskFile.setSize(is.available());

			File file = new File(diskFile.getFilePath());
			file.delete();

			String filePath = generateFilePath(diskFile.getId(),
					diskFile.getFileType(), is);
			os = new FileOutputStream(filePath);
			FileCopyUtils.copy(is, os);

			diskFile.setFilePath(filePath);
			diskFileDao.update(diskFile);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getContentType(String fileType) {
		if (fileType == null)
			return "application/x-msdownload";
		if (fileType.equals("jpg"))
			return "image/jpeg";
		else if (fileType.equals("png"))
			return "image/png";
		else if (fileType.equals("jpeg"))
			return "image/jpeg";
		else if (fileType.equals("gif"))
			return "image/gif";
		else if (fileType.equals("bmp"))
			return "image/bmp";
		else if (fileType.equals("ico"))
			return "image/x-icon";
		else if (fileType.equals("flv"))
			return "video/x-flv";
		else if (fileType.equals("avi"))
			return "video/avi";
		else if (fileType.equals("rmvb"))
			return "application/vnd.rn-realmedia-vbr";
		else if (fileType.equals("mpg"))
			return "video/mpeg";
		else if (fileType.equals("mpeg"))
			return "video/mpeg";
		else if (fileType.equals("wmv"))
			return "video/x-ms-wmv";
		else if (fileType.equals("rar"))
			return "application/x-rar-compressed";
		else if (fileType.equals("txt"))
			return "text/plain";
		else if (fileType.equals("doc") || fileType.equals("docx"))
			return "application/msword";
		else if (fileType.equals("xls") || fileType.equals("xlsx"))
			return "application/vnd.ms-excel";
		else if (fileType.equals("ppt") || fileType.equals("pptx"))
			return "application/vnd.ms-powerpoint";
		else if (fileType.equals("dwg"))
			return "image/vnd.dwg";
		else if (fileType.equals("eps"))
			return "application/postscript";
		else if (fileType.equals("max"))
			return "octet-stream";
		else if (fileType.equals("mp3"))
			return "audio/mpeg";
		else if (fileType.equals("pdf"))
			return "application/pdf";
		else if (fileType.equals("pot"))
			return "application/vnd.ms-powerpoint";
		else if (fileType.equals("psd"))
			return "application/postscript";
		else if (fileType.equals("swf"))
			return "application/x-shockwave-flash";
		else if (fileType.equals("tga"))
			return "application/x-tga";
		else if (fileType.equals("tif"))
			return "image/tiff";
		else if (fileType.equals("wav"))
			return "audio/x-wav";
		else if (fileType.equals("zip"))
			return "application/x-rar-compressed";
		return "application/x-msdownload";
	}


	public void downloadDiskFile(DiskFile diskFile, HttpServletResponse response) {
		String downLoadFileName = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			downLoadFileName = diskFile.getFileName();
			downLoadFileName = URLEncoder.encode(downLoadFileName, "UTF-8");
			downLoadFileName = downLoadFileName.replaceAll("\\+", "%20");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ downLoadFileName);
			response.setHeader("Content-Type",getContentType(diskFile.getFileType()));
			response.setHeader("Content-Length", ""+diskFile.getSize());
			response.setCharacterEncoding("UTF-8");
			os = response.getOutputStream();

			is = new FileInputStream(diskFile.getFilePath());
			byte[] bytebuffer = new byte[2048];
			while (true) {
				int length = -1;

				length = is.read(bytebuffer);

				if (length < 0) {
					break;
				}

				os.write(bytebuffer, 0, length);

			}
			os.flush();
			System.out.println("OK");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		System.out.println("OK666");
	}
	
	@Transactional
	public void updateDiskFileContract(DiskFile diskFile) {
		diskFileDao.update(diskFile);
	}
}
