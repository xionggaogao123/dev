<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ page import="java.util.UUID"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>用户基本信息</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/css/nivo-slider.css" type="text/css"/>
    <link rel="stylesheet" href="/static/css/main-nivo-thumb.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/message.css"/>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/js/experienceScore.js"></script>
    <script src="/static/js/jquery.nivo.slider.js"></script>
    <script type="text/javascript" src="/static/sharedpart.js"></script>
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>
        <div id="right-container">


            <div id="content_main_container" style="margin-top: 5px;">
                <div id="main-content" style="position: relative; overflow:hidden;" class="main-content-msg">
                    <div id="account-right">
                        <div id="account-right-title">
                            <div id="account-r-sel">
                                <a href="/basic" target="_self" style="color: #fcbc5e!important;border: 1px solid #848484!important;border-bottom: 1px solid white!important;width: 100px!important;height: 26px!important;margin-top: -7px!important;line-height: 24px!important;background: white;display: inline-block!important;z-index: 999999999999;">基本信息</a>
                                <a href="/editphoto" target="_self">修改头像</a>
                                <a href="/password" target="_self">修改密码</a>
                                <%--<a href="" target="-_self">基本信息</a>--%>
                                <div class="clear"></div>
                            </div>
                        </div>
                        <input id="cacheKeyId"  name="cacheKeyId" type="hidden" />
                        <div id="account-right-content" class="account-right-content">
                            <form action="">
                                <table class="account-tb">
                                    <tr>
                                        <td class="account-tb-r">基本信息（“<i>*</i>”为必填项）</td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td class="account-tb-r"><i>*</i> 用户登录名：</td>
                                        <td><input id="userName" type="text" name="" id="userName"
                                                   style="border:1px solid #CCC; height: 20px;padding: 2px;width: 200px" value="${loginName}"/>
                                        </td>
                                        <td>可用于账号登录</td>
                                    </tr>
                                    <tr>
                                        <td class="account-tb-r"> 图片验证码：</td>
                                        <td><input id="verifyCode" type="text" name=""
                                                   style="border:1px solid #CCC; height: 20px;padding: 2px;width: 74px" />
                                            <img id="imgObj" alt="" src="/verify/verifyCode.do" style="position: relative;top: 7px;left: 2px" onclick="changeImg()"/>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr class="account-tb-w">
                                        <td class="account-tb-r">手机：</td>
                                        <td><input type="text" name="mobileNUmber" id="mobileNUmber" 
                                                   style="border:1px solid #CCC;height: 20px;padding: 2px;width: 200px" value="${mobile}"/>
                                        </td>

                                        <td>
                                            <span onclick="getVerificationCode()">发送短信校验码</span>
                                        </td>
                                    </tr>
                                    <tr class="account-tb-e" id="cacheKeyId_tr" style="display: none;">
                                        <td class="account-tb-r">手机验证码：</td>
                                        <td>
                                            <input type="text" name="valiCode" id="valiCode"
                                                   style="border:1px solid #CCC;height: 20px;padding: 2px;width: 115px;background: #eeeeee"/>
                                        </td>
                                        <td>
                                             <span class="account-C" onclick="getVerificationCode()">重新发送</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="account-tb-r">电子邮箱：</td>
                                        <td><input type="text" name="email" id="email"
                                                   style="border:1px solid #CCC; border-radius: 4px;height: 20px;padding: 2px;width: 200px" value="${email}"/>
                                        </td>
                                        <td></td>
                                    </tr>
                                    <tr>
                                        <td class="account-tb-r">性别：</td>
                                        <td>
                                        
                                        
                                        
                                         <c:choose>
                                           <c:when test="${sex==1}">
                                              <input type="radio" name="sex" id=""
                                                   style="" value="1" checked="checked"/>男
                                              <input type="radio" name="sex" id=""
                                                   style="" value="0"/>女
                                           </c:when>
                                           
                                            <c:when test="${sex==0}">
                                              <input type="radio" name="sex" id=""
                                                   style="" value="1" />男
                                              <input type="radio" name="sex" id=""
                                                   style="" value="0" checked="checked"/>女
                                           </c:when>
                                           
                                           <c:otherwise>
                                              <input type="radio" name="sex" id=""
                                                   style="" value="1" />男
                                              <input type="radio" name="sex" id=""
                                                   style="" value="0" />女
                                           </c:otherwise>
                                         </c:choose>
                                           
                                            
                                        </td>
                                        <td></td>
                                    </tr>
      
                                    <tr>
                                        <td > <input  class="orange-submit-btn" value="保存" onclick="sendInfos()"/></td>
                                        <td colspan="2">
                                           
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>


<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('userBasic');
</script>
</html>
