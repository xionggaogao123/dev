<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<% 
  String path = request.getContextPath();    
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
    pageContext.setAttribute("basePath",basePath); 
    %>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技-区域联考</title>
    <meta name="description" content="">
    <meta name="author" content="" />
    <meta name="copyright" content="" />
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <link rel="shortcut icon" href="" type="image/x-icon" />
    <link rel="icon" href="" type="image/x-icon" />
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="<%=basePath%>static_new/css/reset.css" rel="stylesheet" />
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="<%=basePath%>static_new/css/growRecord.css" rel="stylesheet" />
     <link href="<%=basePath%>static_new/css/education.css" rel="stylesheet" />
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
</head>
<body>


<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

<!--.col-left-->
<%@ include file="../common_new/col-left.jsp" %>
<!--/.col-left-->

<!--.col-right-->
<div class="col-right">

    <!--.banner-info-->

    <!--/.banner-info-->


        <div class="grow-tab-head clearfix contactExam">
            <ul class="clearfix">
                
                <li class="cur"><a href="#cjdId" class="curNone">区域联考</a></li>
            </ul>


        </div>    

    <div class="grow-col">





        <div>
            <!-- 成绩单-->
            <div class="cjd" id="cjdId">
                <div class="clearfix">
                <div class="grow-select aa">
                    <select id="select">
                    </select>
                
                 </div>
                 <a class="newExam" href="/regional/newRegional.do" target="blank">新建联考</a>
            </div>
                <table class="gray-table exam-table" width="100%">
                    <thead class="th">
                        <th >考试名称</th>
                        <th width="80">年级</th>
                        <th >考试时间</th>
                        <th >上报状态</th>
                        <th >成绩统计分析</th>
                        <th >操作</th>
                    </thead>
                    
                    <tbody id="coursewareList">
                    </tbody>   
                         
                </table>

                
            </div>
                    
                
                

           
        </div>
    </div>

</div>
<!--/.col-right-->

<!-- 分页div -->
<div class="new-page-links"></div>

</div>
<!--/#content-->

 
<!--#foot-->



<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- 查看状态上报状态 -->
<div class="pop-wrap cenDIv" id="status">
    <div class="pop-title upStrus">
    <span>成绩上报状态</span>
    <span class="closePop"></span>
    </div>
   <div class="statusTitle"></div>
    <table>
        <thead>
            <th>学校名称</th>
            <th>状态</th>
        </thead>
        <tbody id="courseware">
            
        </tbody>
    </table>
</div>


<div  class="pop-wrap cenDIv" id="delConExam">
    <div class="pop-title">删除确认</div>
    <div class="tip">确定删除吗？</div> 
    <div class="pop-btn"><span class="active deleteArea">确定</span><span>取消</span></div>
</div>



<div class="pop-wrap cenDIv" id="delConExamm">
    <div class="pop-title">删除确认</div>
    <div class="tip">已有学校提交,还要删除？</div> 
    <div class="pop-btn"><span class="active deleteArea">确定</span><span>取消</span></div>
</div>



<div class="bg-dialog"></div>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/jquery.min.js"></script>
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('educationAreaExam');
  
</script>
</body>
</html>
