<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<c:set var="fullPage" value="${empty param.page}"/>
<c:if test="${fullPage}">
    <c:if test="${not empty total}">
        <script>
            $('.course-right-container').data('total', ${total});
            //@ sourceURL=foo.js
        </script>
    </c:if>
    <c:if test="${not empty parentId}">
        <script>
            _currentDir = ${parentId};
        </script>
    </c:if>
    <div class="right-container-title">
        <div style="padding:2px 10px;line-height:30px;height:48px;">

            <c:choose>
                <c:when test="${not empty classSubjects}">
                    <div style="float:left;width:182px;">
                        <div class="course-title">班级目录</div>
                        <div class="course-include">${fn:length(classSubjects)}个文件夹，0个课程</div>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:set var="lessonsCount" value="${not empty total && total > 0?total:fn:length(lessons)}"/>
                    <div style="float:left;width:182px;">
                        <div class="course-title ellipsis" title="${parentDir.name}">${parentDir.name}</div>
                        <div class="course-include">
                            <c:if test="${not hideDirectoryIndicator}">${fn:length(dirs)}个文件夹，</c:if>${lessonsCount}个课程
                        </div>
                    </div>
                    <div style="float:left;margin-left:-30px;padding-top:5px;padding-bottom:5px;overflow:hidden;">
                        <ul id="icons" style="display:inline;">
                            <c:if test="${writable}">
                                <c:if test="${parentId>0}">
                                    <li class="fa fa-plus fa-lg" style="cursor:pointer" title="新建课程"
                                        onclick="newLesson(${parentId})">新建课程
                                    </li>
                                </c:if>
                            </c:if>
                        </ul>
                    </div>

                    <input class="search-file-input" type='text' onkeypress="searchKeyPress(event.keyCode, this.value)"
                           style="margin-top:8px;">
                </c:otherwise>
            </c:choose>
            <c:if test="${writable}">

            </c:if>
        </div>
    </div>
    <div class="course-list-container">
</c:if>
<c:choose>
    <c:when test="${not empty classSubjects}">
        <c:forEach items="${classSubjects}" var="classSubject">
            <div class="course-container dir virtual"
                 type="COURSE" file-id="${classSubject.id}"
                 title="${classSubject.classInfo.classname}${classSubject.subject.name}">
                <img class="folder-img" src="/img/K6KT/document.png" onclick="navClassSubject(${dir.id})">

                <div class="folder-name ellipsis">${classSubject.classInfo.classname}${classSubject.subject.name}</div>

            </div>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <%--<c:forEach items="${dirs}" var="dir">
            <div class="course-container dir real" file-id="${dir.id}" title="${dir.name}">
                <c:if test="${writable}">
                    <i class="fa fa-times fa-2 file-op" onclick="deleteDir(${dir.id})"></i>
                </c:if>
                <img class="folder-img" src="/img/K6KT/document.png" dirId="${dir.id}" onclick="nav(${dir.id})">

                <div class="folder-name ellipsis">${dir.name}</div>
            </div>
        </c:forEach>--%>
        <c:forEach items="${lessons}" var="lesson">
            <div style="overflow: visible;" class="course-container lesson" file-id="${lesson.id}"
                 title="${lesson.courseName}" isFromCloud="${lesson.isFromCloud}">
                    <%-- <c:choose>
                                <c:when test="${writable||param.op}">
                                    <i class="fa fa-bars fa-2 file-op"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="fa fa-play fa-2 file-op" onclick="play(${lesson.id})"></i>
                                </c:otherwise>
                            </c:choose> --%>
                <div class="file-cover" onclick="play(${lesson.id})">
                    <img class="file-img"
                         src="<c:choose><c:when test="${not empty lesson.imageUrl}">${lesson.imageUrl}</c:when><c:otherwise>/img/K6KT/filecourse.png</c:otherwise></c:choose>">

                    <div class="file-info">
                        <div class="hover-title">
                            <span class="ellipsis" style="max-width: 10em;">${lesson.courseName}</span>
                        </div>
                        <div class="files">
                            <div>
                                <!-- <img src="/img/K6KT/video.png"> -->
                                <span>视频:${lesson.videoCount}</span>
                            </div>
                            <div>
                                <!-- <img src="/img/K6KT/files.png"> -->
                                <span>文档:${lesson.docCount}</span>
                            </div>
                            <div>
                                <!-- <img src="/img/K6KT/lianxi.png"> -->
                                <span>习题:${lesson.questionCount}</span>
                            </div>
                        </div>
                        <div class="play">
                            <img src="/img/play_icon.png"/>
                        </div>
                    </div>
                </div>

                <div class="file-title ellipsis">${lesson.courseName}</div>
                <ul class="file-toolbar" style="text-align: center;">
                    <c:if test="${writable}">
                        <c:choose>
                            <c:when test="${dirType=='TEACHER'}">
                                <a href="#" class="dropdown-toggle btn btn-primary btn-use"
                                   data-toggle="dropdown">推送<b class="caret"></b></a>
                                <a class="btn btn-primary btn-use"
                                   onclick="editLessonByType(getLessonId(this),1)">编辑</a>
                                <a class="btn btn-default btn-try"
                                   onclick="deleteLesson(getLessonId(this))">删除</a>
                                <ul class="dropdown-menu" style="padding: 0;">
                                    <li><a href="#"
                                           onclick="showCoursePushingDialog(getLessonId(this))">${isVote?"参加评比" : "推送到班级课程"}</a>
                                    </li>
                                    <li><a href="#" onclick="showSchoolPushingDialog(getLessonId(this))">推送到校本资源</a>
                                    </li>
                                    <li><a href="#" onclick="showLeaguePushingDialog(getLessonId(this))">推送到联盟资源</a>
                                    </li>
                                    <c:if test="${lesson.isFromCloud == true}">
                                        <li><a href="#" onclick="alert('该课程来自云课程,不可以推送到电子超市哦!');"
                                               style="color: #aaa;cursor: not-allowed;">推送到电子超市</a></li>
                                    </c:if>
                                    <c:if test="${lesson.isFromCloud == false}">
                                        <c:choose>
                                            <c:when test="${lesson.videoCount gt 0 || lesson.docCount gt 0 || lesson.questionCount gt 0}">
                                                <li><a href="#"
                                                       onclick="showsaleCoursePushingDialog(getLessonId(this),this)">推送到电子超市</a>
                                                </li>
                                            </c:when>
                                            <c:otherwise>
                                                <li><a href="#" onclick="alert('课程至少要包含一个附件(视频、文档或者习题),才可以推送到电子超市哦!')"
                                                       style="color: #aaa;cursor: not-allowed;">推送到电子超市</a></li>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </ul>
                            </c:when>
                            <c:when test="${dirType=='COURSE'}">
                                <a href="#" class=" btn btn-primary btn-use"
                                   onclick="editLessonByType(getLessonId(this),2)">编辑</a>
                                <a class="btn btn-primary btn-use" onclick="stat(getLessonId(this))">统计</a>
                                <a class="btn btn-default btn-try"
                                   onclick="deleteLesson(getLessonId(this))">删除</a>
                            </c:when>
                            <c:when test="${dirType=='VOTE'}">
                                <a href="#" class=" btn btn-primary btn-use"
                                   onclick="editLesson(getLessonId(this))">编辑</a>
                                <a class="btn btn-default btn-try"
                                   onclick="deleteLesson(getLessonId(this))">删除</a>
                            </c:when>
                            <c:when test="${dirType=='EXCELLENT'}">
                                <a href="#" class=" btn btn-primary btn-use"
                                   onclick="editExcellentLesson(getLessonId(this))">编辑</a>
                                <a class="btn btn-default btn-try"
                                   onclick="deleteExcellentLesson(getLessonId(this))">删除</a>
                            </c:when>
                        </c:choose>
                    </c:if>
                    <c:choose>
                        <c:when test="${dirType=='LEAGUE'}">

                            <a href="#" class=" btn btn-primary btn-use"
                               onclick="showTeacherPushingDialog(getLessonId(this))">推送</a>


                            <a
                                    class="btn btn-default btn-try" onclick="deleteLesson(getLessonId(this))">删除</a>

                        </c:when>
                        <c:when test="${dirType=='SCHOOL'}">
                            <a href="#" class=" btn btn-primary btn-use"
                               onclick="showTeacherPushingDialog(getLessonId(this))">推送</a>

                            <a
                                    class="btn btn-default btn-try" onclick="deleteLesson(getLessonId(this))">删除</a>
                            <ul class="dropdown-menu" style="padding: 0;">
                                <li></li>
                            </ul>
                        </c:when>
                    </c:choose>
                </ul>
            </div>
        </c:forEach>
    </c:otherwise>
</c:choose>
<c:if test="${fullPage}">
    </div>
</c:if>