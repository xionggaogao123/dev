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
define("addLessonWareToGit",['common','imguploader','fileUploadTwo'],function(require,exports,module){
    /**
     *初始化参数
     */
    var addLessonWareToGit = {},
        Common = require('common'),
		fileUpload = require('fileUploadTwo'),
        imguploader = require('imguploader');

	var allOptionHtml = "<option value='ALL'>--请选择--</option>";
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * addLessonWareToGit.init()
     */
    addLessonWareToGit.init = function(){


        //图片上传
    	imguploader(
			function(successResponse){
				if("200" == successResponse.code){
					$("#coverId").val(successResponse.data);
					$('#imageUrl').val("");
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

		var termType= $("#termType").val();
		var subjectType= $("#subjectType").val();
		var bookVertion= $("#bookVertion").val();
		var gradeType= $("#gradeType").val();
		var chapterType= $("#chapterType").val();
		var partType= $("#partType").val();
		
		//页面开始时的初始化
		findAndFillEmptySelection();

        /**
         * 取消按钮方法
         */
        $('#cancelBtn').on('click',function(){
			var isSaved=$("#isSaved").val();
			if(isSaved==0){
				location.href = "/cloudres/lessonWareMge.do";
			}
			if(isSaved==1){
				location.href = "/cloudres/lessonWareGit.do";
			}
        });

		$('.add-ziyuan-col').on('click', '.form-head em', function() {
			$(this).closest('.form-col').remove();
		});
		/**
		 * 所有下拉菜单在这里
		 */
		//初始化页面开始时的下拉或新增的下拉
		function findAndFillEmptySelection(){
			var allVersionRootSelections = $(".versionTermTypeSelection");
			for(var i = 0;i < allVersionRootSelections.length;i++){
				var subVersionSelection = $(allVersionRootSelections[i]);
				if(subVersionSelection.find("option").length == 0 ){
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
				async: false,
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

		

		/*$(".versionSubjectSelection").val(subjectType);
		$(".bookVertionSelection").val(bookVertion);
		$(".gradeSelection").val(gradeType);
		$(".chapterSelection").val(chapterType);
		$(".partSelection").val(partType);*/

		queryAndeFillSelection($(".versionTermTypeSelection").attr("subClass"),termType,$(".versionTermTypeSelection"));

		queryAndeFillSelection($(".versionSubjectSelection").attr("subClass"),subjectType,$(".versionSubjectSelection"));

		queryAndeFillSelection($(".bookVertionSelection").attr("subClass"),bookVertion,$(".bookVertionSelection"));

		queryAndeFillSelection($(".gradeSelection").attr("subClass"),gradeType,$(".gradeSelection"));

		queryAndeFillSelection($(".chapterSelection").attr("subClass"),chapterType,$(".chapterSelection"));


		//填充下拉菜单的方法
		function fillSelection($selectionDom,srcDatas) {
			var targetSelection = $selectionDom;
			targetSelection.html(allOptionHtml);
			if (srcDatas) {
				for (var i = 0; i < srcDatas.length; i++) {
					var subData = srcDatas[i];
					if (subData.id == termType || subData.id == subjectType || subData.id == bookVertion || subData.id == gradeType || subData.id == chapterType || subData.id == partType) {
						targetSelection.append("<option value='" + subData.id + "' selected>" + subData.name + "</option>");
					} else {
						targetSelection.append("<option value='" + subData.id + "'>" + subData.name + "</option>");
					}

				}
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

        /**
         * 新增按钮的方法
         */
        $('#commitUploadBtn').on('click',function(){
			//检查封面和文件上传情况
			if($('#coverId').val() == ""&&$('#imageUrl').val() == ""){
				alert("请上传封面！");
				return;
			}
			if($('#id').val() == ""){
				alert("请上传课程课件！");
				return;
			}
			//获取相应字段值
			var addObj = {};
			//检查信息是否填写完整并获取相应字段
			if($('.partSelection').val() == "ALL"){
				alert("请将属性信息填写完整！");
				return;
			}

			addObj.id=$('#id').val();
			addObj.imageUrl = $('#imageUrl').val();
			addObj.coverId = $('#coverId').val();
			var verId = $('.partSelection').val();
			addObj.verId = verId;
			var isSaved=$("#isSaved").val();
			//addObj.fileId = $('#fileId').val();
			var msg="";
			if(isSaved==0){
				msg="是否确认新增？";
			}
			if(isSaved==1){
				msg="是否确认保存？";
			}
			if(!confirm(msg)){
				return;
			}

			$.ajax({
				type: "POST",
				url: "/cloudres/updLessonWare.do",
				data: addObj,
				dataType: "json",
				success: function(data){
					if(data.code == "200"){
						alert("操作完成！");

						if(isSaved==0){
							location.href = "/cloudres/lessonWareMge.do";
						}
						if(isSaved==1){
							location.href = "/cloudres/lessonWareGit.do";
						}
					}else{
						alert("操作失败,请联系管理员！");
					}
				}
			});
        });
    };

    module.exports = addLessonWareToGit;
})
