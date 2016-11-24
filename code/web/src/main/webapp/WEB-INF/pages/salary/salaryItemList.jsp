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
    boolean isAdmin = UserRole.isManager(new BaseController().getSessionValue().getUserRole());
%> 
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="#"/>
    <title>复兰科技</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet"/>
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/salaryItem.css?v=2015071201" rel="stylesheet"/>
    <link href="<%=basePath%>static/css/homepage.css" type="text/css" rel="stylesheet">
    <script type="text/javascript"
            src="<%=basePath%>static_new/js/modules/core/0.1.0/jquery.min.js?v=2015041602"></script>
</head>
<body>
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">
 

    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <div class="col-right">
        <!--.txt-info-->
        <div class="txt-info">
            <div class="tab-head-new clearfix">
                <ul>
                    <li class="cur"><a href="itemList.do">工资项目管理</a></li>
                    <li><a href="showTable.do">薪酬表格</a></li>
                </ul>
            </div>
            <div class="salaryItem-list">
                <dl>
                    <dt>工资项目编辑</dt>
                    <dd class="clearfix">
                        <button type="button" class="green-btn">添加项目</button>
                        <label>*本次工资薪酬项目修改在下一次制表中生效</label>
                    </dd>
                </dl>
                <div class="salaryItem-table">
                    <div>
                        <table>
                            <thead>
                            <th width="20%">编号</th>
                            <th width="25%">项目名称</th>
                            <th width="25%">类型</th>
                            <th width="30%">操作</th>
                            </thead>
                        </table>
                    </div>
                    <div class="salaryItem-d">
                        <table>
                            <tbody class="salaryItem-body">
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <!--/.txt-info-->
    </div>
    <!--/.col-right-->
</div>
<div class="pop-wrap" id="addSalaryItem">
    <div class="pop-title">新建项目</div>
    <div class="pop-content">
        <div class="pop-list clearfix">
            <dl class="item">
                <dt>项目名称</dt>
                <dd>
                    <input class="item-input" type="text" name="itemInfo.itemName" id="itemName"/>
                </dd>
            </dl>
        </div>
        <div class="pop-list clearfix">
            <dl class="item">
                <dt>项目类型</dt>
                <dd>
                    <select class="item-input" name="itemInfo.type" id="itemType">
                        <option value="发款">发款</option>
                        <option value="扣款">扣款</option>
                    </select>
                </dd>
            </dl>
        </div>
    </div>
    <div class="pop-btn"><span class="active" id="saveItem">确定</span><span>取消</span></div>
</div>
<div class="pop-wrap" id="editSalaryItem">
    <div class="pop-title">编辑项目</div>
    <div class="pop-content">
        <div class="pop-list clearfix">
            <dl class="item">
                <dt>项目名称</dt>
                <dd>
                    <input type="hidden" id="editItemId"/>
                    <input class="item-input" type="text" name="itemInfo.itemName" value="${itemInfo.itemName}"
                           id="editItemName"/>
                </dd>
            </dl>
        </div>
        <div class="pop-list clearfix">
            <dl class="item">
                <dt>项目类型</dt>
                <dd>
                    <select class="item-input" name="itemInfo.type" id="editItemType">
                        <option value="发款">发款</option>
                        <option selected="selected" value="扣款">扣款</option>
                    </select>
                </dd>
            </dl>
        </div>
    </div>
    <div class="pop-btn">
        <span class="active" id="updateItem">确定</span><span>取消</span>
    </div>
</div>
<div class="bg-dialog"></div>
<!--/#content-->
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<script type="text/template" id="itemListTemp">
    {{~it:value:index}}
    <tr>
        <td>{{=index+1}}</td>
        <td>{{=value.itemName}}</td>
        <td>{{=value.type}}</td>
        <td><a href="#" class="jc-edit" data-name="{{=value.id}}"></a>|<a href="#" class="jc-del"
                                                                          data-name="{{=value.id}}"></a></td>
    </tr>
    {{~}}
</script>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('salaryItem', function (salaryItem) {
        var data = ${salaryItemList};
        salaryItem.loadData(data);
    });
</script>
</body>
</html>