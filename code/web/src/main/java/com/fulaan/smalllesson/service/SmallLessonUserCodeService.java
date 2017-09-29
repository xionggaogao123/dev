package com.fulaan.smalllesson.service;

import com.db.smalllesson.SmallLessonUserCodeDao;
import com.fulaan.smalllesson.dto.SmallLessonUserCodeDTO;
import com.fulaan.util.QRUtils;
import com.pojo.smalllesson.SmallLessonUserCodeEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by scott on 2017/9/28.
 */
@Service
public class SmallLessonUserCodeService {

    public char[] chars={'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
    'o','p','q','r','s','t','u','v','w','x','y','z'};


    SmallLessonUserCodeDao smallLessonUserCodeDao=new SmallLessonUserCodeDao();

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
