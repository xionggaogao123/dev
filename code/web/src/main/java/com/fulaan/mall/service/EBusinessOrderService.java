package com.fulaan.mall.service;


import com.db.ebusiness.EGoodsDao;
import com.db.ebusiness.EOrderAddressDao;
import com.db.ebusiness.EOrderDao;
import com.db.ebusiness.EVoucherDao;
import com.db.user.UserDao;
import com.fulaan.experience.service.ExperienceService;
import com.mongodb.BasicDBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.ebusiness.*;
import com.pojo.ebusiness.EGoodsEntry.Kind;
import com.pojo.ebusiness.EOrderEntry.EOrderGoods;
import com.pojo.emarket.OrderState;
import com.pojo.user.ExpLogType;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

/**
 * 电子商务订单service
 *
 * @author fourer
 */
@Service
public class EBusinessOrderService {

    private static final Logger EBusinessLog = Logger.getLogger("EBusiness");
    private EOrderDao eOrderDao = new EOrderDao();
    private EGoodsDao eGoodsDao = new EGoodsDao();
    private EOrderAddressDao eOrderAddressDao = new EOrderAddressDao();
    private ExperienceService experienceService = new ExperienceService();
    private EVoucherDao eVoucherDao = new EVoucherDao();
    private UserDao userDao = new UserDao();
    private EExpressTemplateService expressTemplateService = new EExpressTemplateService();

    /**
     * 详情
     *
     * @param orderId
     * @return
     */
    public EOrderEntry getEOrderEntry(ObjectId orderId) {
        return eOrderDao.getEOrderEntry(orderId);
    }


    /**
     * 生成订单
     *
     * @param userId
     * @throws Exception
     */
    public EOrderEntry createOrder(ObjectId userId, List<EBusinessCartGoods> list, ObjectId addressId, int usedExp, ObjectId voucherId, String message, ObjectId parentId) throws Exception {
        Map<String, String> messageMap = getMessageMap(message);
        List<EOrderGoods> orderGoodsList = new ArrayList<EOrderEntry.EOrderGoods>();
        for (EBusinessCartGoods ecg : list) {
            String goodsMessage = messageMap.get(ecg.getId());
            EOrderGoods g = new EOrderGoods(new ObjectId(ecg.getId()), MongoUtils.convert(ecg.getObjKinds()), ecg.getPrice(), ecg.getCount(), goodsMessage, "", "");
            orderGoodsList.add(g);
        }
        int voucherOff = getVoucherOff(voucherId, orderGoodsList);
        EOrderAddressEntry addressEntry = eOrderAddressDao.getEOrderAddressEntry(addressId);
        int expressPrice = getExpressPrice(addressEntry, orderGoodsList);
        EOrderEntry eoe = new EOrderEntry(userId, addressId, 0, "", OrderState.READY.getType(), orderGoodsList, createOrderNumber(), usedExp, voucherId, voucherOff, expressPrice, parentId);

        ObjectId oId = eOrderDao.addEOrderEntry(eoe);
        EBusinessLog.info("OrderId=" + oId.toString());

        //todo 减积分   记录  更新抵用券状态
        if (eoe.getUsedExp() > 0) {
            ExpLogType expLogType = ExpLogType.MALL_USED;
            experienceService.updateScoreForMall(eoe.getUserId().toString(), expLogType, 0 - eoe.getUsedExp(), eoe.getID().toString());
        }

        if (voucherId != null) {
            EVoucherEntry eVoucherEntry = eVoucherDao.getEVoucherEntry(voucherId);
            eVoucherEntry.setState(1);
            eVoucherDao.add(eVoucherEntry);
        }
        return eoe;
    }

    /**
     * 直接购买商品列表(只有一个商品)
     */
    public List<EBusinessGoods> getGoods(EGoodsEntry goodsEntry, int count, String kinds) throws IllegalParamException {
        EBusinessGoods goods = new EBusinessGoods(goodsEntry, count);
        goods.setObjKinds(kinds);

        List<ObjectId> kindsList = MongoUtils.convert(kinds);
        StringBuilder kindInfo = new StringBuilder();
        List<Kind> kindList = goodsEntry.getKindList();
        for (Kind k : kindList) {
            kindInfo.append(k.getName()).append(":");
            for (EGoodsEntry.Spec ivp : k.getList()) {
                if (kindsList.contains(ivp.getSpecId())) {
                    kindInfo.append(ivp.getSpecName()).append("	");
                    goods.setPrice(goods.getPrice() + ivp.getSpecPrice());
                }
            }
        }
        goods.setKind(kindInfo.toString());
        List<EBusinessGoods> list = new ArrayList<EBusinessGoods>();
        list.add(goods);
        return list;
    }


    private int getVoucherOff(ObjectId voucherId, List<EOrderGoods> orderGoodses) {
        int voucherOff = 0;
        if (voucherId != null) {
            List<ObjectId> goodsIds = new ArrayList<ObjectId>();
            for (EOrderGoods eOrderGoods : orderGoodses) {
                goodsIds.add(eOrderGoods.geteGoodsId());
            }
            List<EGoodsEntry> eGoodsEntries = eGoodsDao.getEGoodsEntrys(goodsIds, Constant.FIELDS);
            Map<ObjectId, EGoodsEntry> map = new HashMap<ObjectId, EGoodsEntry>();
            for (EGoodsEntry eGoodsEntry : eGoodsEntries) {
                map.put(eGoodsEntry.getID(), eGoodsEntry);
            }
            for (EOrderGoods eOrderGoods : orderGoodses) {
                EGoodsEntry eGoodsEntry = map.get(eOrderGoods.geteGoodsId());
                voucherOff += eGoodsEntry.getVoucherOff() * eOrderGoods.getCount();
            }
            EVoucherEntry eVoucherEntry = eVoucherDao.getEVoucherEntry(voucherId);
            if (voucherOff > eVoucherEntry.getDenomination()) {
                voucherOff = eVoucherEntry.getDenomination();
            }
        }
        return voucherOff;
    }

    private int getExpressPrice(EOrderAddressEntry addressEntry, List<EOrderGoods> goodsList) {
        double amount = 0.0;
        double firstPrice = 0.0;
        double addOnePrice = 0.0;
        Stack<EExpressTemplateEntry.ExpTempDetail> maxFPtemp = new Stack<EExpressTemplateEntry.ExpTempDetail>();

        for (EOrderEntry.EOrderGoods eOrderGoods : goodsList) {
            EGoodsEntry goods = eGoodsDao.getEGoodsEntry(eOrderGoods.geteGoodsId(), new BasicDBObject());
            if (goods.getExpTempId() != null) {
                EExpressTemplateDTO templateDTO = expressTemplateService.getTemplateById(goods.getExpTempId().toString());
                double aop = 0.0;
                for (EExpressTemplateEntry.ExpTempDetail detail : templateDTO.getDetails()) {
                    if (detail.getZoneName().contains(addressEntry.getProvince())) {
                        if (detail.getFirstPrice() > firstPrice) {
                            firstPrice = detail.getFirstPrice();
                            maxFPtemp.push(detail);
                        }
                        aop = detail.getAddOnePrice();
                        break;
                    }
                }
                addOnePrice += eOrderGoods.getCount() * aop;
            }
        }
        amount = firstPrice + addOnePrice;
        if (maxFPtemp.size() > 0) {
            amount -= maxFPtemp.peek().getAddOnePrice();
        }
        return (int) (amount * 100);
    }

    private Map<String, String> getMessageMap(String message) {
        Map<String, String> messageMap = new HashMap<String, String>();
        if (!message.equals("")) {
            String[] goodsMessage = message.split(",");
            for (int i = 0; i < goodsMessage.length; i++) {
                String[] goods = goodsMessage[i].split("-");
                messageMap.put(goods[0], goods.length == 2 ? goods[1] : "");
            }
        }
        return messageMap;
    }

    /**
     * 支付宝回调，更新订单状态
     *
     * @param eoe
     * @param payType
     * @param payNumber
     * @throws Exception
     */
    public void payed(EOrderEntry eoe, int payType, String payNumber) throws Exception {
        FieldValuePair p1 = new FieldValuePair("pt", payType);
        FieldValuePair p2 = new FieldValuePair("pn", payNumber);
        FieldValuePair p3 = new FieldValuePair("st", OrderState.PAYED.getType());
        eOrderDao.update(eoe.getID(), p1, p2, p3);

        for (EOrderGoods eog : eoe.getOrderGoods()) {
            eGoodsDao.increaseSellCount(eog.geteGoodsId(), eog.getCount());
        }

    }

    /**
     * 更改订单状态
     *
     * @param orderId
     * @param orderState
     */
    public void updateOrderState(ObjectId orderId, int orderState) {
        FieldValuePair pair = new FieldValuePair("st", orderState);
        eOrderDao.update(orderId, pair);
    }


    //-------------------------------------------------------订单地址---------------------------------------------------//

    /**
     * 增加订单地址
     *
     * @param address
     */
    public ObjectId addOrderAddress(EOrderAddressEntry address) {
        return eOrderAddressDao.addEOrderAddress(address);
    }


    /**
     * 得到订单地址
     *
     * @param ui
     * @return
     */
    public List<EOrderAddressDTO> getEOrderAddressDTOs(ObjectId ui, String userName) {
        List<EOrderAddressDTO> retList = new ArrayList<EOrderAddressDTO>();
        List<EOrderAddressEntry> list = eOrderAddressDao.getEOrderAddressEntrys(ui);

        for (EOrderAddressEntry e : list) {
            retList.add(new EOrderAddressDTO(e));
        }
        return retList;
    }


    /**
     * 得到订单地址详细
     *
     * @param
     * @return
     */
    public EOrderAddressDTO getEOrderAddressDTO(ObjectId id) {
        EOrderAddressEntry e = eOrderAddressDao.getEOrderAddressEntry(id);
        if (null != e) {
            return new EOrderAddressDTO(e);
        }

        return null;
    }

    /**
     * 订单地址Entry
     */
    public EOrderAddressEntry getEOrderAddressEntry(ObjectId addressId) {
        EOrderAddressEntry e = eOrderAddressDao.getEOrderAddressEntry(addressId);
        if (e != null) {
            return e;
        }
        return null;
    }

    /**
     * 删除地址
     */
    public void deleteAddress(ObjectId id) {
        eOrderAddressDao.deleteAddress(id);
    }

    /**
     * 编辑地址
     */
    public ObjectId updateAddress(EOrderAddressEntry address) {
        return eOrderAddressDao.updateAddress(address);
    }


    /**
     * 设为默认地址
     */
    public void updateDefaultAddress(ObjectId userId, ObjectId addressId) {
        eOrderAddressDao.updateDefaultAddress(userId, addressId);
    }


    /**
     * 得到订单商品
     *
     * @param ui
     * @param skip
     * @param limit
     * @param state 订单状态
     * @return
     */
    public List<EOrderInfoDTO> getEOrderInfoDTOs(ObjectId ui, int state, int skip, int limit, boolean isParent) {

        List<EOrderEntry> list = eOrderDao.getEOrderEntrys(ui, state, skip, limit, isParent);

        Set<ObjectId> set = new HashSet<ObjectId>();//商品id
        Set<ObjectId> userIds = new HashSet<ObjectId>();//用户id
        Set<ObjectId> addressIds = new HashSet<ObjectId>();//地址id

        Map<ObjectId, EOrderInfoDTO> eOrderInfoDTOMap = new HashMap<ObjectId, EOrderInfoDTO>();

        for (EOrderEntry ee : list) {
            eOrderInfoDTOMap.put(ee.getID(), new EOrderInfoDTO(ee));
            userIds.add(ee.getUserId());
            addressIds.add(ee.getAddressId());

            for (EOrderGoods eog : ee.getOrderGoods()) {
                set.add(eog.geteGoodsId());
            }
        }

        Map<ObjectId, EGoodsEntry> goodsMap = eGoodsDao.getEGoodsEntryMap(set, Constant.FIELDS);
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        Map<ObjectId, EOrderAddressEntry> addressEntryMap = eOrderAddressDao.getEOrderAddressEntryMap(addressIds, Constant.FIELDS);

        EGoodsEntry eGoodsEntry;
        for (EOrderEntry ee : list) {
            EOrderInfoDTO dto = eOrderInfoDTOMap.get(ee.getID());
            UserEntry userEntry = userEntryMap.get(ee.getUserId());
            dto.setUserName(userEntry == null ? "匿名" : userEntry.getUserName());
            EOrderAddressEntry addressEntry = addressEntryMap.get(ee.getAddressId());
            dto.setProvince(addressEntry == null ? "" : addressEntry.getProvince());
            dto.setCity(addressEntry == null ? "" : addressEntry.getCity());
            dto.setDistrict(addressEntry == null ? "" : addressEntry.getXian());
            dto.setAddress(addressEntry == null ? "暂无地址" : addressEntry.getAddress());
            dto.setPhoneNumber(addressEntry == null ? "暂无号码" : addressEntry.getTelephone() == null ? "暂无号码" : addressEntry.getTelephone());
            dto.setReceiver(addressEntry == null ? "匿名" : addressEntry.getUser());
            dto.setExpressPrice(ee.getExpressPrice());
            dto.setTotalPrice(ee.getTotalPrice());

            for (EOrderGoods eog : ee.getOrderGoods()) {
                eGoodsEntry = goodsMap.get(eog.geteGoodsId());


                try {
                    dto.addEBusinessCartGoods(build(ee, eog, eGoodsEntry));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        }

        List<EOrderInfoDTO> retList = new ArrayList<EOrderInfoDTO>(eOrderInfoDTOMap.values());

        Collections.sort(retList, new Comparator<EOrderInfoDTO>() {
            @Override
            public int compare(EOrderInfoDTO arg0, EOrderInfoDTO arg1) {

                try {
                    return new ObjectId(arg1.getId()).compareTo(new ObjectId(arg0.getId()));
                } catch (Exception ex) {
                    return 0;
                }
            }
        });

        return retList;
    }


    /**
     * 删除订单
     *
     * @param
     * @param userId
     */
    public void removeOrder(EOrderEntry eOrderEntry, ObjectId userId) {
        eOrderDao.removeOrder(eOrderEntry.getID(), userId);
        if (eOrderEntry.getState() == OrderState.READY.getType()) {
            //todo 加积分   记录  更新抵用券状态
            if (eOrderEntry.getUsedExp() > 0) {
                ExpLogType expLogType = ExpLogType.MALL_USED;
                experienceService.updateScoreForMall(eOrderEntry.getUserId().toString(), expLogType, eOrderEntry.getUsedExp(), eOrderEntry.getID().toString());
            }

            if (eOrderEntry.getVoucherId() != null) {
                EVoucherEntry eVoucherEntry = eVoucherDao.getEVoucherEntry(eOrderEntry.getVoucherId());
                eVoucherEntry.setState(0);
                eVoucherDao.add(eVoucherEntry);
            }
        }

    }


    private EBusinessOrderGoods build(EOrderEntry eoe, EOrderGoods ge, EGoodsEntry ee) {


        EBusinessOrderGoods ecg = new EBusinessOrderGoods(eoe, ge, ee);
        List<ObjectId> kinds = ge.getKindIds();

        if (null != kinds && kinds.size() > 0) {
            StringBuilder kindInfo = new StringBuilder();
            List<Kind> kindList = ee.getKindList();
            for (Kind k : kindList) {
                kindInfo.append(k.getName()).append(":");
                for (EGoodsEntry.Spec ivp : k.getList()) {
                    if (kinds.contains(ivp.getSpecId())) {
                        kindInfo.append(ivp.getSpecName()).append("	");
                    }
                }
            }
            ecg.setObjKinds(MongoUtils.convertToStr(kinds));
            ecg.setKind(kindInfo.toString());
        }

        return ecg;
    }


    private String createOrderNumber() {
        String num = String.valueOf(System.currentTimeMillis());
        num += RandomUtils.nextInt(Constant.MIN_PASSWORD);
        return num;

    }

    /**
     * 查询订单数
     *
     * @param ui
     * @param state
     * @return
     */
    public int getOrdersCount(ObjectId ui, int state, boolean isParent) {
        return eOrderDao.getEOrderEntrysCount(ui, state, isParent);
    }

    /**
     * 管理后台更新订单信息
     */
    public void updateExpress(ObjectId orderId, ObjectId goodsId, String kinds, String expressNo, String expressComNo) throws IllegalParamException {

        eOrderDao.updateExpress(orderId, goodsId, MongoUtils.convert(kinds), expressNo, expressComNo);
    }

    /**
     * 查询收货人
     */
    public String getUserName(String orderId) {
        EOrderEntry order = eOrderDao.getEOrderEntry(new ObjectId(orderId));
        EOrderAddressEntry addressEntry = eOrderAddressDao.getEOrderAddressEntry(order.getAddressId());
        return addressEntry.getUser();
    }

    /**
     * 查询物流信息
     */
    public String getExpressList(String exCompanyNo, String expressNo) {
        return new KdGoldAPIDemo(exCompanyNo, expressNo).getOrderTracesByJson();
    }


    /**
     * 物流信息查询
     */
    public static class KdGoldAPIDemo {
        //电商ID
        private String EBusinessID = "1256229";
        //电商加密私钥，快递鸟提供，注意保管，不要泄漏
        private String AppKey = "da4b8535-0b2e-4565-b815-09a0c959cc63";
        //请求url
        private String ReqURL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";
        //快递单号
        private String expressNo;
        //快递公司编号
        private String exCompanyNo;

        KdGoldAPIDemo(String exCompanyNo, String expressNo) {
            this.exCompanyNo = exCompanyNo;
            this.expressNo = expressNo;
        }

        public String getOrderTracesByJson() {
            //订单号、快递公司编号、快递编号
            String requestData = "{'OrderCode':'','ShipperCode':'" + exCompanyNo + "','LogisticCode':" + expressNo + "}";

            Map<String, String> params = new HashMap<String, String>();
            params.put("EBusinessID", EBusinessID);
            params.put("RequestType", "1002");
            try {
                params.put("RequestData", urlEncoder(requestData, "UTF-8"));
                String dataSign = encrypt(requestData, AppKey, "UTF-8");
                params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            params.put("DataType", "2");
            String result = sendPost(ReqURL, params);

            return result;
        }

        /**
         * base64编码
         *
         * @param str     内容
         * @param charset 编码方式
         * @throws UnsupportedEncodingException
         */
        private String base64(String str, String charset) throws UnsupportedEncodingException {
            String encoded = Base64.encode(str.getBytes(charset));
            return encoded;
        }

        private String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
            String result = URLEncoder.encode(str, charset);
            return result;
        }

        /**
         * MD5加密
         *
         * @param str     内容
         * @param charset 编码方式
         * @throws Exception
         */
        private String MD5(String str, String charset) throws Exception {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(charset));
            byte[] result = md.digest();
            StringBuffer sb = new StringBuffer(32);
            for (int i = 0; i < result.length; i++) {
                int val = result[i] & 0xff;
                if (val <= 0xf) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString().toLowerCase();
        }

        /**
         * 电商Sign签名生成
         *
         * @param content  内容
         * @param keyValue Appkey
         * @param charset  编码方式
         * @return DataSign签名
         * @throws UnsupportedEncodingException ,Exception
         */
        private String encrypt(String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception {
            if (keyValue != null) {
                return base64(MD5(content + keyValue, charset), charset);
            }
            return base64(MD5(content, charset), charset);
        }

        /**
         * 向指定 URL 发送POST方法的请求
         *
         * @param url    发送请求的 URL
         * @param params 请求的参数集合
         * @return 远程资源的响应结果
         */
        private String sendPost(String url, Map<String, String> params) {
            OutputStreamWriter out = null;
            BufferedReader in = null;
            StringBuilder result = new StringBuilder();
            try {
                URL realUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // POST方法
                conn.setRequestMethod("POST");
                // 设置通用的请求属性
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.connect();
                // 获取URLConnection对象对应的输出流
                out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                // 发送请求参数
                if (params != null) {
                    StringBuilder param = new StringBuilder();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        if (param.length() > 0) {
                            param.append("&");
                        }
                        param.append(entry.getKey());
                        param.append("=");
                        param.append(entry.getValue());
                        System.out.println(entry.getKey() + ":" + entry.getValue());
                    }
                    System.out.println("param:" + param.toString());
                    out.write(param.toString());
                }
                // flush输出流的缓冲
                out.flush();
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != out) {
                        out.close();
                    }
                    if (null != in) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result.toString();
        }
    }

    /**
     * 计算运费
     */
    public double getExpAmount(OrderGoodsExpTempDTO goodsExpTemp) {
        double amount = 0.0;
        double firstPrice = 0.0;
        double addOnePrice = 0.0;
        Stack<EExpressTemplateEntry.ExpTempDetail> maxFPtemp = new Stack<EExpressTemplateEntry.ExpTempDetail>();

        for (OrderGoodsExpTempDTO.OrderGoodsExpTemp dto : goodsExpTemp.getGoodsList()) {
            EGoodsEntry goods = eGoodsDao.getEGoodsEntry(new ObjectId(dto.getGoodsId()), new BasicDBObject());
            if (goods.getExpTempId() != null) {
                EExpressTemplateDTO templateDTO = expressTemplateService.getTemplateById(goods.getExpTempId().toString());
                double aop = 0.0;
                for (EExpressTemplateEntry.ExpTempDetail detail : templateDTO.getDetails()) {
                    if (goodsExpTemp.getAddress().equals("")) {
                        break;
                    } else if (detail.getZoneName().contains(goodsExpTemp.getAddress())) {
                        if (detail.getFirstPrice() > firstPrice) {
                            firstPrice = detail.getFirstPrice();
                            maxFPtemp.push(detail);
                        }
                        aop = detail.getAddOnePrice();
                        break;
                    }
                }
                addOnePrice += dto.getCount() * aop;
            }
        }
        amount = firstPrice + addOnePrice;
        if (maxFPtemp.size() > 0) {
            amount -= maxFPtemp.peek().getAddOnePrice();
        }
        return amount;
    }

    /**
     * 订单详情
     */
    public EOrderDetailDTO getEOrderDetailInfo(ObjectId orderId) {
        EOrderDetailDTO dto = new EOrderDetailDTO();
        EOrderEntry order = eOrderDao.getEOrderEntry(orderId);
        EOrderAddressEntry addressEntry = eOrderAddressDao.getEOrderAddressEntry(order.getAddressId());
        dto.setExpressPrice(order.getExpressPrice());
        dto.setExp(order.getUsedExp());
        dto.setVoff(order.getVoucherOff());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setName(addressEntry.getUser());
        dto.setAddress(addressEntry.getProvince() + " " + addressEntry.getCity() + " " + addressEntry.getXian() + " " + addressEntry.getAddress());
        dto.setTel(addressEntry.getTelephone());

        List<EOrderDetailDTO.EOrderDetailGoods> goodsList = new ArrayList<EOrderDetailDTO.EOrderDetailGoods>();
        for (EOrderEntry.EOrderGoods orderGoods : order.getOrderGoods()) {
            EGoodsEntry goodsEntry = eGoodsDao.getEGoodsEntry(orderGoods.geteGoodsId(), Constant.FIELDS);
            EOrderDetailDTO.EOrderDetailGoods goods = new EOrderDetailDTO.EOrderDetailGoods();
            goods.setGoodsName(goodsEntry.getName());
            goods.setImg(goodsEntry.getSuggestImage());
            goods.setPrice(orderGoods.getPrice());
            goods.setCount(orderGoods.getCount());
            goods.setId(goodsEntry.getID().toString());

            List<ObjectId> kindIds = orderGoods.getKindIds();
            StringBuilder kind = new StringBuilder();
            for (ObjectId kid : kindIds) {
                EGoodsEntry.Kind k = goodsEntry.getKindList().get(0);
                kind.append(k.getName()).append("：");
                for (EGoodsEntry.Spec spec : k.getList()) {
                    if (spec.getSpecId().equals(kid)) {
                        kind.append(spec.getSpecName());
                        break;
                    }
                }
            }
            goods.setKind(kind.toString());
            goodsList.add(goods);
        }

        dto.setGoodsList(goodsList);

        return dto;
    }
}
