<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.fulaan.base.controller.BaseController" %>
<%@ page import="com.pojo.user.UserRole" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>  
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

  
<%@page import="com.pojo.app.SessionValue"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%    
    String path = request.getContextPath();    
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
    pageContext.setAttribute("basePath",basePath);  
    int userRole = new BaseController().getSessionValue().getUserRole();
    boolean isAdmin = UserRole.isManager(userRole) || UserRole.isHeadmaster(userRole) || UserRole.isK6ktHelper(userRole);
    boolean isEdu = UserRole.isEducation(userRole);
%>  
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-学籍管理</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/growRecord.css?v=2015041602" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>
<script>
	var familyCanEdit = ${familyRole};
	var resumeCanEdit = ${resumeRole};
</script>
<style>
.pop-wrap {
  width: 740px;
  background: #fff;
  position: absolute;
  top: 30px;
  left: 50%;
  margin-left: -370px;
  z-index: 99;
  display: none;
}
.bg-dialog{
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,.7);
    position: absolute;
    top: 0;
    left: 0;
    z-index: 98;
    display: none;
}
.pop-title{
    height: 48px;
    line-height: 48px;
    border-bottom: 1px solid #808080;
    color: #5db75d;
    font-size: 18px;
    padding-left: 40px;
    background: #fff;
    display: block;
}
.pop-content{
    width: 100%;
    background: #fff;
    padding-top: 10px;
}
.pop-content table{
    width: 230px;
    margin: 0 auto;
}
.pop-content table td{
    line-height: 40px;
}
.pop-btn{
    width: 210px;
    margin: 30px auto;
}
.pop-btn span{
    display: inline-block;
    width: 76px;
    height: 28px;
    line-height: 28px;
    text-align: center;
    color: #000;
    font-size: 14px;
    border: 1px solid #d1d1d1;
    cursor: pointer;
}
.pop-btn span.active{
    background: #ffa800;
    border: 1px solid #ffa800;
    color: #fff;
    margin-right: 25px;
}
</style>
<body>


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <div class="col-right">

        <!--.banner-info-->
        <!--/.banner-info-->

        <div class="grow-col">

            <div class="grow-col-head clearfix">
                <h3>学籍管理</h3>
            </div>

            <div class="grow-tab-head clearfix">
                <ul>
                    <li class="cur">
                        <a href="#familyMemberId">家庭成员</a>
                    </li>
                    <li >
                        <a href="#studyResumeId">学习简历</a>
                    </li>
                    <li >
                        <a href="#punishRecordId">奖惩记录</a>
                    </li>
                </ul>
                <!-- <h4 class="click">家庭成员</h4>
                <h4>学习简历</h4>
                <h4>奖惩记录</h4> -->
            </div>

            <div class="xuejiManage">
                <!--.家庭成员-->
                <div class="familyMember" id="familyMemberId">
                	<c:if test="${familyRole}">
                    <div class="add">
                        <a href="#" class="addMember">+添加成员</a>    
                    </div>
                    </c:if>
                    <h4 class="gray-table-h4">${userName}的家庭成员</h4>
                    <table class="gray-table" width="100%">
                        <thead>
                            <th width="84">成员姓名</th>
                            <th width="72">成员关系</th>
                            <th width="56">民族</th>
                            <th width="82">出生年月</th>
                            <th width="50">性别</th>
                            <th width="162">联系地址</th>
                            <th width="108">联系电话</th>
                            <th width="90">操作</th>
                        </thead>
                        <tbody id="memberListArea">
                                  
                        </tbody>
                    </table>
                </div>
                <!--.家庭成员-->

                <!--.学习简历-->
                <div class="hide studyResume" id="studyResumeId">
                	<c:if test="${resumeRole}">
                    <div class="add">
                        <a href="#" class="addResume">+添加简历</a>             
                    </div>
                    </c:if>
                    <h4 class="gray-table-h4">${userName}的学习简历</h4>
                    <table class="gray-table" width="100%">

                        <thead>
                        <th width="88">起止日期</th>
                        <th width="90">终止日期</th>
                        <th width="110">入学方式</th>
                        <th width="74">就读方式</th>
                        <th width="94">学习单位</th>
                        <th width="180">学习简历备注</th>
                        <th >操作</th>
                        </thead>
                        <tbody id="resumeListArea">
                                                             
                        </tbody>
                    </table>
                </div>
                <!--.学习简历-->

                <!--.奖惩记录-->
                <div class="hide punishRecord" id="punishRecordId">
                    
                    <h4 class="gray-table-h4">${userName}的奖惩记录</h4>
                    <table class="gray-table" width="100%">
                        <thead>
                        <th width="110">奖惩日期</th>
                        <th width="110">类型</th>
                        <th width="110">等级</th>
                        <th>内容</th>
                        </thead>
                        <tbody id="rewardListArea">
                        </tbody>
                    </table>
                </div>
                <!--.奖惩记录-->
            </div>

        </div>

    </div>
    <!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %> 
<!--#foot-->
<!-- 添加家庭成员 -->
<div class="pop-wrap" id="addFalmilyWindow">
    <div class="pop-title">添加家庭成员</div>
    <div class="pop-content add1 clearfix">
        <div>
            <form>
                <table class="addFamilyTable">
                    <tbody>
                        <tr>
                            <td class="tdRight">成员姓名</td>
                            <td><input type="text" id="memberName_add"></td>
                            <td class="tdRight">成员关系</td>
                            <td>
                                <select id="memberRelation_add">
                                    <option value="父亲">父亲</option>
                                    <option value="母亲">母亲</option>
                                    <option value="夫妻">夫妻</option>
                                    <option value="子女">子女</option>
                                </select>
                            </td>                           
                        </tr>
                        <tr>
                            <td class="tdRight">民族</td>
                            <td>
								<select id="memberRace_add">
                                    <option value="汉族">汉族</option>
                                    <option value="其他">其他</option>
                                </select>
							</td>
                            <td class="tdRight">国籍地区</td>
                            <td>
                                <select id="memberNationality_add">
                                    <option value="中国">中国</option>
                                    <option value="香港">香港</option>
                                    <option value="台湾">台湾</option>
                                    <option value="澳门">澳门</option>
                                    <option value="其他">其他</option>
                                </select></td>
                        </tr>
                        <tr>
                            <td class="tdRight">性别</td>
                            <td>
								<select id="memberSex_add">
                                    <option value="1">男</option>
                        			<option value="0">女</option>
                                </select>
							</td>
                            <td class="tdRight">出生日期</td>
                            <td>
                                <input type="text" id="memberBirthday_add" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate" />

                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight">学历</td>
                            <td>
                                <select id="memberEducation_add">
                                    <option value="小学">小学</option>
                                    <option value="初中">初中</option>
                                    <option value="高中">高中</option>
                                    <option value="大专">大专</option>
                                    <option value="本科">本科</option>
                                    <option value="硕士">硕士</option>
                                    <option value="博士">博士</option>
                                </select>
                            </td>
                            <td class="tdRight">从业</td>
                            <td>
                                <select id="memberWork_add">
                                    <option value="公务员">公务员</option>
                                    <option value="工人">工人</option>
                                    <option value="农民">农民</option>
                                    <option value="学生">学生</option>
                                    <option value="职员">职员</option>
                                    <option value="军人">军人</option>
                                    <option value="退休人员">退休人员</option>
                                    <option value="其他">其他</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight">政治面貌</td>
                            <td>
                                <select id="memberPolitics_add">
                                	<option value="群众">群众</option>
                                    <option value="共青团员">共青团员</option>
                                    <option value="中共党员">中共党员</option>
                                </select>
                            </td>
                            <td class="tdRight">健康状态</td>
                            <td>
                                <select id="memberHealth_add">
                                    <option value="健康">健康</option>
                        			<option value="一般">一般</option>
                        			<option value="较差">较差</option>
                        			<option value="残疾">残疾</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight" valign="top">现在住址</td>
                            <td>
                                <textarea id="memberAddressNow_add"></textarea>
                            </td>
                            <td class="tdRight" valign="top">户籍地址</td>
                            <td>
                                <textarea id="memberAddressRegistration_add"></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight">联系电话</td>
                            <td><input type="text" id="memberPhone_add"></td>
                            <td class="tdRight">电子邮箱</td>
                            <td><input type="text" id="memberEmail_add"></td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <div class="pop-btn">
        <span class="active" id="member_save_btn_add">保存</span>
        <span class="closeBtnClass">取消</span>
    </div>
</div>
<c:if test="${familyRole}">
	<!-- 编辑家庭成员 -->
<div class="pop-wrap" id="editFalmilyWindow">
    <div class="pop-title">编辑家庭成员</div>
    <div class="pop-content add1 clearfix">
        <div>
            <form>
                <table class="addFamilyTable">
                    <tbody>
                        <tr>
                            <td class="tdRight">成员姓名</td>
                            <td><input type="text" id="memberName_edit"></td>
                            <td class="tdRight" >成员关系</td>
                            <td>
                                <select id="memberRelation_edit">
                                    <option value="父亲">父亲</option>
                                    <option value="母亲">母亲</option>
                                    <option value="夫妻">夫妻</option>
                                    <option value="子女">子女</option>
                                </select>
                            </td>                           
                        </tr>
                        <tr>
                            <td class="tdRight">民族</td>
                            <td>
								<select id="memberRace_edit">
                                    <option value="汉族">汉族</option>
                                    <option value="其他">其他</option>
                                </select>
							</td>
                            <td class="tdRight">国籍地区</td>
                            <td>
                                <select id="memberNationality_edit">
                                    <option value="中国">中国</option>
                                    <option value="香港">香港</option>
                                    <option value="台湾">台湾</option>
                                    <option value="澳门">澳门</option>
                                    <option value="其他">其他</option>
                                </select>
                           </td>
                        </tr>
                        <tr>
                            <td class="tdRight">性别</td>
                            <td>
								<select id="memberSex_edit">
                                    <option value="1">男</option>
                        			<option value="0">女</option>
                                </select>
							</td>
                            <td class="tdRight">出生日期</td>
                            <td>
                                <input type="text" id="memberBirthday_edit" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate" />

                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight">学历</td>
                            <td>
                                <select id="memberEducation_edit">
                                    <option value="小学">小学</option>
                                    <option value="初中">初中</option>
                                    <option value="高中">高中</option>
                                    <option value="大专">大专</option>
                                    <option value="本科">本科</option>
                                    <option value="硕士">硕士</option>
                                    <option value="博士">博士</option>
                                </select>
                            </td>
                            <td class="tdRight">从业</td>
                            <td>
                                <select id="memberWork_edit">
                                    <option value="公务员">公务员</option>
                                    <option value="工人">工人</option>
                                    <option value="农民">农民</option>
                                    <option value="学生">学生</option>
                                    <option value="职员">职员</option>
                                    <option value="军人">军人</option>
                                    <option value="退休人员">退休人员</option>
                                    <option value="其他">其他</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight">政治面貌</td>
                            <td>
                                <select id="memberPolitics_edit">
                                    <option value="群众">群众</option>
                                    <option value="共青团员">共青团员</option>
                                    <option value="中共党员">中共党员</option>
                                </select>
                            </td>
                            <td class="tdRight">健康状态</td>
                            <td>
                                <select id="memberHealth_edit">
                                    <option value="健康">健康</option>
                        			<option value="一般">一般</option>
                        			<option value="较差">较差</option>
                        			<option value="残疾">残疾</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight" valign="top">现在住址</td>
                            <td>
                                <textarea id="memberAddressNow_edit"></textarea>
                            </td>
                            <td class="tdRight" valign="top">户籍地址</td>
                            <td>
                                <textarea id="memberAddressRegistration_edit"></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight">联系电话</td>
                            <td><input type="text" id="memberPhone_edit"></td>
                            <td class="tdRight">电子邮箱</td>
                            <td><input type="text" id="memberEmail_edit"></td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <div class="pop-btn">
        <span class="active" id="member_save_btn_edit">保存</span>
        <span class="closeBtnClass">取消</span>
    </div>
</div>
</c:if>
<c:if test="${!familyRole}">
	<!-- 查看家庭成员 -->
<div class="pop-wrap" id="editFalmilyWindow">
    <div class="pop-title">查看家庭成员</div>
    <div class="pop-content add1 clearfix">
        <div>
            <form>
                <table class="addFamilyTable">
                    <tbody>
                        <tr>
                            <td class="tdRight">成员姓名</td>
                            <td><input type="text" id="memberName_edit" readonly="readonly"></td>
                            <td class="tdRight" >成员关系</td>
                            <td>
                                <select id="memberRelation_edit" disabled="disabled">
                                    <option value="父亲">父亲</option>
                                    <option value="母亲">母亲</option>
                                    <option value="夫妻">夫妻</option>
                                    <option value="子女">子女</option>
                                </select>
                            </td>                           
                        </tr>
                        <tr>
                            <td class="tdRight">民族</td>
                            <td>
								<select id="memberRace_edit" disabled="disabled">
                                    <option value="汉族">汉族</option>
                                    <option value="其他">其他</option>
                                </select>
							</td>
                            <td class="tdRight">国籍地区</td>
                            <td>
                                <select id="memberNationality_edit" disabled="disabled">
                                    <option value="中国">中国</option>
                                    <option value="香港">香港</option>
                                    <option value="台湾">台湾</option>
                                    <option value="澳门">澳门</option>
                                    <option value="其他">其他</option>
                                </select>
                           </td>
                        </tr>
                        <tr>
                            <td class="tdRight">性别</td>
                            <td>
								<select id="memberSex_edit" disabled="disabled">
                                    <option value="1">男</option>
                        			<option value="0">女</option>
                                </select>
							</td>
                            <td class="tdRight">出生日期</td>
                            <td>
                                <input readonly="readonly" type="text" id="memberBirthday_edit"  class="Wdate" />

                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight">学历</td>
                            <td>
                                <select id="memberEducation_edit" disabled="disabled">
                                    <option value="小学">小学</option>
                                    <option value="初中">初中</option>
                                    <option value="高中">高中</option>
                                    <option value="大专">大专</option>
                                    <option value="本科">本科</option>
                                    <option value="硕士">硕士</option>
                                    <option value="博士">博士</option>
                                </select>
                            </td>
                            <td class="tdRight">从业</td>
                            <td>
                                <select id="memberWork_edit" disabled="disabled">
                                    <option value="公务员">公务员</option>
                                    <option value="工人">工人</option>
                                    <option value="农民">农民</option>
                                    <option value="学生">学生</option>
                                    <option value="职员">职员</option>
                                    <option value="军人">军人</option>
                                    <option value="退休人员">退休人员</option>
                                    <option value="其他">其他</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight">政治面貌</td>
                            <td>
                                <select id="memberPolitics_edit" disabled="disabled">
                                    <option value="群众">群众</option>
                                    <option value="共青团员">共青团员</option>
                                    <option value="中共党员">中共党员</option>
                                </select>
                            </td>
                            <td class="tdRight">健康状态</td>
                            <td>
                                <select id="memberHealth_edit" disabled="disabled">
                                    <option value="健康">健康</option>
                        			<option value="一般">一般</option>
                        			<option value="较差">较差</option>
                        			<option value="残疾">残疾</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight" valign="top">现在住址</td>
                            <td>
                                <textarea id="memberAddressNow_edit" readonly="readonly"></textarea>
                            </td>
                            <td class="tdRight" valign="top">户籍地址</td>
                            <td>
                                <textarea id="memberAddressRegistration_edit" readonly="readonly"></textarea>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight">联系电话</td>
                            <td><input type="text" id="memberPhone_edit" readonly="readonly"></td>
                            <td class="tdRight">电子邮箱</td>
                            <td><input type="text" id="memberEmail_edit" readonly="readonly"></td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <div class="pop-btn">
        <span class="active" id="member_save_btn_edit" style="display:none">保存</span>
        <span class="closeBtnClass" style="width:158px">关闭</span>
    </div>
</div>
</c:if>

<!-- 添加学习简历 -->
<div class="pop-wrap" id="addStudyHistoryWindow">
    <div class="pop-title">添加学习简历</div>
    <div class="pop-content add1 clearfix">
        <div>
            <form>
                <table class="addResumeTable">
                    <tbody>
                        <tr>
                            
                            <td class="tdRight">起始日期</td>
                            <td>
                                <input type="text" id="resumeSD_add" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate" />

                            </td>
                            <td class="tdRight">终止日期</td>
                            <td>
                                <input type="text" id="resumeED_add" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate" />

                            </td>

                        </tr>
                                                                      
                        <tr>
                            <td class="tdRight">入学方式</td>
                            <td>
                                <select id="entrance_add">
                                    <option value="统一招生考试">统一招生考试</option>
                                    <option value="保送">保送</option>
                                    <option value="艺术特招">艺术特招</option>
                                    <option value="体育特招">体育特招</option>
                                    <option value="留学">留学</option>
                                    <option value="其他">其他</option>
                                </select>

                            </td>
                            <td class="tdRight">就读方式</td>
                            <td>
                                <select id="studyType_add">
                                    <option value="走读">走读</option>
                                    <option value="住校">住校</option>
                                    <option value="借宿">借宿</option>
                                    <option value="其他">其他</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight" valign="top">学习单位</td>
                            <td>
                                <textarea id="studyCompany_add"></textarea>
                            </td>
                            <td class="tdRight beizhu" valign="top">学习简历备注</td>
                            <td>
                                <textarea id="resumePostscript_add"></textarea>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <div class="pop-btn">
        <span class="active" id="resume_save_btn_add">保存</span>
        <span class="closeBtnClass">取消</span>
    </div>
</div>

<c:if test="${resumeRole}">
	<!-- 编辑学习简历 -->
<div class="pop-wrap" id="editStudyHistoryWindow">
    <div class="pop-title">编辑学习简历</div>
    <div class="pop-content add1 clearfix">
        <div>
            <form>
                <table class="addResumeTable">
                    <tbody>
                        <tr>
                            
                            <td class="tdRight">起始日期</td>
                            <td>
                                <input type="text" id="resumeSD_edit" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate" />

                            </td>
                            <td class="tdRight">终止日期</td>
                            <td>
                                <input type="text" id="resumeED_edit" onfocus="WdatePicker({minDate:'{%y-100}-%M-%d',maxDate:'{%y+100}-%M-%d'})" class="Wdate" />

                            </td>

                        </tr>
                                                                      
                        <tr>
                            <td class="tdRight">入学方式</td>
                            <td>
                                <select id="entrance_edit">
                                    <option value="统一招生考试">统一招生考试</option>
                                    <option value="保送">保送</option>
                                    <option value="艺术特招">艺术特招</option>
                                    <option value="体育特招">体育特招</option>
                                    <option value="留学">留学</option>
                                    <option value="其他">其他</option>
                                </select>

                            </td>
                            <td class="tdRight">就读方式</td>
                            <td>
                                <select id="studyType_edit">
                                    <option value="走读">走读</option>
                                    <option value="住校">住校</option>
                                    <option value="借宿">借宿</option>
                                    <option value="其他">其他</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight" valign="top">学习单位</td>
                            <td>
                                <textarea id="studyCompany_edit"></textarea>
                            </td>
                            <td class="tdRight beizhu" valign="top">学习简历备注</td>
                            <td>
                                <textarea id="resumePostscript_edit"></textarea>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <div class="pop-btn">
        <span class="active" id="resume_save_btn_edit">保存</span>
        <span class="closeBtnClass">取消</span>
    </div>
</div>
</c:if>
<c:if test="${!resumeRole}">
	<!-- 查看学习简历 -->
<div class="pop-wrap" id="editStudyHistoryWindow">
    <div class="pop-title">查看学习简历</div>
    <div class="pop-content add1 clearfix">
        <div>
            <form>
                <table class="addResumeTable">
                    <tbody>
                        <tr>
                            
                            <td class="tdRight">起始日期</td>
                            <td>
                                <input readonly="readonly" type="text" id="resumeSD_edit"  class="Wdate" />

                            </td>
                            <td class="tdRight">终止日期</td>
                            <td>
                                <input readonly="readonly" type="text" id="resumeED_edit"  class="Wdate" />

                            </td>

                        </tr>
                                                                      
                        <tr>
                            <td class="tdRight">入学方式</td>
                            <td>
                                <select id="entrance_edit" disabled="disabled">
                                    <option value="统一招生考试">统一招生考试</option>
                                    <option value="保送">保送</option>
                                    <option value="艺术特招">艺术特招</option>
                                    <option value="体育特招">体育特招</option>
                                    <option value="留学">留学</option>
                                    <option value="其他">其他</option>
                                </select>

                            </td>
                            <td class="tdRight">就读方式</td>
                            <td>
                                <select id="studyType_edit" disabled="disabled">
                                    <option value="走读">走读</option>
                                    <option value="住校">住校</option>
                                    <option value="借宿">借宿</option>
                                    <option value="其他">其他</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="tdRight" valign="top">学习单位</td>
                            <td>
                                <textarea id="studyCompany_edit" readonly="readonly"></textarea>
                            </td>
                            <td class="tdRight beizhu" valign="top">学习简历备注</td>
                            <td>
                                <textarea id="resumePostscript_edit" readonly="readonly"></textarea>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <div class="pop-btn">
        <span class="active" id="resume_save_btn_edit" style="display:none">保存</span>
        <span class="closeBtnClass" style="width:158px">关闭</span>
    </div>
</div>
</c:if>




<div class="bg-dialog"></div>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
		var rewardList = ${rewards}; 
		var memberList = ${members};
		var resumeList = ${resumes};
		var PUBLIC_USER_NAME = "${userName}";
		var PUBLIC_USER_ID = "${userId}";
        seajs.use('family');
</script>
</body>
</html>