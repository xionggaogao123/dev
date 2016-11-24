<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="com.jintizi.entity.UserInfo" %>
<script type="text/javascript" src="/js/WdatePicker.js"></script>
<script type="application/javascript">

    $(function(){
        $('.homepage-left-one').click(function(){

            if($(this).children(".hocu").prop('className').indexOf('fa-chevron-left') > 0){
                $(this).children(".hocu").addClass("fa-chevron-down").removeClass('fa-chevron-left');
                /*  $(this).css("background-color","#666666")
                 $(this).css("color","white")*/
                $(this).addClass("copy-one").removeClass("copy-two")

            }else{
                $(this).children(".hocu").addClass("fa-chevron-left").removeClass('fa-chevron-down');
                /*$(this).css("background-color","#eeeeee")
                 $(this).css("color","#666666")*/
                $(this).addClass("copy-two").removeClass("copy-one")

            }

        })
    })
    $(function(){
        $(".homepage-left-one").click(function(){
            $(this).next("div").toggle();
        });
    });



    $(function(){
        /*  $(window).scroll(function(){
         var top=$(window).scrollTop();
         if(top>=0){
         $(".homepage-upward").fadeIn();
         }else{
         $(".homepage-upward").fadeOut();
         }
         });*/
//点击返回头部效果
        $(".homepage-upward").click(function(){
            $("html,body").animate({scrollTop:0});
        });
         
         
         /*点击高亮代码*/

         var chatValue=jQuery("#chatValue").val();
         var showtypeValue=jQuery("#showtypeValue").val();
         var version=jQuery("#versionValue").val();
         if("1"==chatValue)
         {
        	 jQuery("#version9").css("backgroundColor","#FF8A00");
             jQuery("#version9").css("background-image","url('/img/JXHD.png')");
             jQuery("#version9").css("background-position","211px 5px");
             jQuery("#version9").css("background-repeat","no-repeat");
      	     jQuery("#version9").find("span").css("color","white");
      	     jQuery("#version9").find("i").css("color","white");

         }
         else if("1"==showtypeValue)
         {
        	 jQuery("#version7").css("backgroundColor","#FF8A00");
             jQuery("#version7").css("background-image","url('/img/JXHD.png')");
             jQuery("#version7").css("background-position","211px 5px");
             jQuery("#version7").css("background-repeat","no-repeat");
      	     jQuery("#version7").find("span").css("color","white");
      	     jQuery("#version7").find("i").css("color","white");
         }
         else if("2"==showtypeValue)
         {
        	 jQuery("#version8").css("backgroundColor","#FF8A00");
             jQuery("#version8").css("background-image","url('/img/JXHD.png')");
             jQuery("#version8").css("background-position","211px 5px");
             jQuery("#version8").css("background-repeat","no-repeat");
      	     jQuery("#version8").find("span").css("color","white");
      	     jQuery("#version8").find("i").css("color","white");
         }
         else
        	 {
			            if(jQuery.isNumeric(version))
			        	 {
			        	      if(version.length==2)
			        		   {
					        	   var v=version.substring(0,1);
					        	   jQuery("#version"+v).css("backgroundColor","#FF8A00");
                                   jQuery("#version"+v).css("background-image","url('/img/JXHD.png')");
                                   jQuery("#version"+v).css("background-position","211px 5px");
                                   jQuery("#version"+v).css("background-repeat","no-repeat");
					        	   jQuery("#version"+v).find("span").css("color","white");
					        	   jQuery("#version"+v).find("i").css("color","white");
					        	   jQuery("#version"+v + " + div.homepage-left-two").show();
					        	   jQuery("#version"+version).css("color","red");
			        		   }
			        	      else
			        	      {
			        	    	 // jQuery("#version"+version).css("color","red");
			        	    	  jQuery("#version"+version).css("backgroundColor","#FF8A00");
                                  jQuery("#version"+version).css("background-image","url('/img/JXHD.png')");
                                  jQuery("#version"+version).css("background-position","211px 5px");
                                  jQuery("#version"+version).css("background-repeat","no-repeat");
			        	      }
			        	 }else
			        		 {
			        		   jQuery(".homepage-left-family").css("backgroundColor","#FF8A00");
                                 jQuery(".homepage-left-family").css("background-image","url('/img/JXHD.png')");
                                 jQuery(".homepage-left-family").css("background-position","211px 5px");
                                 jQuery(".homepage-left-family").css("background-repeat","no-repeat");
			        		 }
        	 }
         
    });
</script>
<input type="hidden" id="versionValue" value="<%=request.getParameter("version") %>" />
<input type="hidden" id="chatValue" value="<%=request.getParameter("chat") %>" />
<input type="hidden" id="showtypeValue" value="<%=request.getParameter("showtype") %>" />


<link href="/css/homepage.css" type="text/css" rel="stylesheet">

<div class="homepage-left" style="z-index:1;overflow: visible">
<!--=============================左边悬浮====================================-->
<div class="homepage-momery" style="display:none;">
    <div class="homepage-momery-top" onclick="location.href = '/user?showtype=3'">
        <img src="/img/activity/homepage-training.png">
    </div>
    <div class="homepage-momery-top" onclick="location.href = '/business/reverse/user/userHelp.jsp'">
        <img src="/img/activity/homepage-user.png">
    </div>
    <div class="homepage-upward">
        <img src="/img/activity/homepage-arrow.png" title="回到顶部">
    </div>
</div>

<!--=========================上边部分===============================-->
<div class="homepage-left-head">

    <%

        UserInfo info =(UserInfo)session.getAttribute("currentUser");

    %>
    <div class="homepage-left-IM">
        <img src="<%= info.getMiddleImageURL() %>">
    </div>
    <div><%=info.getUserName() %></div>
    <div class="homepage-left-JY">

        <% if (((UserInfo)session.getAttribute("currentUser")).getRole()==1 ){%>




        <% } %>
        <span class="homepage-left-JYZ">经验值<%=info.getExperiencevalue()%></span>
    </div>
    <%

    %>

</div>

<!-- 
<a href="/user">
<div class="homepage-left-family">
    <i class="fa fa-home"></i>
    <span>家校互动</span>
</div>
</a>
 -->



<!--=================尖角====================-->
    <!--<span class="homepage-left-horn"></span>-->
<% if (((UserInfo)session.getAttribute("currentUser")).getRole()==2 || ((UserInfo)session.getAttribute("currentUser")).getRole()==8 || ((UserInfo)session.getAttribute("currentUser")).getRole()==1) { %>

<div class="homepage-left-navigation" style="overflow: visible">
    <ul style="height:2189px; background: #eeeeee;">
        <% if(((UserInfo)session.getAttribute("currentUser")).getRole() != 8 ) { %>
        <li id="version9" class="homepage-left-one copy-two" style="position: relative; border-bottom: none; background: #ff8a00; color:white;">
            <span>管理统计</span>

            <%--<img style="position: absolute;width: 15px;right: -5px;" src="/img/JXHD.png">--%>
            <%-- <i class="fa fa-chevron-down"></i>--%>
        </li>
    
        <div class="homepage-left-two" style="overflow: visible">
            <a href="/user?version=91&index=6"> <li id="version91">微校园</li></a>
            <a href="/user?version=92&index=7"><li id="version92">微家园</li></a>


            <% if("1".equals(session.getAttribute("headmaster")) ||   ((UserInfo)session.getAttribute("currentUser")).getRole()==1 ) { %>
               <a href="/user?version=93&index=8"><li id="version93">通知</li></a>

            <a href="/user?index=2&version=94"><li id="version94">作业</li></a>
            <% } %>


        </div>

    

        <!--=======================二级导航========================-->
        <div class="homepage-left-two" style="overflow: visible">
            <a href="/cloudClass?version=11"> <li id="version11">微课 @ 翻转课堂</li></a>
            <% if ("1".equals(session.getAttribute("headmaster")) ||   ((UserInfo)session.getAttribute("currentUser")).getRole()==1 ) { %>
            <a href=" ${(currentUser.role==0||currentUser.role==4)?'/exercise/onlineStudentTest.do?version=13':'/exercise/myExam4Teacher.do?version=13'}"> <li id="version13">考试</li></a>
            <% } %>
            <a href="/flippedExams?version=14"><li id="version14">题库</li></a>
        </div>






        <!--=======================二级导航========================-->
        <div class="homepage-left-two" style="overflow: visible">
            <a href="/excellentLesson?version=21"><li  id="version21">电子超市首页</li></a>
            <a href="/excellentLesson/accountOrder?version=22"><li  id="version22">个人账户</li></a>
        </div>

        <% if ("1".equals(session.getAttribute("headmaster")) ||   ((UserInfo)session.getAttribute("currentUser")).getRole()==1 ) { %>

        <% } %>
        <!--=======================二级导航========================-->

 	<% } %>
        <!--=======================二级导航========================-->


        <!--=======================二级导航========================-->
        <div class="homepage-left-two" style="overflow: visible">
        <% if (((UserInfo)session.getAttribute("currentUser")).getRole()==1 ){
         %>
          <a href="/myColleagues?version=51"> <li id="version51">我的同事</li></a>
         <%
          }
         if (((UserInfo)session.getAttribute("currentUser")).getRole()==2  ){
        	%>
        	  <a href="/headmaster?version=52&tag=1"><li id="version52">班级</li></a>
        	  <a href="/headmaster?version=53&tag=2"><li id="version53">老师</li></a>
        	  <a href="/headmaster?version=54&tag=3"><li id="version54">通知</li></a>
        	  
        	  
        	  <a href="/manager?version=55&tag=1"><li id="version55">管理学科</li></a>
        	  <a href="/manager?version=56&tag=2"><li id="version56">管理老师</li></a>
        	  <a href="/manager?version=57&tag=3"><li id="version57">管理校园</li></a>
        	  <a href="/manager?version=58&tag=4"><li id="version58">管理拓展课</li></a>
        	  <a href="/manager?version=59&tag=5"><li id="version59">管理新闻</li></a>
        	<%
         }
         if ((((UserInfo)session.getAttribute("currentUser")).getRole()==1 &&  ((UserInfo)session.getAttribute("currentUser")).getIsmanage()==1 ) || ((UserInfo)session.getAttribute("currentUser")).getRole()==8 ){
        	 
        	 %>
        	 
              <a href="/manager?version=55&tag=1"><li id="version55">管理学科</li></a>
        	  <a href="/manager?version=56&tag=2"><li id="version56">管理老师</li></a>
        	  <a href="/manager?version=57&tag=3"><li id="version57">管理校园</li></a>
        	  <a href="/manager?version=58&tag=4"><li id="version58">管理拓展课</li></a>
        	  <a href="/manager?version=59&tag=5"><li id="version59">管理新闻</li></a>
        	 
        	 <% 
         }
         
        %>
        </div>
        
        <% if(((UserInfo)session.getAttribute("currentUser")).getRole() != 8 ) { %>

      
        <% if("1".equals(session.getAttribute("headmaster"))  || ((UserInfo)session.getAttribute("currentUser")).getRole()==1) { 
        	
        	
        	%>

		        

        	
        	<%
        	
        }%>
          <% } %>
        
        <% if(((UserInfo)session.getAttribute("currentUser")).getRole() != 8 ) { %>

        <% } %>
        
        
    </ul>
</div>
<% } else if( ((UserInfo)session.getAttribute("currentUser")).getRole()==1  &&  (((UserInfo)session.getAttribute("currentUser")).getIsmanage()==1)) {%>





<% } else if( ((UserInfo)session.getAttribute("currentUser")).getRole()==8  ) {%>



<div class="homepage-left-navigation" style="overflow: visible">
    <ul>
       

        <li id="version9" class="homepage-left-one copy-two" style="border-top:1px solid #d0d0d0">
            <i class="fa fa-home"></i>
            <span>家校互动</span>
            <i class="fa fa-chevron-left hocu"></i>
            <%-- <i class="fa fa-chevron-down"></i>--%>
        </li>
    
        <div class="homepage-left-two" style="overflow: visible">
            <a href="/user?version=91&index=6"> <li id="version91">微校园</li></a>
            <a href="/user?version=92&index=7"><li id="version92">微家园</li></a>
            <a href="/user?version=93&index=8"><li id="version93">通知</li></a>
        </div>
        <a href="/chat">
            <li id="groupchat-li" class="homepage-left-one copy-two">
                <i class="fa fa-users"></i>

                <span>群组交流</span>

                <%--<i class="fa fa-chevron-left hocu"></i>--%>
            </li>
        </a>
        <li id="version7"  class="homepage-left-one copy-two">
            <i class="fa fa-bar-chart-o"></i>
            <span onclick="location.href = '/user?showtype=1'">投票选举</span>
            <%--<i class="fa fa-chevron-left hocu"></i>--%>
        </li>
        <li id="version8" class="homepage-left-one copy-two">
            <i class="fa fa-file"></i>
            <span onclick="location.href = '/user?showtype=2'">调查问卷</span>
            <%--<i class="fa fa-chevron-left hocu"></i>--%>
        </li>
        
          <li id="version10" class="homepage-left-one copy-two" style="overflow: visible;background-color:#EEEEEE;">
              <i class="fa fa-fire"></i>
              <a href="/business/fieryactivitylist.do"><span>火热活动</span></a>
        </li>
    </ul>
</div>

<% } else if( ((UserInfo)session.getAttribute("currentUser")).getRole()==10 ) {%>
	<ul>
	<li id="version9" class="homepage-left-one copy-two" style="border-top:1px solid #d0d0d0; background: #FF8A00 url('/img/JXHD.png') no-repeat 211px 5px;
color: white;">
            <i class="fa fa-home"></i>
            <span>管理统计</span>
            <%-- <i class="fa fa-chevron-down"></i>--%>
     </li>
	</ul>
<% }else{ %>


<div class="homepage-left-navigation" style="overflow: visible">
    <ul>
        <li id="version9" class="homepage-left-one copy-two" style="border-top:1px solid #d0d0d0">
            <i class="fa fa-home"></i>
            <span>家校互动</span>
            <i class="fa fa-chevron-left hocu"></i>
            <%-- <i class="fa fa-chevron-down"></i>--%>
        </li>
    
        <div class="homepage-left-two" style="overflow: visible">
            <a href="/user?version=91&index=6"> <li id="version91">微校园</li></a>
            <a href="/user?version=92&index=7"><li id="version92">微家园</li></a>
            <a href="/user?version=93&index=8"><li id="version93">通知</li></a>
            <a href="/user?index=2&version=94"><li id="version94">作业</li></a>
        </div>
        
       <li id="version1" class="homepage-left-one copy-two">
            <i class="fa fa-edit"></i>
            <span>学习中心</span>
            <i class="fa fa-chevron-left hocu"></i>
            <%-- <i class="fa fa-chevron-down"></i>--%>
        </li>
        <!--=======================二级导航========================-->
        <div class="homepage-left-two" style="overflow: visible">
            <a href="/cloudClass?version=11"> <li id="version11">微课 @ 翻转课堂</li></a>
            <a href=" ${(currentUser.role==0||currentUser.role==4)?'/exercise/onlineStudentTest.do?version=13':'/exercise/myExam4Teacher.do?version=13'}"> <li id="version13">考试</li></a>
            <a href="/flippedExams?version=14"><li id="version14">题库</li></a>
        </div>


         <li id="version2" class="homepage-left-one copy-two">
            <i class="fa fa-shopping-cart"></i>
            <span>电子超市</span>
            <i class="fa fa-chevron-left hocu"></i>
        </li>
        <!--=======================二级导航========================-->
        <div class="homepage-left-two" style="overflow: visible">
            <a href="/excellentLesson?version=21"><li  id="version21">电子超市首页</li></a>
            <a href="/excellentLesson/accountOrder?version=22"><li  id="version22">个人账户</li></a>
        </div>

        <% if( ((UserInfo)session.getAttribute("currentUser")).getRole()==0 || ((UserInfo)session.getAttribute("currentUser")).getRole()==4  ) {%>
        <!--=======================二级导航========================-->
        <a href="/student/class?version=31">
        <li id="version3"  class="homepage-left-one copy-two">
            <i class="fa fa-user"></i>
            <span>我的班级</span>
        </li>
        </a>
        <%} %>

        <% if( ((UserInfo)session.getAttribute("currentUser")).getRole()==1  ) {%>
        <li id="version3" class="homepage-left-one copy-two">
            <i class="fa fa-user"></i>
            <span><a href="/teacher/class?version=31">我的班级</a></span>
        </li>
        <%} %>



        <!--  -->
        <!--=======================二级导航========================-->
       <li id="version4" class="homepage-left-one copy-two" style="overflow: visible;background-color:#EEEEEE;">
           <i class="fa fa-smile-o"></i>
           <a href="/activity/activityMain?version=41"><span>好友圈</span></a>
        </li>

        
        
        <!--=======================二级导航========================-->
        <!--=======================二级导航========================-->
       
        <a href="/chat">	<li id="groupchat-li" class="homepage-left-one copy-two">
            <i class="fa fa-users"></i>
            <span>群组交流</span>
            <%--<i class="fa fa-chevron-left hocu"></i>--%>
        </li>
        </a>
        <li id="version7"  class="homepage-left-one copy-two">
            <i class="fa fa-bar-chart-o"></i>
            <span onclick="location.href = '/user?showtype=1'">投票选举</span>

        </li>

       <li id="version8" class="homepage-left-one copy-two">
           <i class="fa fa-file"></i>
            <span onclick="location.href = '/user?showtype=2'">调查问卷</span>
        </li>
        
         <li id="version10" class="homepage-left-one copy-two" style="overflow: visible;background-color:#EEEEEE;">
             <i class="fa fa-fire"></i>
             <a href="/business/fieryactivitylist.do"><span>火热活动</span></a>
        </li>

    </ul>
</div>
<%} %>





<!--============================同学排行=======================================-->
<div class="homepage-left-bottom" style="display:none">
    <div class="homepage-left-bottom-top">
        <span>同学排行</span>
    </div>
    <div class="homepage-left-bottom-text">
        <ul  id="version_student_sort">
        </ul>
    </div>
</div>
</div>