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
    <c:choose>
        <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
            <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
        </c:otherwise>
    </c:choose>
    <!--/.banner-info-->


        <div class="grow-tab-head clearfix contactExam">
            <ul class="clearfix">
                <li ><a href="#szeduId">成绩单</a></li>
                <li class="cur"><a href="#cjdId">区域联考</a></li>
            </ul>


        </div>    

    <div class="grow-col">





        <div>
            <!-- 成绩单-->
            <div class="cjd " id="cjdId">
                <div class="clearfix">
                <div class="grow-select aa">
                    <label>排序</label>
                    <select>
                        <option></option>
                    </select>

                </div>
                <a class="newExam" href="#">导出</a>
                </div>
                
           <table class="gray-table exam-table" width="100%">
                       <thead class="th">
                        <th >校名次</th>
                        <th width="80">区域名次</th>
                        <th >姓名</th>
                        <th width="80">班级</th>
                        <th >总分</th>
                        <th >数学</th>
                        <th >语文</th>
                        <th >外语</th>
                        <th >物理</th>
                        <th >化学</th>
                        <th >生物</th>
                        <th >历史</th>
                        <th >地理</th>
                        <th >政治</th>
                       </thead>
                       <tbody>
                        <tr>
                            <td>560</td>
                            <td>70</td>
                            <td>李丽</td>
                            <td>七六班</td>
                            <td>560</td>
                            <td>70</td>
                            <td>80</td>
                            <td>90</td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                      
                       </tbody>        
                    </table>
                
            </div>
                     
                    
                    
                
            <div class="hide szedu" id="szeduId">


            </div>
           
        </div>
    </div>

</div>
<!--/.col-right-->

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- 查看状态上报状态 -->
<div class="pop-wrap" id="status">
    <div class="pop-title">成绩上报状态</div>
    <div class="statusTitle">2015年初三第一次联考学校列表</div>
    
    <table>
        <thead>
            <th>学校名称</th>
            <th>状态</th>
        </thead>
        <tbody>
            <tr>
                <td width="250">嘻嘻嘻学校</td>
                <td width="150">未提交</td>                
            </tr>
            <tr>
                <td width="250">翰林学校</td>
                <td width="150">已提交</td>                
            </tr>
            <tr>
                <td width="250">希望学校</td>
                <td width="150">未提交</td>                
            </tr>            
        </tbody>
    </table>
    
    <div class="pop-btn"><span class="active">确定</span><span>取消</span></div>
</div>


<div class="bg-dialog"></div>


<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="<%=basePath%>static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="<%=basePath%>static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('roll');
</script>
</body>
</html>