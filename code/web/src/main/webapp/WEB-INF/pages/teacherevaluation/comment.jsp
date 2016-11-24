<%--
  Created by IntelliJ IDEA.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <title>教师评价-老师</title>
  <!-- css files -->
  <!-- Normalize default styles -->
  <meta name="renderer" content="webkit">
  <link rel="stylesheet" type="text/css" href="/static_new/css/teacherevaluation/te-comments.css">
  <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>

<body>


<!--#head-->
<%@ include file="../common_new/head.jsp"%>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

  <!--.col-left-->
  <%@ include file="../common_new/col-left.jsp" %>
  <!--/.col-left-->

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
    <!--==========================我的教材start==========================-->
    <div class="tab-col" id="JC">

      <div class="tab-head clearfix">
        <a>教师评价系统</a>
      </div>
      <div class="tab-main">
        <div class="te-comments">
          <ul>
            <li>
              <em>学期</em>
              <select id="termlist">
                <c:forEach items="${termList}" var="term">
                  <option value="${term}">${term}</option>
                </c:forEach>
              </select>
            </li>
            <li>
              <em>年级</em>
              <select id="gradelist">
                <option value="">全部</option>
             <c:forEach items="${gradeList}" var="grade">
               <option value="${grade.id}">${grade.name}</option>
             </c:forEach>
              </select>
            </li>
            <li>
              <em>教师姓名</em>
              <input id="userName">
            </li>
          </ul>
          <span id="seachQuality">查询</span>
        </div>
        <div class="te-comments-info">
          <table>
            <thead>
            <tr>
              <th width="105px">教师姓名</th>
              <th width="100px;">年级</th>
              <th width="60px;">微课数</th>
              <th width="60px;">课件数</th>
              <th width="60px;">习题数</th>
              <th width="160px;">系统星级评价</th>
              <th width="105px;">上级领导评价</th>
              <th width="105px;">打分</th>
            </tr>
            </thead>
            <tbody id="qualityList">

            </tbody>

            <script type="text/template" id="quality_templ">
              {{ if(it.rows.length>0){ }}
              {{ for (var i = 0, l = it.rows.length; i < l; i++) { }}
              {{var obj=it.rows[i];}}
              <tr>
                <td width="105px">{{=obj.teacherName}}</td>
                <td width="100px;">{{=obj.gradeName}}</td>
                <td width="60px;">{{=obj.lessonCnt}}</td>
                <td width="60px;">{{=obj.wareCnt}}</td>
                <td width="60px;">{{=obj.examCnt}}</td>
                <td width="160px;">
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  {{?obj.lessonCnt<=1}}
                  <img width="17" src="/static_new/images/te-comm.jpg"/>
                  <img width="17" src="/static_new/images/te-comm.jpg"/>
                  <img width="17" src="/static_new/images/te-comm.jpg"/>
                  <img width="17" src="/static_new/images/te-comm.jpg"/>
                  <em>需努力</em>
                  {{?}}
                  {{?obj.lessonCnt>1 && obj.lessonCnt<=10}}
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  <img width="17" src="/static_new/images/te-comm.jpg"/>
                  <img width="17" src="/static_new/images/te-comm.jpg"/>
                  <img width="17" src="/static_new/images/te-comm.jpg"/>
                  <em>需努力</em>
                  {{?}}
                  {{?obj.lessonCnt>10 && obj.lessonCnt<=20}}
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  <img width="17" src="/static_new/images/te-comm.jpg"/>
                  <img width="17" src="/static_new/images/te-comm.jpg"/>
                  <em>合格</em>
                  {{?}}
                  {{?obj.lessonCnt>20 && obj.lessonCnt<=40}}
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  <img width="17" src="/static_new/images/te-comm.jpg"/>
                  <em>良好</em>
                  {{?}}
                  {{?obj.lessonCnt>40}}
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  <img width="17" src="/static_new/images/te-com.jpg"/>
                  <em>优秀</em>
                  {{?}}
                </td>
                <td width="105px;">
                  {{?obj.type==1}}
                  <span class="comments-cha" tid="{{=obj.teacherId}}" com="{{=obj.comment}}" tnm="{{=obj.teacherName}}">查看评语</span>
                  {{?}}
                  <c:choose>
                    <c:when test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                                    <span class="comment-red" tid="{{=obj.teacherId}}" com="{{=obj.comment}}" tnm="{{=obj.teacherName}}">写评语</span>
                      </c:when>
                    </c:choose>

                </td>
                <td width="105px;" tid="{{=obj.teacherId}}">
                <c:choose>
                  <c:when test="${roles:isHeadmaster(sessionValue.userRole) || roles:isManager(sessionValue.userRole)}">
                  <input value="{{=obj.score}}" class="score"/>
                </c:when>
                  <c:otherwise>
                    <input value="{{=obj.score}}" style="display: none;"/>
                  </c:otherwise>
            </c:choose>
                </td>
              </tr>
              {{ } }}
              {{ } }}
            </script>
          </table>
        </div>
        <!--==================分页======================-->
        <div class="mypromoteactivity" style="display:block;"></div>
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
        <!--==================分页end======================-->
        <div class="te-comments-bo">
          <div style="font-size: 15px;font-weight: bold;">系统星级评价说明</div>
          <div style="margin-top: 10px;">
            <ul>
              <li>
               <%-- <em>1</em>--%>
                <p id="commentDetail"><em>星级评价说明：</em><span>系统根据老师备课的数量（微课数、课件数和习题数）自动生成星星级评价。</span></p>
                <p><em>评价共分5级：</em><span>五星为“优秀”，四星为“良好”，三星为“合格”，二星及一星为“需努力”，系统初始默认为一星。</span></p>
                <p><em>评价规则：</em><span>备课总数40以上，五星</span></p>
                <p><em></em><span>21-40,四星；</span></p>
                <p><em></em><span>11-20，三星；</span></p>
                <p><em></em><span>2-10，二星；</span></p>
              </li>
            <%--  <li>
                <em>2</em>
                <p></p>
              </li>
              <li>
                <em>3</em>
                <p></p>
              </li>--%>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!--写评语start-->
<div class="comments-popup">
  <div class="comments-popup-top">
    <em>领导评价</em><i class="h-cursor SQ-X XZZB-QX">X</i>
  </div>
  <h1 id="uname"></h1>
  <div class="comments-popu-info">
    <span>上级领导评价</span><em></em>
  </div>
  <input id="teaId" hidden="hidden">
  <textarea class="input" id="comment"></textarea>
  <div class="popup-XZZB-bottom">
    <span class="XZZB-TJ sure-comment">确认</span>
    <span class="XZZB-QX">取消</span>
  </div>
</div>
<!--写评语end-->
<!--评语start-->
<div class="comments-popupp">
  <div class="comments-popup-top">
    <em>领导评价</em><i class="h-cursor SQ-X XZZB-QX">X</i>
  </div>
  <h1 id="uname2"></h1>
  <div class="comments-popu-info">
    <span>上级领导评价</span><em></em>
  </div>
  <p id="commentD">啦时刻提防；阿卡；士大夫啊；塑料袋封口</p>
  <div class="popup-XZZB-bottom">
    <span class="XZZB-QX">确认</span>
    <span class="XZZB-QX">取消</span>
  </div>
</div>
<!--评语end-->
<div class="bg"></div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/teacherevaluation/0.1.0/comment');
</script>

</body>
</html>
