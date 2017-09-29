package com.fulaan.smalllesson.service;

import com.db.smalllesson.SmallLessonCodeDao;
import com.db.smalllesson.SmallLessonUserCodeDao;
import com.fulaan.smalllesson.dto.SmallLessonUserCodeDTO;
import com.fulaan.util.QRUtils;
import com.pojo.smalllesson.SmallLessonCodeEntry;
import com.pojo.smalllesson.SmallLessonUserCodeEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by scott on 2017/9/28.
 */
@Service
public class SmallLessonUserCodeService {

    public char[] chars={'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
    'o','p','q','r','s','t','u','v','w','x','y','z'};


    SmallLessonUserCodeDao smallLessonUserCodeDao=new SmallLessonUserCodeDao();

    SmallLessonCodeDao smallLessonCodeDao = new SmallLessonCodeDao();

    /**
     * 获取课程对应的二维码以及课程编码code
     * @param lessonId
     * @return
     */
    public Map<String,String> getSmallLessonCode(ObjectId lessonId){
        Map<String,String> retMap=new HashMap<String, String>();
        String qrUrl= QRUtils.getSmallLessonUserCodeQrUrl(lessonId);
        retMap.put("qrUrl",qrUrl);
        //生成code
        String code="";
        boolean flag=true;
        while(flag) {
            code= generateNum(6);
            SmallLessonCodeEntry codeEntry=smallLessonCodeDao.getCodeEntry(code);
            if(null==codeEntry){
                flag=false;
            }
        }
        SmallLessonCodeEntry entry=new SmallLessonCodeEntry(code);
        smallLessonCodeDao.saveSmallLessonCodeEntry(entry);
        retMap.put("code",code);
        return retMap;
    }

    /**
     * 下课时删除对应的课程编码
     * @param code
     */
    public void removeSmallLessonCode(String code){
        smallLessonCodeDao.removeSmallLessonCodeEntry(code);
    }

    public SmallLessonUserCodeDTO getDto(ObjectId userId){
        SmallLessonUserCodeEntry entry=smallLessonUserCodeDao.getEntryByUserId(userId);
        String code="";
        if(null!=entry){
            return new SmallLessonUserCodeDTO(entry);
        }else{
            boolean flag=true;
            while (flag){
                code=generateCode(1);
                SmallLessonUserCodeEntry codeEntry=smallLessonUserCodeDao.getEntryByCode(code);
                if(null!=codeEntry){
                    code=generateCode(2);
                    SmallLessonUserCodeEntry entryByCode=smallLessonUserCodeDao.getEntryByCode(code);
                    if(null==entryByCode) {
                        flag = false;
                    }
                }else{
                    flag = false;
                }
            }
            String qrUrl= QRUtils.getSmallLessonUserCodeQrUrl(userId);
            SmallLessonUserCodeEntry userCodeEntry=new SmallLessonUserCodeEntry(userId,qrUrl,code);
            smallLessonUserCodeDao.saveSmallLessonUserCodeEntry(userCodeEntry);
            return new SmallLessonUserCodeDTO(userCodeEntry);
        }
    }



    public SmallLessonUserCodeDTO getDtoByCode(String code){
        SmallLessonUserCodeEntry codeEntry=smallLessonUserCodeDao.getEntryByCode(code);
        if(codeEntry != null){
            return new SmallLessonUserCodeDTO(codeEntry);
        }
        return null;

    }



    public String generateCode(int type){
        int max=6;
        int min=0;
        Random random = new Random();
        int argNum = random.nextInt(max)%(max-min+1) + min;
        int num=6-argNum;
        String argStr=generateArg(argNum);
        String numStr=generateNum(num);
        if(type==1) {
                return argStr + numStr;
        }else{
            return numStr + argStr;
        }
    }

    public  String generateArg(int num){
        String str="";
        int max=chars.length;
        int min=0;
        Random random = new Random();
        for(int i=0;i<num;i++){
            int n=random.nextInt(max)%(max-min+1) + min;
            char c=chars[n];
            str+=c;
        }
        return str;
    }

    public String generateNum(int num){
        String str="";
        int max=9;
        int min=0;
        Random random = new Random();
        for(int i=0;i<num;i++){
            int n=random.nextInt(max)%(max-min+1) + min;
            str+=n;
        }
        return str;
    }
}
