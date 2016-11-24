<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>组卷</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/itempool/teacher_information.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="/static/plugins/pagination/jqPaginator.min.js"></script>

    <script type="text/javascript">
       var kwArr=[];
       var exeObj={};
       exeObj.level=-1;
       var selKw=[];//
       var selectIts=new Array();
       var obj;//选择的知识点集合

	   $(document).ready(function(){

		   $(".wind-up em").click(function(){
			   hide();
		   })

		   $(".teacher_information_new_nav span").click(function(){
			   $(this).addClass('cur-span').siblings('.teacher_information_new_nav span').removeClass('cur-span');
			   exeObj.type = $(this).attr("type");
			   exeObj.select = $("#subject_sel").val();
			   getItems(1);
		   })
	   });

       function deletePaper(id)
       {
    	 if(confirm("确定要删除数据吗？"))
    	{
	      	 $.ajax({
	             url: '/testpaper/delete.do?id='+id,
	             type: 'get',
	             contentType: 'application/json',
	             success: function (res) {
	               if(res.code=="200")
	            		{
	            	      jQuery("#li_"+id).remove();
	            	    }else
	            	    {
	            	    	alert(res.message);
	            	    }
	             }
	         });
      	 
        }
      	 
      	 
       }
       
       
       


       //手工制作
       function makePaper(ty)
       {
    	   exeObj.makeType=ty; //1是手工 2是智能
    	   if(ty==2)
    		{
    		     jQuery("#levle_li").show();
    		}
    	   else
    	   {
    		   jQuery("#levle_li").hide();
    	   }
    	   showDiv('hand_paper');
       }
       
       function showDiv(id)
       {
       	jQuery("#continer").children().each(function(){
       		if(jQuery(this).attr("id")==id)
       		{
       			jQuery(this).show();
       		}
       		else
       		{
       			jQuery(this).hide();
       		}
       	});
       }
       
       
       function handPaper()
       {
    	   exeObj.bookType=1;
    	   exeObj.name=jQuery("#hand_paper_name").val();
    	   exeObj.time=jQuery("#hand_paper_time").val();
    	   exeObj.subject=jQuery("#subject_sel").val();
    	   exeObj.grade=jQuery("#grade_sel").val();
    	  
//    	   exeObj.itemType=-1;
//    	   exeObj.cltys="";
//    	   exeObj.bs="";
//    	   exeObj.bsType=-1;
//    	   exeObj.page=0;

		   exeObj.level=-1;
		   exeObj.itemType=0;
		   exeObj.type = 1;
		   exeObj.select = $("#subject_sel").val();
		   exeObj.isBook = false;
		   exeObj.regular = '';
		   exeObj.page = 1;
		   exeObj.pageSize = 10;
    	   
    	   
    	   
    	   if(!exeObj.name || exeObj.name.length>10)
    	    {
    		   alert("试卷名字不为空并且不超过10个字");
    		   return ;
    		}
    	   
    	   var regExp=/^\d+(\.\d+)?$/;
    	   if(!regExp.test(exeObj.time))
    	   {
    	     alert("请正确输入考试时间。");
    	     return;
    	   }
    	   var time= parseInt(exeObj.time);
 		   if(time>180)
 			{
 			  alert("考试时间不超过180分钟");
 			  return;
 			}
 		  
    	   jQuery("#handle_first").hide();
    	   
    	   if(exeObj.makeType==1)
    	   {
    		   handPaper1();//手工
    	   }
    	   else
           {
    		   handPaper2();//智能选题
           }
       } 
       
       
       function handPaper1()
       {
    	   showKws();
    	   getItems(1);
    	   jQuery("#handle_second").show();
       }
       
       
       function handPaper2()
       {
    	   getKws();
    	   jQuery("#auto_make_div").show();
       }
       
       
       function handleCondintion(t)
       {
    	   if(exeObj.condition!=t)
    	   {
    		   exeObj.condition=t;
    		   if(t==1)
    			{
					exeObj.isBook = false;
    			   exeObj.bsType=-1;
    			   showKws();
    			}
    		   else
    			{
					exeObj.isBook = true;
    			   exeObj.cltys="";
    			   chubanshe(exeObj.subject);
    			}
    	   }
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
			   
			   
			   // if(type==2)
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
       jQuery("#"+id).removeClass("teacher_information_difficulty_I");
	   
       if(id!="buxian")
    	   {
    	      exeObj.itemType=ty;
    	   }
       else
    	   {
    	      exeObj.itemType="";
    	   }
	   
	      getItems(1);
   }
   
   function selectLevel1(id,ty)
   {
	   if(ty!=exeObj.level)
		{
	      exeObj.level=ty;
	      jQuery(".teacher_information_new_middle_button").removeClass("teacher_information_new_middle_button");
	      jQuery("#"+id).addClass("teacher_information_new_middle_button");
		}
   }
   
   
   
   function selectLevel(id,ty)
   {
	   var $obj= jQuery(".teacher_information_difficulty_IIII");
	   $obj.removeClass("teacher_information_difficulty_IIII");
	   $obj.addClass("teacher_information_difficulty_III");
	   jQuery("#"+id).addClass("teacher_information_difficulty_IIII");
       jQuery("#"+id).removeClass("teacher_information_difficulty_III");
	   
	   exeObj.level=ty;
	   getItems(1);
   }
   function select(kw)
   {
	   exeObj.select=kw;
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
						 html+='<span class="span-teacher-infor">出现在'+res[i].totalCount+'个学生的错题本中<em onclick="showErrorCount(\''+res[i].id+'\')">[查看]</em></span>';
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

	   function showErrorCount(id){
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
		   selectIts.push(ti);
		   var $obj=jQuery("#item_"+ti);
		   $obj.removeClass("teacher_information_content");
		   $obj.addClass("teacher_information_content_I");
		   jQuery("#add_"+ti).hide();
		   jQuery("#remove_"+ti).show();
		   inCreaseSelectItem(1,ty);
		}
   }
   
   
   function removeItem(ti,ty)
   {
	  var index= jQuery.inArray(ti, selectIts);  
	  if(index>-1)
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
   
   
   function create()
   {
	   var ids=getItemids();
	   if(!ids)
	   {
		   alert("请配置题目");
		   return;
	   }
	   
	   jQuery("#grade").val(exeObj.grade);
	   jQuery("#subject").val(exeObj.subject);
	   jQuery("#name").val(exeObj.name);
	   jQuery("#time").val(Math.round(exeObj.time));
	   jQuery("#ids").val(ids);
	   
	   jQuery("#cForm").submit();
   }
   
   
   function getItemids()
   {
	   var ids="";
   	   for(var i=0;i<selectIts.length;i++)
   	   {
   		ids+=selectIts[i]+",";
   	   }
   	   return ids;
   }
   
   
   function reset(id)
   {
 	  document.location.href="/testpaper/reset.do?pid="+id;
   }
   
   
   function getKws()
   {
   	if(!exeObj.subject)
   		return;
    	$.ajax({
           url: '/itempool/kw/list.do?type=1',
           type: 'get',
           data:{subject:exeObj.subject},
           contentType: 'application/json',
           success: function (res) {
           	kwArr=res;
        	jQuery("#kw_ui").empty();
       		jQuery("#kw_list").empty();
       		jQuery("#seled_kw").empty();
			selKw=[];
			
			jQuery("#c1_count,#c3_count,#c4_count,#c5_count").text(0);
       		
       		jQuery("#c1_count_in,#c3_count_in,#c4_count_in,#c5_count_in").val(0);
       		
			
           	if(kwArr.length>0)
           	{
           		for(var i=0;i<kwArr.length;i++)
           		{
           			var html='<li id="li_'+kwArr[i].idStr+'"><div id="kw_'+kwArr[i].idStr+'"><div  onclick="showKwDetail(\''+kwArr[i].idStr+'\')" >'+kwArr[i].name+'</div></div></li>';
           			jQuery("#kw_ui").append(html);
           		}
           		showKwDetail(kwArr[0].idStr);
           	}
           }
       });
   }
   
   
   function showKwDetail(kwid)
   {
	    var h=false;
    	for(var i=0;i<kwArr.length;i++)
		{
			if(kwArr[i].idStr==kwid)
			{
				obj=kwArr[i];
				h=true;
				break;
			}
		}
   	
    	if(h)
    	{
	   		var $obj=jQuery(".teacher_information_ssd_left_top");
	   		
	   		$obj.find("span").remove();
	   		$obj.removeClass("teacher_information_ssd_left_top");
	   		
	   		jQuery("#kw_"+kwid).addClass("teacher_information_ssd_left_top");
	   		jQuery("#kw_"+kwid).append('<span class="teacher_information_ssd_sj"></span>');
	   		
         
	   		jQuery("#kw_list").empty();
	   		for(var i=0;i<obj.list.length;i++)
    		{
    			var html='<div><input id="check_'+obj.list[i].idStr+'" type="checkbox"  onchange="handleKw(\''+obj.list[i].idStr+'\')" ><span>'+obj.list[i].value+'</span></div>';
    			jQuery("#kw_list").append(html);
    		}
	   		
   	    }
  }
   
   function handleKw(kwid)
   {
   	var check =jQuery("#check_"+kwid).is(':checked');
   	if(check)
   	{
   		 seleKw(kwid);
   		 increaseKw(1);
   	}
   	else
   	{
   		delKw(kwid);
   	}
   	try
   	{
   	  count();
   	}catch(x){alert(x)}
   }
   
   
   
   function increaseKw(tag)
   {
	   var c=parseInt(jQuery("#kw_count").text());
	   var c=c+tag;
	   jQuery("#kw_count").text(c)
   }
   
   
   //用户选择知识点
   function seleKw(kwid)
   {
   	if(obj)
   	{
   		var index=-1;
       	for(var i=0;i<selKw.length;i++)
       	{
       		if(selKw[i].idStr==kwid)
       		{
       			index=i;
       		}
       	}
       	
       	
       	if(index==-1)
       	{
		    	for(var i=0;i<obj.list.length;i++)
		    	{
		    			if(obj.list[i].idStr==kwid)
		    			{
		    				var html='<div id="kw_s_'+kwid+'"><span>'+obj.list[i].value+'</span><span class="teacher_information_bottom_II"  onclick="delKw(\''+kwid+'\')" >x</span></div>';
		    				jQuery("#seled_kw").append(html);
		    				selKw.push(obj.list[i]);
		    				break;
		    			}
		    	}
       	}
   	}
   }
   
   
   function delKw(kwid)
   {
	   	jQuery("#kw_s_"+kwid).remove();
	   	jQuery("#check_"+kwid).attr("checked",false);
	   	var index=-1;
	   	for(var i=0;i<selKw.length;i++)
	   	{
	   		if(selKw[i].idStr==kwid)
	   		{
	   			index=i;
	   		}
	   	}
	 
	   	if(index!=-1)
	   	{
	   		selKw.splice(index, 1);
	   	}
	   	increaseKw(-1);
	   	count();
   }
   
   
   
   
   function count()
   {
   	$.ajax({
           url: '/itempool/count.do',
           type: 'get',
           data:{kws:getKwStr(),level:exeObj.level},
           contentType: 'application/json',
           success: function (res) {
        	var c1=0,c2=0,c3=0,c4=0,c5=0;
           	if(res.length>0)
           	{
           		for(var i=0;i<res.length;i++)
           		{
           			var ty=res[i].name;
           			if(ty=="选择题") c1=res[i].value;
           			if(ty=="多选题") c2=res[i].value;
           			if(ty=="判断题") c3=res[i].value;
           			if(ty=="填空题") c4=res[i].value;
           			if(ty=="主观题") c5=res[i].value;
           		}
           	}
           	jQuery("#c1_count").text(c1+c2);
       		jQuery("#c3_count").text(c3);
       		jQuery("#c4_count").text(c4);
       		jQuery("#c5_count").text(c5);
       		
       		showCount("c1_count");
       		showCount("c3_count");
       		showCount("c4_count");
       		showCount("c5_count");
           }
       });
   }
   
   function getKwStr()
   {
   	var kw="";
   	for(var i=0;i<selKw.length;i++)
   	{
   		kw+=selKw[i].idStr+",";
   	}
   	return kw;
   }
   
   function increase(tag,inc)
   {
   	try
   	{
   	  var total=parseInt(jQuery("#"+tag).text());
   	  var nowCount=parseInt(jQuery("#"+tag+"_in").val());
   	  nowCount=nowCount+inc;
   	  if(nowCount>=total)
   	  {
   		  jQuery("#"+tag+"_in").val(total);
   	  }
   	  else if(nowCount<=0)
   	  {
   		  showCount(tag);
   	  }
   	  else
   	  {
   		  jQuery("#"+tag+"_in").val(nowCount);
   	  }
   	}catch(ex)
   	{
   		 showCount(tag);
   	}
   }
   
   
   function showCount(tag)
   {
   	var total=parseInt(jQuery("#"+tag).text());
   	if(total>100)
   	{
   		jQuery("#"+tag+"_in").val(50);
   		return;
   	}
   	if(total>50)
   	{
   		jQuery("#"+tag+"_in").val(20);
   		return;
   	}
   	if(total>20)
   	{
   		jQuery("#"+tag+"_in").val(10);
   		return;
   	}
   	jQuery("#"+tag+"_in").val(total);
   }
       
   
   function getCount(tag)
   {
   	try
   	{
   	  return parseInt(jQuery("#"+tag+"_in").val());
   	}catch(e)
   	{
   		return 0;
   	}
   }
   
   function autoCreate()
   {
	   $.ajax({
           url: '/testpaper/create1.do',
           type: 'get',
           data:{
           	  name:exeObj.name,
              grade:exeObj.grade,
           	  level:exeObj.level,
           	  subject:exeObj.subject,
           	  time:exeObj.time,
           	  kws:getKwStr(),
           	  ch:getCount("c1_count"),
           	  tf:getCount("c3_count"),
           	  gap:getCount("c4_count"),
           	  sub:getCount("c5_count"),
           	 },
           contentType: 'application/json',
           success: function (res) {
           	if(res.code=="200")
           	{
           		location.href="/testpaper/view.do?pid="+res.message+"&type=0";
           	}
           	else
           	{
           		alert(res.message);
           	}
           }
       });
   }
   

   function showSubject()
   {
	   var xueke=jQuery("#grade_sel").val();
	   $.ajax({
	         url: '/testpaper/subject.do?xd='+xueke,
	         type: 'get',
	         contentType: 'application/json',
	         success: function (res) {
	           jQuery("#subject_sel").empty();
	           for(var i=0;i<res.length;i++)
	        	   {
	        	    var html='<option value="'+res[i].idStr+'">'+res[i].value+'</option>';
	        	    jQuery("#subject_sel").append(html);
	        	   }
	         }
	     });
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
   
   
   
   function xueduan()
   {
  	 $.ajax({
         url: '/testpaper/res.do?type=1',
         type: 'get',
         contentType: 'application/json',
         success: function (res) {
           jQuery("#grade_sel").empty();
           for(var i=0;i<res.length;i++)
        	   {
        	    var html='<option value="'+res[i].idStr+'">'+res[i].value+'</option>';
        	    jQuery("#grade_sel").append(html);
        	   }
           
           
           showSubject();
         }
     });
   }
   
   function itemType()
   {
	   var html='<span id="'+res[i].idStr+'" class="teacher_information_difficulty_I"  onclick="selectItemType(\''+res[i].idStr+'\')">'+res[i].value+'</span>';
	   jQuery("#item_type_dd").append(html);
	    
	   $.ajax({
	         url: '/testpaper/itemType.do?subject='+exeObj.subject,
	         type: 'get',
	         contentType: 'application/json',
	         success: function (res) {
	 
	           for(var i=0;i<res.length;i++)
	        	   {
	        	    var html='<span id="'+res[i].idStr+'" class="teacher_information_difficulty_I"  onclick="selectItemType(\''+res[i].idStr+'\')">'+res[i].value+'</span>';
	        	    jQuery("#item_type_dd").append(html);
	        	   }
	         }
	     });
   }
	   function getTestPaperList(type){
		   var role = $('body').attr('userRole');
		   var isHead = false;
		   if(role == 'headMaster'){
			   isHead = true;
		   }
		   $.ajax({
			   url: '/testpaper/getTestPaperList.do?type='+type,
			   type: 'get',
			   contentType: 'application/json',
			   success: function (res) {
					$('.teacher_information_NO_info_botton ul').empty();
				   if(res.length > 0){
					   $('#empty_container').hide();
					   $('#paper_container').show();
				   } else {
					   $('#empty_container').show();
					   $('#paper_container').hide();
				   }
				   for(var i=0;i<res.length;i++) {
					   var exe = res[i];
					   var html='';
					   html += '<li id="li_'+exe.id+'">';
					   html += '<div class="teacher_information_NO_info_botton_I">';
					   html += '<span class="teacher_information_NO_info_botton_II">'+exe.name+'</span>';
					   html += '<span class="teacher_information_NO_info_botton_III">'+exe.subject+'</span>';
					   html += '<span class="teacher_information_NO_info_botton_IV">'+exe.total+'</span>';
					   html += '<span class="teacher_information_NO_info_botton_V">'+exe.createTime+'</span>';
					   html += '<span class="teacher_information_NO_info_botton_VII">'+exe.userName+'</span>';
					   html += '<span class="teacher_information_NO_info_botton_VI">';
					   if(0 == type){
						   html += '<i class="fa fa-location-arrow"></i><span onclick="reset(\''+exe.id+'\')">&nbsp;手工编辑</span>';
					   }
					   html += '<i class="fa fa-location-arrow"></i><a target="_black" href="/testpaper/view.do?pid='+exe.id+'&type='+type+'">&nbsp;预览</a>';
					   if(0 == type || isHead){
						   html += '<i class="fa fa-trash"></i><span class="teacher_delete" onclick="deletePaper(\''+exe.id+'\')">&nbsp;删除</span>';
					   }

					   html += '</span></div></li>';

					   $('.teacher_information_NO_info_botton ul').append(html);
				   }
			   }
		   });
	   }
   
   $(document).ready(function(){
       $(".teacher_information_li").click(function(){
           $(this).removeClass('cur-a').siblings('.teacher_information_info ul li').addClass('cur-a');
		   if($(this).attr('type') == 0){
				$('#creatTime').text('创建时间');
		   } else {
			   $('#creatTime').text('推送时间');
		   }
		   getTestPaperList($(this).attr('type'));
       });

	   xueduan();
	   //itemType();
	 });
  </script>
    
</head>
<body style="background: #fff;" userRole="${userRole}">
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



    <form id="cForm"  action="/testpaper/create.do" method="post">
      <input id="grade" name="grade" type="hidden" />
      <input id="subject" name="subject" type="hidden" />
      <input id="name" name="name" type="hidden" />
      <input id="time" name="time" type="hidden" />
      <input id="ids" name="ids" type="hidden" />
    </form>

  
    <div id="continer" class="teacher_information_right" >
        <div class="teacher_information_info" >
              <ul>
                <li class="teacher_information_li" type="0"><a href="javascript:void(0)" >我的组卷</a></li>
                <li class="teacher_information_li cur-a" type="1"><a href="javascript:void(0)" >校本试卷</a></li>
             </ul>
            
            
             <c:if test="${ fn:length(list) > 0}">
             <div  class="teacher_information_right_I" >
             </c:if>
             
            <c:if test="${ fn:length(list) == 0}">
             <div  class="teacher_information_right_I" style="display:none;" >
             </c:if>
            
                <button onclick="makePaper(1)">手工组卷</button>
                <button onclick="makePaper(2)">智能组卷</button>
            </div>


        </div>



         <c:if test="${fn:length(list) == 0}">
          <div id="empty_container" class="teacher_information_new" >
            <div class="teacher_new_info">
                <span>您还没有创建组卷，马上去创建一份吧！</span>
                <button class="teacher_new_practice" onclick="makePaper(2)">智能组卷</button>
                <button class="teacher_new_practice" id="teacher_sg" onclick="makePaper(1)">手工组卷</button>
             </div>
         </div>
        </c:if>


        <c:if test="${ fn:length(list) >= 0}">
        <div id="paper_container" <c:if test="${ fn:length(list) == 0}"> style="display: none"</c:if>>
            <div class="teacher_information_NO">
                <div class="teacher_information_NO_info">
                    <span class="teacher_information_NO_info_I">试卷名称</span>
                    <span class="teacher_information_NO_info_II">科目</span>
                    <span class="teacher_information_NO_info_III">题量</span>
                    <span class="teacher_information_NO_info_IV" id="creatTime">创建时间</span>
					<span class="teacher_information_NO_info_V">创建老师</span>
                </div>
            </div>
            <div class="teacher_information_NO_info_botton">
                <ul>
                     <c:forEach items="${list}" var="exe">
                        <li id="li_${exe.id}">
                        <div class="teacher_information_NO_info_botton_I">
                            <span class="teacher_information_NO_info_botton_II" title="${exe.name}">${exe.name}</span>
                            <span class="teacher_information_NO_info_botton_III">
                               ${exe.subject}
                            </span>
                            <span class="teacher_information_NO_info_botton_IV"> ${exe.total}</span>
                            
                            <span class="teacher_information_NO_info_botton_V">${exe.createTime}</span>
							<span class="teacher_information_NO_info_botton_VII">${exe.userName}</span>
                            <span class="teacher_information_NO_info_botton_VI">
                                     
                                     <i class="fa fa-location-arrow"></i><span onclick="reset('${exe.id}')">&nbsp;手工编辑</span>
                                     <i class="fa fa-location-arrow"></i><a target="_black" href="/testpaper/view.do?pid=${exe.id}&type=0">&nbsp;预览</a>
                                     <i class="fa fa-trash"></i><span class="teacher_delete" onclick="deletePaper('${exe.id}')">&nbsp;删除</span>
                            </span>
                        </div>
                    </li>
                   </c:forEach> 
                </ul>
            </div>
        </div>
        </c:if>


        
        <!--=====================================手工组卷=========================================-->
        <div id="hand_paper" style="display: none">
            <div id="handle_first" class="teacher_information_new_I">
                <div class="teacher_information_new_I_top">
                    <span class="information_I">我的组卷</span>
                    <span class="information_II">/&nbsp;试卷结构</span>
                </div>
                <div class="teacher_information_new_info">
                    试卷结构
                </div>
                <div class="teacher_information_new_middle">
                    <ul>
                        <li>
                            <span>试卷名称</span>
                            <input id="hand_paper_name">
                        </li>
                        <li>
                            <span>考试时间</span>
                            <input id="hand_paper_time" type="number" min="1" step="1">
                                                                                                  （ 分钟）
                        </li>
                        <li>
                            <span>学段</span>
                            <select id="grade_sel" onchange="showSubject()">
                            
                              <!-- 
                                    <option value="1">一年级</option>
									<option value="2">二年级</option>
									<option value="3">三年级</option>
									<option value="4">四年级</option>
									<option value="5">五年级</option>
									<option value="6">六年级</option>
									
									
									<option value="7">初一</option>
									<option value="8">初二</option>
									<option value="9">初三</option>
									 -->
				
                            </select>
                        </li>
                        <li>
                            <span>科目</span>
                            <select id="subject_sel">
                            
                                  <!--  
                                    <option id="subject_sel_1" value="1">语文</option>
                                    <option id="subject_sel_2" value="2">数学</option>
                                    <option id="subject_sel_3" value="3">英语</option>
                                    <option id="subject_sel_4" value="4">物理</option>
                                    <option id="subject_sel_5" value="5">化学</option>
                                    -->
                            </select>
                        </li>
                        
                        <li id="levle_li" style="display:none;">
                            <span>难度</span>
                            
                            <button id="button_6" class="teacher_information_new_middle_button" onclick="selectLevel1('button_6',-1)" >不限</button>
                            
                            
                            <button id="button_1" onclick="selectLevel1('button_1',1)" >容易</button>
                            <button id="button_2" onclick="selectLevel1('button_2',2)" >较容易</button>
                            <button id="button_3" onclick="selectLevel1('button_3',3)" >中等</button>
                            <button id="button_4" onclick="selectLevel1('button_4',4)" >较难</button>
                            <button id="button_5" onclick="selectLevel1('button_5',5)" >难</button>
                            
                        </li>
                        
                    </ul>
                </div>
                <div class="teacher_information_new_buttom">
                    <span id="teacher_new_information" onclick="handPaper()">下一步</span>
                </div>
            </div>
            
            
            
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
                        <dd id="item_type_dd">
                            <span>题型</span>
                             <span id="itemtype_6" class="teacher_information_difficulty_II" onclick="selectItemType('itemtype_6',0)">不限</span>
                            <span id="itemtype_1" class="teacher_information_difficulty_I" onclick="selectItemType('itemtype_1',1)">选择题</span>
							<span id="itemtype_3" class="teacher_information_difficulty_I" onclick="selectItemType('itemtype_3',3)">判断题</span>
							<span id="itemtype_4" class="teacher_information_difficulty_I" onclick="selectItemType('itemtype_4',4)">填空题</span>
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
                               
                            </div>
                            <span>2.判断题</span>
                            <div id="select_3"  class="teacher_information_right_VV_info">
                               
                            </div>
                             <span>3.填空题</span>
                            <div id="select_4" class="teacher_information_right_VV_info">
                               
                            </div>
                             <span>4.主观题</span>
                            <div id="select_5" class="teacher_information_right_VV_info">
                               
                            </div>
                            <button class="teacher_information_right_VV_bottom" onclick="create()">确定生成试卷</button>
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
        
        
        <!--===============================自动组卷=============================-->
        <div id="auto_make_div" style="display: none">
        
            <!--===========================选择知识点===================================-->
            <div class="teacher_information_new_II">
                <div class="teacher_information_new_I_top">
                    <span class="information_I" style="margin-left: 20px;">我的组卷</span>
                    <span class="information_I">/&nbsp;新建组卷</span>
                    <span class="information_II">/&nbsp;选择知识点</span>
                </div>
                
                <!--  
                <div class="teacher_information_new_info">
                                                         选择知识点
                </div>
                <div class="teacher_information_xz">
                                                           选择版本
                </div>
                
                
                
                <div class="teacher_information_xl">
                    <select id="auto_etype_select" onchange="getKws()">
                            <option value="1">人民教育出版社</option>
                            <option value="2">上海科学技术出版社</option>
                            <option value="3">上海教育出版社</option>
                            <option value="4">江苏科学技术出版社</option>
                            <option value="5">译林出版社</option>
                            <option value="6">山东教育出版社</option>
                            <option value="7">中考真题</option>
                    </select>
                </div>
                -->
                
                
                <div class="teacher_information_xz">
                    <span>选择知识点</span>
                    <button style="display:none;">选择全部知识点</button>
                    <!--同步教材-->
                    <button style="display: none">选择全部单元</button>
                </div>
                <div class="teacher_information_ssd">
                    <table>
                        <tr>
                            <td>
                                <div class="teacher_information_ssd_left">
                                    <div class="teacher_information_ssd_ul">
                                        <ul id="kw_ui">
                                        </ul>
                                    </div>
                                </div>
                            </td>
                            <td colspan="3">
                                <div class="teacher_information_ssd_right">
                             
                                    <div id="kw_list" class="teacher_information_ssd_right_info">
                                      <!--  
                                        <div> <input type="checkbox" name="checkbox1" id="Checkbox1"><span>函数asfasfsadfasf</span></div>
                                        <div> <input type="checkbox" name="checkbox1" id="Checkbox2"><span>函阿里看见对方拉就是浪费骄傲了放假啦快速减肥来看数</span></div>
                                        <div> <input type="checkbox" name="checkbox1" id="Checkbox3"><span>函数</span></div>
                                        <div> <input type="checkbox" name="checkbox1" id="Checkbox4"><span>函数</span></div>
                                        -->
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="4">
                                <div  class="teacher_information_info_bottom">
                                                                                                                        已选知识点(<span id="kw_count">0</span>)
                                    <div id="seled_kw" class="teacher_information_info_bottom_I">
                                      <!--  
                                        <div>
                                            <span>函数</span>
                                            <span class="teacher_information_bottom_II">x</span>
                                        </div>
                                        <div>
                                            <span>基本初等函数与应用</span>
                                            <span class="teacher_information_bottom_II">x</span>
                                        </div>
                                        -->
                                    </div>
                                </div>

                            </td>
                        </tr>
                    </table>
                </div>
                
                
                
                <div class="teacher_information_info_bottom_III">
                    <ul>
                        <li>
                            <div>
                                <img src="/images/teacher_LI.png">
                                <div>选择题</div>
                                <input id="c1_count_in" class="teacher_information_info_bottom_IIII" value="0">
                                <div class="teacher_information_info_bottom_V" onclick="increase('c1_count',1)" title="点击增加选择题数量">+</div><br>
                                <div class="teacher_information_info_bottom_IV" onclick="increase('c1_count',-1)" title="点击减少选择题数量">-</div>
                                <div class="teacher_information_info_bottom_VI">共<span id="c1_count"></span>题</div>
                            </div>
                        </li>
                        <li>
                            <div>
                                <img src="/images/teacher_LI.png">
                                <div>判断题</div>
                                <input id="c3_count_in" class="teacher_information_info_bottom_IIII"  value="0">
                                <div class="teacher_information_info_bottom_V" onclick="increase('c3_count',1)" title="点击增加判断题数量">+</div><br>
                                <div class="teacher_information_info_bottom_IV" onclick="increase('c3_count',-1)" title="点击减少选择题数量">-</div>
                                <div class="teacher_information_info_bottom_VI">共<span id="c3_count"></span>题</div>
                            </div>
                        </li>
                        
                         <li>
                            <div>
                                <img src="/images/teacher_LI.png">
                                <div>填空题</div>
                                <input id="c4_count_in" class="teacher_information_info_bottom_IIII"  value="0">
                                <div class="teacher_information_info_bottom_V" onclick="increase('c4_count',1)" title="点击增加填空题数量">+</div><br>
                                <div class="teacher_information_info_bottom_IV" onclick="increase('c4_count',-1)" title="点击减少填空题数量">-</div>
                                <div class="teacher_information_info_bottom_VI">共<span id="c4_count"></span>题</div>
                            </div>
                        </li>
                        
                         <li>
                            <div>
                                <img src="/images/teacher_LI.png">
                                <div>主观题</div>
                                <input id="c5_count_in"  class="teacher_information_info_bottom_IIII"  value="0">
                                <div class="teacher_information_info_bottom_V" onclick="increase('c5_count',1)" title="点击增加主观题数量">+</div><br>
                                <div class="teacher_information_info_bottom_IV" onclick="increase('c5_count',-1)" title="点击减少选择题数量">-</div>
                                <div class="teacher_information_info_bottom_VI">共<span id="c5_count"></span>题</div>
                            </div>
                        </li>
                    </ul>
                </div>
                
                
                <div class="teacher_information_new_buttom">
                    <span id="new_information_I" onclick="autoCreate()">下一步</span>
                </div>
            </div>
        </div>
        
        
        
    </div>
</div>



<!-- 页尾 -->
   <%@ include file="../common_new/foot.jsp"%>
</body>
</html>
