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
    var ziyuanguanli = {},
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
     * @func init
     * @desc 页面初始化
     * @example
     * ziyuanguanli.init()
     */
    ziyuanguanli.init = function(){


        
        /**
         * 下拉菜单相关
         */
        $('#propertyType').on('change',function(){
        	$('.versionSelection').remove();
        	$('.knowledgeSelection').remove();
        	if($(this).val() == "ver"){
        		$("#selectionArea").append(versionSelectionHTML);
        		queryAndeFillSelection("versionTermTypeSelection","ALL");
        	}else{
        		$("#selectionArea").append(knowledgeSelectionHTML);
        		queryAndeFillSelection("knowledgeTermtypeSelection","ALL");
        	}
        });
      //下拉菜单查询填充方法
        function queryAndeFillSelection(selectionId,parentObjId){
        	if(selectionId == "0"){
        		return;
        	}
        	var $selection = $("#" + selectionId);
        	if(parentObjId == "ALL" && selectionId != "versionTermTypeSelection" && selectionId !="knowledgeTermtypeSelection"){
        		if(selectionId != "0"){
            		resetLittleBrothers(selectionId);
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
            	for(var i = allSelections.length - 1;i > -1;i--){
            		var subSelection = $(allSelections[i]);
            		if(subSelection.val() != "ALL" && $.trim(subSelection.html()) != ""){
            			typeInt = subSelection.attr("typeInt");
            			id = subSelection.val();
            			break;
            		}
            	}
            	queryObjCatch.pageSize = PUBLIC_PAGE_SIZE; 
            	queryObjCatch.typeInt = typeInt; 
            	queryObjCatch.id = id; 
            	queryObjCatch.propertyType = propertyType.val(); 
        	}
        	queryObjCatch.pageNo = pageNo;
        	
        	var queryObj = queryObjCatch;
        	
        	
        	$.ajax({
        		type: "POST",
                url: "/courseware/queryK6KTUnSavedList.do",   
                data: queryObj,
                dataType: "json",
                success: function(data){
//                	alert(data.datas.length);
                	$('.new-page-links').jqPaginator({
          	            totalPages: Math.ceil(data.pagejson.total/PUBLIC_PAGE_SIZE) == 0?1:Math.ceil(data.pagejson.total/PUBLIC_PAGE_SIZE),//总页数  
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
          	            		isInit = false;
          	            		queryList(n);
          	            	}else{ 
          	            		isInit = true; 
          	            	}
          	            }
          	        });
                	var courseList = data.datas;
                	
                	$("#coursewareList").empty();
                	for(var i=0;i<courseList.length;i++){
                		var course = courseList[i];
                		var fromStr = course.from;
                		if(fromStr == "资源板块前端"){
                			fromStr = "用户上传";
                		}
                		if(fromStr == "资源板块后端"){
                			fromStr = "管理员上传";
                		}

                		var htmlStr = '  <tr flag="' + course.id + '" fid="' + course.fId + '">'+
                		'                    <td>'+
                		'                        <img src="' + course.cover + '" href="' + course.name + '" class="img-view-dialog" style="cursor:pointer"/>'+
                		'                        <em>' + course.name + '</em>'+
                		'                    </td>'+
                		'                    <td>' + course.time + '</td>'+
                		'                    <td>' + uploadPropertyStringForShow(course.ver) + '</td>'+
                		'                    <td>' + uploadPropertyStringForShow(course.kno) + '</td>'+
                		'                    <td>'+
                		'                        <span>创建人：' + course.userName + '</span>'+ 
                		'                        <span>来源：' + fromStr + '</span>'+
                		'                        <span>格式：' + course.fileType + '</span>'+
                		'                    </td>'+
                		'                    <td>'+
                		'                        <a href="javascript:;" class="zy-orange-sm-btn add-to-git-btn">添加</a>'+
                		//'                        <a href="javascript:;" class="zy-orange-sm-btn ignore-btn">忽略</a>'+
                		'                        <a href="javascript:;" class="zy-gray-sm-btn del-btn">删除</a>'+
                		'                    </td>'+
                		'                </tr>';
                		$("#coursewareList").append(htmlStr);
                	}
                	
                }
        	});
        	
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
        	var fId = $(this).closest("tr").attr("fId");
        	var httpURL = "/courseware/delCoursewarePreRev.do";
        	if(fId == ""){
        		httpURL = "/courseware/delCourseware.do";
        	}
        	$.ajax({
        		type: "POST",
                url: httpURL,  
                data: {id : targetId , fId : fId},
                dataType: "json",
                success: function(data){
                	alert("操作完成!");
                	queryList(queryObjCatch.pageNo);
                }
        	});
        });
      //忽略方法绑定
        $('body').on('click','.ignore-btn',function(){
        	if(!confirm("忽略后不能恢复，是否确认？")){
        		return;
        	}
        	var targetId = $(this).closest("tr").attr("flag");
        	var fId = $(this).closest("tr").attr("fId");
        	var httpURL = "/courseware/ignoreCoursewarePreRev.do";
        	if(fId == ""){
        		httpURL = "/courseware/ignoreCourseware.do";
        	}
        	$.ajax({
        		type: "POST",
                url: httpURL,  
                data: {id : targetId , fId : fId}, 
                dataType: "json",
                success: function(data){
                	alert("操作完成!");
                	queryList(queryObjCatch.pageNo);
                }
        	});
        });
        //绑定添加至资源库按钮的跳转方法
        $('body').on('click','.add-to-git-btn',function(){
        	location.href = "/courseware/addToGit.do?id=" + $(this).closest('tr').attr('flag') + "&paramFileId=" + $(this).closest('tr').attr('fid');
        });
      //资源管理的查看
        playerLinks.init('.img-view-dialog',function(e){
        	var viewObjId = e.closest('tr').attr('flag');
        	var viewObjFId = e.closest('tr').attr('fid'),
        		url;
				$.ajax({
					async:false,
					type: "POST",
					url: "/courseware/getResourceUrlForManagerPage.do",  
					data: {id : viewObjId , fId : viewObjFId},
					dataType: "json",
					success: function(data){
						if(data.url == ""){
							alert("获取课件地址出错，请联系管理员！");
							return;
						}
						url = data.url;
					}
				});
        	return url;
        });
        
        
    };

    (function(){
        ziyuanguanli.init();
    })();

    module.exports = ziyuanguanli;
})