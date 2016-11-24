<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>练习</title>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
    <link rel="stylesheet" type="text/css" href="/static/css/itempool/student_information.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script src='http://static.polyv.net/file/polyvplayer_v2.0.min.js'></script>
    <script type="text/javascript">

    var exeObj={};
    var kwArr=[];
    var obj;//选择的知识点集合
    var selKw=[];//
    var itemDetail={}; //题目详情
    $(function(){
    	exeObj.level=1;
        $(".new_practice").click(function(){
            $(".student_information_new").hide();
            $(".student_information_new_I").show();
            $(".student_II").hide();
        })

        $("#new_information").click(function(){
            $(".student_information_new_I").hide();
            $(".student_information_new_II").show();
        })
       
    })


    $(function(){
        $(".chooseall").click(function(){
            var isChecked = $(this).prop("checked");
            $("input[name='checkbox1']").prop("checked", isChecked);
        });
    });



    $(function(){
        $(".student_information_NO_info_botton>ul>li:even div").css("background-color","#f9f9f9")
    })
   
    $(function(){
        $(".student_information_new_VV>span").click(function(){
            $(".student_information_new_VV").hide();
            $(".student_information_new_VVI").show();
        })
    })
    
    
    
    function deleteExercise(id)
    {
    	 $.ajax({
             url: '/itempool/student/delete.do?id='+id,
             type: 'get',
             contentType: 'application/json',
             success: function (res) {
               if(res.code=="200")
            		{
            	      jQuery("#li_"+id).remove();
            	    }
             }
         });
    }

    function getFinishExercise(st)
    {
    	 jQuery("#error_div,.student_information_new_I").hide();
    	 jQuery("#content_ul").empty();
    	jQuery(".student_information_li").removeClass("student_information_li");
    	var obj="a_link_1";
    	if(st==1)
    		{
    		obj="a_link_2";
    		}
    	jQuery("#"+obj).addClass("student_information_li");
    	$.ajax({
            url: '/itempool/student/exercise/list.do',
            type: 'get',
            data:{state:st,skip:0,limit:100},
            contentType: 'application/json',
            success: function (res) {
            	
            	if(!res||res.length==0)
            	{
            		jQuery(".student_information_new").show();
            		return;
            	}
            	
            	showDiv("content_div");
            	jQuery(".student_information_new").hide();
               
            	var html='';
            	for(var i=0;i<res.length;i++ )
            	{
            		try
            		{
            			html='';
	            		html+='<li id="li_'+res[i].idStr+'">';
	            		html+='<div class="student_information_NO_info_botton_I">';
	            		html+='<span class="student_information_NO_info_botton_II">'+res[i].name+'</span>';
	            		html+='<span class="student_information_NO_info_botton_III">'+res[i].subjectName+'</span>';
	            		html+='<span class="student_information_NO_info_botton_IV"><span>'+res[i].alreadyFinish+'</span>/<span>'+res[i].totalItem+'</span></span>';
	            		html+='<span class="student_information_NO_info_botton_V"><span>'+res[i].time+'</span></span>';
	            		html+='<span class="student_information_NO_info_botton_VI">';
	            		if(res[i].state==1)
	            		{
	            			  html+='<i class="fa fa-play-circle"></i><span onclick="beginAnswer(\''+res[i].idStr+'\')">&nbsp;再做一遍&nbsp;&nbsp;</span>';
	            		}
	            		if(res[i].state==0)
	            		{
	            			  html+='<i class="fa fa-play-circle"></i><span onclick="beginAnswer(\''+res[i].idStr+'\')">&nbsp;答题</span>';
	            		}
	            		html+='<i class="fa fa-trash"></i><span class="student_delete" onclick="deleteExercise(\''+res[i].idStr+'\')">&nbsp;删除</span>';
	            		html+='</span></div></li>';
	            		jQuery("#content_ul").append(html);
            		}catch(x)
            		{}
            	}
            }
        });
    }
    
    function beginAnswer(id)
    {
    	exeObj.answerExe=id;
    	$.ajax({
            url: '/itempool/student/exer/begin.do?exeid='+exeObj.answerExe,
            type: 'get',
            contentType: 'application/json',
            success: function (res) {
            	exeBindData(res);
            }
        });    	
    	showDiv("beginAnswer");
    }
    
    //组卷题绑定数据
    function exeBindData(res)
    {
    	try
    	{
    		var ss=jQuery(".student_information_start_bottom_B");
    		ss.removeClass("student_information_start_bottom_B").addClass("student_information_start_bottom_A");
    		jQuery("#sub_answer").val("");
    		exeObj.answer="";
    		exeObj.answerItem=res.id;
    	    jQuery("#exe_unfin_count").text(res.totalCount);
	    	jQuery("#exe_level").text(res.level);
	    	jQuery("#exe_type").text(res.itemType);
	    	jQuery("#exe_kwp").text(res.kw);
	    	jQuery("#exe_scope").text(res.score);
	    	jQuery("#exe_tigan").html(res.item); 
	    	if(res.type==1 || res.type==2) //选择题
	    	{
	    		jQuery("#exe_choice_div").show();
	    		jQuery("#exe_sub_div").hide();
	    	}
	    	else
	    	{
	    		jQuery("#exe_choice_div").hide();
	    		jQuery("#exe_sub_div").show();
	    	}
	    	
    	}catch(x)
    	{
    		
    	}
    }
    
    //得到上一道题，或者下一道题,提交答案
    function getExeitem(direction)
    {
    	
    	if(direction==0)
    		{
    		 if(!exeObj.answerItem)
    		 {
    			 return ;
    		 }
    		}
    	if(!exeObj.answer)
    	{
    		exeObj.answer=jQuery("#sub_answer").val();
    	}	
    	
    	var cuttent=parseInt(jQuery("#exe_current_count").text());
    	var total=parseInt(jQuery("#exe_unfin_count").text());
    	if(direction==0 && exeObj.answerItem)
    	{
    		if(cuttent>1)
    			{
    		      jQuery("#exe_current_count").text(cuttent-1);
    			}
    	}
    	if(direction==1)
    	{
    		if(cuttent<total)
    		{
    		      jQuery("#exe_current_count").text(cuttent+1);
    		}
    		if(cuttent==total-1)
    		{
    			   jQuery("#exercise_submit,#exercise_submit1").show();
    		}
    	}
    	
    	
    	$.ajax({
            url: '/itempool/student/exercise/item.do?exeid='+exeObj.answerExe+'&itemId='+exeObj.answerItem+'&direction='+direction+'&answer='+exeObj.answer,
            type: 'get',
            contentType: 'application/json',
            success: function (res) {
            	{
            		if(!res && direction=="1")
            			{
            			 alert("你已经做完了，可以提交了！！！");
            			}
            		else
            			{
            			 exeBindData(res);
            			}
            	   
            	}
            }
        });
    }
    
    //查看答案
    function viewAnswer()
    {
    	jQuery("#exe_sub_div").show();
    	var url='/itempool/student/exercise/view/answer.do?exeid='+exeObj.answerExe;
    	if(exeObj.viewItem)
    	{
    		url='/itempool/student/exercise/view/answer.do?exeid='+exeObj.answerExe+'&itemId='+exeObj.viewItem;
    	}
    	
    	$.ajax({
            url: url,
            type: 'get',
            contentType: 'application/json',
            success: function (res) {
            	{
            		showDiv("view_myAnswer");
            		myAnswerBindData(res);
            	}
            }
        });
    }
    
    
    function GetPercent(num, total) {
    	num = parseFloat(num);
    	total = parseFloat(total);
    	if (isNaN(num) || isNaN(total)) {
    	return "-";
    	}
    	return total <= 0 ? "0%" : (Math.round(num / total * 10000) / 100.00 + "%");
    } 
    
    //我的答案数据绑定
    function myAnswerBindData(res)
    {
    	try
    	{
    		exeObj.viewItem=res.id;
    	    jQuery("#total_count").text(res.totalCount);
	    	jQuery("#right_count").text(res.rightCount);
	    	jQuery("#right_v").text(GetPercent(res.rightCount,res.totalCount));
	    	jQuery("#right_answer").html(res.answer);
	    	//jQuery("#parse_answer").text(res.parse);
	    	
	    	
	    	jQuery("#answer_total").text(res.totalCount);
	    	
	    	var c= parseInt(jQuery("#answer_current").text());
	    	if(c<res.totalCount)
	    	{
	    		jQuery("#answer_current").text(c+1);
	    	}
	    	
	    	jQuery("#answer_level").text(res.level);
	    	
	    	jQuery("#answer_type").text(res.itemType);
	    	jQuery("#answer_kw").text(res.kw);
	    	
	    	jQuery("#answer_score").text(res.score);
	    	
	    	jQuery("#answer_tigan").html(res.item);
	    	
	    	if(res.myAnswer)
	    	{
	    	   jQuery("#myanswer_show").text("我的答案"+res.myAnswer);
	    	}
	    	
	    	if(res.right)
	    	{
	    		jQuery("#answer_cuowu").hide();
	    		jQuery("#answer_zhengque").show();
	    	}
	    	else
	    	{
	    		jQuery("#answer_cuowu").show();
	    		jQuery("#answer_zhengque").hide();
	    	}
	    	
	    	

	    	if(res.type==1 || res.type==2) //选择题
	    	{
	    		jQuery("#exe_choice_div").show();
	    		jQuery("#exe_sub_div").hide();
	    	}
	    	else
	    	{
	    		jQuery("#exe_choice_div").hide();
	    		jQuery("#exe_sub_div").show();
	    	}
	    	
    	}catch(x)
    	{
    		
    	}
    }
    
    //得到上一道题，或者下一道题,提交答案
    function submitExeit()
    {
    	if(!exeObj.answer)
    	{
    		exeObj.answer=jQuery("#sub_answer").val();
    	}	
    	$.ajax({
            url: '/itempool/student/exer/submit.do?exeid='+exeObj.answerExe+'&itemId='+exeObj.answerItem+'&answer='+exeObj.answer,
            type: 'get',
            contentType: 'application/json',
            success: function (res) {
            		showDiv("subSuccess_div");
            }
        });
    }
    function selectAnswer(obj,v)
    {
    	var old=jQuery(".student_information_start_bottom_B");
    	old.removeClass("student_information_start_bottom_B").addClass("student_information_start_bottom_A");
    	jQuery(obj).addClass("student_information_start_bottom_B");
    	exeObj.answer=v;
    }
    
    function newExercise()
    {
    	jQuery(".student_information_new,#content_div,.student_information_new_I").hide();
    	jQuery(".student_information_new_I").show();
    	//jQuery("#new_zujuan").remove();
    }
    
    function showKnowledgePoint()
    {
    	exeObj.sub=jQuery("#subject_sel").val();
    	exeObj.name=jQuery("#exe_name").val();
    	if(!exeObj.name || exeObj.name.length>10)
        {
    		alert("练习名字不为空并且不超过10个字");
    		return;
        }   
    	getKws();
    	jQuery(".student_information_new_I").hide();
    	jQuery("#KnowledgePoint").show();
    }
    
    function getKws()
    {
    	if(!exeObj.sub)
    		return;
    	$.ajax({
            url: '/itempool/kw/list.do?type=1',
            type: 'get',
            data:{subject:exeObj.sub},
            contentType: 'application/json',
            success: function (res) {
            	kwArr=res;
            	jQuery("#kw_ui").empty();
            	jQuery("#kw_list").empty();
            	jQuery("#seled_kw").empty();
            	selKw=[];
            	
            	if(kwArr.length>0)
            	{
            		jQuery("#kw_ui").empty();
            		for(var i=1;i<kwArr.length;i++)
            		{
            			var html='<li><div><div id="kw_'+kwArr[i].idStr+'" onclick="showKwDetail(\''+kwArr[i].idStr+'\')" >'+kwArr[i].name+'</div></div></li>';
            			jQuery("#kw_ui").append(html);
            		}
            		showKwDetail(kwArr[0].idStr);
            	}
            }
        });
    }
    
    
    
    function showKwDetail(kwid)
    {
    	jQuery(".student_information_ssd_left_top").removeClass("student_information_ssd_left_top");
    	jQuery("#kw_"+kwid).addClass("student_information_ssd_left_top");
    	
    	for(var i=0;i<kwArr.length;i++)
		{
			if(kwArr[i].idStr==kwid)
			{
				obj=kwArr[i];
				break;
			}
		}
    	
    	if(obj)
    	{
    		jQuery("#sel_kw,#sel_kw_span").text(obj.name);
    		
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
    
    
   function handleAll()
   {
	  // var check =jQuery("#kwAll").is(':checked');
	  // jQuery("#kw_list").find("input")
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
		    				var html='<div id="kw_s_'+kwid+'"><span>'+obj.list[i].value+'</span><span class="student_information_bottom_II"  onclick="delKw(\''+kwid+'\')" >x</span></div>';
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
    	count();
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

	
	
    function crateExercise()
    {
    	var name=jQuery.trim(exeObj.name);
    	
    	if(!name || name.length>10)
        {
    		alert("练习名字不为空并且不超过10个字");
    		return;
        }
    	
       var regExp=/^\d+(\.\d+)?$/;
  	   if(!regExp.test(jQuery("#c1_count_in").val()))
  	   {
  	     alert("请正确输入选择题数量。");
  	     return;
  	   }
	   if(!regExp.test(jQuery("#c3_count_in").val()))
	   {
	     alert("请正确输入判断题数量。");
	     return;
	   }
	   if(!regExp.test(jQuery("#c4_count_in").val()))
	   {
	     alert("请正确输入填空题数量。");
	     return;
	   }
	   if(!regExp.test(jQuery("#c5_count_in").val()))
	   {
	     alert("请正确输入主观题数量。");
	     return;
	   }
    	
    	
      $.ajax({
            url: '/itempool/student/exer/create.do',
            type: 'get',
            data:{
            	  name:name,
            	  level:exeObj.level,
            	  subject:exeObj.sub,
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
            		location.href="/itempool/student/exercise.do?version=14";
            	}
            	else
            	{
            		alert(res.message);
            	}
            }
        });
    	
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
    
    function myErrorItem()
    {
    	var obj=jQuery("#new_zujuan");
    	
    	if(obj)
    		{
    		obj.hide();
    		}
    	$.ajax({
            url: '/itempool/subject/count.do',
            type: 'get',
            contentType: 'application/json',
            success: function (res) {
            	if(res.length>0)
            	{
            		for(var i=0;i<res.length;i++)
            		{
            			try
            			{
            			jQuery("#subject_"+res[i].id).text(res[i].value);
            			}catch(x){}
            		}
            	}
            }
        });
    	
    	jQuery(".student_information_li").removeClass("student_information_li");
    	jQuery("#a_link_3").addClass("student_information_li");
    	showDiv("error_div");
    }
    
    function showErrorItem(sub)
    {
    	var count=parseInt(jQuery("#subject_"+sub).text());
    	if(count<=0)
    		{
    		 return ;
    		}
    	itemDetail.sub=sub;
    	itemDetail.direction=-1;
    	$.ajax({
            url: '/itempool/erritem/scope.do?sub='+sub,
            type: 'get',
            contentType: 'application/json',
            success: function (res) {
            	jQuery("#error_kw").empty();
            	if(res.length>0)
            	{
            		for(var i=0;i<res.length;i++)
            		{
            			var html="<span id=\"err_"+res[i].idStr+"\">"+res[i].value+"</span>";
            			jQuery("#error_kw").append(html);
            		}
            	}
            }
        });
    	try
    	{
    	 loadItemDetail(1);
    	}catch(x){}
		showDiv("det_error_div");
    }
    
    
    function loadItemDetail(order)
    {
    	errorItemClear();
    	jQuery(".student_information_new_info_S").removeClass("student_information_new_info_S").addClass("student_information_new_info_SS");
    	jQuery("#error_sort_"+order).removeClass("student_information_new_info_SS").addClass("student_information_new_info_S");
    	
    	itemDetail.order=order;//默认乱序
    	$.ajax({
            url: '/itempool/erritem/detail.do?order='+order+"&subject="+itemDetail.sub,
            type: 'get',
            contentType: 'application/json',
            success: function (res) {
            	itemDetail.itemId=res.id;
            	bindData(res,true);
            }
        });
    }
    
    
    function nextItemDetail(direction)
    {
    	$.ajax({
            url: '/itempool/erritem/detail.do?order='+itemDetail.order+"&itemId="+itemDetail.itemId+"&direction="+direction+"&subject="+itemDetail.sub,
            type: 'get',
            contentType: 'application/json',
            success: function (res) {
            	itemDetail.itemId=res.id;
            	bindData(res,false);
            }
        });
    }
    
    function deleteItem()
    {
    	$.ajax({
            url: '/itempool/erritem/delete.do?item='+itemDetail.itemId,
            type: 'get',
            contentType: 'application/json',
            success: function (res) {
            	if(res.code=='200')
            	{
            		var cur=parseInt(jQuery("#error_count_current").text());
            		var total=parseInt(jQuery("#error_c_total").text());
            		if(cur!=total)
            		{
            			jQuery(".error_count_total").text(total-1);
            			jQuery("#error_count_current").text(cur-1);
            			nextItemDetail(1);
            		}
            		else
            		{
            			loadItemDetail(itemDetail.order);
            		}
            	}
            }
        });
    }
    
    function bindData(res,isf)
    {
    	try
    	{
    		if(isf)
    		{
    			  jQuery(".error_count_total").text(res.totalCount);
    		}
    		
    		if(!isf)
    			{
		    		var total =parseInt(jQuery("#error_c_total").text());
		    		var current= parseInt(jQuery("#error_count_current").text());
		    		
		    		if(total>current)
		    		{
		    			jQuery("#error_count_current").text(current+1);
		    		}
    			}
    		else
    			{
    			   jQuery("#error_count_current").text(1);
    			}
    		
    		jQuery("#error_count").text(res.count);
	    	jQuery("#error_time").text(res.time);
	    	jQuery("#error_level").text(res.level);
	    	jQuery("#error_type").text(res.itemType);
	    	jQuery("#error_kwp").text(res.kw);
	    	if(itemDetail.kwid)
	    	{
	    	  jQuery("#err_"+itemDetail.kwid).find("span").remove();
	    	}
	    	jQuery("#err_"+res.kwId).prepend("<span>√</span>");
	    	jQuery("#error_scope").text(res.score);
	    	jQuery("#answer").html(res.answer);
	    	//jQuery("#parseAnswer").text(res.parse);
            jQuery("#tigan").html(res.item);
            
            jQuery("#myAnswer").html("我的答案：<span>"+res.myAnswer+"</span>");
            itemDetail.kwid=res.kwId;
                  
    	}catch(x)
    	{
    		
    	}
    }
    
    function errorItemClear()
    {
    	jQuery("#error_count,#error_time,#error_level,#error_type,#error_kwp,#error_scope,#answer,#tigan,#myAnswer").text("");
    }
    
    
    function selectLevel1(id,ty)
    {
 	   if(ty!=exeObj.level)
 		{
 	      exeObj.level=ty;
 	      jQuery(".student_information_new_middle_button").removeClass("student_information_new_middle_button");
 	      jQuery("#"+id).addClass("student_information_new_middle_button");
 		}
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
   
    
    $(document).ready(function(){
    	xueduan();
 	   //itemType();
 	 });
    
</script>
    
</head>
<body style="background: #fff;">
<!-- 页头 -->
<%@ include file="../common_new/head.jsp"%>
<!-- 页头 -->

<div id="YCourse_player" class="player-container">
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
<div class="student_infromation_body">
    <div class="student_infromation_main">
        <!-- 左侧导航-->
        <%@ include file="../common_new/col-left.jsp" %>
        <!-- left end -->

        <!-- right start-->

          <div class="student_information_right">
            <!--==========================我的组卷==============================================-->
            <div class="student_information_info">
                <ul>
                    <li><a id="a_link_1" href="javascript:getFinishExercise(-1)" class="student_information_li">我的练习</a></li>
                    <li><a id="a_link_2" href="javascript:getFinishExercise(1)" >已完成</a></li>
                    <li style="display:none"><a id="a_link_3" href="javascript:myErrorItem()">错题本</a></li>
                </ul>
            </div>
            <!--============================组卷为空===================================-->
            
            
           <c:if test="${fn:length(list) == 0}">
            <div id="new_zujuan" class="student_information_new" >
                <div class="student_new_info">
                    <span>您还没有创建练习，马上去创建一份吧！</span>
                    <button class="new_practice" onclick="newExercise()">新建练习</button>
                    
                </div>
            </div>
           </c:if>
         
         
               <div id="continer" class="student_information_finish_I student_information_finish_II" >
               
                <c:if test="${ fn:length(list) == 0}">
                  <div id="content_div" class="student_information_NO student_II"  style="display:none;">
                    <div class="student_information_NO_top">
                        <button  class="new_practice" onclick="newExercise()">新建练习</button>
                    </div>
                    <div class="student_information_NO_info">
                        <span class="student_information_NO_info_I">名称</span>
                        <span class="student_information_NO_info_II">科目</span>
                        <span class="student_information_NO_info_III">完成度</span>
                        <span class="student_information_NO_info_IV">创建时间</span>
                    </div>
                    <div class="student_information_NO_info_botton">
                        <ul id="content_ul">
    
                        </ul>
                    </div>
                </div>
              </c:if>
               
                <c:if test="${ fn:length(list) > 0}">
                <div id="content_div" class="student_information_NO student_II" >
                    <div class="student_information_NO_top">
                        <button  class="new_practice" onclick="newExercise()">新建练习</button>
                    </div>
                    <div class="student_information_NO_info">
                        <span class="student_information_NO_info_I">名称</span>
                        <span class="student_information_NO_info_II">科目</span>
                        <span class="student_information_NO_info_III">完成度</span>
                        <span class="student_information_NO_info_IV">创建时间</span>
                    </div>
                    <div class="student_information_NO_info_botton">
                        <ul id="content_ul">
                        <c:forEach items="${list}" var="exe">
                           <li id="li_${exe.idStr}">
                                <div class="student_information_NO_info_botton_I">
                                    <span class="student_information_NO_info_botton_II">${exe.name}</span>
                                    <span class="student_information_NO_info_botton_III">
                                    ${exe.subjectName}
                                    </span>
                                    <span class="student_information_NO_info_botton_IV"><span>${exe.alreadyFinish}</span>/<span>${exe.totalItem}</span></span>
                                    <span class="student_information_NO_info_botton_V"><span>${exe.time}</span></span>
                                    <span class="student_information_NO_info_botton_VI">
                                    
                                        <c:if test="${exe.state==1}">
                                        <i class="fa fa-play-circle"></i><span onclick="beginAnswer('${exe.idStr}')">&nbsp;再做一遍&nbsp;&nbsp;</span>
                                        </c:if>
                                         <c:if test="${exe.state==0}">
                                        <i class="fa fa-play-circle"></i><span onclick="beginAnswer('${exe.idStr}')">&nbsp;答题</span>
                                        </c:if>
                                        <i class="fa fa-trash"></i><span class="student_delete" onclick="deleteExercise('${exe.idStr}')">&nbsp;删除</span>
                                    </span>
                                </div>
                            </li>
                        </c:forEach> 
                        </ul>
                    </div>
                </div>

              </c:if>
               <!--=================================新建组卷=============================================-->
                <div class="student_information_new_I" style="display:none">
                    <div class="student_information_new_I_top">
                        <span class="information_I">我的练习</span>
                        <span class="information_II">/&nbsp;新建练习</span>
                    </div>
                    <div class="student_information_new_info">
                                                                                        练习结构
                    </div>
                    <div class="student_information_new_middle">
                        <ul>
                        
                        
                             <li>
                                <span>学段</span>
                                <select id="grade_sel" onchange="showSubject()">
                   
                                </select>
                            </li>
                            <li >
                                <span>科目</span>
                                <select id="subject_sel">
                   
                                </select>
                            </li>
                            <li>
                                <span>名称</span>
                                <input id="exe_name">
                            </li>
                            <li>
                            
                            
                            
                                <span>难度</span>
                                
                               <button id="button_1" class="student_information_new_middle_button" onclick="selectLevel1('button_1',1)" >容易</button>
                               <button id="button_2" onclick="selectLevel1('button_2',2)" >较容易</button>
                               <button id="button_3" onclick="selectLevel1('button_3',3)" >中等</button>
                               <button id="button_4" onclick="selectLevel1('button_4',4)" >较难</button>
                               <button id="button_5" onclick="selectLevel1('button_5',5)" >难</button>
                               <button id="button_6" onclick="selectLevel1('button_6',-1)" >不限</button>
                            </li>
                        </ul>
                    </div>
                    <div class="student_information_new_buttom">
                        <span id="new_information" onclick="showKnowledgePoint()">下一步</span>
                    </div>
                </div>
                
                <!--===========================选择知识点===================================-->

                 <div id="KnowledgePoint" class="student_information_new_II" style="display:none;">
                    <div class="student_information_new_I_top">
                        <span class="information_I">我的练习</span>/
                        <span class="information_I">&nbsp;新建练习</span>/
                        <span class="information_II">&nbsp;选择知识点</span>
                    </div>

                    <div class="student_information_xz">
                        选择知识点
                    </div>
                    <div class="student_information_ssd">
                        <table>
                            <tr>
                                <td>
                                    <div class="student_information_ssd_left">
                                        <div  class="student_information_ssd_ul">
                                        
                                        
                                         
                                            <div class="student_information_ssd_left_top">
                                                <div id="sel_kw"></div>
                                            </div>

                                            
                                            
                                            <div class="student_information_ssd_sj"></div>
                                            <ul id="kw_ui">
                                               
                                            </ul>
                                        </div>
                                    </div>
                                </td>
                                <td colspan="3">
                                    <div class="student_information_ssd_right">
                                        <div style="display:none;">
                                            <input id="kwAll" type="checkbox" class="chooseall" onchange="handleAll()" >
                                            <span id="sel_kw_span"></span>
                                        </div>
                                        <div id="kw_list" class="student_information_ssd_right_info">
                                           
                                            
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
                                    <div class="student_information_info_bottom">
                                                                                                                                            已选知识点
                                        <div id="seled_kw" class="student_information_info_bottom_I">
                                           
                                        </div>
                                    </div>

                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="student_information_xz">
                        选择题型
                    </div>
                    <div class="student_information_info_bottom_III">
                        <ul>
                            <li>
                                <div>
                                    <div>选择题</div>
                                    <input  id="c1_count_in" class="student_information_info_bottom_IIII" value="0">
                                    <div class="student_information_info_bottom_V" onclick="increase('c1_count',1)">+</div><br>
                                    <div class="student_information_info_bottom_IV" onclick="increase('c1_count',-1)">-</div>
                                    <div class="student_information_info_bottom_VI">共<span id="c1_count">0</span>题</div>
                                </div>
                            </li>
                            <li>
                                <div>
                                    <div>判断题</div>
                                    <input id="c3_count_in" class="student_information_info_bottom_IIII" value="0">
                                    <div class="student_information_info_bottom_V" onclick="increase('c3_count',1)">+</div><br>
                                    <div class="student_information_info_bottom_IV" onclick="increase('c3_count',-1)">-</div>
                                    <div class="student_information_info_bottom_VI">共<span id="c3_count">0</span>题</div>
                                </div>
                            </li>
                            <li>
                                <div>
                                    <div>填空题</div>
                                    <input id="c4_count_in" class="student_information_info_bottom_IIII" value="0">
                                    <div class="student_information_info_bottom_V" onclick="increase('c4_count',1)">+</div><br>
                                    <div class="student_information_info_bottom_IV" onclick="increase('c4_count',-1)">-</div>
                                    <div class="student_information_info_bottom_VI">共<span id="c4_count">0</span>题</div>
                                </div>
                            </li>
                             <li>
                                <div>
                                    <div>主观题</div>
                                    <input id="c5_count_in" class="student_information_info_bottom_IIII" value="0">
                                    <div class="student_information_info_bottom_V" onclick="increase('c5_count',1)">+</div><br>
                                    <div class="student_information_info_bottom_IV" onclick="increase('c5_count',-1)">-</div>
                                    <div class="student_information_info_bottom_VI">共<span id="c5_count">0</span>题</div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div class="student_information_new_buttom">
                        <span id="new_information_I" onclick="crateExercise()">下一步</span>
                    </div>
                </div>
                
                
                <div id="error_div" style="display:none;">
                          <div class="student-information_new_anlaysis">
                        <ul>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_red" onclick="showErrorItem(1)">
                                    <span class="student-information_new_anlaysis_YW_left">语文</span>
                                    <span id="subject_1" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_blue" onclick="showErrorItem(2)">
                                    <span class="student-information_new_anlaysis_YW_left">数学</span>
                                    <span id="subject_2" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_green" onclick="showErrorItem(3)">
                                    <span class="student-information_new_anlaysis_YW_left">英语</span>
                                    <span id="subject_3" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_orange" onclick="showErrorItem(4)">
                                     <span class="student-information_new_anlaysis_YW_left">物理</span>
                                    <span id="subject_4" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_purple" onclick="showErrorItem(5)">
                                    <span class="student-information_new_anlaysis_YW_left">化学</span>
                                    <span id="subject_5" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            <li>
                                <div class="student-information_new_anlaysis_YW information_yellow" onclick="showErrorItem(6)">
                                     <span class="student-information_new_anlaysis_YW_left">生物</span>
                                    <span id="subject_6" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            
                            
                             <li>
                                <div class="student-information_new_anlaysis_YW information_yellow" onclick="showErrorItem(7)">
                                     <span class="student-information_new_anlaysis_YW_left">地理</span>
                                    <span id="subject_7" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            
                             <li>
                                <div class="student-information_new_anlaysis_YW information_yellow" onclick="showErrorItem(8)">
                                     <span class="student-information_new_anlaysis_YW_left">历史</span>
                                    <span id="subject_8" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                            
                            <li>
                                <div class="student-information_new_anlaysis_YW information_yellow" onclick="showErrorItem(9)">
                                     <span class="student-information_new_anlaysis_YW_left">政治</span>
                                    <span id="subject_9" class="student-information_new_anlaysis_YW_right">0</span>
                                </div>
                            </li>
                        </ul>
                    </div>
               </div>
                
                
                
                
                
                
               <div id="det_error_div" class="student_information_finish_II" style="display:none;">
                <div style="/*display: none*/">
                    <div style="clear: both"></div>
                    <div class="student_information_new_I_top">
                      <!-- 
                        <span class="student_information_CT_top_I">我的练习</span>/
                        <span class="information_I">&nbsp;新建练习</span>
                         -->
                    </div>
                    <div class="student_information_new_CT_zs">
                        <div class="student_information_new_CT_info">
                            知识点
                        </div>
                        <div id="error_kw" class="student_information_new_CT_info_I">
                        
                        </div>
                    </div>
                    <div class="student_information_new_info_II">
                        <ul>
                            <li><span id="error_sort_1" class="student_information_new_info_S" onclick="loadItemDetail(1)">乱序<i class="fa fa-arrow-down"></i></span></li>
                            <li><span id="error_sort_2" class="student_information_new_info_SS" onclick="loadItemDetail(2)">最新错题<i class="fa fa-arrow-down"></i></span></li>
                            <li><span id="error_sort_3" class="student_information_new_info_SS" onclick="loadItemDetail(3)">错次最多<i class="fa fa-arrow-down"></i></span></li>
                        </ul>
                        <span><span class="error_count_total"></span>个结果</span>
                    </div>
                    <div class="student_information_new_info_III">
                        <div class="student_information_new_info_III_top">
                            <div class="student_information_new_III_top_left">
                                <span>题目</span>
                                <span id="error_count_current">1</span>/
                                <span id="error_c_total" class="error_count_total"></span>
                            </div>
                            <div class="student_information_new_III_top_right">
                                <span class="student_information_new_III_I">错误次数：<span id="error_count"></span>次</span>
                                <span>上一次做错：<span id="error_time"></span></span>
                            </div>
                        </div>
                        <div class="student_information_new_V">
                            <dl>
                             
                                <dd>难度：<span id="error_level"></span></dd>
                                <dd>题型：<span id="error_type"></span></dd>
                                <dd>知识点：<span id="error_kwp"></span></dd>
                                <dd>分值：<span id="error_scope"></span></dd>
                            </dl>
                        </div>
                        <div v class="student_information_new_VI" >
                            <span id="tigan"></span>
                        </div>
                        <div v class="student_information_new_VII" id="myAnswer">
                            
                        </div>
                    </div>
                    <div class="student_information_new_xuehui">
                                <span class="student_information_new_VVV" onclick="deleteItem()">我已学会,删除该题</span>
                                <span class="student_information_new_VIII" onclick="nextItemDetail(1)">下一题</span>
                       <%-- <div class="student_information_new_VVV">
                            <span >我已学会</span>
                        </div>
                        <div class="student_information_new_VIII">
                            <span onclick="nextItemDetail(1)">下一题</span>
                        </div>--%>
                    </div>
                    <div class="student_information_new_VV">
                        <span onclick="javascript:jQuery('#answer_div').show();">显示答案</span>
                    </div>
                    <div id="answer_div" class="student_information_new_VVI" style="display:none">
                        <div class="student_information_new_VVI_info">
                            <div class="student_information_new_VVI_answer">
                                <dl>
                                    <dt>答案</dt>
                                    <dd id="answer"></dd>
                                </dl>
                               
                            </div>
                        </div>
                    </div>
                </div>

             </div>
             
             
             
             
            <div id="beginAnswer" style="display:none;">
                    <!--===================选择题======================-->
                    <div class="student_information_start">
                        <div class="student_information_new_I_top">
                        <!-- 
                            <span class="information_I">我的练习</span>
                            <span class="information_II">/&nbsp;新建练习</span>
                             -->
                        </div>
                        <div class="student_information_new_info">
                            <span>题目</span>
                            <span><span id="exe_current_count">1</span>/<span id="exe_unfin_count"></span></span>
                        </div>
                        <div class="student_information_new_V">
                            <dl>
                                <dd>难度：<span id="exe_level"></span></dd>
                                <dd>题型：<span id="exe_type"></span></dd>
                                <dd>知识点：<span id="exe_kwp"></dd>
                                <dd>分值：<span id="exe_scope"></dd>
                                
                            </dl>
                        </div>
                        <div v class="student_information_new_VI">
                            <span id="exe_tigan"></span>
                        </div>
                        
                        <div id="exe_choice_div" style="display:none;">
	                       
	                        <div class="student_information_start_bottom" id="exe_my_answer">
	                            <div onclick="selectAnswer(this,'A')" class="student_information_start_bottom_A"><span>A</span></div>
	                            <div onclick="selectAnswer(this,'B')" class="student_information_start_bottom_A"><span>B</span></div>
	                            <div onclick="selectAnswer(this,'C')" class="student_information_start_bottom_A"><span>C</span></div>
	                            <div onclick="selectAnswer(this,'D')" class="student_information_start_bottom_A"><span>D</span></div>
	                        </div>
	                        <div class="student_information_new_buttom">
	                            <span  onclick="getExeitem(0)">上一题</span>
	                            <span  onclick="getExeitem(1)">下一题</span>
	                            <span id="exercise_submit" onclick="submitExeit()" >提交</span>
	                        </div>
                        </div>
                    </div>
                    <!--==========================问答题================================-->
                    <div id="exe_sub_div" class="student_information_start_I" style="display:none;">
                        <div class="student_information_start_I_bottom">
                            <span>答：</span><textarea id="sub_answer" placeholder="点击输入我的答案"></textarea>
                        </div>
                        <div  class="student_information_start_II_bottom">
                        
                        
                        
                            <span class="student_information_start_II_bottom_left"  onclick="getExeitem(0)">上一题</span>
                            <span class="student_information_start_II_bottom_left" onclick="getExeitem(1)">下一题</span>
                            <span id="exercise_submit1" class="student_information_start_II_bottom_right" onclick="submitExeit()" >提交</span>
                        </div>
                    </div>
                </div>
             
             
             
              <div id="subSuccess_div" style="clear: both;display:none">
                <div class="student_information_all_finish">
                    <div class="student_information_new_I_top">
                    
                      
                        <span class="information_I">我的练习</span>
                        <span class="information_I">/&nbsp;已完成</span>
                        <span class="information_II">/&nbsp;提交完成</span>
                    </div>
                    <div class="student_information_all_finish_I">
                        <div class="student_information_all_finish_II">
                            <i class="fa fa-check-circle"></i><span class="student_information_all_finish_III">提交完成！</span><span>您可以查看自动批改的组卷答案哦！</span>
                        </div>
                    </div>
                    <div class="student_information_new_buttom">
                        <span id="" onclick="viewAnswer()">查看答案</span>
                    </div>
                </div>
            </div>
            
            
             <div id="view_myAnswer" style="display:none;">
                <!--===================选择题======================-->
                <div class="student_information_start">
                    <div class="student_information_new_I_top">
                        <span class="information_I">我的练习</span>
                        <span class="information_I">/&nbsp;已完成</span>
                        <span class="information_II">/&nbsp;查看答案</span>
                    </div>
                    <div class="student_information_check">
                        <div class="student_information_check_left">
                            <dl>
                                <dt>正确率</dt>
                                <dd id="right_v"></dd>
                            </dl>
                        </div>
                        <div class="student_information_check_center">
                            <dl>
                                <dt>
                                    <span>共<span id="total_count"></span>题</span>,<span>答对了<span id="right_count"></span>题!</span>
                                </dt>
                                <dd>
                                    <span>正确率有点低啊！快去多做些练习吧！</span>
                                </dd>
                            </dl>
                        </div>
      
                    </div>
                    <div class="student_information_new_info">
                        <span>题目</span>
                        <span><span id="answer_current">0</span>/<span id="answer_total"></span></span>
                    </div>
                    <div class="student_information_new_V">
                        <dl>
                            <dd>难度：<span id="answer_level"></span></dd>
                            <dd>体型：<span id="answer_type"></span></dd>
                            <dd>知识点：<span id="answer_kw"></span></dd>
                            <dd>分值：<span id="answer_score"></span></dd>
                        </dl>
                    </div>
                    <div id="answer_tigan" v class="student_information_new_VI">
                        <span></span>
                    </div>
                    <div class="student_information_new_VII" id="myanswer_show">
                        <span>A.a</span>B<span></span>C<span></span><span>D</span>
                    </div>
                    <div class="student_information_start_bottom" style="display:none">
                        <div class="student_information_start_bottom_A"><span>A</span></div>
                        <div class="student_information_start_bottom_A"><span>B</span></div>
                        <div class="student_information_start_bottom_A"><span>C</span></div>
                        <div class="student_information_start_bottom_B"><span>D</span></div>
                        <!--===============================答案正确====================================-->
                         <div class="student_information_PD">
                             <!--===========答案错误-->
                             <img id="answer_cuowu" src="/images/student_CW.png" style="display: none">
                             <!--===========答案正确-->
                             <img id="answer_zhengque" src="/images/student_ZQ.png">
                         </div>
                    </div>
                    <div class="student_information_new_buttom">
                        <span id="" onclick="viewAnswer()">下一题</span>
                    </div>
                    <!--============================显示答案================================-->
                    <div class="student_information_new_VVI">
                        <div class="student_information_new_VVI_info">
                            <div class="student_information_new_VVI_answer">
                                <dl>
                                    <dt>答案</dt>
                                    <dd id="right_answer"></dd>
                                </dl>
                               
                            </div>
                        </div>
                    </div>
                </div>
                <!--==========================问答题================================-->
 
            </div>
			
            
            
             
             
             
             
             
             
           
           </div>
        </div>
    </div>
</div>
            
<!-- 页尾 -->
    <%@ include file="../common_new/foot.jsp"%>
</body>
</html>
