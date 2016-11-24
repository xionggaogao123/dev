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
define('itempoolMge',['common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var itempoolMge = {},
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
     * itempoolMge.init()
     */
    itempoolMge.init = function(){


		var versionSelectionHTML = '<dd class="versionSelection">'+
									'<span>'+
									'<label>学段</label>'+
									'<select id="versionTermTypeSelection" subId="versionSubjectSelection" typeInt="1">'+
									'</select>'+
									'</span>'+
									'<span>'+
									'<label>学科</label>'+
									'<select id="versionSubjectSelection" subId="bookVertionSelection" typeInt="2">'+
									'</select>'+
									'</span>'+
									'<span>'+
									'<label>教材版本</label>'+
									'<select id="bookVertionSelection" subId="gradeSelection" typeInt="3">'+
									'</select>'+
									'</span>'+
									'<span>'+
									'<label>年级/课本</label>'+
									'<select id="gradeSelection" subId="chapterSelection" typeInt="4">'+
									'</select>'+
									'</span>'+
									'</dd>'+
									'<dd class="versionSelection">'+
									'<span>'+
									'<label>章目录</label>'+
									'<select id="chapterSelection" subId="partSelection" typeInt="5">'+
									'</select>'+
									'</span>'+
									'<span>'+
									'<label>节目录</label>'+
									'<select id="partSelection" subId="0" typeInt="6">'+
									'</select>'+
									'</span>'+
									'<button type="button" class="zy-gray-btn search-btn">搜 索</button>'+
								'</dd>';

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
			'                    <select id="knowledgePointSelection" subId="littleknowledgePointSelection" typeInt="8">'+
			'                    </select>'+
			'                </span>'+
			'			</dd>'+
			'			<dd class="knowledgeSelection">'+
			'                <span>'+
			'					<label>小知识点</label>'+
			'					<select id="littleknowledgePointSelection" subId="0" typeInt="9">'+
			'					</select>'+
			'                </span>'+
			'                <button type="button" class="zy-gray-btn search-btn">搜 索</button>'+
			'            </dd>';

		var propertyType = $('#propertyType').val();
		if(propertyType== "tcv") {
			$('.knowledgeSelection').remove();
			itempoolMge.queryAndeFillSelection("versionTermTypeSelection", "ALL");
		}else{
			$('.versionSelection').remove();
			$("#selectionArea").append(knowledgeSelectionHTML);
			itempoolMge.queryAndeFillSelection("knowledgeTermtypeSelection","ALL");
		}
		//给所有资源字典类型选择下拉菜单增加事件
		$('body').on('change','.versionSelection select,.knowledgeSelection select',function(){
			itempoolMge.queryAndeFillSelection($(this).attr("subId"),$(this).val());
		});

		/**
		 * 下拉菜单相关
		 */
		$('#propertyType').on('change',function(){
			$('.versionSelection').remove();
			$('.knowledgeSelection').remove();
			if($(this).val() == "tcv"){
				$("#selectionArea").append(versionSelectionHTML);
				itempoolMge.queryAndeFillSelection("versionTermTypeSelection","ALL");
			}else{
				$("#selectionArea").append(knowledgeSelectionHTML);
				itempoolMge.queryAndeFillSelection("knowledgeTermtypeSelection","ALL");
			}
			itempoolMge.queryList(1);
		});

		//初始查询
		itempoolMge.queryList(1);

        /**
         * 查询填充列表相关
         */
		$('body').on('click','.search-btn',function(){
			itempoolMge.queryList(1);
		});
    };

	itempoolMge.queryAndeFillSelection=function(selectionId,parentObjId){
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
				itempoolMge.fillSelection(selectionId,data.datas);
				if($selection.attr("subId") != "0"){
					itempoolMge.resetLittleBrothers($selection.attr("subId"));
				}
			}
		});
	}

	//填充下拉菜单的方法
	itempoolMge.fillSelection = function(selectionId,srcDatas){
		var targetSelection = $("#" + selectionId);
		targetSelection.html(allOptionHtml);
		for(var i=0;i<srcDatas.length;i++){
			var subData = srcDatas[i];
			targetSelection.append("<option value='" + subData.id + "'>" + subData.name + "</option>");
		}
	}

	//置空弟弟们
	itempoolMge.resetLittleBrothers = function(targetId){
		if(targetId == "0"){
			return;
		}else{
			var $targetSelection = $("#" + targetId);
			$targetSelection.html(allOptionHtml);
			itempoolMge.resetLittleBrothers($targetSelection.attr("subId"));
		}
	}


	var searchData={};
	searchData.pageSize=12;
	itempoolMge.queryList = function(pageNo){
		var allSelections = $(".versionSelection select,.knowledgeSelection select");
		var typeInt = -1;
		var typeId = "ALL";
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
		searchData.isSaved=$("#isSaved").val();
		searchData.searchName = "";
		searchData.pageNo = pageNo;
		searchData.propertyType = $("#propertyType").val();
		searchData.questionTopic=$("#quesType").val();
		searchData.level=$("#level").val();
		Common.getPostData('/itempool/queryItemList.do', searchData,function(data){
			var list = data.datas;
			if(list.length == 0){
				$("#itemList").html('');
				$('.new-page-links').html('');
				return;
			}
			$('.new-page-links').jqPaginator({
				totalPages: Math.ceil(data.pagejson.total/searchData.pageSize) == 0?1:Math.ceil(data.pagejson.total/searchData.pageSize),//总页数
				visiblePages: 10,//分多少页
				currentPage: parseInt(data.pagejson.cur),//当前页数
				first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
				prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
				next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
				last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
				page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
				onPageChange: function (n) { //回调函数
					//点击页码的查询
					//alert('当前第' + n + '页');
					if(searchData.pageNo!=n){
						itempoolMge.queryList(n);
					}
				}
			});

			$("#itemList").empty();
			var htmlStr ="";
			for(var i=0;i<list.length;i++){
				var course = list[i];
				var subHtmlStr ="";
				if(course.isSaved==0){
					subHtmlStr = '<span>状态：待审核</span>';
				}else if(course.isSaved==1){
					subHtmlStr ='<span calss="tt">状态：通过</span>';
				}else if(course.isSaved==2){
					subHtmlStr ='<span calss="tt">状态：未通过</span>';
				}
				htmlStr = '  <tr flag="' + course.id + '" >'+
					'                    <td>' + course.itemType + '</td>'+
					'                    <td>' + course.uploadTime + '</td>'+
					'                    <td>' + course.bk + '</td>'+
					'                    <td>' + course.kw + '</td>'+
					'                    <td>'+
					'						<span>创建人：' + course.userName + '</span>'+
					subHtmlStr+
					'                    </td>'+
					'                    <td>'+
					'                        <a href="javascript:;" class="zy-orange-sm-btn look-btn">查看</a>'+
					'                        <a href="/itempool/editItem.do?id=' + course.id + '" class="zy-orange-sm-btn update-btn">编辑</a>'+
					'                        <a href="javascript:;" class="zy-gray-sm-btn del-btn">删除</a>'+
					'                    </td>'+
					'                </tr>';
				$("#itemList").append(htmlStr);
			}

			//删除方法绑定
			$(".del-btn").click(function(){
				if(!confirm("删除后不能恢复，是否确认？")){
					return;
				}
				var delData={};
				delData.id = $(this).closest("tr").attr("flag");
				delData.type=1;
				Common.getPostData('/itempool/remove.do', delData,function(data){
					alert("操作完成!");
					itempoolMge.queryList(searchData.pageNo);
				});
			});

			$('.look-btn').click(function(){
				public_target_id = $(this).closest('tr').attr('flag');
				$.ajax({
					type: "POST",
					url: "/itempool/queryOneItem.do",
					data: {id : public_target_id},
					dataType: "json",
					success: function(data){
						var obj = data.itemDTO;
						var isShowBtn = false;
						if(obj.isSaved!=1){
							isShowBtn = true;
						}
						var html= '';
						html = html + '<p>【教材版本】</p>';
						html = html + '<p>' + obj.bk  + '</p>';
						html = html + '<p>【知识点】</p>';
						html = html + '<p>' + obj.kw + '</p>';
						html = html + '<p>【题目内容】</p>';
						html = html + '<p>' + obj.item + '</p>';
						html = html + '<p>【答案】</p>';
						html = html + '<p>' +obj.answer  + '</p> ';
						html = html + '<p>【解析】</p>';
						html = html + '<p>' + obj.parse + '<p>';
						itempoolMge.dialog(html,itempoolMge.suCallback,itempoolMge.erCallback,isShowBtn);
					}
				});

			})

		});
	}

	var public_target_id="";
	/**
	 * @func dialog
	 * @desc 页面初始化
	 * @example
	 * ziyuan.init()
	 */
	itempoolMge.dialog = function(html,suCallback,erCallback,isShowBtn){
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

	itempoolMge.suCallback=function(){
		$.ajax({
			type: "POST",
			url: "/itempool/updateStatus1.do",
			data: {id : public_target_id},
			dataType: "json",
			success: function(data){
				if(data.code==200){
					window.location.href="/itempool/frontList.do";
				}

			}
		});
	}
	itempoolMge.erCallback=function(){
		$.ajax({
			type: "POST",
			url: "/itempool/updateStatus2.do",
			data: {id : public_target_id},
			dataType: "json",
			success: function(data){
				if(data.code==200){
					window.location.href="/itempool/frontList.do";
				}

			}
		});
	}

    module.exports = itempoolMge;
})