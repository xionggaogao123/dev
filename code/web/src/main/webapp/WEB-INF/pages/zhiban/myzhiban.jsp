<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>值班</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/zhiban/jiaban.css"/>
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script>
    $(function(){
      $(".tab-head li").click(function(){
        $(this).addClass("cur").siblings().removeClass("cur");
        $(".tab-main>div").hide();
        var names = $(this).attr("id");
        $("#" + "tab-" + names).show();
      })
      $(".LWDJB-CK").click(function(){
        $(".LWDZB-I").hide();
        $(".LWDZB-II").show();
      })
      $(".LWDZB-II a").click(function(){
        $(".LWDZB-I").show();
        $(".LWDZB-II").hide();
      })
      $(".LWDJB-HB").click(function(){
        $(".popup-HB").show();
        $(".bg").show();
      })
      $(".SQ-X").click(function(){
        $(".popup-HB").hide();
        $(".bg").hide();
        $(".popup-ZBJL").hide();
      })
      $(".LWDJB-TX").click(function(){
        $(".popup-ZBJL").show();
        $(".bg").show();
      })
    })
  </script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>

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
          <li class="cur" id="LWDZB"><a href="javascript:;" >我的值班</a><em></em></li>
          <li id="LZBXC"><a href="javascript:;" >值班薪酬</a><em></em></li>
        </ul>
      </div>

      <div class="tab-main">
        <!--================我的值班start=================-->
        <div id="tab-LWDZB" class="tab-LWDZB">
          <div class="LWDZB-I">
            <div class="LWDJB-top">
              <div class="LWDJB-top-left">
                <span>当前时间</span><em>2016/11/11</em>
              </div>
              <div class="LWDJB-top-right">
                <span class="LWDJB-CK h-cursor">查看加班记录</span>
              </div>
            </div>
            <div class="LWDJB-main">
              <table>
                <thead>
                <tr>
                  <th width="185">时间</th>
                  <th width="565">事项</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                  <td class="JBSQ-td" width="185">
                    <img src="/static_new/images/zhiban/LWDJB-day.jpg">
                    216/6/6星期日
                  </td>
                  <td widht="565"></td>
                </tr>
                <tr>
                  <td class="JBSQ-td" width="185">星期一</td>
                  <td width="565">
                    <em>上午8:00-12:00学校值班日常值班</em>
                    <div>
                      <span class="LWDJB-TX h-cursor">填写值班记录</span><span class="LWDJB-QT h-cursor">签退</span>
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="JBSQ-td" width="185">星期二</td>
                  <td width="565">
                    <em></em>
                    <div>
                      <span class="LWDJB-HB h-cursor">申请换班</span><span class="LWDJB-QD h-cursor">签到</span>
                    </div>

                  </td>
                </tr>
                <tr>
                  <td class="JBSQ-td" width="185">星期三</td>
                  <td width="565"></td>
                </tr>
                <tr>
                  <td class="JBSQ-td" width="185">星期四</td>
                  <td width="565"></td>
                </tr>
                <tr>
                  <td class="JBSQ-td" width="185">星期五</td>
                  <td width="565"></td>
                </tr>
                <tr>
                  <td class="JBSQ-td" width="185">星期六</td>
                  <td width="565"></td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="LWDZB-II">
            <a href="#">&lt;返回我的值班</a>
            <div class="JBSQ-top">
              <select>
                <option>2016年</option>
                <option>2015年</option>
              </select>
              <select>
                <option>一月</option>
                <option>二月</option>
                <option>三月</option>
                <option>四月</option>
                <option>五月</option>
                <option>六月</option>
                <option>七月</option>
                <option>八月</option>
                <option>九月</option>
                <option>十月</option>
                <option>十一月</option>
                <option>十二月</option>
              </select>
              <span>查询</span>
            </div>
            <div class="LWDJB-main">
              <table>
                <thead>
                <tr>
                  <th width="105">值班日期</th>
                  <th width="80">值班时段</th>
                  <th width="110">值班时间</th>
                  <th width="115">值班项目</th>
                  <th width="340">值班记录</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                  <td class="JBSQ-td" width="105">2016/11/11</td>
                  <td width="80">上午</td>
                  <td width="110">8：00-12:00</td>
                  <td width="115">日常值班</td>
                  <td width="340">一起正常</td>
                </tr>
                <tr>
                  <td class="JBSQ-td" width="105">2016/11/11</td>
                  <td width="80">上午</td>
                  <td width="110">8：00-12:00</td>
                  <td width="115">日常值班</td>
                  <td width="340">一起正常</td>
                </tr>
                <tr>
                  <td class="JBSQ-td" width="105">2016/11/11</td>
                  <td width="80">上午</td>
                  <td width="110">8：00-12:00</td>
                  <td width="115">日常值班</td>
                  <td width="340">一起正常</td>
                </tr>
                <tr>
                  <td class="JBSQ-td" width="105">2016/11/11</td>
                  <td width="80">上午</td>
                  <td width="110">8：00-12:00</td>
                  <td width="115">日常值班</td>
                  <td width="340">一起正常</td>
                </tr>
                <tr>
                  <td class="JBSQ-td" width="105">2016/11/11</td>
                  <td width="80">上午</td>
                  <td width="110">8：00-12:00</td>
                  <td width="115">日常值班</td>
                  <td width="340">一起正常</td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
        <!--================我的值班end=================-->
        <!--===============值班薪酬start=================-->
        <div class="tab-LZBXC" id="tab-LZBXC">
          <div class="JBSQ-top">
            <select>
              <option>2016年</option>
              <option>2015年</option>
            </select>
            <select>
              <option>一月</option>
              <option>二月</option>
              <option>三月</option>
              <option>四月</option>
              <option>五月</option>
              <option>六月</option>
              <option>七月</option>
              <option>八月</option>
              <option>九月</option>
              <option>十月</option>
              <option>十一月</option>
              <option>十二月</option>
            </select>
            <span>查询</span>
          </div>
          <div class="JBSQ-middle">
            <span class="JBSQ-daochu h-cursor">导出</span>
          </div>
          <div class="JBSQ-main">
            <table>
              <thead>
              <tr>
                <th width="120">值班日期</th>
                <th width="120">值班时段</th>
                <th width="125">值班时间</th>
                <th width="125">值班内容</th>
                <th width="130">值班时长(h)</th>
                <th width="130">值班薪酬(元)</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td class="JBSQ-td" width="120">2016/6/1</td>
                <td width="120">上午</td>
                <td width="125">8:00-12:00</td>
                <td width="125">日常值班</td>
                <td width="130">4</td>
                <td width="130">
                  30
                </td>
              </tr>
              <tr>
                <td class="JBSQ-td" width="120">2016/6/1</td>
                <td width="120">上午</td>
                <td width="125">8:00-12:00</td>
                <td width="125">日常值班</td>
                <td width="130">4</td>
                <td width="130">
                  30
                </td>
              </tr>
              <tr>
                <td class="JBSQ-td" width="120">2016/6/1</td>
                <td width="120">上午</td>
                <td width="125">8:00-12:00</td>
                <td width="125">日常值班</td>
                <td width="130">4</td>
                <td width="130">
                  30
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
        <!--================值班薪酬end=================-->
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
<div class="bg"></div>
<!--=================申请换班弹窗框start===================-->
<div class="popup-HB">
  <div class="popup-HB-top">
    <em>申请换班</em><i class="h-cursor SQ-X">X</i>
  </div>
  <div class="popup-HB-main">
    <dl>
      <dd>
        <em>申请人：</em><label>siri</label>
      </dd>
      <dd>
        <em>值班日期：</em><label>8:00-12:00</label>
      </dd>
      <dd>
        <em>值班时段：</em><label>晚间</label>
      </dd>
      <dd>
        <em>值班项目：</em><label>日常值班</label>
      </dd>
      <dd>
        <em>换班理由：</em>
        <textarea></textarea>
      </dd>
    </dl>
  </div>
  <div class="popup-HB-bottom">
    <span class="HB-TJ h-cursor">提交</span>
    <span class="HB-QX h-cursor">取消</span>
  </div>
</div>
<!--=================申请换班弹窗框end===================-->
<!--=================值班记录弹窗框start===================-->
<div class="popup-ZBJL">
  <div class="popup-HB-top">
    <em>值班记录</em><i class="h-cursor SQ-X">X</i>
  </div>
  <div class="popup-HB-main">
    <dl>
      <dd>
        <em>值班日期：</em><label>8:00-12:00</label>
      </dd>
      <dd>
        <em>值班时段：</em><label>晚间</label>
      </dd>
      <dd>
        <em>值班项目：</em><label>日常值班</label>
      </dd>
      <dd>
        <em>值班地点：</em><label>192.168.11.111</label>
      </dd>
      <dd>
        <em>换班理由：</em>
        <textarea></textarea>
      </dd>
      <dd>
        <em>添加附件</em>
      </dd>
    </dl>
  </div>
  <div class="popup-HB-bottom">
    <span class="HB-TJ h-cursor">提交</span>
    <span class="HB-QX h-cursor">取消</span>
  </div>
</div>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('zhiban');
</script>

</body>
</html>