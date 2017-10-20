package com.fulaan.mall.controller;

import com.fulaan.annotation.LoginInfo;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.BaseController;
import com.fulaan.forum.service.FLogService;
import com.fulaan.mall.service.EBusinessCartService;
import com.pojo.app.SessionValue;
import com.pojo.ebusiness.EBusinessCartGoods;
import com.pojo.ebusiness.EBusinessCartPageDTO;
import com.pojo.forum.FLogDTO;
import com.pojo.utils.MongoUtils;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.RespObj;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 购物车实现
 *
 * @author fourer
 */
@Api(value="购物车实现",hidden = true)
@Controller
@RequestMapping("/jxmapi/mall/cars")
public class DefaultEBusinessCartController extends BaseController {

    private static final Logger EBusinessLog = Logger.getLogger("EBusiness");
    private EBusinessCartService eShoppingCartService = new EBusinessCartService();
    private FLogService fLogService = new FLogService();
    @ApiOperation(value = "获取群聊详情", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = String.class)})
    @RequestMapping("/load")
    @LoginInfo
    public String loadCart(HttpServletRequest request, Map<String, Object> model) {
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

        FLogDTO fLogDTO = new FLogDTO();
        fLogDTO.setActionName("cart");
        SessionValue sv = getSessionValue();
        if (null != sv && !sv.isEmpty()) {
            fLogDTO.setPersonId(sv.getId());
        }
        fLogDTO.setPath("/mall/cars/load.do");
        fLogDTO.setTime(System.currentTimeMillis());
        fLogService.addFLog(fLogDTO);

        return "mall/cart";
    }

    /**
     * 将商品添加到购物车
     *
     * @param goodsId
     * @param kinds
     * @param count
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "将商品添加到购物车", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = List.class)})
    @RequestMapping("/add")
    @ResponseBody
    public List<EBusinessCartGoods> addGoods(@ApiParam(name = "goodsId", required = true, value = "goodsId") @ObjectIdType ObjectId goodsId, String kinds,
                                             @RequestParam(defaultValue = "0") int count,
                                             @RequestParam(defaultValue = "0", required = false) Integer type) throws Exception {

        EBusinessLog.info("CarAdd:" + "userId:" + getUserId() + ";goodsId:" + goodsId + ";count=" + count + ";学校：" + getSessionValue().getSchoolName());
        List<ObjectId> objKinds = MongoUtils.convert(kinds);
        List<EBusinessCartGoods> list = eShoppingCartService.addGoods(getUserId(), goodsId, objKinds, count, type);
        return list;
    }


    /**
     * 从购物车取出商品
     *
     * @return
     * @throws ClassNotFoundException
     * @throws java.io.IOException
     */
    @ApiOperation(value = "从购物车取出商品", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = EBusinessCartPageDTO.class)})
    @RequestMapping("/list")
    @ResponseBody
    public EBusinessCartPageDTO getGoodsFromCars(@RequestParam(defaultValue = "-1", required = false) Integer type) throws ClassNotFoundException, IOException {
        EBusinessLog.info("CarGet:" + "userId:" + getUserId() + ";学校：" + getSessionValue().getSchoolName());
        List<EBusinessCartGoods> list = eShoppingCartService.getGoodsFromCars(getUserId(), type);
        return new EBusinessCartPageDTO(list);
    }


    /**
     * 删除商品
     *
     * @param ebcId
     * @return
     * @throws ClassNotFoundException
     * @throws java.io.IOException
     */
    @ApiOperation(value = "删除商品", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = List.class)})
    @RequestMapping("/del")
    @ResponseBody
    public List<EBusinessCartGoods> deleteEGoods(@ApiParam(name = "ebcId", required = true, value = "ebcId") @ObjectIdType ObjectId ebcId) throws ClassNotFoundException, IOException {
        EBusinessLog.info("CarDel:" + "userId:" + getUserId().toString() + ";学校：" + getSessionValue().getSchoolName());
        List<EBusinessCartGoods> list = eShoppingCartService.deleteEGoods(getUserId(), ebcId);
        return list;
    }

    /**
     * 批量删除商品
     */
    @ApiOperation(value = "批量删除商品", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = List.class)})
    @RequestMapping("/delMultiple")
    @ResponseBody
    public List<EBusinessCartGoods> deleteEGoods(String ebcIds) throws ClassNotFoundException, IOException {
        EBusinessLog.info("CarDel:" + "userId:" + getUserId().toString() + ";学校：" + getSessionValue().getSchoolName());
        List<EBusinessCartGoods> list = eShoppingCartService.deleteEGoods(getUserId(), ebcIds);
        return list;
    }


    /**
     * 更改所售商品数量
     *
     * @param ebcId
     * @param count
     * @return
     * @throws ClassNotFoundException
     * @throws java.io.IOException
     */
    @ApiOperation(value = "更改所售商品数量", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/update")
    @ResponseBody
    public RespObj updateCount(@ApiParam(name = "ebcId", required = true, value = "ebcId") @ObjectIdType ObjectId ebcId, int count) throws ClassNotFoundException, IOException {
        EBusinessLog.info("CarUpdate:" + "userId:" + getUserId().toString() + ";ebcId=" + ebcId.toString() + ";count=" + count + ";学校：" + getSessionValue().getSchoolName());
        eShoppingCartService.updateCount(getUserId(), ebcId, count);
        return RespObj.SUCCESS;
    }


    /**
     * 选择要购买的商品
     *
     * @param ebcIds
     * @return
     * @throws ClassNotFoundException
     * @throws java.io.IOException
     * @throws com.sys.exceptions.IllegalParamException
     */
    @ApiOperation(value = "选择要购买的商品", httpMethod = "POST", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成",response = RespObj.class)})
    @RequestMapping("/select")
    @ResponseBody
    public RespObj seleteEGoods(String ebcIds) throws ClassNotFoundException, IOException, IllegalParamException {
        List<ObjectId> selectEbcIds = MongoUtils.convert(ebcIds);
        eShoppingCartService.selectGoodsFromCars(getUserId(), selectEbcIds);
        return RespObj.SUCCESS;
    }


}
