package com.fulaan.learningcenter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pojo.lesson.DirType;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fulaan.base.controller.BaseController;
import com.fulaan.base.service.DirService;
import com.fulaan.learningcenter.service.LeagueService;
import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO;
import com.pojo.lesson.DirDTO;
import com.pojo.lesson.DirEntry;
import com.pojo.school.LeagueDTO;
import com.pojo.school.LeagueEnrty;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 联盟成员
 * 
 * @author fourer
 *
 */
@Controller
@RequestMapping("/league")
public class LeagueController extends BaseController {

	private LeagueService leagueService = new LeagueService();
	private DirService dirService =new DirService();
	
	
	@RequestMapping("/info")
	@ResponseBody
	public List<LeagueDTO>  getInfos()
	{
		LeagueDTO dto;
		Map<ObjectId,LeagueDTO> map =new HashMap<ObjectId, LeagueDTO>();
		ObjectId mySchoolId =new ObjectId(getSessionValue().getSchoolId());
		List<LeagueEnrty> list=leagueService.getLeagueEnrtys(mySchoolId, Constant.FIELDS);
		
		for(LeagueEnrty entry:list)
		{
			if(!map.containsKey(entry.getID()))
			{
				dto =new LeagueDTO();
				dto.setName(entry.getName());
                //dto.setId("1");
                map.put(entry.getID(),dto);
			}
			
			for(IdValuePair pair:entry.getSchools())
			{
				map.get(entry.getID()).addMemberDTO(new IdValuePairDTO(pair));
			}
		}
		
		List<ObjectId> ownerList =MongoUtils.getFieldObjectIDs(list, Constant.ID);
		
		List<DirEntry> dirs=dirService.getDirEntryList(ownerList, Constant.FIELDS, DirType.UNION_RESOURCE.getType());
		
		for(DirEntry dir:dirs)
		{
			DirDTO dirDTO =new DirDTO(dir);
            //dirDTO.setParentId("1");
			map.get(dir.getOwerId()).addDirListDTO(dirDTO);
		}
		return new ArrayList<LeagueDTO>(map.values());
	}
}
