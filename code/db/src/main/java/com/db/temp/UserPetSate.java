package com.db.temp;

import com.db.pet.PetTypeDao;
import com.db.pet.UserPetDao;
import com.db.user.UserDao;
import com.pojo.pet.PetInfo;
import com.pojo.pet.PetTypeEntry;
import com.pojo.pet.UserPetEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by guojing on 2016/4/1.
 */
public class UserPetSate {
    public static void main(String[] args) {
        UserDao userDao=new UserDao();
        PetTypeDao petTypeDao=new PetTypeDao();
        UserPetDao userPetDao=new UserPetDao();

        Set<String> names=new HashSet<String>();
        names.add("唐誉清7");names.add("谢巧"); names.add("谢帝");names.add("胡溢昕");
        names.add("李嘉豪8");names.add("王太宇2"); names.add("徐方圆1");names.add("张静雯18");
        names.add("张金海");names.add("王靖松"); names.add("王明栓");names.add("孙国鑫");
        names.add("苗华星");names.add("王志斌"); names.add("孙浩53");names.add("戴兴龙");
        names.add("武禧龙");names.add("王宇恒2"); names.add("钮伟霖");names.add("沈南南");
        names.add("闻菲");names.add("卢红宇"); names.add("郑胜南");names.add("王昊翔");
        names.add("张志豪29");names.add("涂昊3"); names.add("马迪7");names.add("程迪迪1");
        names.add("刘朝辉1");names.add("王博文48"); names.add("杜福雷");names.add("田乐宁");
        names.add("赵琦3");names.add("张飞祥"); names.add("刘爽爽");names.add("王海天");
        names.add("杨威龙");names.add("王星宇11"); names.add("朱广亮");names.add("徐静茹3");
        names.add("张启月1");names.add("张书睿"); names.add("朱越7");names.add("俞玟桐");
        names.add("赵明阳1");names.add("王宏坤"); names.add("左乐心");names.add("徐柯莹");
        names.add("王珂25");names.add("季豪1"); names.add("何宜平");names.add("刘玥盈");
        names.add("方兴邦");names.add("高璇5"); names.add("常昀");names.add("胡睿20");
        names.add("杨立齐");names.add("蒋成燕"); names.add("王旭东32");names.add("黄墨涵17");
        names.add("张济航");names.add("孙夏阳23"); names.add("曹文森");names.add("葛奕曼15");
        names.add("陈昕羽19");names.add("舒奕轩"); names.add("姚雅沁19");names.add("周弘道5");
        names.add("胡佳睿9");names.add("邹文心28"); names.add("王菡童25");names.add("韩昊轩");
        names.add("奚宇喆9");names.add("沈裕棠"); names.add("吴恩祈10");names.add("嵇琳14");
        names.add("徐欣宁23");names.add("孙文希26"); names.add("魏国申9");names.add("马瑜汝18");
        names.add("陈昕羽19");names.add("覃昕晔19"); names.add("张智杰7");names.add("王梓瑶2");
        names.add("刘诗妤1");names.add("俞之洋5"); names.add("胡佳辰1");

        List<ObjectId> userIds = userDao.getUserIdsByUserName(names);
        String enName="adorable_monkey";
        //获取所有的宠物类型信息
        PetTypeEntry pettype=petTypeDao.getPetByEnName(enName);
        PetInfo petInfo =new PetInfo(
                pettype.getID(),
                pettype.getPetname(),
                0,//是否是当前宠物
                new Date().getTime(),
                1,//已孵化
                new Date().getTime());

        System.out.println("************************给用户送新年萌宠开始" + DateTimeUtils.getCurrDate(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H) + "*******************************");
        for(ObjectId userId:userIds){
            //获取用户宠物数量
            int userPetCount=userPetDao.selPetCount(userId);
            //判断用户是否已经存在宠物
            if(userPetCount>0) {//已经存在宠物
                UserPetEntry userPet=userPetDao.findUserPet(userId);
                List<PetInfo> pets=userPet.getPetInfos();
                boolean isExist=true;
                for(PetInfo pet:pets){
                    if(pet.getIshatch()==1) {
                        if (pet.getPetid().equals(petInfo.getPetid())) {
                            //给用户再添加一个宠物蛋信息
                            isExist=false;
                        }
                    }
                }
                if(isExist){
                    //将所有的宠物置为未选中
                    userPetDao.updatepetNotSelectType(userId);
                    userPetDao.addPetEntry2User(userId, petInfo);
                }
            } else {//不存在宠物
                petInfo.setSelecttype(1);
                List<PetInfo> pets=new ArrayList<PetInfo>();
                pets.add(petInfo);
                UserPetEntry userPetEntry=new UserPetEntry(userId,pets);
                //给用户再添加一个宠物蛋信息
                userPetDao.addUserPetEntry(userPetEntry);
            }
        }
        System.out.println("************************给用户送新年萌宠结束" + DateTimeUtils.getCurrDate(DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H) + "*******************************");
    }
}
