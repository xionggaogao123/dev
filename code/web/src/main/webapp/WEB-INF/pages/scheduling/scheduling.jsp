<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <title>复兰科技-排课软件</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/css/scheduling/scheduling.css?v=2015072702">
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet" />
    <link rel="stylesheet" href="/static/css/homepage.css">
    <link href="/static_new/js/modules/core/0.1.0/fancyBox/jquery.fancybox.css?v=2015041602" rel="stylesheet" type="text/css" media="screen"/>
    <link href="/static/css/homepage.css" type="text/css" rel="stylesheet">
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/schoolsecurity.css?v=2015041602" rel="stylesheet" />
     <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
    
</head>
<body>
<!--================================start头部=========================================-->
<%@ include file="../common_new/head.jsp" %>
<!--================================end头部=========================================-->
<div class="scheduling-main">

    <!--================================start导航=========================================-->
    
      <%@ include file="../common_new/col-left.jsp" %>
    <!--================================end导航=========================================-->
    <!--====================================start-scheduling-right===============================================-->

    <div class="scheduling-right">
        <dl>
            <dd class="scheduling-right-T">
                <div>
                    <p class="scheduling-right-I">
                        <i style="font-size: 34px;font-weight: normal">最好的排课软件！</i><br>
                        <em>完美地导入Excel数据,真正生成Excel课程表，先进的排课算法，功能强大的手动排课，完美解决令人头疼的学校排课问题。</em>
                    </p>
                    <a href="/myschool/paike.do">
                    <img src="/images/scheduling-xx.png">
                    </a>
                </div>
            </dd>
            <dd>
                <p>
                    <i>【简介一】拖动就是调课：</i>
                    首创的拖动式调课，方便快捷。
                </p>
            </dd>
            <dd>
                <div>
                     <img src="/images/scheduling-TY.png">
                </div>
            </dd>
            <dd>
                <p>
                    <i>【简介二】活如游鱼般自定义条件和预设：</i>
                    有强大无比的排课算法和预设功能，可以设置多大十几种的预设限制，结合强大的排课算法，适合各类学校的排课需求，满足任何苛刻的排课要求。经过多年的研究，最新版本的排课质量更是遥遥领先于同类软件。
                </p>
                <div>
                    <img src="/images/scheduling-middle.png">
                </div>
            </dd>
            <dd>
                <p>
                    <i>【简介三】Excel导入导出自由自在：</i>
                    完美支持EXCEL的导入，导出，完美地生成EXCEL课程表，方便二次编辑。
                </p>
                <div>
                    <img  src="/images/scheduling-DR.png">
                    <img  src="/images/scheduling-DC.png">
                </div>
            </dd>
            <dd>
                <p>
                    <i>【简介四】兼容日常教务工作：</i>
                    完全按照教务工作者的思路设计，完全与日常教务工作兼容。如：设置科目时，根本就是在填一张课程设置表。
                </p>
                <div>
                    <img class="scheduling-CP" src="/images/scheduling-7_03.png">
                </div>
            </dd>
            <dd>
                <p>
                    <i>【简介五】操作易如反掌：</i>
                    每个界面都尽量切合学者的思路，都有独立详细的帮助说明，且包含全程视频演示，令操作易如反掌。
                </p>
            </dd>
        </dl>
    </div>
    <!--====================================end-scheduling-right===============================================-->
</div>
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('common',function(common){

    });
</script>
</body>
</html>
