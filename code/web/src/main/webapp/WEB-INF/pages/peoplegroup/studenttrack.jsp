<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>学生轨迹</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="/static_new/css/peoplegroup/consume.css">
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
        <!--==========================学生轨迹start==========================-->
        <div  id="dorm-GL">
          <div class="tab-head clearfix">
            <ul>
              <li class="cur"><a href="javascript:;" >学生轨迹</a><em></em></li>
            </ul>
          </div>
          <div class="tab-main">
            <div class="track-main">
              <div class="track-top">
                <div>
                  <em>年级</em>
                  <select id="grade">
                    <option value="">全年级</option>
                    <c:forEach items="${gradelist}" var="grade">
                      <option value="${grade.id}">${grade.name}</option>
                    </c:forEach>
                  </select>
                </div>
                <div>
                  <em>班级</em>
                  <select id="class">
                    <option value="">全部</option>
                  </select>
                </div>
                <div>
                  <em>姓名</em>
                  <select id="user">
                    <option value="">全部</option>
                  </select>
                </div>
                <div class="consume-DV">
                  <em>起止时间</em>
                  <input onclick="WdatePicker()">-<input onclick="WdatePicker()">
                </div>
                <span>查询</span>
              </div>
              <div class="track-map">
                <div class="track-map-I"></div>
                <!--热点图-->
                <!--德善楼-->
                <em class="track-hotsopt-D"></em>
                <!--综合大楼-->
                <em class="track-hotsopt-Z"></em>
                <!--操场-->
                <em class="track-hotsopt-S"></em>
                <!--善进楼-->
                <em class="track-hotsopt-J"></em>
                <!--图书馆-->
                <em class="track-hotsopt-T"></em>
                <!--篮球场-->
                <em class="track-hotsopt-L"></em>
                <!--善恒楼-->
                <em class="track-hotsopt-SJ"></em>
                <!--器材室-->
                <em class="track-hotsopt-Q"></em>
              </div>
              <div class="track-bottom">
                <div class="track-btoom-left">
                  <table>
                    <tr>
                      <th>地点</th>
                      <th>次数</th>
                      <th>累计时长/h</th>
                      <th>操作</th>
                    </tr>
                    <tr>
                      <td>善进楼</td>
                      <td>1</td>
                      <td>2h</td>
                      <td>
                        <em class="track-XIQ">详情
                          <div class="track-btoom-right">
                            <em class="triangle-left"></em>
                            <em class="triangle-left-I"></em>
                            <div class="track-bottom-hot">
                              <div class="track-hot-left">
                                <em class="track-hot-I"></em>
                                <em class="track-hot-II"></em>
                              </div>
                              <div class="track-hot-right">
                                <i>善进楼</i>
                                <label>2016/8/16</label><label>15:23</label></br>
                                <label>2016/8/16</label><label>15:23</label>
                                <div>共计：2小时5分</div>
                              </div>
                            </div>
                          </div>
                        </em>
                      </td>
                    </tr>
                    <tr>
                      <td>德善楼</td>
                      <td>1</td>
                      <td>2h</td>
                      <td><em class="track-XIQ">详情
                        <div class="track-btoom-right">
                          <em class="triangle-left"></em>
                          <em class="triangle-left-I"></em>
                          <div class="track-bottom-hot">
                            <div class="track-hot-left">
                              <em class="track-hot-I"></em>
                              <em class="track-hot-II"></em>
                            </div>
                            <div class="track-hot-right">
                              <i>德善楼</i>
                              <label>2016/8/16</label><label>15:23</label></br>
                              <label>2016/8/16</label><label>15:23</label>
                              <div>共计：2小时5分</div>
                            </div>
                          </div>
                        </div>
                      </em>
                      </td>
                    </tr>
                    <tr>
                      <td>图书馆</td>
                      <td>4</td>
                      <td>4h</td>
                      <td><em class="track-XIQ">详情
                        <div class="track-btoom-right">
                          <em class="triangle-left"></em>
                          <em class="triangle-left-I"></em>
                          <div class="track-bottom-hot">
                            <div class="track-hot-left">
                              <em class="track-hot-I"></em>
                              <em class="track-hot-II"></em>
                            </div>
                            <div class="track-hot-right">
                              <i>图书馆</i>
                              <label>2016/8/16</label><label>15:23</label></br>
                              <label>2016/8/16</label><label>15:23</label>
                              <div>共计：2小时5分</div>
                            </div>
                          </div>
                        </div>
                      </em>
                      </td>
                    </tr>
                    <tr>
                      <td>善恒楼</td>
                      <td>0</td>
                      <td>0</td>
                      <td><em class="track-XIQ">详情
                        <div class="track-btoom-right">
                          <em class="triangle-left"></em>
                          <em class="triangle-left-I"></em>
                          <div class="track-bottom-hot">
                            <div class="track-hot-left">
                              <em class="track-hot-I"></em>
                              <em class="track-hot-II"></em>
                            </div>
                            <div class="track-hot-right">
                              <i>善恒楼</i>
                              <label>2016/8/16</label><label>15:23</label></br>
                              <label>2016/8/16</label><label>15:23</label>
                              <div>共计：2小时5分</div>
                            </div>
                          </div>
                        </div>
                      </em>
                      </td>
                    </tr>
                    <tr>
                      <td>综合大楼</td>
                      <td>0</td>
                      <td>0</td>
                      <td><em class="track-XIQ">详情
                        <div class="track-btoom-right">
                          <em class="triangle-left"></em>
                          <em class="triangle-left-I"></em>
                          <div class="track-bottom-hot">
                            <div class="track-hot-left">
                              <em class="track-hot-I"></em>
                              <em class="track-hot-II"></em>
                            </div>
                            <div class="track-hot-right">
                              <i>综合大楼</i>
                              <label>2016/8/16</label><label>15:23</label></br>
                              <label>2016/8/16</label><label>15:23</label>
                              <div>共计：2小时5分</div>
                            </div>
                          </div>
                        </div>
                      </em>
                      </td>
                    </tr>
                    <tr>
                      <td>篮球场</td>
                      <td>1</td>
                      <td>0</td>
                      <td><em class="track-XIQ">详情
                        <div class="track-btoom-right">
                          <em class="triangle-left"></em>
                          <em class="triangle-left-I"></em>
                          <div class="track-bottom-hot">
                            <div class="track-hot-left">
                              <em class="track-hot-I"></em>
                              <em class="track-hot-II"></em>
                            </div>
                            <div class="track-hot-right">
                              <i>篮球场</i>
                              <label>2016/8/16</label><label>15:23</label></br>
                              <label>2016/8/16</label><label>15:23</label>
                              <div>共计：2小时5分</div>
                            </div>
                          </div>
                        </div>
                      </em>
                      </td>
                    </tr>
                    <tr>
                      <td>操场</td>
                      <td>0</td>
                      <td>1h</td>
                      <td><em class="track-XIQ">详情
                        <div class="track-btoom-right">
                          <em class="triangle-left"></em>
                          <em class="triangle-left-I"></em>
                          <div class="track-bottom-hot">
                            <div class="track-hot-left">
                              <em class="track-hot-I"></em>
                              <em class="track-hot-II"></em>
                            </div>
                            <div class="track-hot-right">
                              <i>操场</i>
                              <label>2016/8/16</label><label>15:23</label></br>
                              <label>2016/8/16</label><label>15:23</label>
                              <div>共计：2小时5分</div>
                            </div>
                          </div>
                        </div>
                      </em>
                      </td>
                    </tr>
                    <tr>
                      <td>器材室</td>
                      <td>0</td>
                      <td>0</td>
                      <td><em class="track-XIQ">详情
                        <div class="track-btoom-right">
                          <em class="triangle-left"></em>
                          <em class="triangle-left-I"></em>
                          <div class="track-bottom-hot">
                            <div class="track-hot-left">
                              <em class="track-hot-I"></em>
                              <em class="track-hot-II"></em>
                            </div>
                            <div class="track-hot-right">
                              <i>器材室</i>
                              <label>2016/8/16</label><label>15:23</label></br>
                              <label>2016/8/16</label><label>15:23</label>
                              <div>共计：2小时5分</div>
                            </div>
                          </div>
                        </div>
                      </em>
                      </td>
                    </tr>
                  </table>
                </div>
                <!-- <div class="track-btoom-right">
                    <em class="triangle-left"></em>
                    <em class="triangle-left-I"></em>
                    <div class="track-bottom-hot">
                        <div class="track-hot-left">
                            <em class="track-hot-I"></em>
                            <em class="track-hot-II"></em>
                        </div>
                        <div class="track-hot-right">
                            <em>善进楼</em>
                            <label>2016/8/16</label><label>15:23</label></br>
                            <label>2016/8/16</label><label>15:23</label>
                            <div>共计：2小时5分</div>
                        </div>
                    </div>
                </div> -->
              </div>
            </div>
          </div>
        </div>
        <!--==========================学生轨迹end==========================-->
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

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('track');
</script>


</body>
</html>