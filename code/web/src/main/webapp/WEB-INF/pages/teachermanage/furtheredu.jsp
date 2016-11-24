<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2016/2/29
  Time: 15:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
  <meta name="renderer" content="webkit">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link href="/static_new/css/teachermanage/furtheredu.css" rel="stylesheet" />
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static/plugins/uploadify/uploadify.css"/>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script src="/static_new/js/modules/webuploader/webuploader.min.js"></script>
  <%--<script src="/static_new/js/modules/webuploader/imgUpload.js"></script>--%>
  <%--<script type="text/javascript" src="/static/js/WdatePicker.js"></script>--%>

  <%--<script type="text/javascript" src="/static_new/js/modules/teachermanage/0.1.0/furtheredu.js"></script>--%>
</head>
<body>


<!--#head-->
<%@ include file="../common_new/head.jsp"%>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

  <!--.col-left-->
  <%--<div class="col-left">--%>
    <%@ include file="../common_new/col-left.jsp" %>
    <!--广告-->
    <c:choose>
      <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
        <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
      </c:when>
      <c:otherwise>
        <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
      </c:otherwise>
    </c:choose>
  <%--</div>--%>
  <!--/.col-left-->

  <!--.col-right-->
  <div class="col-right">

    <!--.tab-col-->
    <div class="tab-col">
      <div class="main-top">
        <div class="main-top2 clearfix ">
          <span class="span-top span1">教师管理</span>
          <span class="span2">教师成长档案</span>
          <span class="span3">教师课程项目</span>
        </div>
      </div>
      <input type="hidden" id="userid">
      <input type="hidden" id="etype">
      <div class="cont-main1">
        <dl class="dl-main">
          <dd>
            <select id="grade">
              <option value="">全年级</option>
              <c:forEach items="${gradelist}" var="grade">
                <option value="${grade.id}">${grade.name}</option>
              </c:forEach>
            </select>
            <select id="customize">
              <option value="0">全部编制</option>
              <option value="1">在编在岗</option>
              <option value="2">在编不在岗（休假）</option>
              <option value="3"> 在编不在岗（待退休）</option>
              <option value="4">退休</option>
              <option value="5">外聘</option>
              <option value="6">派遣</option>
              <option value="7">退休返聘</option>
            </select>
            <select id="teachersel">
              <option value="0">全部教师</option>
              <option value="1">任教教师</option>
              <option value="2">未任课教师</option>
            </select>
            <input type="text" class="input-search">
            <span class="span-search">搜索</span>

          </dd>
          <dd class="clearfix">
            <button class="add-tea">添加老师</button>
          </dd>
          <dd class="clearfix">
            <table class="users">

            </table>
            <script type="text/template" id="user_templ">
              <tr>
              <th colspan="7" class="th0">教师列表</th>
              </tr>
              <tr>
                <th class="th0">#</th>
                <th class="th5">字母</th>
                <th class="th1">用户名</th>
                <th class="th2">任教情况</th>
                <th class="th3">职务名称</th>
                <th class="th3">当前权限</th>
                <th class="th4">操作</th>
              </tr>
              {{ if(it.rows.length>0){ }}
              {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
              {{var obj=it.rows[i];}}
              <tr>
                <th class="th0">{{=i+1}}</th>
                <td class="td6">{{=obj.letter}}</td>
                <td class="td1">{{=obj.name}}</td>
                <td class="td2">
                  {{~obj.infos:value:index}}
                  <span class="span-int">{{=value}}</span>
                  {{~}}
                </td>
                <td class="td3">{{=obj.postionDec}}</td>
                <td class="th3">{{=obj.role}}</td>
                <td class="td5">
                  <button class="btn-edit" tid="{{=obj.id}}">编辑</button>
                  <button class="btn-set" tid="{{=obj.id}}" tnm="{{=obj.name}}" tnb="{{=obj.jobNumber}}" usr="{{=obj.userRole}}" ism="{{=obj.ismanage}}">设置权限</button>
                  <button class="btn-reset" tid="{{=obj.id}}" tnm="{{=obj.name}}">重置密码</button>
                  <button class="btn-del" tid="{{=obj.id}}">删除</button>
                </td>
              </tr>
              {{ } }}
              {{ } }}
              <%--{{ if(it.rows.length>0){ }}--%>
              <%--{{ for (var j = 0, l = it.rows.length; j < l; j++) { }}--%>
              <%--{{var obj=it.rows[j];}--%>

              <%--{{}}}--%>
              <%--{{}}}--%>
            </script>
            <div class="new-page-links" id="pageDiv"></div>
          </dd>
        </dl>

      </div>


      <div class="cont-main1-1">
        <div><a class="back-jslb">&lt;返回教师列表页</a></div>
        <div class="add-title">添加老师</div>
        <table class="table-infor">
          <tr>
            <th colspan="4">基本信息</th>
          </tr>
          <tr>
            <td rowspan="4" class="td1">
                <img class="img-upload-pic" src="../../../static_new/images/origin_pic.png" width="87px" height="118px;" >
            </td>
            <td class="td2">用户名<i class="red-xing">*</i><span class="bothname">生成：<span id="teacher-name"></span></span></td>
            <td colspan="2"><input id="name3" style="width: 340px;"></td>
          </tr>
          <tr>
            <td >权限<i class="red-xing">*</i></td>
            <td colspan="2" class="td3">
              <select id="userRole" class="power">
                <option value="8">校领导</option>
                <option value="2">老师</option>
                <option value="64">管理员</option>
                <option value="2048">门卫</option>
                <option value="4096">宿舍管理员</option>
              </select>
              <span class="ismanageclass" style="display: none;"><input type="checkbox" id="ismanage" class="input-set">是否设置成管理员</span>
            </td>
          </tr>
          <tr>
            <td >职务<i class="red-xing">*</i></td>
            <td colspan="2" class="td3">
              <input type="text" id="postiondec">
            </td>
          </tr>
          <tr>
            <td >姓名<i class="red-xing">*</i></td>
            <td colspan="2" class="td3">
              <input type="text" id="realname">
            </td>
          </tr>
          <tr>
            <td>性别<i class="red-xing">*</i></td>
            <td colspan="2" class="td3">
              <select id="sex">
                <option value="1">男</option>
                <option value="0">女</option>
              </select>
            </td>
          </tr>
          <tr>
            <td >出生日期</td>
            <td>
              <input type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" id="birth">
            </td>
            <td class="td3">籍贯</td>
            <td class="td4">
              <input type="text" id="place">
            </td>
          </tr>
          <tr>
            <td>民族</td>
            <td class="td2">
              <select id="nation">
                <option value="0">汉族</option>
                <option value="1">其他</option>
              </select>
            </td>
            <td>身份证类型</td>
            <td class="td4">
              <select id="card">
                <option value="0">身份证</option>
                <option value="1">护照</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>身份证件号码</td>
            <td class="td2">
              <input type="text" id="cardnum">
            </td>
            <td>身份证件有效期</td>
            <td class="td4">
              <input type="text" id="vail">
            </td>
          </tr>
          <tr>
            <td>婚姻状况</td>
            <td class="td2">
              <select id="maritalstatus">
                <option value="0">未婚</option>
                <option value="1">已婚</option>
                <option value="2">离异</option>
              </select>
            </td>
            <td>政治面貌</td>
            <td class="td4">
              <select id="political">
                <option value="0">中共党员</option>
                <option value="1">共青团员</option>
                <option value="2">民主党派</option>
                <option value="3">无党派人士</option>
                <option value="4">群众</option>
                <option value="5">其他</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>户口所在地</td>
            <td class="td2">
              <textarea id="registerplace"></textarea>
            </td>
            <td>家庭住址</td>
            <td class="td4">
              <textarea id="adress"></textarea>
            </td>
          </tr>
          <tr>
            <td>现住址</td>
            <td class="td2">
              <textarea id="nowadress"></textarea>
            </td>
            <td>邮政编码</td>
            <td class="td4">
              <input type="text" id="zipcode">
            </td>
          </tr>
          <tr>
            <td>学段</td>
            <td class="td2">
                <select id="area">
                    <option value="0">小学</option>
                    <option value="1">初中</option>
                    <option value="2">高中</option>
                </select>
            </td>
            <td>紧急联系人</td>
            <td class="td4">
              <input type="text" id="contact">
            </td>
          </tr>
          <tr>
            <td>联系人电话</td>
            <td class="td2">
              <input type="text" id="contactphone">
            </td>
            <td>电子信箱</td>
            <td class="td4">
              <input type="text" id="email">
            </td>
          </tr>
          <tr>
            <td>初始学历</td>
            <td class="td2">
              <select id="education">
                <option value="0">大专</option>
                <option value="1">本科</option>
                <option value="2">硕士</option>
                <option value="3">博士</option>
              </select>
            </td>
            <td>专业</td>
            <td class="td4">
              <input type="text" id="major2">
            </td>
          </tr>
          <tr>
            <td>取得时间</td>
            <td class="td2">
              <input type="text" id="deutime"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
            </td>
            <td>初始学位</td>
            <td class="td4">
              <select id="degree2">
                <option value="0">学士</option>
                <option value="1">硕士</option>
                <option value="2">博士</option>
                <option value="3">名誉博士</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>取得时间</td>
            <td class="td2">
              <input type="text" id="degtime"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
            </td>
            <td>参加工作时间</td>
            <td class="td4">
              <input type="text" id="jobtime"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
            </td>
          </tr>
          <tr>
            <td>来校年月</td>
            <td class="td2">
              <input type="text" id="schooltime"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
            </td>
            <td>从教年月</td>
            <td class="td4">
              <input type="text" id="teachtime"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
            </td>
          </tr>
          <tr>
            <td>从教学科</td>
            <td class="td2">
              <select id="teachSubject">
                <option value="0">语文</option>
                <option value="1">数学</option>
                <option value="2">英语</option>
                <option value="3">历史</option>
                <option value="4">地理</option>
                <option value="5">政治</option>
                <option value="6">化学</option>
                <option value="7">生物</option>
                <option value="8">物理</option>
                <option value="9">美术</option>
                <option value="10">音乐</option>
                <option value="11">体育</option>
                <option value="12">计算机</option>
              </select>
            </td>
            <td>普通话等级</td>
            <td class="td4">
              <select id="mandarinlevel">
                <option value="0">一级甲等</option>
                <option value="1">一级乙等</option>
                <option value="2">二级甲等</option>
                <option value="3">二级乙等</option>
                <option value="4">三级甲等</option>
                <option value="5">三级乙等</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>教职工号</td>
            <td class="td2">
              <input type="text" id="teachernumber">
            </td>
            <td>编制</td>
            <td class="td4">
              <select id="organization">
                <option value="1">在编在岗</option>
                <option value="2">在编不在岗（休假）</option>
                <option value="3"> 在编不在岗（待退休）</option>
                <option value="4">退休</option>
                <option value="5">外聘</option>
                <option value="6">派遣</option>
                <option value="7">退休返聘</option>
              </select>
            </td>
          </tr>
        </table>

        <div class="table-div">
          <div class="table-div-title">职后学历</div>
          <table class="table-all table-all-1">
            <tr>
              <th class="th1">职后最高学历</th>
              <th class="th2">职后最高学位</th>
              <th class="th3">所学专业</th>
              <th class="th4">获取时间</th>
              <th class="th5">公开</th>
              <th class="th6">操作</th>
            </tr>
            <tbody id="addTr" class="addTr">

            </tbody>
            <script type="text/template" id="addTr_templ">
              {{ if(it.educationEntryList.length>0){ }}
              {{ for (var i = 0, l = it.educationEntryList.length; i < l; i++) { }}
              {{var obj=it.educationEntryList[i];}}
              <tr>
                <td>{{=obj.education}}</td>
                <td>{{=obj.degree}}</td>
                <td>{{=obj.major}}</td>
                <td>{{=obj.time}}</td>
                <td>{{?obj.open==1}}是{{??}}否{{?}}</td>
                <td>
                  <button class="btn-xx del1">删除</button>
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
            <tr>
              <td class="td1">
                <select id="edu1">
                  <option value="0">大专</option>
                  <option value="1">本科</option>
                  <option value="2">硕士</option>
                  <option value="3">博士</option>

                </select>
              </td>
              <td class="td2">
                <select id="degree1">
                  <option value="0">学士</option>
                  <option value="1">硕士</option>
                  <option value="2">博士</option>
                  <option value="3">名誉博士</option>
                </select>
              </td>
              <td class="td3">
                <input type="text" id="major">
              </td>
              <td class="td4">
                <input type="text" id="time1">
              </td>
              <td class="td5">
                <select id="open1">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </td>
              <td>
                <button class="add1">添加</button>
              </td>
            </tr>

          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">工作简历</div>
          <table class="table-all table-all-2">
            <tr>
              <th class="th7">工作时间</th>
              <th class="th3">工作单位</th>
              <th class="th4">所任职务</th>
              <th class="th5">公开</th>
              <th class="th6">操作</th>
            </tr>
            <tbody id="addTr2" class="addTr2">

            </tbody>
            <script type="text/template" id="addTr2_templ">
              {{ if(it.jobEntryList.length>0){ }}
              {{ for (var i = 0, l = it.jobEntryList.length; i < l; i++) { }}
              {{var obj=it.jobEntryList[i];}}
              <tr>
                <td>{{=obj.jobtime}}</td>
                <td>{{=obj.organization}}</td>
                <td>{{=obj.position}}</td>
                <td>{{?obj.open==1}}是{{??}}否{{?}}</td>
                <td>
                  <button class="btn-xx del2">删除</button>
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
            <tr>
              <td class="td1">
                <input type="text" id="time2">
              </td>
              <td class="td2">
                <input type="text" id="job">
              </td>
              <td class="td3">
                <input type="text" id="postion">
              </td>
              <td>
                <select id="open2">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </td>
              <td>
                <button class="add2">添加</button>
              </td>
            </tr>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">职称</div>
          <table class="table-all table-all-3">
            <tr>
              <th class="th1">名称</th>
              <th class="th2">获取时间</th>
              <th class="th3">岗位类别</th>
              <th class="th4">岗位等级</th>
              <th class="th7">聘任时间</th>
              <th class="th5">公开</th>
              <th class="th6">操作</th>
            </tr>
            <tbody id="addTr3" class="addTr3">

            </tbody>
            <script type="text/template" id="addTr3_templ">
              {{ if(it.titleEntryList.length>0){ }}
              {{ for (var i = 0, l = it.titleEntryList.length; i < l; i++) { }}
              {{var obj=it.titleEntryList[i];}}
              <tr>
                <td>{{=obj.name}}</td>
                <td>{{=obj.time}}</td>
                <td>{{=obj.jobType}}</td>
                <td>{{=obj.level}}</td>
                <td>{{=obj.appointmentTime}}</td>
                <td>{{?obj.open==1}}是{{??}}否{{?}}</td>
                <td>
                  <button class="btn-xx del3">删除</button>
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
            <tr>
              <td class="td1">
                <input type="text" id="name">
              </td>
              <td class="td2">
                <input type="text" id="time3">
              </td>
              <td class="td3">
                <input type="text" id="type" style="width: 85px;">
              </td>
              <td class="td4">
                <input type="text" id="level" style="width: 85px;">
              </td>
              <td class="td7">
                <input type="text" id="time4">
              </td>
              <td>
                <select id="open3">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </td>
              <td>
                <button class="add3">添加</button>
              </td>
            </tr>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">行政职务</div>
          <table class="table-all table-all-4">
            <tr>
              <th class="th1">任职时间</th>
              <th class="th2">任职单位</th>
              <th class="th3">所在职务</th>
              <th class="th5">公开</th>
              <th class="th6">操作</th>
            </tr>
            <tbody id="addTr4" class="addTr4">

            </tbody>
            <script type="text/template" id="addTr4_templ">
              {{ if(it.postionEntryList.length>0){ }}
              {{ for (var i = 0, l = it.postionEntryList.length; i < l; i++) { }}
              {{var obj=it.postionEntryList[i];}}
              <tr>
                <td>{{=obj.jobtime}}</td>
                <td>{{=obj.organization}}</td>
                <td>{{=obj.position}}</td>
                <td>{{?obj.open==1}}是{{??}}否{{?}}</td>
                <td>
                  <button class="btn-xx del4">删除</button>
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
            <tr>
              <td class="td1">
                <input type="text" id="time5">
              </td>
              <td class="td2">
                <input type="text" id="workplace">
              </td>
              <td class="td3">
                <input type="text" id="postion2">
              </td>
              <td>
                <select id="open4">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </td>
              <td>
                <button class="add4">添加</button>
              </td>
            </tr>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">社会兼职</div>
          <table class="table-all table-all-4">
            <tr>
              <th class="th1">任职时间</th>
              <th class="th2">任职单位</th>
              <th class="th3">所在职务</th>
              <th class="th5">公开</th>
              <th class="th6">操作</th>
            </tr>
            <tbody id="addTr5" class="addTr5">

            </tbody>
            <script type="text/template" id="addTr5_templ">
              {{ if(it.partTimeEntryList.length>0){ }}
              {{ for (var i = 0, l = it.partTimeEntryList.length; i < l; i++) { }}
              {{var obj=it.partTimeEntryList[i];}}
              <tr>
                <td>{{=obj.time}}</td>
                <td>{{=obj.unit}}</td>
                <td>{{=obj.position}}</td>
                <td>{{?obj.open==1}}是{{??}}否{{?}}</td>
                <td>
                  <button class="btn-xx del5">删除</button>
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
            <tr>
              <td class="td1">
                <input type="text" id="time6">
              </td>
              <td class="td2">
                <input type="text" id="workplace2">
              </td>
              <td class="td3">
                <input type="text" id="postion3">
              </td>
              <td>
                <select id="open5">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </td>
              <td>
                <button class="add5">添加</button>
              </td>
            </tr>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">获得成果（包括荣誉、论文发表或出版、课题与项目）</div>
          <table class="table-all table-all-5">
            <tr>
              <th class="th1">成果说明</th>
              <th class="th2">级别</th>
              <th class="th3">获得时间</th>
              <th class="th5">公开</th>
              <th class="th6">操作</th>
            </tr>
            <tbody id="addTr6" class="addTr6">

            </tbody>
            <script type="text/template" id="addTr6_templ">
              {{ if(it.resultEntryList.length>0){ }}
              {{ for (var i = 0, l = it.resultEntryList.length; i < l; i++) { }}
              {{var obj=it.resultEntryList[i];}}
              <tr>
                <td>{{=obj.introduce}}</td>
                <td>{{=obj.level}}</td>
                <td>{{=obj.time}}</td>
                <td>{{?obj.open==1}}是{{??}}否{{?}}</td>
                <td>
                  <button class="btn-xx del6">删除</button>
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
            <tr>
              <td class="td1">
                <input type="text" id="result">
              </td>
              <td class="td2">
                <input type="text" id="level2" style="width: 85px;">
              </td>
              <td class="td3">
                <input type="text" id="time7">
              </td>
              <td>
                <select id="open6">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </td>
              <td>
                <button class="add6">添加</button>
              </td>
            </tr>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">证书</div>
          <table class="table-all table-all-6">
            <tr>
              <th class="th1">获得时间</th>
              <th class="th2">证书名称</th>
              <th class="th3">成绩</th>
              <th class="th5">公开</th>
              <th class="th6">操作</th>
            </tr>
            <tbody id="addTr7" class="addTr7">
            <%--<tr>--%>
              <%--<td>2001</td>--%>
              <%--<td>学士</td>--%>
              <%--<td>教育技术学</td>--%>
              <%--<td>否</td>--%>
              <%--<td>--%>
                <%--<button class="btn-xx del7">删除</button>--%>
              <%--</td>--%>
            <%--</tr>--%>
            </tbody>
            <script type="text/template" id="addTr7_templ">
              {{ if(it.certificateEntryList.length>0){ }}
              {{ for (var i = 0, l = it.certificateEntryList.length; i < l; i++) { }}
              {{var obj=it.certificateEntryList[i];}}
              <tr>
                <td>{{=obj.time}}</td>
                <td>{{=obj.name}}</td>
                <td>{{=obj.record}}</td>
                <td>{{?obj.open==1}}是{{??}}否{{?}}</td>
                <td>
                  <button class="btn-xx del7">删除</button>
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
            <tr>
              <td class="td1">
                <input type="text" id="time8">
              </td>
              <td class="td2">
                <input type="text" id="certificate">
              </td>
              <td class="td3">
                <input type="text" id="score2">
              </td>
              <td>
                <select id="open7">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </td>
              <td>
                <button class="add7">添加</button>
              </td>
            </tr>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">继续教育中心</div>
          <table class="table-all table-all-7">
            <tbody id="addTr8">
            <tr>
              <th class="th1">时间</th>
              <th class="th2">培训机构</th>
              <th class="th3">培训课程</th>
              <th class="th4">证书</th>
              <th class="th7">成绩</th>
              <th class="th5">公开</th>
              <th class="th6">操作</th>
            </tr>
            <tr>
              <td class="td1">
                <input type="text" id="value1">
              </td>
              <td class="td2">
                <input type="text" id="value2">
              </td>
              <td class="td3">
                <select id="value3" class="value3" style="width: 100px;">

                </select>
                <script type="text/template" id="value_templ">

                  {{ if(it.rows.length>0){ }}
                  {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                  {{var obj=it.rows[i];}}
                          <option mid="{{=obj.id}}">{{=obj.course}}</option>
                  {{ } }}
                  {{ } }}
                </script>
              </td>
              <td class="td4">
                <input type="text" id="value4">
              </td>
              <td class="td7">
                <input type="text" id="value5">
              </td>
              <td rowspan="3">
                <select id="open8">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </td>
              <td rowspan="3">
                <button class="add8">添加</button>
              </td>
            </tr>
            <tr>
              <th>地点</th>
              <th>培训课时</th>
              <th colspan="3">培训内容</th>
            </tr>
            <tr>
              <td><input type="text" id="value6"></td>
              <td><input type="text" id="value7"></td>
              <td colspan="3">
                <textarea class="textarea-nr" id="value8"></textarea>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">自我介绍（50字以内）</div>
          <div>
            <textarea class="zwjs" id="introduction"></textarea>
          </div>

        </div>
        <%--<form id="resForm" action="/manage/addPhoto.do" method="post" enctype="multipart/form-data">
        <div class="table-div">
          <div class="table-div-title">生活照<i class="red-xing">*</i></div>
          <div>
            <div class="pic-list">
                <span class="span-photo">
                    <input type="file" name="pic" id="pic" class="input-file-text">
                    &lt;%&ndash;<input type="text" class="input-text-file">&ndash;%&gt;
                  <input type="hidden" name="uid" value="111"/>
                </span>
                <span class="submit-button add-pic" id="addUserImage">添加</span>
                <em>格式为JPG|JPEG</em>
            </div>
          </div>
        </div>
        </form>--%>
        <div class="save-div">
            <button class="addInfo">保存</button>
        </div>
      </div>
      <div class="cont-main2">
        <div class="main2-top">
          <span>培训项目</span>
          <select id="sel_project_id" class="project"></select>
          <script type="text/template" id="project_templ">

            {{ if(it.rows.length>0){ }}
            {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
            {{var obj=it.rows[i];}}
            <option value="{{=obj.id}}">{{=obj.course}}</option>
            {{ } }}
            {{ } }}
          </script>
          <span>成绩排行</span>
          <select id="sel_score_sort_type">
            <option value="0">从高到低</option>
            <option value="1">从低到高</option>
          </select>
        </div>
        <table class="table-group">

        </table>
        <script type="text/template" id="group_templ">
          <tr>
            <th class="th1">姓名</th>
            <th class="th2">培训经历/课程</th>
            <th class="th3">成绩</th>
            <th class="th4">证书</th>
            <th class="th5">操作</th>
          </tr>
          {{ if(it.rows.length>0){ }}
          {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
          {{var obj=it.rows[i];}}
          <tr>
            <td>{{=obj.username}}</td>
            <td>{{=obj.course}}</td>
            <td>{{=obj.score}}</td>
            <td>{{=obj.certificate}}</td>
            <td>
              <button class="btn-check" uid="{{=obj.userid}}">查看</button>
            </td>
          </tr>
          {{ } }}
          {{ } }}
        </script>
      </div>
      <div class="cont-main3">
        <div class="clearfix">
          <button class="btn-add-xm">添加项目</button>
        </div>
        <table class="table-xm">

        </table>
        <script type="text/template" id="xm_templ">
          <tr>
            <th class="th1">#</th>
            <th class="th2">培训课程</th>
            <th class="th3">说明</th>
            <th class="th4">满分</th>
            <th class="th5">操作</th>
          </tr>
          {{ if(it.rows.length>0){ }}
          {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
          {{var obj=it.rows[i];}}
          <tr>
            <td>{{=i+1}}</td>
            <td>{{=obj.course}}</td>
            <td>{{=obj.content}}</td>
            <td>{{=obj.score}}</td>
            <td>
              <button class="btn-dell" mid="{{=obj.id}}" mnm="{{=obj.course}}">删除</button>
            </td>
          </tr>
          {{ } }}
          {{ } }}
          </script>
      </div>

    </div>
    <!--弹窗-->
      <!--上传头像弹窗-->
      <div class="wind-upload-pic wind">
        <form id="userimg" action="/manage/addPhoto.do" method="post" enctype="multipart/form-data">
          <div class="wind-title wind-title2">
              修改头像
              <i class="btn-x">×</i>
          </div>
          <div class="wind-main">
              <div class="pic-list">
                <span class="span-photo">
                    <input type="file" name="pic" class="input-file-text">
                    <input type="text" class="input-text-file">
                </span>
                  <span class="btn-uppic" id="adduserimg">上传照片</span>
              </div>
          </div>
          </form>
      </div>
      <!--/上传头像弹窗-->

    <!--删除教师信息弹窗-->
    <div class="wind-x-tea wind">
      <div class="wind-title">
        提示
        <i class="btn-x">×</i>
      </div>
      <div class="wind-main">
        <input id="delTeacherId" type="hidden" value="">
        <div class="ask-div">是否要删除此教师信息？</div>
        <div class="btn-div">
          <button class="btn-y btn-x-y delteacherinfo">确定</button>
          <button class="btn-n btn-x-n">取消</button>
        </div>
      </div>
    </div>
    <!--/删除教师信息弹窗-->

    <!--删除课程项目弹窗-->
    <div class="wind-x-xm wind">
      <div class="wind-title">
        提示
        <i class="btn-x">×</i>
      </div>
      <div class="wind-main">
        <div class="ask-div" id="showproject"></div>
        <div class="btn-div">
          <button class="btn-y btn-x-y delproject">确定</button>
          <button class="btn-n btn-x-n">取消</button>
        </div>
      </div>
    </div>
    <!--/删除课程项目弹窗-->

    <!--设置权限弹窗-->
    <div class="wind-set-power wind">
      <div class="wind-title">
        设置权限
        <i class="btn-x">×</i>
      </div>
      <div class="wind-main">
        <div class="ask-div">
          <dl>
            <dd>
              <span id="tnm">老师姓名</span>
              <input id="teacherName" type="text">
            </dd>
            <dd>
              <span id="jobnum">教职工号</span>
              <input id="teacherNo" type="text">
            </dd>
            <dd>
              <span>权&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;限</span>
              <select id="role">
                <option value="8">校领导</option>
                <option value="2">老师</option>
                <option value="64">管理员</option>
                <option value="2048">门卫</option>
                <option value="4096">宿舍管理员</option>
              </select><br>
              <div id="isManageDiv" style="display:none;">
                <input type="checkbox" id="ismanage2" class="input-set">是否设置成管理员
              </div>
            </dd>
          </dl>
        </div>
        <div class="btn-div">
          <button class="btn-y btn-x-y surerole">确定</button>
          <button class="btn-n btn-x-n">取消</button>
        </div>
      </div>
    </div>
    <!--/设置权限弹窗-->


    <!--重置密码弹窗-->
    <div class="wind-mima-reset wind">
      <div class="wind-title">
        提示
        <i class="btn-x">×</i>
      </div>
      <div class="wind-main">
        <div class="ask-div">
          <input type="radio" name="tea-radio" checked value="0">仅当前教师
          <input type="radio" name="tea-radio" value="1">所有教师
        </div>
        <div>
          <span class="span-reset">重置老师初始密码</span>
        </div>
        <div class="btn-div">
          <button class="btn-reset-y btn-y">确定</button>
          <button class="btn-reset-n btn-n">取消</button>
        </div>
      </div>
    </div>

    <div class="wind-mima-reset2 wind">
      <div class="wind-title">
        提示
        <i class="btn-x">×</i>
      </div>
      <div class="wind-main">
        <div class="ask-div">
          确认要重置密码吗？
        </div>
        <div class="btn-div">
          <button class="btn-reset-y2 btn-y">确定</button>
          <button class="btn-reset-n2 btn-n">取消</button>
        </div>
      </div>
    </div>
    <div class="wind-mima-reset3 wind">
      <div class="wind-title">
        提示
        <i class="btn-x">×</i>
      </div>
      <div class="wind-main">
        <div id="resetPwdInfo" class="ask-div">
          老师：A，初始密码：1234
        </div>
        <div class="btn-div">
          <button class="btn-reset-y3 btn-y">确认</button>
        </div>
      </div>
    </div>

    <div></div>
    <!--/重置密码弹窗-->
    <!--添加项目弹窗-->
    <div class="wind-add-xm wind">
      <div class="wind-title">
        添加培训课程
        <i class="btn-x">×</i>
      </div>
      <div class="wind-main">
        <div class="ask-div">
          <dl>
            <dd>
              <span>培训课程</span>
              <input type="text" id="course">
            </dd>
            <dd>
              <span>说&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;明</span>
              <input type="text" id="con">
            </dd>
            <dd>
              <span>满&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;分</span>
              <select id="score">
                <option value="100">100</option>
                <option value="90">90</option>
                <option value="80">80</option>
              </select>
            </dd>
          </dl>
        </div>
        <div class="btn-div">
          <button class="btn-y btn-x-y psure">确定</button>
          <button class="btn-n btn-x-n">取消</button>
        </div>
      </div>
    </div>
    <!--/添加项目弹窗-->

    <!--半透明背景-->
    <div class="bg"></div>
  </div>
  <!--/.col-right-->
</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('furtheredu');
</script>

</body>
</html>
