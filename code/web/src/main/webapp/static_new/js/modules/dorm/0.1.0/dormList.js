/**
 * 宿舍列表
 * Created by huanxiaolei@ycode.cn on 15/12/9.
 */
    define(['common','pagination'], function(require, exports, module) {
        /**
         *初始化参数
         */
        var dorm = {},
            Common = require('common');
                     require('pagination');
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
        dormList.init = function() {
//
//
//            Common.tab('click', $('.tab-head-new'))


            $('.addDorm').on('click', function() {
                Common.dialog($('#newDorm'));
                return false;
            });
            $('.closePop').on('click', function() {
                Common.dialog.close($('#newDorm'));
                newDormReset();//关闭时清除填写内容
                return false;

            });
            
            $('.closePop').on('click', function() {
                Common.dialog.close($('#addStuId'));
                newDormStuClose();//关闭时清除填写内容
                return false;

            });

        	/**
        	 * 公共变量
        	 */
            //当前宿舍ID和NAME
            var PUBLIC_DORM_ID;
            var PUBLIC_DORM_NAME;
            var PUBLIC_DORM_SEX;
            var PUBLIC_TRFA = false;
            var PUBLIC_N;
            
            var allInputHtml = "<input type='text' readonly='readonly'>";
        	var allOptionHtml = "<option value='ALL'>全部</option>";
            var allNewDormOptionHtml = "<option value='ALL'>请选择</option>";
            
            $("#susheListview").click(function(){
            	//填充之前先清空
            	clearAll();
            	//初始化最顶级下拉菜单内容
                $.ajax({
            		type: "POST",
                    url: "/dorm/findDormAreaFullList.do", 
                    data: {},
                    dataType: "json",
                    success: function(data){
                    	fillSelection("dormArea",allOptionHtml,data.datas);
                    	fillSelection("newDormArea",allNewDormOptionHtml,data.datas);
                    }
            	});
                
              //初始化页面时查询所有信息并填充列表
                queryAndFillList("","");
                
            });
            //
            function clearAll(){
            	$("#dormArea").val("ALL");
            	$("#dormBuilding").html(allOptionHtml);
            	$("#dormFloor").html(allOptionHtml);
            }
            //填充下拉菜单的方法
            function fillSelection(selectionId,OptionHtml,srcDatas){
            	var targetSelection = $("#" + selectionId);
            	targetSelection.html(OptionHtml);
            	for(var i=0;i<srcDatas.length;i++){
            		var subData = srcDatas[i];
            		targetSelection.append("<option value='" + subData.id + "'>" + subData.name + "</option>");
            	}
            }
            //下拉菜单查询change事件(前二个)
            $("#dormArea").change(function(){
            	queryAndFillChildBuilingSelection(this,"dormBuilding","dormFloor",$(this).val(),allOptionHtml);
            	queryAndFillList($(this).val(),"daid",this);
            });
            $("#dormBuilding").change(function(){
            	queryAndFillChildFloorSelection(this,"dormFloor",$(this).val(),allOptionHtml);
            	queryAndFillList($(this).val(),"dbid",this);
            });
            $("#dormFloor").change(function(){
            	queryAndFillList($(this).val(),"dfid",this);
            });
            
            //新建宿舍下拉菜单change事件(前二个)
            $("#newDormArea").change(function(){
            	queryAndFillChildBuilingSelection(this,"newDormBuilding","newDormFloor",$(this).val(),allNewDormOptionHtml);
            });
            $("#newDormBuilding").change(function(){
            	queryAndFillChildFloorSelection(this,"newDormFloor",$(this).val(),allNewDormOptionHtml);
            });
            //下拉菜单change事件使用的方法
            //查询宿舍楼和填充    
            function queryAndFillChildBuilingSelection(changedSelection,sectionBuild,sectionFloor,parentId,OptionHtml){
            	var isAll = false;
            	if($(changedSelection).val() == "ALL"){
            		$("#"+sectionBuild+" option[value!='ALL']").remove();
            		$("#"+sectionFloor+" option[value!='ALL']").remove();
            		isAll = true;
            	}
            	$("#"+sectionFloor+" option[value!='ALL']").remove();
            	if( !(isAll)){
            		$.ajax({
                		type: "POST",
                        url: "/dorm/findDormBuildList.do", 
                        data: {dormAreaId : parentId},
                        dataType: "json",
                        success: function(data){
                        	fillSelection(sectionBuild,OptionHtml,data.datas);
                        }
                	});
            	}
            };
            //查询楼层和填充
            function queryAndFillChildFloorSelection(changedSelection,sectionFloor,parentId,OptionHtml){
            	var isAll = false;
            	if($(changedSelection).val() == "ALL"){
            		$("#"+sectionFloor+" option[value!='ALL']").remove();
            		isAll = true;
            	}
            	if(!(isAll)){
            		$.ajax({
                		type: "POST",
                        url: "/dorm/findDormFloorList.do", 
                        data: {dormBuildId : parentId},
                        dataType: "json",
                        success: function(data){
                        	fillSelection(sectionFloor,OptionHtml,data.datas);
                        }
                	});
            	}
            };
            
            //公共变量缓存一个之前查询的查询条件
            var lastQueryObj ;
            var isInit = true;
            var pageSize = 10;
            //根据给定的ID和字段名查询并填充列表
            function queryAndFillList(queryId,columName,targetSelection,pageNo){
            	if(queryId == "ALL" ){
            		var preDom = $(targetSelection).prev().prev();
            		if(preDom.length == 0){
            			queryId = "";
            			columName = "";
            		}else{
            			queryId = preDom.val();
            			if(columName == "dbid"){
            				columName = "daid";
            			}
            			if(columName == "dfid"){
            				columName = "dbid";
            			}
            		}
            	}
            	var queryObj = {};
            	queryObj.queryId = queryId==null?"":queryId;
            	queryObj.columName = columName==null?"":columName;
            	queryObj.pageNo = pageNo==null?1:pageNo;
            	queryObj.pageSize = pageSize;
            	
            	if(lastQueryObj == null){
            		lastQueryObj = {};
            	}
            	
            	lastQueryObj.queryId = queryObj.queryId;
            	lastQueryObj.columName = queryObj.columName;
            	lastQueryObj.targetSelection = targetSelection;
            	lastQueryObj.pageNo = queryObj.pageNo;
            	
            	$.ajax({
            		type: "POST",
                    url: "/dorm/dormList.do", 
                    data: queryObj,
                    dataType: "json",
                    success: function(data){
                    	var datas = data.datas;
                    	var dormList = $("#dormList");
                    	$("#dormList tr:gt(0)").remove();
                    	$('#new-page-links').html("");
                    	if(datas.length==0){
                    		dormList.append("<tr><td colspan='7' style='border:0'>暂无数据</td></tr>");
                    		return;
                    	}
                    	//渲染数据
                    	for(var i=0;i<datas.length;i++){
                          	var subData = datas[i];
                            var subDa = subData.gAndc;
                          	var subSex = subDa.sex==null?"":subDa.sex;
                          	var subGC = subDa.gradeAndClass==null?"":subDa.gradeAndClass;
                          	var subEqu = subData.equipment;
                          	if(subGC.length > 9){
                          		var subStrGC = subGC.substring(0,9)+"..";
                          	}else{
                          		var subStrGC = subGC;
                          	}
                          	if(subEqu.length > 7){
                          		var subStrEqu = subEqu.substring(0,7)+"..";
                          	}else{
                          		var subStrEqu = subEqu;
                          	}
                          	var htmlStr = "<tr> ";
                          	htmlStr = htmlStr + "<td>" + subData.dormName + "</td>";
                          	htmlStr = htmlStr + "<td>" + subSex + "</td>";
                          	htmlStr = htmlStr + "<td class='objects grade' title='"+ subGC +"'>" + subStrGC + "</td>";                                            
                          	htmlStr = htmlStr + "<td>" + subData.dormPhone + "</td>";
                          	htmlStr = htmlStr + "<td>" + subData.bedNumber + "</td>";
                          	htmlStr = htmlStr + "<td class='equipment' title='"+ subEqu +"'>" + subStrEqu + "</td>";
                          	htmlStr = htmlStr + "<td class='solid'><a class='delete look' dormId='"+subData.id+"' flag='"+subData.dormName+"'>查看</a>";
                          	if(isAdmin){
                          		htmlStr = htmlStr + "<a class='delete deldorm'>删除</a>";
                          	}
                          	htmlStr = htmlStr + "</td>";
                          	htmlStr = htmlStr + "</tr>";
                          	dormList.append(htmlStr); 
                         }
                    	//分页方法
                      	 $('#new-page-links').jqPaginator({
                      	    totalPages: Math.ceil(data.pagejson.total/pageSize) == 0?1:Math.ceil(data.pagejson.total/pageSize),//总页数 
                      	    visiblePages: 10,//分多少页
                      	    currentPage: parseInt(data.pagejson.pageNo),//当前页数
                      	    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                      	    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                      	    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                      	    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                      	    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                      	    onPageChange: function (n) { //回调函数
                      	       if(isInit){
                      	    	    PUBLIC_N=n;
                      	            queryAndFillList(lastQueryObj.queryId,lastQueryObj.columName,lastQueryObj.targetSelection,n);
                      	          	isInit = false;
                      	          }else{
                      	          	isInit = true; 
                      	          }
                      	       }
                      	  });
                     }
            	});
            };
         
            //新建宿舍
            $("#newDormDone").on("click",function(){
            	var dataObj = {};
            	dataObj.dormAreaId = $("#newDormArea").val();
            	dataObj.dormBuildingId = $("#newDormBuilding").val();
            	dataObj.dormFloorId = $("#newDormFloor").val();
            	dataObj.dormName = $("#dormName").val().trim();
            	dataObj.bedNumber = $("#bedNum").val().trim();
            	dataObj.dormPhone = $("#dormPhone").val().trim();
            	dataObj.equipment = $("#receiveGoods").val().trim();
            	if(dataObj.dormAreaId=="ALL"){
            		alert("请选择宿舍区号");
            		return;
            	}
            	if(dataObj.dormBuildingId=="ALL"){
            		alert("请选择宿舍楼号");
            		return;
            	}
            	if(dataObj.dormFloorId=="ALL"){
            		alert("请选择宿舍楼层");
            		return;
            	}
            	if(dataObj.dormName==""){
            		alert("请填写宿舍名称");
            		return;
            	}
            	if(isNaN(dataObj.bedNumber)||dataObj.bedNumber==""){
            		alert("床位不能为空或非数字");
            		return;
            	}
            	else{
            	    if(dataObj.bedNumber>20){
            	        alert("您输入的床位数量过大!");
            	        return;
            	    }
            	    if(dataObj.bedNumber==0){
            	        alert("床位数量不能为零!");
            	        return;
            	    }
            	}
            	if(dataObj.dormPhone==""){
            		alert("请填写宿舍电话");
            		return;
            	}
            	if(dataObj.dormPhone.length>13){
            		alert("您输入的电话号码长度不符！");
            		return;
            	}
            	if(dataObj.equipment==""){
            		alert("请填写设备配置");
            		return;
            	}
            	
            	$.ajax({
            		type: "POST",
                    url: "/dorm/addDorm.do", 
                    data: dataObj,
                    dataType: "json",
                    success: function(data){
                    	if(data.code==200){
                    	   alert("新建宿舍成功！");
                    	   Common.dialog.close($('#newDorm'));
                    	   $('.susheList-table').show();
                    	   clearAll();
                    	   queryAndFillList("","");
                    	   newDormReset();
                    	}else{
                    	    alert(data.message);
                    	}
                    },
                    error:function(){
                       alert("新建宿舍失败！");
                    }
            	});
            });
            //新建宿舍重置按钮
            $("#newDormReset").on("click",function(){
            	newDormReset();
            });
            function newDormReset(){
            	$("#newDormArea").val("ALL");
            	$("#newDormBuilding").html(allNewDormOptionHtml);
        		$("#newDormFloor").html(allNewDormOptionHtml);
            	$("#dormName").val("");
            	$("#bedNum").val("");
            	$("#dormPhone").val("");
            	$("#receiveGoods").val("");
            };
            //宿舍查看按钮
            $('#dormList').on('click','.look', function() {
            	showDormStu();
                var dormId = $(this).attr("dormId");
                PUBLIC_DORM_ID = $(this).attr("dormId");
                PUBLIC_DORM_NAME = $(this).attr("flag");
                loadDormStu(dormId);
            });
            //删除宿舍
            $("#dormList").on("click",".deldorm",function(){
            	if(confirm("确定删除此宿舍吗？")){
	            	var dormId = $(this).siblings().attr("dormId");
	       		    $.ajax({
	                 	type: "POST",
	                 	url: "/dorm/dormStudentDetail.do",
	                 	data: {dormId:dormId},
	                 	dataType: 'json',
	                 	success:function(data){
	                 		var lengths = data.length;
	                 		if(lengths==0){
	                 			delDorm(dormId);
	                 		}else{
	                 			if(confirm("确定删除此宿舍吗？此操作会删除该宿舍的所有学生！")){
	                 				delDorm(dormId);
	                 			}
	                 		}
	                 	}
	       		    });
            	}
            });
          //删除宿舍方法
            function delDorm(dormId){
            	$.ajax({
	            	  type:"post",
	            	  url:"/dorm/delDorm.do",
	            	  data:{dormId:dormId},
	            	  dataType:"json",
	                  success:function(data){
	                	if(data.code==200){
	                		alert("删除成功！");
	                		var index = $("#dormList").find("tr:last").index();
				    		if(index-1==0&&PUBLIC_N>1){
				    			queryAndFillList(lastQueryObj.queryId,lastQueryObj.columName,lastQueryObj.targetSelection,PUBLIC_N-1);
				    		}else{
				    			queryAndFillList(lastQueryObj.queryId,lastQueryObj.columName,lastQueryObj.targetSelection,PUBLIC_N);
				    		}
	                	}else{
	                		alert("删除失败！");
	                	}
	            	  },
	            	  error:function(){
	            		  alert("删除失败,联系管理员！");
	            	  }
	                });
            };
            //返回宿舍列表按钮
            $('.back').on('click', function() {
                $('.susheList-table').show();
                $('.look-table').hide();
                //返回宿舍列表清空之前的数据
                $("#dormDetial tr:gt(0)").remove();
                //刷新宿舍列表
                queryAndFillList(lastQueryObj.queryId,lastQueryObj.columName,"",PUBLIC_N);
            });
            //宿舍查看学生页面展示
            function showDormStu(){
            	$('.susheList-table').hide();
                $('.look-table').show();
            };
            //查看宿舍学生加载方法
            function loadDormStu(dormId){
            	 showDormStu();//再次加载显示页面
            	 $.ajax({
                 	type: "POST",
                 	url: "/dorm/dormStudentDetail.do",
                 	data: {dormId:dormId},
                 	dataType: 'json',
                 	success:function(data){
                 		var dormDetial = $("#dormDetial");
                 		$("#dormDetial tr:gt(0)").remove();//先清空之前的
                 		if(data.length==0){
                 			if(isAdmin){
                 				dormDetial.append("<tr><td colspan='8' style='border:0'>暂无数据</td></tr>");
                 				return;
                 			}
                 			dormDetial.append("<tr><td colspan='7' style='border:0'>暂无数据</td></tr>");
                 			return;
                 		}
                 		for(var i=0;i<data.length;i++){
                 			var receiveGood = data[i].receiveGoods;
                 			if(receiveGood.length > 6){
                 				var subGood = receiveGood.substring(0,6);
                 			}else{
                 				var subGood = receiveGood;
                 			}
                 			var htmlStr =       " <tr>";
                     		htmlStr = htmlStr + " <td>" + data[i].bed + "号床位</td>";
                     		htmlStr = htmlStr + " <td>" + data[i].studentNumber + "</td>";
                     		htmlStr = htmlStr + " <td>" + data[i].studentName + "</td>";
                     		htmlStr = htmlStr + " <td>" + data[i].sex + "</td>";
                     		htmlStr = htmlStr + " <td>" + data[i].grade + "</td>";
                     		htmlStr = htmlStr + " <td>" + data[i].clas + "</td>";
                     		htmlStr = htmlStr + " <td class='objects grade solid' title='"+receiveGood+"'>" + subGood + "</td>";
                     		if(isAdmin){
                     			htmlStr = htmlStr + " <td class='solid'><a class='delete look delStu' flag='"+data[i].id+"'>删除</a></td>";
                     		}
                     		htmlStr = htmlStr + " </tr>";
                     		dormDetial.append(htmlStr);
                 		}
                 	},
                 	error:function(){
                 		alert('数据加载失败！');
                 	}
                 });
            };
            
            //宿舍添加学生按钮
            $('.addStu').on('click', function() {
                Common.dialog($('#addStuId'));
                //加载宿舍床位
                loadBedNum();
            });
            //添加学生确定按钮
            $('#addStuDone').on('click', function() {
            	var dataObjs = {};
               	dataObjs.dormId = PUBLIC_DORM_ID;
               	dataObjs.dormName = PUBLIC_DORM_NAME;
               	dataObjs.gradeId = $("#newDormGrade").val();
               	dataObjs.grade = $("#newDormGrade option:selected").html();
               	dataObjs.classId = $("#newDormCl").val();
               	dataObjs.dclass = $("#newDormCl option:selected").html();
               	dataObjs.studentId = $("#stuName").val();//学生id
               	dataObjs.studentName = $("#stuName option:selected").html();//学生名字
               	dataObjs.sex = $("#stuSex").find('input').val();
               	dataObjs.studentNumber = $("#stuNum").find('input').val();
               	dataObjs.bed = $("#bed").val();
               	dataObjs.receiveGoods = $("#stuGoods").val().trim();
               	if(dataObjs.grade=="请选择"){
               		alert("请选择年级！");
               		return;
               	}
               	if(dataObjs.dclass=="请选择"){
               		alert("请选择班级！");
               		return;
               	}
               	if(dataObjs.studentName=="请选择"){
               		alert("请选择学生姓名！");
               		return;
               	}
            	if(PUBLIC_TRFA==true){
               		alert("请重新选择学生！");
               		return;
               	}
               	if(dataObjs.bed=="ALL"){
               		alert("请选择床位！");
               		return;
               	}
               	if(dataObjs.bed==0){
               		alert("此宿舍已住满！");
               		return;
               	}
//               	if(dataObjs.receiveGoods==""){
//               		alert("请填写领取物品！");
//               		return;
//               	}
               	if(!(PUBLIC_TRFA)){
               	  	$.ajax({
                   		type: "POST",
                        url: "/dorm/addDormStudent.do", 
                        data: dataObjs,
                        dataType: "json",
                        success: function(data){
                           	if(data.code==200){
                           	   alert("添加学生成功！");
                           	   Common.dialog.close($('#addStuId'));
                           	   loadDormStu(PUBLIC_DORM_ID);
                           	   newDormStuClose();
                           	}else{
                           	    alert(data.message);
                           	}
                           },
                           error:function(){
                              alert("添加学生失败！");
                           }
                   	});
               	}
            });
            
            //加载当前宿舍床位
            function loadBedNum(){
            	 $.ajax({
                 	 type: "post",
                     url: "/dorm/findDormBed.do",
                     data: {dormId:PUBLIC_DORM_ID},
                     dataType: 'json',
                     success:function(data){
                    	 var bedOption = $("#bed");
                    	 var sex = data.sex==null?"":data.sex;
                    	 PUBLIC_DORM_SEX = sex;
                     	 var bedNum = data.bed;
                     	 if(bedNum.length==0){
                     		var option="<option value='0'>已住满</option>";
                     		bedOption.append(option);
                     		return;
                     	 }
                     	 for(var i=0;i<bedNum.length;i++){
 	            	    	 var option="<option value='"+bedNum[i]+"'>"+bedNum[i]+"号床位</option>";
 	            	    	 bedOption.append(option);
 	            		 }
                     }
                 });
            };
            //删除学生按钮
            $("#dormDetial").on("click",".delStu",function(){
            	if(confirm("确定删除此学生吗?")){
                	var id = $(this).attr("flag");
                	$.ajax({
    	            	  type:"post",
    	            	  url:"/dorm/delStudent.do",
    	            	  data:{id:id},
    	            	  dataType:"json",
    	                  success:function(data){
    	                	if(data.code==200){
    	                		alert("删除成功！");
    	                		loadDormStu(PUBLIC_DORM_ID);
    	                	}else{
    	                		alert("删除失败！");
    	                	}
    	            	  },
    	            	  error:function(){
    	            		  alert("删除失败,联系管理员！");
    	            	  }
    	            });
            	}
            });
            
            //添加宿舍学生重置按钮
            $("#newDormStuReset").on("click",function(){
            	$("#bed").val("ALL");
            	resetAndClose();
            });
            //关闭添加学生按钮
            function newDormStuClose(){
            	$("#bed option[value!='ALL']").remove();
            	resetAndClose();
            };
            //添加宿舍学生重置和关闭公用部分
            function resetAndClose(){
            	$("#newDormGrade").val("ALL");
            	$("#newDormCl").html(allNewDormOptionHtml);
        		$("#stuName").html(allNewDormOptionHtml);
            	$("#stuSex").html(allInputHtml);
            	$("#stuNum").html(allInputHtml);//学号
            	$("#stuGoods").val("");
            }
             //宿舍添加学生更改年级后自动切换班级
            $("#newDormGrade").on('change',function(){
            	var selectGrade=$(this).val();
            	var isAll = false;
            	if(selectGrade == "ALL"){
            		$("#newDormCl").html(allNewDormOptionHtml);
            		clearText();
            		isAll = true;
            	}
            	clearText();
            	if(!(isAll)){
	               $.ajax({
	            	  type:"post",
	            	  url:"/dorm/findClassList.do",
	            	  dataType:"json",
	            	  data:{"gradeId":selectGrade},
	                  success:function(data){
	                	 var newDormCl = $("#newDormCl");
	                	 newDormCl.html(allNewDormOptionHtml);
	                	 for(var i=0;i<data.length;i++){
	            	    	 var option="<option value='"+data[i].id+"'>"+data[i].name+"</option>";
	            	    	 newDormCl.append(option);
	            		 }
	            	  }
	               });
            	}
            });
            function clearText(){
            	$("#stuName").html(allNewDormOptionHtml);
        		$("#stuSex").html(allInputHtml);
        		$("#stuNum").html(allInputHtml);
            };
          //宿舍添加学生更改班级后填充学生
            $('#newDormCl').on('change',function(){
            	var isAll = false;
             	var selectClass=$(this).val();
            	if(selectClass == "ALL"){
            		clearText();
            		isAll = true;
            	}
            	clearNumSex();
            	if(!(isAll)){
 	               $.ajax({
 	            	  type:"post",
 	            	  url:"/dorm/findStuInfo.do",
 	            	  dataType:"json",
 	            	  data:{"classId":selectClass},
 	                  success:function(data){
 	                	 var stuName = $("#stuName");
 	                	 stuName.html(allNewDormOptionHtml);
 	                	 for(var i=0;i<data.length;i++){
 	                		 var stuNum=data[i].studyNum==null?"":data[i].studyNum;
 	                		 var stuSex=data[i].sex==null?"":data[i].sex;
 	            	    	 var option="<option value='"+data[i].id+"' flagNum='"+stuNum+"' flagSex='"+stuSex+"'>"+data[i].studentName+"</option>";
 	            	    	 stuName.append(option);
 	            		 }
 	            	  }
 	               });
             	}
            });
            //去除学号和性别
            function clearNumSex(){
            	$("#stuNum").html(allInputHtml);
        		$("#stuSex").html(allInputHtml);
            }
          //宿舍添加学生选定学生后添加学号和性别
            $('#stuName').on('change',function(){
            	var selectStudy=$(this).val();
            	if(selectStudy == "ALL"){
            		clearNumSex();
            		return;
            	}
            	var studyNum=$(this).find("option:selected").attr("flagNum");
             	var studySex=$(this).find("option:selected").attr("flagSex");
            	$("#stuNum").html('<input readonly="readonly" value="'+studyNum+'">');
            	$("#stuSex").html('<input readonly="readonly" value="'+studySex+'">');
            	if(PUBLIC_DORM_SEX==""){
            		PUBLIC_TRFA = false;
            	    return;
            	}else{
            		if(studySex!=PUBLIC_DORM_SEX){
                		alert("此宿舍不能住"+studySex+"生,请重新选择！");
                		PUBLIC_TRFA = true;
                	}else{
                		PUBLIC_TRFA = false;
                	}
            	}
            });
            
        };

        (function() {
        	dormList.init();
        })();

        module.exports = dormList;
    })
