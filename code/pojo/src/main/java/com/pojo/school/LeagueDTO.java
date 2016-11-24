package com.pojo.school;

import java.util.ArrayList;
import java.util.List;
import com.pojo.app.IdValuePairDTO;
import com.pojo.lesson.DirDTO;

/***
 * 联盟DTO,包括成员和dir
 * @author fourer
 *
 */
public class LeagueDTO {

	private String id;
	private String name;
	private List<IdValuePairDTO> memberList =new ArrayList<IdValuePairDTO>();
	private List<DirDTO> dirList =new ArrayList<DirDTO>();
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<IdValuePairDTO> getMemberList() {
		return memberList;
	}
	public void setMemberList(List<IdValuePairDTO> memberList) {
		this.memberList = memberList;
	}
	public List<DirDTO> getDirList() {
		return dirList;
	}
	public void setDirList(List<DirDTO> dirList) {
		this.dirList = dirList;
	}
	
	
	
	public void addMemberDTO(IdValuePairDTO dto)
	{
		this.memberList.add(dto);
	}
	
	
	public void addDirListDTO(DirDTO dto)
	{
		this.dirList.add(dto);
	}
}
