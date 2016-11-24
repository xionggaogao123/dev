/**
 * Created by Zoukai on 15/11/03.
 */
/**
 * @author 邹凯
 * @module  成绩排名查看
 */
/* global Config */
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
    /**
     * 公共变量
     */
    var PUBLIC_PAGE_SIZE = 20;
	var pageNoNow = 1;
	var isInit=true;
	var exId=$('#s-ranking').attr('flag');
	var total=$('.new-page-links').attr('flag');
	
	
		/**
		 * 根据考试编码查询
		 */
	function byId(page){
		var skip=(page-1)*PUBLIC_PAGE_SIZE;
	 $.ajax({
		 type: "POST",
		   url:"/score1/findByExid.do",
		   data: {"exid":exId,"skip":skip,"size":PUBLIC_PAGE_SIZE},
         dataType: "json",
         success: function(data){
        	 
        	 $('#ranking').html('');
      	 var rankList = data.datas;
  	   
  	   
  	   $('.new-page-links').jqPaginator({
	            totalPages:Math.ceil(total/PUBLIC_PAGE_SIZE) == 0?1:Math.ceil(total/PUBLIC_PAGE_SIZE),//总页数 
	            visiblePages: 10,//分多少页
	            currentPage: page,//当前页数
	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
	            onPageChange: function (n) { //回调函数
	            	if(isInit){
	            		byId(n);
	            		isInit = false;
	            		
	            	}else{ 
	            		isInit = true; 
	            		return;
	            	}
	            }
	        });
  	  
  	   for(var i=0;i<rankList.length;i++){
  		   var rank = rankList[i];
  		   
  		   var kemu = rank.sList[i];
  		
      	   var htmls=	'	<tr>'+
     		'	<td>'+rank.sr+'</td>	'		+
     		'	<td>'+rank.ar+'</td>	'		+
     		'	<td>'+rank.stuNm+'</td>	'		+
     		'	<td>'+rank.cna+'</td>	'		+
     		'	<td>'+rank.suc+'</td>	'		;
     		for(var j=0;j<rank.sList.length;j++){
     			htmls = htmls + '	<td>'+rank.sList[j]+'</td>	';
     		}
     		htmls = htmls + '	</tr>';
      	    $('#ranking').append(htmls);
  	   }
         },
		 error:function(){
			 	alert("查看失败");
			 }
	 })
}
		/**
		 * 下拉框排序查询	
		 */
	function xiala(page){
		   
			   var key=$('#selec option:selected').attr('flag');
			 
			   var skip=(page-1)*PUBLIC_PAGE_SIZE;
			    
			   if(key == "班级名称排序"){
				   byId(page);
				   return false;
			   }
			   
			   
			   $.ajax({
				   type: "POST",
				   url:"/score1/findByRanking.do",
				   data: {"exid":exId,"skip":skip,"size":PUBLIC_PAGE_SIZE},
		           dataType: "json",
		           success: function(data){
		        	   $('#ranking').html('');
		        	   var rankList = data.datas;
		        	   
		        	   
		        	   $('.new-page-links').jqPaginator({
		      	            totalPages:Math.ceil(total/PUBLIC_PAGE_SIZE) == 0?1:Math.ceil(total/PUBLIC_PAGE_SIZE),//总页数 
		      	            visiblePages: 10,//分多少页
		      	            currentPage: page,//当前页数
		      	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
		      	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
		      	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
		      	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
		      	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
		      	            onPageChange: function (n) { //回调函数
		      	            	if(isInit){
		      	            		xiala(n);
		      	            		isInit = false;
		      	            		
		      	            	}else{ 
		      	            		isInit = true; 
		      	            		return;
		      	            	}
		      	            }
		      	        });
		        	  
		        	   for(var i=0;i<rankList.length;i++){
		          		   var rank = rankList[i];
		          		   
		          		   var kemu = rank.sList[i];
		          		   console.log(rank);
		              	   var htmls=	'	<tr>'+
		             		'	<td>'+rank.sr+'</td>	'		+
		             		'	<td>'+rank.ar+'</td>	'		+
		             		'	<td>'+rank.stuNm+'</td>	'		+
		             		'	<td>'+rank.cna+'</td>	'		+
		             		'	<td>'+rank.suc+'</td>	'		;
		             		for(var j=0;j<rank.sList.length;j++){
		             			htmls = htmls + '	<td>'+rank.sList[j]+'</td>	';
		             		}
		             		htmls = htmls + '	</tr>';
				            $('#ranking').append(htmls);

		          	   }
		           },
					 error:function(){
						 	alert("查看失败");
						 }
			   })
}	   
		/**
		 * 导出
		 */
		   $('#output').click(function(){
			   $("#eId").val($("#s-ranking").attr("flag"));
           	   $("#exportExamItemScoreModelForm").submit();

		   })
		   
		   
	byId(1);
    $('#selec').change(function(){ 
    	xiala(1);
    })
})