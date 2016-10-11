<%@ include file="/common/taglib.jsp" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="en">
    <head>
    	<title>公司人员管理</title>
        <meta charset="utf-8">
        <script type="text/javascript" src="${ctx }/js/jquery-1.11.1.js"></script>
        <link rel="shortcut icon" href="${ctx }/images/page_logo1.png" >
        <link rel="stylesheet" type="text/css" href="${ctx }/css/bootstrap.min.css">
        <script type="text/javascript" src="${ctx }/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="${ctx }/js/jqPaginator.js"></script>
        <link rel="stylesheet" type="text/css" href="${ctx }/css/reset.css">
        <script type="text/javascript" src="${ctx }/js/member.js"></script>
        <script type="text/javascript" src="${ctx }/js/jquery.chained.js"></script>
        <script type="text/javascript">
        	$(function() {
        		var totalPage = ${totalPage};
        		var context = "" || '${ctx}'
        		pagenation(totalPage, context);
        		
				$('#ed_sec_sel').chained("#ed_first_sel"); 
				$('#new_sec_sel').chained("#new_first_sel"); 
        	});
        </script>
    </head>
    <body>
    	<input type="hidden" id="ctx" value="${ctx }">
    	<div class="header">
    		<button onclick="window.location.href='${ctx}/user/logout'">退出</button>
    		<p class="p2">${loginedStaff.name }&nbsp;&nbsp;&nbsp;${loginedStaff.jobTitle }<br>${loginedStaff.loginName }</p>
    		<img class="img2" src="${ctx }/images/img_top_head.png">
    		<img class="img1" src="${ctx }/images/top_logo.png">
    		<p class="p1">复兰项目管理</p>
    	</div>
    	<div class="left-nav">
    		<ul>
    			<li class="li1" onclick="window.location.href='${ctx}/project/new_project'">
    				<div class="li1-img"></div>
    				<p>创建新项目</p>
    			</li>
    			<li class="li2" onclick="window.location.href='${ctx}/project/list'">
    				<div class="li2-img"></div>
    				<p>我的项目清单</p>
    			</li>
    			<li class="li3" onclick="window.location.href='${ctx}/staff/list'">
    				<div class="li3-imgi"></div>
    				<p class="p-blue">公司人员管理</p>
    			</li>
    		</ul>
    	</div>

    	<div class="container">
            <div class="member-cont">
                <p class="p-member-top">公司人员管理
                    <span><em>+</em>添加</span>
                </p>
                <table class="tab-member mt38">
                    <tr>
                        <th class="th1">工号</th>
                        <th class="th2">姓名</th>
                        <th class="th3">性别</th>
                        <th class="th4">部门</th>
                        <th class="th5">职位</th>
                        <th class="th6">操作</th>
                    </tr>
                </table>
                <table class="tab-member mt13" id="member">
                </table>
                

                <div class="page" id="paginaion">
                    <ul id="list" class="pagination"></ul>
                </div>
            </div>
    	</div>

    
        <div class="bg"></div>

        <!--编辑员工信息弹窗-->
        <div class="wind-edit wind">
            <p class="p1">编辑员工信息<em></em></p>
            <ul class="ul-infor">
                <li>
                    <span class="sp1">工号</span>
                    <input type="text" class="inp1" id="ed_staffNum">
                </li>
                <li>
                    <span class="sp1">姓名</span>
                    <input type="text" class="inp1" id="ed_name">
                </li>
                <li id="ed_is_owner">
                	<span class="sp1">项目负责人</span>
                	<label>
                        <input type="radio" name="isPrjOwner" value="1">是
                    </label>
                    <label>
                        <input type="radio" name="isPrjOwner" value="0">否
                    </label>
                </li>
                <li id="ed_gender">
                    <span class="sp1">性别</span>
                    <label>
                        <input type="radio" name="sex" value="1">男
                    </label>
                    <label>
                        <input type="radio" name="sex" value="0">女
                    </label>
                </li>
                <li id="ed_depart">
                    <span class="sp1">部门</span>
                    <select id="ed_first_sel">
                    	<c:forEach items="${departmentList }" var="department">
                    		<option value="${department.id }">${department.departmentName }</option>
                    	</c:forEach>
                    </select>
                </li>
                <li id="ed_sub_depart">
                    <span class="sp1">子部门</span>
                    <select id="ed_sec_sel">
                    	<c:forEach items="${departmentList }" var="department">
                    		<c:forEach items="${department.subDepartments }" var="subDepart">
                    			<option value="${subDepart.id }" class="${department.id }">${subDepart.subDepartmentName }</option>
                    		</c:forEach>
                    	</c:forEach>
                    </select>
                </li>
                <li>
                    <span class="sp1">职位</span>
                    <input type="text" class="inp1" id="ed_jbTitle">
                </li>
                <li class="tcenter">
                    <button class="btn-ok" id="updateBtn">确定</button>
                    <button class="btn-no">取消</button>
                </li>
            </ul>
        </div>
        
        <!-- 删除员工信息弹窗 -->
        <div class="wind-del wind">
            <p class="p1">提示<em></em></p>
            <ul class="ul-infor">
                <li>是否确定删除?</li>
                <li class="tcenter">
                    <button class="btn-ok" id='deleteBtn'>确定</button>
                    <button class="btn-no">取消</button>
                </li>
            </ul>
        </div>
        
        <!--添加员工信息弹窗-->
        <div class="wind-new wind">
            <p class="p1">添加员工信息<em></em></p>
            <ul class="ul-infor">
                <li>
                    <span class="sp1">工号</span>
                    <input type="text" class="inp1" id="new_staffNum">
                </li>
                <li>
                    <span class="sp1">姓名</span>
                    <input type="text" class="inp1" id="new_name">
                </li>
                <li>
                	<span class="sp1">登录名</span>
                    <input type="text" class="inp1" id="new_login_name">
                </li>
                <li>
                	<span class="sp1">密码</span>
                    <input type="password" class="inp1" id="new_pwd">
                </li>
                <li id="new_is_owner">
                	<span class="sp1">项目负责人</span>
                	<label>
                        <input type="radio" name="isPrjOwner" value="1">是
                    </label>
                    <label>
                        <input type="radio" name="isPrjOwner" value="0">否
                    </label>
                </li>
                <li id="new_gender">
                    <span class="sp1">性别</span>
                    <label>
                        <input type="radio" name="sex" value="1">男
                    </label>
                    <label>
                        <input type="radio" name="sex" value="0">女
                    </label>
                </li>
                <li id="new_depart">
                    <span class="sp1">部门</span>
                    <select id="new_first_sel">
                    	<c:forEach items="${departmentList }" var="department">
                    		<option value="${department.id }">${department.departmentName }</option>
                    	</c:forEach>
                    </select>
                </li>
                <li id="new_sub_depart">
                    <span class="sp1">子部门</span>
                    <select id="new_sec_sel">
                    	<c:forEach items="${departmentList }" var="department">
                    		<c:forEach items="${department.subDepartments }" var="subDepart">
                    			<option value="${subDepart.id }" class="${department.id }">${subDepart.subDepartmentName }</option>
                    		</c:forEach>
                    	</c:forEach>
                    </select>
                </li>
                <li>
                    <span class="sp1">职位</span>
                    <input type="text" class="inp1" id="new_jbTitle">
                </li>
                <li class="tcenter">
                    <button class="btn-ok" id="addBtn">确定</button>
                    <button class="btn-no">取消</button>
                </li>
            </ul>
        </div>
    </body>
</html>