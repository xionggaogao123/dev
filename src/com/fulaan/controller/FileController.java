package com.fulaan.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fulaan.auth.annotation.AuthFunctionType;
import com.fulaan.auth.annotation.Authority;
import com.fulaan.auth.annotation.ModuleType;
import com.fulaan.common.CommonResult;
import com.fulaan.common.FileExtension;
import com.fulaan.common.ProjectContent;
import com.fulaan.entity.Directory;
import com.fulaan.entity.ProjectFile;
import com.fulaan.service.DirectoryService;
import com.fulaan.service.FileService;

@Controller
@RequestMapping(value = "/file")
public class FileController {

	@Resource
	FileService fileService;
	
	@Resource
	DirectoryService directoryService;
	
	/**
	 * 保存上传的文件
	 * @param request
	 * @param response
	 * @param file 上传的文件
	 * @param dirID 目录id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@Authority(module = ModuleType.FILE, function = AuthFunctionType.INSERT)
	public CommonResult uploadFile(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam MultipartFile file,
			@RequestParam int dirID) {
		
		CommonResult result = null;
		
		Directory targetDir = directoryService.get(Directory.class, dirID);
		if(targetDir == null) {
			result = new CommonResult(1, "error", "上传失败，不存在该上传目录");
			return result;
		}
		
		if(file == null || file.getSize() == 0 
				|| "".equals(file.getOriginalFilename())) { // 未选择上传文件，或者上传文件大小为0
			result = new CommonResult(1, "error", "未选择文件或者文件大小为0");
			return result;
		}
		
		String fileName = file.getOriginalFilename();
		String extension = FilenameUtils.getExtension(fileName);
		int fileType = FileExtension.matchFileType(extension);
		if(fileType == -1) {
			result = new CommonResult(1, "error", "不支持该类型文件");
			return result;
		}
		
		for(ProjectFile pf : targetDir.getFiles()) { // 存在同名文件
			if(fileName.equals(pf.getFileName())) {
				result = new CommonResult(1, "error", "存在同名文件");
				return result;
			}
		}
		
		String filePath = bulidFilePath(targetDir);
		String fullPath = ProjectContent.PROJECT_FILE_ROOT_PATH + filePath;
		
		File uploadDir = new File(fullPath);
		if(!uploadDir.exists()) { // 不存在目录结构，创建多级目录
			uploadDir.mkdirs();
		}
		
		File uploadFile = new File(uploadDir, fileName);
		
		try {
			// 保存文件
			OutputStream os = new FileOutputStream(uploadFile);
			os.write(file.getBytes());
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ProjectFile projFile = new ProjectFile();
		projFile.setFileType(fileType);
		//projFile.setFilePath(fullPath);
		projFile.setFileName(fileName);
		projFile.setParentDir(targetDir);
		fileService.save(projFile);
		
		result = new CommonResult(0, "success", projFile.getId() + "");
		
		return result;
	}
	
	/**
	 * 构建上传文件保存路径
	 * @return
	 */
	private String bulidFilePath(Directory partentDir) {
		
		if(partentDir == null) {
			return "";
		}
		
		String path = "/" + partentDir.getName();
		
		if(partentDir.getParentDir() == null 
				|| partentDir.getParentDir().getId() == 0) {
			return path;
		} else {
			path = bulidFilePath(partentDir.getParentDir()) + path;
		}
		
		return path;
	}
	
	/**
	 * 重命名文件
	 * @param request
	 * @param response
	 * @param fileId 文件id
	 * @param name 新文件名
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rename", method = RequestMethod.POST)
	@Authority(module = ModuleType.FILE, function = AuthFunctionType.UPDATE)
	public CommonResult renameFile(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam int fileId,
			@RequestParam String name) {
		
		CommonResult result = null;
		
		ProjectFile prjFile = fileService.get(ProjectFile.class, fileId);
		if(prjFile == null) { // 不存在该文件
			result = new CommonResult(1, "error", "不存在该文件");
			return result;
		}

		if(name == null || "".equals(name.trim())) { // 空文件名称
			result = new CommonResult(1, "error", "文件名不允许为空");
			return result;
		}
		
		String oldName = prjFile.getFileName();
		String extension = FilenameUtils.getExtension(oldName);
		String newName = name + "." + extension;
		
		for(ProjectFile pf : prjFile.getParentDir().getFiles()) { // 存在同名文件
			if(newName.equals(pf.getFileName()) 
					&& pf.getId() != fileId) {
				result = new CommonResult(1, "error", "存在同名文件");
				return result;
			}
		}
		
		String filePath = ProjectContent.PROJECT_FILE_ROOT_PATH + bulidFilePath(prjFile.getParentDir());
		// 修改物理文件名称
		File oldNameFile = new File(filePath, oldName);
		if(!oldNameFile.exists()) {
			result = new CommonResult(1, "error", "文件不存在");
			return result;
		}
		File newNameFile = new File(filePath, newName);
		oldNameFile.renameTo(newNameFile);
		
		// 修改表中文件名称
		prjFile.setFileName(newName);
		fileService.update(prjFile);
		
		result = new CommonResult(0, "success", "修改文件成功");
		
		return result;
	}
	
	/**
	 * 删除文件
	 * @param request
	 * @param response
	 * @param fileId 文件id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@Authority(module = ModuleType.FILE, function = AuthFunctionType.DELETE)
	public CommonResult removeFile(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam int fileId) {
		
		CommonResult result = null;
		
		ProjectFile prjFile = fileService.get(ProjectFile.class, fileId);
		if(prjFile == null) { // 不存在该文件
			result = new CommonResult(1, "error", "不存在该文件");
			return result;
		}
		
		String fileName = prjFile.getFileName();
		String dirPath = ProjectContent.PROJECT_FILE_ROOT_PATH + bulidFilePath(prjFile.getParentDir());
		
		// 删除物理文件
		File toRemoveFile = new File(dirPath, fileName);
		if(!toRemoveFile.exists()) {
 			result = new CommonResult(1, "error", "文件不存在");
			return result;
		}
		toRemoveFile.delete(); // 删除
		
		fileService.delete(prjFile);
		
		result = new CommonResult(0, "success", "修改文件成功");
		
		return result;
	}
	
	/**
	 * 下载文件
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/download/{fileId}")
	@Authority(module = ModuleType.FILE, function = AuthFunctionType.READ)
	public ResponseEntity<byte[]> download(@PathVariable int fileId) throws Exception {
		
		ProjectFile prjFile = fileService.get(ProjectFile.class, fileId);
		if(prjFile == null) {
			return new ResponseEntity ("File Not Found", HttpStatus.OK);
		}
		String fileName = prjFile.getFileName();
		String dfileName = new String(fileName.getBytes(), "utf-8");
		
		String dirPath = ProjectContent.PROJECT_FILE_ROOT_PATH + bulidFilePath(prjFile.getParentDir());
		File file = new File(dirPath, fileName);
		if(!file.exists()) {
			return new ResponseEntity ("File Not Found", HttpStatus.OK);
		}
		
		HttpHeaders headers = new HttpHeaders(); 
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); 
		headers.setContentDispositionFormData("attachment", URLEncoder.encode(dfileName, "utf-8")); 
		
		return new ResponseEntity(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}
	
}
