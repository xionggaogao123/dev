package com.pojo.itempool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bson.types.ObjectId;

import com.pojo.app.IdValuePairDTO;
import com.pojo.app.IdValuePairSortDTO;
import com.pojo.resources.ResourceDictionaryEntry;
import com.sys.constants.Constant;

/**
 * 知识点DTO
 * @author fourer
 *
 */
public class KnowledgePointDTO {

	private ObjectId id;
	private String name;
	private long sort;
	
	private List<IdValuePairDTO> list =new ArrayList<IdValuePairDTO>();
	
	//用于构建节点数，包括2及以及3及节点
	private List<List<IdValuePairSortDTO>> nodes =new ArrayList<List<IdValuePairSortDTO>>();

	public KnowledgePointDTO(ResourceDictionaryEntry e) {
		this.id = e.getID();
		this.name = e.getName();
		this.sort=e.getSort();
	}

	public KnowledgePointDTO() {
		
	}
	
	public String getIdStr() {
		if(null!=this.id)
		{
			return this.id.toString();
		}
		return Constant.EMPTY;
	}

	public void setIdStr(String idStr) {
		//do nothing
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	
	public List<List<IdValuePairSortDTO>> getNodes() {
		return nodes;
	}

	public void setNodes(List<List<IdValuePairSortDTO>> nodes) {
		this.nodes = nodes;
	}

	public void addSecondNode(IdValuePairSortDTO dto,List<IdValuePairSortDTO> thirdList)
	{
		thirdList.add(Constant.ZERO, dto);
		nodes.add(thirdList);
	}

	public List<IdValuePairDTO> getList() {
		return list;
	}

	public void setList(List<IdValuePairDTO> list) {
		this.list = list;
	}
	
	
	
	public void addDTO(IdValuePairDTO dto)
	{
		this.list.add(dto);
	}

	public long getSort() {
		return sort;
	}

	public void setSort(long sort) {
		this.sort = sort;
	}
	public void sort()
	{
		
		Collections.sort(nodes, new Comparator<List<IdValuePairSortDTO>>() {
			@Override
			public int compare(List<IdValuePairSortDTO> arg0, List<IdValuePairSortDTO> arg1) {
				
				try
				{
					long a= arg0.get(0).getSort()-arg1.get(0).getSort();
					if(a>0)
					{
						return Constant.ONE;
					}
					if(a<0)
					{
						return Constant.NEGATIVE_ONE;
					}
					return Constant.ZERO;
				}catch(Exception ex)
				{
					
				}
				
				return Constant.ZERO;
			}
		});
		
		
		
	}
	
}
