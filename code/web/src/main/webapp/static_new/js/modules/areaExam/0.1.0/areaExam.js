define(['common', 'ajaxfileupload','treeview','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var rankList = {},
        Common = require('common');
        require('treeview');
        require('ajaxfileupload'); 
        require('pagination');
    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    } 
	var PUBLIC_PAGE_SIZE = 10;

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
             		var htmlStr='<option>' + course + '</option>';
					 $("#select").append(htmlStr);
            	 }
            	 queryList(1);
             }
         });
    }
	
	  var isInit = true;
	$('body').on('click','#select',function(){
    	queryList(1);
    });
	
	function queryList(pageNo){
		
		var trem=$(".cjd select").val();
		var pageNo=pageNo;
		var pageSize=PUBLIC_PAGE_SIZE;
    	
    		$.ajax({
        		type: "POST",
                url: "/regional/findBySchoolTrem.do",   
                data: {"schoolYear":trem,"pageNo" :pageNo,"pageSize":pageSize},
                dataType: "json",
                success: function(data){
                	
                	

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
//          	            	alert('当前第' + n + '页'); 
          	            	if(isInit){
          	            		queryList(n);	
          	            		isInit = false;
          	            	}else{ 
          	            		isInit = true; 
          	            		return;
          	            	}
          	            }
          	        });

                	
                	
                	var courseList = data.datas;
//                	console.log(courseList);
//                	var courseList =  data.datas;
                	$("#coursewareList").empty();
                	for(var i=0;i<courseList.length;i++){
                		var course = courseList[i];
                		var htmlStr = '  <tr flag="' + course.id + '" >'+
                		'                    <td>' + course.name + '</td>'+
                		'                    <td>' + course.gna + '</td>'+
                		'                    <td>' + course.ed + '</td>'+
                		'                    <td>'+
                		'                        <a href="/regional/inputGrade.do?id='+course.id+'" target="_blank">录入</a>'+
                		'                    </td>'+
                		'                    <td class="zka">'+
                		'                        <a href="/score1/rankList.do?exId='+course.id+'&skip=1&size=5" target="_blank">查看</a>'+
                		'                    </td>'+
                		'                </tr>';
                		$("#coursewareList").append(htmlStr);
                	
                }
                }
        	});
    		}
	select();

	queryList(1);
	
})


