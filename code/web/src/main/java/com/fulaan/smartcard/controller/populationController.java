package com.fulaan.smartcard.controller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.school.service.SchoolService;
import com.fulaan.smartcard.dto.ClassDTO;
import com.fulaan.smartcard.dto.RoomDTO;
import com.pojo.school.SchoolEntry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang_xinxin on 2016/9/7.
 */
@Controller
@RequestMapping("/population")
public class populationController extends BaseController {

    private static final Logger logger = Logger.getLogger(populationController.class);

    @Autowired
    private SchoolService schoolService;

    @RequestMapping("/peopleGroup")
    public String myKaoQin(Map<String, Object> model) {
        SchoolEntry schoolEntry = schoolService.getSchoolEntry(getSchoolId(), null);
        model.put("schoolName",schoolEntry.getName());
        return "peoplegroup/population";
    }

    /**
     *
     * @return
     */
    @RequestMapping("/selRoomDetail")
    public @ResponseBody Map selRoomDetail() {
        Map map = new HashMap();
        List<RoomDTO> roomDTOs = new ArrayList<RoomDTO>();
        roomDTOs.add(new RoomDTO("善进楼",512));
        roomDTOs.add(new RoomDTO("德善楼",620));
        roomDTOs.add(new RoomDTO("图书馆",258));
        roomDTOs.add(new RoomDTO("善恒楼",216));
        roomDTOs.add(new RoomDTO("综合大楼",398));
        roomDTOs.add(new RoomDTO("篮球场",23));
        roomDTOs.add(new RoomDTO("操场",45));
        roomDTOs.add(new RoomDTO("器材室",6));
        roomDTOs.add(new RoomDTO("总计",2078));
        map.put("rows",roomDTOs);
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/selLoopInfoList")
    public @ResponseBody Map selLoopInfoList() {
        Map map = new HashMap();
        List<RoomDTO> roomDTOs = new ArrayList<RoomDTO>();
        roomDTOs.add(new RoomDTO("6F",189));
        roomDTOs.add(new RoomDTO("5F",217));
        roomDTOs.add(new RoomDTO("4F",167));
        roomDTOs.add(new RoomDTO("3F",156));
        roomDTOs.add(new RoomDTO("2F",234));
        roomDTOs.add(new RoomDTO("1F",124));
        map.put("rows",roomDTOs);
        return map;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/selRoomInfoList")
    public @ResponseBody Map selRoomInfoList() {
        Map map = new HashMap();
        List<RoomDTO> roomDTOs = new ArrayList<RoomDTO>();
        roomDTOs.add(new RoomDTO("301",89));
        roomDTOs.add(new RoomDTO("302",42));
        roomDTOs.add(new RoomDTO("303",46));
        roomDTOs.add(new RoomDTO("304",41));
        roomDTOs.add(new RoomDTO("305",41));
        roomDTOs.add(new RoomDTO("306",46));
        roomDTOs.add(new RoomDTO("307",45));
        roomDTOs.add(new RoomDTO("308",6));
        roomDTOs.add(new RoomDTO("309",42));
        roomDTOs.add(new RoomDTO("自习室",23));
        roomDTOs.add(new RoomDTO("总计",156));
        map.put("rows",roomDTOs);
        return map;
    }

    @RequestMapping("/selOneRoomList")
    public @ResponseBody Map selOneRoomList(String roomId) {
        Map map = new HashMap();
        List<ClassDTO> classDTOs = new ArrayList<ClassDTO>();
        classDTOs.add(new ClassDTO("张秦风","三年（2）班","女"));
        classDTOs.add(new ClassDTO("黄玉山","三年（2）班","男"));
        classDTOs.add(new ClassDTO("张薇薇","三年（2）班","女"));
        classDTOs.add(new ClassDTO("秦海新","三年（2）班","女"));
        classDTOs.add(new ClassDTO("江巧巧","三年（2）班","女"));
        classDTOs.add(new ClassDTO("陈绮","三年（2）班","男"));
        classDTOs.add(new ClassDTO("张艺多","三年（2）班","女"));
        classDTOs.add(new ClassDTO("金梦蝶","三年（2）班","女"));
        classDTOs.add(new ClassDTO("江多燕","三年（2）班","女"));
        classDTOs.add(new ClassDTO("林菲","三年（2）班","女"));
        classDTOs.add(new ClassDTO("李彦","三年（2）班","女"));
        classDTOs.add(new ClassDTO("程一飞","三年（2）班","男"));
        classDTOs.add(new ClassDTO("郭德烟","三年（2）班","女"));
        classDTOs.add(new ClassDTO("姜海陆","三年（2）班","女"));
        classDTOs.add(new ClassDTO("马哲涛","三年（2）班","男"));
        classDTOs.add(new ClassDTO("许易鹏","三年（2）班","男"));
        classDTOs.add(new ClassDTO("王雪飞","三年（2）班","女"));
        classDTOs.add(new ClassDTO("孙灿灿","三年（2）班","女"));
        classDTOs.add(new ClassDTO("林响","三年（2）班","男"));
        classDTOs.add(new ClassDTO("金源","三年（2）班","女"));
        classDTOs.add(new ClassDTO("陈宇","三年（2）班","男"));
        classDTOs.add(new ClassDTO("华晨","三年（2）班","男"));
        classDTOs.add(new ClassDTO("李海波","三年（2）班","男"));
        classDTOs.add(new ClassDTO("黄丽琳","三年（2）班","女"));
        classDTOs.add(new ClassDTO("施海燕","三年（2）班","女"));
        classDTOs.add(new ClassDTO("何飞飞","三年（2）班","男"));
        classDTOs.add(new ClassDTO("徐鑫","三年（2）班","男"));
        classDTOs.add(new ClassDTO("张小辰","三年（2）班","女"));
        classDTOs.add(new ClassDTO("马艳琳","三年（2）班","女"));
        classDTOs.add(new ClassDTO("费绮","三年（2）班","女"));
        classDTOs.add(new ClassDTO("宗雅","三年（2）班","女"));
        classDTOs.add(new ClassDTO("黄志分","三年（2）班","男"));
        map.put("rows", classDTOs);
        return map;
    }
}
