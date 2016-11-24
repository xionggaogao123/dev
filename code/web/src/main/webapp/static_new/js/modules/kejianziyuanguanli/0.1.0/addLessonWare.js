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
define("addLessonWare",['common','imguploader','fileUploadTwo'],function(require,exports,module){
    /**
     *初始化参数
     */
    var addLessonWare = {},
        Common = require('common'),
		fileUpload = require('fileUploadTwo'),
        imguploader = require('imguploader');

	var allOptionHtml = "<option value='ALL'>--请选择--</option>";
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * addLessonWare.init()
     */

    addLessonWare.init = function(){

        //图片上传
    	imguploader(
			function(successResponse){
				if("200" == successResponse.code){
					$("#coverId").val(successResponse.data);
				}
			},
			function(errorResponse){
				if("500" == successResponse.code){
					alert(errorResponse.data);
				}
		}
		);
        //文件上传
		fileUpload(
			function(successResponse){
				if("200" == successResponse.code){
					$("#fileId").val(successResponse.data);
				}
			},
			function(errorResponse){
				if("500" == successResponse.code){
					alert(errorResponse.data);
				}
			}
		);


		$('.add-ziyuan-col').on('click', '.form-head em', function() {
			$(this).closest('.form-col').remove();
		});
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
				}
			}
		}
		//下拉菜单查询填充方法
		function queryAndeFillSelection(selectionClass,parentObjId,$parentDom){
			if(selectionClass == "0"){
				return;
			}
//    	var $selection = $("#" + selectionId);
			var $selection = $($parentDom.closest(".complex-value-class").find("." + selectionClass).get(0));
			if(parentObjId == "ALL" && selectionClass != "versionTermTypeSelection"){
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
		 * 取消方法
		 */
		$('#cancelBtn').on('click',function(){
			location.href = "/cloudres/lessonWareGit.do";
		});

		/**
		 * 新增按钮的方法
		 */
		$('#commitUploadBtn').on('click',function(){

			//检查封面和文件上传情况
			if($('#coverId').val() == ""){
				alert("请上传封面！");
				return;
			}
			if($('#fileId').val() == ""){
				alert("请上传课件！");
				return;
			}
			//获取相应字段值
			var addObj = {};
			//检查信息是否填写完整并获取相应字段
			if($('.partSelection').val() == "ALL"){
				alert("请将属性信息填写完整！");
				return;
			}
			if(!confirm("是否确认新增？")){
				return;
			}
			var verId = $('.partSelection').val();
			addObj.verId = verId;
			addObj.coverId = $('#coverId').val();
			addObj.fileId = $('#fileId').val();
			$.ajax({
				type: "POST",
				url: "/cloudres/saveLessonWare.do",
				data: addObj,
				dataType: "json",
				success: function(data){
					if(data.code == "200"){
						alert("操作完成!");
						location.href = "/cloudres/lessonWareGit.do";
					}else{
						alert("操作失败,请联系管理员！");
					}
				}
			});
		});


    };


    module.exports = addLessonWare;
})
