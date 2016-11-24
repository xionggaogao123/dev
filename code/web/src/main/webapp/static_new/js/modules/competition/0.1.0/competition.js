/**
 * @author 陈小雨
 * @module  
 * @description
 *
 */
define(['common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var competition = {},
        Common = require('common');
    	require('pagination');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }
    var guolvStr = ["detailInnerBtn","editBatchConfirmBtn","editCompetitionItemAddBtn","addCompetitionConfirm","editCompetitionConfirm"];
    /**
     * @func dialog
     * @desc 模拟弹窗
     * @param {jQuery obj} jQuery
     * @example
     * qcdj.dialog(jQuery)
     */

    competition.dialog = function(obj){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
        obj.find('button,.edit-close').click(function(){
        	for(var i=0;i<guolvStr.length;i++){
        		if(guolvStr[i] == this.id){
        			return;
        		}
        	}
            $(this).closest('.pop-wrap').hide();
            $('.bg-dialog').hide();
        });
        $('body').scrollTop(0);
    };
    
    var isFirstFill = true;
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiangCheng.init()
     */ 
    competition.init = function(){
//================================================================init开始===================================================


        Common.tab('click',$('.tab-head-new'));

		//获取学期信息
		getTermTypes();

		$("#huizongTabTab").click(function(){
			getTermTypes();
			initSelectionForReportTab();
		});

        $("#importTabTab").click(function(){
			getTermTypes();
        	initSelectionForInputTab();
        });

        $("#reportTabTab").click(function(){
			getTermTypes();
			queryCompetitionsForEditTAB();
        });


		//获取学期信息
		function getTermTypes(){
			$.ajax({
				type: "POST",
				url: "/competition/termTypes.do",
				data: {},
				dataType: "json",
				async: false,
				success: function(data){
					var html="";
					$.each(data,function(i,item){
						html+='<option value="'+item.key+'">'+item.value+'</option>';
					});
					$("#editTermType").html(html);
					$("#termTypeSelection").html(html);
					$("#inputTermTypeSelection").html(html);
				}
			});
		}

		//添加评比相关
		queryCompetitionsForEditTAB();

		/**
		 * 综合评比编辑TAB页内容
		 */

		//评比编辑查询方法
		function queryCompetitionsForEditTAB(){
			var termType = $("#editTermType").val();
			$.ajax({
				type: "POST",
				url: "/competition/queryCompetitions.do",
				data: {termType : termType},
				async: false,
				dataType: "json",
				success: function(data){
					editCompetitionCatch = data.competitions;
					fillCompetitionList(data.competitions);
				}
			});
		}

		$("#editTermType").change(function(){
			queryCompetitionsForEditTAB();
		});
		$("#termTypeSelection").change(function(){
			initSelectionForReportTab();
		});


        //点击添加按钮打开对话框并将表中相应内容置空
        $('#addCompetitionBtn').on('click',function(){
        	$("#addCompetitionName").val("");
        	$("#addCompetitionPostscript").val("");
        	$("#addRange").find("input").prop("checked",false);
        	competition.dialog($('#addCompetitionWindow'));
            return false;
        });

        //查询结果缓存
        var editCompetitionCatch ;
        //初始化添加评比窗口范围年级勾选框
        document.getElementById("addRange").innerHTML = "";
        for(var i=0;i<grades.length;i++){
        	var g = grades[i];
        	var s = "<label><input type='checkbox' value='" + g.id + "'>" + g.name + "</label>";
        	$("#addRange").append(s);
        	$("#editRange").append(s);
        }
        //新增评比对话框确认按钮绑定事件
        $("#addCompetitionConfirm").click(function(){
        	var addParams = {};
        	addParams.competitionName = $("#addCompetitionName").val();
        	addParams.competitionPostscript = $("#addCompetitionPostscript").val();
        	addParams.redFlagNum = $("#addRedFlagNum").val();
			addParams.termType = $("#editTermType").val();
        	var rangeGrades = "";
    		var checkedGrades = $("input[type=checkbox]:checked",$("#addRange"));
    		for(var i=0;i<checkedGrades.length;i++){
    			var gradeCheckDom = checkedGrades[i];
    			if(gradeCheckDom.value.indexOf("ALL") == -1){
    				rangeGrades = rangeGrades + gradeCheckDom.value;
    				if(i != checkedGrades.length - 1){
    					rangeGrades = rangeGrades + ",";
    				}
    			}
    		}
    		addParams.competitionRange = rangeGrades;
    		if(rangeGrades == "" || $("#addCompetitionName").val() == ""){
    			alert("评比名称和评比范围不能为空，请填写完整！");
    			return;
    		}
    		//手动关闭弹出窗口
    		$(this).closest('.pop-wrap').hide();
            $('.bg-dialog').hide();
        	$.ajax({
        		type: "POST",
                url: "/competition/addCompetition.do",
                data: addParams,
                dataType: "json",
                success: function(data){
                	queryCompetitionsForEditTAB();
                }
        	}); 
        });

        //组装并显示评比列表
        function fillCompetitionList(srcData){
        	$("#competitionList").empty();
        	for(var i=0;i<srcData.length;i++){
        		var o = srcData[i];
        		var rangeStr = "";
        		if(o.competitionRange != ""){
        			var rangeArr = o.competitionRange.split(",");
        			for(var j=0;j<rangeArr.length;j++){
            			var r = rangeArr[j];
            			for(var k=0;k<grades.length;k++){
            				if(grades[k].id == r){
            					rangeStr = rangeStr + grades[k].name;
            				}
            			}
            			if(j != rangeArr.length - 1){
            				rangeStr = rangeStr + "、";
            			}
            		}
        		}
        		
        		var htmlStr = "<tr flag='" + o.competitionId + "'><td>" + (i + 1) +"</td><td>" + o.competitionName + "</td><td>" + o.competitionPostscript + "</td>" +
        				"<td>" + rangeStr + "</td><td><a class='config'>设置</a></td><td><a class='editBatch'>" + o.competitionBatches.length + "</a></td><td><span class='edit'>编辑</span>&nbsp;<span class='delete'>删除</span></td></tr>";
        	 
        		$("#competitionList").append(htmlStr);
        	}
        } 
        //绑定删除按钮点击事件
        $("#competitionList").delegate(".delete","click",function(){
        	if(confirm("是否确定删除？")){
        		var delId = $(this).parent().parent().attr("flag");
        		$.ajax({
            		type: "POST",
                    url: "/competition/deleteCompetition.do",
                    data: {competitionId : delId},
                    dataType: "json",
                    success: function(data){
//                    	editCompetitionCatch = data.competitions;
//                    	fillCompetitionList(data.competitions);
                    	queryCompetitionsForEditTAB();
                    	
                    }
            	}); 
        	}
        });
        //绑定编辑按钮点击事件
        $("#competitionList").delegate(".edit","click",function(){
        	var editObj = getCompetitionFromCatchById($(this).parent().parent().attr("flag"));
        	$("#editCompetitionConfirm").data("flag" , editObj.competitionId);
        	$("#editCompetitionName").val(editObj.competitionName);
        	$("#editCompetitionPostscript").val(editObj.competitionPostscript);
			$("#editRedFlagNum").val(editObj.redFlagNum);
        	var rangeCheckbox = $("input[type=checkbox]",$("#editRange"));
        	var ranges = editObj.competitionRange;
        	var arr = ranges.split(",");
        	$("#editRange").find("input").prop("checked",false);
        	for(var i=0;i<arr.length;i++){
        		if(arr[i] != ""){
        			for(var j=0;j<rangeCheckbox.length;j++){
        				if(arr[i] == rangeCheckbox[j].value){
        					$(rangeCheckbox[j]).prop("checked",true);
        				}
        			}
        		}
        	}
        	competition.dialog($('#editCompetitionWindow'));
        });
        //绑定编辑对话框确认按钮事件
        $("#editCompetitionConfirm").click(function(){
        	var editObject = {};
        	editObject.competitionId = $("#editCompetitionConfirm").data("flag");
        	editObject.competitionName = $("#editCompetitionName").val();
        	editObject.competitionPostscript = $("#editCompetitionPostscript").val();
			editObject.redFlagNum = $("#editRedFlagNum").val();
        	var rangeGrades = "";
    		var checkedGrades = $("input[type=checkbox]:checked",$("#editRange"));
    		for(var i=0;i<checkedGrades.length;i++){
    			var gradeCheckDom = checkedGrades[i];
    			if(gradeCheckDom.value.indexOf("ALL") == -1){
    				rangeGrades = rangeGrades + gradeCheckDom.value;
    				if(i != checkedGrades.length - 1){
    					rangeGrades = rangeGrades + ",";
    				}
    			}
    		}
    		editObject.competitionRange = rangeGrades;
    		if(rangeGrades == "" || $("#editCompetitionName").val() == ""){
    			alert("评比名称和评比范围不能为空，请填写完整！");
    			return;
    		}
    		//手动关闭弹出窗口
    		$(this).closest('.pop-wrap').hide();
            $('.bg-dialog').hide();
    		$.ajax({
        		type: "POST",
                url: "/competition/editCompetition.do",
                data: editObject,
                dataType: "json",
                success: function(data){
                	queryCompetitionsForEditTAB();
                }
        	}); 
        });
        //绑定设置评比项目文字点击事件
        $("#competitionList").delegate(".config","click",function(){
			//alert($(this).parent().parent().attr("flag"));
        	fillCompetitionItemList($(this).parent().parent().attr("flag"));
			$("#config2Tab").show();
			$("#configTab").hide();
        	//competition.dialog($('#editCompetitionItemWindow'));
        });
        jQuery(document).ready(function(){
			/*$(".config").click(function(){
				fillCompetitionItemList($(this).parent().parent().attr("flag"));
				$("#config2Tab").show();
				$("#configTab").hide();
			});*/
            $(".back-configtab1").click(function(){
                $("#configTab").show();
                $("#config2Tab").hide();
            });
            $("#btn-no2").click(function(){
                $(".wind-pbdetail2").fadeOut();
                $(".bg").fadeOut();
            });
			//查看一个评比项目的明细
			$("#inputCompetititonScoreTable").delegate(".i-details","click",function(){
				var competitionScoreId=$(this).parent().attr("flag");
				$("#btn-ok2").data("flag", competitionScoreId);
				$.ajax({
					type: "POST",
					url: "/competition/getComScoreItemDetailsInfo.do",
					data: {competitionScoreId:competitionScoreId},
					dataType: "json",
					success: function(data){
						var html="";
						if(data.itemDetails){
							$.each(data.itemDetails,function(i,item){
								html+='<div detailId="'+item.id+'" class="pingbi-new clearfix"><span title="'+item.itemDetailName+'">'+item.itemDetailName+':</span>';
								html+='<input type="text" name="itemDetail" oldvalue="'+item.itemDetail+'" value="'+item.itemDetail+'"></div>';
							});
						}
						$("#inputItemDetail").html(html);
						$(".wind-pbdetail2").fadeIn();
						$(".bg").fadeIn();
					}
				});
			});

			$("#btn-ok2").click(function(){
				var itemObj = {};
				itemObj.competitionScoreId = $("#btn-ok2").data("flag");
				var ids=new Array();
				var itemDetails=new Array();
				var total=0;
				$("input[name='itemDetail']").each(function() {
					if ($.trim($(this).val())!=""&&$(this).val()!=$(this).attr("oldvalue")) {
						total++;
						ids.push($(this).parent().attr("detailId"));
						itemDetails.push($(this).val());
					}
				});
				if(total>0){
					itemObj.ids=ids;
					itemObj.itemDetails=itemDetails;
					$.ajax({
						type: "post",
						url: "/competition/updateCompetitionScoreItemDetail.do",
						data:itemObj,
						async: false,
						dataType: "json",
						traditional :true,
						success: function(data){
							$(".wind-pbdetail2").fadeOut();
							$(".bg").fadeOut();
						}
					});
				}else{
					$(".wind-pbdetail2").fadeOut();
					$(".bg").fadeOut();
				}
			});

            $("body").on("mouseover",".em-mx",function(){
                $(this).prev().fadeIn();
            });
            $("body").on("mouseout",".em-mx",function(){
                $(this).prev().fadeOut();
            });/*
            $(".em-mx").mouseover(function(){
                $(this).prev().fadeIn();
            });
            $(".em-mx").mouseout(function(){
                $(this).prev().fadeOut();
            });*/

			$("#btn-ok").click(function(){
				var itemObj = {};
				itemObj.competitionId = $("#editCompetitionItemAddBtn").data("flag");
				itemObj.itemId = $("#btn-ok").data("flag");
				itemObj.detailIdsStr =$("#detailIdsStr").val();
				var total=0;
				var ids=new Array();
				var itemDetailNames=new Array();
				$("input[name='itemDetailName']").each(function() {
					if ($.trim($(this).val())!=""&&$(this).val()!=$(this).attr("oldvalue")) {
						total++;
						ids.push($(this).parent().attr("detailId"));
						itemDetailNames.push($(this).val());
					}
				});
				//if(total>0){
					itemObj.ids=ids;
					itemObj.itemDetailNames=itemDetailNames;
					$.ajax({
						type: "post",
						url: "/competition/addOrEditCompetitionItemDetails.do",
						data:itemObj,
						async: false,
						dataType: "json",
						traditional :true,
						success: function(data){
							$("#detailIdsStr").val("");
							$(".wind-pbdetail").fadeOut();
							$(".bg").fadeOut();
						}
					});
				/*}else{
					$("#detailIdsStr").val("");
					$(".wind-pbdetail").fadeOut();
					$(".bg").fadeOut();
				}*/
			});

            $("#btn-no").click(function(){
				$("#detailIdsStr").val("");
                $(".wind-pbdetail").fadeOut();
                $(".bg").fadeOut();
            });
        });

		$(".add-pb").click(function(){
			$("#pingbi-all").append("<div detailId='new' class='pingbi-new'><span>明细项</span><input type='text' maxlength='10' oldvalue='' name='itemDetailName'><i class='i-dell'>X</i></div>");
		});

        //填充评比项目小列表
        function fillCompetitionItemList(flagId){
        	$.ajax({
        		type: "POST",
                url: "/competition/queryOneCompetition.do",
                data: {competitionId : flagId},
                dataType: "json",
                success: function(data){
                	var editObj = data;
                	$("#editCompetitionItemAddBtn").data("flag",editObj.competitionId);
                	var items = editObj.competitionItems;
                	$("#editItemList").empty();
                	for(var i=0;i<items.length;i++){
                		var item = items[i];
                		var htmlStr = "<tr flag='" + item.id + "'><td>" + (i+1) + "</td>" +
                					"<td><input type='text' value='" + item.itemName + "' class='inputOfTable'/></td>" + 
                					"<td><input type='text' value='" + item.itemPostscript + "' class='inputOfTable'/></td>" + 
                					"<td><input type='text' value='" + item.itemFullScore + "' class='inputOfTable'/></td>" +
                					"<td><span class='span-cur span-detail'>明细</span><i class='i-l'></i><span class='span-cur item-del'>删除</span></td></tr>";
                		$("#editItemList").append(htmlStr); 
                	}
                }
        	}); 
        }
        //添加一个评比项目的方法绑定
        $("#editCompetitionItemAddBtn").click(function(){
        	var cId = $(this).data("flag");
        	$.ajax({
        		type: "POST",
                url: "/competition/addCompetitionItem.do",
                data: {competitionId : cId},
                dataType: "json",
                success: function(data){
                	queryCompetitionsForEditTAB();
                	fillCompetitionItemList(cId);
                	initSelectionForReportTab();
                }
        	}); 
        });

		//查看一个评比项目的明细
		$("#editItemList").delegate(".span-detail","click",function(){
			var itemObj = {};
			itemObj.itemId = $(this).parent().parent().attr("flag");
			$("#btn-ok").data("flag", itemObj.itemId);
			$.ajax({
				type: "POST",
				url: "/competition/getCompetitionItemDetails.do",
				data: itemObj,
				dataType: "json",
				success: function(data){
					var html="";
					if(data.itemDetails){
						$.each(data.itemDetails,function(i,item){
							html+='<div detailId="'+item.id+'" class="pingbi-new"><span>明细项</span>';
							html+='<input type="text" name="itemDetailName" maxlength="10" oldvalue="'+item.itemDetailName+'" value="'+item.itemDetailName+'">';
							html+='<i class="i-dell">X</i></div>';
						});
					}

					if(html==""){
						html="<div detailId='new' class='pingbi-new'><span>明细项</span><input type='text' maxlength='10' oldvalue='' name='itemDetailName'><i class='i-dell'>X</i></div>";
					}
					$("#pingbi-all").html(html);
					$(".wind-pbdetail").fadeIn();
					$(".bg").fadeIn();
				}
			});
		});

		$("#pingbi-all").delegate(".i-dell","click",function(){
			var detailIdsStr=$("#detailIdsStr").val();
			if(detailIdsStr==''){
				detailIdsStr=$(this).parent().attr("detailId");
			}else{
				detailIdsStr+=","+$(this).parent().attr("detailId");
			}
			$("#detailIdsStr").val(detailIdsStr);
			$(this).parent().fadeOut();
		});

        //删除一个评比项目
        $("#editItemList").delegate(".item-del","click",function(){
        	if(confirm("是否确认删除？")){
        		var delItemObj = {};
            	var cId = $("#editCompetitionItemAddBtn").data("flag");
            	delItemObj.competitionId = cId;
            	delItemObj.itemId = $(this).parent().parent().attr("flag");
            	$.ajax({
            		type: "POST",
                    url: "/competition/delCompetitionItem.do",
                    data: delItemObj,
                    dataType: "json",
                    success: function(data){
                    	queryCompetitionsForEditTAB();
                    	fillCompetitionItemList(cId);
                    	initSelectionForReportTab();
                    }
            	}); 
        	}
        });


        //给评比项目设置里的每一个input窗添加失焦保存事件
        $("#editItemList").delegate("input","change",function(){
        	var editItemObj = {};
        	editItemObj.competitionId = $("#editCompetitionItemAddBtn").data("flag");
        	editItemObj.itemId = $(this).parent().parent().attr("flag");
        	editItemObj.itemName = $(this).parent().parent().find("input").get(0).value;
        	editItemObj.itemPostscript = $(this).parent().parent().find("input").get(1).value;
        	editItemObj.itemFullScore = $(this).parent().parent().find("input").get(2).value;
        	$.ajax({
        		type: "POST",
                url: "/competition/editCompetitionItem.do",
                data: editItemObj,
                dataType: "json",
                success: function(data){
                	queryCompetitionsForEditTAB();
                }
        	}); 
        	
        });
        //点击列表中批次设置数字打开设置窗口的方法
        $("#competitionList").delegate(".editBatch","click",function(){
        	var batchNum = $(this).html();
        	var targetCompetitionId = $(this).parent().parent().attr("flag");
        	$("#editBatchNum").val(batchNum);
        	$("#editBatchConfirmBtn").data("flag",targetCompetitionId);
        	fillCompetitionBatchList(targetCompetitionId);
        	competition.dialog($('#editCompetitionBatchWindow'));
        });
        //填充评比批次小列表
        function fillCompetitionBatchList(competitionId){
        	$.ajax({
        		type: "POST",
                url: "/competition/queryOneCompetition.do",
                data: {competitionId : competitionId},
                dataType: "json",
                success: function(data){
                	var editObj = data; 
                	var batches = editObj.competitionBatches;
                	$("#editBatchList").empty();
                	for(var i=0;i<batches.length;i++){
                		var batch = batches[i];
                		var htmlStr = "<tr flag='" + batch.id + "'><td>" + (i+1) + "</td>" +
                					"<td><input type='text' value='" + batch.batchName + "' class='inputOfTable'/></td></tr>";
                		$("#editBatchList").append(htmlStr); 
                	}
                }
        	}); 
        }
        //确认根据批次数生成新批次按钮
        $("#editBatchConfirmBtn").click(function(){
        	if(confirm("注意！重新生成批次将清除该评比所有已录入的分数信息！是否确认？")){
        		var createBatchObj = {};
        		var competitionId = $(this).data("flag");
        		var batchNum = $("#editBatchNum").val(); 
        		createBatchObj.competitionId = competitionId;
        		createBatchObj.batchNum = batchNum;
        		$.ajax({
            		type: "POST",
                    url: "/competition/createNewBatches.do",
                    data: createBatchObj,
                    dataType: "json",
                    success: function(data){
                    	queryCompetitionsForEditTAB();
                    	fillCompetitionBatchList(competitionId);
                    	initSelectionForInputTab();
                    	initSelectionForReportTab()
                    }
            	}); 
        		
        	}
        });
        //保存批次设置按钮方法
        $("#editBatchSaveBtn").click(function(){
        	var competitionId = $("#editBatchConfirmBtn").data("flag");
        	var batchesStr = "";
        	var trs = $("#editBatchList").find("tr");
        	for(var i=0;i<trs.length;i++){
        		var targetInput = $(trs.get(i)).find("input").get(0);
        		batchesStr = batchesStr + $(trs.get(i)).attr("flag") + "," + $(targetInput).val();
        		if(i != trs.length - 1){
        			batchesStr = batchesStr + ";";
        		}
        	}
        	$.ajax({
        		type: "POST",
                url: "/competition/updateBatches.do",
                data: {competitionId:competitionId,batchesStr:batchesStr},
                dataType: "json",
                success: function(data){
//                	queryCompetitionsForEditTAB();
//                	fillCompetitionBatchList(competitionId);
                }
        	}); 
        });
        //根据id从缓存那种拿取相应的一条数据
        function getCompetitionFromCatchById(cId){ 
        	var targetObj;
        	for(var i=0;i<editCompetitionCatch.length;i++){
        		if(editCompetitionCatch[i].competitionId == cId){
        			targetObj = editCompetitionCatch[i];
        		}
        	}
        	return targetObj;
        }
        
      /**
       * 综合评比成绩录入TAB
       */
        var inputPageNo = 1;
    	var inputPageSize = 20;
    	var inputIsFirstPage = true;
        //查询出来的所有competitions缓存
        var competitionsCatchForInput = {};
      //初始化成绩录入TAB页中的查询下拉选框
        function initSelectionForInputTab(){
			var termType = $("#inputTermTypeSelection").val();
        	$.ajax({
        		type: "POST",
                url: "/competition/getCompetitionsForSelection.do",
                data: {termType:termType},
                dataType: "json",
                success: function(data){
                	competitionsCatchForInput = data.competitions;
                	$("#inputCompetitionSelection").html("");
                	$("#inputBatchSelection").html("");
					$("#inputGradeSelection").html("<option value='ALL'>全部</option>");
					var first=true;
                	for(var i=0;i<competitionsCatchForInput.length;i++){
                		var subCom = competitionsCatchForInput[i];
						if(subCom.competitionItems.length>0) {
							$("#inputCompetitionSelection").append("<option value='" + subCom.competitionId + "'>" + subCom.competitionName + "</option>");
							var subBatches = subCom.competitionBatches;
							//if (i == 0) {
							if (first) {
								first=false;
								changeLabelsForInputTab(subCom);
								for (var j = 0; j < subBatches.length; j++) {
									var subBatch = subBatches[j];
									$("#inputBatchSelection").append("<option value='" + subBatch.id + "'>" + subBatch.batchName + "</option>")
								}
							}

							var subGrades = subCom.competitionRange.split(",");
							if(i == 0 && subGrades[0] != ""){
								for(var j=0;j<subGrades.length;j++){
									var subGrade = subGrades[j];
									var subGradeName = "";
									for(var k=0;k<grades.length;k++){
										if(grades[k].id == subGrade){
											subGradeName = grades[k].name;
										}
									}
									$("#inputGradeSelection").append("<option value='" + subGrade + "'>" + subGradeName + "</option>")
								}
							}
						}
                	}
					inputIsFirstPage=false;
                	createAndFillScoreList();
                }
        	}); 
        }

		$("#inputTermTypeSelection").change(function(){
			initSelectionForInputTab();
		});

        //评比下拉框change绑定方法
        $("#inputCompetitionSelection").change(function(){
        	$("#inputBatchSelection").html("");
			$("#inputGradeSelection").html("<option value='ALL'>全部</option>");
        	for(var i=0;i<competitionsCatchForInput.length;i++){
        		var subCom = competitionsCatchForInput[i];

        		var subBatches = subCom.competitionBatches;
        		if(subCom.competitionId == $(this).val()){
        			changeLabelsForInputTab(subCom);
        			for(var j=0;j<subBatches.length;j++){
            			var subBatch = subBatches[j];
            			$("#inputBatchSelection").append("<option value='" + subBatch.id + "'>" + subBatch.batchName + "</option>")
            		}

					var subGrades = subCom.competitionRange.split(",");
					for(var j=0;j<subGrades.length;j++){
						var subGrade = subGrades[j];
						var subGradeName = "";
						for(var k=0;k<grades.length;k++){
							if(grades[k].id == subGrade){
								subGradeName = grades[k].name;
							}
						}
						$("#inputGradeSelection").append("<option value='" + subGrade + "'>" + subGradeName + "</option>")
					}
        		}
        	}
			inputIsFirstPage=false;
        	createAndFillScoreList();
        });
        $("#inputBatchSelection").change(function(){
			inputIsFirstPage=false;
        	createAndFillScoreList();
        });
		$("#inputGradeSelection").change(function(){
			inputIsFirstPage=false;
			createAndFillScoreList();
		});

        //修改表头等label信息
        function changeLabelsForInputTab(subCom){
			$(".luru-detail").show();
        	$("#inputCompetitionNameLabel").html(subCom.competitionName);
			$("#inputCompetitionPostscriptLabel").html(subCom.competitionPostscript);
			
			var o = subCom;
    		var rangeStr = "";
    		if(o.competitionRange != ""){
    			var rangeArr = o.competitionRange.split(",");
    			for(var j=0;j<rangeArr.length;j++){
        			var r = rangeArr[j];
        			for(var k=0;k<grades.length;k++){
        				if(grades[k].id == r){
        					rangeStr = rangeStr + grades[k].name;
        				}
        			}
        			if(j != rangeArr.length - 1){
        				rangeStr = rangeStr + "、";
        			}
        		}
    		}
			
			$("#inputCompetitionRangeLabel").html(rangeStr);
			$("#inputCompetitionBatchLabel").html("共 " + subCom.competitionBatches.length + " 批次");
        }
        //评比成绩录入表单的创建和生成方法
        function  createAndFillScoreList(pageNo){
        	var queryScoreListObj = {};
        	queryScoreListObj.competitionId = $("#inputCompetitionSelection").val();
        	queryScoreListObj.competitionBatch = $("#inputBatchSelection").val();
			queryScoreListObj.gradeId = $("#inputGradeSelection").val();
        	queryScoreListObj.pageNo = pageNo==null?1:pageNo;
        	queryScoreListObj.pageSize = inputPageSize;
        	$.ajax({
        		type: "POST",
                url: "/competition/createAndQueryScoreList.do",
                data: queryScoreListObj,
				async: false,
                dataType: "json",
                success: function(data){
					if(data.pagejson.total>0) {
						$('#inputPageDiv').jqPaginator({
							totalPages: Math.ceil(data.pagejson.total / inputPageSize) == 0 ? 1 : Math.ceil(data.pagejson.total / inputPageSize),//总页数
							visiblePages: 10,//分多少页
							currentPage: queryScoreListObj.pageNo,//当前页数
							first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
							prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
							next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
							last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
							page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
							onPageChange: function (n) { //回调函数
								//点击页码的查询
//         	            	alert('当前第' + n + '页'); 
								if (inputIsFirstPage) {
									inputIsFirstPage = false;
									//if (n != queryScoreListObj.pageNo) {
										createAndFillScoreList(n);
									//}

								} else {
									inputIsFirstPage = true;
								}
							}
						});
					}
                	//组装显示
                	var competitionScroes = data.competitionScroes;
                	var competitionItems = data.competitionItems;
                	var itemNum = competitionItems.length;
                	var tableDom = $("#inputCompetititonScoreTable");
                	tableDom.html("");
                	tableDom.append("<tr><td class='white' colspan='4'>" +
                			"</td><td class='xuanzhong' colspan='" + itemNum + "'>" +
                					"" + $("#inputBatchSelection option:selected").text() + "</td></tr>");
                	var itemsTitleHtml = "<tr class='hz-title'><td width='72'>#</td><td width='67'>年级 </td><td width='57'>班级</td><td width='71'>得分</td>";
                	for(var i=0;i<competitionItems.length;i++){
                		var subItem = competitionItems[i];
                		itemsTitleHtml = itemsTitleHtml + "<td class='active' width='103' flag='" + subItem.itemId + "'>" +
                									subItem.itemName + "</br>(" + subItem.itemFullScore + ")</td>";
                	}
                	itemsTitleHtml = itemsTitleHtml + "</tr>";
                	tableDom.append(itemsTitleHtml);
                	$("#inputCompetititonScoreList").empty();
                	for(var i=0;i<competitionScroes.length;i++){
                		var subScore = competitionScroes[i];
                		var htmlStr = "<tr class='list-detail'><td width='72'>" + (i+1+(queryScoreListObj.pageNo-1)*inputPageSize) + "</td><td width='67'>" + subScore.gradeName + "</td>" +
                				"<td width='57'>" + subScore.className + "</td>" +
                				"<td width='71'>" + subScore.allScore + "</td>" ;
                		var subItemScores = subScore.itemScores;
						var itemDetailCounts = subScore.itemDetailCounts;
                		for(var j=0;j<subItemScores.length;j++){
							var itemDetailCount = itemDetailCounts[j];
							var htmlSubStr="";
							if(itemDetailCount>0){
								htmlSubStr=" <i class='i-details'>详情</i> ";
							}
                			htmlStr = htmlStr + "<td width='103' flag='" + subItemScores[j].scoreId + "' fullScore='" + competitionItems[j].itemFullScore + "'>" +
                					"<input type='text' class='inputOfTable onKeyUpScore' value='" + subItemScores[j].score + "'/>" +
								htmlSubStr+
								"</td>";
                		}
                		htmlStr = htmlStr + "</tr>";	 
                		tableDom.append(htmlStr);
                	}
					$('.onKeyUpScore').keyup(function(){
						//1.判断是否有多于一个小数点
						var index1 = $(this).val().indexOf(".") + 1;//取第一次出现.的后一个位置
						var index2 = $(this).val().indexOf(".",index1);
						while(index2!=-1) {
							//alert("有多个.");

							$(this).val($(this).val().substring(0,index2));
							index2 = $(this).val().indexOf(".",index1);
						}
						//2.如果输入的不是.或者不是数字，替换 g:全局替换
						$(this).val($(this).val().replace(/[^(-?\d|.)]/g,""));
					});
                }
        	}); 
        }

        //成绩录入表单input绑定change修改方法。
        $("#inputCompetititonScoreTable").delegate("input","change",function(){
        	if (isNaN($(this).val())) {
				alert("请输入数字!");
				$(this).val("");
				this.focus();
				return ;
        	};
        	var fullStr = $(this).parent().attr("fullScore");
        	if(parseFloat($(this).val()) > parseInt(fullStr)){
        		alert("输入分数大于满分，请重新输入!"); 
    			$(this).val("");
				this.focus();
				return ;
        	}
        	
        	var inputs = $(this).parent().parent().find("input"); 
        	var sumTd = $($(this).parent().parent().find("td").get(3));
        	 $.ajax({
         		type: "POST",
                 url: "/competition/updateCompetitionScore.do",
                 data: {competitionScoreId:$(this).parent().attr("flag"),score:$(this).val()},
                 dataType: "json",
                 success: function(data){
                	 if(data.code == "200"){
                      	var sum = 0;
                      	for(var i=0;i<inputs.length;i++){
                      		sum = sum + (inputs.get(i).value * 1);
                      	}
                      	sumTd.html(sum + ""); 
                	 }
                 }
         	}); 
        });
        
     /**
      * 汇总页面
      */
        var reportPageNo = 1;
    	var reportPageSize = 20;
    	var reportIsFirstPage = true;
        //点击明细弹窗
        $("#showDetailBtn").click(function(){
        	competition.dialog($('#detailDialog')); 
        });
      //查询出来的所有competitions缓存
        var competitionsCatchForReport = {};

        //初始化下拉选框
        //initSelectionForReportTab();

        function initSelectionForReportTab(){
			var termType = $("#termTypeSelection").val();
			$.ajax({
        		type: "POST",
                url: "/competition/getCompetitionsForSelection.do",
                data: {termType : termType},
                dataType: "json",
                success: function(data){
                	competitionsCatchForReport = data.competitions;
                	$("#competitionSelectionForReport").html("");
                	/*$("#gradeSelectionForReport").html("<option value='ALL'>全部</option>");*/
					$("#gradeSelectionForReport").html("");
                	for(var i=0;i<competitionsCatchForReport.length;i++){ 
                		var subCom = competitionsCatchForReport[i];
                		$("#competitionSelectionForReport").append("<option value='" + subCom.competitionId + "'>" + subCom.competitionName + "</option>");
                		var subGrades = subCom.competitionRange.split(",");
                		if(i == 0 && subGrades[0] != ""){
                			for(var j=0;j<subGrades.length;j++){
                    			var subGrade = subGrades[j];
                    			var subGradeName = "";
                    			for(var k=0;k<grades.length;k++){
                    				if(grades[k].id == subGrade){
                    					subGradeName = grades[k].name;
                    				}
                    			}
                    			$("#gradeSelectionForReport").append("<option value='" + subGrade + "'>" + subGradeName + "</option>")
                    		}
                		}
                	}
					reportIsFirstPage=false;
                	queryAndUploadReportAndDetailList(1);
                }
        	}); 
        }
        //绑定下拉菜单改变事件方法
        $("#competitionSelectionForReport").change(function(){
        	/*$("#gradeSelectionForReport").html("<option value='ALL'>全部</option>");*/
			$("#gradeSelectionForReport").html("");
        	var subCom;
        	for(var i=0;i<competitionsCatchForReport.length;i++){
        		if(competitionsCatchForReport[i].competitionId == $(this).val()){
        			subCom = competitionsCatchForReport[i];
        		}
        	}
    		var subGrades = subCom.competitionRange.split(",");
        	for(var j=0;j<subGrades.length;j++){
    			var subGrade = subGrades[j];
    			var subGradeName = "";
    			for(var k=0;k<grades.length;k++){
    				if(grades[k].id == subGrade){
    					subGradeName = grades[k].name;
    				}
    			}
    			$("#gradeSelectionForReport").append("<option value='" + subGrade + "'>" + subGradeName + "</option>")
    		}
			reportIsFirstPage=false;
        	queryAndUploadReportAndDetailList(1);
        });

        $("#gradeSelectionForReport").change(gradeSelectionForReportChagen);

        function gradeSelectionForReportChagen(){
			reportIsFirstPage=false;
        	queryAndUploadReportAndDetailList(1);
        }
        //查询装填内外列表总方法
        function queryAndUploadReportAndDetailList(pageNo){
        	var queryReportInfoObj = {};
        	queryReportInfoObj.competitionId = $("#competitionSelectionForReport").val();
        	queryReportInfoObj.gradeId = $("#gradeSelectionForReport").val();
        	queryReportInfoObj.pageNo = pageNo==null?1:pageNo;
        	queryReportInfoObj.pageSize =reportPageSize;
        	
        	$.ajax({
         		type: "POST",
                 url: "/competition/queryReportCompetitionList.do",
                 data: queryReportInfoObj,
				 async: false,
                 dataType: "json",
                 success: function(data){
					 if(data.pagejson.total>0){
						 $('#reportPageDiv').jqPaginator({
							totalPages: Math.ceil(data.pagejson.total/reportPageSize) == 0?1:Math.ceil(data.pagejson.total/reportPageSize),//总页数
							visiblePages: 10,//分多少页
							currentPage: queryReportInfoObj.pageNo,//当前页数
							first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
							prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
							next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
							last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
							page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
							onPageChange: function (n) { //回调函数
								//点击页码的查询
	//          	            	alert('当前第' + n + '页');
								if(reportIsFirstPage){
									reportIsFirstPage = false;
									//if(n!=queryReportInfoObj.pageNo){
										queryAndUploadReportAndDetailList(n);
									//}
								}else{
									reportIsFirstPage = true;
								}
							}
						});
					 }
                	 //公共变量
                	  var dataBatches = data.batches;
               	  	  var dataItems = data.items;
               	  	  var dataScores = data.datas;
					  if(dataBatches.length>0){
						  $(".huizong-list").show();
						  //拼出汇总表
						  $reportTableHead = $("#reportTableHead");
						  $reportTableBody = $("#reportTableList");
						  var headHtml = "<tr><th width='90'>排名</th><th> 年级 </th><th>班级</th><th>总分 </th><th>平均分 </th>";
						  for(var i=0;i<dataBatches.length;i++){
							  headHtml = headHtml + "<th>" +  dataBatches[i].batchName + " 分数</th>";
						  }
						  headHtml = headHtml + "</tr>";
						  $reportTableHead.html(headHtml);

						  $reportTableBody.html("");
						  for(var i=0;i<dataScores.length;i++){
							  var subData = dataScores[i];
							  var batchScores = subData.batches;
							  var gradeName = "";
							  for(var j=0;j<grades.length;j++){
								  if(grades[j].id == subData.gradeId){
									  gradeName = grades[j].name;
								  }
							  }

							  var bodyHtml = "<tr>";
							  if(subData.redFlag==1){
								  bodyHtml = bodyHtml + "<td class='competition-TD'>"+ subData.ranking + "</td>";
							  }else{
								  bodyHtml = bodyHtml + "<td>"+ subData.ranking + "</td>";
							  }
							  bodyHtml = bodyHtml + "<td>" + gradeName + "</td>";
							  bodyHtml = bodyHtml + "<td>" + subData.className + "</td>";
							  bodyHtml = bodyHtml + "<td>" + subData.allTotalScore + "</td>";
							  bodyHtml = bodyHtml + "<td>" + subData.averageScore + "</td>";
							  for(var j=0;j<batchScores.length;j++){
								  bodyHtml = bodyHtml + "<td>" + batchScores[j].allScore + "</td>";
							  }
							  bodyHtml = bodyHtml + "</tr>";
							  $reportTableBody.append(bodyHtml);
						  }
						  //拼出明细表
						  $detailTableBody = $("#detailTableBody");
						  $detailTableBody.empty();
						  var itemNum = dataItems.length;
						  var batchNum = dataBatches.length;
						  var detailHtml = "<tr><td class='white' colspan='4'></td>";
						  for(var i=0;i<batchNum;i++){
							  detailHtml = detailHtml + "<td class='xuanzhong' colspan='" + (itemNum + 1) + "'>" + dataBatches[i].batchName + "</td>";
							  if(i == batchNum - 1){
								  detailHtml = detailHtml + "</tr>";
							  }else{
								  detailHtml = detailHtml + "<td width='2'></td>";
							  }
						  }
						  detailHtml = detailHtml + "<tr class='hz-title'><td width='72'><div class='div72'>排名</div></td><td width='67'><div class='div67'></div>年级 </td><td width='57'><div class='div57'>班级</div></td><td width='71'> <div class='div71'>总分</div> </td><td width='71'> <div class='div71'>平均分</div> </td>";
						  for(var i=0;i<batchNum;i++){
							  for(var j=0;j<itemNum;j++){
								  detailHtml = detailHtml + "<td class='active' width='103'><div class='div103'>" + dataItems[j].itemName + "</br>(" + dataItems[j].itemFullScore + ")</div></td>";
							  }
							  detailHtml = detailHtml + "<td class='active'  width='72'><div class='div72'>得分</div></td>";
							  if(i != batchNum - 1){
								  detailHtml = detailHtml + "<td width='2'></td>";
							  }else{
								  detailHtml = detailHtml + "</tr>";
							  }
						  }
						  for(var i=0;i<dataScores.length;i++){
							  var subData = dataScores[i];
							  var gradeName = "";
							  for(var j=0;j<grades.length;j++){
								  if(grades[j].id == subData.gradeId){
									  gradeName = grades[j].name;
								  }
							  }
							  detailHtml = detailHtml + "<tr class='list-detail'><td width='72'>" + subData.ranking + "</td>";
							  detailHtml = detailHtml + "<td width='67'>" + gradeName + " </td><td width='57'>" + subData.className +"</td>" +
								  "<td width='71'>" + subData.allTotalScore + "</td><td width='71'>" + subData.averageScore + "</td>";
							  var subBatches = subData.batches;
							  for(var j=0;j<subBatches.length;j++){
								  var subBatch = subBatches[j];
								  var subItems = subBatch.items;
								  var itemDetails = subBatch.itemDetails;
								  for(var k=0;k<subItems.length;k++){
									  if(itemDetails[k].length>0){
										  var detailSubHtml="<div class='div-hover'>";
										  $.each(itemDetails[k],function(i,item) {
											  detailSubHtml+="<div>"+item.itemDetailName+":<span>"+item.itemDetail+"</span></div>";
										  });
										  detailSubHtml+="</div>";
										  detailHtml = detailHtml + "<td class='active' width='103'>"
											  +detailSubHtml+ subItems[k] + "  <em class='em-mx'>明细</em> </td>";
									  }else{
										  detailHtml = detailHtml + "<td class='active' width='103'>" + subItems[k] + "</td>";
									  }
								  }
								  detailHtml = detailHtml + "<td class='active'  width='72'>" + subBatch.allScore + "</td>";
								  if(j != subBatches.length - 1){
									  detailHtml = detailHtml + "<td width='2'></td>";
								  }else{
									  detailHtml = detailHtml + "</tr>";
								  }
							  }
						  }
						  $detailTableBody.html(detailHtml);
					  }else{
						  $(".huizong-list").hide();
					  }
                 }
         	}); 
        }
        //导出汇总表
        $("#exportReportBtn").click(function(){
        	$("#competitionIdFormReport").val($("#competitionSelectionForReport").val());
        	$("#gradeIdFormReport").val($("#gradeSelectionForReport").val());  
        	$("#exportFormReport").submit();
        });
      //导出明细表
        $("#exportDetailBtn,#detailInnerBtn").click(function(){ 
        	$("#competitionIdFormDetail").val($("#competitionSelectionForReport").val());
        	$("#gradeIdFormDetail").val($("#gradeSelectionForReport").val()); 
        	$("#exportFormDetail").submit(); 
        });
//================================================================init结束===================================================
    };

    (function(){
    	competition.init();
    })();

    module.exports = competition;
})