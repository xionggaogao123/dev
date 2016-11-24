<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>宿舍管理</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/peoplegroup/population.css">
  <script language="javascript" type="text/javascript" src="/static/js/WdatePicker.js"></script>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>

<!--#head-->
<%@ include file="../common_new/head.jsp" %>

<!--#content-->
<div id="content" class="clearfix">
  <!--.col-left-->
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

    <%--<!--.banner-info-->--%>
    <%--<img src="http://placehold.it/770x100" class="banner-info" />--%>
    <%--<!--/.banner-info-->--%>

    <!--.tab-col右侧-->
      <div class="tab-col">
        <div class="tab-head clearfix">
          <ul>
            <li class="dorm head-dorm"><a class="dormManage">宿舍管理</a><em></em></li>
          </ul>
          <div class="check-right">
            <span class="dorm-QCTJ">迁出统计</span>
            <span class="dorm-RZTJ">入住统计</span>
            <span class="cleanAll">清空所有</span>
          </div>
        </div>
        <!--==========================宿舍管理start==========================-->
        <div  id="dorm-GL">

          <div class="tab-main">
            <div class="dorm-main">
              <div class="dorm-top">
                <div class="dorm-top-right">
                  <span class="dorm-newly">新增</span>
                  <span class="dorm-RZGL">入住管理</span>
                </div>
              </div>
              <div class="dorm-info">
                <table>
                  <thead>
                    <th width="120">序号</th>
                    <th width="215">分区名称</th>
                    <th width="210">备注</th>
                    <th width="200">操作</th>
                  </thead>
                  <tbody class="dormList">
                  </tbody>
                  <script type="text/template" id="dormList_templ">
                    {{ if(it.rows.length>0){ }}
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                    {{var obj=it.rows[i];}}
                      <tr>
                        <td width="120">{{=i+1}}</td>
                        <td width="215">{{=obj.name}}</td>
                        <td width="210">{{=obj.remark}}</td>
                        <td width="200"  did="{{=obj.id}}">
                          <em class="dorm-detail" nm="{{=obj.name}}">详情</em>|
                          <em class="dorm-edit edit-dorm" rmk="{{=obj.remark}}" nm="{{=obj.name}}">编辑</em>|
                          <em class="dorm-del del-dorm">删除</em>
                        </td>
                      </tr>
                    {{ } }}
                    {{ } }}
                  </script>
                </table>
              </div>
            </div>
          </div>
        </div>
        <!--==========================宿舍管理end==========================-->
        <!--=======================一号宿舍楼start=========================-->
        <div id="dorm-SS">

          <div class="tab-main">

            <div class="dorm-main" >
              <div class="dorm-top">
                <div class="dorm-top-right">
                  <span class="loop-add">新增</span>
                </div>
              </div>
              <div class="dorm-info">
                <table>
                  <thead>
                    <th width="120">序号</th>
                    <th width="215">分区名称</th>
                    <th width="210">备注</th>
                    <th width="200">操作</th>
                  </thead>
                  <tbody class="loopList">
                  </tbody>
                  <script type="text/template" id="loopList_templ">
                    {{ if(it.rows.length>0){ }}
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                    {{var obj=it.rows[i];}}
                    <tr>
                      <td width="120">{{=i+1}}</td>
                      <td width="215">{{=obj.name}}</td>
                      <td width="210">{{=obj.remark}}</td>
                      <td width="200"  lid="{{=obj.id}}">
                        <em class="dorm-XQ" nm="{{=obj.name}}">详情</em>|
                        <em class="dorm-edit edit-loop" rmk="{{=obj.remark}}" nm="{{=obj.name}}">编辑</em>|
                        <em class="dorm-del del-loop">删除</em>
                      </td>
                    </tr>
                    {{ } }}
                    {{ } }}
                  </script>
                </table>
              </div>
            </div>
          </div>
        </div>
        <!--=======================一号宿舍楼end=========================-->
        <!--=======================一号宿舍楼1Fstart=========================-->
        <div id="dorm-SSL">

          <div class="tab-main">

            <div class="dorm-main" >
              <div class="dorm-top">
                <div class="dorm-top-right">
                  <span class="room-add">新增</span>
                </div>
              </div>
              <div class="dorm-info">
                <table>
                  <thead>
                    <th width="120">序号</th>
                    <th width="215">分区名称</th>
                    <th width="210">备注</th>
                    <th width="200">操作</th>
                  </thead>
                  <tbody class="roomList">
                  </tbody>
                  <script type="text/template" id="roomList_templ">
                    {{ if(it.rows.length>0){ }}
                    {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                    {{var obj=it.rows[i];}}
                    <tr>
                      <td width="120">{{=i+1}}</td>
                      <td width="215">{{=obj.name}}</td>
                      <td width="210">{{=obj.remark}}</td>
                      <td width="200"  rid="{{=obj.id}}">
                        <%--<em class="detail-room">详情</em>|--%>
                        <em class="dorm-edit edit-room" rmk="{{=obj.remark}}" nm="{{=obj.name}}" bn="{{=obj.bedNum}}" rty="{{=obj.roomType}}">编辑</em>|
                        <em class="dorm-del del-room">删除</em>
                      </td>
                    </tr>
                    {{ } }}
                    {{ } }}
                  </script>
                </table>
              </div>
            </div>
          </div>
        </div>
        <!--=======================一号宿舍楼1Fend=========================-->
        <!--=======================宿舍入住管理start=========================-->
        <div id="dorm-check">
          <div class="tab-main">
            <div class="dorm-main">
              <div class="check-top">
                <div>
                  <em>分区</em>
                  <select id="dormlist">

                  </select>
                </div>
                <div>
                  <em>楼层</em>
                  <select id="looplist">
                    <option value="">全部</option>

                  </select>
                </div>
                <div>
                  <em>性别</em>
                  <select id="sexlist">
                    <option value="2">全部</option>
                    <option value="1">男</option>
                    <option value="0">女</option>
                  </select>
                </div>
                <span id="seachRooms">查询</span>
              </div>
              <div class="check-num">
                <div id="dormNm"></div>
                <span>楼层总数：<em id="loopCnt"></em></span>
                <span>房间总数：<em id="roomCnt"></em></span>
                <span>入住总数：<em id="cin"></em></span>
                <span>空床总数：<em id="cbed"></em></span>
                <span>空宿舍：<em id="croom"></em></span>
              </div>
              <div class="check-main">
                <ul class="roomUsers">

                </ul>
                <script type="text/template" id="roomUsers_templ">
                  {{ if(it.roomList.length>0){ }}
                  {{ for (var i = 0, l = it.roomList.length; i < l; i++) { }}
                  {{var obj=it.roomList[i];}}
                  <li>
                    <div class="check-li-top">{{=obj.roomName}}<em>{{=obj.occupancyNum}}/{{=obj.bedNum}}</em></div>
                    <div class="check-li-bo">
                      <ul>
                        {{~obj.userList:value:index}}
                        {{?value.userName==''}}
                        <li class="check-RZ">
                          <div class="check-bo-num">{{=value.bedNum}}</div>
                          <div class="check-RZZ" roomId="{{=obj.id}}" bnum="{{=value.bedNum}}">
                            <em>入住</em>
                          </div>
                        </li>
                        {{??}}
                        <li class="check-bo-li">
                          <div class="check-bo-num">{{=value.bedNum}}</div>
                          <em>{{=value.userName}}</em>
                          <div class="check-TZ" unm="{{=value.userName}}" sex="{{=obj.roomType}}" bnum="{{=value.bedNum}}" uid="{{=value.userId}}" roomId="{{=obj.id}}" rnm="{{=obj.roomName}}" lnm="{{=obj.loopName}}">
                            <em class="check-TZ-I">调整</em>
                            <em class="check-TZ-II">迁出</em>
                          </div>
                        </li>
                        {{?}}
                        {{~}}
                      </ul>
                    </div>
                  </li>
                  {{ } }}
                  {{ } }}
                </script>
              </div>
            </div>
          </div>
        </div>
        <!--=======================宿舍入住管理end=========================-->
        <!--=======================宿舍入住名单start=========================-->
        <div id="dorm-RZMD">

              <div class="tab-main">
                <div class="dorm-main">
                  <div class="check-top">
                    <div>
                      <em>分区</em>
                      <select id="dormList8">
                        <option>全部</option>
                      </select>
                    </div>
                    <div>
                      <em>楼层</em>
                      <select id="loopList8">
                        <option>全部</option>

                      </select>
                    </div>
                    <div>
                      <em>房间号</em>
                      <select id="roomList8">
                      </select>
                    </div>
                    <div>
                      <em>姓名</em>
                      <input id="userName8">
                    </div>
                    <span id="seach8">查询</span>
                  </div>
                  <div class="check-main">
                    <div class="dorm-info">
                      <table>
                        <thead>
                          <th width="45">序号</th>
                          <th width="100">姓名</th>
                          <th width="100">分区</th>
                          <th width="100">楼层</th>
                          <th width="100">房间号</th>
                          <th width="150">入住日期</th>
                          <%--<th width="155">备注</th>--%>
                        </thead>
                        <tbody class="roomUser">

                        </tbody>
                        <script type="text/template" id="roomUser_templ">
                          {{ if(it.rows.length>0){ }}
                          {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                          {{var obj=it.rows[i];}}
                          <tr>
                            <td width="45">{{=i+1}}</td>
                            <td width="100">{{=obj.userName}}</td>
                            <td width="100">{{=obj.dormName}}</td>
                            <td width="100">{{=obj.loopName}}</td>
                            <td width="100">{{=obj.roomName}}</td>
                            <td width="150">{{=obj.time}}</td>
                            <%--<td width="155">{{=obj.cause}}</td>--%>
                          </tr>
                          {{ } }}
                          {{ } }}
                        </script>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            <!--===================分页=====================-->
            <div class="page-paginator">
                <span class="first-page">首页</span>
                      <span class="page-index">
                          <span class="active">1</span>
                          <span>2</span>
                          <span>3</span>
                          <span>4</span>
                          <span>5</span>
                          <i>···</i>
                     </span>
                <span class="last-page">尾页</span>
            </div>
            <!--===================分页=====================-->
        </div>
        <!--=======================宿舍入住名单end=========================-->
        <!--=======================宿舍迁出名单start=========================-->
        <div id="dorm-QCMD">

              <div class="tab-main">
                <div class="dorm-main">
                  <div class="check-top">
                    <div>
                      <em>分区</em>
                      <select id="dormList7">
                        <option>全部</option>
                      </select>
                    </div>
                    <div>
                      <em>楼层</em>
                      <select id="loopList7">
                        <option>全部</option>
                      </select>
                    </div>
                    <div>
                      <em>房间号</em>
                      <select id="roomList7">
                      </select>
                    </div>
                    <div>
                      <em>姓名</em>
                      <input id="userName7">
                    </div>
                    <span id="seach7">查询</span>
                  </div>
                  <div class="check-main">
                    <div class="dorm-info">
                      <table>
                        <thead>
                          <th width="45">序号</th>
                          <th width="100">姓名</th>
                          <th width="100">分区</th>
                          <th width="100">楼层</th>
                          <th width="100">房间号</th>
                          <th width="150">迁出日期</th>
                          <th width="155">迁出原因</th>
                        </thead>
                        <tbody class="moveUser">

                        </tbody>
                        <script type="text/template" id="moveUser_templ">
                          {{ if(it.rows.length>0){ }}
                          {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                          {{var obj=it.rows[i];}}
                          <tr>
                            <td width="45">{{=i+1}}</td>
                            <td width="100">{{=obj.userName}}</td>
                            <td width="100">{{=obj.dormName}}</td>
                            <td width="100">{{=obj.loopName}}</td>
                            <td width="100">{{=obj.roomName}}</td>
                            <td width="150">{{=obj.time}}</td>
                            <td width="155">{{=obj.cause}}</td>
                          </tr>
                          {{ } }}
                          {{ } }}
                        </script>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            <!--===================分页=====================-->
              <div class="page-paginator">
                  <span class="first-page">首页</span>
                      <span class="page-index">
                          <span class="active">1</span>
                          <span>2</span>
                          <span>3</span>
                          <span>4</span>
                          <span>5</span>
                          <i>···</i>
                     </span>
                  <span class="last-page">尾页</span>
              </div>
            <!--===================分页=====================-->
        </div>
        <!--=======================宿舍迁出名单end=========================-->
      </div>
  </div>
  <!--/.tab-col右侧-->

</div>
<!--/.col-right-->

</div>
<!--/#content-->
</div>
</div>
<!--==========================分区新建编辑弹出框start==============================-->
<div class="dorm-new" id="dorm-new">
  <div class="popup-top">
    <em>新建/编辑分区</em><i class="dorm-QX">X</i>
  </div>
  <div class="dorm-new-main">
    <input id="dormId" hidden="hidden">
    <dl>
      <dd>
        <em>分区名称</em><input id="areaName">
      </dd>
      <dd>
        <em>备注</em><input id="remark">
      </dd>
    </dl>
  </div>
  <div class="dorm-new-bottom">
    <span class="dorm-QD sure-area">确定</span>
    <span class="dorm-QX">取消</span>
  </div>
</div>
<!--==========================新建编辑弹出框end==============================-->
<!--==========================宿舍楼层新建编辑弹出框start==============================-->
<div class="dorm-new" id="dorm-newW">
  <div class="popup-top">
    <em>新建/编辑楼层</em><i class="dorm-QX">X</i>
  </div>
  <div class="dorm-new-main">
    <input hidden="hidden" id="loopId">
    <dl>
      <dd>
        <em>楼层名称</em><input id="loopName">
      </dd>
      <dd>
        <em>备注</em><input id="remark2">
      </dd>
    </dl>
  </div>
  <div class="dorm-new-bottom">
    <span class="dorm-QD sure-loop">确定</span>
    <span class="dorm-QX">取消</span>
  </div>
</div>
<!--==========================新建编辑弹出框end==============================-->
<!--==========================宿舍楼层房间新建编辑弹出框start==============================-->
<div class="dorm-new" id="dorm-newWw">
  <div class="popup-top">
    <input hidden="hidden" id="roomId">
    <em>新建/编辑宿舍</em><i class="dorm-QX">X</i>
  </div>
  <div class="dorm-new-main">
    <dl>
      <dd>
        <em>房间号</em><input id="roomName">
      </dd>
      <dd>
        <em>床位数</em>
        <select id="bedNum">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
          <option value="6">6</option>
          <option value="7">7</option>
          <option value="8">8</option>
        </select>
      </dd>
      <dd id="sex">
        <em>男/女</em>
        <input class="dorm-ra" type="radio" name="na" value="1" checked><em>男宿舍</em>
        <input class="dorm-ra" type="radio" name="na" value="0"><em>女宿舍</em>
      </dd>
      <dd>
        <em>备注</em><input id="remark3">
      </dd>
    </dl>
  </div>
  <div class="dorm-new-bottom">
    <span class="dorm-QD sure-room">确定</span>
    <span class="dorm-QX">取消</span>
  </div>
</div>
<!--==========================新建编辑弹出框end==============================-->
<!--==========================分区迁出start==============================-->
<div class="dorm-QC" id="dorm-QC">
  <div class="popup-top">
    <input hidden="hidden" id="dormId2">
    <em></em><i class="dorm-QX">X</i>
  </div>
  <div class="dorm-new-main">
    <dl>
      <dd>
        <label class="dorm-dell">确定删除？</label>
      </dd>
      <dd>
        <label>删除分区，分区内的全部信息都将被删除！</label>
      </dd>
    </dl>
  </div>
  <div class="dorm-new-bottom">
    <span class="dorm-QD sure-del-dorm2">确定</span>
    <span class="dorm-QX">取消</span>
  </div>
</div>
<!--==========================分区迁出end==============================-->
<!--==========================楼层迁出start==============================-->
<div class="dorm-QC" id="dorm-QCC">
  <div class="popup-top">
    <input hidden="hidden" id="loopId2">
    <em></em><i class="dorm-QX">X</i>
  </div>
  <div class="dorm-new-main">
    <dl>
      <dd>
        <label class="dorm-dell">确定删除？</label>
      </dd>
      <dd>
        <label>删除分区，楼层内的全部信息都将被删除！</label>
      </dd>
    </dl>
  </div>
  <div class="dorm-new-bottom">
    <span class="dorm-QD sure-del-loop">确定</span>
    <span class="dorm-QX">取消</span>
  </div>
</div>
<!--==========================楼层迁出end==============================-->
<!--==========================宿舍迁出start==============================-->
<div class="dorm-QC" id="dorm-QCCC">
  <div class="popup-top">
    <input hidden="hidden" id="roomId2">
    <em></em><i class="dorm-QX">X</i>
  </div>
  <div class="dorm-new-main">
    <dl>
      <dd>
        <label class="dorm-dell">确定删除宿舍？</label>
      </dd>
      <dd>
        <label>删除宿舍，宿舍内的全部信息都将被删除！</label>
      </dd>
    </dl>
  </div>
  <div class="dorm-new-bottom">
    <span class="dorm-QD sure-del-room">确定</span>
    <span class="dorm-QX">取消</span>
  </div>
</div>
<!--==========================宿舍迁出end==============================-->
<!--===========================寝室迁出start==============================-->
<div class="dorm-QC" id="dorm-QSQC">
  <input id="roomId4" hidden="hidden">
  <input id="bedNum4" hidden="hidden">
  <div class="popup-top">
    <em>寝室迁出</em><i class="dorm-QX">X</i>
  </div>
  <div class="dorm-new-main">
    <dl>
      <dd>
        <label class="dorm-dell">确定将“<span id="userName"></span>”迁出寝室？</label>
      </dd>
      <dd>
        <em>迁出原因</em>
        <textarea id="cause4"></textarea>
      </dd>
    </dl>
  </div>
  <div class="dorm-new-bottom">
    <span class="dorm-QD sure-qianchu">确定</span>
    <span class="dorm-QX">取消</span>
  </div>
</div>
<!--===========================寝室迁出end==============================-->
<!--===========================寝室调整start==============================-->
<div class="dorm-QC" id="dorm-QSTZ">
  <div class="popup-top">
    <input id="sex5" hidden="hidden">
    <input id="orgRoomId" hidden="hidden">
    <input id="orgBedNum" hidden="hidden">
    <em>寝室调整</em><i class="dorm-QX">X</i>
  </div>
  <div class="dorm-new-main">
    <dl>
      <dd>
        <label class="dorm-dell" id="userName5"></label>
      </dd>
      <dd>
        <span class="QSTZ-QS">原寝室</span>
        <span id="dormName5"></span>
        <span id="loopName5"></span>
        <span id="roomName5"></span>
        <span>床位号：<span id="bedNum5"></span></span>
      </dd>
      <dd>
        <span class="QSTZ-XQS">现寝室</span>
        <em>分区</em>
        <select id="dormList6">
        </select>
        <em>楼层</em>
        <select id="loopList6">
        </select>
      </dd>
      <dd class="QSTZ-faly">
        <em>房间号</em>
        <select id="roomList6">
        </select>
        <em>床位</em>
        <select id="bedList6">
        </select>
      </dd>
    </dl>
  </div>
  <div class="dorm-new-bottom">
    <span class="dorm-QD sure-EditUser">确定</span>
    <span class="dorm-QX">取消</span>
  </div>
</div>
<!--===========================寝室调整end==============================-->
<!--===========================寝室入住start==============================-->
<div class="dorm-QC" id="dorm-QSRZ">
  <input hidden="hidden" id="roomId3">
  <div class="popup-top">
    <em>寝室入住</em><i class="dorm-QX">X</i>
  </div>
  <div class="dorm-new-main">
    <dl>
      <dt>
        <em>选择学生</em>
      </dt>
      <dd>
        <em>年级</em>
        <select id="gradeList">
        </select>
        <em>班级</em>
        <select id="classList">
        </select>
        <em>姓名</em>
        <select id="userList">

        </select>
      </dd>
      <dt>
        <em>入住宿舍</em>
      </dt>
      <dd class="dorm-DOQS">
        <em>分区</em>
        <span id="dormName2"></span>
        <em>楼层</em>
        <span id="loopName2"></span>
        <em>房间号</em>
        <span id="roomName2"></span>
        <em>床位</em>
        <select id="bedlist">

        </select>
      </dd>
    </dl>
  </div>
  <div class="dorm-new-bottom">
    <span class="dorm-QD sure-roomUser">确定</span>
    <span class="dorm-QX">取消</span>
  </div>
</div>
<!--===========================寝室入住end==============================-->
<!--==================清空所有弹出框start=================-->
<div class="dorm-clean">
    <div class="popup-top">
        <em>清空所有</em><i class="dorm-QX">X</i>
    </div>
    <div class="dorm-new-main">
        <dl>
            <dt>
                <em>清空原因</em>
                <input id="reson">
            </dt>
        </dl>
    </div>
    <div class="dorm-new-bottom">
        <span class="dorm-QD sure-clean">确定</span>
        <span class="dorm-QX">取消</span>
    </div>
</div>
<!--==================清空所有弹出框end=================-->
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('dorm');
</script>


</body>
</html>