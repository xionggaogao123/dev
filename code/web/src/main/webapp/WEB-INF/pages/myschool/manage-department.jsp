<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理校园-复兰科技 K6KT-快乐课堂</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/department/manage.css">
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <%--<link rel="stylesheet" type="text/css" href="/static/css/manage/news.css"/>--%>
     <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <style type="text/css">

        .reset-teacher-password {
            padding: 7px 0px;
            background-color: #FF5555;
            margin-left: 150px;
            width: 130px;
            font-size: 14px;
            color: #fff;
            font-weight: bold;
            text-align: center;
            border-radius: 3px;
            cursor: pointer;
        }

        .Wdate {
            border: #999 1px solid;
            height: 20px;
            background: #fff url(/img/datePicker.gif) no-repeat right;
        }

        .Wdate::-ms-clear {
            display: none;
        }

        .WdateFmtErr {
            font-weight: bold;
            color: red;
        }
    </style>
</head>
<body>
<%@ include file="../common_new/head.jsp"%>

<div id="content_main_container">
    <div id="content_main">
        <!-- 左侧导航-->
       <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->

        <!-- right start-->
        <div class="modal-bg"></div>
        <div id="right-container">
            <!--广告-->
            <%@ include file="../common/right.jsp" %>
            <!--/广告-->
            <div class="main-container">
               <script id="department_script" type="text/x-dot-template">
						{{~it:value:index}}
                              <li>
                                <div class="list-content" onclick="gotodetail('{{=value.id}}')">
                                    <span>{{=value.value}}</span>
                                </div>
                                <i class="fa fa-trash-o list-delete  list-li" onclick="departmentdel('{{=value.id}}')"></i>
                            </li>
						{{~}}
				</script>
                <div class="manage-right-container">

                    <div class="list-container class-list" belong="class">
                        <div class="list-title" style="line-height:28px;">
                            <img src="/img/K6KT/list_3.png" style="margin-right: 5px;float:left;">
                            <span  style="cursor:pointer;" >部门列表 &gt; </span>
                        </div>
                        <div class="list-add-class"  ><i class="fa fa-plus"></i> <span onclick="showDiv()">新建部门</span></div>
                        <ul class="list-ul" id="department_li">
                        
                        
                          <!-- 
                            <li>
                                <div class="list-content">
                                    <span>一年级1班</span>
                                </div>
                                <i class="fa fa-trash-o list-delete"></i>
                            </li>
                             -->
                        </ul>

                    </div>


                    <div class="edit-container">
                        <!-- 设置默认密码 -->
                        <div class="edit-info-container edit-password" id="create_div">
                            <div class="edit-title-bar">
                                <div>增加部门</div>
                                <i class="fa fa-times close-modal" onclick="closeDiv()"></i>
                            </div>
                            <div class="edit-form">
                                <div class="edit-input-group">
                                    <span>名称</span>
                                    <input type="text" class="edit-password-input" id="department_name">
                                </div>
                            </div>
                        </div>

                        <div class="edit-commit-btn-container">
                            <div class="edit-commit-btn" onclick="departmentadd()">确定</div>
                        </div>
                    </div>
                </div>

            </div>
            <!-- right end-->
        </div>
    </div>
</div>

<%@ include file="../common_new/foot.jsp" %>

</body>
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('department',function(department){
    	department.init();
    });
</script>
</html>