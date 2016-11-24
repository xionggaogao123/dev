<%@page import="org.apache.commons.lang.StringUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@page import="com.pojo.app.SessionValue" %>
<%@page import="com.pojo.user.UserRole" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE  >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no">
    <link   href="/static_new/css/homeschool/blogshare.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src='/static/js/jquery.min.js'></script>
    <%--<script type="text/javascript">--%>
    <%--var cssEl = document.createElement('style');--%>
    <%--document.documentElement.firstElementChild.appendChild(cssEl);--%>

    <%--function setPxPerRem(){--%>
    <%--var dpr = 1;--%>
    <%--//把viewport分成10份的rem，html标签的font-size设置为1rem的大小;--%>
    <%--var pxPerRem = document.documentElement.clientWidth * dpr / 10;--%>
    <%--cssEl.innerHTML = 'html{font-size:' + pxPerRem + 'px!important;}';--%>
    <%--}--%>
    <%--setPxPerRem();--%>
    <%--</script>--%>
    <title>K6KT快乐课堂</title>
</head>
<body>
<div id='wx_logo' style='margin:0 auto;display:none;'>
    <img src='${data.filenameAry[0]}' />
</div>
<div data-role="page" class="page">
    <%--<div data-role="header">--%>
    <%--<div class="wei-top">--%>
    <%--<table>--%>
    <%--<tr>--%>
    <%--<td class="wei-f">--%>
    <%--<a data-icon="arrow-l">返回</a>--%>
    <%--</td>--%>
    <%--<td class="wei-t">K6KT快乐课堂</td>--%>
    <%--<td class="wei-k"><img src="/img/moblie/wei-ed.jpg" alt=""></td>--%>
    <%--</tr>--%>
    <%--</table>--%>
    <%--</div>--%>
    <%--</div>--%>
    <div data-role="content">
        <div class="wei-x">
            <div class="wei-L">
                <img class="lis" src="/img/moblie/wei-X.png">
                <div class="wei-L-i">
                    <span>K6KT快乐课堂</span>
                    <span class="wei-s">客户端看帖更快更省流量</span>
                </div>
            </div>
            <div class="wei-R"><a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.fulaan.fippedclassroom"> 下载  </a></div>
        </div>

        <div class="wei-p">
            <div class="wei-top-p">
                <%--<div class="icon-p">--%>
                <%----%>
                <%--</div>--%>
                <div class="wei-p-i">
                    <img class="wei-p-lis" src="${data.userimage}">
                    <div class="wei-LL">
                        <span>${data.username}</span>
                        <span class="wei-p-s">${publictime}</span>
                    </div>
                </div>
            </div>
            <p>${data.blogcontent}</p>

            <c:forEach items="${data.filenameAry}" var="imgpath"  >
                <img class="wei-M" src=${imgpath}>
            </c:forEach>
        </div>
        <div class="wei-q">
            <span>评论</span>
        </div>
        <div class="wei-v">
            <ul id="commentlist">
                <script>
                    var id='${data.id}';
                    var userid='${userid}';
                    var jqCommentlist = $.getJSON('/homeschool/moblie/friendReplyInfo.do', {
                        blogid:id,
                        userId:userid,
                        page: 1,
                        pageSize:2
                    }).done(function (data) {
                                console.log('成功, 收到的数据: ' + JSON.stringify(data));

                                for(var i=0;i<data.rows.length;i++){

                                    $("#commentlist").append("<li><img class=\"wei-v-lis\" src="+data.rows[i].userimage+">"+
                                            "  <div class=\"wei-v-i\">"+
                                            "<span class=\"wei-v-tx\">"+data.rows[i].username+"</span><label class=\"wei-v-L\">"+data.rows[i].timedes+"</label>"+
                                            " <span class=\"wei-v-s\">"+data.rows[i].blogcontent+"</span>"+
                                            "</div> </li>");
                                }
                            }).fail(function (xhr, status) {
                                console.log('失败: ' + xhr.status + ', 原因: ' + status);
                            }).always(function () {
                                console.log('请求完成: 无论成功或失败都会调用');
                            });
                </script>
            </ul>
        </div>
        <div class="wei-bottom">
            <div class="wei-b-top">
                <a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.fulaan.fippedclassroom"> 打开APP客户端，查看全部评论</a>
                <%-- <a href="/mobile"> <img src="/img/moblie/wei-P.png" alt=""></a>--%>
            </div>
            <div class="wei-b-J">
                <img class="wei-b-a" src="/img/moblie/wei-F.png" alt="">
                <p>上海复兰信息科技有限公司，专注于信息化技术，教育互联网科技产品及多媒体互动技术的设计、开发和运营是互联网在线教育平台科技产品的创新者和领导者。</p>
                <a href="http://www.fulaan-tech.com/application.html"><img class="wei-b-v" src="/img/moblie/wei-j-r.png" alt="" ></a>
            </div>
            <div class="wie-guanzhu">
                <span></span><i>关注</i><span></span>
            </div>
            <div class="wei-weixin">
                <img src="/img/moblie/wei-weix.png">
                <img src="/img/moblie/wei-weis.png" alt="">
            </div>
            <div class="wei-D">
                打造融学校，社区，学生和家长于一体的健康教育生态圈
            </div>
            <div class="wei-bottom-a">
                <img src="/img/moblie/wei-zheng.png" alt="">
                <span><a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.fulaan.fippedclassroom"> 点击下载App  </a></span>
            </div>
            <div class="wei-K-bottom"></div>
        </div>
    </div>
</div>
</body>
</html>