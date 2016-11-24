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
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">

    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery ui styles -->
    <link href="<%=basePath%>static_new/js/modules/treeView/0.2.0/zTreeStyle.css" rel="stylesheet" />
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/qicaidengji.css?v=2015071201" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <style>
        .pop-wrap {
            width: 360px;
            background: #fff;
            position: absolute;
            top: 30px;
            left: 50%;
            margin-left: -180px;
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
</head>
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



            <!--.tab-col-new-->
            <div class="tab-col-new">
                <div class="tab-head-new clearfix">
                    <ul>
                        <li class="cur">
                            <i></i>
                            <a href="#">器材登记</a>
                        </li>
                    </ul>
                </div>
                <div class="tab-main-new clearfix">

                    <!--.treeview-->
                    <div class="treeview-cols">

                        <div class="treeview-head clearfix">
                            <h3>器材分类</h3>
                            <span>
                                <a href="#" class="add-folder"></a>
                                <a href="#" class="add-file"></a>
                                <a href="#" class="minus-file"></a>
                                <!-- <a href="#" class="icon-down"></a>
                                <a href="#" class="icon-up"></a>  -->
                            </span>
                        </div>

                        <ul id="treeDemo" class="ztree"></ul>

                    </div>
                    <!--/.treeview-->

                    <!--.treefrom-->
                    <div class="treeform">

                        <h3>使用器材</h3>
                        <form action="#">
                            <table class="form-table">
                                <tr>
                                    <td width="96">分类序号：</td>
                                    <td id="equipmentClassificationIndex"></td>
                                </tr>
                                <tr>
                                    <td>分类名称：</td>
                                    <td><input type="text" id="equipmentClassificationName"/></td>
                                </tr>
                                <tr>
                                    <td>分类描述：</td>
                                    <td><input type="text" id="equipmentClassificationPostscript"/></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                    <td><button type="submit" class="orange-big-btn" id="updateEquipmentClassificationBtn">保存分类设置</button></td>
                                </tr>
                            </table> 
                        </form>

                        <div class="treeform-right">
                            <div class="treeform-head clearfix">
                                <h4>分类器材</h4>
                                <span>
                                    <a href="#" class="green-btn" id="addEquipmentBtn">添加器材</a>
                                    <a href="#" class="gray-line-btn" id="exportModelBtn">导出模版</a> 
                                    <a href="#" class="gray-line-btn" id="showImportWindowBtn">导入数据</a>
                                </span>
                            </div>

                            <table class="gray-table" width="100%">
                                <thead>
                                    <th width="40"><label><!-- <input type="checkbox" />  -->#</label></th>
                                    <th width="80">器材名称</th>
                                    <th width="50">产地</th>
                                    <th width="60">品牌</th>
                                    <th width="70">规格</th>
                                    <th width="70">使用登记</th>
                                    <th>查看详情</th>
                                </thead>
                                <tbody id='equipmentList'>
                                    
                                </tbody>
                            </table>
                            <!-- 分页   -->
							<div class="new-page-links" id="pageForView">
                    		</div>
                        </div>

                    </div>
                    <!--/.treefrom-->

                </div>
            </div>
            <!--/.tab-col-new-->

        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->

    <!--#foot-->
    <%@ include file="../common_new/foot.jsp" %>
    <!--#foot-->
    <!-- 新增器材 -->
    <div class="pop-wrap" id="addQicaiId">
        <div class="pop-title">新增器材</div>
        <div class="pop-content">
            <table>
                <tr>
                    <td width="70">器材标号</td>
                    <td><input type="text" id="addEquipmentNumber"/></td>
                </tr>
                <tr>
                    <td>器材名称</td>
                    <td><input type="text" id="addEquipmentName"/></td>
                </tr>
                <tr>
                    <td>器材规格</td>
                    <td><input type="text" id="addEquipmentSpecifications"/></td>
                </tr>
                <tr>
                    <td>器材产地</td>
                    <td><input type="text" id="addEquipmentOrgin"/></td>
                </tr>
                <tr>
                    <td>器材品牌</td>
                    <td><input type="text" id="addEquipmentBrand"/></td>
                </tr>
                <tr>
                    <td>使用人员</td>
                    <td><input type="text" id="addEquipmentUserName"/></td>
                </tr>
            </table>
        </div>
        <div class="pop-btn"><span class="active" id="addEquipmentConfirmBtn">确定</span><span>取消</span></div>
    </div>
<!-- 查看器材 -->
    <div class="pop-wrap" id="viewQicaiId">
        <div class="pop-title">查看器材</div>
        <div class="pop-content">
            <table>
                <tr>
                    <td width="70" readOnly="true" >器材标号</td>
                    <td><input type="text" readOnly="true" id="viewEquipmentNumber"/></td>
                </tr>
                <tr>
                    <td>器材名称</td>
                    <td><input type="text" readOnly="true" id="viewEquipmentName"/></td>
                </tr>
                <tr>
                    <td>器材规格</td>
                    <td><input type="text" readOnly="true" id="viewEquipmentSpecifications"/></td>
                </tr>
                <tr>
                    <td>器材产地</td>
                    <td><input type="text" readOnly="true" id="viewEquipmentOrgin"/></td>
                </tr>
                <tr>
                    <td>器材品牌</td>
                    <td><input type="text" readOnly="true" id="viewEquipmentBrand"/></td>
                </tr>
                <tr>
                    <td>使用人员</td>
                    <td><input type="text" readOnly="true" id="viewEquipmentUserName"/></td>
                </tr>
            </table>
        </div>
        <div class="pop-btn"><span class="active" style="width:210px">关闭</span></div>
    </div>
	<!-- 编辑器材 -->
	<div class="pop-wrap" id="editQicaiId">
        <div class="pop-title">编辑器材</div>
        <div class="pop-content">
            <table>
                <tr>
                    <td width="70">器材标号</td>
                    <td><input type="text" id="editEquipmentNumber"/></td>
                </tr>
                <tr>
                    <td>器材名称</td>
                    <td><input type="text" id="editEquipmentName"/></td>
                </tr>
                <tr>
                    <td>器材规格</td>
                    <td><input type="text" id="editEquipmentSpecifications"/></td>
                </tr>
                <tr>
                    <td>器材产地</td>
                    <td><input type="text" id="editEquipmentOrgin"/></td>
                </tr>
                <tr>
                    <td>器材品牌</td>
                    <td><input type="text" id="editEquipmentBrand"/></td>
                </tr>
                <tr>
                    <td>使用人员</td>
                    <td><input type="text"  id="editEquipmentUserName"/></td>
                </tr>
            </table>
        </div>
        <div class="pop-btn"><span class="active" id="editConfirmBtn">确定</span><span>取消</span></div>
    </div>
    <!-- 器材导入 -->
    <div class="pop-wrap daoru" id="importWindow">
    <div class="pop-title">器材导入</div>
    <div class="pop-content">
        <form enctype="multipart/form-data" id="importForm">
            <ul>
                <li>
                    <input id="uploadFile" name="scoreData" type="file" accept=".xls,.xlsx">
                </li>
            </ul>
        </form>
    </div>
    <div class="pop-btn"><span class="active" id="beginImportBtn">导入</span><span>取消</span></div>
</div>
    <div class="bg-dialog"></div>
<!-- 导出用表单 -->
	<form action="/equipment/exportModel.do" method="post" id="exportModelForm" style="display:none">
		<input type="text" id="equipmentClassificationIdReport" name="equipmentClassificationId"/>
	</form>
    <!-- Javascript Files -->
    <!-- initialize seajs Library -->
    <script src="<%=basePath%>static_new/js/sea.js"></script>
    <!-- Custom js -->
    <script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
    <script>
        seajs.use('qcdj');
    </script>
</body>
</html>