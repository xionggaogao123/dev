<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
     <title>复兰科技-试卷预览</title>
     <meta charset="utf-8">
</head>
<body>
<!-- 页头 -->
 
<!-- 页头 -->
<!--======================================生成试卷打印预览=======================================-->
<div>
    <div class="teacher_information_generata_info">

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

		                        <div style="margin: 10px;">
		                            <dl>
		                                <dt><span class="item_index">${status.index+1 }</span> <span>${item.item }</span></dt>
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
		                                <dt><span class="item_index">${status.index+1 }</span> <span>${item.item }</span></dt>
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
		                               <dt><span class="item_index">${status.index+1 }</span> <span>${item.item }</span></dt>
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
		                                <dt><span class="item_index">${status.index+1 }</span> <span>${item.item }</span></dt>
		                            </dl>
		                        </div>
		                    </div>
	                    </c:forEach>
	                    </div>
                   </c:if>
                </div>
            </div>
           
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>