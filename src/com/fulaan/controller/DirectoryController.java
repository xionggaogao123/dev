package com.fulaan.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.auth.annotation.AuthFunctionType;
import com.fulaan.auth.annotation.Authority;
import com.fulaan.auth.annotation.ModuleType;
import com.fulaan.common.CommonResult;
import com.fulaan.common.ProjectContent;
import com.fulaan.dao.base.BaseDao;
import com.fulaan.dto.DirectoryDto;
import com.fulaan.dto.ProjectFileDto;
import com.fulaan.entity.Directory;
import com.fulaan.entity.ProjectFile;
import com.fulaan.service.DirectoryService;
import com.fulaan.service.ProjectService;

@Controller
@RequestMapping(value = "/dir")
public class DirectoryController {

	@Resource
	BaseDao baseDao;
	
	@Resource
	ProjectService projectService;
	
	@Resource
	DirectoryService directoryService;
	
	/**
	 * 新增文件夹
	 * @param request
	 * @param response
	 * @param pid 父目录id
	 * @param dirName 文件名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@Authority(module = ModuleType.DIRECTORY, function = AuthFunctionType.INSERT)
	public CommonResult addDir(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(defaultValue = "0") int pid, // 父节点id
			@RequestParam String dirName) { 
		
		CommonResult result = null;
		
		if(dirName == null || "".equals(dirName.trim())) {
			result = new CommonResult(1, "error", "文件夹名为空");
			return result;
		}
		
		Directory pDir = baseDao.get(Directory.class, pid);
		if(pDir == null) { // 不存在该父文件夹
			result = new CommonResult(1, "error", "创建文件夹失败");
			return result;
		}
		
		List<Directory> childDirList = pDir.getChildDirs();
		if(childDirList != null && childDirList.size() > 0) {
			for(Directory cDir : childDirList) { // 同一父文件夹下是否存在同名文件夹
				if(cDir.getName().equals(dirName)) {
					result = new CommonResult(1, "error", "存在重名文件夹");
					return result;
				}
			}
		}
		
		Directory newDir = new Directory();
		newDir.setName(dirName);
		newDir.setParentDir(pDir);
		baseDao.save(newDir);
		
		result = new CommonResult(0, "success", newDir.getId().toString());
		
		return result;
	}
	
	/**
	 * 删除目录
	 * @param request
	 * @param response
	 * @param id 目录id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	@Authority(module = ModuleType.DIRECTORY, function = AuthFunctionType.DELETE)
	public CommonResult removeDir(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam int id) {
		
		CommonResult result = null;
		Directory dir = baseDao.get(Directory.class, id);
		if(dir == null) { // 不存在该文件夹
			result = new CommonResult(1, "error", "不存在该文件夹");
			return result;
		}
		
		boolean hasFile = isContainFile(dir);
		if(hasFile) { // 禁止删除非空文件夹
			result = new CommonResult(1, "error", "非空文件夹");
			return result;
		}
		
		String dirPath = ProjectContent.PROJECT_FILE_ROOT_PATH + bulidFilePath(dir);
		File dirFile = new File(dirPath);
		if(dirFile.exists()) { // 存在则删除物理目录
			dirFile.delete();
		}
		
		baseDao.delete(dir);
			
		result = new CommonResult(0, "success", "删除成功");
		
		return result;
	}
	
	/**
	 * 目录下是否含有文件
	 * @param dir
	 * @return
	 */
	private boolean isContainFile(Directory dir) {
		
		if(dir == null) 
			return false;
		
		if(dir.getFiles() != null 
				&& dir.getFiles().size() > 0) {
			return true;
		}
		
		List<Directory> dirList = dir.getChildDirs();
		if(dirList != null 
				|| dirList.size() > 0) {
			for(Directory d : dirList) {
				return isContainFile(d);
			}
		}
		
		return false;
	}
	
	/**
	 * 重命名目录
	 * @param request
	 * @param response
	 * @param id 目录id
	 * @param rename 新目录名
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rename", method = RequestMethod.POST)
	@Authority(module = ModuleType.DIRECTORY, function = AuthFunctionType.UPDATE)
	public CommonResult renameDir(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam int id,
			@RequestParam String rename) {
		
		CommonResult result = null;
		
		if(rename == null || "".equals(rename.trim())) {
			result = new CommonResult(1, "error", "文件夹名为空");
			return result;
		}
		
		Directory dir = baseDao.get(Directory.class, id);
		if(dir == null) { // 不存在该文件夹
			result = new CommonResult(1, "error", "重命名文件夹失败");
			return result;
		}
		
		List<Directory> childDirList = dir.getParentDir().getChildDirs();
		if(childDirList != null && childDirList.size() > 0) {
			for(Directory cDir : childDirList) { // 同一父文件夹下是否存在同名文件夹
				if(cDir.getName().equals(rename)) {
					result = new CommonResult(1, "error", "存在重名文件夹");
					return result;
				}
			}
		}
		
		String rootPath = ProjectContent.PROJECT_FILE_ROOT_PATH + bulidFilePath(dir.getParentDir());
		String oldDirPath = rootPath + File.separator + dir.getName(); 
		String newDirPath = rootPath + File.separator + rename;
		File oldDir = new File(oldDirPath);
		if(oldDir.exists()) { // 存在物理文件夹
			/*result = new CommonResult(1, "error", "不存在该文件夹");
			return result;*/
			File newDir = new File(newDirPath);
			oldDir.renameTo(newDir);
		}
		
		// 修改表中记录
		dir.setName(rename);
		baseDao.update(dir);
		
		result = new CommonResult(0, "success", "删除成功");
		
		return result;
	}

	/**
	 * 构建路径
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
	 * 刷新目录
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/refush", method = RequestMethod.POST)
	public Map<String, Object> refushDir(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam int parentDirId) { // 父目录id
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		Directory dir = directoryService.get(Directory.class, parentDirId);
		
		if(dir == null) {
			result.put("code", 1);
			result.put("msg", "error");
			return result;
		}
		
		List<DirectoryDto> dirDtoList = new ArrayList<DirectoryDto>();
		for(Directory d : dir.getChildDirs()) { // 保存子目录信息
			DirectoryDto dto = new DirectoryDto();
			dto.setId(d.getId());
			dto.setName(d.getName());
			if(d.getParentDir() != null) {
				dto.setPdirId(d.getParentDir().getId());
			}
			dirDtoList.add(dto);
		}
		
		List<ProjectFileDto> fileDtoList = new ArrayList<ProjectFileDto>();
		for(ProjectFile pf : dir.getFiles()) { // 保存子目录中文件信息
			ProjectFileDto fileDto = new ProjectFileDto();
			fileDto.setId(pf.getId());
			fileDto.setName(pf.getFileName());
			fileDto.setFileType(pf.getFileType());
			fileDto.setPdirId(pf.getParentDir().getId());
			fileDtoList.add(fileDto);
		}
		
		result.put("code", 0);
		result.put("childDir", dirDtoList);
		result.put("files", fileDtoList);
		result.put("msg", "success");
		
		return result;
	}
	
}
