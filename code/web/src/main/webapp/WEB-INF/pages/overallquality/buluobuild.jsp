<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2016/8/11
  Time: 9:41
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>杨小综合素质评价</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link rel="stylesheet" type="text/css" href="/static_new/css/overallquality/yangxiao.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
</head>
<body>


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div class="bg-wrap">
    <div class="yx-con">
        <div class="yx-head clearfix">
            <ul>
                <li id="XXYH"><a href="/qualityitem/imageBank.do">形象银行</a></li>
                <li id="XXCS"><a href="/qualityitem/imageMarket.do" >形象超市</a></li>
                <li id="WMBL" class="m-active"><a href="javascript:;">文明部落</a></li>
            </ul>
        </div>
        <div class="tab-main">
            <div class="blgl-main">
                <!--================================文明部落start========================================-->
                <div class="wmbl-con" id="tab-MDBL">
                    <!--============================部落建设start===============================-->
                    <div class="wmbl-main">
                        <div class="clearfix">
                            <div class="score-left">
                                <span>${gradeName}</span>
                                <span>${className}</span>
                            </div>
                        </div>
                        <div class="xxcs-title clearfix">
                            <ul class="yx-tab clearfix">
                                <li class="yx-active"><a href="javascript:;">部落建设</a></li>
                            </ul>
                            <input type="hidden" id="gradeId" value="${dto.gradeId}">
                            <input type="hidden" id="classId" value="${dto.classId}">
                        </div>
                        <div class="wmbl-score clearfix">
                            <div class="score-left">
                                <span>我的部落现有：</span>
                                <span>行规${hgScore}分；</span>
                                <span>卫生${wsScore}分；</span>
                                <span>体锻${tdScore}分；</span>
                            </div>
                            <div class="score-right">
                                <span>大本营数量：<em>${dto.baseCampCount}</em>个</span>
                                <span>城堡数量：<em>${dto.castleCount}</em>个</span>
                                <span>村民数量：<em>${dto.villagerCount}</em>个</span>
                                <span>士兵数量：<em>${dto.soldiersCount}</em>个</span>
                            </div>
                        </div>
                    </div>
                    <!--============================积分兑换end===============================-->
                    <div class="score-ex clearfix">
                        <div class="xxcs-title clearfix xx-market">
                            <ul class="yx-tab clearfix">
                                <li class="yx-active"><a href="javascript:;">积分兑换</a></li>
                            </ul>
                            <!--<a href="javascript:;" class="dh-history">文明部落</a>-->
                        </div>
                        <div class="score-left">
                            <span>我的部落现有：</span>
                            <span>行规${hgScore}分；</span>
                            <span>卫生${wsScore}分；</span>
                            <span>体锻${tdScore}分；</span>
                        </div>
                        <ul class="ex-list clearfix">
                            <li>
                                <div class="ex-img">
                                    <img src="/static_new/images/overallquality/goods-3.jpg">
                                </div>
                                <dl>
                                    <dt>城堡</dt>
                                    <dd>8分卫生分</dd>
                                    <c:if test="${isTea}">
                                        <c:if test="${ws}">
                                            <dd type="ws" class="yx-dh"><span>兑换</span></dd>
                                        </c:if>
                                        <c:if test="${!ws}">
                                            <dd class="yx-nodh"><span>兑换</span></dd>
                                        </c:if>
                                    </c:if>
                                </dl>
                            </li>
                            <li>
                                <div class="ex-img">
                                    <img src="/static_new/images/overallquality/goods-1.jpg">
                                </div>
                                <dl>
                                    <dt>村民</dt>
                                    <dd>5分行规分</dd>
                                    <c:if test="${isTea}">
                                        <c:if test="${hg}">
                                            <dd type="hg" class="yx-dh"><span>兑换</span></dd>
                                        </c:if>
                                        <c:if test="${!hg}">
                                            <dd class="yx-nodh"><span>兑换</span></dd>
                                        </c:if>
                                    </c:if>
                                </dl>
                            </li>
                            <li>
                                <div class="ex-img">
                                    <img src="/static_new/images/overallquality/goods-2.jpg">
                                </div>
                                <dl>
                                    <dt>士兵</dt>
                                    <dd>4分体锻分</dd>
                                    <c:if test="${isTea}">
                                        <c:if test="${td}">
                                            <dd type="td" class="yx-dh"><span>兑换</span></dd>
                                        </c:if>
                                        <c:if test="${!td}">
                                            <dd class="yx-nodh"><span>兑换</span></dd>
                                        </c:if>
                                    </c:if>
                                </dl>
                            </li>
                        </ul>
                    </div>
                    <!--============================积分兑换end===============================-->
                    <!--============================项目兑换start===============================-->
                    <div class="project-ex clearfix">
                        <div class="xxcs-title clearfix">
                            <ul class="yx-tab clearfix">
                                <li class="yx-active"><a href="javascript:;">项目兑换</a></li>
                            </ul>
                            <!--<a href="javascript:;" class="dh-history">文明部落</a>-->
                        </div>
                        <div class="score-left">
                            <span>我的部落现有：</span>
                            <span>大本营<em>${dto.baseCampCount}</em>个；</span>
                            <span>城堡<em>${dto.castleCount}</em>个；</span>
                            <span>村民<em>${dto.villagerCount}</em>个；</span>
                            <span>士兵<em>${dto.soldiersCount}</em>个；</span>
                        </div>
                        <ul class="ex-list clearfix">
                            <li>
                                <div class="ex-img">
                                    <img src="/static_new/images/overallquality/goods-3.jpg">
                                </div>
                                <dl>
                                    <dt>城堡</dt>
                                    <dd>3村民兑换1城堡</dd>
                                    <c:if test="${isTea}">
                                        <c:if test="${dto.villagerCount>=3}">
                                            <dd type="3t1" class="yx-dh"><span>兑换</span></dd>
                                        </c:if>
                                        <c:if test="${dto.villagerCount<3}">
                                            <dd class="yx-nodh"><span>兑换</span></dd>
                                        </c:if>
                                    </c:if>
                                </dl>
                            </li>
                            <li>
                                <div class="ex-img">
                                    <img src="/static_new/images/overallquality/goods-1.jpg">
                                </div>
                                <dl>
                                    <dt>村民</dt>
                                    <dd>2士兵兑换3村民</dd>
                                    <c:if test="${isTea}">
                                        <c:if test="${dto.soldiersCount>=2}">
                                            <dd type="2t3" class="yx-dh"><span>兑换</span></dd>
                                        </c:if>
                                        <c:if test="${dto.soldiersCount<2}">
                                            <dd class="yx-nodh"><span>兑换</span></dd>
                                        </c:if>
                                    </c:if>
                                </dl>
                            </li>
                            <li>
                                <div class="ex-img">
                                    <img src="/static_new/images/overallquality/goods-2.jpg">
                                </div>
                                <dl>
                                    <dt>士兵</dt>
                                    <dd>1城堡兑换2士兵</dd>
                                    <c:if test="${isTea}">
                                        <c:if test="${dto.castleCount>=1}">
                                            <dd type="1t2" class="yx-dh"><span>兑换</span></dd>
                                        </c:if>
                                        <c:if test="${dto.castleCount<1}">
                                            <dd class="yx-nodh"><span>兑换</span></dd>
                                        </c:if>
                                    </c:if>
                                </dl>
                            </li>
                        </ul>
                    </div>
                    <!--============================项目兑换end===============================-->
                    <!--============================部落建设end===============================-->
                </div>
                <!--================================文明部落end========================================-->
            </div>
            <!--================================校长部落管理end========================================-->
        </div>
        <!--================================文明部落end========================================-->
    </div>
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!--/#content-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('buluobuild',function(buluobuild){
        buluobuild.init();
    });
</script>

</body>
</html>
