/**
 * Created by caotiecheng on 15/12/21.
 */
/**
 * @author 曹铁成
 * @module  我的预约操作
 * @description
 */
/* global Config */
define(['common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var myAppointment = {},
        Common = require('common');
    			require('pagination');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }

    myAppointment.dialog = function(obj,callback){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
        $('body').scrollTop(0);
    };

    myAppointment.close = function(obj){
        obj.closest('.pop-wrap').hide();
        $('.bg-dialog').hide();
    };

    /**
     * @func init
     * @desc 页面初始化
     * @example;
     * jiangCheng.init()
     */
    myAppointment.init = function(){
//
//
//        Common.tab('click',$('.tab-head-new'));
        
        $('.count').on('click', function(event) {
			Common.dialog($('#count'));
			return false;
		});
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#count'));
		});
		
		//弹出编辑弹框
		$('.editReserv').on('click', function(event) {
			Common.dialog($('#editReserv'));
			return false;
		});
		//关闭编辑弹框
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#editMyAppointment'));
		});
  
        //********我的预约查询页面***********************************************************************************************
		var del={};
		//我的预约页面查询缓存
        var queryManageCatch;
		var pageSize = 10;
		var classTotalPage;
		//分页组件中默认为第一页
		var isFirstPageForQueryFun = true;
	    //全查询编辑页面弹框
        $("#myReservation").on('click','.editReserver',function(){
        	var rDetail;
        	var eid = $(this).parent().parent().attr("flag");
			for(var i=0;i<queryManageCatch.myList.length;i++){
				
				if(queryManageCatch.myList[i].id == eid){
					rDetail = queryManageCatch.myList[i];
				}
			}
			var startTime = rDetail.startTime.replace(new RegExp('/','g'),'-');
			var endTime = rDetail.endTime.replace(new RegExp('/','g'),'-');
			var reasons = rDetail.reasons;
			var userName = rDetail.user;
            Common.dialog($('#editMyAppointment')); 
            $(editSubmitfunctionBtn).attr("flag",eid);
            $("#startTimeId").val(startTime);
            $("#endTimeId").val(endTime);
            $("#useInfoId").val(reasons);
            $("#userNameId").val(userName);
            return false;
        });
        
      
        
      //我的预约页面编辑对话框确认提交按钮事件绑定
        /*$("#editSubmitfunctionBtn").click(function(){*/
        $("#editMyAppointment").on('click','#editSubmitfunctionBtn',function(){
        	var updateFunctionObj = {};
        	updateFunctionObj.id = $(this).attr("flag");
        	updateFunctionObj.startTime = $("#startTimeId").val()==null?"":$('#startTimeId').val().trim();
        	updateFunctionObj.endTime = $("#endTimeId").val()==null?"":$('#endTimeId').val().trim();
        	updateFunctionObj.reasons = $("#useInfoId").val()==null?"":$('#useInfoId').val().trim();
        	updateFunctionObj.user = "";
        	if($.trim(updateFunctionObj.startTime) == ""){
        		alert("起始时间不能为空！");
        		return false;
        	}else if($.trim(updateFunctionObj.endTime) == ""){
        		alert("结束时间不能为空！");
        		return false;
        	}else if(updateFunctionObj.startTime>=updateFunctionObj.endTime){
        		alert("您选择的结束时间不应小于或等于起始实时间!");
				return false;
        	}else if($.trim(updateFunctionObj.reasons) == ""){
        		alert("预约说明不能为空！");
        		return false;
        	}
        	$.ajax({
        		type: "POST",
                url: "/functionclassroom/updateAppoint.do",
                data: updateFunctionObj,
                dataType: "json",
                success: function(data){
                	if(data.flag==0){
						alert("更新失敗!");
					}else if(data.flag==1){
                	alert("编辑保存成功！");
                	myAppointment.close($("#editSubmitfunctionBtn"));
                	queryFunctionClassRoomList(del.ff);
					}else if(data.flag==2){
						alert("该功能教室此时间段内已有人预约，请重新选择日期");
						return false;
					}
                }
        	}); 
        	
        });
      //我的预约页面编辑对话框关闭按钮绑定事件
        $('#editCloseBtn').click(function(){
        	myAppointment.close($("#editSubmitfunctionBtn"));
        });
      //我的预约页面编辑对话框确定关闭按钮绑定事件
//        $('#editSubmitfunctionBtn').click(function(){
//        	myAppointment.close($("#editSubmitfunctionBtn"));
//        });
      //给我的预约列表"到该页"按钮添加事件
		$('#currentPageForAppoint').on('click',function(){
			var goPage = $(this).prev().val();
			if((!isNaN(goPage))&&(goPage<=del.totals)){
				queryFunctionClassRoomList(parseInt(goPage));
			}
		});
      //点击我的预约加载页面数据
        $("#myReservation123").click(function(){
        	//我的预约查询方法
        	queryFunctionClassRoomList(1);
 	    });
        
        
      //我的预约全查询页面删除按钮绑定事件
		$("#myReservation").on('click','.appointDelete',function(){
        	if(confirm('确定删除?')){
        		var delId = $(this).parent().parent().attr("flag")
                var delObj = {};
                delObj.id = delId;
                $.ajax({
            		type: "post",
                    url: "/functionclassroom/deleteAppoint.do",
                    data: delObj,
                    dataType: "json",
                    success: function(data){
						if (data.flag) {
							alert("删除成功！");
							if(del.total==1){
								queryFunctionClassRoomList(del.ff);

							}else if(del.total % pageSize == 1){
								queryFunctionClassRoomList(del.ff-1);
							}else{
								queryFunctionClassRoomList(del.ff);
							}
						} else {
							alert("删除出错，请联系管理员！");
						}

	                    }, 
                    error:function(data){
                    	alert("删除出错，请联系管理员！");
                    }
            	}); 
        	}
                return false;
        });
        
		
		
		//点击检索按钮根据检索时间查询我的预约列表
        $("#searchBySearchTime").click(function(){
        	queryFunctionClassRoomList(1);
        	});
        
		
        
      //全查询参数组装并查询
        function queryFunctionClassRoomList(pageNo){
        	var page = {};
        	page.size = pageSize;
        	page.skip=(pageNo-1)*pageSize;
        	page.searchTime=$("#searchTime").val();
        	$.ajax({
        		type: "POST",
                url: "/functionclassroom/queryMyAppointments.do",
                data: page,
                dataType: "json", 
                success: function(data){
                	document.getElementById("functionClassRoomList").innerHTML = "";
                	queryManageCatch = data;
                	del.total = data.total;
                	del.totals = data.total/pageSize;
                	//jqPaginator为分页组件
                	$('#pageForFun').jqPaginator({
          	            totalPages: Math.ceil(data.total/pageSize) == 0?1:Math.ceil(data.total/pageSize),//总页数  
          	            visiblePages: 10,//分多少页
          	            currentPage: pageNo,//当前页数
          	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
          	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
          	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
          	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
          	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
          	            onPageChange: function (n) { //回调函数
          	            	del.ff=n;
          	            	//点击页码的查询
          	            	if(isFirstPageForQueryFun){
          	            		queryFunctionClassRoomList(n);
          	            		isFirstPageForQueryFun = false;
          	            	}else{
          	            		isFirstPageForQueryFun = true; 
          	            	}
          	            }
          	        });
                	fillQueryClassRoomList(data.myList);
                }
        	}); 
        }
      //为我的预约全查询页面表单组装内容
        function fillQueryClassRoomList(funData){
        	document.getElementById("functionClassRoomList").innerHTML = "";
        	if(funData.length==0){
    			var trHtml = "<tr><td colspan='5' style='border:0'>暂无数据</td></tr>";
    			document.getElementById("functionClassRoomList").innerHTML = document.getElementById("functionClassRoomList").innerHTML +  trHtml;
    		}else{
        	for(var i=0;i<funData.length;i++){
        		var subObj = funData[i];
        		var td=subObj.reasons.trim();
        		if(td.length>10){
        			var tt=td.substring(0,10) + "..."
        		}else{
        			var tt=td
        		}

        		var trHtml = "<tr flag='" + subObj.id +"'><td>";//id
        		trHtml = trHtml + subObj.classRoomName + "</td><td>";//功能教室名字
        		trHtml = trHtml + subObj.startTime + "</td><td>";//起始时间
        		trHtml = trHtml + subObj.endTime + "</td><td style='cursor:pointer' title='"+td+"'>";//结束时间
        		trHtml = trHtml + tt + "</td><td>";//预约详情
        		trHtml = trHtml + "<a href='#' class='abtn editReserver'>编辑</a>"+"<a href='#' class='appointDelete del'>删除</a>"
        		trHtml = trHtml + "</td></tr>";
        		document.getElementById("functionClassRoomList").innerHTML = document.getElementById("functionClassRoomList").innerHTML +  trHtml;
        	}
    	  }
        }
        
        //我的预约查询方法
	    queryFunctionClassRoomList(1);
//======init方法后括号==============  
    };
  //======init方法后括号==============

    (function(){
    	myAppointment.init();
    	
    })();
    
    module.exports = myAppointment;
})
