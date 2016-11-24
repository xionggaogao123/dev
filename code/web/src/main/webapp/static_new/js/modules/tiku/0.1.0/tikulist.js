/**
 * Created by liwei on 15/9/06.
 */
/**
 * @author 李伟
 * @module  学籍管理操作
 * @description
 * 学籍管理操作
 */
/* global Config */
define(['common','pagination'],function(require,exports,module){
    
  
    /**
     *初始化参数
     */
    var ziyuanGit = {},
        Common = require('common');
    			require('pagination');
    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }
    
    /**
	 * 公共变量
	 */
	var allOptionHtml = "<option value='ALL'>全部</option>";
	var PUBLIC_PAGE_SIZE = 10;
	var public_target_id = "";
    
    var versionSelectionHTML = '<dd class="versionSelection">'+
    '                <span>'+
    '                    <label>学段</label>'+
    '                    <select id="versionTermTypeSelection" subId="versionSubjectSelection" typeInt="1">'+
    '                    </select>'+
    '                </span>'+
    '                <span>'+
    '                    <label>学科</label>'+
    '                    <select id="versionSubjectSelection" subId="bookVertionSelection" typeInt="2">'+
    '                    </select>'+
    '                </span>'+
    '                <span>'+
    '                    <label>教材版本</label>'+
    '                    <select id="bookVertionSelection" subId="gradeSelection" typeInt="3">'+
    '                    </select>'+
    '                </span>'+
    '                <span>'+
    '                    <label>年级/课本</label>'+
    '                    <select id="gradeSelection" subId="chapterSelection" typeInt="4">'+
    '                    </select>'+
    '                </span>'+
    '            </dd>'+
    '            <dd class="versionSelection">'+
    '                <span>'+
    '                    <label>章目录</label>'+
    '                    <select id="chapterSelection" subId="partSelection" typeInt="5">'+
    '                    </select>'+
    '                </span>'+
    '                <span>'+
    '                    <label>节目录</label>'+
    '                    <select id="partSelection" subId="0" typeInt="6">'+
    '                    </select>'+
    '                </span>'+
    '                <button type="button" class="zy-gray-btn search-btn">搜 索</button>'+
    '            </dd>';
    
    var knowledgeSelectionHTML = '<dd class="knowledgeSelection">'+
    '                <span>'+
    '                    <label>学段</label>'+
    '                    <select id="knowledgeTermtypeSelection" subId="knowledgeSubjectSelection" typeInt="1">'+
    '                    </select>'+
    '                </span>'+
    '                <span>'+
    '                    <label>学科</label>'+
    '                    <select id="knowledgeSubjectSelection" subId="knowledgeAreaSelection" typeInt="2">'+
    '                    </select>'+
    '                </span>'+
    '                <span>'+
    '                    <label>知识面</label>'+
    '                    <select id="knowledgeAreaSelection" subId="knowledgePointSelection" typeInt="7">'+
    '                    </select>'+
    '                </span>'+
    '                <span>'+
    '                    <label>知识点</label>'+
    '                    <select id="knowledgePointSelection" subId="0" typeInt="8">'+
    '                    </select>'+
    '                </span>'+
    '                <button type="button" class="zy-gray-btn search-btn">搜 索</button>'+
    '            </dd>';
    
    
    
    /**
     * @func dialog
     * @desc 页面初始化
     * @example
     * ziyuan.init()
     */
    ziyuanGit.dialog = function(html,suCallback,erCallback,isShowBtn){
        Showbo.Msg.defaultWidth = 640;
        Showbo.Msg.className = 'dvMsgBox dialogAsk';
        var alertHtml;
        if(isShowBtn){
        	alertHtml = '<div id="dialogAsk"><div class="ask-btn"><button type="button" class="cur">同意</button><button type="button">拒绝</button></div><div class="ask-info">'+html+'</div></div>';
        }else{
        	alertHtml = '<div id="dialogAsk"><div class="ask-btn"></div><div class="ask-info">'+html+'</div></div>';
        }
        
        
        Showbo.Msg.alert(alertHtml,'查看题目');
        $('#dvMsgBtns input').val('关闭');
        $('#dvMsgBox').find('.top').css('margin-bottom','10px').next('.body').height(350);
        $('.ask-btn button').on('click',function(){
            $(this).hasClass('cur') ? suCallback && suCallback() : erCallback && erCallback();
            Showbo.Msg.hide();
        });


    }
    
    function suCallback(){
    	$.ajax({
    		type: "POST",
            url: "/itemstore/updateStatus1.do",  
            data: {id : public_target_id},
            dataType: "json",
            success: function(data){
            	if(data.code==200){
            		window.location.href="/itemstore/list.do";
            	}
            	
            }
    	});
    }
    function erCallback(){
    	$.ajax({
    		type: "POST",
            url: "/itemstore/updateStatus2.do",  
            data: {id : public_target_id},
            dataType: "json",
            success: function(data){
            	if(data.code==200){
            		window.location.href="/itemstore/list.do";
            	}
            	
            }
    	});
    }
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * ziyuanGit.init()
     */
    ziyuanGit.init = function(){


        
        //弹窗显示详情
      //题库查看

        
        	
        $('body').on('click','.look-btn',function(){
        	public_target_id = $(this).closest('tr').attr('flag');
        	var statusStr = $($(this).parent().prev().find('span').get(1)).html();
        	var isShowBtn = false;
        	if(statusStr.indexOf("待审核")>-1){
        		isShowBtn = true;
        	}
        	
        	$.ajax({
        		type: "POST",
                url: "/itemstore/queryOneItem.do",  
                data: {id : public_target_id},
                dataType: "json",
                success: function(data){
                	var aa=data.qt;
                	var an=data.answer;
                	var html= '';
                	html = html + '<p>【教材版本】</p>';
                	for(var i = 0;i<data.verList.length;i++){
                		html = html + '<p class="pStyle">' + data.verList[i] + '</p>';
                	}
                	html = html + '<p>【知识点】</p>';
                	for(var i = 0;i<data.verList.length;i++){
                		html = html + '<p class="pStyle">' + data.knoList[i] + '</p>';
                	}
                	html = html + '<p>【题目内容】</p>';
                	html = html + '' + data.content + '';
                	html = html + '<p>【答案】</p>';
                	
                	if("选择题"==aa){
                	for(var i=0;i<an.length;i++){
                		
                		html = html + '<p>' + an[i] + '</p> ';
                	}
                	
                	}
                	else if("填空题"==aa){
                		
                		for(var i=0;i<an.length;i++){
                    		
                    		html = html + '<p>' + an[i] + '</p> ';
                		}
                	}else{
                		html = html + '<p>' +data.answer  + '</p> ';
                	}
                
                	html = html + '<p>【解析】</p>';
                	html = html + '' + data.why + '';
                	ziyuanGit.dialog(html,suCallback,erCallback,isShowBtn);
                }
        	});
        	
        })
        
        
        
        
        /**
         * 下拉菜单相关
         */
        $('#propertyType').on('change',function(){
        	$('.versionSelection').remove();
        	$('.knowledgeSelection').remove();
        	if($(this).val() == "tcv"){
        		$("#selectionArea").append(versionSelectionHTML);
        		queryAndeFillSelection("versionTermTypeSelection","ALL");
        	}else{
        		$("#selectionArea").append(knowledgeSelectionHTML);
        		queryAndeFillSelection("knowledgeTermtypeSelection","ALL");
        	}
        	queryObjCatch = null;
        	queryList(1);
        });
      //下拉菜单查询填充方法
        function queryAndeFillSelection(selectionId,parentObjId){
        	if(selectionId == "0"){
        		return;
        	}
        	var $selection = $("#" + selectionId);
        	if(parentObjId == "ALL" && selectionId != "versionTermTypeSelection" && selectionId !="knowledgeTermtypeSelection"){
        		if(selectionId != "0"){
            		resetLittleBrsrothe(selectionId);
            	}
        		return;
        	}
        	var querySelectionObj = {};
        	querySelectionObj.type = $selection.attr("typeInt");
        	querySelectionObj.parentId = parentObjId;
        	$.ajax({
        		type: "POST",
                url: "/resourceDictionary/querySubResourcesForManager.do",  
                data: querySelectionObj,
                dataType: "json",
                success: function(data){
                	fillSelection(selectionId,data.datas);
                	if($selection.attr("subId") != "0"){
                		resetLittleBrothers($selection.attr("subId"));
                	}
                }
        	});
        }
        
        //填充下拉菜单的方法
        function fillSelection(selectionId,srcDatas){
        	var targetSelection = $("#" + selectionId);
        	targetSelection.html(allOptionHtml);
        	for(var i=0;i<srcDatas.length;i++){
        		var subData = srcDatas[i];
        		targetSelection.append("<option value='" + subData.id + "'>" + subData.name + "</option>");
        	}
        }
       //置空弟弟们
        function resetLittleBrothers(targetId){
        	if(targetId == "0"){
        		return;
        	}else{
        		var $targetSelection = $("#" + targetId);
        		$targetSelection.html(allOptionHtml);
        		resetLittleBrothers($targetSelection.attr("subId"));
        	}
        }
        //给所有资源字典类型选择下拉菜单增加事件 
        $('body').on('change','.versionSelection select,.knowledgeSelection select',function(){
        	queryAndeFillSelection($(this).attr("subId"),$(this).val());
        });
        
        //初始化一下最开始的下拉
        $("#selectionArea").append(versionSelectionHTML);
        queryAndeFillSelection("versionTermTypeSelection","ALL");

        /**
         * 查询填充列表相关
         */
        var isInit = true;
        var queryObjCatch;
        $('body').on('click','.search-btn',function(){
        	queryObjCatch = null;
        	queryList(1);
        });
        
     
        function queryList(pageNo){
        	if(queryObjCatch == null){
        		queryObjCatch = {};
        		var propertyType = $("#propertyType");
            	var allSelections = $(".versionSelection select,.knowledgeSelection select");
            	var typeInt = -1;
            	var id = "";
            	var questionTopic=$(".tixing select").val();
            	for(var i = allSelections.length - 1;i > -1;i--){
            		var subSelection = $(allSelections[i]);
            		if(subSelection.val() != "ALL"){
            			typeInt = subSelection.attr("typeInt");
            			id = subSelection.val();
            			break;
            		}
            	}
            	queryObjCatch.id = id;//3id
            	queryObjCatch.propertyType = propertyType.val(); //4leixin
            	queryObjCatch.questionTopic=questionTopic;
            	queryObjCatch.pageSize = PUBLIC_PAGE_SIZE; //1
            	queryObjCatch.typeInt = typeInt; 
        	}
        	queryObjCatch.pageNo = pageNo;//2
        	
        	var queryObj = queryObjCatch;
        	if(queryObjCatch.id=="" || queryObjCatch.id==null){
        		$.ajax({
            		type: "POST",
                    url: "/itemstore/all.do",   
                    data: {"pageNo":queryObjCatch.pageNo,"pageSize":queryObjCatch.pageSize,"questionTopic":queryObjCatch.questionTopic },
                    dataType: "json",
                    success: function(data){
                    	
                    	$('.new-page-links').jqPaginator({
              	            totalPages:  Math.ceil(data.pagejson.total/PUBLIC_PAGE_SIZE) == 0?1:Math.ceil(data.pagejson.total/PUBLIC_PAGE_SIZE),//总页数  
              	            visiblePages: 10,//分多少页
              	            currentPage: parseInt(data.pagejson.cur),//当前页数
              	  
              	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
              	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
              	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
              	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
              	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
              	            onPageChange: function (n) { //回调函数
              	            	//点击页码的查询
//              	            	alert('当前第' + n + '页'); 
              	            	if(isInit){
              	            		queryList(n);
              	            		isInit = false;
              	            	}else{ 
              	            		isInit = true; 
              	            	}
              	            }
              	        });
                    	var courseList = data.datas;
                    	if(courseList.length==0){
                    		$(".new-page-links").html(" ");
                    	}
//                    	var courseList = data.datas;
//                    	console.log(courseList);
                    	$("#coursewareList").empty();
                    	for(var i=0;i<courseList.length;i++){
                    		var course = courseList[i];
                    		var htmlStr = '  <tr flag="' + course.id + '" >'+
                    		'                    <td>' + course.qt + '</td>'+
                    		'                    <td>' + course.pti + '</td>'+
                    		'                    <td>' + uploadPropertyStringForShow(course.tcv) + '</td>'+
                    		'                    <td>' + uploadPropertyStringForShow(course.kpn) + '</td>'+
                    		'                    <td>'+
                    		'                        <span>创建人：' + course.pun + '</span>'+ 
                    		'                        <span>状态：待审核</span>'+
                    		'                    </td>'+
                    		'                    <td>'+
                    		'                        <a href="#" class="zy-orange-sm-btn look-btn">查看</a>'+
                    		'                        <a href="/itemstore/editor.do?id=' + course.id + '" class="zy-orange-sm-btn update-btn">编辑</a>'+
                    		'                        <a href="#" class="zy-gray-sm-btn del-btn">删除</a>'+
                    		'                    </td>'+
                    		'                </tr>';
                    			if(course.is==0){
                    				$("#coursewareList").append(htmlStr);
                    			}else if(course.is==1){
                    				var htmlSt = '  <tr flag="' + course.id + '" >'+
                            		'                    <td>' + course.qt + '</td>'+
                            		'                    <td>' + course.pti + '</td>'+
                            		'                    <td>' + uploadPropertyStringForShow(course.tcv) + '</td>'+
                            		'                    <td>' + uploadPropertyStringForShow(course.kpn) + '</td>'+
                            		'                    <td>'+
                            		'                        <span>创建人：' + course.pun + '</span>'+ 
                            		'                        <span calss="tt">状态：通过</span>'+
                            		'                    </td>'+
                            		'                    <td>'+
                            		'                        <a href="#" class="zy-orange-sm-btn look-btn">查看</a>'+
                            		'                        <a href="/itemstore/editor.do?id=' + course.id + '" class="zy-orange-sm-btn update-btn">编辑</a>'+
                            		'                        <a href="#" class="zy-gray-sm-btn del-btn">删除</a>'+
                            		'                    </td>'+
                            		'                </tr>';
                    				$("#coursewareList").append(htmlSt);
                    			}else if(course.is==2){
                    				var htmlSt = '  <tr flag="' + course.id + '" >'+
                            		'                    <td>' + course.qt + '</td>'+
                            		'                    <td>' + course.pti + '</td>'+
                            		'                    <td>' + uploadPropertyStringForShow(course.tcv) + '</td>'+
                            		'                    <td>' + uploadPropertyStringForShow(course.kpn) + '</td>'+
                            		'                    <td>'+
                            		'                        <span>创建人：' + course.pun + '</span>'+ 
                            		'                        <span calss="tt">状态：未通过</span>'+
                            		'                    </td>'+
                            		'                    <td>'+
                            		'                        <a href="#" class="zy-orange-sm-btn look-btn">查看</a>'+
                            		'                        <a href="/itemstore/editor.do?id=' + course.id + '" class="zy-orange-sm-btn update-btn">编辑</a>'+
                            		'                        <a href="#" class="zy-gray-sm-btn del-btn">删除</a>'+
                            		'                    </td>'+
                            		'                </tr>';
                    				$("#coursewareList").append(htmlSt);
                    			}
                    			
                    		
                    		
                    		
                    		
                    	}
                    	
                    }
            	});
        		
        	}else{
        	$.ajax({
        		type: "POST",
                url: "/itemstore/allItem.do",   
                data: queryObj,
                dataType: "json",
                success: function(data){
                	
                	$('.new-page-links').jqPaginator({
          	            totalPages:  Math.ceil(data.pagejson.total/PUBLIC_PAGE_SIZE) == 0?1:Math.ceil(data.pagejson.total/PUBLIC_PAGE_SIZE),//总页数  
          	            visiblePages: 10,//分多少页
          	            currentPage: parseInt(data.pagejson.cur),//当前页数
          	           
          	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
          	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
          	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
          	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
          	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
          	            
          	            onPageChange: function (n) { //回调函数
          	            	//点击页码的查询
//          	            	alert('当前第' + n + '页'); 
          	            	if(isInit){
          	            		queryList(n);
          	            		isInit = false;
          	            	}else{ 
          	            		isInit = true; 
          	            	}
          	            }
          	        });
                	var courseList = data.datas;
                	if(courseList.length==0){
                		$(".new-page-links").html(" ");
                	}
//                	var courseList = data.datas;
//                	console.log(courseList);
                	$("#coursewareList").empty();
                	for(var i=0;i<courseList.length;i++){
                		var course = courseList[i];
                		var htmlStr = '  <tr flag="' + course.id + '" >'+
                		'                    <td>' + course.qt + '</td>'+
                		'                    <td>' + course.pti + '</td>'+
                		'                    <td>' + uploadPropertyStringForShow(course.tcv) + '</td>'+
                		'                    <td>' + uploadPropertyStringForShow(course.kpn) + '</td>'+
                		'                    <td>'+
                		'                        <span>创建人：' + course.pun + '</span>'+ 
                		'                        <span>状态：待审核</span>'+
                		'                    </td>'+
                		'                    <td>'+
                		'                        <a href="#" class="zy-orange-sm-btn look-btn">查看</a>'+
                		'                        <a href="/itemstore/editor.do?id=' + course.id + '" class="zy-orange-sm-btn update-btn">编辑</a>'+
                		'                        <a href="#" class="zy-gray-sm-btn del-btn">删除</a>'+
                		'                    </td>'+
                		'                </tr>';
                			if(course.is==0){
                				$("#coursewareList").append(htmlStr);
                			}else if(course.is==1){
                				var htmlSt = '  <tr flag="' + course.id + '" >'+
                        		'                    <td>' + course.qt + '</td>'+
                        		'                    <td>' + course.pti + '</td>'+
                        		'                    <td>' + uploadPropertyStringForShow(course.tcv) + '</td>'+
                        		'                    <td>' + uploadPropertyStringForShow(course.kpn) + '</td>'+
                        		'                    <td>'+
                        		'                        <span>创建人：' + course.pun + '</span>'+ 
                        		'                        <span calss="tt">状态：通过</span>'+
                        		'                    </td>'+
                        		'                    <td>'+
                        		'                        <a href="#" class="zy-orange-sm-btn look-btn">查看</a>'+
                        		'                        <a href="/itemstore/editor.do?id=' + course.id + '" class="zy-orange-sm-btn update-btn">编辑</a>'+
                        		'                        <a href="#" class="zy-gray-sm-btn del-btn">删除</a>'+
                        		'                    </td>'+
                        		'                </tr>';
                				$("#coursewareList").append(htmlSt);
                			}else if(course.is==2){
                				var htmlSt = '  <tr flag="' + course.id + '" >'+
                        		'                    <td>' + course.qt + '</td>'+
                        		'                    <td>' + course.pti + '</td>'+
                        		'                    <td>' + uploadPropertyStringForShow(course.tcv) + '</td>'+
                        		'                    <td>' + uploadPropertyStringForShow(course.kpn) + '</td>'+
                        		'                    <td>'+
                        		'                        <span>创建人：' + course.pun + '</span>'+ 
                        		'                        <span calss="tt">状态：未通过</span>'+
                        		'                    </td>'+
                        		'                    <td>'+
                        		'                        <a href="#" class="zy-orange-sm-btn look-btn">查看</a>'+
                        		'                        <a href="/itemstore/editor.do?id=' + course.id + '" class="zy-orange-sm-btn update-btn">编辑</a>'+
                        		'                        <a href="#" class="zy-gray-sm-btn del-btn">删除</a>'+
                        		'                    </td>'+
                        		'                </tr>';
                				$("#coursewareList").append(htmlSt);
                			}
                			
                		
                		
                		
                		
                	}
                	
                }
        	});
        	}
        }
      
        function uploadPropertyStringForShow(srcArr){
        	var resultStr = "";
        	for(var i=0;i<srcArr.length;i++){
        		if(i % 2 == 0){
        			resultStr = resultStr + "<span>";
        		}
        		
        		resultStr = resultStr + srcArr[i].name;
        		if( i != srcArr.length - 1 && i % 2 == 0){
        			resultStr = resultStr + "/";
        		}
        		if(i % 2 == 1){
        			resultStr = resultStr + "</span>";
        		}
        	}
        	if((srcArr.length - 1) == 0){

        		resultStr = resultStr + "</span>";
        	}
        	return resultStr;
        }
        //初始查询
        queryList(1); 
        
        
        
        //删除方法绑定
        
        $('body').on('click','.del-btn',function(){
        	if(!confirm("删除后不能恢复，是否确认？")){
        		return;
        	}
        	var targetId = $(this).closest("tr").attr("flag");
        	$.ajax({
        		type: "POST",
                url: "/itemstore/removeItem.do",  
                data: {id : targetId},
                dataType: "json",
                success: function(data){
                	queryList(queryObjCatch.pageNo);
                	alert("删除成功")
                }
        	});
        });
//        $('body').on('click','.update-btn',function(){
//        	location.href = "/itemstore/editor.do?id=" + $(this).closest('tr').attr('flag');
//        })
        
    };

    (function(){
        ziyuanGit.init();
    })();
    module.exports = ziyuanGit;
})


