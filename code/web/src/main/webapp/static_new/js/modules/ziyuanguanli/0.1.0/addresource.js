/**
 * Created by liwei on 15/9/06.
 */
/**
 * @author 李伟
 * @module  添加资源
 * @description
 * 添加资源操作
 */
/* global Config */
define(['common','imguploader','fileuploader'],function(require,exports,module){
    /**
     *初始化参数
     */
    var addresource = {},
        Common = require('common'),
        fileuploader = require('fileuploader'),
        imguploader = require('imguploader');

    /**
	 * 公共变量
	 */
	var allOptionHtml = "<option value='ALL'>--请选择--</option>";
    
    
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * addresource.init()
     */

    addresource.init = function(){

        //图片上传
    	imguploader(
         		function(successResponse){
         			if("200" == successResponse.code){
         				$("#coverId").val(successResponse.data)
         			}
         		},
         		function(errorResponse){
         			if("500" == successResponse.code){
         				alert(errorResponse.data); 
         			}
         		});
        //文件上传
        fileuploader(
        		function(successResponse){
         			if("200" == successResponse.code){
         				$("#fileId").val(successResponse.data)
         			}
         		},
         		function(errorResponse){
         			if("500" == successResponse.code){
         				alert(errorResponse.data); 
         			}
         		});

        

        var htmlDom = '<div class="form-col">'+
            '    <div class="form-head clearfix">'+
            '        <h4>资源分类属性</h4>'+
            '        <span>同步教材和综合知识点都需同时填写 <em>删除</em></span>'+
            '    </div>'+
            '    <div class="form-main clearfix complex-value-class">'+
            '        <dl>'+
            '            <dt>同步教材版本</dt>'+
            '            <dd>'+
            '                <label>学段</label>'+
            '                <select class="versionTermTypeSelection" subClass="versionSubjectSelection" typeInt="1">'+
            '                </select>'+
            '            </dd>'+
            '            <dd>'+
            '                <label>学科</label>'+
            '                <select class="versionSubjectSelection" subClass="bookVertionSelection" typeInt="2">'+
            '                </select>'+
            '            </dd>'+
            '            <dd>'+
            '                <label>教材版本</label>'+
            '                <select class="bookVertionSelection" subClass="gradeSelection" typeInt="3">'+
            '                </select>'+
            '            </dd>'+
            '            <dd>'+
            '                <label>年纪/课本</label>'+
            '                <select class="gradeSelection" subClass="chapterSelection" typeInt="4">'+
            '                </select>'+
            '            </dd>'+
            '            <dd>'+
            '                <label>章</label>'+
            '                <select class="chapterSelection" subClass="partSelection" typeInt="5">'+
            '                </select>'+
            '            </dd>'+
            '            <dd>'+
            '                <label>节</label>'+
            '                <select class="partSelection" subClass="0" typeInt="6">'+
            '                </select>'+
            '            </dd>'+
            '        </dl>'+
            '        <dl>'+
            '            <dt>综合知识点</dt>'+
            '            <dd>'+
            '                <label>学段</label>'+
            '                <select class="knowledgeTermtypeSelection" subClass="knowledgeSubjectSelection" typeInt="1">'+
            '                </select>'+
            '            </dd>'+
            '            <dd>'+
            '                <label>学科</label>'+
            '                <select class="knowledgeSubjectSelection" subClass="knowledgeAreaSelection" typeInt="2">'+
            '                </select>'+
            '            </dd>'+
            '            <dd>'+
            '                <label>知识面</label>'+
            '                <select class="knowledgeAreaSelection" subClass="knowledgePointSelection" typeInt="7">'+
            '                </select>'+
            '            </dd>'+
            '            <dd>'+
            '                <label>知识点</label>'+
            '                <select class="knowledgePointSelection" subClass="0" typeInt="8">'+
            '                </select>'+
            '            </dd>'+
            '        </dl>'+
            '    </div>'+
            '</div>';

        $('.add-ziyuan-btn').on('click',function(){
            $('.add-ziyuan-col').append(htmlDom);
            //初始化新加入的属性
            findAndFillEmptySelection();
        });

        $('.add-ziyuan-col').on('click','.form-head em',function(){
            $(this).closest('.form-col').remove();
        })

        var answerHtml='<div class="answer-check clearfix">'+
            '    <label><input type="checkbox" name="answer" />对</label>'+
            '    <label><input type="radio" name="answer" />错</label>'+
            '    <em>X</em>'+
            '</div>';

        $('.add-answer').on('click',function(){
            $('.answer-col').append(answerHtml);
        });

        $('.form-main').on('click','.answer-check em',function(){
            $(this).closest('.answer-check').remove();
        })

        /**
         * 所有下拉菜单在这里
         */
        //初始化页面开始时的下拉或新增的下拉
        function findAndFillEmptySelection(){
        	var allVersionRootSelections = $(".versionTermTypeSelection");
        	var allKnowledgeRootSelections = $(".knowledgeTermtypeSelection");
        	for(var i = 0;i < allVersionRootSelections.length;i++){
        		var subVersionSelection = $(allVersionRootSelections[i]);
        		var subKnowledgeSelection = $(allKnowledgeRootSelections[i]);
        		if(subVersionSelection.find("option").length == 0 && subKnowledgeSelection.find("option").length == 0){
        			//TODO  挨个初始化
        			queryAndeFillSelection("versionTermTypeSelection","ALL",subVersionSelection);
        			queryAndeFillSelection("knowledgeTermtypeSelection","ALL",subKnowledgeSelection);
        		}
        	}
        }
      //下拉菜单查询填充方法
        function queryAndeFillSelection(selectionClass,parentObjId,$parentDom){
        	if(selectionClass == "0"){
        		return;
        	}
//        	var $selection = $("#" + selectionId);
        	var $selection = $($parentDom.closest(".complex-value-class").find("." + selectionClass).get(0));
        	if(parentObjId == "ALL" && selectionClass != "versionTermTypeSelection" && selectionClass !="knowledgeTermtypeSelection"){
        		if(selectionClass != "0"){
            		resetLittleBrothers($selection);
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
                	fillSelection($selection,data.datas);
                	if($selection.attr("subClass") != "0"){
                		var $needResetSel = $($parentDom.closest(".complex-value-class").find("." + $selection.attr("subClass")).get(0));
                		resetLittleBrothers($needResetSel);
                	}
                } 
        	});
        }
        
        //填充下拉菜单的方法
        function fillSelection($selectionDom,srcDatas){
        	var targetSelection = $selectionDom;
        	targetSelection.html(allOptionHtml);
        	for(var i=0;i<srcDatas.length;i++){
        		var subData = srcDatas[i];
        		targetSelection.append("<option value='" + subData.id + "'>" + subData.name + "</option>");
        	}
        }
       //置空弟弟们
        function resetLittleBrothers($targetSel){
        	if($targetSel.length == 0){
        		return;
        	}else{
        		$targetSel.html(allOptionHtml);
        		if($targetSel.attr("subClass") != "0"){
        			var $needResetSel = $($targetSel.closest(".complex-value-class").find("." + $targetSel.attr("subClass")).get(0));
            		resetLittleBrothers($needResetSel);
        		}
        	}
        }
        //给所有资源字典类型选择下拉菜单增加事件 
        $('body').on('change','.complex-value-class select',function(){
        	queryAndeFillSelection($(this).attr("subClass"),$(this).val(),$(this));
        	$(this).find("[value|='ALL']").remove();
        });
        //页面开始时的初始化
        findAndFillEmptySelection();
        /**
         * 取消按钮方法
         */
        $('#cancelBtn').on('click',function(){
        	location.href = "/cloudres/manager/list.do"; 
        })
        
        /**
         * 新增按钮的方法
         */
        $('#commitUploadBtn').on('click',function(){
        	
        	
        
        	
        	
        	if(!confirm("是否确认新增？")){
        		return;
        	}
        	//检查封面和文件上传情况
//        	if(!$('#resImage').val()){
//        		alert("请上传封面！");
//        		return;
//        	}
        	if(!$('#resFile').val()){
        		alert("请上传课件！");
        		return;
        	}
        	//获取相应字段值
        	var addObj = {};
        	
        	var verStr = "";
        	var knoStr = "";
        	//检查信息是否填写完整并获取相应字段
        	var $allSelections = $('.complex-value-class');
        	for(var i=0;i<$allSelections.length;i++){
        		var $subSel = $($allSelections[i]);
        		if($subSel.find('.partSelection').val() == "ALL" || $subSel.find('.knowledgePointSelection').val() == "ALL"){
//        			alert("请将属性信息填写完整！");
//        			return;
        		}
        		verStr = verStr + $subSel.find('.partSelection').val();
        		knoStr = knoStr + $subSel.find('.knowledgePointSelection').val();
        		if(i != ($allSelections.length - 1)){
        			verStr = verStr + ",";
        			knoStr = knoStr + ",";
        		}
        	}
        	addObj.verStr = verStr;
        	addObj.knoStr = knoStr;
        	//addObj.coverId = $('#coverId').val();
        	//addObj.fileId = $('#fileId').val();
        	
        	jQuery("#verStr").val(verStr);
        	jQuery("#knoStr").val(knoStr);
        	
        	
        	jQuery("#resForm").submit();
        	
        	/**
        	$.ajax({
        		type: "POST",
                url: "/courseware/addCourseware.do",  
                data: addObj,
                dataType: "json",
                success: function(data){
                	if(data.code == "200"){
                		alert("操作完成!");
                		location.href = "/courseware/gitList.do";
                	}else{
                		alert("操作失败,请联系管理员！");
                	}
                } 
        	});
        	**/
        });
        

    };


    (function(){
        addresource.init();
    })();

    module.exports = addresource;
})
