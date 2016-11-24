<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <title>复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" href="/static/css/dialog.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/exercise/question.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    
    
    
    <style type="text/css">
        html, body {
            background: #f9f9f9 !important;
        }
    </style>
</head>
<body>
<!-- 页头 -->
<%--<%@ include file="/business/slice/k6ktHeader.jsp" %>--%>
<!--=======================头部==============================-->
<%@ include file="../common_new/head.jsp" %>
<div style="width: 1000px;margin: 0 auto;">

<%@ include file="../common_new/col-left.jsp" %>

<div style="width: 770px;position:relative;left:230px;">


<link rel="stylesheet" href="/static/css/common/test-banner.css"/>
<div class="test-banner"  >
    <!-- 左侧导航-->

    <%--<%@ include file="/business/common/left.jsp" %>--%>
    <div style="width: 20px;width: 100%;background-color: #ffffff;height: 10px;"></div>
    <div class="test-banner-container" style="margin:8px auto;position: relative;top: -990px;left: 150px;height: 60px">

        <table style="margin: auto;margin-left: -10px;position: absolute;top: 1000px;left: -150px;">
            <tr>

               
                    <td>
                        <a href="/exam/student/index.do"><div class="test-item my-exam active">我的考试</div></a>
                    </td>
               

            </tr>
        </table>
    </div>
</div>

<!-- 内容 -->


<div class="mytest_main" style="width: 770px;">
    <!--====================没有考试记录==========================-->
    <%--<imgsrc="/img/exercise/ZW_03.png">--%>


    <!--=======================有记录====================================-->

        <c:if test="${fn:length(dtos) == 0}">
            <div class="mytest_main_NO" >暂无考试记录</div>
        </c:if>


        <!--=========================未答题==========================-->
        <c:forEach items="${dtos}" var="studentExerciseInfo">




        <ul>
        <li>
            <div class="mytest_main_V" style="/*position:relative;left:50px;*/">
                <div class="mytest_main_V_LT">

                  
                    <div  class="mytest_main_V_LT_I" onclick="goresult(${studentExerciseInfo.id})">
                        ${studentExerciseInfo.name}
                         
                    </div>
                    
                    
                    
                  
                    
                  

                    <c:if test="${studentExerciseInfo.state == 0}">
                    <div  class="mytest_main_V_LT_II" style="position: relative;left: -210px;">
                        未答题
                    </div>
                    </c:if>

                    <c:if test="${studentExerciseInfo.state == 1}">
                        <div  class="mytest_main_V_LT_II">
                            等待批改
                        </div>
                    </c:if>

                    <c:if test="${studentExerciseInfo.state == 2}">
                        <div  class="mytest_main_Y_LT_II">
                            已答题
                        </div>
                          <c:if test="${studentExerciseInfo.isGoods==1}">
                    <div class="mytest_main_Y_LT_VVVV"><img  src="/img/ks-update.png"></div>
                    </c:if>
                    </c:if>
                  

                </div>
                
                
                
                 
                
                
                
                
                <div class="mytest_main_V_RT" style="width:180px;position: relative;left: -300px;">

                    <c:if test="${studentExerciseInfo.state == 0 && isParent==0}">

                        <div class="mytest_main_V_RT_I" onclick="dopractiseTest('${studentExerciseInfo.id}')">
                                                                                        去答题
                        </div>
                        </a>
                    </c:if>

                    <c:if test="${studentExerciseInfo.state == 1}">
                        <div class="mytest_main_D_RT_I" onclick="goresult('${studentExerciseInfo.id}')">
                            查看试卷
                        </div>
                    </c:if>

                    <c:if test="${studentExerciseInfo.state == 2}">
                        <div class="mytest_main_Y_RT_I" onclick="goresult('${studentExerciseInfo.id}')">
                            查看批改
                        </div>
                    </c:if>

                    <span class="mytest_main_V_RT_II">
                        ${studentExerciseInfo.time}
                    </span>
                    <span class="mytest_main_V_RT_III">
                          ${studentExerciseInfo.submitStudent}人提交
                    </span>
                </div>
            </div>
        </li>
    </ul>
        </c:forEach>

</div>
</div>
<c:if test="${fn:length(dtos) != 0}">
    <div id="foot-fenye" style="position: relative;top:10px;margin: 0 auto">
        <ul id="" >
            <c:if test="${page.currentPage > 5}">
            <li><a class="foot-fenye-F" href="/exam/student/index.do">首页</a></li>
            </c:if>
            <c:if test="${page.currentPage > 1}">
                <li ><a href="/exam/student/index.do?page=${page.currentPage-1}"><</a></li>
            </c:if>

            <c:forEach begin="${page.currentPage}" end="${page.tailPage}" var="pageIndex">
                <c:choose>
                    <c:when test="${page.currentPage == pageIndex}">
                        <li ><a style="color: #999999;background-color: #F5F5F5">${pageIndex}</a></li>
                    </c:when>
                    <c:otherwise>
                        <li ><a href="/exam/student/index.do?page=${pageIndex}">${pageIndex}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${page.currentPage < page.pageCount}">
                <li ><a href="/exam/student/index.do?page=${page.currentPage+1}">></a></li>
            </c:if>

            
            <li><a  class="foot-fenye-L" href="/exam/student/index.do?page=${page.pageCount}">尾页</a></li>
            
        </ul>
    </div>
</c:if>
<!--=============================分页================================-->

<div style="/*background-color: #ffffff;*/height: 20px; width: 100%"></div>
</div>
<!-- 页尾 -->
<%@ include file="../common_new/foot.jsp" %>
</body>
<script type="text/javascript">
    //查看结果
    function goresult(wordexerciseId){
    	 window.location.href="/exam/view.do?id="+wordexerciseId+"&type=2";;
    }
    //去答题
    function dopractiseTest(wordexerciseId){
        window.location.href="/exam/view.do?id="+wordexerciseId+"&type=1&tty=1";
    }

    $(function(){
        if(${type == 1}){
            alert("试题略有改动 ,请稍后答题!");
        }
    });
</script>
</html>
