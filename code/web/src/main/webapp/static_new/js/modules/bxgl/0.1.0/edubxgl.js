/**
 * Created by liwei on 15/7/12.
 */
/**
 * @author 李伟
 * @module  报修管理操作
 * @description
 * 报修管理操作
 */
/* global Config */
define(['common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var edubxgl = {},
        Common = require('common');
    			require('pagination');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }

    edubxgl.dialog = function(obj,callback){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
        $(document).scrollTop(0);
    };

    edubxgl.close = function(obj){
        obj.closest('.pop-wrap').hide();
        $('.bg-dialog').hide();
    };


    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiangCheng.init()
     */
    edubxgl.init = function(){


        Common.tab('click',$('.tab-head-new'))
  
        //********教育局查询页面***********************************************************************************************
      
         $(document).delegate('.content-view-manage','click',function(){
			var rcDetail;
			for(var i=0;i<queryManageCatch.repairData.length;i++){
				if(queryManageCatch.repairData[i].id == $(this).parent().parent().attr("flag")){
					rcDetail = queryManageCatch.repairData[i];
				}
			}
			document.getElementById("repairContentForView").innerHTML = rcDetail.repairContent==null?"":rcDetail.repairContent;
			document.getElementById("repairContentForReason").innerHTML = rcDetail.reportReason == null?"":rcDetail.reportReason;
            Common.dialog($('#bxcontentId'));
            return false;
        })
        //指定维修人员按钮绑定事件
        $(document).delegate('.icon-set','click',function(){
        	$("#eduAssign").data("flag",$(this).parent().parent().attr("flag"));
        	edubxgl.dialog($('#opstatusId'));
            return false;
        })
          //结果填写按钮绑定事件
        $(document).delegate('.icon-result','click',function(){
			$("#repairResultSubmitBtn").data("flag",$(this).parent().parent().attr("flag"));
			$("#repairResult").val("");
			edubxgl.dialog($('#bxresultId')); 
            return false;
        })
         //查看教育局报修结果情况
        $(document).delegate('.content-result-manage','click',function(){
			var rcDetail;
			for(var i=0;i<queryManageCatch.repairData.length;i++){
				if(queryManageCatch.repairData[i].id == $(this).parent().parent().attr("flag")){
					rcDetail = queryManageCatch.repairData[i];
				}
			}
			document.getElementById("repairResultForView").innerHTML = rcDetail.repairResult==null?"":rcDetail.repairResult;
            Common.dialog($('#jgcontentId')); 
            return false;
        })
        //初始化表头
		changeListTitle();
		//表头自动变化方法和下拉菜单绑定方法
		function changeListTitle(){
			var titleText = "报修明细  " + $("#eduHandlershools option:selected").text() + "(" + $("#eduHandlerTermType option:selected").text() + ")"  ;
			$("#eduManageListTitle").html(titleText);
		}
        
		//管理报修页面查询缓存
        var queryManageCatch;
		//管理报修页面查询参数缓存
		var queryObjForQueryManage = {};
		var publicPageSizeForQueryManage = 15;
		//分页组件中默认为第一页
		var isFirstPageForQueryManage = true;
        
//	  //为查询按钮添加点击事件
//        $("#eduSearch").click(function(){
//        	//changeListTitle();
//        	alert("124");
//        });
        
      //管理报修页面查询方法
        $("#queryEduHandlerListBtn").click(queryFirstForManage);
        function queryFirstForManage(){
        	uploadQueryObjForQueryManage(1);
        }
        
      //查询参数组装并查询
        function uploadQueryObjForQueryManage(pageNo){
        	queryObjForQueryManage.termType = $("#eduHandlerTermType").val()==null?"All":$("#eduHandlerTermType").val();
        	queryObjForQueryManage.repairShoolId = $("#eduHandlershools").val() == null ? "All":$("#eduHandlershools").val();
        	queryObjForQueryManage.repairProcedure = $("#eduHandlerRepairProcedure").val() == null ? "All":$("#eduHandlerRepairProcedure").val();
        	queryObjForQueryManage.pageNo = pageNo;
        	queryObjForQueryManage.pageSize = publicPageSizeForQueryManage;
        	queryRepairsForManage(null);
        }
        
      //管理报修页面查询方法
        function queryRepairsForManage(pageNo){
        	var qObj = {};
        	qObj.termType = queryObjForQueryManage.termType ;
        	qObj.repairShoolId = queryObjForQueryManage.repairShoolId;
        	qObj.repairProcedure = queryObjForQueryManage.repairProcedure;
        	qObj.pageSize = queryObjForQueryManage.pageSize;
        	if(pageNo == null){
        		qObj.pageNo = queryObjForQueryManage.pageNo;
        	}else{
        		qObj.pageNo = pageNo;
        	}
        	
        	$.ajax({
        		type: "POST",
                url: "/repair/findEduList.do",
                data: qObj,
                dataType: "json", 
                success: function(data){
                	changeListTitle();
                	queryManageCatch = data;
                	//jqPaginator为分页组件
                	$('#pageForEdu').jqPaginator({
          	            totalPages: Math.ceil(data.pagejson.total/publicPageSizeForQueryManage) == 0?1:Math.ceil(data.pagejson.total/publicPageSizeForQueryManage),//总页数  
          	            visiblePages: 10,//分多少页
          	            currentPage: parseInt(data.pagejson.pageNo),//当前页数
          	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
          	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
          	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
          	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
          	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
          	            onPageChange: function (n) { //回调函数
          	            	//点击页码的查询
//          	            	alert('当前第' + n + '页'); 
          	            	if(isFirstPageForQueryManage){
          	            		queryRepairsForManage(n);
          	            		isFirstPageForQueryManage = false;
          	            	}else{
          	            		isFirstPageForQueryManage = true; 
          	            	}
          	            }
          	        });
                	fillQueryManageList(data.repairData);
                }
        	}); 
        }
        
      //为管理报修页面表单组装内容
        function fillQueryManageList(repairArr){
//        	$("#queryList").html("<tbody></tbody>");
        	document.getElementById("handlerEduList").innerHTML = "";
        	for(var i=0;i<repairArr.length;i++){
        		var subObj = repairArr[i];
        		var placeName = subObj.repairPlace;
        		if(placeName.length>10){
        			placeName = subObj.repairPlace.substring(0,13)+"...";
        		}
        		var trHtml = "<tr flag='" + subObj.id +"'><td>";//id
        		trHtml = trHtml + subObj.schoolName + "</td><td>";//学校名字
        		trHtml = trHtml + subObj.repairDate + "</td><td>";//报修日期
        		trHtml = trHtml + subObj.manager + "</td><td style='cursor:pointer' title="+subObj.repairPlace+">";//报修人
        		trHtml = trHtml + placeName + "</td><td>";//报修地点
        		trHtml = trHtml + "<a href='#' class='content-view-manage'>查看</a>" +"</td><td>";//内容查看
        		//进度筛选
        		if(subObj.isReport == 1 && subObj.isResolve == 0){
        			trHtml = trHtml + "待处理" + "</td><td>";
        		}else if(subObj.repairProcedure == "已完毕"){
        			trHtml = trHtml + "已完毕" + "</td><td>";
        		}else if(subObj.isReport == 1 && subObj.isResolve == 1){
        			trHtml = trHtml + "已受理" + "</td><td>";
        		}
        		//维修人员
        		trHtml = trHtml + ( subObj.solveRepairPersonName==null?"":subObj.solveRepairPersonName ) + "</td><td>";
        		//查看结果
        		trHtml = trHtml + ( subObj.repairResult==null?"":"<a href='#' class='content-result-manage'>查看</a>" ) + "</td><td>";
        		//操作
        		if(subObj.isReport == 1 && subObj.isResolve == 0){
        			trHtml = trHtml + "<a href='#' class='icon-set'>";
        		}else if(subObj.repairProcedure == "已完毕"){
        			trHtml = trHtml + "";
        		}else if(subObj.isResolve == 1 && subObj.repairProcedure == "已受理"){
        			trHtml = trHtml + "<a href='#' class='icon-result'>";
        		}
        		
        		trHtml = trHtml + "</td></tr>";
        		document.getElementById("handlerEduList").innerHTML = document.getElementById("handlerEduList").innerHTML +  trHtml;
        		//
        	}
        }
        //更新指派维修人员
        $("#eduAssign").on("click",function(){
        	var updateObj = {};
        	updateObj.id = $("#eduAssign").data("flag");
        	updateObj.solveRepairPersonName = $("#eduResolvePerson").val();
        	if($.trim(updateObj.solveRepairPersonName) == ""){
        		alert("请填写维修人员！");
        		return ;
        	}
        	$.ajax({
        		type: "post",
        		url: "/repair/updateEduRepairs.do",
        		data: updateObj,
        		dataType: "json",
        		success: function(data){
        			alert("指派维修人员成功！");
        			edubxgl.close($("#eduAssign"));
        			$("#eduResolvePerson").val("");
        			queryRepairsForManage();
        		}
        	});
        });
        //关闭维修人员框
        $('#closeRepairBtn').on('click',function(){
        	edubxgl.close($("#eduAssign"));
        	$("#eduResolvePerson").val("");
        });
        //更新维修结果
        $("#repairResultSubmitBtn").on("click",function(){
        	
        	var updateObj = {};
        	updateObj.id = $("#repairResultSubmitBtn").data("flag");
        	updateObj.repairResult = $("#repairResult").val();
        	if($.trim(updateObj.repairResult) == ""){
        		alert("请填写维修结果！");
        		return ;
        	}
        	$.ajax({
        		type: "post",
        		url: "/repair/updateEduResult.do",
        		data: updateObj,
        		dataType: "json",
        		success: function(data){
        			alert("维修结果保存成功！");
        			edubxgl.close($("#repairResultSubmitBtn"));
        			$("#repairResult").val("");
        			queryRepairsForManage();
        		}
        	});
        });
        //登记维修结果取消按钮事件绑定
        $('#closeResultBtn').click(function(){
        	edubxgl.close($("#repairResultSubmitBtn"));
        });
        
        
//======init方法后括号==============  
    };
  //======init方法后括号==============

    (function(){
        edubxgl.init();
    })();

    module.exports = edubxgl;
})
