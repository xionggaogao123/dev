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
define(['common','pagination'], function(require, exports, module) {
    /**
     *初始化参数
     */
    var dorm = {},
        Common = require('common');

    Array.prototype.remove = function(dx) {
        if (isNaN(dx) || dx > this.length) {
            return false;
        }
        this.splice(dx, 1);
    }



    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiangCheng.init()
     */
    dorm.init = function() {
//
//
//        Common.tab('click', $('.tab-head-new'))


        $('.look').on('click', function() {
            $('.susheList-table').hide();
            $('.look-table').show();
            return false;

        })
        $('.back').on('click', function() {
            $('.susheList-table').show();
            $('.look-table').hide();
            return false;

        })

        $('.addDorm').on('click', function() {
            Common.dialog($('#newDorm'));
            return false;
        })
        $('.closePop').on('click', function() {
            Common.dialog.close($('#newDorm'));
            return false;

        })
        $('.addStuDorm').on('click', function() {
            Common.dialog($('#newStuDormId'));
            return false;
        })
        $('.closePop').on('click', function() {
            Common.dialog.close($('#newStuDormId'));
            return false;

        })
        $('.addFloor').on('click', function() {
            Common.dialog($('#newLou'));
            return false;
        })
        $('.closePop').on('click', function() {
            Common.dialog.close($('#newLou'));
            return false;

        })
        $('.addCeng').on('click', function() {
            Common.dialog($('#newFloor'));
            return false;
        })
        $('.closePop').on('click', function() {
            Common.dialog.close($('#newFloor'));
            return false;

        })
        $('.addStu').on('click', function() {
            Common.dialog($('#addStuId'));
            return false;
        })
        $('.closePop').on('click', function() {
            Common.dialog.close($('#addStuId'));
            return false;

        })

       function show(obj, id,event) {
            var objDiv = $("#" + id + "");
            $(objDiv).css("display", "block");
            $(objDiv).css("left", event.clientX);
            $(objDiv).css("top", event.clientY+8);
        }

        function hide(obj, id,event) {
            var objDiv = $("#" + id + "");
            $(objDiv).css("display", "none");
        }
        
//        $('body').on('mouseover','.objects',function(event){
////        	var a = $(this).attr('complete');
//        	$('#obj2').html($(this).attr('complete'));
//            show(this,'obj2',event);
//        })
//        
//        $('body').on('mouseout','.objects',function(event){
//        	hide(this,'obj2',event);
//        })
//        
        /**
         * 更改年级后自动切换班级
         */
        $("#gradeName").on('change',function(){
        	var select_content=$('#gradeName').val();
        		var isAll = false;
            	if(select_content == "ALL"){
            		$("#class_name").html("<option value='ALL'>全部</option>");
            		isAll = true;
            	}
            	if(!(isAll)){
        		$.ajax({
        		url:"/dorm/findClassList.do",
        		type:"post",
        		dataType:"json",
        		data:{"gradeId":select_content},
        		success:function(classes){
        			$("#class_name").html("");
        			var classHtml = "<option value='ALL'>全部</option>";
        			for(var i=0;i<classes.length;i++){
        				var class_=classes[i];
        				var option="<option value='"+class_.id+"'>"+class_.name+"</option>";
        				classHtml += option;
        			}
        			$("#class_name").append(classHtml);
        		}	
        	});
            	}
        });
        
 
        
        //初始化页面参数
        var queryData={};
        var firstPageNo=1;
        var pageSize=10;
        var isFirstPage=true;
        /**
         * 点击查询显示结果
         */
        $("#findStudents").click(queryFirstPageView);
        //绑定查询按钮
        function queryFirstPageView(){
        	//alert(1);
        	loadQueryPage(1);
        }
        
        //点击入住学生列表加载全部学生
//        $(document).click("#findStudents"){
//            
//        }
//        $(document).ready(function(){ 
//        	　
//        }); 
        $(function(){
        	queryFirstPageView();
        });
       // queryFirstPageView();
        
        //拼接分页前准备
        function loadQueryPage(pageNo){
        	//alert(pageNo);
        	queryData.gradeId=$("#gradeName").val();
        	queryData.dlassId=$("#class_name").val();
        	queryData.sex_name=$("#sex_name").val();
        	queryData.studentName=$("#student_name").val().trim();
        	queryData.student_No=$("#student_No").val().trim();
        	queryData.pageNo=pageNo;
        	queryData.pageSize=pageSize;
        	queryPageView(null);
        }
        //拼接分页
        function queryPageView(pageNo){
        	//alert(pageNo);
        	var rObj={};
        	rObj.gradeId=queryData.gradeId;
        	rObj.dlassId=queryData.dlassId;
        	rObj.sex=queryData.sex_name;
        	rObj.studentName=queryData.studentName;
        	rObj.studentNum=queryData.student_No;
        	if(pageNo==null){
        		rObj.pageNo=firstPageNo;
        	}else{
        		rObj.pageNo=pageNo;
        	}
        	$.ajax({
        		url:"/dorm/findAllStudents.do",
        		type:"post",
        		data:rObj,
        		dataType:"json",
        		success:function(data){
        			//alert(data);
        			$("#pageForDorm").jqPaginator({
        				totalPages:Math.ceil(data.pageJson.total/pageSize)==0?1:Math.ceil(data.pageJson.total/pageSize),
        				visiblePages:10,
        				currentPage:parseInt(data.pageJson.pageNo),
        				first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
           	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
           	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
           	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
           	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
           	            onPageChange:function(n){
           	        	 //alert('当前第'+n+'页');
           	        	 if(isFirstPage){
           	        		queryPageView(n);
           	        		isFirstPage=false;
           	        	 }else{
           	        		isFirstPage=true;
           	        	 }
           	         }
        			});
        			fillQueryList(data.list);
        			
        			
        		}
        		
        	});
        }
        
        //学生列表表头
        function studentHead(){
        	var hHtml='<tr class="inHead">';
        		hHtml=hHtml+'<td>姓名</td>';
        		hHtml=hHtml+'<td>学号</td>';
        		hHtml=hHtml+'<td width="194">宿舍名</td>';
        		hHtml=hHtml+'<td>床位</td>';
        		hHtml=hHtml+'<td>性别</td>';
        		hHtml=hHtml+'<td>年级</td>';
        		hHtml=hHtml+'<td>班级</td>';
        		hHtml=hHtml+'<td >领取物品</td>';
        		hHtml=hHtml+'</tr>';
        	$("#studentList").html(hHtml);
        }
        
        //拼接表单内容
        function fillQueryList(DormAndStudents){
        	//alert(DormAndStudents);
//        	if(DormAndStudents.length<=0){
//        	&"ALL"==$("#gradeName").val()&"ALL"==$("#class_name").val()&
//        		"ALL"==$("#sex_name").val()&""==$("#student_name").val().trim()&""==$("#student_No").val().trim()){
//        		alert("未查到对应的学生!");
//        		return;
//        	}
        	document.getElementById("studentList").innerHTML = "";
        	studentHead();
        	for(var i=0;i<DormAndStudents.length;i++){
        		var subObj=DormAndStudents[i];
        		//alert(subObj.studentName);
        		if((subObj.receiveGoods).length<=4){
        			subObj.subGoods=subObj.receiveGoods;
        		}else{
        			subObj.subGoods=(subObj.receiveGoods).substring(0, 4);
        		}
				var tHtml= "<tr>";
				var tHtml=tHtml+"<td>"+subObj.studentName+"</td>";
				var tHtml=tHtml+"<td>"+subObj.studentNum+"</td>";
				var tHtml=tHtml+"<td>"+subObj.dormName+"</td>";
				var tHtml=tHtml+"<td>"+subObj.bed+"</td>";
				var tHtml=tHtml+"<td>"+subObj.sex+"</td>";
				var tHtml=tHtml+"<td>"+subObj.grade+"</td>";
				var tHtml=tHtml+"<td>"+subObj.dlass+"</td>";
				var tHtml=tHtml+"<td class='objects' title="+subObj.receiveGoods+">"+subObj.subGoods+"</td>";
				var tHtml=tHtml+"</tr>";
				//alert(tHtml);
				$("#studentList").append(tHtml);	
//				document.getElementById("studentList").innerHTML =  document.getElementById("studentList").innerHTML + tHtml;
        	}	
        }


//*****init方法末尾括号**********************************
    };
//****************************************

    (function() {
        dorm.init();
    })();
  //  module.exports = studentIn;
})
