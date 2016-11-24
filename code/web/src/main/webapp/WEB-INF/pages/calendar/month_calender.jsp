<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>k6kt-月历</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/calendar/calender2.css">
    <link rel="stylesheet" type="text/css" href="/static/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/activity/css.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
	<script type="text/javascript" src="/static/js/calendar/cal2.js"></script>
	<script type="text/javascript">
	
	function addMonth(month)
    {
	    try
	    {
		       jQuery(".calender_center_info_YL_II_I").text("");
			   
	           for(var c=0;c<42;c++)
	           {
			      jQuery("#td"+c+" > div > .calender_center_info_YL_IIII").remove();
				  jQuery("#td"+c+" > div > .calender_center_info_YL_VI").remove();
				  jQuery("#td"+c+" > div > .calender_center_info_YL_V").remove();
	              jQuery("#td"+c).data("data","");
	           }
	          var dateStr=jQuery("#dateStr").text()+"-01";
		      var date=new Date(dateStr.replace(/-/g,   "/"));
		      date.setMonth(date.getMonth() + month); // 系统会自动转换
		      var d=date.getFullYear()+"-"+(date.getMonth()+1);
		      jQuery("#dateStr").text(d);
		      var days =getDaysInMonth(date.getMonth()+1,date.getFullYear());
		      var w=getWeek(date);
		      
		      for(var i=0;i<days;i++)
		      {
		        jQuery("#td"+(i+w)+" > .calender_center_info_YL_III > .calender_center_info_YL_II_I").text((i+1));
		        jQuery("#td"+(i+w)).data("data",getCacheDate(date,i));
		      }
			  getEvent();
	      }catch(x){}
    }
    
	function getWeek(date)
    {
      var w=date.getDay();
      if(w==0)
      {
       return 6;
      }else
      {
       return w-1;
      }
    }
	
	function getDaysInMonth(month,year){    
      var days;    
      if (month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12) days=31;    
      else if (month==4 || month==6 || month==9 || month==11) days=30;    
      else if (month==2)
      {    
        if (0==year%4&&((year%100!=0)||(year%400==0)))
        { 
          days=29; 
        }    
        else { 
        days=28;
         }    
      }    
      return (days);
     }
	 
	 
	 function getCacheDate(date,i)
    {
                var day=i+1;
		        if(day<=9)
		        {
		          day="0"+day;
		        }
		        var mon= date.getMonth()+1;
		        if(mon<9)
		        {
		         mon="0"+mon;
		        }
		        return date.getFullYear()+"-"+mon+"-"+day;
    }
	
	
	
	  
     function getEvent()
     {
           jQuery.ajax({
			  url: '/calendar/month/data.do',
			  type: 'get',
			  data:{"date":jQuery("#dateStr").text()+"-01"},
			  contentType: 'application/json',
			  success: function (res)
			  {
			   for(var i=0;i<res.length;i++)
			   {
    			    try
    			    {
    			       var showDate=res[i].time;
    			       var tdTag =getTD(showDate);
					   
					   for(var j=0;j<res[i].list.length;j++)
					   {
					     var html;
						 
						 if(res[i].list.length==2)
						 {
    					     if(j==0)
    						 {
							 
							       
    							    html='<div id="'+res[i].list[j].id+'" class="calender_center_info_YL_V" onclick="edit(this)" style="background-color:'+getColor(res[i].list[j].type)+'">';	
    						 }
    						 if(j==1)
    						 {
    							    html='<div id="'+res[i].list[j].id+'" class="calender_center_info_YL_VI" onclick="edit(this)" style="background-color:'+getColor(res[i].list[j].type)+'">';	
    						 }
						 }
						 else
						 {
						            html='<div id="'+res[i].list[j].id+'" class="calender_center_info_YL_IIII" onclick="edit(this)" style="background-color:'+getColor(res[i].list[j].type)+'">';	
						 }
						 
						 html+='<span>'+res[i].list[j].title+'</span>';
    					 html+='</div>';
						 
						 jQuery("#"+tdTag).find(".calender_center_info_YL_VII").append(html);
						 jQuery("#"+res[i].list[j].id).data("data",res[i].list[j].orgId);
						 jQuery("#"+res[i].list[j].id).data("dateStr",res[i].time);
						 jQuery("#"+res[i].list[j].id).data("origBeginTime",res[i].list[j].origBeginTime);
					   }
					   
					   
					   if(res[i].count>0)
					   {
					    var events='<span class="calender_center_info_YL_II_II">还有'+res[i].count+'个事项</span>';
					    jQuery("#"+tdTag).find(".calender_center_info_YL_III").append(events);
					   }
    			    }catch(x){}
			   }
			  }
			});
     }
	 
	 
	 
	 function getColor(ty)
	 {
	    if(ty==1) return "#FEA517";
	    if(ty==2) return "#3D87C6";
	    if(ty==3) return "#FEA517"; 
	 }

	 
	 function getTD(showDate)
     {
       for(var i=0;i<42;i++)
       {
        if(jQuery("#td"+i).data("data")==showDate)
        {
         return "td"+i;
        }
       }
     }
     
	
	 $(document).ready(function(){
	    pageTag=3;
        getPrivateLetterCount();
		
		try
        {
         addMonth(0);
        }catch(x){}
     });
     
    </script>
</head>
<body>
<!--===========================================新建=====================================================-->
<div id="insertDIV" class="popup_text_T" style="display:none;z-index:999999">
    <div class="popup_text_II"></div>
    <div class="popup_text_XJ" style="position: fixed;left: 50%;top: 50%;margin-left: -275px;margin-top: -275px;">
        <div class="popup_text_I_friest">
            <span id="insertSpan" class="popup_text_II_friest">新建</span>
            <span class="popup_text_II_friestT" onclick="hideAll()">x</span>
        </div>
        <div class="popup_text_XJ_top">
            <input type="radio" id="insert_richeng" name="I" class="popup_top_I" value="2" checked />新建事项
            <input type="radio" id="insert_kecheng" name="I" class="popup_top_II" value="1"   />新建课程
        </div>




        <!--============================新建事项==================================-->
        <div>
            <div class="popup_text_XJ_top_V">
                标题<input id="titleInput" class="popup_text_XJ_top_V_I" type="text">
            </div>
            <div class="popup_text_XJ_top_VI" >
                内容<textarea id="contentArea"></textarea>
            </div>
        </div>

        <!--==========================新建课程==============================================-->
        <div class="popup_text_XJ_top_VII">
            开始时间<input id="bTime" class="popup_text_XJ_top_VII_I" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">&nbsp;&nbsp;&nbsp;&nbsp;
            结束时间<input id="eTime" class="popup_text_XJ_top_VII_II" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})">
        </div>
        <div class="popup_text_XJ_VIII">
            重复
            <select id="lpSelect" onchange="showLp()">
                <option value="-1">不重复</option>
                <option value="1">每天</option>
                <option value="2">每周</option>
                <option value="3">每月</option>
                <option value="4">每年</option>
            </select>
        </div>
        <!--==========================每年每月每周==============================================-->
        <div id="otherDIV" class="popup_text_XJ_YE"  style="display: none">
            <input id="dayInput1"  class="popup_text_XJ_YE_IIII" type="radio" name="year" value="0">一直持续
            <input id="dayInput2" class="popup_text_XJ_YE_II" type="radio" name="year" value="1" checked >发生<input class="popup_text_XJ_YE_I" id="otherCishu">次后结束
            <input id="dayInput3" class="popup_text_XJ_YE_III" type="radio" name="year" value="2">于
            <input  type="text" id="otherEndInput" style="border:1px solid #C3C3C3;width:120px;height:30px;" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"/>
        </div>
        <!--================================每日===================================================-->
        <div id="dayDIV" style="display: none" >
            <div class="popup_text_XJ_DY" >
                <input id="dayRepeat1" class="popup_text_XJ_DY_III" type="radio" name="day" value="1" checked >每<input id="dayTypeInput" class="popup_text_XJ_DY_I">天重复一次
                <input id="dayRepeat2" class="popup_text_XJ_DY_II" type="radio" name="day" value="0">每个工作日
            </div>
            <div class="popup_text_XJ_JS" >结束</div>
            <div class="popup_text_XJ_DY_V"  >
                <input id="otherRepeat1"  class="popup_text_XJ_DY_V_I" type="radio" name="V" value="0" >一直持续
                <input id="otherRepeat2"  class="popup_text_XJ_DY_V_II" type="radio" name="V" value="1" checked >发生<input id="dayCishu"  class="popup_text_XJ_DY_V_III" type="text">次后
                <input id="otherRepeat3"  class="popup_text_XJ_DY_V_IIII" type="radio" name="V" value="2">于

                <input  type="text" id="dayEndInput" style="border:1px solid #C3C3C3;width:120px;height:30px;" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"/>
            </div>
        </div>

        <div class="popup_text_XJ_bottom">
            <span id="deleteBtn" class="popup_text_bottom_I" onclick="showDelete()">删除</span>
            <span id="cancelBtn" class="popup_text_bottom_I" onclick="hideAll()">取消</span>
            <span class="popup_text_botton_II" onclick="insert()">保存</span>
        </div>
    </div>
</div>
<!--=====================================删除课程/日程=======================================-->
<div id="deleteDIV" class="popup_text_T" style="display:none;z-index:999999">
    <div id="popup_text_III"></div>

    <div id="popup_text_info_III" >
        <div class="popup_text_I_friest">
            <span id="real_d_text" class="popup_text_II_friest">删除</span>
        </div>
        <div class="popup_S_I">
                <span id="deleteSapn1">
                    <input id="singleDelete" class="popup_S_V_I" type="radio" name="VV" value="0">
                    <span class="popup_S_V_I_I">您确定要删除<span id="real_day1">2012-12-12</span><span class="popup_SS_II">语文课</span><span class="popup_S_V_I_II"></span><span>吗</span></span>
                </span>
            <br>
                <span id="deleteSapn2">
                    <input id="multiDelete" class="popup_S_V_II" type="radio" name="VV" value="1">
                     <span class="popup_S_V_II_I">您确定要删除<span id="real_day2">2012-12-12</span>当天及后面所有的<span class="popup_SS_II">语文课</span><span class="popup_S_V_I_II"></span><span>吗</span></span>
                </span>
        </div>
        <div class="popup_text_S_BC_I">
            <div class="popup_text_BC_I" onclick="delOper()">确定</div>
            <div class="popup_text_BC_II" onclick="hideAll()">取消</div>
        </div>
    </div>
</div>
</div>


<%@ include file="../common_new/head.jsp" %>
<div id="content_main_container"style="width: 900px;margin: 0 auto">
    <div id="content_main">
     

  
        
        <div id="right-container"style="width: 770px;float: left;margin-left:10px;margin-top:10px;">
		      <div id="content">



<!--=======================头部==============================-->
<div id="content-title">
<%@ include file="../calendar/oper.jsp" %>
<input type="hidden" id="date_span"/>
<!--============================头部================================-->
<div class="calender_mian">
<ul>
    <li>
        <div class="calender_Z_titile">
            <div class="calender_Z_titile_LT">
                <img src="../images/RL.png">
                <span id="dateStr">${dateStr }</span>
			    
                <img style="position: relative;top: 4px;" class="calender_Z_img_I" src="../images/arrow_03.png" onclick="addMonth(-1)"/>
                <img style="position: relative;top: 4px;" class="calender_Z_img_II" src="../images/arrow_05.png" onclick="addMonth(1)"/>
				<img style="position: relative;top: 8px"  class="calender_Z_img_VV" src="../images/++_03.png"><span class="calender_Z_img_VVI" onclick="showInsert()" >新建</span>
            </div>
            <div class="calender_Z_titile_RT">
                <div class="calender_Z_title_RT_I">
                    <ul>
						 <li onclick="goPage(0)">
                                                                                                                日历
                         </li>
                         <li onclick="goPage(1)">
                                                                                                                周历
                         </li>
                        <li style="background-color: #17a3e4"  onclick="goPage(2)">月历</li>
                        <li  onclick="goPage(3)">列表</li>
                    </ul>
                </div>
                <select id="typeSelect" onchange="select()">
                    <option value="0">全部</option>
                    <option value="3">通知</option>
                    <option value="1">课程</option>
                    <option value="2">事项</option>
                </select>
            </div>
        </div>
    </li>
    </ul>
<!--=========================周一到周日内容================================-->
<div class="calender_center_info_text">
    <table class="calender_center_info_TB_I">
        <tr>
            <td class="calender_center_info_TB_II">周一</td>
            <td class="calender_center_info_TB_II">周二</td>
            <td class="calender_center_info_TB_II">周三</td>
            <td class="calender_center_info_TB_II">周四</td>
            <td class="calender_center_info_TB_II">周五</td>
            <td class="calender_center_info_TB_II">周六</td>
            <td class="calender_center_info_TB_II">周日</td>
        </tr>
        <!--================================日期行=======================================-->
        <tr class="calender_center_info_YL_I">
            <td  id="td0" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td  id="td1" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td2" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td3" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td4" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td5" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td6" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
        </tr>

        <tr class="calender_center_info_YL_I">
            <td id="td7" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td8" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td9" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td10" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td11" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td12" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td13" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
        </tr>
        <tr class="calender_center_info_YL_I">
            <td id="td14" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
                <!--=============================只有一件事=============================================-->
          

            </td>
            <td id="td15" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
                <!--=============================只有2件事=============================================-->
            </td>
            <td id="td16" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
                <div class="calender_center_info_YL_VII"> </div>

            </td>
            <td id="td17" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td18" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td19" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
           

            </td>
            <td id="td20" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				<div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
            </td>
        </tr>
        <tr class="calender_center_info_YL_I">
            <td id="td21" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
                <!--=============================只有一件事=============================================-->
               <div class="calender_center_info_YL_VII"> </div>

            </td>
            <td id="td22" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
                <div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td23" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
                <!--=============================没有事情=============================================-->
                 <div class="calender_center_info_YL_VII"> </div>
            </td>
            <td id="td24" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
                 <div class="calender_center_info_YL_VII"> </div>

            </td>
            <td id="td25" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
                <!--=============================没有事情=============================================-->
                 <div class="calender_center_info_YL_VII"> </div>
            </td>
            <td  id="td26" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
            </td>
            <td id="td27" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
            </td>
        </tr>
        <tr class="calender_center_info_YL_I">
            <td id="td28" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================只有一件事=============================================-->
            </td>
            <td id="td29" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================只有2件事=============================================-->
            </td>
            <td id="td30" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
            </td>
            <td  id="td31" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
            </td>
            <td id="td32" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
             

            </td>
            <td id="td33" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
            

            </td>
            <td id="td34" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
             

            </td>
        </tr>
        <tr  class="calender_center_info_YL_I">
            <td id="td35" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================只有一件事=============================================-->
           

            </td>
            <td id="td36" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================只有2件事=============================================-->

            </td>
            <td id="td37" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
               

            </td>
            <td id="td38"  class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
              

            </td>
            <td id="td39" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
              

            </td>
            <td id="td40" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
             

            </td>
            <td id="td41" class="calender_center_info_YL_II">
                <div class="calender_center_info_YL_III">
                    <span class="calender_center_info_YL_II_I"></span>
                </div>
				 <div class="calender_center_info_YL_VII"> </div>
                <!--=============================没有事情=============================================-->
            

            </td>
        </tr>
    </table>
    <div style="width: 0px;height: 0px;background-color: #EBEBEB"></div>
</div>
</div>

</div>





        </div>
			
     </div>
       </div>
       <div style="clear: both"></div>


       <!--=================================底部==================================-->
         <%@ include file="../common_new/foot.jsp" %>
<div>
	
	
	
	
	
	
	
	
	
<!--=======================头部==============================-->

</body>
</html>