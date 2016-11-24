<%--
  Created by IntelliJ IDEA.
  User: Niu Xin
  Date: 14-10-15
  Time: 16:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>期末总评-复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/static/plugins/uploadify/uploadify.css">
    <link rel="stylesheet" href="/static/css/k6kt.css"/>
    <link rel="stylesheet" href="/static/css/dialog.css"/>
    <link rel="stylesheet" href="/static/css/evaluation/term-end.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/static/js/card.js"></script>
    <script type="text/javascript" src="/static/js/evaluation/term-end.js"></script>

    <!-- Add fancyBox main JS and CSS files -->
    <script type="text/javascript" src="/static/plugins/fancyBox/jquery.fancybox.js?v=2.1.5"></script>
    <link rel="stylesheet" type="text/css" href="/static/plugins/fancyBox/jquery.fancybox.css?v=2.1.5" media="screen"/>
    <script type="text/javascript" src="/static/plugins/jquery-mousewheel/jquery.mousewheel.min.js"></script>

    <script type="text/javascript">
        var currentPageID = 2;
        var classId = '${param.classId}';
        var type = '${param.type}';
        var termType = '${param.termType}';
    </script>
    <style>
        html,body{font: 12px/18px "lucida grande", "lucida sans unicode", 'Microsoft YaHei', tahoma, verdana, arial, sans-serif !important;}
    </style>
</head>
<body>


<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container">
    <div id="content_main">

        <%@ include file="../common_new/col-left.jsp" %>


        <div id="right-container" style="width: 770px;position: relative;">
            <!-- 内容 -->
            <div class="k6kt-body" >

                <div class="term-end-table-container">
                    <table class="table-k6kt term-end-table">
                        <tr>
                            <th style="width: 88px;">学生姓名</th>
                            <th colspan="2">成果展示(上传一张图片)</th>
                            <th style="width: 186px;">老师评语 <span class="small-font">(250字以内)</span></th>
                            <th>考勤</th>
                            <th style="width:100px;">日常评价</th>
                            <th style="width:100px;">总体评价</th>
                            <th>打印成绩单</th>
                        </tr>

                        <c:forEach items="${stuList}" var="student" varStatus="loop">
                            <tr stu-id="${student.id}">
                                <td>
                                    <img src="${student.imgUrl}" class="stu-list-img avatar-min"
                                         target-id="${student.id}"/>
                                        ${student.userName}
                                </td>

                                <%--<c:choose>--%>
                                    <%--<c:when test="${tranScriptMap[student.id] != null}">--%>
                                        <c:set var="transcript" value="${tranScriptMap[student.id]}"></c:set>
                                        <td class="image-statement-td">
                                            <c:if test="${transcript.resultspicsrc != null}">
                                                <a class="fancybox" href="${transcript.resultspicsrc}"
                                                   data-fancybox-group="gallery" title="${student.userName}">
                                                    <img src="${transcript.resultspicsrc}" class="statement-image"/>
                                                </a>
                                            </c:if>
                                        </td>
                                        <td class="uploader-td">
                                            <label for="upload-statement-${loop.index}" class="upload-label">
                                                <span class="upload-button">上传</span>
                                            </label>
                                            <i class="fa fa-spinner fa-spin"></i>

                                            <div class="input-file-container">
                                                <input type="file" class="upload-statement"
                                                       id="upload-statement-${loop.index}" name="Filedata"
                                                       accept="image/*"/>
                                            </div>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${transcript.teachercomments != null && transcript.teachercomments != ''}">
                                                    <textarea class="statement-textarea readonly" readonly
                                                              maxlength="500">${transcript.teachercomments}</textarea>
                                                    <span><button class="button-k6kt"
                                                                  onclick="statementButtonAction(this)" state="change">
                                                        修改
                                                    </button></span>
                                                </c:when>
                                                <c:otherwise>
                                                    <textarea class="statement-textarea" maxlength="500"></textarea>
                                                    <span><button class="button-k6kt"
                                                                  onclick="statementButtonAction(this)" state="save">保存
                                                    </button></span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="start-NUM">
                                            ${transcript.attendance}<c:if test="${transcript.attendance == null}">0/0</c:if>
                                        </td>
                                        <td class="stars-container orange-color" id="usualscore" style="width: 50px"
                                            stars="${transcript.semesterscore != null? transcript.semesterscore: 0}">
                                            <i class="fa fa-star-o fa-lg orange"></i>
                                            <i class="fa fa-star-o fa-lg orange"></i>
                                            <i class="fa fa-star-o fa-lg orange"></i>
                                            <i class="fa fa-star-o fa-lg orange"></i>
                                            <i class="fa fa-star-o fa-lg orange"></i>
                                            <span></span>
                                        </td>
                                        <td class="stars-container orange-color" id="finalscore" style="width:70px"
                                            stars="${transcript.finalresult != null? transcript.finalresult: 0}">
                                            <i class="fa fa-star-o fa-lg orange"></i>
                                            <i class="fa fa-star-o fa-lg orange"></i>
                                            <i class="fa fa-star-o fa-lg orange"></i>
                                            <i class="fa fa-star-o fa-lg orange"></i>
                                            <i class="fa fa-star-o fa-lg orange"></i>
                                            <span></span>
                                        </td>
                                        <td>
                                            <a href="/myclass/printTranscript.do?classId=${param.classId}&userId=${student.id}&termType=${param.termType}"
                                               target="_blank"><i class="fa fa-print fa-2x orange-color"></i></a>
                                        </td>
                                    <%--</c:when>--%>
                                    <%--<c:otherwise>--%>
                                        <%--<td class="image-statement-td"></td>--%>
                                        <%--<td class="uploader-td">--%>
                                            <%--<label for="upload-statement-${loop.index}" class="upload-label">--%>
                                                <%--<span class="upload-button">上传</span>--%>
                                            <%--</label>--%>
                                            <%--<i class="fa fa-spinner fa-spin"></i>--%>

                                            <%--<div class="input-file-container">--%>
                                                <%--<input type="file" class="upload-statement"--%>
                                                       <%--id="upload-statement-${loop.index}" name="Filedata"--%>
                                                       <%--accept="image/*"/>--%>
                                            <%--</div>--%>
                                        <%--</td>--%>
                                        <%--<td><textarea class="statement-textarea" maxlength="500"></textarea><span><button--%>
                                                <%--class="button-k6kt" onclick="statementButtonAction(this)" state="save">--%>
                                            <%--保存--%>
                                        <%--</button></span></td>--%>
                                        <%--<td>暂无</td>--%>
                                        <%--<td class="stars-container orange-color" stars="0">--%>
                                            <%--<i class="fa fa-star-o fa-lg orange"></i>--%>
                                            <%--<i class="fa fa-star-o fa-lg orange"></i>--%>
                                            <%--<i class="fa fa-star-o fa-lg orange"></i>--%>
                                            <%--<i class="fa fa-star-o fa-lg orange"></i>--%>
                                            <%--<i class="fa fa-star-o fa-lg orange"></i>--%>
                                        <%--</td>--%>
                                        <%--<td>--%>
                                            <%--<a href="/myclass/printTranscript.do?classId=${param.classId}&userId=${student.id}"--%>
                                               <%--target="_blank"><i class="fa fa-print fa-2x orange-color"></i></a>--%>
                                        <%--</td>--%>
                                    <%--</c:otherwise>--%>
                                <%--</c:choose>--%>
                            </tr>
                        </c:forEach>
                    </table>
                </div>

                <a href="/myclass/tointerestclass?version=16"><span
                        class="inline-button btn-orange fixed-size">返回</span></a>
                <span class="inline-button btn-orange fixed-size pull-right" onclick="saveMultiStatements()">保存</span>
            </div>


        </div>

    </div>
    <div>
    </div>
    <!-- 页尾 -->
    <%@ include file="../common_new/foot.jsp" %>
</div>


</body>
</html>
