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
    <link rel="stylesheet" type="text/css" href="/static/css/manage/news.css"/>
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
        .select2-container{
            width: 300px;
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

        
        <input id="depId" type="hidden" value="${depId}">
        <!--
        <div class="modal-bg"></div>
        -->
        <div id="right-container">
            <div class="main-container">


                 <script id="member_script" type="text/x-dot-template">
						{{~it:value:index}}
                            <li>
                                <div class="list-content">
                                    <span>{{=value.value}}</span>
                                </div>
                                <i class="fa fa-trash-o list-delete list-li" onclick="delMember('{{=value.idStr}}')"></i>
                            </li>
						{{~}}
				</script>
				
				
				
                <div class="manage-right-container">

                    <!-- 各班列表 -->
                    <div class="list-container pre-class-list" belong="pre-class" >
                        <div class="list-title" style="line-height:28px;">
                            <img src="/img/K6KT/list_3.png" style="margin-right: 5px;float:left;">
                            <span onclick="gotoDepartList()" style="cursor:pointer;">部门列表 &gt; </span>
                            <span class="list-title-grade-pre" style="cursor:pointer;" id="dep_name"></span>
                        </div>
                        
                        <div class="list-class-add class-add-student" onclick="showAddMember()"><i class="fa fa-plus"></i> 添加成员</div>
                         <div class="list-class-add class-add-student" onclick="gotoFile()"><i class="fa fa-plus"></i>查看部门文档</div>
                        <ul class="list-ul class-student-list" id="member_div">

                          <!-- 
                            <li >
                                <div class="list-content">
                                    <span>王子恒</span>
                                </div>
                                <i class="fa fa-trash-o list-delete"></i>
                            </li>
                             -->
                        </ul>
                    </div>
                        
                       <script id="department_user_script" type="text/x-dot-template">
						{{~it:value:index}}
                             <option value="{{=value.idStr}}" >{{=value.value}}</option>
						{{~}}
				    </script>
				
                          <!-- 编辑学生 -->
                        <div class="edit-info-container edit-student"  id="addMember_div" style="border:1px solid #A4A4A4;position: fixed;top: 0;margin: 50px 80px;">
                            <div class="edit-title-bar">
                                <div>添加成员</div>
                                <i class="fa fa-times close-modal" onclick="hideAddMember()"></i>
                            </div>
                            <div class="edit-form">

                                <div class="edit-input-group">
                                    <span>成员姓名</span>
                                   
                                    
                                    <select id="department_user" multiple>
                                      
                                    </select>
                                </div>
                                

                               <input class="edit-commit-btn" type="button" value="确定" onclick="addMember()">
                               
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
    	department.depmembers();
    });
</script>
</html>