//$(function(){ 
define(['common', 'ajaxfileupload','treeview','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var rankList = {},
        common = require('common');
        require('treeview');
        require('ajaxfileupload'); 
        require('pagination');
    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }




     var queryObjCatch={};
	var PUBLIC_PAGE_SIZE =10;
	 var Common={};
	function select(){
		 $.ajax({
             url: "/regional/Trem.do",
             type: "post",
             datatype: "json",
             data: {},
             success: function (data) {
            	 var courseList =  eval("(" + data + ")").datas;
            	 $("#select").empty(); 
            	 for(var i=0;i<courseList.length;i++){
             		var course = courseList[i];
             		var htmlStr='<option>' + course+ '</option>';
					 $("#select").append(htmlStr);
            	 }

            	 queryList(1);
            	 
            	 
             }
             
             
             
             
         });
    }
	
	  var isInit = true;
//	$('body').on('change','#select',function(){
//    	queryList(1);
//    });
	  
	   
	
	$("#select").change(function(){
		queryList(1);
	})
	function queryList(pageNo){
		
		var xueqi=$(".cjd select").val();
		queryObjCatch.page=pageNo;
		var pageNo=pageNo;
		var pageSize=PUBLIC_PAGE_SIZE;
    	
    		$.ajax({
        		type: "POST",
                url: "/regional/findByEducationTrem.do",   
                data: {"term":xueqi,"pageNo" :pageNo,"pageSize":pageSize},
                dataType: "json",
                success: function(data){
                	queryObjCatch.total=data.pagejson.total;
                	queryObjCatch.tt=Math.ceil(data.pagejson.total/PUBLIC_PAGE_SIZE) == 0?1:Math.ceil(data.pagejson.total/PUBLIC_PAGE_SIZE);
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
          	            	if(isInit){
          	            		isInit = false;
          	            		queryList(n);
          	            	}else{ 
          	            		isInit = true; 
          	            		return;
          	            	}
          	            }
          	        });
                	
                	
                	var courseList = data.datas;
                	$("#coursewareList").empty();
                	for(var i=0;i<courseList.length;i++){
                		var course = courseList[i];
                		var htmlStr = '  <tr flag="' + course.id + '" >'+
                		'                    <td>' + course.nm + '</td>'+
                		'                    <td>' + course.gnm + '</td>'+
                		'                    <td>' + course.date + '</td>'+
                		'                    <td>'+
                		'                        <a href="#"  flag="' + course.id + '" class="look" >查看</a>'+
                		'                    </td>'+
                		'                    <td>'+
                		'                        <a href="#" class="count" flag="'+course.id+'">统计</a>'+
                		'                    </td>'+
                		'                    <td>'+
                		'                        <a href="#" class="delete" flag="'+course.id+'">删除</a>'+
                		'                    </td>'+
                		'                </tr>';
                		$("#coursewareList").append(htmlStr);
                	
                }
                }
        	});
    		}
	
	var targetIdNow;
	
	$('.gray-table').on('click','.delete',function(){
		targetIdNow = $(this).closest("tr").attr("flag");
		
		$.ajax({
    		type: "POST",
            url: "/regional/findByJointId.do",  
            data: {"id":targetIdNow},
            dataType: "json",
            success: function(data){
            	var area=data.datas[0];
            	queryObjCatch.fg=area.sch[0].baseEntry.fg;
            	
            	if(queryObjCatch.fg==1){
    	        	Common.dialog($('#delConExamm'));
    	        	 
    	        }else if(queryObjCatch.fg==0){
    	        	Common.dialog($('#delConExam'));
    	        	
    	        }
            }
    	});
		
	        
       
       return false;
    });
	
	$('#delConExamm').on('click','.deleteArea',function(){
 		 del(targetIdNow);
         return false;
     });
	
	
	 $('#delConExam').on('click','.deleteArea',function(){
 		 del(targetIdNow);
         return false;
     });
	
	
	$('.gray-table').on('click','.look',function(){
		var targetId = $(this).closest("tr").attr("flag");
        look(targetId);
        
        return false;
    });
	$('.gray-table').on('click','.count',function(){
		var targetId = $(this).closest("tr").attr("flag");
		sessionStorage.setItem("jointExamId", targetId);
		window.open("/score/jointExam.do");

		return false;
	});
	$('.pop-title').on('click','.closePop',function(){
		
		Common.dialog.close($(this));
        
        return false;
    });
	
	
	
	
	
	
	 Common.dialog = function(obj,callback){
	        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
	        $('.pop-wrap').hide();
	        obj.show();
	        if(callback){
	            callback.apply(this,arguments[0]);
	            obj.find('.pop-btn span:not(".active")').click(function(){
	                Common.dialog.close($(this));
	            });
	        }else{
	            obj.find('.pop-btn span').click(function(){
	                Common.dialog.close($(this));
	            });
	        }
	        $(document).scrollTop(0);
	        console.log($('document').scrollTop(0));
	    };

	    Common.dialog.close = function(obj){
	        $(obj).closest('.pop-wrap').hide();
	        $('.bg-dialog').hide();
	    };
	    
	    
	    
        function look(targetId){
        	
        	$.ajax({
        		type: "POST",
                url: "/regional/findByJointId.do",  
                data: {"id":targetId},
                dataType: "json",
                success: function(data){
                	var area=data.datas[0];
                	var tch=area.sch;
                	 $(".statusTitle").empty();
                	var div='<div>' + area.nm + '</div>';
                	$(".statusTitle").append(div)
                	
                	 $("#courseware").empty(); 
                	//if(area.sch[0].baseEntry.fg==0)
                		for(var i=0;i<tch.length;i++){
                     		var course = tch[i];
                    		var htmlStr = '  <tr flag="' + course.id + '" >'+
                    		'                    <td width=250>' + course.name + '</td>'+
                    		'                    <td width=150>';
                    		if(area.sch[i].baseEntry.fg==0){
                    			htmlStr=htmlStr+'    <span>未提交</span>';
                    		}else{
                    			htmlStr=htmlStr+'    <span>已提交</span>';
                    		}
                    		htmlStr=htmlStr+'      </td>'+
                    		                 ' </tr>';
                    		$("#courseware").append(htmlStr);
                    	 }
                		
                		 Common.dialog($('#status'));
                }
        	});
        }
        	
        	function del(targetId){
        		 $.ajax({
                     url: "/regional/removeAll.do",
                     type: "post",
                     datatype: "json",
                     data: {"JointId":targetId },
                     success: function (data) {
                    	 if(queryObjCatch.page==queryObjCatch.tt){
                    		 if(queryObjCatch.total%PUBLIC_PAGE_SIZE==1 && queryObjCatch.total!=1){
                        		 alert("删除成功");
                        		 queryList(queryObjCatch.page-1);
                        		
                        	 }else{
                        		 alert("删除成功");
                   	       		queryList(queryObjCatch.page);
                   	       		
                        	 }
                    	 }else{
                    		 alert("删除成功");
                	       	queryList(queryObjCatch.page);
                    		 
                    	 }
                    	 
                    	 
                     }
                  
                 });
        	}
	    
	
	select();
	
	})
