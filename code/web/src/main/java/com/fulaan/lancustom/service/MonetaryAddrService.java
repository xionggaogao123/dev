package com.fulaan.lancustom.service;

import cn.jiguang.commom.utils.StringUtils;
import com.db.lancustom.MonetaryAddrDao;
import com.fulaan.lancustom.dto.MonetaryAddrDto;
import com.pojo.lancustom.MonetaryAddrEntry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: taotao.chan
 * @Date: 2018/8/7 11:28
 * @Description:
 */
@Service
public class MonetaryAddrService {

    private MonetaryAddrDao dao = new MonetaryAddrDao();
    /**
     * 保存地址，分新增和修改
     * @param monetaryAddrDto
     * @param userId
     * @throws Exception
     */
    public void saveMonetaryAddr(MonetaryAddrDto monetaryAddrDto, ObjectId userId) throws Exception {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(monetaryAddrDto.getTelphone());
        if (m.matches()) {
            MonetaryAddrEntry entry = new MonetaryAddrEntry(monetaryAddrDto.getName(), monetaryAddrDto.getTelphone(), monetaryAddrDto.getArea(), monetaryAddrDto.getDetail(), userId);
//            if (StringUtils.isNotEmpty(monetaryAddrDto.getId())) {//逻辑删除以前的地址
//                dao.updateMonetaryAddr(new ObjectId(monetaryAddrDto.getId()), monetaryAddrDto.getArea(), monetaryAddrDto.getDetail(), monetaryAddrDto.getName(), monetaryAddrDto.getTelphone(), userId.toString());
//            }
            dao.updateMonetaryAddr(new ObjectId(monetaryAddrDto.getId()), monetaryAddrDto.getArea(), monetaryAddrDto.getDetail(), monetaryAddrDto.getName(), monetaryAddrDto.getTelphone(), userId.toString());
            dao.addEntry(entry);
        } else {
            throw new Exception("请输入正确的手机号码!");
        }
    }

    /**
     * 获取用户地址信息
     * @param userId
     * @return
     */
    public List<MonetaryAddrDto> getUserAddrsList(ObjectId userId) {
        List<MonetaryAddrEntry> list = dao.getUserAddrsList(userId);
        List<MonetaryAddrDto> dtoList = new ArrayList<MonetaryAddrDto>();
        for (MonetaryAddrEntry entry : list) {
            MonetaryAddrDto dto = new MonetaryAddrDto(entry);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
