package com.fulaan.dorm.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.dorm.dto.MoveLogDTO;
import com.fulaan.dorm.service.DormManageService;
import com.fulaan.dorm.service.DormService;
import com.pojo.app.IdValuePair;
import com.pojo.dormitory.*;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/9/9.
 */

@Controller
@RequestMapping("/dormManage")
public class DormManageController extends BaseController {
    private static final Logger logger = Logger.getLogger(DormManageController.class);

    @Autowired
    private DormManageService dormService;

    @RequestMapping("/dormpage")
    public String dormpage(Map<String, Object> model) {
        return "peoplegroup/dorm";
    }

    /**
     *
     * @param name
     * @param remark
     * @return
     */
    @RequestMapping("addDormitoryInfo")
    @ResponseBody
    public Map addDormitoryInfo(String name,String remark) {
        Map map = new HashMap();
        try {
            dormService.addDormitoryEntry(new DormitoryEntry(name, getSchoolId(), remark));
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param dormId
     * @return
     */
    @RequestMapping("deleteDormitoryInfo")
    @ResponseBody
    public Map deleteDormitoryInfo(String dormId) {
        Map map = new HashMap();
        try {
            dormService.deleteDormitoryEntryById(new ObjectId(dormId));
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param dormId
     * @return
     */
    @RequestMapping("updateDormitoryInfo")
    @ResponseBody
    public Map updateDormitoryInfo(String dormId,String name,String remark) {
        Map map = new HashMap();
        try {
            dormService.updateDormitoryEntry(dormId, name, remark);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("selDormitoryEntryList")
    @ResponseBody
    public Map selDormitoryEntryList() {
        Map map = new HashMap();
        map.put("rows", dormService.selDormitoryEntryList(getSchoolId()));
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("selLoopEntryList")
    @ResponseBody
    public Map selLoopEntryList(String dormId) {
        Map map = new HashMap();
        map.put("rows", dormService.selLoopEntryList(dormId));
        return map;
    }

    /**
     *
     * @param name
     * @param remark
     * @return
     */
    @RequestMapping("addLoopInfo")
    @ResponseBody
    public Map addLoopInfo(String name,String dormId,String remark) {
        Map map = new HashMap();
        try {
            dormService.addLoopEntry(new LoopEntry(name, new ObjectId(dormId), remark));
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param loopId
     * @return
     */
    @RequestMapping("deleteLoopInfo")
    @ResponseBody
    public Map deleteLoopInfo(String loopId) {
        Map map = new HashMap();
        try {
            dormService.deleteLoopEntryById(new ObjectId(loopId));
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param loopId
     * @return
     */
    @RequestMapping("updateLoopInfo")
    @ResponseBody
    public Map updateLoopInfo(String loopId,String name,String remark) {
        Map map = new HashMap();
        try {
            dormService.updateLoopEntry(loopId, name, remark);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("selRoomEntryList")
    @ResponseBody
    public Map selRoomEntryList(String loopId,String dormId) {
        Map map = new HashMap();
        map.put("rows", dormService.selRoomEntryList(loopId,dormId));
        return map;
    }

    /**
     *
     * @param name
     * @param remark
     * @return
     */
    @RequestMapping("addRoomInfo")
    @ResponseBody
    public Map addRoomInfo(String name,String remark,String loopId,int roomType,int bedNum) {
        Map map = new HashMap();
        try {
            List<RoomUserEntry> simpleDTOs = new ArrayList<RoomUserEntry>();
            for (int i=0;i<bedNum;i++) {
                simpleDTOs.add(new RoomUserEntry(i+1,"",null));
            }
            dormService.addRoomEntry(new RoomEntry(name, bedNum,new ObjectId(loopId),roomType, remark,simpleDTOs));
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param roomId
     * @return
     */
    @RequestMapping("deleteRoomInfo")
    @ResponseBody
    public Map deleteRoomInfo(String roomId) {
        Map map = new HashMap();
        try {
            dormService.deleteRoomEntryById(new ObjectId(roomId));
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     *
     * @param roomId
     * @return
     */
    @RequestMapping("updateRoomInfo")
    @ResponseBody
    public Map updateRoomInfo(String roomId,String name,String remark,int roomType,int bedNum) {
        Map map = new HashMap();
        try {
            dormService.updateRoomEntry(roomId, name, remark, roomType, bedNum);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     * 查询入住管理
     * @param dormId
     * @param loopId
     * @param sex
     * @return
     */
    @RequestMapping("selDormManage")
    @ResponseBody
    public Map selDormManageListInfo(String dormId,String loopId,int sex) {
        Map map = new HashMap();
        dormService.selDormManageListInfo(getSchoolId(),dormId,loopId,sex,map);
        return map;
    }

    /**
     * 人员入住
     * @param roomId
     * @param userId
     * @param bedNum
     * @return
     */
    @RequestMapping("addRoomUserInfo")
    @ResponseBody
    public Map addRoomUserInfo(@ObjectIdType ObjectId roomId,@ObjectIdType ObjectId userId,int bedNum) {
        Map map = new HashMap();
        try {
            dormService.addRoomUserInfo(roomId,userId,bedNum);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     * 获取入住弹框信息
     * @param roomId
     * @param bedNum
     * @return
     */
    @RequestMapping("selBedOptionInfo")
    @ResponseBody
    public Map selBedOptionInfo(@ObjectIdType ObjectId roomId,String bedNum) {
        Map map = new HashMap();
        dormService.selBedOptionInfo(getSchoolId(),roomId,bedNum,map);
        return map;
    }

    /**
     * 获取寝室调整弹框信息
     * @param sex
     * @param dormId
     * @param loopId
     * @param roomId
     * @param type
     * @return
     */
    @RequestMapping("selRoomOptionInfo")
    @ResponseBody
    public Map selRoomOptionInfo(int sex,String dormId,String loopId,String roomId,int type) {
        Map map = new HashMap();
        dormService.selRoomOptionInfo(getSchoolId(), sex, dormId, loopId, roomId,type,map);
        return map;
    }

    /**
     * 选择年级班级
     * @param gradeId
     * @param classId
     * @return
     */
    @RequestMapping("selGradeClassUserInfo")
    @ResponseBody
    public Map selGradeClassUserInfo(String gradeId,String classId,@ObjectIdType ObjectId roomId,int type) {
        Map map = new HashMap();
        dormService.selGradeClassUserInfo(getSchoolId(),gradeId,classId,roomId,type,map);
        return map;
    }

    /**
     * 迁出人员
     * @param roomId
     * @param bedNum
     * @return
     */
    @RequestMapping("moveUserInfo")
    @ResponseBody
    public Map moveUserInfo(@ObjectIdType ObjectId roomId,int bedNum,String cause) {
        Map map = new HashMap();
        try {
            dormService.moveUserInfo(roomId,bedNum, cause);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     * 调整宿舍
     * @param orgRoomId
     * @param orgBedNum
     * @param roomId
     * @param bedNum
     * @return
     */
    @RequestMapping("updateUserRoom")
    @ResponseBody
    public Map updateUserRoom(String orgRoomId,int orgBedNum,String roomId,int bedNum) {
        Map map = new HashMap();
        try {
            dormService.updateUserRoom(orgRoomId, orgBedNum, roomId, bedNum);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

    /**
     * 迁出名单列表
     * @param dormId
     * @param loopId
     * @param roomId
     * @param userName
     * @return
     */
    @RequestMapping("selMoveUserList")
    @ResponseBody
    public Map selMoveUserList(@ObjectIdType ObjectId dormId,String loopId,String roomId,String userName,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
        Map map = new HashMap();
        int count = dormService.selMoveUserCount(dormId,loopId,roomId,userName);
        List<MoveLogDTO> moveLogDTOs = dormService.selMoveUserList(dormId,loopId,roomId,userName,page,pageSize);
        map.put("rows",moveLogDTOs);
        map.put("total", count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }

    /**
     * 住宿名单列表
     * @param dormId
     * @param loopId
     * @param roomId
     * @param userName
     * @return
     */
    @RequestMapping("selRoomUserList")
    @ResponseBody
    public Map selRoomUserList(@ObjectIdType ObjectId dormId,String loopId,String roomId,String userName,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
        Map map = new HashMap();
        int count = dormService.selRoomUserCount(dormId, loopId, roomId, userName);
        List<MoveLogDTO> moveLogDTOs = dormService.selRoomUserList(getSchoolId(),dormId, loopId, roomId, userName, page, pageSize);
        map.put("rows",moveLogDTOs);
        map.put("total", count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }

    /**
     * 清空数据
     * @param remark
     * @return
     */
    @RequestMapping("cleanRoomUserInfo")
    @ResponseBody
    public Map cleanRoomUserInfo(String remark) {
        Map map = new HashMap();
        try {
            dormService.cleanRoomUserInfo(getSchoolId(), remark);
            map.put("flag",true);
        } catch (Exception e) {
            map.put("flag",false);
        }
        return map;
    }

}