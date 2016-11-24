<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
     <title>复兰科技-练习预览</title>
     <meta charset="utf-8">
    <link rel="stylesheet" type="text/css" href="/static/css/style.css" />
     <link rel="stylesheet" type="text/css" href="/static/css/itempool/teacher_information1.css"/>
     

      <style type="text/css">
   *{
    font-size: 16px;
    font-weight: bold;
    }
   </style>
     <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
     <script type="text/javascript">
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
    	  document.location.href="/testpaper/push/load.do?pid=${dto.id}";
      }
      
      
     </script>
</head>
<body>
<!-- 页头 -->

<!-- 页头 -->
<!--======================================生成试卷打印预览=======================================-->
<div>
    <div class="teacher_information_generata_info">

        
        
         <c:set var="name" value="${dto.name }" />
         <c:set var="schoolName" value="${dto.schoolName }" />
         <c:set var="totalCount" value="${dto.total }" />
         
         
         
         
        <div class="teacher_information_generata_middle">
           
           
            
            <div class="teacher_information_generata_all">

                <!--===========================显示试卷=========================-->
                <div class="teacher_information_generata_right">
                
                 <!-- 选择题 -->
                 <c:if test="${ fn:length(dto.chList) > 0}">
	                    <em>选择题</em>
	                    <div id="ch_div">
	                    <c:forEach items="${dto.chList}" var="item" begin="0"  varStatus="status">
	                    
		                     <div id="item_${item.id }" class="teacher_information_generata_XS">
		                        <div>
		                            <dl>
		                                <dt> <span>${status.index+1 }     ${item.item }</span></dt>
		                                
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
		                        <div>
		                            <dl>
		                                <dt> ${status.index+1 }  <span>${item.item }</span></dt>
		                                
		                                
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
		                        <div>
		                            <dl>
		                                <dt>  ${status.index+1 } <span>${item.item }</span></dt>
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
		                        <div>
		                            <dl>
		                                <dt>  ${status.index+1 }     <span>${item.item }</span></dt>
		                                
		                                
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

        </div>
    </div>
</div>
<!--==========================================弹出框===========================================-->

</body>
</html>