package com.fulaan.mall.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.controller.BaseController;
import com.fulaan.alipay.config.AlipayConfig;
import com.fulaan.alipay.util.AlipayCore;
import com.fulaan.alipay.util.AlipayNotify;
import com.fulaan.alipay.util.AlipaySubmit;
import com.fulaan.forum.service.FLogService;
import com.fulaan.mall.pojo.OrderInfo;
import com.fulaan.mall.service.EBusinessCartService;
import com.fulaan.mall.service.EBusinessOrderService;
import com.fulaan.mall.service.EBusinessVoucherService;
import com.fulaan.mall.service.EGoodsService;
import com.fulaan.user.service.UserService;
import com.pojo.app.SessionValue;
import com.pojo.ebusiness.*;
import com.pojo.ebusiness.EGoodsEntry.Kind;
import com.pojo.ebusiness.EOrderEntry.EOrderGoods;
import com.pojo.emarket.OrderState;
import com.pojo.forum.FLogDTO;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.mails.MailUtils;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.RespObj;
import com.sys.utils.ValidationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 电子商务订单实现
 *
 * @author fourer
 */
@Controller
@RequestMapping("/mall/order")
public class EBusinessOrderController extends BaseController {

    private static final Logger EBusinessLog = Logger.getLogger("EBusiness");

    private static String payment_type = "1";

    private String notify_url = Resources.getProperty("fulaan.domain", Resources.getProperty("domain")) + "/mall/order/notify.do";

    private String return_url = Resources.getProperty("fulaan.domain", Resources.getProperty("domain")) + "/mall/order/page.do";

    private String seller_email = "zhifu@fulaan.com";

    private String show_url = "http://www.fulaan.com/mall/index.do";

    private String anti_phishing_key = "";

    private String exter_invoke_ip = "";

    private String emails = Resources.getProperty("notify.emails", "madison.he@fulaan.com");

    private EBusinessOrderService orderService = new EBusinessOrderService();
    private EBusinessCartService eBusinessCartService = new EBusinessCartService();
    private EGoodsService eGoodsService = new EGoodsService();
    @Autowired
    private UserService userService;
    @Autowired
    private EBusinessVoucherService eBusinessVoucherService;
    @Autowired
    private FLogService fLogService = new FLogService();

    /**
     * 订单提交页面
     *
     * @return
     * @throws IllegalParamException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @RequestMapping("/address")
    @LoginInfo
    public String orderAddress(String ebcIds, HttpServletRequest request, Map<String, Object> model) throws IllegalParamException, ClassNotFoundException, IOException {
        if (StringUtils.isNotBlank(ebcIds)) {
            List<ObjectId> selectEbcIds = MongoUtils.convert(ebcIds);
            eBusinessCartService.selectGoodsFromCars(getUserId(), selectEbcIds);
        }
        loginInfo(request, model);

        boolean buyNow = "true".equals(request.getParameter("buyNow"));
        boolean parentPay = "true".equals(request.getParameter("parentPay"));
        if (buyNow || parentPay) {
            setGoodsList(request, model);
        }

        FLogDTO fLogDTO = new FLogDTO();
        fLogDTO.setActionName("order");
        SessionValue sv = getSessionValue();
        if (null != sv && !sv.isEmpty()) {
            fLogDTO.setPersonId(sv.getId());
        }
        fLogDTO.setPath("/mall/order/address.do");
        fLogDTO.setTime(System.currentTimeMillis());
        fLogService.addFLog(fLogDTO);

        return "mall/orderaddress";
    }

    private void setGoodsList(HttpServletRequest request, Map<String, Object> model) throws IllegalParamException {
        boolean buyNow = "true".equals(request.getParameter("buyNow"));
        boolean parentPay = "true".equals(request.getParameter("parentPay"));
        String goodsId = request.getParameter("goodsId");
        int count = Integer.valueOf(request.getParameter("count"));
        String kinds = request.getParameter("kinds");

        EGoodsEntry goodsEntry = eGoodsService.getEGoodsEntry(new ObjectId(goodsId));
        List<EBusinessGoods> list = orderService.getGoods(goodsEntry, count, kinds);
        List<Object> objectList = new ArrayList<Object>();
        for (EBusinessGoods goods : list) {
            objectList.add(goods);
        }
        model.put("goodsList", new JSONArray(objectList));

        if (buyNow) {//立即购买
            model.put("buyNow", true);
        }
        if (parentPay) {//家长一键购
            model.put("parentPay", true);
            String father = userService.getFather(getUserId());
            String mother = userService.getMother(getUserId());
            model.put("father", father);
            model.put("mother", mother);
        }
    }


    /**
     * 订单页面
     *
     * @return
     */
    @RequestMapping("/page")
    public String orderInfoPage(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(request, model);
        model.put("menuItem", 1);
        boolean isParent = UserRole.isParent(userService.getUserInfoById(getUserId().toString()).getRole());
        int orderstate1 = orderService.getOrdersCount(getUserId(), 1, isParent);//代付款数量
        int orderstate2 = orderService.getOrdersCount(getUserId(), 2, isParent);//待发货数量
        int orderstate6 = orderService.getOrdersCount(getUserId(), 6, isParent);//待收货数量
        model.put("orderstate1", orderstate1);
        model.put("orderstate2", orderstate2);
        model.put("orderstate6", orderstate6);
        return "mall/orderpage";
    }

    private void loginInfo(HttpServletRequest request, Map<String, Object> model) {
        SessionValue sessionValue = (SessionValue) request.getAttribute(BaseController.SESSION_VALUE);
        if (null == sessionValue || sessionValue.isEmpty()) {
            model.put("userName", "");
            model.put("userId", "");
            model.put("login", false);
            model.put("k6kt", -1);
        } else {
            model.put("userName", sessionValue.getUserName());
            model.put("userId", sessionValue.getId());
            model.put("login", true);
            model.put("k6kt", sessionValue.getK6kt());
        }
    }


    @RequestMapping("/isVoucher")
    @ResponseBody
    public RespObj IsVoucher(@RequestParam(required = false, defaultValue = "") String voucherId) {
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        ObjectId vId = null;
        if (!voucherId.equals("")) {
            vId = new ObjectId(voucherId);
            EVoucherDTO eVoucherDTO = eBusinessVoucherService.getUnUseEVoucher(vId);
            if (eVoucherDTO == null) {
                respObj.setMessage("不存在未使用的优惠券！");
            } else {
                respObj.setCode(Constant.SUCCESS_CODE);
            }
        }
        return respObj;

    }


    //--------------------------------------------------------------web端订单支付-----------------------------------------------//

    /**
     * 生成订单并且支付(立即购买)
     *
     * @param
     * @param addressId
     * @return
     * @throws Exception
     */
    @RequestMapping("/createNow")
    @ResponseBody
    public String createOrder(@ObjectIdType ObjectId addressId, @RequestParam(required = false, defaultValue = "0") int usedExp,
                              @RequestParam(required = false, defaultValue = "") String voucherId,
                              @RequestParam(required = false, defaultValue = "") String message,
                              String goodsList, HttpServletResponse response) throws Exception {
        EBusinessLog.info("OrderCreate;UserId=" + getUserId().toString() + ";学校：" + getSessionValue().getSchoolName() + ";使用积分：" + usedExp);
        if (StringUtils.isEmpty(goodsList)) {
            throw new Exception("Can not find goods in shopping cars for user:" + getUserId().toString());
        }
        int exp = (Integer) getUserExperience().get("exp");
        if (usedExp > exp) {
            throw new Exception("usedExp is large than exp for user:" + getUserId().toString());
        }
        ObjectId vId = null;
        if (!voucherId.equals("")) {
            vId = new ObjectId(voucherId);
            EVoucherDTO eVoucherDTO = eBusinessVoucherService.getUnUseEVoucher(vId);
            if (eVoucherDTO == null) {

                throw new Exception("voucher(id:" + voucherId + ") is not exist for user:" + getUserId().toString());
            }
        }

        List<EBusinessGoods> list = JSON.parseArray(goodsList, EBusinessGoods.class);
        EBusinessLog.info("OrderGoods:" + list);
        List<EBusinessCartGoods> cartGoodsList = new ArrayList<EBusinessCartGoods>();
        for (EBusinessGoods goods : list) {
            cartGoodsList.add(goods.toCartGoods());
        }
        EOrderEntry eoe = orderService.createOrder(getUserId(), cartGoodsList, addressId, usedExp, vId, message, null);
        EBusinessLog.info("************************");
        EBusinessLog.info("OrderInfo:" + eoe.getBaseEntry());
        EBusinessLog.info("************************");
        if (eoe.getTotalPrice() == 0) {
            orderService.updateOrderState(eoe.getID(), OrderState.PAYED.getType());
            EBusinessLog.info("订单伟0元，状态变为已支付");
            response.sendRedirect("/mall/order/page.do");
            return null;
        }
        return orderPay(eoe);
    }

    /**
     * 生成订单并且支付(购物车)
     *
     * @param
     * @param addressId
     * @return
     * @throws Exception
     */
    @RequestMapping("/create")
    @ResponseBody
    public String createOrder(@ObjectIdType ObjectId addressId, @RequestParam(required = false, defaultValue = "0") int usedExp,
                              @RequestParam(required = false, defaultValue = "") String voucherId,
                              @RequestParam(required = false, defaultValue = "") String message,
                              HttpServletResponse response) throws Exception {
        EBusinessLog.info("OrderCreate;UserId=" + getUserId().toString() + ";学校：" + getSessionValue().getSchoolName() + ";使用积分：" + usedExp);
        List<EBusinessCartGoods> list = eBusinessCartService.getGoodsFromCars(getUserId(), Constant.ONE);
        if (null == list || list.size() == 0) {
            throw new Exception("Can not find goods in shopping cars for user:" + getUserId().toString());
        }
        int exp = (Integer) getUserExperience().get("exp");
        if (usedExp > exp) {
            throw new Exception("usedExp is large than exp for user:" + getUserId().toString());
        }
        ObjectId vId = null;
        if (StringUtils.isNotBlank(voucherId)) {
            vId = new ObjectId(voucherId);
            EVoucherDTO eVoucherDTO = eBusinessVoucherService.getUnUseEVoucher(vId);
            if (eVoucherDTO == null) {
                throw new Exception("voucher(id:" + voucherId + ") is not exist for user:" + getUserId().toString());
            }
        }
        EBusinessLog.info("OrderGoods:" + list);
        EOrderEntry eoe = orderService.createOrder(getUserId(), list, addressId, usedExp, vId, message, null);
        EBusinessLog.info("************************");
        EBusinessLog.info("OrderInfo:" + eoe.getBaseEntry());
        EBusinessLog.info("************************");
        eBusinessCartService.deleteBuyedGoodsFromCars(getUserId());
        if (eoe.getTotalPrice() == 0) {
            orderService.updateOrderState(eoe.getID(), OrderState.PAYED.getType());
            EBusinessLog.info("订单伟0元，状态变为已支付");
            response.sendRedirect("/mall/order/page.do");
            return null;
        }

        return orderPay(eoe);
    }

    /**
     * 学生推送订单给家长
     */
    @RequestMapping("/pushOrder")
    public String pushOrderToParent(@ObjectIdType ObjectId addressId,
                                    @RequestParam(required = false, defaultValue = "0") int usedExp,
                                    @RequestParam(required = false, defaultValue = "") String voucherId,
                                    @RequestParam(required = false, defaultValue = "") String message,
                                    @ObjectIdType ObjectId parentId, String goodsList,
                                    HttpServletResponse response) throws Exception {
        EBusinessLog.info("OrderCreate;UserId=" + getUserId().toString() + ";学校：" + getSessionValue().getSchoolName() + ";使用积分：" + usedExp);
        if (StringUtils.isEmpty(goodsList)) {
            throw new Exception("Can not find goods in shopping cars for user:" + getUserId().toString());
        }
        int exp = (Integer) getUserExperience().get("exp");
        if (usedExp > exp) {
            throw new Exception("usedExp is large than exp for user:" + getUserId().toString());
        }
        ObjectId vId = null;
        if (!voucherId.equals("")) {
            vId = new ObjectId(voucherId);
            EVoucherDTO eVoucherDTO = eBusinessVoucherService.getUnUseEVoucher(vId);
            if (eVoucherDTO == null) {
                throw new Exception("voucher(id:" + voucherId + ") is not exist for user:" + getUserId().toString());
            }
        }

        List<EBusinessGoods> list = JSON.parseArray(goodsList, EBusinessGoods.class);
        EBusinessLog.info("OrderGoods:" + list);
        List<EBusinessCartGoods> cartGoodsList = new ArrayList<EBusinessCartGoods>();
        for (EBusinessGoods goods : list) {
            cartGoodsList.add(goods.toCartGoods());
        }
        EOrderEntry eoe = orderService.createOrder(getUserId(), cartGoodsList, addressId, usedExp, vId, message, parentId);
        EBusinessLog.info("************************");
        EBusinessLog.info("OrderInfo:" + eoe.getBaseEntry());
        EBusinessLog.info("************************");
        return "redirect:/mall/order/page.do";
    }


    /**
     * 支付回调
     *
     * @param request
     * @throws NumberFormatException
     * @throws Exception
     */
    @SessionNeedless
    @RequestMapping("/notify")
    public void callBackNotify(HttpServletRequest request) throws Exception {


        EBusinessLog.info("支付宝回调;" + request.getParameterMap());

        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            EBusinessLog.info("-------name-----" + name + "----values" + values);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
            EBusinessLog.info("name-----" + name + "----value" + valueStr);
        }

        EBusinessLog.info("OrderNotify;" + params);

        String trade_no = request.getParameter("trade_no");                //支付宝交易号
        String out_trade_no = request.getParameter("out_trade_no");            //获取订单号
        String total_fee_string = request.getParameter("total_fee");            //获取总金额
        String trade_status = request.getParameter("trade_status");

        EBusinessLog.info("trade_no;" + trade_no);
        EBusinessLog.info("out_trade_no;" + out_trade_no);
        EBusinessLog.info("total_fee_string;" + total_fee_string);
        EBusinessLog.info("trade_status;" + trade_status);

        try {
            if (AlipayNotify.verify(params)) {
                EBusinessLog.info("支付宝回调验证通过");
                if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                    EBusinessLog.info("TRADE_SUCCESS");
                    EOrderEntry eoe = orderService.getEOrderEntry(new ObjectId(out_trade_no));
                    if (null == eoe) {
                        EBusinessLog.info("没有找到商品订单;" + out_trade_no);
                    }
                    Double totalPrice = (Double.valueOf(total_fee_string) * 100D);

                    if (shouldUpdateOrder(eoe, totalPrice.intValue())) {
                        try {
                            orderService.payed(eoe, Integer.parseInt(payment_type), trade_no);
                        } catch (Exception ex) {
                            EBusinessLog.error("", ex);
                        }
                        try {
                            sendEmail(eoe);
                        } catch (Exception ex) {
                            EBusinessLog.error("", ex);
                        }
                    }
                }
            } else {
                EBusinessLog.info("支付宝验证失败");
            }
        } catch (Exception ex) {
            EBusinessLog.error("支付宝报错", ex);
        }

    }

    //----------------------------------------------------移动端订单支付-----------------------------------------------//

    /**
     * 获取订单信息(包含rsa签名)
     *
     * @param orderId
     * @return OrderInfo
     */
    @RequestMapping("/mobile/getOrderInfo")
    @ResponseBody
    public RespObj getOrderInfo(@ObjectIdType ObjectId orderId) {

        EOrderEntry eoe = orderService.getEOrderEntry(orderId);
        if (null == eoe) {
            EBusinessLog.info("没有找到商品订单;" + orderId);
            return RespObj.FAILD("没有找到商品订单;");
        }

        ObjectId gid = eoe.getOrderGoods().get(0).geteGoodsId();
        EGoodsDTO dto = eGoodsService.detail(gid);
        String goodsName = dto.getGoodsName();

        OrderInfo orderInfo = new OrderInfo(eoe.getID().toString(), goodsName, "复兰教育商品",
                String.valueOf(Double.valueOf(eoe.getTotalPrice()) / 100.0), notify_url);
        return RespObj.SUCCESS(orderInfo);
    }


    /**
     * 移动端生成订单
     *
     * @param
     * @param addressId
     * @return
     * @throws Exception
     */
    @RequestMapping("/mobile/create")
    @ResponseBody
    public RespObj createOrder1(@ObjectIdType ObjectId addressId, @RequestParam(required = false, defaultValue = "0") int usedExp,
                                @RequestParam(required = false, defaultValue = "") String voucherId,
                                @RequestParam(required = false, defaultValue = "") String message,
                                HttpServletResponse response) throws Exception {
        EBusinessLog.info("mobile OrderCreate;UserId=" + getUserId().toString() + ";学校：" + getSessionValue().getSchoolName());
        List<EBusinessCartGoods> list = eBusinessCartService.getGoodsFromCars(getUserId(), Constant.ONE);
        if (null == list || list.size() == 0) {
            return RespObj.FAILD;
        }
        int exp = (Integer) getUserExperience().get("exp");
        if (usedExp > exp) {
            throw new Exception("usedExp is large than exp for user:" + getUserId().toString());
        }
        ObjectId vId = null;
        if (!voucherId.equals("")) {
            vId = new ObjectId(voucherId);
            EVoucherDTO eVoucherDTO = eBusinessVoucherService.getUnUseEVoucher(vId);
            if (eVoucherDTO == null) {
                throw new Exception("voucher(id:" + voucherId + ") is not exist for user:" + getUserId().toString());
            }
        }
        EBusinessLog.info("mobile OrderGoods:" + list);
        EOrderEntry eoe = orderService.createOrder(getUserId(), list, addressId, usedExp, vId, message, null);
        EBusinessLog.info("************************");
        EBusinessLog.info("mobile OrderInfo:" + eoe.getBaseEntry());
        EBusinessLog.info("************************");
        eBusinessCartService.deleteBuyedGoodsFromCars(getUserId());

        if (eoe.getTotalPrice() == 0) {
            orderService.updateOrderState(eoe.getID(), OrderState.PAYED.getType());
            EBusinessLog.info("订单伟0元，状态变为已支付");
        }
        RespObj obj = new RespObj(Constant.SUCCESS_CODE, eoe.getID().toString());
        return obj;
    }

    /**
     * 移动支付回调
     *
     * @throws NumberFormatException
     * @throws Exception
     */
    @SessionNeedless
    @RequestMapping("/mobile/notify")
    @ResponseBody
    public RespObj callBackNotify(@ObjectIdType ObjectId orderId,
                                  String trade_no) throws Exception {

        return RespObj.SUCCESS;
    }

    /**
     * 订单支付
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/pay")
    @ResponseBody
    public String payOrder(@ObjectIdType ObjectId orderId) throws Exception {
        EBusinessLog.info("OrderPay;user:" + getSessionValue().getMap() + ";学校：" + getSessionValue().getSchoolName());
        EOrderEntry eoe = orderService.getEOrderEntry(orderId);
        if (null == eoe) {
            throw new Exception("Can not find order;the id=" + orderId.toString());
        }
        if (eoe.getState() != OrderState.READY.getType()) {
            throw new Exception("order state error;the id=" + orderId.toString());
        }
        return orderPay(eoe);
    }


    @RequestMapping("/experiences")
    @ResponseBody
    public Map<String, Object> getUserExperience() {
        Map<String, Object> model = new HashMap<String, Object>();
        UserDetailInfoDTO user = userService.getUserInfoById(getSessionValue().getId());
        model.put("exp", user.getExperienceValue());
        return model;
    }


    private boolean shouldUpdateOrder(EOrderEntry eoe, int price) {
        EBusinessLog.info("eoePrice" + eoe.getTotalPrice() + "price" + price);
        if (eoe.getState() != OrderState.READY.getType())
            return false;
        return true;
    }


    private String orderPay(EOrderEntry eoe) {

        String goodsName = "复兰商城商品";

        try {
            ObjectId gid = eoe.getOrderGoods().get(0).geteGoodsId();
            EGoodsDTO dto = eGoodsService.detail(gid);
            goodsName = dto.getGoodsName();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", payment_type);
        sParaTemp.put("notify_url", notify_url);
        sParaTemp.put("return_url", return_url);
        sParaTemp.put("seller_email", seller_email);
        sParaTemp.put("out_trade_no", eoe.getID().toString());
        sParaTemp.put("subject", goodsName);
        sParaTemp.put("total_fee", String.valueOf(Double.valueOf(eoe.getTotalPrice()) / 100.0));
        sParaTemp.put("body", "");
        sParaTemp.put("show_url", show_url);
        sParaTemp.put("anti_phishing_key", anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        String sign = AlipaySubmit.buildRequestMysign(sPara, "MD5");
        EBusinessLog.info("OrderCreateSign:" + sign);
        // 建立请求
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");

        return sHtmlText;
    }


    private void sendEmail(final EOrderEntry eoe) {

        final StringBuilder goodsInfo = new StringBuilder();

        for (EOrderGoods eog : eoe.getOrderGoods()) {
            EGoodsEntry ge = eGoodsService.getEGoodsEntry(eog.geteGoodsId());
            goodsInfo.append(ge.getName()).append(" ");
            List<ObjectId> kinds = eog.getKindIds();
            if (null != kinds && kinds.size() > 0) {
                StringBuilder kindInfo = new StringBuilder();
                List<Kind> kindList = ge.getKindList();
                for (Kind k : kindList) {
                    kindInfo.append(k.getName()).append(":");
                    for (EGoodsEntry.Spec ivp : k.getList()) {
                        if (kinds.contains(ivp.getSpecId())) {
                            kindInfo.append(ivp.getSpecName()).append("	");
                        }
                    }
                }
                goodsInfo.append(kindInfo.toString()).append(";");
                goodsInfo.append("数量：" + eog.getCount());
                goodsInfo.append("单价(单位分)：" + eog.getPrice());

            }
        }


        final StringBuilder addressInfo = new StringBuilder();

        EOrderAddressDTO dto = orderService.getEOrderAddressDTO(eoe.getAddressId());
        addressInfo.append(dto.getUserName()).append(" ");
        addressInfo.append(dto.getTelephone()).append(" ");
        addressInfo.append(dto.getAddress()).append(" ");


        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                MailUtils sendMail = new MailUtils();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("订单ID：" + eoe.getEorderNumber());
                stringBuilder.append("时间：" + DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_A));
                stringBuilder.append("， 商品信息：" + goodsInfo.toString());
                stringBuilder.append("， 物流信息：" + addressInfo.toString());
                stringBuilder.append("， 总价(单位分)：" + eoe.getTotalPrice());
                stringBuilder.append("， 支付单号：" + eoe.getPayNumber());

                String[] emailArr = emails.split(",");
                for (String email : emailArr) {
                    try {
                        sendMail.sendMail("订单信息", email, stringBuilder.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String[] mails = new String[]{"siri@fulaan.com", "carry.xue@fulaan.com"};
                for (String email : mails) {
                    try {
                        sendMail.sendMail("订单信息", email, stringBuilder.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        th.start();

    }


    //----------------------------------------------------------订单地址---------------------------------------------------------//

    /**
     * 订单地址
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/address/list")
    @ResponseBody
    public List<EOrderAddressDTO> orderAddressList() throws Exception {
        EBusinessLog.info("OrderAddress;user:" + getSessionValue().getMap() + ";学校：" + getSessionValue().getSchoolName());
        List<EOrderAddressDTO> list = orderService.getEOrderAddressDTOs(getUserId(), getSessionValue().getUserName());
        return list;
    }


    /**
     * 增加订单地址
     *
     * @param address
     * @return
     * @throws Exception
     */
    @RequestMapping("/address/add")
    @ResponseBody
    public List<EOrderAddressDTO> addOrderAddress(String province, String city, String district, String user, String address, String tel) throws Exception {
        if (province.contains("省") || province.contains("市") || province.contains("区")) {//iOS
            if (province.contains("黑龙江")) {
                province = "黑龙江";
            } else if (province.contains("内蒙古")) {
                province = "内蒙古";
            } else {
                province = province.substring(0, 2);
            }
        }
        if (province.contains("行政") || province.contains("自治")) {//Android
            if (province.contains("内蒙古")) {
                province = "内蒙古";
            } else {
                province = province.substring(0, 2);
            }
        }
        EBusinessLog.info("OrderAddressAdd;user:" + getSessionValue().getMap() + ";学校：" + getSessionValue().getSchoolName());

        if (ValidationUtils.isRequestModile(tel)) {
            EOrderAddressEntry eAddress = new EOrderAddressEntry(province, city, district, getUserId(), user, address, tel, 0);
            orderService.addOrderAddress(eAddress);
        }


        List<EOrderAddressDTO> list = orderService.getEOrderAddressDTOs(getUserId(), getSessionValue().getUserName());
        return list;
    }


    /**
     * 地址详细
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/address/detail")
    @ResponseBody
    public EOrderAddressDTO orderAddressDetail(@ObjectIdType ObjectId id) throws Exception {
        EOrderAddressDTO dto = orderService.getEOrderAddressDTO(id);
        return dto;
    }

    /**
     * 修改默认地址
     */
    @RequestMapping("/address/updateDefault")
    @ResponseBody
    public RespObj updateDefaultAddress(@ObjectIdType ObjectId addressId) {
        RespObj respObj = RespObj.FAILD;
        try {
            EBusinessLog.info("设置默认地址,OrderAddressId:" + addressId);
            orderService.updateDefaultAddress(getUserId(), addressId);
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("设置失败");
        }
        return respObj;
    }


    /**
     * 修改地址
     */
    @RequestMapping("/address/update")
    @ResponseBody
    public Map<String, Object> updateOrderAddress(String addressId, String province, String city, String district, String user, String address, String tel) throws Exception {
        if (province.contains("省") || province.contains("市") || province.contains("区")) {//iOS
            if (province.contains("黑龙江")) {
                province = "黑龙江";
            } else if (province.contains("内蒙古")) {
                province = "内蒙古";
            } else {
                province = province.substring(0, 2);
            }
        }
        if (province.contains("行政") || province.contains("自治")) {//Android
            if (province.contains("内蒙古")) {
                province = "内蒙古";
            } else {
                province = province.substring(0, 2);
            }
        }
        EBusinessLog.info("OrderAddressUpdate,orderAddressId:" + addressId);

        ObjectId id = null;
        if (ValidationUtils.isRequestModile(tel)) {
            EOrderAddressEntry eAddress = orderService.getEOrderAddressEntry(new ObjectId(addressId));
            eAddress.setProvince(province);
            eAddress.setCity(city);
            eAddress.setXian(district);
            eAddress.setUser(user);
            eAddress.setAddress(address);
            eAddress.setTelephone(tel);
            id = orderService.updateAddress(eAddress);
        }
        List<EOrderAddressDTO> list = orderService.getEOrderAddressDTOs(getUserId(), getSessionValue().getUserName());
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", id.toString());
        result.put("list", list);
        return result;
    }


    /**
     * 删除地址
     */
    @RequestMapping("/address/delete")
    @ResponseBody
    public RespObj deleteAddress(String id) {
        RespObj respObj = RespObj.FAILD;
        try {
            orderService.deleteAddress(new ObjectId(id));
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            respObj.setMessage("删除失败");
        }
        return respObj;
    }

    //---------------------------------------------------------订单---------------------------------------------//

    /**
     * 得到订单信息
     *
     * @return
     */
    @RequestMapping("/data")
    @ResponseBody
    public List<EOrderInfoDTO> orderInfoList(@RequestParam(required = false, defaultValue = "-1") int orderState) {
        EBusinessLog.info("orderInfoList;" + getSessionValue().getMap() + ";学校：" + getSessionValue().getSchoolName());
        boolean isParent = UserRole.isParent(userService.getUserInfoById(getUserId().toString()).getRole());
        return orderService.getEOrderInfoDTOs(getUserId(), orderState, 0, 100, isParent);
    }


    /**
     * 删除订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/remove")
    @ResponseBody
    public RespObj removeOrder(@ObjectIdType ObjectId id) {
        EBusinessLog.info("removeOrder;" + getSessionValue().getMap() + ";id=" + id.toString());

        EOrderEntry oe = orderService.getEOrderEntry(id);
        if (null == oe) {
            return new RespObj(Constant.FAILD_CODE, "参数错误");
        }
        if (oe.getState() != OrderState.READY.getType() && oe.getState() != OrderState.COMPLETE.getType() && oe.getState() != OrderState.ROVOKE.getType()) {
            return new RespObj(Constant.FAILD_CODE, "该订单已经付款，不允许删除");
        }
        orderService.removeOrder(oe, getUserId());
        return RespObj.SUCCESS;
    }


    //-----------------------------------------------------------物流-----------------------------------------------//

    /**
     * 跳转到物流信息界面
     */
    @RequestMapping("/expressPage")
    public String toExpressPage(HttpServletRequest request, Map<String, Object> model, Model model1) {
        loginInfo(request, model);

        String exCompanyNo = request.getParameter("exCompanyNo");
        String expressNo = request.getParameter("expressNo");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        String gName = request.getParameter("gName");
        String gKind = request.getParameter("gKind");

        String orderId = request.getParameter("orderId");
        String username = orderService.getUserName(orderId);

        try {
            username = URLDecoder.decode(username, "UTF-8");
            gName = URLDecoder.decode(gName, "UTF-8");
            gKind = URLDecoder.decode(gKind, "UTF-8");
            address = URLDecoder.decode(address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        model1.addAttribute("exCompanyNo", exCompanyNo);
        model1.addAttribute("expressNo", expressNo);
        model1.addAttribute("address", address);
        model1.addAttribute("username", username);
        model1.addAttribute("phone", phone);
        model1.addAttribute("gName", gName);
        model1.addAttribute("gKind", gKind);

        return "mall/express";
    }

    /**
     * 查询物流信息
     *
     * @param expressNo 快递单号
     * @return
     */
    @RequestMapping("/express")
    @ResponseBody
    public Map<String, String> getExpressInfo(String exCompanyNo, String expressNo) {
        String data = orderService.getExpressList(exCompanyNo, expressNo);
        Map<String, String> expressList = new HashMap<String, String>();
        expressList.put("expressList", data);
        return expressList;
    }

    /**
     * 计算运费
     *
     * @return
     */
    @RequestMapping("/expressAmount")
    @ResponseBody
    public double getExpressAmount(@RequestBody OrderGoodsExpTempDTO goodsExpTemp) {
        double amount = orderService.getExpAmount(goodsExpTemp);
        return amount;
    }

    /**
     * 订单详情页面
     */
    @RequestMapping("/orderDetail")
    public String toOrderDetailPage(HttpServletRequest request, Map<String, Object> model) {
        loginInfo(request, model);
        model.put("orderId", request.getParameter("orderId"));
        return "mall/orderdetail";
    }

    /**
     * 订单详情数据
     */
    @RequestMapping("/orderDetailInfo")
    @ResponseBody
    public EOrderDetailDTO getEOrderDetailInfo(@ObjectIdType ObjectId orderId) {
        return orderService.getEOrderDetailInfo(orderId);
    }
}
