<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>复兰科技 K6KT-快乐课堂</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/itempool/teacher_information.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="/static/plugins/pagination/jqPaginator.min.js"></script>

    <script type="text/javascript">
    
       var exeObj={};
       var selectIts=new Array();
       
       $(document).ready(function(){
		   $(".wind-up em").click(function(){
			   hide();
		   })
    	   handPaper();
           $(".teacher_information_new_nav span").click(function(){
               $(this).addClass('cur-span').siblings('.teacher_information_new_nav span').removeClass('cur-span');
			   exeObj.type = $(this).attr("type");
			   exeObj.select = $("#subject").val();
			   getItems(1);
           })
       });
       
       
       function handPaper()
       {
//    	   exeObj.bookType=1;
//    	   exeObj.name="";
//    	   exeObj.time=1;
    	   exeObj.subject=jQuery("#subject").val();
    	   exeObj.grade=jQuery("#grade").val();
    	   exeObj.level=-1;
    	   exeObj.itemType=0;
//    	   exeObj.cltys="";
//    	   exeObj.bs="";
//    	   exeObj.bsType=-1;
		   exeObj.type = 1;
		   exeObj.select = $("#subject").val();
		   exeObj.isBook = false;
		   exeObj.regular = '';
		   exeObj.page = 1;
		   exeObj.pageSize = 10;
    	   
    	   var idstr=jQuery("#itemIds").val();
    	   if(idstr)
    	   {
    		   selectIts=idstr.split(",");
    	   }
    	   
    	   showKws();
    	   //得到题目
    	   getItems(1);
    	   
    	   jQuery("#handle_first").hide();
    	   
    	   jQuery("#handle_second").show();
       }
       
       
       function handleCondintion(t)
       {
    	   if(exeObj.condition!=t)
    	   {
    		   exeObj.condition=t;
    		   if(t==1)
    			{
    			   exeObj.bsType=-1;
					exeObj.isBook = false;
    			   showKws();
    			}
    		   else
    			{
    			   exeObj.cltys="";
					exeObj.isBook = true;
    			   chubanshe(exeObj.subject);
    			}
    			  
    	   }
       }
       
       
       function chubanshe(xueke)
       {
      	 $.ajax({
             url: '/testpaper/subject.do?type=3&xd='+xueke,
             type: 'get',
             contentType: 'application/json',
             success: function (res) {
               jQuery("#bk_select").empty();
               for(var i=0;i<res.length;i++)
            	   {
            	    var html='<option value="'+res[i].idStr+'">'+res[i].value+'</option>';
            	    jQuery("#bk_select").append(html);
            	   }
               
               showBookSel();
             }
         });
       }
       
       function showKws()
       {
    	   jQuery("#div_book").removeClass("teacher_topics_I");
    	   jQuery("#div_kw").addClass("teacher_topics_I");
    	   exeObj.condition=1;
    	   
    	   jQuery("#condition_span").text("请选择知识点");
    	   jQuery("#bk_select").hide();
    	   $.ajax({
               url: '/itempool/kw/list.do',
               type: 'get',
               data:{subject:exeObj.subject,grade:exeObj.grade},
               contentType: 'application/json',
               success: function (res) {
            	   bindData(res,1);
               }
           });
       }
   
       function showBookSel()
       {
    	   jQuery("#div_kw").removeClass("teacher_topics_I");
    	   jQuery("#div_book").addClass("teacher_topics_I");
    	   exeObj.condition=2;
    	   jQuery("#condition_span").text("请选择章节");
    	   jQuery("#bk_select").show();
    	   exeObj.bs="";
    	   exeObj.bsType=jQuery("#bk_select").val();
    	   $.ajax({
               url: '/itempool/bs/list.do?eType='+jQuery("#bk_select").val(),
               type: 'get',
               contentType: 'application/json',
               success: function (res) {
                  {
                	 bindData(res,2);
                  }
               }
           });
       }
   
   //ty 1知识点 2 教科书章节
   function bindData(res,ty)
   {
	   jQuery("#condition_li").empty();
	   var html="";
	   for(var i=0;i<res.length;i++)
       {
		   obj=res[i];
		   html+='<li id="li_'+obj.idStr+'">';
		   html+='<div>';
		   html+='<span class="teacher_information_right_info_first"></span>';
		   html+='<span class="teacher_information_right_info_th" onclick="select(\''+obj.idStr+'\')">'+obj.name+'</span>';
		   html+='</div>';
		   
		   
		   
		   for(var j=0;j<obj.nodes.length;j++)
			{
			   
			   var thisobj=obj.nodes[j];
			   
			   
			   var thishead=obj.nodes[j][0];
			   
			   
			   html +='<div class="teacjer_information_right_last_info">';
			    html +='<ul>';
			    html +=' <li>';
			    html +=' <div class="teacher_information_right_last_info">';
			    html +=' <span class="teacher_infomation_right_last"></span>';
			    html +=' <span class="teahcer_information_right_last_V" onclick="select(\''+thishead.idStr+'\')">'+thishead.value+'</span>';
			    html+='</div></li></ul></div>';
			   
			   
//			   if(type==2)
			    {
				    html+='<div class="teacjer_information_right_last_infoo">';
				    html+='<ul>';
				    for(var jj=1;jj<thisobj.length;jj++)
				    {
					       var jjobj=thisobj[jj];
						    html +=' <li>';
							html +=' <span class="teacher_infomation_right_last"></span>';

							if(ty==1)
								{
								html +=' <span class="teahcer_information_right_last_VT" onclick="select(\''+jjobj.idStr+'\')">'+jjobj.value+'</span>';
								}
							else
								{
								 html +=' <span class="teahcer_information_right_last_VT" onclick="select(\''+jjobj.idStr+'\')">'+jjobj.value+'</span>';
								}

						    html +=' </li>';

				    }
				    html+='</ul>';
			    }

			    html+='</div>';
		    }
		   
		   
		   
		   html+='</li>';
	   }
	   
	   jQuery("#condition_li").append(html);
   }
   
   
   function selectItemType(id,ty)
   {
	   var $obj= jQuery(".teacher_information_difficulty_II");
	   $obj.removeClass("teacher_information_difficulty_II");
	   $obj.addClass("teacher_information_difficulty_I");
	   jQuery("#"+id).addClass("teacher_information_difficulty_II");
	   
	   exeObj.itemType=ty;
	   getItems(1);
   }
   function selectLevel(id,ty)
   {
	   var $obj= jQuery(".teacher_information_difficulty_IIII");
	   $obj.removeClass("teacher_information_difficulty_IIII");
	   $obj.addClass("teacher_information_difficulty_III");
	   jQuery("#"+id).addClass("teacher_information_difficulty_IIII");
	   
	   exeObj.level=ty;
	   getItems(1);
   }
   function select(kw) {
	   exeObj.select = kw;
	   getItems(1);
   }
   function getItems(page)
   {
	   exeObj.page = page;
	   var isInit = true;
	   $.ajax({
		   url: '/itempool/getItems.do',
		   type: 'get',
		   data:exeObj,
           contentType: 'application/json',
           success: function (resp) {
              {
				  res = resp.dtoList;

				  $('.new-page-links').html("");
				  if(res.length > 0) {
					  //分页方法
					  $('.new-page-links').jqPaginator({
						  totalPages: Math.ceil(resp.count / exeObj.pageSize) == 0 ? 1 : Math.ceil(resp.count / exeObj.pageSize),//总页数
						  visiblePages: 10,//分多少页
						  currentPage: parseInt(page),//当前页数
						  first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
						  prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
						  next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
						  last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
						  page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
						  onPageChange: function (n) { //回调函数
							  if (isInit) {
								  isInit = false;
							  } else {
								  getItems(n);
							  }
						  }
					  });
				  }


            	 jQuery("#item_continer").empty();
            	 var html="";
            	 for(var i=0;i<res.length;i++)
            	 {
            		 var index= jQuery.inArray(res[i].id, selectIts);
            		 html="";
            		 if(index==-1)
            		 {
            		    html+='<div id="item_'+res[i].id+'" class="teacher_information_content">';
            		 }
            		 else
            		 {
            			 html+='<div id="item_'+res[i].id+'" class="teacher_information_content_I">';
            		 }
            		 html+='<div class="teacher_information_new_V">';
					 if($('.teacher_information_new_nav .cur-span').attr('type') == 4){
					 html+='<span class="span-teacher-infor">出现在'+res[i].totalCount+'个学生的错题本中<em onclick="showCount(\''+res[i].id+'\')">[查看]</em></span>';
					 }
            		 html+='<dl>';
            		 
            		 if(index==-1)
            		 {
            		     html+='<dd id="add_'+res[i].id+'" class="teacher_information_new_dd" onclick="addItem(\''+res[i].id+'\','+res[i].type+')">加入试卷</dd>';
            		     html+='<dd id="remove_'+res[i].id+'" class="teacher_information_new_dd_I" style="display:none;" onclick="removeItem(\''+res[i].id+'\','+res[i].type+')">删除本题</dd>';
            		 }else
            		 {
            			 html+='<dd id="add_'+res[i].id+'" class="teacher_information_new_dd" onclick="addItem(\''+res[i].id+'\','+res[i].type+')" style="display:none;">加入试卷</dd>';
                		 html+='<dd id="remove_'+res[i].id+'" class="teacher_information_new_dd_I" onclick="removeItem(\''+res[i].id+'\','+res[i].type+')">删除本题</dd>';
            		 }
            		 
            		 html+='<dd>难度：'+res[i].level+'</dd>';
            		 html+='<dd>题型：'+res[i].itemType+'</dd>';
            		 html+='<dd>知识点：'+res[i].kw+'</dd>';
            		 html+='<dd>分值：'+res[i].score+'</dd>';
            		 html+='</dl>';
            		 html+='</div>';
            		 html+='<div v class="teacher_information_new_VI">';
            		 html+='<span>'+res[i].item+'</span>';
            		 html+='</div>';
            		// html+='<div v class="teacher_information_new_VII">';
            		// html+='<span>A.a</span>B<span></span>C<span></span><span>D</span>';
            		// html+='</div>';
            		 html+='</div>';
            		 jQuery("#item_continer").append(html);
            	 }
              }
           }
       });
   }
   
   
   function showCount(id){
	   $.ajax({
		   url: '/itempool/getItemErrorCount.do',
		   data:{itemId:id},
		   type: 'get',
		   contentType: 'application/json',
		   success: function (res) {
			   $('#errorCount').empty();
			   var html = '';
			   for(var i in res){
				   html += '<tr>';
				   html += '<td>'+res[i].name+'</td>';
				   html += '<td>'+res[i].className+'</td>';
				   html += '<td>'+res[i].count+'</td>';
				   html += '</tr>';
			   }
			   $('#errorCount').append(html);
			   show();
		   }
	   });
   }

	   function hide(){
		   $(".bg").fadeOut();
		   $(".wind-up").fadeOut();
	   }

	   function show(){
		   $(".bg").fadeIn();
		   $(".wind-up").fadeIn();
	   }
   
   
   

   
   function addItem(ti,ty)
   {
	   var index= jQuery.inArray(ti, selectIts);
	   if(index==-1)
		{
		   $.ajax({
	              url: '/testpaper/item/add.do?pid='+jQuery("#paperId").val()+"&type="+ty+"&tid="+ti,
	              type: 'get',
	              contentType: 'application/json',
	              success: function (res) {
	                if(res.code=="200")
	             	{
	                	 selectIts.push(ti);
	          		    var $obj=jQuery("#item_"+ti);
	          		   $obj.removeClass("teacher_information_content");
	          		   $obj.addClass("teacher_information_content_I");
	          		   jQuery("#add_"+ti).hide();
	          		   jQuery("#remove_"+ti).show();
	          		   inCreaseSelectItem(1,ty);
	             	}
	              }
	          });
		}
   }
   
   
   function removeItem(ti,ty)
   {
	  var index= jQuery.inArray(ti, selectIts);  
	  if(index>-1)
	  {
		  
		  $.ajax({
              url: '/testpaper/item/delete.do?pid='+jQuery("#paperId").val()+"&type="+ty+"&tid="+ti,
              type: 'get',
              contentType: 'application/json',
              success: function (res) {
                if(res.code=="200")
             	{
                  selectIts.splice(index,1);
           		   var $obj=jQuery("#item_"+ti);
           		   $obj.removeClass("teacher_information_content_I");
           		   $obj.addClass("teacher_information_content");
           		   jQuery("#add_"+ti).show();
           		   jQuery("#remove_"+ti).hide();
           		   
           		   inCreaseSelectItem(-1,ty);
             	}
              }
          });
		 
	  }
   }
   
   
   

   function inCreaseSelectItem(inc,type)
   {
	   var ty=type;
	   var arr=  jQuery("#select_"+ty).find("span").toArray();
	   if(inc==1) //增加
	   {
		 var newCount=1;
		 if(arr)
		 {
			   newCount=arr.length+1;
			   jQuery("#select_"+ty).append("<span>"+newCount+"</span>");
		 }
	   }
	   else //减少
	   {
		   if(arr)
		   {
			   jQuery("#select_"+ty +" span:eq("+(arr.length-1)+")").remove();
		   }
	   }   
   }
   
   
   function goView()
   {
 	  document.location.href="/testpaper/view.do?pid="+jQuery("#paperId").val()+"&type=0";
   }
   
  </script>
    
</head>
<body style="background: #fff;">
<!-- 页头 -->
<%@ include file="../common_new/head.jsp"%>
<!-- 页头 -->

<div id="YCourse_player" class="player-container" >
    <div id="player_div" class="player-div"></div>
    <div id="sewise-div" style="display: none; width: 800px; height: 450px;max-width: 800px;">
        <script type="text/javascript" src="/static/plugins/sewiseplayer/sewise.player.min.js"></script>
    </div>
    <span onclick="closeCloudView()" class="player-close-btn"></span>
</div>

<script type="text/javascript">
    SewisePlayer.setup({
        server : "vod",
        type : "m3u8",
        skin : "vodFlowPlayer",
        logo : "none",
        lang: "zh_CN",
        topbardisplay : 'disabled',
        videourl: ""
    });
</script>
<div class="teacher_infromation_body">
<%@ include file="../common_new/col-left.jsp" %>
    
    <input id="paperId" name="paperId" type="hidden" value="${paperId}" />
    <input id="grade" name="grade" type="hidden" value="${grade}" />
    <input id="subject" name="subject" type="hidden" value="${subject}"/>
    <input id="itemIds" name="itemIds" type="hidden" value="${itemIds}"/>
    
    <div id="continer" class="teacher_information_right" >
    
        
        <!--=====================================手工组卷=========================================-->
        <div id="hand_paper">
            
            <!--======================手工组卷2========================-->
            <div id="handle_second" class="teacher_information_new_II" style="display:none;">
                <div class="teacher_information_new_I_top">
                    <span class="information_I" style="margin-left: 20px;">我的组卷</span>
                    <span class="information_I">/&nbsp;试卷结构</span>
                    <span class="information_II">/&nbsp;手工组卷</span>
                </div>
                <div class="teacher_information_new_nav">
                    <span class="cur-span" type="1">云题库</span>
                    <span type="2">教师个人题库</span>
                    <span type="3">校本题库</span>
                    <span type="4">学生错题本</span>
                </div>
                <div class="teacher_information_topics">
                    <div id="div_kw" class="teacher_topics_I" onclick="handleCondintion(1)">综合知识点选题</div>
                    <div id="div_book" onclick="handleCondintion(2)">同步教材选题</div>
                </div>

                <div class="teacher_information_difficulty">
                    <dl>
                        <dd>
                            <span>题型</span>
                            <span id="itemtype_6" class="teacher_information_difficulty_II" onclick="selectItemType('itemtype_6',0)">不限</span>
                            <span id="itemtype_1" class="teacher_information_difficulty_I" onclick="selectItemType('itemtype_1',1)">选择题</span>
                            <span id="itemtype_3" class="teacher_information_difficulty_I"  onclick="selectItemType('itemtype_3',3)">判断题</span>
                            <span id="itemtype_4" class="teacher_information_difficulty_I"  onclick="selectItemType('itemtype_4',4)">填空题</span>
                            <span id="itemtype_5" class="teacher_information_difficulty_I" onclick="selectItemType('itemtype_5',5)">主观题</span>
                        </dd>
                        <dd>
                            <span>难度</span>
							<span id="level_6" class="teacher_information_difficulty_IIII" onclick="selectLevel('level_6',-1)">不限</span>
                            <span id="level_1" class="teacher_information_difficulty_III" onclick="selectLevel('level_1',1)">容易</span>
                            <span id="level_2" class="teacher_information_difficulty_III" onclick="selectLevel('level_2',2)">较容易</span>
                            <span id="level_3" class="teacher_information_difficulty_III" onclick="selectLevel('level_3',3)">中等</span>
                            <span id="level_4" class="teacher_information_difficulty_III" onclick="selectLevel('level_4',4)">较难</span>
                            <span id="level_5" class="teacher_information_difficulty_III" onclick="selectLevel('level_5',5)">难</span>
                        </dd>
                    </dl>
                </div>
                <div id="item_continer">

	              
	              
                </div>
				<div class="new-page-links"></div>
             
                <div class="teacher_information_right_TV">
                    <div class="teacher_information_right_V_top">
                        <span id="condition_span">选择知识点</span>
                    </div>
                    <div class="teacher_information_right_V_info">
                         <select id="bk_select" onchange="showBookSel()">
                            <option value="1">人民教育出版社</option>
                            <option value="2">上海科学技术出版社</option>
                            <option value="3">上海教育出版社</option>
                            <option value="4">江苏科学技术出版社</option>
                            <option value="5">译林出版社</option>
                            <option value="6">山东教育出版社</option>
                            <option value="7">中考真题</option>
                        </select>
                        <ul id="condition_li">
                           
                        </ul>
                    </div>

                    <div class="teacher_information_right_VV">
                        <div class="teacher_information_right_V_top">
                            <span>题目卡</span>
                        </div>
                        <div class="teacher_information_right_V_info">
                            <span>1.选择题</span>
                            
                            <div id="select_1" class="teacher_information_right_VV_info">
                                <c:forEach items="${ch}" var="item" begin="0"  varStatus="status">
		                         <span>${status.index+1 }</span>
		                       </c:forEach>
                            </div>
                            <span>2.判断题</span>
                            <div id="select_3"  class="teacher_information_right_VV_info">
                                 <c:forEach items="${tf}" var="item" begin="0"  varStatus="status">
		                         <span>${status.index+1 }</span>
		                       </c:forEach>
                            </div>
                             <span>3.填空题</span>
                            <div id="select_4" class="teacher_information_right_VV_info">
                                <c:forEach items="${gap}" var="item" begin="0"  varStatus="status">
		                         <span>${status.index+1 }</span>
		                       </c:forEach>
                            </div>
                             <span>4.主观题</span>
                            <div id="select_5" class="teacher_information_right_VV_info">
                                <c:forEach items="${sub}" var="item" begin="0"  varStatus="status">
		                         <span>${status.index+1 }</span>
		                       </c:forEach>
                            </div>
                            <button class="teacher_information_right_VV_bottom" onclick="goView()">确定生成试卷</button>
                        </div>
                    </div>
                </div>

				<!--弹窗-->
				<div class="wind-up">
					<div class="tc-top">错题本名单 <em>×</em></div>
					<div class="winde-up2">
						<table>
							<thead>
							<tr>
								<th>学生名单</th>
								<th>班级</th>
								<th>错误次数</th>
							</tr>
							</thead>
							<tbody id="errorCount">
							<%--<tr>--%>
								<%--<td>1</td>--%>
								<%--<td>1</td>--%>
								<%--<td>1</td>--%>
							<%--</tr>--%>
							</tbody>
						</table>
					</div>
				</div>
				<!--弹窗-->

				<!--半透明背景-->
				<div class="bg"></div>
				<!--/半透明背景-->
            </div>
        </div>
    </div>
</div>
            
<!-- 页尾 -->
<%@ include file="../common_new/foot.jsp"%>
</body>
</html>
