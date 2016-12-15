package com.fulaan.mall.service;

import com.db.ebusiness.EVoucherDao;
import com.db.user.UserDao;
import com.pojo.ebusiness.EVoucherDTO;
import com.pojo.ebusiness.EVoucherEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fl on 2016/3/7.
 */
@Service
public class EBusinessVoucherService {
    private EVoucherDao eVoucherDao = new EVoucherDao();
    private UserDao userDao = new UserDao();
    //抵用券状态
    private static final int UNUSED = 0;
    private static final int USED = 1;
    private static final int EXPIRATION = 2;

    /**
     * 添加和编辑
     *
     * @param eVoucherEntry
     * @return
     */
    public ObjectId addEVoucher(EVoucherEntry eVoucherEntry) {
        return eVoucherDao.add(eVoucherEntry);
    }

    /**
     * 批量添加，用于管理员
     *
     * @param eVoucherEntries
     * @return
     */
    public Boolean addEVouchers(List<EVoucherEntry> eVoucherEntries) {
        return eVoucherDao.add(eVoucherEntries);
    }

    public List<EVoucherDTO> getEVouchers(int page, int pageSize) {
        List<EVoucherDTO> eVoucherDTOs = new ArrayList<EVoucherDTO>();
        List<EVoucherEntry> eVoucherEntries = eVoucherDao.getAllEVouchers((page - 1) * pageSize, pageSize);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (EVoucherEntry eVoucherEntry : eVoucherEntries) {
            userIds.add(eVoucherEntry.getUserId());
        }
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        for (EVoucherEntry eVoucherEntry : eVoucherEntries) {
            EVoucherDTO eVoucherDTO = new EVoucherDTO(eVoucherEntry);
            UserEntry userEntry = userEntryMap.get(eVoucherEntry.getUserId());
            String userName = userEntry == null ? "匿名" : userEntry.getUserName();
            eVoucherDTO.setUserName(userName);
            eVoucherDTOs.add(eVoucherDTO);
            userIds.add(eVoucherEntry.getUserId());
        }
        return eVoucherDTOs;
    }

    public int countVouchers() {
        return eVoucherDao.countEVouchers();
    }

    /**
     * 用户点击领取生成抵用券
     *
     * @param userId
     * @return
     */
    public ObjectId addEVoucher(ObjectId userId, int amount) {
        String num = String.valueOf(System.currentTimeMillis());
        num += RandomUtils.nextInt(Constant.MIN_PASSWORD);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 30);
        Long expTime = DateTimeUtils.getDayMaxTime(calendar.getTimeInMillis());
        EVoucherEntry eVoucherEntry = new EVoucherEntry(userId, num, amount, expTime, 0);
        return eVoucherDao.add(eVoucherEntry);
    }

    /**
     * 根据用户Id查询该用户的优惠券数
     */
    public int countEVouchersByUserId(ObjectId userId) {
        return eVoucherDao.countEVouchersByUserId(userId);
    }

    /**
     * 用户活动领取生成优惠券
     */
    public Boolean addActivityEVoucher(List<EVoucherEntry> eVoucherEntries) {
        return eVoucherDao.add(eVoucherEntries);
    }

    /**
     * 用户给账户充值抵用券
     *
     * @param userId
     * @param voucherNo
     * @return
     * @throws Exception
     */
    public ObjectId userRechargeVoucherToMyVoucherAccount(ObjectId userId, String voucherNo) throws Exception {
        EVoucherEntry voucherEntry = eVoucherDao.getEVoucherEntryByNo(voucherNo);
        if (voucherEntry == null) {
            throw new Exception("券号错误");
        }
        if (voucherEntry.getUserId() != null) {
            throw new Exception("抵用券已被充值");
        }
        if (EXPIRATION == voucherEntry.getState()) {
            throw new Exception("抵用券已过期");
        }
        if (voucherEntry.getState() == 5) {
            throw new Exception("抵用券已删除");
        }
        voucherEntry.setUserId(userId);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 30);
        Long expTime = DateTimeUtils.getDayMaxTime(calendar.getTimeInMillis());
        voucherEntry.setExpTime(expTime);
        voucherEntry.setState(UNUSED);

        eVoucherDao.add(voucherEntry);
        return null;
    }

    /**
     * 抵用券详情
     *
     * @param eVoucherId
     * @return
     */
    public EVoucherDTO getEVoucher(ObjectId eVoucherId) {
        EVoucherEntry eVoucherEntry = eVoucherDao.getEVoucherEntry(eVoucherId);
        if (eVoucherEntry != null) {
            return new EVoucherDTO(eVoucherEntry);
        }
        return null;
    }

    public EVoucherDTO getUnUseEVoucher(ObjectId eVoucherId) {
        EVoucherEntry eVoucherEntry = eVoucherDao.getUnUseEVoucherEntry(eVoucherId);
        if (eVoucherEntry != null) {
            return new EVoucherDTO(eVoucherEntry);
        }
        return null;
    }


    /**
     * 得到用户抵用券
     *
     * @param userId
     * @return
     */
    public List<EVoucherDTO> getEVouchersByUserIdAndState(ObjectId userId, String state) {
        List<EVoucherEntry> eVoucherEntries = eVoucherDao.getEVoucherEntrysByUserIdAndState(userId, getIntStateByStringStateForVoucher(state));
        List<EVoucherDTO> eVoucherDTOs = new ArrayList<EVoucherDTO>();
        if (eVoucherEntries != null && eVoucherEntries.size() > 0) {
            for (EVoucherEntry eVoucherEntry : eVoucherEntries) {
                eVoucherDTOs.add(new EVoucherDTO(eVoucherEntry));
            }
        }
        return eVoucherDTOs;
    }

    private int getIntStateByStringStateForVoucher(String stringState) {
        if (stringState.equals("unused")) {
            return UNUSED;
        } else if (stringState.equals("used")) {
            return USED;
        } else if (stringState.equals("expiration")) {
            return EXPIRATION;
        }
        return UNUSED;
    }

    public void updateVoucherState(ObjectId voucherId, int state) throws Exception {
        EVoucherEntry eVoucherEntry = eVoucherDao.getEVoucherEntry(voucherId);
        if (eVoucherEntry.getState() == 5) {
            throw new Exception("抵用券已被删除，不可更改状态");
        }
        eVoucherEntry.setState(state);
        eVoucherDao.add(eVoucherEntry);

    }

}
