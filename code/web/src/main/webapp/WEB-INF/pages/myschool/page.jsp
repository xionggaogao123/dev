<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- 提供 pageCount 和 page参数--%>
<c:set var="url" value=""></c:set>
<style>
    #foot-fenye > ul > li {
        display: inline;
    }

    #foot-fenye > ul > li > a:hover {
        color: red
    }

    #foot-fenye > ul > li:first-child > a {
        border-top-left-radius: 4px;
        border-bottom-left-radius: 4px;
        border-left-width: 1px;
    }

    #foot-fenye > ul > li:last-child > a {
        border-top-right-radius: 4px;
        border-bottom-right-radius: 4px;
        border-right: 1px solid #dddddd;
    }

    #foot-fenye ul li a {
        color: #4c4c4c;
        float: left;
        padding: 4px 12px;
        line-height: 20px;
        text-decoration: none;
        background-color: #ffffff;
        border-top: 1px solid #dddddd;
        border-left: 1px solid #dddddd;
        cursor: pointer;
        border-bottom: 1px solid #dddddd;
    }
</style>
<div id="foot-fenye">
    <ul id="">
        <c:choose>
            <c:when test="${pageCount<5}">
                <c:choose>
                    <c:when test="${pageCount==0}"></c:when>
                    <c:otherwise>
                        <c:forEach begin="1" end="${pageCount}" var="k">
                            <c:choose>
                                <c:when test="${page==k}">
                                    <li><a
                                            style="color: #999999; background-color: #F5F5F5"
                                            href="${url}?page=${k}">${k}</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a
                                            href="${url}?page=${k}">${k}</a></li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <c:if test="${page!=1}">
                    <li><a
                            href="${url}?page=${k}">首页</a></li>
                </c:if>
                <c:set value="${pageCount - page}" var="tt"></c:set>
                <c:choose>
                    <c:when test="${page<=3}">
                        <c:set var="s" value="${page+3}"></c:set>
                        <c:forEach var="k" begin="1" end="${s}">
                            <c:choose>
                                <c:when test="${page==k}">
                                    <li><a
                                            style="color: #999999; background-color: #F5F5F5"
                                            href="${url}?page=${k}">${k}</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a
                                            href="${url}?page=${k}">${k}</a></li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <li><a>&gt;</a></li>
                    </c:when>
                    <c:when test="${tt<=3}">
                        <c:set var="s" value="${page-3}"></c:set>
                        <li><a>&lt;</a></li>
                        <c:forEach var="k" begin="1" end="${s}">
                            <c:choose>
                                <c:when test="${page==k}">
                                    <li><a
                                            style="color: #999999; background-color: #F5F5F5"
                                            href="${url}?page=${k}">${k}</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a
                                            href="${url}?page=${k}">${k}</a></li>
                                </c:otherwise>
                            </c:choose>

                        </c:forEach>

                    </c:when>
                    <c:otherwise>
                        <c:set var="s" value="${page-2}"></c:set>
                        <c:set var="t" value="${page+3}"></c:set>
                        <li><a>&lt;</a></li>
                        <c:forEach var="k" begin="${s}" end="${t}">
                            <c:choose>
                                <c:when test="${page==k}">
                                    <li><a
                                            style="color: #999999; background-color: #F5F5F5"
                                            href="${url}?page=${k}">${k}</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a
                                            href="${url}?page=${k}">${k}</a></li>
                                </c:otherwise>
                            </c:choose>

                        </c:forEach>
                        <li><a>&gt;</a></li>
                    </c:otherwise>
                </c:choose>
                <c:if test="${page !=pageCount}">
                    <li><a
                            href="${url}?page=${pageCount}">末页${pageCount}</a></li>
                </c:if>
            </c:otherwise>
        </c:choose>
    </ul>
</div>