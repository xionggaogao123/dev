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
%> 
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="#" />
    <title>复兰科技</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" /> 
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/jiangcheng.css?v=2015071201" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
</head>
<script>
	var rewardType = {
		ALL : '全部',
		REWARD : '表彰奖励',
		PUNISHMENT : '违纪违规'
		
	};
	var rewardLevel = {
		PUNISHMENT : ['口头警告','警告','严重警告','记过处分','留校察看','开除学籍'],
		REWARD : ['特等奖','一等奖','二等奖','三等奖']
	};
	var jsonObjAll = ${message};
	var grades = jsonObjAll.grades;
	var classes = jsonObjAll.classes;
	var departments = jsonObjAll.departments;
	var rewardListCatch;
	
	
</script>
<body>


    <!--#head-->
    <%@ include file="../common_new/head.jsp" %>
    <!--/#head-->

    <!--#content-->
    <div id="content" class="clearfix">


        <!--.col-left-->
       <%@ include file="../common_new/col-left.jsp" %>

 
        <!--/.col-left-->
        <!--.banner-info-->
        <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
        </c:choose>
        <!--/.banner-info-->
        <!--.col-right-->
        <div class="col-right">



            <!--.txt-info-->
            <div class="txt-info">
                <div class="tab-head-new clearfix">
                    <ul>
                        <li class="cur">
                            <i></i>
                            <a href="#jiangchengTab">表彰违纪</a>
                        </li>
                     </ul>
                </div>
                <div class="jiangcheng-list" id="jiangchengTab">
                    <dl>
                       <%-- <dt>查询</dt>--%>
                        <dd class="clearfix">
                            <form>
                            <label>
                                <span>学期</span>
                                <select>
                                    <option value="2016第二学期">2016第二学期</option>
                                    <option value="2016第一学期">2016第一学期</option>
                                    <option value="2015第二学期">2015第二学期</option>
                                </select>
                            </label>
                            <label>
                                <span>年级</span>
                                <select id="pageGradeSelect" >
                                </select>
                            </label>
                            <label>
                                <span>班级</span>
                                <select id="pageClassSelect">
                                </select>
                            </label>
                            <label>
                                <span>类型</span>
                                <select id="pageType" >
                                    <option value='ALL'>全部</option>
                            		<option value="REWARD">表彰奖励</option>
                            		<option value="PUNISHMENT">违纪违规</option>
                                </select>
                            </label>
                            <label>
                                <span>等级</span>
                                <select id="pageLevelSelect" >
                                    <option value='ALL'>全部</option>
                        			<option value="特等奖" >特等奖</option>
                            		<option value="一等奖">一等奖</option>
                            		<option value="二等奖">二等奖</option>
                            		<option value="三等奖">三等奖</option>
                            		<option value="口头警告">口头警告</option>
                            		<option value="警告">警告</option>
                            		<option value="严重警告">严重警告</option>
                            		<option value="记过处分">记过处分</option>
                            		<option value="留校察看">留校察看</option> 
                            		<option value="开除学籍">开除学籍</option>
                            		
                                </select>
                            </label>
                            <label>
                                <span>学生姓名</span>
                                <input type="text" id="pageStudentName"/>
                            </label>
                            <button type="button" class="green-btn">查询</button>
                            </form>
                        </dd>
                    </dl>

                    <div class="jiangcheng-table">
                       <%-- <h4>列表</h4>--%>
                        <div class="btn-list">
                        <% if(isAdmin){ %>
                            <button class="green-btn green-btnn">新增</button>
                        <%} %>
                            <button class="btn green-btnn" id="exportBtn">导出</button>
                        </div>
                        <div class="jiangcheng-tab">
                            <table>
                            <thead>
                                <%--<th width="40">#</th>--%>
                                <th width="100">姓名</th>
                                <th width="100">年级</th>
                                <th width="100">班级</th>
                                <th width="100">类型</th>
                                <th width="100">等级</th>
                                <%--<th width="100">内容</th>--%>
                                <th width="100">日期</th>
                                <% if(isAdmin){ %>
                                <th>操作</th>
                                <%} %>
                            </thead>
                            <tbody id="rewardList">
                            </tbody>
                        </table>
                        </div>
                    </div>
					<!-- 分页div -->
					 <div class="new-page-links">
                    </div>

                </div>

                </div>
            </div>
            <!--/.txt-info-->
			


        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->

    <!--#foot-->
    <%@ include file="../common_new/foot.jsp" %> 

    <!--#foot-->


    <div class="pop-wrap" id="editJiangCheng">
        <div class="pop-title">表彰违纪设置</div>
        <div class="pop-content">
            <div class="pop-list clearfix">
                <dl class="item items">
                    <%--<dt>姓名</dt>--%>
                    <dd id="editName"></dd>
                </dl>
                <dl class="item items">
                   <%-- <dt>年级</dt>--%>
                    <dd id="editGrade"></dd>
                </dl>
                <dl class="item items">
                  <%--  <dt >班级</dt>--%>
                    <dd id="editClass"></dd>
                </dl>
            </div>
            <div class="pop-list clearfix">
                <dl class="item tiemm">
                    <dt>类型</dt>
                    <dd >
                    	<select class="item-input" id="editTypeSelect">
                        	<option value="REWARD">表彰奖励</option>
                            <option value="PUNISHMENT">违纪违规</option>
                    	</select> 
                    </dd>
                </dl>

                <dl class="item tiemm">
                    <dt>等级</dt>
                    <dd>
                        <select class="item-input" id="editLevelSelect">
                        	
                    	</select> 
                    </dd>
                </dl>
                <dl class="item tiemm">
                    <dt>日期</dt>
                    <dd>
                        <input type="text" onClick="WdatePicker()" id="editDateInput"/>
                    </dd>
                </dl>
            </div>
            <div class="pop-list clearfix">
                <dl class="enter-content">
                    <dt>描述</dt>
                    <dd><textarea id="editRewardContent"></textarea></dd>
                </dl>
            </div>
            <div class="pop-fanwei">
                <h5>公示范围</h5>
                <label class="fclass-all"><input type="checkbox" id="editHoleSchoolCheckbox"/>全校</label>
                <dl class="fanwei-list clearfix" id="editHoleDepartment">
                    <dt></dt>
                    <dd>
                        <div class="single"><label><input type="checkbox" id="editAllDepartment">所有部门</span></div>
                        <div class="single" id="editDepartmentBox">
                            
                        </div>
                    </dd>
                </dl>
                <dl class="fanwei-list clearfix">
                    <dt></dt>
                    <dd id="editClassBox">
                        <div class="all"><input type="checkbox">所有班级</div>
                    </dd>
                </dl>
            </div>
        </div>
        <div class="pop-btn"><span class="active" id="confirmEdit">确定</span><span>取消</span></div>
    </div>

    <div class="pop-wrap" id="jiLuJiangCheng">
        <div class="pop-title">记录详情</div>
        <div class="pop-content">

        </div>
        <div class="pop-btn"><span>关闭</span></div>
    </div>

    <div class="pop-wrap" id="addJiangCheng">
        <div class="pop-title">添加奖惩记录</div>
        <div class="pop-content">
            <div class="pop-list clearfix">
                <dl class="item">
                    <dt>年级</dt>
                    <dd >
                        <select class="item-input" id="addGradeSelect" >
                        </select>
                    </dd>
                </dl>
                <dl class="item" >
                    <dt>班级</dt>
                    <dd >
                        <select class="item-input" id="addClassSelect" >
                        </select>
                    </dd>
                </dl>
                <dl class="item">
                    <dt>类型</dt> 
                    <dd >
                        <select class="item-input" id="addTypeSelect" >
                            <option value="REWARD">表彰奖励</option>
                            <option value="PUNISHMENT">违纪违规</option>
                        </select>
                    </dd>
                </dl>
                <dl class="item">
                    <dt>等级</dt>
                    <dd>
                        <select class="item-input" id="addLevelSelect">
                            <option value="特等奖" >特等奖</option>
                            <option value="一等奖">一等奖</option>
                            <option value="二等奖">二等奖</option>
                            <option value="三等奖">三等奖</option>
                        </select>
                    </dd>
                </dl>
            </div>
            <div class="pop-list clearfix">

                 <dl class="item">
                    <dt >日期</dt>
                    <dd>
                        <input type="text" onClick="WdatePicker()" id="addDateInput"/>
                    </dd>
                </dl>
            </div>
            <div class="xuan-student">
                <div class="student-title clearfix">
                    <span>选择学生</span>
                    <%--<label><input type="checkbox" id="addClassAllBox">全选</label>--%>
                </div>
                <div class="student-box clearfix" id="studentBox">


                </div>
            </div>
            <div class="pop-list clearfix">
                <dl class="enter-content">
                    <dt>描述</dt>
                    <dd><textarea id="addRewardContent"></textarea></dd>
                </dl>
            </div>
            <div class="pop-fanwei" id="addRange">
                <h5>公示范围</h5>
                <label class="fclass-all"><input type="checkbox" id="addHoleSchoolCheckbox"/>全校</label> 
                <dl class="fanwei-list clearfix" id="addHoleDepartment">
                    <dt></dt>
                    <dd>
                        <div class="all"><label><input type="checkbox" id="addAllDepartment">所有部门</label></div>
                        <div class="single" id="departmentBox">
                        </div>
                    </dd>
                </dl>
                <dl class="fanwei-list clearfix">
                    <dt></dt>
                    <dd id="classBox">
                        <div class="all"><label><input type="checkbox" id="ALL">所有班级</label></div>
                        <!--  <div class="single clearfix"><label class="fclass"></label></div>-->
                    </dd>
                    
                </dl>
            </div>
        </div>
        <div class="pop-btn"><span class="active" id="confirmAdd">确定</span><span>取消</span></div>
    </div>

    <div class="bg-dialog"></div>

	<form action="/reward/exportRewards.do" method="post" id="exportForm">
		<input type="text"  id="gradeIdForm" name="gradeId"/>
		<input type="text" id="classIdForm" name="classId"/>
		<input type="text" id="rewardTypeForm" name="rewardType"/>
		<input type="text" id="rewardGradeForm" name="rewardGrade"/>
		<input type="text" id="studentNameForm" name="studentName"/>
	</form>


    <script type="text/template" id="tiaojianId">
        {{~it:value:index}}
        <tr>
           <%-- <td>{{=index+1}}</td>--%>
            <td>{{=value.name}}</td>
            <td>{{=value.nianji}}</td>
            <td>{{=value.banji}}</td>
            <td>{{=value.type}}</td>
            <td>{{=value.dengji}}</td>
            <%--<td><a href="#" class="view" data-name="{{=value.id}}">查看</a></td>--%>
            <td>{{=value.date}}</td>
	<% if(isAdmin){ %>
            <td>
                <a href="#" class="view" data-name="{{=value.id}}">详情</a>|
                <a href="#" class="jc-edit" data-name="{{=value.id}}">编辑</a>|
                <a href="#" class="jc-del" data-name="{{=value.id}}">删除</a>
            </td>
	<% }%>
        </tr>
        {{~}}
    </script>
 
 	<!-- 分页模板 -->
 	<script type="text/template" id="pageTplId">

        {{ if(it.total>0){ }}
            <a href="#" data-page="1" {{ if(it.cur === 1) { }} class="dis" {{ }else{ }} class="prev-all" {{ } }}>首页</a>
            <a href="#" data-page="{{=it.cur-1}}" {{ if(it.cur === 1) { }} class="dis" {{ }else{ }} class="prev" {{ } }}>上一页</a>
            {{ for(var i=1;i<=it.total;i++){ }}
                    {{ if(it.cur === i){ }}
                        <span>{{=i}}</span>
                    {{ }else{ }}
                        <a href="#" data-page="{{=i}}">{{=i}}</a>
                    {{ } }}

                    {{ if(i>10){ }}

                        {{ if(it.cur > 12 && it.cur < it.total-2){ }}
                            <em>...</em>
                            <span>{{=it.cur}}</span>
                            <em>...</em>
                            <a href="#" data-page="">{{=it.total-1}}</a>
                            <a href="#" data-page="">{{=it.total}}</a>
                        {{ }else if(it.cur > it.total-2 && it.cur < it.total){ }}
                            <em>...</em>
                            <span>{{=it.cur}}</span>
                            <a href="#">{{=it.total}}</a>
                        {{ }else if(it.cur === 12){ }}
                            <span>{{=it.cur}}</span>
                            <em>...</em>
                            <a href="#">{{=it.total-1}}</a>
                            <a href="#">{{=it.total}}</a>
                        {{ }else{ }}
                            <em>...</em>
                            <span>{{=it.cur}}</span>
                        {{}}}

                        {{ break; }}
                    {{ } }}

            {{ } }}
            <a href="#" data-page="{{=it.cur+1}}" {{ if(it.cur === it.total) { }} class="dis" {{ }else{ }} class="next" {{ } }}>下一页</a>
            <a href="#" data-page="{{=it.total}}"  {{ if(it.cur === it.total) { }} class="dis" {{ }else{ }} class="next-all" {{ } }}>末页</a>
        {{ } }}

    </script>
 
    <script type="text/template" id="viewListId">
        <div class="pop-list clearfix">
            <dl class="item">
                <dt>姓名</dt>
                <dd class="item-input">{{=it.name}}</dd>
            </dl>
            <dl class="item">
                <dt>年级</dt>
                <dd class="item-input">{{=it.nianji}}</dd>
            </dl>
            <dl class="item">
                <dt>班级</dt>
                <dd class="item-input">{{=it.banji}}</dd>
            </dl>
        </div>
        <div class="pop-list clearfix">
            <dl class="item">
                <dt>类型</dt>
                <dd class="item-input">{{=it.type}}</dd>
            </dl>
            <dl class="item">
                <dt>等级</dt>
                <dd class="item-input">{{=it.dengji}}</dd>
            </dl>
            <dl class="item">
                <dt>日期</dt>
                <dd class="item-input">{{=it.date}}</dd>
            </dl>
        </div>
        <div class="pop-list clearfix">
            <dl class="enter-content">
                <dt>内容</dt>
                <dd><textarea readonly>{{=it.contents}}</textarea></dd>
            </dl>
        </div>
    </script>

    <script type="text/template" id="addListId">
        {{~it:value:index}}
        <label><input type="checkbox" value="{{=value.id}}">{{=value.name}} </label>
        {{~}}
			
    </script>
	
	<script type="text/template" id="editListId">
        {{~it:value:index}}
        <label><input type="checkbox" value="EDIT-{{=value.id}}" id="EDIT-{{=value.id}}">{{=value.name}} </label>
        {{~}}
			
    </script>
	
    <script type="text/template" id="editListId">

        <label><input type="checkbox">韩梅梅 </label>

    </script>



    <!-- Javascript Files -->
    <!-- initialize seajs Library --> 
    <script src="<%=basePath%>static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script src="<%=basePath%>static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    

    <script>
        seajs.use('jiangcheng');
        
    </script>
  
</body>
</html>