package com.fulaan.pet.service;

import com.db.pet.PetTypeDao;
import com.db.pet.UserPetDao;
import com.db.user.UserDao;
import com.db.user.UserExperienceLogDao;
import com.fulaan.pet.dto.PetDTO;
import com.fulaan.pet.dto.UserPetDTO;
import com.pojo.pet.PetInfo;
import com.pojo.pet.PetTypeEntry;
import com.pojo.pet.UserPetEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserExperienceLogEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PetService {

    UserDao userDao=new UserDao();
    UserPetDao userPetDao=new UserPetDao();

    /**
     * 给用户添加宠物信息
     * @param userid
     * @return Map
     */
	public Map addPet(ObjectId userid) {
		Map map = new HashMap();
		int experience = getExperience(userid);
        //判断用户经验值是否>=50,经验值>=50才可以添加宠物
		if (experience>=50) {
            //获取用户宠物数量
            int userPetCount=selPetCount(userid);
            //判断用户是否已经存在宠物
            if(userPetCount>0) {//已经存在宠物
                //判断用户的经验值是否满足再次添加宠物蛋
                if (experience >= (userPetCount * 100 + 50)) {//用户的经验值满足再次添加宠物蛋
                    PetDTO petDTO = new PetDTO();
                    petDTO.setSelecttype(0);
                    petDTO.setCreatedate(new Date());
                    petDTO.setIshatch(0);
                    //给用户再添加一个宠物蛋信息
                    userPetDao.addPetEntry2User(userid, petDTO.buildPetEntryAdd());
                    map.put("resultCode", 0);
                } else {//用户的经验值不满足再次添加宠物蛋
                    map.put("resultCode", 1);
                    map.put("experience", userPetCount * 100 + 50);
                }
            } else {//不存在宠物
                UserPetDTO userPetDTO = new UserPetDTO();
                userPetDTO.setUserid(userid.toString());
                List<PetInfo> pets=new ArrayList<PetInfo>();
                PetDTO petDTO = new PetDTO();
                petDTO.setSelecttype(0);
                petDTO.setCreatedate(new Date());
                petDTO.setIshatch(0);
                pets.add(petDTO.buildPetEntryAdd());
                UserPetEntry userPetEntry=userPetDTO.buildUserPetEntry();
                userPetEntry.setPetInfos(pets);
                //给用户再添加一个宠物蛋信息
                userPetDao.addUserPetEntry(userPetEntry);
                map.put("resultCode", 0);
            }
		} else {
			map.put("resultCode", 1);
			map.put("experience", 50);
		}
		
		return map;
	}

    /**
     * 判断用户是否有宠物蛋
     * @param userid
     * @return Map
     */
	public Map getIsPetEgg(ObjectId userid) {
		Map map = new HashMap();
        //根据用户经验值计算出用户是否满足领取一个宠物蛋
		int petcount = getExperience(userid)/ 50;
        if (petcount>1) {
			int count =  (int) Math.ceil((float)petcount/2);
			if (count > selPetCount(userid)) {
				map.put("isflag", 1);
			} else {
				map.put("isflag", 0);
			}
		} else {
			map.put("isflag", 0);
		}
		return map;
	}

    /**
     * 获取用户所有宠物信息
     * @param userid
     * @return Map
     */
	public Map selAllPet(ObjectId userid) {
		Map map = new HashMap();
        //查询用户宠物信息
        UserPetEntry userPetEntry=userPetDao.findUserPet(userid);
        //存放宠物蛋信息集合
        List<PetDTO> notHatchList = new ArrayList<PetDTO>();
        //存放孵化后的宠物信息集合
        List<PetDTO> hatchList =new ArrayList<PetDTO>();
        //获取全部的宠物类型
        Map<String,PetTypeEntry> petTypeMap=selPetTypeList();
        if(userPetEntry!=null) {
            for (PetInfo petInfo : userPetEntry.getPetInfos()) {
                PetDTO pet = new PetDTO(petInfo);
                //判断是否是宠物蛋
                if (0 == petInfo.getIshatch()) {
                    //将宠物蛋存放宠物蛋信息集合
                    notHatchList.add(pet);
                }
                //判断是否孵化后的宠物
                if (1 == petInfo.getIshatch()) {
                    PetTypeEntry petType = petTypeMap.get(pet.getPetid());
                    //pet.setPetname(petType.getPetname());
                    pet.setPetimage(petType.getPetimage());
                    pet.setPetexplain(petType.getPetexplain());
                    pet.setMaxpetimage(petType.getMaxpetimage());
                    pet.setMiddlepetimage(petType.getMiddlepetimage());
                    pet.setMinpetimage(petType.getMinpetimage());
                    //将宠物存放孵化后的宠物信息集合
                    hatchList.add(pet);
                }
            }
        }
		map.put("experience", getExperience(userid));
        map.put("notHatchList", notHatchList);
		map.put("hatchList", hatchList);
		return map;
	}

    /**
     * 修改宠物名称
     * @param userid
     * @param petEntryId
     * @param newPetName
     */
	public void updatePetName(ObjectId userid,ObjectId petEntryId,String newPetName) {
        //修改宠物名称
        userPetDao.updatePetName(userid, petEntryId, newPetName);
	}
    /**
     * 获取用户选择的宠物信息
     * @param userid
     * @return PetDTO
     */
	public PetDTO selectedPet(ObjectId userid) {
        int selecttype=1;
        UserPetDTO userPetDTO=selPetList(userid, selecttype);
        List<PetDTO> pets=userPetDTO.getPets();
        if(pets!=null&&pets.size()>0){
            return pets.get(0);
        }
		return null;
	}

    /**
     * 获取用户所有未选中宠物信息
     * @param userid
     * @return UserPetDTO
     */
    public UserPetDTO selAllPetList(ObjectId userid) {
        int selecttype=0;
        UserPetDTO userPetDTO=selPetList(userid, selecttype);
        return userPetDTO;
    }

    /**
     * 根据条件获取用户所有宠物信息
     * @param userid
     * @param selecttype
     * @return
     */
    public UserPetDTO selPetList(ObjectId userid,int selecttype){
        //根据条件获取用户所有宠物信息
        UserPetEntry userPetEntry=userPetDao.selPetByParam(userid, selecttype);
        UserPetDTO userPetDTO=new UserPetDTO(userPetEntry);
        //获取用户所有宠物类型信息
        Map<String,PetTypeEntry> map=selPetTypeList();
        List<PetDTO> pets=userPetDTO.getPets();
        if(pets!=null&&pets.size()>0) {
            for (PetDTO pet : userPetDTO.getPets()) {
                PetTypeEntry petType = map.get(pet.getPetid());
                //pet.setPetname(petType.getPetname());
                pet.setPetimage(petType.getPetimage());
                pet.setPetexplain(petType.getPetexplain());
                pet.setMaxpetimage(petType.getMaxpetimage());
                pet.setMiddlepetimage(petType.getMiddlepetimage());
                pet.setMinpetimage(petType.getMinpetimage());
            }
        }
        return userPetDTO;
    }
    /**
     * 获取用户所有宠物类型信息
     * @return Map<String,PetTypeEntry>
     */
    public Map<String,PetTypeEntry> selPetTypeList(){
        PetTypeDao petTypeDao=new PetTypeDao();
        List<PetTypeEntry> pets=petTypeDao.selAllPetType();
        Map<String,PetTypeEntry> map=new HashMap<String, PetTypeEntry>();
        for(PetTypeEntry petType:pets){
            map.put(petType.getID().toString(),petType);
        }
        return map;
    }

    /**
     * 选取取用户宠物
     * @param userid
     * @param petEntryId
     * @return
     */
    public void chooseMyPet(ObjectId userid,ObjectId petEntryId) {
        //将所有的宠物置为未选中
        userPetDao.updatepetNotSelectType(userid);
        //将id等于petEntryId的宠物置为选中
        userPetDao.updatePetSelectType(userid,petEntryId);
    }

    /**
     * 获取用户经验值
     * @param userid
     * @return
     */
    public int getExperience(ObjectId userid) {
        //获取用户信息
        UserEntry userEntry = userDao.getUserEntry(userid, Constant.FIELDS);
        return userEntry.getExperiencevalue();
    }
    /**
     * 获取用户宠物数量
     * @param userid
     * @return
     */
    public int selPetCount(ObjectId userid) {
        return userPetDao.selPetCount(userid);
    }
    /**
     * 获取用户孵化宠物数量
     * @param userid
     * @return
     */
    public int selHatchPetCount(ObjectId userid) {
        return userPetDao.selHatchPetCount(userid);
    }

    /**
     * 获取学生用户宠物
     * @param studentId
     * @return
     */
    public Object selectedStudentPet(ObjectId studentId) {
        return selectedPet(studentId);
    }
    /**
     * 孵化宠物宠物蛋
     * @param id
     * @param userid
     * @return
     */
	public Map hatchPet(ObjectId id,ObjectId userid) {
		Map map = new HashMap();
        //获取要孵化的宠物信息
        UserPetEntry userpet = userPetDao.selHatchPetByIdCount(userid,id);
        for(PetInfo petInfo :userpet.getPetInfos()){
            //判断宠物蛋时候已经孵化
            if(1== petInfo.getIshatch()){
                map.put("resultCode",1);
                map.put("mesg", "该宠物已孵化！");
                return map;
            }
        }
        //获取用户选中的宠物数量
		int count = userPetDao.selAllPetByUserIdCount(userid, 1);
        //根据用户经验值判断用户是否可以孵化宠物蛋
		int petcount = getExperience(userid)/100;
		if (petcount>=1) {
			if (petcount>count) {
                PetInfo petInfo=userpet.getPetInfos().get(0);

                String startDate="2015-07-10 00:00:00";
                String endDate="2015-09-30 23:59:59";
                DateTimeUtils time=new DateTimeUtils();
                long dsl=time.getStrToLongTime(startDate);
                long del=time.getStrToLongTime(endDate);
                //随机获取一个宠物类型信息
                PetTypeEntry pet = getRandomNotActivityPet();
                if (petInfo.getCreatedate()>= dsl&& petInfo.getCreatedate()<=del) {
                    UserExperienceLogDao userExpLogDao=new UserExperienceLogDao();
                    List<UserExperienceLogEntry.ExperienceLog> expList = userExpLogDao.getExperienceLogList(userid, dsl,del);
                    int totalExp = 0;
                    for (UserExperienceLogEntry.ExperienceLog expLog : expList) {
                        //计算出今日增减积分总和
                        totalExp += expLog.getExperience();
                    }
                    if (totalExp>=50) {
                        pet = getPetByEnName("monkey");
                    }

                }
                //获取用户选中宠物的数量
				int selectCount = userPetDao.selMyPetByUserIdCount(userid);
                int selecttype=0;
				if (selectCount==0) {
                    selecttype=1;
				}
                PetDTO petDTO = new PetDTO();
                petDTO.setId(id.toString());
                petDTO.setPetid(pet.getID().toString());
                petDTO.setPetname(pet.getPetname());
                petDTO.setSelecttype(selecttype);
                petDTO.setUpdatedate(new Date());
                petDTO.setIshatch(1);
                //将宠物蛋信息修改为宠物信息
                userPetDao.updateUserPetInfo(userid, petDTO.buildPetEntryUpd());
				map.put("resultCode",0);
				map.put("petname", pet.getPetname());
				map.put("petimage", pet.getMiddlepetimage());
			} else {
				map.put("resultCode",1);
				map.put("experience",(count+1)*100);
			}
		} else {
			map.put("resultCode",1);
			map.put("experience",100);
		}
		return map;
	}

    /**
     * 随机获取一个宠物类型信息
     * @return
     */
    public PetTypeEntry getRandomNotActivityPet() {
        PetTypeDao petTypeDao=new PetTypeDao();
        //获取所有的宠物类型信息
        List<PetTypeEntry> pets=petTypeDao.getNotActivityPet();
        if(null!=pets&&!pets.isEmpty()){
            //取得宠物类型的数目
            int size=pets.size();
            //在宠物类型数目内产生一个随机数
            Random random = new Random();
            int index = random.nextInt(size);
            //返回随机获取的宠物类型
            return pets.get(index);
        }
        return null;
    }
    /**
     * 指定宠物类型信息
     * @return
     */
    public PetTypeEntry getPetByEnName(String enName) {
        PetTypeDao petTypeDao=new PetTypeDao();
        //获取所有的宠物类型信息
        PetTypeEntry pet=petTypeDao.getPetByEnName(enName);
        return pet;
    }

    /**
     * 获取非展示宠物信息
     * @return
     */
    public List<PetDTO> getShowPet() {
        List<PetDTO> list =new ArrayList<PetDTO>();
        PetTypeDao petTypeDao=new PetTypeDao();
        //获取所有的宠物类型信息
        List<PetTypeEntry> pets=petTypeDao.getShowPet();
        for(PetTypeEntry entry: pets){
            PetDTO dto=new PetDTO();
            //dto.setPetname(entry.getPetname());
            //dto.setPetimage(entry.getPetimage());
            //dto.setPetexplain(entry.getPetexplain());
            //dto.setMaxpetimage(entry.getMaxpetimage());
            //dto.setMiddlepetimage(entry.getMiddlepetimage());
            dto.setMinpetimage(entry.getMinpetimage());
            list.add(dto);
        }
        return list;
    }


}
