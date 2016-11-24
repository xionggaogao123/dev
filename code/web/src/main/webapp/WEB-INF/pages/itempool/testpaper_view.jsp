<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
     <title>复兰科技-试卷预览</title>
     <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
     <link rel="stylesheet" type="text/css" href="/static/css/itempool/teacher_information.css"/>
     <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
     <script type="text/javascript">
         var type=${param.type};

         $(document).ready(function() {
             if(0==type){
                 $('.teacher_information_generata_XS').on({
                     mouseenter:function(){
                         $(this).find('.edit').show();
                     },
                     mouseleave:function(){
                         $(this).find('.edit').hide();
                     }
                 })

             } else {
                 $('.teacher_information_right_VVV, .only').hide();
             }

         });
      function showParse(id)
      {
    	  jQuery("#p_"+id).toggle();
      }
      
      function deleteItem(pid,ty,tid)
      {
    	  $.ajax({
              url: '/testpaper/item/delete.do?pid='+pid+"&type="+ty+"&tid="+tid,
              type: 'get',
              contentType: 'application/json',
              success: function (res) {
                if(res.code=="200")
             	{
                	  var tot=parseInt(jQuery("#totalSpan").text());
                	  jQuery("#totalSpan").text(tot-1);
             	      jQuery("#item_"+tid).remove();
             	      inCreaseSelectItem(ty);
             	}
              }
          });
      }
      
      function changeItemPos(pid,ty,tid,pos)
      {
    	  $.ajax({
              url: '/testpaper/item/pos/update.do?pid='+pid+"&type="+ty+"&tid="+tid+"&pos="+pos,
              type: 'get',
              contentType: 'application/json',
              success: function (res) {
                if(res.code=="200")
             	{
                	var $obj=jQuery("#ch_div");
                	if(ty==3) $obj=jQuery("#tf_div");
                	if(ty==4) $obj=jQuery("#gap_div");
                	if(ty==5) $obj=jQuery("#sub_div");
                	
                	var idArr=[];
                	
                	jQuery.each($obj.children("div"),function(){
                		idArr.push(jQuery(this).attr("id"));
                	});
                	
                	var index=jQuery.inArray("item_"+tid,idArr);
                	
                	if(index!=-1)
                	{
		                	if(pos==1 && index!=0)
		                	{
		                		jQuery("#item_"+tid).after(jQuery("#"+idArr[index-1]));
		                	}
		                	if(pos==0 && index!=idArr.length-1)
		                	{
		                		jQuery("#"+idArr[index+1]).after(jQuery("#item_"+tid));
		                	}
		                	
		                	var type="ch_div";
		                	if(ty==3) type="tf_div";
		                	if(ty==4) type="gap_div";
		                	if(ty==5) type="sub_div";
		                	
		                	jQuery("#"+type).children("div").each(function(i){
		                		jQuery(this).find(".item_index").text(i+1);
		                	});
                	}
             	}
              }
          });
      }
      
      
      function inCreaseSelectItem(ty)
      {
	   	   var arr=  jQuery("#select_"+ty).find("span").toArray();
	   	   if(arr)
	   	   {
	   		jQuery("#select_"+ty +" span:eq("+(arr.length-1)+")").remove();
	   	   }
      }
      
      function reset()
      {
    	  document.location.href="/testpaper/reset.do?pid=${dto.id}";
      }
      
      
      function save()
      {
    	  jQuery("#alert_total").text(jQuery("#totalSpan").text());
    	  jQuery("#alert_div").show();
      }
      
      
      function sureSave()
      {
    	  document.location.href="/testpaper/list.do";
      }
      
      
      function loadPush()
      {
    	  document.location.href="/testpaper/push/load.do?pid=${dto.id}&type=" + type;
      }
      
      
     </script>
</head>
<body>
<!-- 页头 -->
 
<!-- 页头 -->
<!--======================================生成试卷打印预览=======================================-->
<div>
    <div class="teacher_information_generata_info">

        <%--<div class="teacher_information_generata">
            <span class="teacher_information_generata_I_left">退出打印</span>
            <span>打印预览</span>
        </div>
        <!-- <div class="teacher_information_generata_top">
             <div class="teacher_information_generata_top_info_I">
                 <span class="teacher_information_generata_top_info_II">B5</span>
             </div>
             <div>
                 <span>A4</span>
             </div>
             <div>
                 <span>横版</span>
                 <span>B4</span>
             </div>
             <div>
                 <span>横版</span>
                 
                 
                 <span>A3</span>
             </div>
             <div class="teacher_information_generata_top_right">
                      <span>确定打印试卷</span>
             </div>
         </div>-->
        <div style="clear: both"></div>--%>
        
        
         <c:set var="name" value="${dto.name }" />
         <c:set var="schoolName" value="${dto.schoolName }" />
         <c:set var="totalCount" value="${dto.total }" />
         
         
         
         
        <div class="teacher_information_generata_middle">
            <div class="teacher_information_generata_middle_SJ">
                <div>${name}</div>
                
                <!-- <span>时间：</span><span>90分钟</span>-->
            </div>
            <div class="teacher_information_generata_middle_SX">

                <dl>
                    <dt>注意事项：</dt>
                    <dd>本卷共有<span id="totalSpan">${totalCount}</span>题</dd>
                </dl>

                
                <!-- <span>时间：</span><span>90分钟</span>-->
            </div>
           
            
            <div class="teacher_information_generata_all">
                <div class="teacher_information_generata_middle_MC">
                    <ul>
                        <li>
                            <span>学校：</span>
                            <input value="${schoolName}">
                        </li>
                        <li>
                            <span>班级：</span>
                            <input>
                        </li>
                        <li>
                            <span>姓名：</span>
                            <input>
                        </li>
                    </ul>
                </div>
                <!--===========================显示试卷=========================-->
                <div class="teacher_information_generata_right">
                
                 <!-- 选择题 -->
                 <c:if test="${ fn:length(dto.chList) > 0}">
	                    <em>选择题</em>
	                    <div id="ch_div">
	                    <c:forEach items="${dto.chList}" var="item" begin="0"  varStatus="status">
	                    
		                     <div id="item_${item.id }" class="teacher_information_generata_XS">
		                        <span class="edit" onclick="changeItemPos('${dto.id}',1,'${item.id }',0)">下移</span>
                                 <span class="edit" onclick="changeItemPos('${dto.id}',1,'${item.id }',1)">上移</span>
                                 <span class="edit" onclick="deleteItem('${dto.id}',1,'${item.id }')">删除</span>
                                 <span class="edit" onclick="showParse('${item.id }')">查看答案</span>
		                        <div>
		                            <dl>
		                                <dt><span class="item_index">${status.index+1 }</span> <span>${item.item }</span></dt>
		                                
		                                <c:if test="${!empty item.image}">
		                                  <dd><img src="/images/activityDef0.jpg" width="300px"></dd>
		                                </c:if>
		                                
		                                <div id="p_${item.id }" style="display:none">
		                                
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">知识点</span><span class="teacher_jiexi_ZT">${item.kw }</span>
				                                </div>
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">试卷解析</span><div class="teacher_jiexi_ZT_I">${item.parse }</div>
				                                </div>
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">答案</span><div class="teacher_jiexi_ZT_I">${item.answer }</div>
				                                </div>
		                                
		                                </div>
		                            </dl>
		                        </div>
		                    </div>
	                    </c:forEach>
	                    </div>
                   </c:if>
                   
                 <!-- 选择题 -->
                 <c:if test="${ fn:length(dto.tfList) > 0}">
	                    <em>判断题</em>
	                    <div id="tf_div">
	                    <c:forEach items="${dto.tfList}" var="item"  begin="0"  varStatus="status">
	                    
		                     <div id="item_${item.id }" class="teacher_information_generata_XS">
		                        <span class="edit" onclick="changeItemPos('${dto.id}',3,'${item.id }',0)">下移</span>
                                 <span class="edit" onclick="changeItemPos('${dto.id}',3,'${item.id }',1)">上移</span>
                                 <span class="edit" onclick="deleteItem('${dto.id}',3,'${item.id }')">删除</span>
                                 <span class="edit" onclick="showParse('${item.id }')">查看答案</span>
		                        <div>
		                            <dl>
		                                <dt><span class="item_index">${status.index+1 }</span> <span>${item.item }</span></dt>
		                                
		                                
		                                <c:if test="${!empty item.image}">
		                                  <dd><img src="/images/activityDef0.jpg" width="300px"></dd>
		                                </c:if>
		                                
		                             
		                                <div id="p_${item.id}" style="display:none">
		                                
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">知识点</span><span class="teacher_jiexi_ZT">${item.kw }</span>
				                                </div>
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">试卷解析</span><div class="teacher_jiexi_ZT_I">${item.parse }</div>
				                                </div>
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">答案</span><div class="teacher_jiexi_ZT_I">${item.answer }</div>
				                                </div>
		                                
		                                </div>
		                            </dl>
		                        </div>
		                    </div>
	                    </c:forEach>
	                    </div>
                   </c:if>
                   
                 <!-- 选择题 -->
                 <c:if test="${ fn:length(dto.gapList) > 0}">
	                    <em>填空题</em>
	                    
	                    <div id="gap_div">
	                    <c:forEach items="${dto.gapList}" var="item"  begin="0"  varStatus="status">
	                    
		                     <div id="item_${item.id }" class="teacher_information_generata_XS">
		                         <span class="edit" onclick="changeItemPos('${dto.id}',4,'${item.id }',0)">下移</span>
                                 <span class="edit" onclick="changeItemPos('${dto.id}',4,'${item.id }',1)">上移</span>
                                 <span class="edit" onclick="deleteItem('${dto.id}',4,'${item.id }')">删除</span>
                                 <span class="edit" onclick="showParse('${item.id }')">查看答案</span>
		                        <div>
		                            <dl>
		                               <dt><span class="item_index">${status.index+1 }</span> <span>${item.item }</span></dt>
		                                <c:if test="${!empty item.image}">
		                                  <dd><img src="/images/activityDef0.jpg" width="300px"></dd>
		                                </c:if>
		                                <div id="p_${item.id }" style="display:none">
		                                
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">知识点</span><span class="teacher_jiexi_ZT">${item.kw }</span>
				                                </div>
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">试卷解析</span><div class="teacher_jiexi_ZT_I">${item.parse }</div>
				                                </div>
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">答案</span><div class="teacher_jiexi_ZT_I">${item.answer }</div>
				                                </div>
		                                
		                                </div>
		                            </dl>
		                        </div>
		                    </div>
	                    </c:forEach>
	                    </div>
                   </c:if>
                  
                  
                 <!-- 主观题 -->
                 <c:if test="${ fn:length(dto.subList) > 0}">
	                    <em>主观题</em>
	                    <div id="sub_div">
	                    <c:forEach items="${dto.subList}" var="item" begin="0"  varStatus="status">
	                    
		                     <div id="item_${item.id }" class="teacher_information_generata_XS">
		                        <span class="edit" onclick="changeItemPos('${dto.id}',5,'${item.id }',0)">下移</span>
                                 <span class="edit" onclick="changeItemPos('${dto.id}',5,'${item.id }',1)">上移</span>
                                 <span class="edit" onclick="deleteItem('${dto.id}',5,'${item.id }')">删除</span>
                                 <span class="edit" onclick="showParse('${item.id }')">查看答案</span>
		                        <div>
		                            <dl>
		                                <dt><span class="item_index">${status.index+1 }</span> <span>${item.item }</span></dt>
		                                
		                                
		                                <c:if test="${!empty item.image}">
		                                  <dd><img src="/images/activityDef0.jpg" width="300px"></dd>
		                                </c:if>
		                                
		                                <div id="p_${item.id }" style="display:none">
		                                
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">知识点</span><span class="teacher_jiexi_ZT">${item.kw }</span>
				                                </div>
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">试卷解析</span><div class="teacher_jiexi_ZT_I">${item.parse }</div>
				                                </div>
				                                <hr style="border-top:1px dashed #cccccc;height: 1px;overflow:hidden;">
				                                <div>
				                                    <span class="teacher_jiexi">答案</span><div class="teacher_jiexi_ZT_I">${item.answer }</div>
				                                </div>
		                                </div>
		                            </dl>
		                        </div>
		                    </div>
	                    </c:forEach>
	                    </div>
                   </c:if>
                </div>
            </div>
            <!--=============================导航================================-->
            <div class="teacher_information_SC_TV">
                <div class="teacher_information_right_VV">
                    <div class="teacher_information_right_VVV" onclick="reset()">
                        <img src="/images/student_DT.png">
                        <span>再选点题</span>
                    </div>
                    <div class="teacher_information_right_V_top">
                        <span>题目卡</span>
                    </div>
                    <div class="teacher_information_right_V_info">


                        <c:if test="${ fn:length(dto.chList) > 0}">
                            <span>1.选择题</span>
                            <div id="select_1" class="teacher_information_right_VV_info">

                                <c:forEach items="${dto.chList}" var="item" begin="0"  varStatus="status">
                                    <span>${status.index+1 }</span>
                                </c:forEach>
                            </div>
                        </c:if>

                        <c:if test="${ fn:length(dto.tfList) > 0}">
                            <span>2.判断题</span>
                            <div id="select_3" class="teacher_information_right_VV_info">

                                <c:forEach items="${dto.tfList}" var="item" begin="0"  varStatus="status">
                                    <span>${status.index+1 }</span>
                                </c:forEach>
                            </div>
                        </c:if>


                        <c:if test="${ fn:length(dto.gapList) > 0}">
                            <span>3.填空题</span>
                            <div id="select_4" class="teacher_information_right_VV_info">

                                <c:forEach items="${dto.gapList}" var="item" begin="0"  varStatus="status">
                                    <span>${status.index+1 }</span>
                                </c:forEach>
                            </div>
                        </c:if>

                        <c:if test="${ fn:length(dto.subList) > 0}">
                            <span>4.主观题</span>
                            <div id="select_5" class="teacher_information_right_VV_info">

                                <c:forEach items="${dto.subList}" var="item" begin="0"  varStatus="status">
                                    <span>${status.index+1 }</span>
                                </c:forEach>
                            </div>
                        </c:if>

                        <button class="teacher_information_right_SC_bottom only" onclick="save()">保存</button>
                        <button class="teacher_information_right_SC_bottom" onclick="loadPush()">推送</button>


                       <!--=================================导出============================-->
                        <a class="teacher_information_right_SC_bottom" href="/testpaper/download/pdf.do?pid=${dto.id}">导出PDF</a>
                         <a class="teacher_information_right_SC_bottom" href="/testpaper/download/pdf.do?pid=${dto.id}&type=2">导出WORD</a>
                  
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--==========================================弹出框===========================================-->
<div id="alert_div" style="display: none">
    <div class="teacher_information_open">
    </div>
    <div class="teacher_information_open_bc">
        <div class="teacher_information_open_top">
            <span onclick="javascript:jQuery('#alert_div').hide();">x</span>
        </div>
        <div class="teacher_information_open_middle">
            <dl>
                <dt>注意事项</dt>
                <dd>
                    <span>共<span id="alert_total"></span>题</span>
                </dd>
            </dl>
        </div>
        
        <div style="height: 80px;width: 210px;margin: 10px auto">
            <span style="display: inline-block;width: 205px;height: 35px;background: #5cb95b;border: 1px solid #3ea93c;color: white;text-align: center;line-height: 35px;cursor: pointer" onclick="sureSave()">确定</span>
            
        </div>
        
        
        
    </div>
</div>
</body>
</html>