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
define('lessonWareGit',['common','pagination'],function(require,exports,module){
	/**
	 *初始化参数
	 */
	var lessonWareGit = {},
		Common = require('common');
	require('pagination');
	Array.prototype.remove = function(dx){
		if(isNaN(dx)||dx>this.length){return false;}
		this.splice(dx,1);
	};

	var allOptionHtml = "<option value='ALL'>全部</option>";

	/**
	 * @func init
	 * @desc 页面初始化
	 * @example
	 * lessonWareGit.init()
	 */
	lessonWareGit.init = function(){



		lessonWareGit.queryAndeFillSelection("versionTermTypeSelection","ALL");

		//给所有资源字典类型选择下拉菜单增加事件
		$('body').on('change','.versionSelection select',function(){
			lessonWareGit.queryAndeFillSelection($(this).attr("subId"),$(this).val());
		});

		//初始查询
		lessonWareGit.queryList(1);

		/**
		 * 查询填充列表相关
		 */
		$(".search-btn").click(function(){
			lessonWareGit.queryList(1);
		});


		//绑定添加至资源库按钮的跳转方法
		$('body').on('click','.add-to-git-btn',function(){
			var id = $(this).closest("tr").attr("id");
			location.href = "/cloudres/addLessonWareToGit.do?id=" + id;
		});

		$("#downQR").click(function(){
			$("#downId").val("");
			$(".new-page-popup").hide();
		});
		$("#downLoadFile").click(function(){
			location.href ='/cloudres/down.do?id='+$("#downId").val();
		});

		//资源管理的查看
		/*playerLinks.init('',function(e){
			var id = e.closest("tr").attr("id");
			var url;
			$.ajax({
				async:false,
				type: "POST",
				url: "/cloudres/getLessonWare.do",
				data: {id : id },
				dataType: "json",
				success: function(data){
					if(data.dto){
						url = data.dto.url;
					}else{
						alert("获取课件地址出错，请联系管理员！");
						return;
					}
				}
			});
			return url;
		});*/
	};

	lessonWareGit.queryAndeFillSelection=function(selectionId,parentObjId){
		if(selectionId == "0"){
			return;
		}
		var $selection = $("#" + selectionId);
		if(parentObjId == "ALL" && selectionId != "versionTermTypeSelection"){
			if(selectionId != "0"){
				lessonWareGit.resetLittleBrothers(selectionId);
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
				lessonWareGit.fillSelection(selectionId,data.datas);
				if($selection.attr("subId") != "0"){
					lessonWareGit.resetLittleBrothers($selection.attr("subId"));
				}
			}
		});
	}

	//填充下拉菜单的方法
	lessonWareGit.fillSelection = function(selectionId,srcDatas){
		var targetSelection = $("#" + selectionId);
		targetSelection.html(allOptionHtml);
		for(var i=0;i<srcDatas.length;i++){
			var subData = srcDatas[i];
			targetSelection.append("<option value='" + subData.id + "'>" + subData.name + "</option>");
		}
	}

	//置空弟弟们
	lessonWareGit.resetLittleBrothers = function(targetId){
		if(targetId == "0"){
			return;
		}else{
			var $targetSelection = $("#" + targetId);
			$targetSelection.html(allOptionHtml);
			lessonWareGit.resetLittleBrothers($targetSelection.attr("subId"));
		}
	}


	var searchData={};
	searchData.pageSize=12;
	lessonWareGit.queryList = function(pageNo){
		var allSelections = $(".versionSelection select");
		var typeInt = -1;
		var typeId = "";
		for(var i = allSelections.length - 1;i > -1;i--){
			var subSelection = $(allSelections[i]);
			if(subSelection.val() != "ALL" && $.trim(subSelection.html()) != ""){
				typeInt = subSelection.attr("typeInt");
				typeId = subSelection.val();
				break;
			}
		}
		searchData.typeId = typeId;
		searchData.typeInt = typeInt;
		searchData.isSaved=1;
		searchData.searchName = "";
		searchData.pageNo = pageNo;
		Common.getPostData('/cloudres/queryCloudList.do', searchData,function(data){
			var courseList = data.datas;
			var list=courseList.list;
			if(list.length == 0){
				$("#coursewareList").html('');
				$('.new-page-links').html('');
				return;
			}
			$('.new-page-links').jqPaginator({
				totalPages: Math.ceil(courseList.count/searchData.pageSize) == 0?1:Math.ceil(courseList.count/searchData.pageSize),//总页数
				visiblePages: 10,//分多少页
				currentPage: parseInt(data.page),//当前页数
				first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
				prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
				next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
				last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
				page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
				onPageChange: function (n) { //回调函数
					//点击页码的查询
					//alert('当前第' + n + '页');
					if(searchData.pageNo!=n){
						lessonWareGit.queryList(n);
					}
				}
			});

			$("#coursewareList").empty();

			for(var i=0;i<list.length;i++){
				var course = list[i];
				var fromStr = course.from;
				if(fromStr == "资源板块前端"){
					fromStr = "用户上传";
				}
				if(fromStr == "资源板块后端"){
					fromStr = "管理员上传";
				}
				var termSubject="";
				if(course.termType==""){
					termSubject=course.subjectName;
				}else{
					termSubject=course.termType + '/' + course.subjectName;
				}
				var gradeBook="";
				if(course.gradeName==""){
					gradeBook=course.bookType;
				}else{
					gradeBook=course.gradeName + '/' + course.bookType;
				}
				var htmlStr = '  <tr id="' + course.id + '">'+
					'                    <td>'+
					'                        <img src="' + course.imageUrl + '" class="img-view-dialog"/>'+
					'                        <em>' + course.type + '</em>'+
					'                    </td>'+
					'                    <td>' + course.createTime + '</td>'+
					'                    <td>' + termSubject+'</td>'+
					'                    <td>' + gradeBook+'</td>'+
					'                    <td>'+
					'                        <span>创建人：' + course.userName + '</span>'+
					'                        <span>来源：' + fromStr + '</span>'+
					'                    </td>'+
					'                    <td>'+
					'                        <a href="javascript:;" class="zy-orange-sm-btn show-me-btn">查看</a>'+
					'                        <a href="javascript:;" class="zy-orange-sm-btn edit-page-btn">编辑</a>'+
					'                        <a href="javascript:;" class="zy-gray-sm-btn del-btn">删除</a>'+
					'                    </td>'+
					'                </tr>';
				$("#coursewareList").append(htmlStr);
			}

			//绑定编辑课件资源方法
			$('body').on('click','.edit-page-btn',function(){
				var id = $(this).closest("tr").attr("id");
				location.href = "/cloudres/editLessonWare.do?id=" + id;
			});

			$(".show-me-btn").click(function(){
				var id = $(this).closest("tr").attr("id");
				$("#downId").val(id);
				$(".new-page-popup").show();
			});

			//删除方法绑定
			$(".del-btn").click(function(){
				if(!confirm("删除后不能恢复，是否确认？")){
					return;
				}
				var delData={};
				delData.id = $(this).closest("tr").attr("id");
				Common.getPostData('/cloudres/delLessonWare.do', delData,function(data){
					alert("操作完成!");
					lessonWareGit.queryList(searchData.pageNo);
				});
			});
		});
	}

	module.exports = lessonWareGit;
})