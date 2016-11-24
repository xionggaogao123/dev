<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <title>复兰教育论坛</title>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/newmain.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    <script type='text/javascript' src='/static/js/newmain.js'></script>
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/ypxxhead.jsp" %>
<div class="company-container">
    <div class="error-container">
        <div class="login-bar"></div>
        <div class='title-bar-container' style="height:105px;">
            <a href="/">
                <img class="title-logo" src="/static/images/K6KT-LOGO.png">
            </a>
            <ul class="title-ul">
                <li>
                    <a href="/">
                        <div class="title-warp"></div>
                        <div class="warp-content">首页</div>
                    </a>
                </li>
                <li class="active">
                    <a href="/aboutus/k6kt">
                        <div class="title-warp"></div>
                        <div class="warp-content">关于我们</div>
                    </a>
                </li>
                <li>
                    <a href="/contactus/k6kt">
                        <div class="title-warp"></div>
                        <div class="warp-content">联系我们</div>
                    </a>
                </li>
                <li>
                    <a href="/service/k6kt">
                        <div class="title-warp"></div>
                        <div class="warp-content">文明守则</div>
                    </a>
                </li>
                <li>
                    <a href="/privacy/k6kt">
                        <div class="title-warp"></div>
                        <div class="warp-content">隐私保护承诺</div>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <div class="company-info-container">
        <div class="info-left">
            <div class="company-info-title">关于我们</div>
            <div class="company-info-content">
                复兰青少年素质教育论坛，为全国青少年群体量身打造，集兴趣交流、学习社交、素质拓展为一体，是中国最专业的青少年素质教育社区。原创精选，涵盖青少年才艺展示与交流、STEM创客学习与体验、学习交流、演讲口才、安全健康等多个维度内容，是青少年分享与探索的专业平台，发展自我、挖掘潜能的教育社区空间。“复兰杯”青少年才艺挑战赛是复兰教育论坛开放给青少年全面展示自我的舞台，包含丰富的竞赛活动和各种有趣的奖品，以鼓励青少年全面发展。
                <br>复兰教育商城，上海复兰科技重磅首创的国内教育垂直领域STEM创客商城，结合国际先进的STEM创客教育理念，精选书籍绘本、安全健康、学习文具、益智玩具等各类教育教学创意新品，致力为全国中小学学生打造专业的教育应用商城，创建独特的创造与学习的体验社区。
                <br>上海复兰信息科技有限公司，专注于信息化技术，教育互联网科技产品及多媒体互动技术的设计、开发和运营是互联网在线教育平台科技产品的创新者和领导者。
                <br>复兰科技旗下的产品包括教育云平台、翻转课堂智慧校园、互动课堂电子书包、未来学校·人机交互。其中在线教育云平台旨在突破传统教育的时空限制实现优秀教师资源共享。提供给全国各地的师生及最高科技的在线教育平台和最具创造性的学习方式。
                <br>复兰科技由复旦杰出校友创立，目前大部分员工具有互联网及管理等领域的中美名校硕士以上学历·创新·自由的企业文化吸引了大批互联网高精尖人才的加盟。高科技的产品和客户至上的服务理念让我们从众多同类企业中脱颖而出，并获得客户的广泛赞誉。目前公司的科技产品现已广泛应用于万余所名校及著名展馆。
                <div style="line-height:25px;">
                    <div style="margin-top:30px;">地址：上海市大渡河路168弄北岸长风E栋12楼</div>
                    <div>电话：021-80125668</div>
                    <div>传真：021-8012 5668*8018</div>
                    <div>复兰科技官网：www.fulaan-tech.com</div>
                    <div>复兰“K6KT-快乐课堂”：www.k6kt.com</div>
                    <div>复兰教育社区：www.fulaan.com</div>
                    <div>邮箱：info@fulaan.com</div>
                </div>
            </div>
        </div>
        <div class="info-right">
            <a href="/aboutus/k6kt" class="company-info-title active">关于我们</a>
            <a href="/contactus/k6kt" class="company-info-title">联系我们</a>
            <a href="/service/k6kt" class="company-info-title">文明守则</a>
            <a href="/privacy/k6kt" class="company-info-title">隐私保护承诺</a>
        </div>
    </div>
</div>
<!-- 页尾 -->
<%@ include file="/WEB-INF/jsp/common/flippedroot.jsp" %>
<!-- 页尾 -->
</body>
</html>