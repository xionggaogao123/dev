<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<script>
    _currentDir = '${param.classId}';
</script>
<div class="right-container-title"  style="float: none">
    <div style="padding:2px 10px;line-height:30px;">
        <div style="float:left;width:182px;">
            <div class="course-title">${classInfo.className}</div>
            <div class="course-include">${fn:length(classSubjects)}个科目</div>
        </div>
        <input class="search-file-input" type='text' onkeypress="searchKeyPress(event.keyCode, this.value)" style="margin-top:8px;">
    </div>
</div>
<div class="course-list-container">
    <c:forEach items="${classSubjects}" var="classSubject">
        <div class="course-container dir virtual"
             type="classSubject" file-id="${classSubject.subjectInfo.id}" title="${classSubject.subjectInfo.value}">
            <img class="folder-img" src="/img/K6KT/document.png" onclick="navClassSubject(${classSubject.subjectInfo.id})">

            <div class="folder-name ellipsis">${classSubject.subjectInfo.value}</div>
        </div>
    </c:forEach>
</div>