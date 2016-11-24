<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@page import="com.pojo.app.SessionValue"%>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>加班</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/zhiban/jiaban.css"/>
  <link rel="stylesheet" type="text/css" href="/static/js/select2/select2.css">
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>

<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

  <%@ include file="../common_new/col-left.jsp" %>
  <!--/.col-left-->
  <!--广告-->
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>

  <!--.col-right-->
  <div class="col-right">

    <!--.tab-col右侧-->
    <div class="tab-col">

      <div class="tab-head clearfix">
        <ul>
          <li id="JBSQ"><a href="javascript:;" >加班申请管理</a><em></em></li>
          <li id="JBXC"><a href="javascript:;" >加班薪酬管理</a><em></em></li>
          <li id="JBSH"><a href="javascript:;" >加班审核</a><em></em></li>
          <li id="JSJB"><a href="javascript:;" >教师加班</a><em></em></li>
          <li><a href="/overTime/myjiaban.do?index=5&version=23&type=5" >我的加班</a><em></em></li>
          <li><a href="/overTime/myjiaban.do?index=5&version=23&type=6" >加班薪酬</a><em></em></li>
        </ul>
      </div>

      <div class="tab-main">
        <!--================加班申请管理start=================-->
        <div class="tab-JBSQ" id="tab-JBSQ">
          <div class="JBSQ-I">
            <div class="JBSQ-top">
              <em>日期：</em><input class="JBSQ-day" id="bTime" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">-<input class="JBSQ-day" id="eTime" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
              <em>姓名：</em><input id="userName">
              <span id="submit-overtime">确认</span>
            </div>
            <div class="JBSQ-middle">
              <span class="JBSQ-jiaban h-cursor">申请加班</span>
              <span class="JBSQ-muban h-cursor">模板管理</span>
              <span class="JBSQ-daochu daochu-jiaban h-cursor">导出</span>
            </div>
            <div class="JBSQ-main">
              <table>
                <thead>
                <tr>
                  <th width="150">教师姓名</th>
                  <th width="92">日期</th>
                  <th width="115">起止时间</th>
                  <th width="245">加班事由</th>
                  <th width="150">操作</th>
                </tr>
                </thead>
                <tbody class="jiabanList">

                </tbody>
                <script type="text/template" id="jiabanList_templ">
                  {{ if(it.rows.length>0){ }}
                  {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                  {{var obj=it.rows[i];}}
                  <tr>
                    <td class="JBSQ-td" width="150">{{=obj.jbUserName}}</td>
                    <td width="92">{{=obj.date}}</td>
                    <td width="115">{{=obj.timeDuan}}</td>
                    <td width="245">{{=obj.cause}}</td>
                    <td width="150">
                      {{?obj.type==0}}
                      <em class="h-cursor submit-vt" vtid="{{=obj.id}}">提交</em>|
                      <em class="h-cursor edit-vt" vtid="{{=obj.id}}">编辑</em>|
                      <em class="h-cursor del-vt" vtid="{{=obj.id}}">删除</em>
                      {{?}}
                      {{?obj.type==1}}
                      <em class="h-cursor view-vt" vtid="{{=obj.id}}">查看</em>|
                      <em class="h-cursor cexiao-vt" vtid="{{=obj.id}}">撤销</em>
                      {{?}}
                      {{?obj.type!=1&&obj.type!=0}}
                      <em class="h-cursor view-vt" vtid="{{=obj.id}}">查看</em>
                      {{?}}
                    </td>
                    </tr>
                  {{ } }}
                  {{ } }}
                </script>
              </table>
            </div>
          </div>
          <!--=============模板管理start=============-->
          <div class="JBSQ-II">
            <a href="#" class="JBSQ-II-back">&lt;返回</a>
            <div class="JBSQ-main">
              <table>
                <thead>
                <tr>
                  <th width="55">序号</th>
                  <th width="355">模板名称</th>
                  <th width="190">模板类型</th>
                  <th width="150">操作</th>
                </tr>
                </thead>
                <tbody id="model-table">

                </tbody>
                <script type="text/template" id="model-table_templ">
                  {{ if(it.rows.length>0){ }}
                  {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                  {{var obj=it.rows[i];}}
                  <tr>
                    <td class="JBSQ-td" width="55">{{=i+1}}</td>
                    <td width="355">{{=obj.name}}</td>
                    <td width="190">加班模板</td>
                    <td width="150">
                      <em class="h-cursor edit-model" mid="{{=obj.id}}" mnm="{{=obj.name}}">编辑</em>|
                      <em class="h-cursor del-model" mid="{{=obj.id}}">删除</em>
                    </td>
                  </tr>
                  {{ } }}
                  {{ } }}
                </script>
              </table>
            </div>
          </div>
          <!--=============模板管理end=============-->
        </div>
        <!--================加班申请管理end=================-->
        <!--===============加班薪酬管理start=================-->
        <div class="tab-JBXC" id="tab-JBXC">
          <div class="JBSQ-top">
            <em>日期：</em><input class="JBSQ-day" id="bTime2" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">-<input class="JBSQ-day" id="eTime2" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
            <em>姓名：</em><input id="userName2">
            <span id="submit-overtime2">确认</span>
          </div>
          <div class="JBSQ-middle">
            <span class="JBSQ-daochu daochu-salary h-cursor">导出</span>
          </div>
          <div class="JBSQ-main">
            <table>
              <thead>
              <tr>
                <th width="150">加班日期</th>
                <th width="92">姓名</th>
                <th width="115">加班时间</th>
                <th width="245">加班内容</th>
                <th width="245">加班时长(h)</th>
                <th width="150">加班薪酬(元)</th>
              </tr>
              </thead>
              <tbody class="jiabanSalary">
              </tbody>
              <script type="text/template" id="jiabanSalary_templ">
                {{ if(it.rows.length>0){ }}
                {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                {{var obj=it.rows[i];}}
                <tr>
                  <td class="JBSQ-td" width="150">{{=obj.date}}</td>
                  <td width="92">{{=obj.jbUserName}}</td>
                  <td width="115">{{=obj.timeDuan}}</td>
                  <td width="245">{{=obj.cause}}</td>
                  <td width="245">{{=obj.times}}</td>
                  <td width="150">
                    <input class="salary" otid="{{=obj.id}}" value="{{=obj.salary}}">
                  </td>
                </tr>
                {{ } }}
                {{ } }}
              </script>
            </table>
          </div>
        </div>
        <!--================加班薪酬管理end=================-->
        <!--===============加班审核start=================-->
        <div class="tab-JBSH" id="tab-JBSH">
          <div class="JBSQ-top">
            <em>日期：</em><input class="JBSQ-day" id="bTime3" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">-<input class="JBSQ-day" id="eTime3" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
            <em>姓名：</em><input id="userName3">
            <select id="shenhe">
              <option value="0">全部</option>
              <option value="1">未审核</option>
              <option value="2">审核通过</option>
              <option value="3">审核驳回</option>
            </select>
            <span id="submit-overtime3">确认</span>
          </div>
          <div class="JBSQ-middle">
            <span class="JBSQ-daochu daochu-shehe h-cursor">导出</span>
          </div>
          <div class="JBSQ-main">
            <table>
              <thead>
              <tr>
                <th width="150">教师姓名</th>
                <th width="92">日期</th>
                <th width="115">起止时间</th>
                <th width="245">加班事由</th>
                <th width="150">审核状态</th>
                <th width="150">操作</th>
              </tr>
              </thead>
              <tbody class="sheheList">

              </tbody>
              <script type="text/template" id="sheheList_templ">
                {{ if(it.rows.length>0){ }}
                {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                {{var obj=it.rows[i];}}
                <tr>
                  <td class="JBSQ-td" width="150">{{=obj.jbUserName}}</td>
                  <td width="92">{{=obj.date}}</td>
                  <td width="115">{{=obj.timeDuan}}</td>
                  <td width="245">{{=obj.cause}}</td>
                  <td width="150">
                    {{?obj.type==1}}
                    未审核
                    {{?}}
                    {{?obj.type==2}}
                    审核通过
                    {{?}}
                    {{?obj.type==3}}
                    审核驳回
                    {{?}}
                  </td>
                  <td width="150">
                    {{?obj.type==2||obj.type==3}}
                    <em class="h-cursor view-detail" otid="{{=obj.id}}">详情</em>
                    {{??}}
                    <em class="h-cursor tongguo" otid="{{=obj.id}}">通过</em>|<em class="h-cursor bohui" otid="{{=obj.id}}">驳回</em>
                    {{?}}

                  </td>
                </tr>
                {{ } }}
                {{ } }}
              </script>
            </table>
          </div>
        </div>
        <!--================加班审核end=================-->
        <!--===============教师加班start=================-->
        <div class="tab-JSJB" id="tab-JSJB">
          <div class="JBSQ-main">
            <table>
              <thead>
              <tr>
                <th width="150">教师姓名</th>
                <th width="90">日期</th>
                <th width="115">起止时间</th>
                <th width="160">加班事由</th>
                <th width="235">操作</th>
              </tr>
              </thead>
              <tbody class="tea_jiabanList">

              </tbody>
              <script type="text/template" id="tea_jiabanList_templ">
                {{ if(it.rows.length>0){ }}
                {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                {{var obj=it.rows[i];}}
                <tr>
                  <td class="JBSQ-td" width="150">{{=obj.jbUserName}}</td>
                  <td width="90">{{=obj.date}}</td>
                  <td width="115">{{=obj.timeDuan}}</td>
                  <td width="160">{{=obj.cause}}</td>
                  <td width="235">
                    {{?obj.inTime==0}}
                    <span class="JSJB-QD h-cursor" otid="{{=obj.id}}">签到</span>
                    {{??}}
                    <span class="JSJB-TX h-cursor" dt="{{=obj.date}}" td="{{=obj.timeDuan}}" cu="{{=obj.cause}}" otid="{{=obj.id}}">填写加班记录</span>
                      {{?obj.outTime==0}}
                      <span class="JSJB-QT h-cursor" otid="{{=obj.id}}">签退</span>
                      {{?}}
                    {{?}}
                  </td>
                </tr>
                {{ } }}
                {{ } }}
              </script>
            </table>
          </div>
        </div>
        <!--================教师加班end=================-->
      </div>
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
</div>
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!--=================申请加班弹窗框start===================-->
<div class="bg"></div>
<div class="popup-SQ">
  <div class="popup-SQ-top">
    <em>申请加班</em><i class="h-cursor SQ-X">X</i>
  </div>
  <div class="popup-SQ-main">
    <div>
      <em>选择创建方式：</em>
    </div>
    <div>
      <span class="SQ-sy h-cursor">使用模板创建</span><span class="SQ-sd h-cursor">手动输入</span>
    </div>
  </div>
</div>
<!--=================申请加班弹窗框end===================-->
<!--=================申请加班(使用模板)弹窗框start===================-->
<div class="popup-MB">
  <div class="popup-MB-top">
    <em>申请加班（使用模板）</em><i class="h-cursor SQ-X">X</i>
  </div>
  <div class="popup-MB-main">
    <dl>
      <dd>
        <em>选择模板：</em>
        <select id="modellist">
          <c:forEach items="${models}" var="model">
            <option value="${model.id}">${model.name}</option>
          </c:forEach>
        </select>
      </dd>
      <dd>
        <em>加班人：</em>
        <select id="jiabanUser5">
          <c:forEach items="${users}" var="user">
            <option value="${user.id}">${user.userName}</option>
          </c:forEach>
        </select>
      </dd>
      <dd>
        <em>加班日期：</em>
        <input id="jiabanDate5" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
      </dd>
      <dd>
        <em>加班时段：</em>
        <input id="startTime5" onClick="WdatePicker({dateFmt:'HH:mm'})">-<input class="JBSQ-day" id="endTime5" onClick="WdatePicker({dateFmt:'HH:mm'})">
      </dd>
      <dd>
        <em>事由：</em>
        <textarea id="cause5"></textarea>
      </dd>
      <dd>
        <em>审核人：</em>
        <select id="shenheUser5">
          <c:forEach items="${headUsers}" var="user">
            <option value="${user.id}">${user.userName}</option>
          </c:forEach>
        </select>
      </dd>
    </dl>
  </div>
  <div class="popup-MB-bottom">
    <span class="MB-TJ h-cursor">提交</span><span class="MB-QX h-cursor">取消</span>
  </div>
</div>
<!--=================申请加班(使用模板)弹窗框end===================-->
<!--=================申请加班(手动输入)弹窗框start===================-->
<div class="popup-SD ADDJB-POP">
  <div class="popup-SD-top">
    <em>申请加班（手动输入）</em><i class="h-cursor SQ-X">X</i>
  </div>
  <input id="overTimeId" hidden="hidden">
  <div class="popup-SD-main">
    <dl>
      <dd>
        <em>加班人：</em>
        <select id="jiabanUser">
          <c:forEach items="${users}" var="user">
            <option value="${user.id}">${user.userName}</option>
          </c:forEach>
        </select>
      </dd>
      <dd>
        <em>加班日期：</em>
        <input id="jiabanDate" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})">
      </dd>
      <dd>
        <em>加班时段：</em>
        <input id="startTime" onClick="WdatePicker({dateFmt:'HH:mm'})">-<input class="JBSQ-day" id="endTime" onClick="WdatePicker({dateFmt:'HH:mm'})">
      </dd>
      <dd>
        <em>事由：</em>
        <textarea id="cause"></textarea>
      </dd>
      <dd>
        <em>审核人：</em>
        <select id="shenheUser">
          <c:forEach items="${headUsers}" var="user">
            <option value="${user.id}">${user.userName}</option>
          </c:forEach>
        </select>
      </dd>
    </dl>
  </div>
  <div class="popup-SD-bottom">
    <span class="SD-TJ h-cursor submit-jiaban">保存</span>
    <span class="SD-QX h-cursor save-model">保存为模板</span>
    <span class="SD-QX h-cursor qx-jiaban">取消</span>
  </div>
</div>
<!--=================申请加班(手动输入)弹窗框end===================-->
<!--=================申请加班(手动输入)弹窗框start===================-->
<div class="popup-SD XXJB-POP">
  <div class="popup-SD-top">
    <em>申请加班详细</em><i class="h-cursor SQ-X">X</i>
  </div>
  <div class="popup-SD-main">
    <dl>
      <dd>
        <em>加班人：</em>
        <select id="jiabanUser2" disabled>
          <c:forEach items="${users}" var="user">
            <option value="${user.id}">${user.userName}</option>
          </c:forEach>
        </select>
      </dd>
      <dd>
        <em>加班日期：</em>
        <input id="jiabanDate2" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})" disabled>
      </dd>
      <dd>
        <em>加班时段：</em>
        <input id="startTime2" onClick="WdatePicker({dateFmt:'HH:mm'})" disabled>-<input class="JBSQ-day" id="endTime2" onClick="WdatePicker({dateFmt:'HH:mm'})" disabled>
      </dd>
      <dd>
        <em>事由：</em>
        <textarea id="cause2" disabled></textarea>
      </dd>
      <dd>
        <em>审核人：</em>
        <select id="shenheUser2" disabled>
          <c:forEach items="${headUsers}" var="user">
            <option value="${user.id}">${user.userName}</option>
          </c:forEach>
        </select>
      </dd>
    </dl>
  </div>
  <div class="popup-SD-bottom">
    <span class="SD-QX h-cursor qx-jiaban">确定</span>
  </div>
</div>
<!--=================申请加班(手动输入)弹窗框end===================-->
<!--=================加班记录弹窗框start===================-->
<div class="popup-JB">
  <div class="popup-JB-top">
    <em>加班记录</em><i class="h-cursor SQ-X">X</i>
  </div>
  <input hidden="hidden" id="overTime3">
  <div class="popup-JB-main">
    <dl>
      <dd>
        <em>加班日期：</em><label id="date3">2016/6/6</label>
      </dd>
      <dd>
        <em>加班时段：</em><label id="timeDuan3">8:00-12:00</label>
      </dd>
      <dd>
        <em>加班事由：</em><label id="cause3">整理年级教案</label>
      </dd>
      <dd>
        <em>加班记录：</em>
        <textarea id="dutylog"></textarea>
      </dd>
      <dd>
        <label for="file_attach" style="cursor: pointer">
          <em  class="popup-TJ h-cursor"></em>
        </label> <img src="/img/loading4.gif" id="fileuploadLoading"
                      style="display: none;" />
        <div style="width: 0; height: 0; overflow: visible">
          <input id="file_attach" type="file" name="file" value="添加附件"
                 size="1" style="width: 0; height: 0; opacity: 0">
        </div>
        <div class="popup-bottom-LAB">
        </div>
      </dd>
    </dl>
  </div>
  <div class="popup-JB-bottom">
    <span class="JB-TJ h-cursor submit-log">提交</span>
    <span class="JB-QX h-cursor">取消</span>
  </div>
</div>
<!--================保存为模板start=================-->
<div class="zb-model-alert">
  <div class="zb-set-title clearfix">
    <p>保存为模板</p>
    <span class="zb-set-close">X</span>
  </div>
  <input hidden="hidden" id="modelId">
  <div class="zb-model-main">
    <span>模板名称：</span><input type="text" id="modelname">
  </div>
  <div class="zb-alert-btn">
    <button class="zb-btn-sure sure-model">确定</button>
    <button class="zb-btn-qx">取消</button>
  </div>
</div>
<!--================保存为模板end=================-->
<!--=================审核人弹窗start===================-->
<div class="popup-SHR">
  <div class="popup-SHR-top">
    <em>审核人</em><i class="h-cursor SQ-X">X</i>
  </div>
  <input hidden="hidden" id="overTimeId6">
  <div class="popup-SHR-main">
    <dl>
      <dd>
        <em>审核</em>
        <input type="radio" class="shcheck" name="check" value="1" id="check1">通过
        <input type="radio" class="shcheck" name="check" value="2" id="check2">转至
        <select id="shenheUser6" disabled>
          <c:forEach items="${headUsers}" var="user">
            <option value="${user.id}">${user.userName}</option>
          </c:forEach>
        </select>
      </dd>
    </dl>
  </div>
  <div class="popup-SHR-bottom">
    <span class="SHR-TJ h-cursor">确定</span>
    <span class="SHR-QX h-cursor">取消</span>
  </div>
</div>
<!--=================审核人弹窗框end===================-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('jiaban');
</script>

</body>
</html>