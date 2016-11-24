<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2016/10/8
  Time: 11:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>宿舍考勤</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/peoplegroup/dormitory.css">
  <script language="javascript" type="text/javascript" src="/static/js/WdatePicker.js"></script>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script type="text/javascript">
    $(function(){
      $('.dormitory-KQ').click(function(){
        $('#dormitory-main').hide();
        $('#dormiroty-record').show();
      })
      $('.dormitory-KS').click(function(){
        $('#dormitory-main').hide();
        $('#dormiroty-set').show();
      })
      $('.dormitory-Le').click(function(){
        $('#dormitory-main').hide();
        $('#dormiroty-leave').show();
      })
      $('.dormitory-Lv').click(function(){
        $('.leave-popup').show();
      })
      $('.dorm-QX').click(function(){
        $('.leave-popup').hide();
      })
      $('.leave-dor').click(function(){
        $('#dormiroty-leave-I').show();
        $('#dormiroty-leave').hide();
      })
    })
  </script>
</head>
<body>
<!--#head-->
<div id="head">

  <!--#head-->
  <%@ include file="../common_new/head.jsp" %>
<!--/#head-->

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

    <!--.tab-col右侧-->
    <div class="tab-col">
      <!--=======================宿舍考勤start=========================-->
      <div id="dormitory-main">
        <div class="tab-head clearfix">
          <ul>
            <li class="dorm"><a href="javascript:;" >宿舍考勤</a><em></em></li>
          </ul>
          <div class="check-right dormitory-right">
            <span class="dormitory-Le">请假登记</span>
            <span class="dormitory-KQ">考勤记录管理</span>
            <span>入住管理</span>
            <span class="dormitory-KS">考勤时间设置</span>
          </div>
        </div>
        <div class="tab-main">
          <div class="dormitory-main">
            <div class="check-top">
              <div>
                <em>分区</em>
                <select>
                  <option>全部</option>
                  <option>1号宿舍</option>
                  <option>2号宿舍</option>
                </select>
              </div>
              <div>
                <em>楼层</em>
                <select>
                  <option>全部</option>
                  <option>1F</option>
                  <option>2F</option>
                </select>
              </div>
              <div>
                <em>性别</em>
                <select>
                  <option>全部</option>
                  <option>男</option>
                  <option>女</option>
                </select>
              </div>
              <div>
                <em>考勤状态</em>
                <select>
                  <option>全部</option>
                  <option>男</option>
                  <option>女</option>
                </select>
              </div>
              <span>查询</span>
            </div>
            <div class="dormitory-info">
              <div>
                <img src="/static_new/images/dormitory-Qu.jpg">
                <em>5</em>
              </div>
              <div>
                <img src="/static_new/images/dormitory-Qi.png">
                <em>5</em>
              </div>
                                <span class="dormitory-info-right">
                                    距离考勤结束:<label>11:11:11</label>
                                </span>
            </div>
            <div class="dormitory-num">
              <span>缺勤:<em>张文珊</em><em>马涛涛</em><em>张文珊</em><em>马涛涛</em><em>张文珊</em><em>马涛涛</em><em>张文珊</em><em>马涛涛</em><em>张文珊</em><em>马涛涛</em><em>张文珊</em><em>马涛涛</em><em>张文珊</em><em>马涛涛</em></span>
              <span class="dormitory-num-last">请假:<em>张文珊</em><em>马涛涛</em></span>
            </div>
            <div class="check-main">
              <ul>
                <li>
                  <div class="check-li-top">301<em>5/6</em></div>
                  <div class="check-li-bo">
                    <ul>
                      <li class="check-bo-li dormitory-QI">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                    </ul>
                  </div>
                </li>
                <li>
                  <div class="check-li-top">301<em>5/6</em></div>
                  <div class="check-li-bo">
                    <ul>
                      <li class="check-bo-li dormitory-QJ">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                    </ul>
                  </div>
                </li>
                <li>
                  <div class="check-li-top">301<em>5/6</em></div>
                  <div class="check-li-bo">
                    <ul>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                    </ul>
                  </div>
                </li>
                <li>
                  <div class="check-li-top">301<em>5/6</em></div>
                  <div class="check-li-bo">
                    <ul>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                      <li class="check-bo-li">
                        <div class="check-bo-num">1</div>
                        <em>张宇</em>
                      </li>
                    </ul>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      <!--=======================宿舍考勤end=========================-->
      <!--========================考勤记录管理start==================-->
      <div id="dormiroty-record">
        <div class="tab-head clearfix">
          <ul>
            <li class="dorm"><a href="javascript:;" >宿舍考勤&gt;考勤记录管理</a><em></em></li>
          </ul>
          <div class="check-right dormitory-right">
            <span>导出</span>
          </div>
        </div>
        <div class="tab-main">
          <div class="dormitory-main">
            <div class="check-top">
              <div>
                <em>分区</em>
                <select>
                  <option>全部</option>
                  <option>1号宿舍</option>
                  <option>2号宿舍</option>
                </select>
              </div>
              <div>
                <em>楼层</em>
                <select>
                  <option>全部</option>
                  <option>1F</option>
                  <option>2F</option>
                </select>
              </div>
              <div>
                <em>性别</em>
                <select>
                  <option>全部</option>
                  <option>男</option>
                  <option>女</option>
                </select>
              </div>
              <div class="dormitory-DV">
                <em>时间</em>
                <input onclick="WdatePicker()">-<input onclick="WdatePicker()">
              </div>
              <span>查询</span>
            </div>
          </div>
        </div>
        <div class="check-main">
          <div class="dorm-info">
            <table>
              <tr>
                <th width="45">序号</th>
                <th width="100">姓名</th>
                <th width="110">班级</th>
                <th width="100">分区</th>
                <th width="70">房间号</th>
                <th width="160">刷卡日趋</th>
                <th width="155">操作</th>
              </tr>
              <tr>
                <td width="45">1</td>
                <td width="100">乔海波</td>
                <td width="110">三年级（1）班</td>
                <td width="100">1号宿舍楼</td>
                <td width="70">301</td>
                <td width="160">2016/08/0818:00</td>
                <td width="155">
                  <em>编辑</em>|<em>删除</em>
                </td>
              </tr>
              <tr>
                <td width="45">1</td>
                <td width="100">乔海波</td>
                <td width="110">三年级（1）班</td>
                <td width="100">1号宿舍楼</td>
                <td width="70">301</td>
                <td width="160">2016/08/0818:00</td>
                <td width="155">
                  <em>编辑</em>|<em>删除</em>
                </td>
              </tr>
              <tr>
                <td width="45">1</td>
                <td width="100">乔海波</td>
                <td width="110">三年级（1）班</td>
                <td width="100">1号宿舍楼</td>
                <td width="70">301</td>
                <td width="160">2016/08/0818:00</td>
                <td width="155">
                  <em>编辑</em>|<em>删除</em>
                </td>
              </tr>
            </table>
          </div>
        </div>
      </div>
      <!--========================考勤记录管理end====================-->
      <!--========================考勤时间设置start==================-->
      <div id="dormiroty-set">
        <div class="tab-head clearfix">
          <ul>
            <li class="dorm"><a href="javascript:;" >宿舍考勤&gt;考勤时间设置</a><em></em></li>
          </ul>
          <div class="check-right dormitory-right">
            <span>导出</span>
          </div>
        </div>
        <div class="set-info">
          <table>
            <thead>
            <tr>
              <th width="90"></th>
              <th width="325">早间时段</th>
              <th width="345">晚间时段</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td width="90">星期一</td>
              <td width="325">
                <input placeholder="8:00">至<input placeholder="8:30">
              </td>
              <td width="345">
                <input placeholder="20:00">至<input placeholder="20:30">
              </td>
            </tr>
            <tr>
              <td width="90">星期二</td>
              <td width="325">
                <input placeholder="8:00">至<input placeholder="8:30">
              </td>
              <td width="345">
                <input placeholder="20:00">至<input placeholder="20:30">
              </td>
            </tr>
            <tr>
              <td width="90">星期三</td>
              <td width="325">
                <input placeholder="8:00">至<input placeholder="8:30">
              </td>
              <td width="345">
                <input placeholder="20:00">至<input placeholder="20:30">
              </td>
            </tr>
            <tr>
              <td width="90">星期四</td>
              <td width="325">
                <input placeholder="8:00">至<input placeholder="8:30">
              </td>
              <td width="345">
                <input placeholder="20:00">至<input placeholder="20:30">
              </td>
            </tr>
            <tr>
              <td width="90">星期五</td>
              <td width="325">
                <input placeholder="8:00">至<input placeholder="8:30">
              </td>
              <td width="345">
                <input placeholder="20:00">至<input placeholder="20:30">
              </td>
            </tr>
            <tr>
              <td width="90">星期六</td>
              <td width="325">
                <input placeholder="8:00">至<input placeholder="8:30">
              </td>
              <td width="345">
                <input placeholder="20:00">至<input placeholder="20:30">
              </td>
            </tr>
            <tr>
              <td width="90">星期天</td>
              <td width="325">
                <input placeholder="8:00">至<input placeholder="8:30">
              </td>
              <td width="345">
                <input placeholder="20:00">至<input placeholder="20:30">
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!--========================考勤时间设置end====================-->
      <!--========================请假记录start==================-->
      <div id="dormiroty-leave">
        <div class="tab-head clearfix">
          <ul>
            <li class="dorm"><a href="javascript:;" >宿舍考勤&gt;请假登记</a><em></em></li>
          </ul>
          <div class="check-right dormitory-right">
            <span class="leave-dor">请假记录</span>
          </div>
        </div>
        <div class="tab-main">
          <div class="dormitory-main">
            <div class="check-top">
              <div>
                <em>分区</em>
                <select>
                  <option>全部</option>
                  <option>1号宿舍</option>
                  <option>2号宿舍</option>
                </select>
              </div>
              <div>
                <em>性别</em>
                <select>
                  <option>全部</option>
                  <option>男</option>
                  <option>女</option>
                </select>
              </div>
              <div>
                <em>楼层</em>
                <select>
                  <option>全部</option>
                  <option>1F</option>
                  <option>2F</option>
                </select>
              </div>
              <div>
                <em>宿舍</em>
                <select>
                  <option>全部</option>
                  <option>1号宿舍</option>
                  <option>2号宿舍</option>
                </select>
              </div>

              <div>
                <em>姓名</em>
                <input>
              </div>
              <span>查询</span>
            </div>
          </div>
        </div>
        <div class="check-main">
          <div class="dorm-info">
            <table>
              <tr>
                <th width="45">序号</th>
                <th width="100">姓名</th>
                <th width="110">班级</th>
                <th width="100">分区</th>
                <th width="70">房间号</th>
                <th width="160">床位</th>
                <th width="155">操作</th>
              </tr>
              <tr>
                <td width="45">1</td>
                <td width="100">乔海波</td>
                <td width="110">三年级（1）班</td>
                <td width="100">1号宿舍楼</td>
                <td width="70">301</td>
                <td width="160">1</td>
                <td width="155">
                  <em class="dormitory-Lv">请假</em>
                </td>
              </tr>
              <tr>
                <td width="45">1</td>
                <td width="100">乔海波</td>
                <td width="110">三年级（1）班</td>
                <td width="100">1号宿舍楼</td>
                <td width="70">301</td>
                <td width="160">2</td>
                <td width="155">
                  <em>延长假期</em>|<em>销假</em>
                </td>
              </tr>
              <tr>
                <td width="45">1</td>
                <td width="100">乔海波</td>
                <td width="110">三年级（1）班</td>
                <td width="100">1号宿舍楼</td>
                <td width="70">301</td>
                <td width="160">3</td>
                <td width="155">
                  <em class="dormitory-Lv">请假</em>
                </td>
              </tr>
            </table>
          </div>
        </div>
      </div>
      <!--========================请假记录end====================-->
      <!--========================请假记录start==================-->
      <div id="dormiroty-leave-I">
        <div class="tab-head clearfix">
          <ul>
            <li class="dorm"><a href="javascript:;" >宿舍考勤&gt;请假登记&gt;请假登记</a><em></em></li>
          </ul>
        </div>
        <div class="tab-main">
          <div class="dormitory-main">
            <div class="check-top">
              <div class="dormitory-DVV">
                <em>起止时间</em>
                <input onclick="WdatePicker()">-<input onclick="WdatePicker()">
              </div>
              <div>
                <em>分区</em>
                <select>
                  <option>全部</option>
                  <option>1号宿舍</option>
                  <option>2号宿舍</option>
                </select>
              </div>
              <div>
                <em>性别</em>
                <select>
                  <option>全部</option>
                  <option>男</option>
                  <option>女</option>
                </select>
              </div>
              <div>
                <em>姓名</em>
                <input>
              </div>
              <span>查询</span>
            </div>
          </div>
        </div>
        <div class="check-main">
          <div class="dorm-info">
            <table>
              <tr>
                <th width="45">序号</th>
                <th width="115">姓名</th>
                <th width="120">班级</th>
                <th width="120">分区</th>
                <th width="70">房间号</th>
                <th width="70">床位</th>
                <th width="210">请假时间</th>
              </tr>
              <tr>
                <td width="45">1</td>
                <td width="115">乔海波</td>
                <td width="120">三年级（1）班</td>
                <td width="120">1号宿舍楼</td>
                <td width="70">301</td>
                <td width="70">1</td>
                <td width="210">2016/08/01-2015/08/04</td>
              </tr>
              <tr>
                <td width="45">1</td>
                <td width="115">乔海波</td>
                <td width="120">三年级（1）班</td>
                <td width="120">1号宿舍楼</td>
                <td width="70">301</td>
                <td width="70">1</td>
                <td width="210">2016/08/01-2015/08/04</td>
              </tr>
              <tr>
                <td width="45">1</td>
                <td width="115">乔海波</td>
                <td width="120">三年级（1）班</td>
                <td width="120">1号宿舍楼</td>
                <td width="70">301</td>
                <td width="70">1</td>
                <td width="210">2016/08/01-2015/08/04</td>
              </tr>
            </table>
          </div>
        </div>
      </div>
      <!--========================请假记录end====================-->
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
</div>
</div>
<!--==========================分区新建编辑弹出框start==============================-->
<div class="leave-popup">
  <div class="popup-top">
    <em>请假</em><i>X</i>
  </div>
  <div class="dorm-new-main">
    <dl>
      <dd>
        <label class="dorm-dell">
          <span>李云飞</span>
          <span>三年（1）班</span>
          <span>房间号：301</span>
        </label>
      </dd>
      <dd>
        <em>请假原因</em>
        <textarea></textarea>
      </dd>
    </dl>
  </div>
  <div class="dorm-new-bottom">
    <span class="dorm-QD">确定</span>
    <span class="dorm-QX">取消</span>
  </div>
</div>
<!--==========================新建编辑弹出框end==============================-->
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
