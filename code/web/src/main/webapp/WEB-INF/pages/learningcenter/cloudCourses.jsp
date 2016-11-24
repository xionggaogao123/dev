<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: Tony
  Date: 2014/8/15
  Time: 16:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<c:forEach items="${returnData.list}" var="course">
  <div class="col-xs-4 subject-container">
    <div class="subject-img-container">
      <img class="subject-img" src="${course.imageUrl}"></div>
    <div class="context">${course.name}</div>
    <a class="btn-default btn-try"  video-length="${course.size}" onclick="tryPlayYCourse($(this));" vurl="${course.path}" vid="${course.id}">试看</a>
    <c:if test="${couldPush}">
    
      <a class="btn-use push-f" >推送
          <ul class="push-select">
              <li onclick="currentCourseId='${course.id}';openPushDialog();">推送至备课空间成为新课程</li>
              <li class="push-lesson" onclick="currentCourseId='${course.id}';openPushDialog1();">推送至备课空间成为课程课件</li>
          </ul>
      </a>
      
    </c:if>
  </div>
</c:forEach>
<c:if test="${page==1}">
  <script>
    resetPaginator(<fmt:formatNumber value="${returnData.count/limit+0.5}" type="number" pattern="#"/>);
  </script>
</c:if>
