package com.fulaan.pet.contraller;

import com.fulaan.base.controller.BaseController;
import com.fulaan.pet.service.PetService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pet")
public class PetController extends BaseController {

     @Autowired
	 private PetService petService;

    /**
     * 宠物背包
     * @param model
     * @return
     */
	 @RequestMapping("/petbag")
	 public String petBagPage(Map<String, Object> model) {
         //获取用户当前选择的宠物信息
		 model.put("userpet", petService.selectedPet(getUserId()));
         //获取用户的宠物数量
         model.put("petCount",petService.selPetCount(getUserId()));
         //获取用户的经验值
		 model.put("experience", petService.getExperience(getUserId()));
		 return "petstore/petbag";
	 }

    /**
     * 宠物中心
     * @param model
     * @return
     */
	 @RequestMapping("/petCenter")
	 public String petCenter(Map<String, Object> model) {
         model.put("pets", petService.getShowPet());
         //获取用户当前选择的宠物信息
		 model.put("userpet", petService.selectedPet(getUserId()));
         //获取用户的宠物数量
         model.put("petCount",petService.selPetCount(getUserId()));
         //获取用户的经验值
		 model.put("experience", petService.getExperience(getUserId()));
		 return "petstore/petcenter";
	 }

    /**
     * 增加宠物
     * @return
     */
    @RequestMapping("/addPet")
    public @ResponseBody  Map addPet() {
        Map map = new HashMap();
        try {
            map = petService.addPet(getUserId());
        } catch(Exception e) {
            map.put("resultCode", 2);
        }
        return map;
    }

    /**
     * 判断用户是否有宠物蛋
     * @return Map
     */
    @RequestMapping("/getIsPetEgg")
    public @ResponseBody
    Map getIsPetEgg() {
        return petService.getIsPetEgg(getUserId());
    }

    /**
     * 获取用户所有宠物信息
     * @return Map
     */
    @RequestMapping("/selAllPet")
    public @ResponseBody
    Map selAllPet() {
        return petService.selAllPet(getUserId());
    }

    /**
     * 获取用户选择的宠物信息
     * @return Map
     */
    @RequestMapping("/selectedPet")
    public @ResponseBody
    Map selectedPet() {
        Map map = new HashMap();
        map.put("petInfo", petService.selectedPet(getUserId()));
        return map;
    }
    /**
     * 获取学生用户选择的宠物信息
     * @param studentId
     * @return Map
     */
    @RequestMapping("/selectedStudentPet")
    public @ResponseBody
    Map selectedStudentPet(@RequestParam("studentId") ObjectId studentId) {
        Map map = new HashMap();
        map.put("petInfo", petService.selectedStudentPet(studentId));
        return map;
    }

    /**
     * 修改宠物名称
     * @param id
     * @param petname
     * @return Map
     */
    @RequestMapping("/updatePetName")
    public @ResponseBody
    Map updatePetName(@RequestParam("id") ObjectId id,@RequestParam("petname") String petname) {
        Map map = new HashMap();
        try {
            petService.updatePetName(getUserId(),id,petname);
            map.put("resultCode", 0);
        }catch (Exception e) {
            map.put("resultCode", 1);
        }
        return map;
    }

    /**
     * 孵化宠物宠物蛋
     * @param id
     * @return Map
     */
    @RequestMapping("/hatchPet")
    public @ResponseBody
    Map hatchPet(@RequestParam("id") ObjectId id) {
        Map map = new HashMap();
        try {
            map = petService.hatchPet(id,getUserId());
        }catch(Exception e) {
            map.put("resultCode", 1);
            map.put("mesg","该宠物孵化失败！请重新孵化！");
        }
        return map;
    }

    /**
     * 选取用户宠物
     * @param id
     * @return
     */
    @RequestMapping("/chooseMyPet")
    public @ResponseBody
    Map chooseMyPet(@RequestParam("id") ObjectId id) {
        Map map = new HashMap();
        try {
            petService.chooseMyPet(getUserId(),id);
            map.put("resultCode", 0);
        }catch(Exception e) {
            map.put("resultCode", 1);
            map.put("mesg","更换宠物失败，请重新更换！");
        }
        return map;
    }
}
