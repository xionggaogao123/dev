<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 <meta charset="utf-8" />
 <meta http-equiv = "X-UA-Compatible" content="IE=edge" />
<title>积分历史</title>
    <link rel="stylesheet" href="/static/css/bootstrap-combined.school.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/flippedStatistics.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/common/info-banner.css">
    <link rel="stylesheet" type="text/css" href="/static_new/css/reset.css">
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script src="/static/plugins/bootstrap/js/bootstrap-paginator.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="/static/js/pet/petstore.js"></script>
<script type="text/javascript">
		var petpage = false;
        var studentid="";
        function getStudentScores(page,size)
        {
        	$('#fc_student_score_history >ul').addClass('hardloadingb');
        	$('#fc_student_score_history >ul').html('');
        	$.ajax({
                url:"/experience/getStuScoreInfo.do",
    	  		type:"post",
    	  		dataType:"json",
    	  		async:true,
    	  		data:{
    	  			   'userid':studentid,
                       page:page,
                       pageSize:size
    	  		     },
    	  		success:function(data){
    	  			showStudentScores(data);
                    var total = data.totalElements;
    	   			var to=Math.ceil(total/size);
    	   			totalPages = to==0?1:to;
    	   			if(page==1)
    				{
    				  resetPaginator(totalPages);
    				}
    	  		},
    	  		error:function(e){
    	  		},
    	  		complete:function(){
    	  			$('#fc_student_score_history >ul').removeClass('hardloadingb');
    	  		}
    	  	});
        }
        
        function resetPaginator(totalPages)
        {
       	 $('#example').bootstrapPaginator("setOptions",{
    	            currentPage: 1,
    	            totalPages: totalPages,
    	            itemTexts: function (type, page, current) {  
    	                switch (type) {  
    	                    case "first":  
    	                        return "首页";  
    	                    case "prev":  
    	                        return "<";  
    	                    case "next":  
    	                        return ">";  
    	                    case "last":  
    	                        return "末页"+page;  
    	                    case "page":  
    	                        return  page;  
    	                }                 
    	            },
    	            onPageClicked: function(e,originalEvent,type,page){
    	            	getStudentScores(page,20)
    	            }
    	    });
        }
        
        function showStudentScores(data)
        {
        	var target=$('#fc_student_score_history >ul');
        	var html="";
            $.each(data.content,function(i,item){
                var jishu=item.experience>=0?'+'+item.experience:item.experience;
                html+='<li class="content"><span class="I">'+jishu+'</span>';
                html+='<span class="II">'+item.experiencename+'</span>';
                html += '<span class="III">' + (item.createtimeStr ? item.createtimeStr : '') + '</span></li>';
            });
          target.html(html);
        }
        
        $(function(){
        	studentid=$.trim($("body").attr("stuid"));
        	getStudentScores(1,20);
        	  $('#example').bootstrapPaginator({
                  currentPage: 1,
                  totalPages: 1,
                  itemTexts: function (type, page, current) {  
  	                switch (type) {  
  	                    case "first":  
  	                        return "首页";  
  	                    case "prev":  
  	                        return "<";  
  	                    case "next":  
  	                        return ">";  
  	                    case "last":  
  	                        return "末页"+page;  
  	                    case "page":  
  	                        return  page;  
  	                }                 
  	            },
                  onPageClicked: function(e,originalEvent,type,page){
                  }
              }); 
        });
</script>
<!--  teacher -->

<style stype="text/css">
		#student_info>* {float: left;}
		#student_img {width: 120px;height: 120px;margin: 36px 30px;border: 5px solid white;border-radius:5px;}
		#student_info div.info>p:first-child {margin-left: 10px;font-size: 20px;}
		#student_info div.info>p:first-child  a {top: 5px;position: relative;margin-left: 10px;}
		#student_info div.info>p:first-child+p {margin-top: 10px;}
		#student_info div.info {margin-top: 20px;color: black;}
		#student_info div.popu>div {display: inline-block;text-align: center;margin: 0 32px;float: left;overflow: hidden;}
		#student_info>div span {margin: 0 10px;}
        .ypheader >div:first-child{padding:10px 5px;border:1px solid #D0D0D0;}
         
        .fc_statistics_list span.I{width:200px;padding-left:10px;}
		.fc_statistics_list span.II{width:400px;}
		.fc_statistics_list span.III{width:250px;}
	.pet-tab {
	    width: 900px;
	    margin: 0 auto;
	    overflow: visible;
    }
    .pet-tab-common {
	    width: 80px;
	    border-right: 1px solid #DDDDDD;
	    color: #363636;
	    background: #F1F1F1;
	    background-repeat: no-repeat;
	    height: 38px;
	    line-height: 38px;
	    font: 16px/38px 'microsoft yahei';
	    padding-left: 17px;
    }
    .active{
	    background: white;
		border-top: 4px solid #FF8900;
		margin-top: -4px;
		border-right: 1px solid #DDDDDD;
		color: #FF8900;
    }
		
</style>
</head>
<body style="font-family:Microsoft YaHei;" stuid="${studentId}">
    <%@ include file="../common_new/head.jsp" %>
    <jsp:include page="../common/infoBanner.jsp">
        <jsp:param name="bannerType" value="petPage"/>
        <jsp:param name="menuIndex" value="2"/>
	</jsp:include>
	<div style="margin-top: 5px;"></div>
	<div class="pet-tab">
	<a href="/petbag" class="title_a"><span class="pet-tab-common">宠物背包</span></a>
	<a href="/petCenter" class="title_a"><span class="pet-tab-common">宠物中心</span></a>
	<a href="/studentScoreList" class="title_a"><span class="pet-tab-common active">历史积分</span></a>
	<a href="/experience/experienceRule.do" class="title_a"><span class="pet-tab-common" style="width: 130px;">经验值获取规则</span></a>
	</div>
        <input id="currentUserId" type="hidden" value="${studentId}">
     	<div id="fc_student_score_history" class="fc_statistics_list ypheader center">
        <div>
		    <h4>积分历史</h4>
        </div>
        <div class="header">
		    <span class="I">积分基数</span>|
		    <span class="II">积分状况</span>|
		    <span class="III">积分时间</span>
		</div>
        <!-- <div class="header_foot"></div> -->
        <ul style="min-height:500px;margin:0;">
            <!--  <li class="content">
                <span class="I"><img sr="">陈奕迅</span>
                <span class="II">2014/15/18:00</span>
                <span class="III">02:20''</span>
            </li>
            <li class="content">
                <span class="I"><img sr="">陈奕迅</span>
                <span class="II">2014/15/18:00</span>
                <span class="III">02:20''</span>
            </li>
            <li class="content">
                <span class="I"><img sr="">陈奕迅</span>
                <span class="II">2014/15/18:00</span>
                <span class="III">02:20''</span>
            </li> -->
        </ul>
        <div style="margin-top: 30px; margin-bottom: 30px; text-align:center">
			<div id="example"></div>
	    </div>
	</div>
    <%@ include file="../common_new/foot.jsp" %>
</body>
</html>