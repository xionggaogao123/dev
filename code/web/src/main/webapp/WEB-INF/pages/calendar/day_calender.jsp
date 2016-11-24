<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>k6kt-天历</title>
    <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/calendar/calender2.css">
    <link rel="stylesheet" type="text/css" href="/static/css/activity/css.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/reset.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
	<script type="text/javascript" src="/static/js/calendar/cal2.js"></script>
	<script type="text/javascript">
	
	function getEvent()
	{
		
	  var day=jQuery("#date_span").text();
	  jQuery(".calender_center_info_TBT_RL_IIII").empty();
	  jQuery.ajax({
           type: 'get',
           url: "/calendar/day/data.do",
           data:{"day":day},
           success: function(msg){
                   for(var i=0;i<msg.length;i++)
    			   {
				      var event=msg[i];
					  try
					  {
    					  var html='<div id="'+event.id+'"  class="'+eventClass[event.type]+'" style="'+event.pos+'" onclick="edit(this)">'+event.title+'</div>';
    					  jQuery(".calender_center_info_TBT_RL_IIII").append(html);
						  jQuery("#"+event.id).data("data",event.orgId);
						  jQuery("#"+event.id).data("origBeginTime",event.origBeginTime);
					  }catch(x){}
    			   }
           }
        });
	}
	
	function addDay(d)
    {
	    try
	    {
		    var time=new Date(jQuery("#date_span").text().replace(/-/g,   "/"));
			var date=new Date((time/1000+86400*d)*1000);
			var now_bt=date.pattern("yyyy-MM-dd");
			jQuery("#date_span").text(now_bt);
			jQuery("#real_day1,#real_day2").text(now_bt);
			getEvent(now_bt);
	      }catch(x){alert(x)}
    }
	
	
	 $(document).ready(function(){
	    pageTag=1;
        //getPrivateLetterCount();
		getEvent(jQuery("#date_span").text());
		window.location.hash = "eightclock";
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
 
 
 
 
<div id="content_main_container" style="width: 900px;margin: 0 auto">

    <div id="content_main">
     

       
        
        <div id="right-container"style="width: 770px;float: left;margin-left:10px;margin-top:10px;">
		    <!--=======================头部==============================-->
<div id="content-title">
 <%@ include file="../calendar/oper.jsp" %>
	
<!--
 <input type="hidden" id="dateStr" value="$dateStr"/>
--.
<!--=================================头部日历=====================================-->
<div class="calender_mian">
<ul>
    <li>
        <div class="calender_Z_titile">
            <div class="calender_Z_titile_LT">
                <img src="../images/RL.png">
                <span id="date_span">${dateStr }</span>
                <img style="position: relative;top: 4px;" class="calender_Z_img_I" src="../images/arrow_03.png" onclick="addDay(-1)">
                <img style="position: relative;top: 4px;" class="calender_Z_img_II" src="../images/arrow_05.png" onclick="addDay(1)">
                <!--===============================================新建按钮===============================================-->
                <img style="position: relative;top: 8px" class="calender_Z_img_VV" src="../images/++_03.png"><span class="calender_Z_img_VVI" onclick="showInsert()" >新建</span>
            </div>


            <div class="calender_Z_titile_RT">
                <div class="calender_Z_title_RT_I">
                    <ul>
                        <li style="background-color: #17a3e4" onclick="goPage(0)">
                                                                                    日历
                        </li>
                        <li onclick="goPage(1)">
                                                                                       周历
                        </li>
                        <li onclick="goPage(2)">月历</li>
                        <li onclick="goPage(3)">列表</li>
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

    <div class="calender_center_info_TBT_III">
        <!--======================周一内容============================-->
        <div class="calender_center_info_TBT_Z_I">
            <table>
                <tr>
                    <!--==================================显示内容==================================-->
                    <div class="calender_center_info_TBT_RL_IIII">
						
						
						<!--
                        <div class="calender_center_info_TBT_RL_blue">飞雪连天射白鹿，笑书神侠倚碧鸳</div>
                        <div class="calender_center_info_TBT_RL_red">dddaj垃圾收到了房间爱死了的减肥拉斯的会计法拉克丝的肌肤拉斯克奖地方绿卡时间法拉斯的减肥拉斯科技的放了撒娇浪费空间撒到了开发</div>
                        <div class="calender_center_info_TBT_RL_yellow">ddd</div>
                        <div></div>
						-->
						
                    </div>
					
					
					
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">00:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">01:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">02:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">03:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">04:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">05:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">06:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">07:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
							<a  name="eightclock" >
                           
                            <span class="calender_center_info_RL_ZZZ_III" style="color: #656565">08:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">09:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">10:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">11:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">12:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				
				
                <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">13:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				 <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">14:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				
				 <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">15:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				
				
				 <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">16:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				
				
				 <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">17:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				 <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">18:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				
				 <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">19:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				
				 <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">20:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				
				 <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">21:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				
				
				 <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">22:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				
				 <tr>
                    <td rowspan="2"  class="calender_center_info_TBT_RL_II">
                        <div  class="calender_center_info_RL_ZZZ_II">
                            <span class="calender_center_info_RL_ZZZ_III">23:00</span>
                        </div>
                    </td>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
			
                <tr>
                    <td  class="calender_center_info_TBT_RL_I">
                        <div  class="calender_center_info_RL_ZZZ_I">

                        </div>
                    </td>

                </tr>
				
				
				
            </table>
        </div>

    </div>
</div>
<div style="width: 900px;height: 20px;background-color: #ffffff"></div>
</div>
<div style="clear: both">

</div>


        </div>
			
     </div>

    <!--=================================底部==================================-->
        <%@ include file="../common_new/foot.jsp" %>
<div>
</body>
</html>