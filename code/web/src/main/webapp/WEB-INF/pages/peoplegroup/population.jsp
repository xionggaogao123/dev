<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>人群分布</title>
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
          <li class="cur" id="MYHD"><a href="javascript:;" >人群分布</a><em></em></li>
        </ul>
      </div>
      <div class="tab-main">
        <!--==========================一卡通人群分布start==========================-->
        <div class="population-main" id="population-I">
          <div class="population-title">${schoolName}平面示意图</div>
          <div class="population-info">
            <div class="population-left">
              <img src="/static_new/images/smartcard/population-bg.jpg" >
              <div class="population-po"></div>
              <div class="population-tb">
                <dl class="hwk2">
                </dl>
                <script type="text/template" id="hwk2_templ">
                  {{ if(it.rows.length>0){ }}
                  {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                  {{var obj=it.rows[i];}}
                  <dd>
                    <i class="population-F">{{=obj.roomName}}</i>
                    <img src="/static_new/images/smartcard/population-tou.jpg">
                    <em>{{=obj.count}}</em>
                  </dd>
                  {{ } }}
                  {{ } }}
                </script>
              </div>
              <div class="population-tri">

              </div>
            </div>
            <div class="population-right">
              <table>
                <thead>
                <tr>
                  <th>位置</th>
                  <th>人数</th>
                </tr>
                </thead>
                <tbody class="hwk1">

                </tbody>
                <script type="text/template" id="hwk1_templ">
                  {{ if(it.rows.length>0){ }}
                  {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                  {{var obj=it.rows[i];}}
                  <tr>
                    {{?obj.roomName=='总计'}}<td class="pop-td">{{??}}<td>{{?}}
                    {{=obj.roomName}}</td>
                    {{?obj.roomName=='总计'}}<td class="pop-td">{{??}}<td>{{?}}
                    {{=obj.count}}</td>
                  </tr>
                  {{ } }}
                  {{ } }}
                </script>
              </table>
            </div>
          </div>
        </div>
        <!--==========================一卡通人群分布end==========================-->
        <!--==========================一卡通人群分布善进楼start==========================-->
        <div class="population-main" id="population-II">
          <a class="population-II" href="javascript:;">&lt;返回校园平面图</a>
          <div class="population-title">${schoolName}平面示意图</div>
          <div class="population-info">
            <div class="population-left">
              <img src="/static_new/images/smartcard/population-bgg.jpg" >
              <div class="population-poo"></div>
            </div>
            <div class="population-right">
              <table>
                <thead>
                <tr>
                  <th>位置</th>
                  <th>人数</th>
                </tr>
                </thead>
                <tbody class="hwk3">

                </tbody>
                <script type="text/template" id="hwk3_templ">
                  {{ if(it.rows.length>0){ }}
                  {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
                  {{var obj=it.rows[i];}}
                  <tr>
                    {{?obj.roomName=='总计'}}<td class="pop-td">{{??}}<td>{{?}}
                    {{=obj.roomName}}</td>
                    {{?obj.roomName=='总计'}}<td class="pop-td">{{??}}<td>{{?}}
                    {{=obj.count}}</td>
                  </tr>
                  {{ } }}
                  {{ } }}
                </script>
              </table>
            </div>
          </div>
        </div>
        <!--==========================一卡通人群分布善进楼end==========================-->
        <!--======================阶梯教室start==========================-->
        <div class="population-main-III" id="population-III">
          <a class="population-III" href="javascript:;">&lt;返回</a>
          <p>301阶教室</p>
          <table>
            <thead>
            <tr>
              <th width="60">序号</th>
              <th width="100">姓名</th>
              <th width="100">班级</th>
              <th width="100">性别</th>
              <th width="60">序号</th>
              <th width="100">姓名</th>
              <th width="100">班级</th>
              <th width="100">性别</th>
            </tr>
            </thead>
            <tbody>
            <tr>
              <td width="60">1</td>
              <td width="100">张秦风</td>
              <td width="100">三年（2）班</td>
              <td width="100">女</td>
              <td width="60">17</td>
              <td width="100">王雪飞</td>
              <td width="100">三年（2）班</td>
              <td width="100">女</td>
            </tr>
            <tr>
              <td>2</td>
              <td>黄玉山</td>
              <td>三年（2）班</td>
              <td>男</td>
              <td>18</td>
              <td>孙灿灿</td>
              <td>三年（2）班</td>
              <td>女</td>
            </tr>
            <tr>
              <td>3</td>
              <td>张薇薇</td>
              <td>三年（2）班</td>
              <td>女</td>
              <td>19</td>
              <td>林响</td>
              <td>三年（2）班</td>
              <td>男</td>
            </tr>
            <tr>
              <td>4</td>
              <td>秦海新</td>
              <td>三年（2）班</td>
              <td>女</td>
              <td>17</td>
              <td>金源</td>
              <td>三年（2）班</td>
              <td>女</td>
            </tr>
            <tr>
              <td>5</td>
              <td>江巧巧</td>
              <td>三年（2）班</td>
              <td>女</td>
              <td>21</td>
              <td>陈宇</td>
              <td>三年（2）班</td>
              <td>男</td>
            </tr>
            <tr>
              <td>6</td>
              <td>陈绮</td>
              <td>三年（2）班</td>
              <td>男</td>
              <td>22</td>
              <td>华晨</td>
              <td>三年（2）班</td>
              <td>男</td>
            </tr>
            <tr>
              <td>7</td>
              <td>张艺多</td>
              <td>三年（2）班</td>
              <td>女</td>
              <td>23</td>
              <td>李海波</td>
              <td>三年（2）班</td>
              <td>男</td>
            </tr>
            <tr>
              <td>8</td>
              <td>金梦蝶</td>
              <td>三年（2）班</td>
              <td>女</td>
              <td>24</td>
              <td>黄丽琳</td>
              <td>三年（2）班</td>
              <td>女</td>
            </tr>
            <tr>
              <td>9</td>
              <td>江多燕</td>
              <td>三年（2）班</td>
              <td>女</td>
              <td>25</td>
              <td>施海燕</td>
              <td>三年（2）班</td>
              <td>女</td>
            </tr>
            <tr>
              <td>10</td>
              <td>林菲</td>
              <td>三年（2）班</td>
              <td>女</td>
              <td>26</td>
              <td>何飞飞</td>
              <td>三年（2）班</td>
              <td>男</td>
            </tr>
            <tr>
              <td>11</td>
              <td>李彦</td>
              <td>三年（2）班</td>
              <td>女</td>
              <td>27</td>
              <td>徐鑫</td>
              <td>三年（2）班</td>
              <td>男</td>
            </tr>
            <tr>
              <td>12</td>
              <td>程一飞</td>
              <td>三年（2）班</td>
              <td>男</td>
              <td>28</td>
              <td>张小辰</td>
              <td>三年（2）班</td>
              <td>女</td>
            </tr>
            <tr>
              <td>13</td>
              <td>郭德烟</td>
              <td>三年（2）班</td>
              <td>女</td>
              <td>29</td>
              <td>马艳琳</td>
              <td>三年（2）班</td>
              <td>女</td>
            </tr>
            <tr>
              <td>14</td>
              <td>姜海陆</td>
              <td>三年（2）班</td>
              <td>女</td>
              <td>30</td>
              <td>费绮</td>
              <td>三年（2）班</td>
              <td>女</td>
            </tr>
            <tr>
              <td>15</td>
              <td>马哲涛</td>
              <td>三年（2）班</td>
              <td>男</td>
              <td>31</td>
              <td>宗雅</td>
              <td>三年（2）班</td>
              <td>女</td>
            </tr>
            <tr>
              <td>16</td>
              <td>许易鹏</td>
              <td>三年（2）班</td>
              <td>男</td>
              <td>32</td>
              <td>黄志分</td>
              <td>三年（2）班</td>
              <td>男</td>
            </tr>
            </tbody>
          </table>
        </div>
        <!--======================阶梯教室end==========================-->
      </div>
    </div>
    <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
</div>
</div>
<div class="zhiban-meng"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('population');
</script>


</body>
</html>