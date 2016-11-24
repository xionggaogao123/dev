<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="fnn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<% 
  String path = request.getContextPath();    
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
    pageContext.setAttribute("basePath",basePath); 
    %>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-考试</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">

    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/exam/studentlist.css?v=2015120801" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
 
</head>
<body>


<!--#head-->
 <%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

<!--.col-left-->
<%@ include file="../common_new/col-left.jsp" %>
<!--/.col-left-->

<%--
   <c:choose>
            <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
                <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
            </c:otherwise>
    </c:choose>--%>
<!--.col-right-->
    <div class="col-right">
        <div class="modify_main_I" style="margin-top: -20px;">
            <c:choose>
                <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                    <span style="cursor: pointer" onclick="gotoPapers()">全部试卷></span>
                </c:when>
            </c:choose>
            <span>${name }</span>
        </div>
        <%--角色判断，不带课的校长不可以上传考卷， modify by miaoqiang--%>
        <c:choose>
            <c:when test="${roles:isTeacher(sessionValue.userRole)}">
                <div class="test-banner" style="width:885px">
                    <div class="test-banner-container" style="margin:16px auto">
                        <table>
                            <tr>
                                <td>
                                    <a href="/exam/index.do">
                                        <div class="test-item my-exam active">上传考卷</div>
                                    </a>
                                </td>

                                    <%--<td>--%>
                                    <%--<a href="/flippedExams"><div class="test-item online-test">高考题库</div></a>--%>
                                    <%--</td>--%>
                                    <%--<td>--%>
                                    <%--<a href="/user/flippedMyQuestionBook"><div class="test-item ques-book">错题集</div></a>--%>
                                    <%--</td>--%>
                            </tr>
                        </table>
                    </div>
                </div>
            </c:when>
        </c:choose>

        <input type="hidden" value="${id}" id="did"/>
        <div class="tab-col">
            <div class="tab-top clearfix">
                 <ul>
                      <li class="" id=""><a href="/exam/answer/stat/list.do?id=${id}">考试题目</a></li>
                      <li class="cur" id=""><a href="javascript:;">考试学生</a></li>
                 </ul>
            </div>
            <div id="contentt">
            
                <div class="student-XS">
                    <table>
                        <tr>
                            <th>姓名</th>
                            <th>得分</th>
                            <th>提交时间</th>
                            <th>优秀试卷</th>
                            <th></th>
                        </tr>
                        
                        <c:forEach var="dto" items="${ list}">
                        <tr>
                            <td>${dto.userName}</td>
                            <td>${dto.score }</td>
                            <td>${dto.time}</td>
                            <td style="width:75px">
                                <c:if test="${dto.isGoods==1}">
                                <img src="/img/ks-update.png" id="${dto.userId}_img">
                                </c:if>
                                <c:if test="${dto.isGoods==0}">
                                <img src="/img/ks-update.png" id="${dto.userId}_img" style="display:none;">
                                </c:if>
                            </td>
                            <td class="list-BU" id="${dto.userId }_td">
                             <c:if test="${dto.isGoods==1}">
                                  <button id="${dto.userId }_1" class="list-blue" onclick="removeGoodPaper('${dto.userId}')">取消优秀试卷</button>
                                  <button id="${dto.userId }_2" class="list-yellow" onclick="addGoodPaper('${dto.userId}')" style="display:none">设为优秀试卷</button>
                             </c:if>
                             <c:if test="${dto.isGoods==0}">
                                 <button id="${dto.userId }_1" class="list-blue" style="display:none" onclick="removeGoodPaper('${dto.userId}')">取消优秀试卷</button>
                                 <button id="${dto.userId }_2" class="list-yellow" onclick="addGoodPaper('${dto.userId}')">设为优秀试卷</button>
                             </c:if>
                             
                             <c:if test="${dto.isHandled==0}">
                                <a class="list-yellow" href="/exam/correct/student.do?did=${id}&ui=${dto.userId}" >去批改</a>
                             </c:if>
                              <c:if test="${dto.isHandled==1}">
                               <a class="list-yellow" href="/exam/view.do?id=${id}&type=2&stuid=${dto.userId}" >查看</a>
                             </c:if>
                            </td>
                        </tr>
                        </c:forEach>
                        
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<script src="/static_new/js/sea.js"></script>
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('studentlist',function(studentlist){
    	studentlist.init();
    });
</script>

</body>
</html>