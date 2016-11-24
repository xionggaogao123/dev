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
define(['common'], function(require, exports, module) {
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


        Common.tab('click', $('.tab-head-new'))


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
        //关闭编辑区弹窗
        $('.closePop').on('click', function() {
        	Common.dialog.close($('#editStuDormId'));
        	return false;
        	
        })
        //关闭编辑楼弹窗
        $('.closePop').on('click', function() {
        	Common.dialog.close($('#editBuild'));
        	return false;
        	
        })
        //关闭编辑层弹窗
        $('.closePop').on('click', function() {
        	Common.dialog.close($('#editFloor'));
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

        function show(obj, id) {
            var objDiv = $("#" + id + "");
            $(objDiv).css("display", "block");
            $(objDiv).css("left", event.clientX);
            $(objDiv).css("top", event.clientY+8);
        }

        function hide(obj, id) {
            var objDiv = $("#" + id + "");
            $(objDiv).css("display", "none");
        }
        $('.objects').mouseover(function(){
            // alert(1);
            show(this,'obj1');
             show(this,'obj2');
        })
        $('.objects').mouseout(function(){
            // alert(1);
            hide(this,'obj1');
            hide(this,'obj2');
        })
        //公共的表头html
        var dormHtml = '<thead>'+
        			   '    <tr class="inHead">'+
                       '        <td width="370" class="quHead">资源名称</td>'+
                       '        <td width="100">类别</td>'+
                       '        <td>操作</td>'+
                       '    </tr>'+
                       ' </thead>';
        //新建宿舍区弹窗
        $('.addDorm').on('click', function() {
            Common.dialog($('#newDorm'));
            return false;
        })
        
      //宿舍资源页面缓存数据
        var dormQueryCatch;
        //新增宿舍区重置按钮
        $('#resetNewDormArea').on('click',function(){
        	$('#newDormAreaName').val('');
        });
        //宿舍资源配置按钮点击加载列表
//        $('#dormResourcesview').on('click',function(){
//        	$("#dormListView").html('');
//        	queryForDormView();
//        });
        
        queryForDormView();
        //*************************************************************************************************
        
        //宿舍资源查询总方法
        function queryForDormView(){
        	$.ajax({
        		type: "POST",
                url: "/dorm/findDormAreaList.do",
                dataType: "json",
                success: function(data){
                	dormQueryCatch = data;
                	fillDormList(data.dormData);
                }
          	});
        	
        } 
        //根据返回的json数据进行拼装表单中内容
        function fillDormList(dormArr){
//        	if(dormArr.length == 0){
//        		alert('还未创建任何宿舍区信息！');
//        	}
        	$("#dormListView").innerHTML = "";
        	for(var i=0;i<dormArr.length;i++){
        		var dormObj = dormArr[i];//0
        		var trHtml = '<tr class="quId" id='+dormObj.id+'>'+
				 			 '	<td class="qu"><input type="checkbox" class="checkbox"> <span';
				 	 if(dormObj.subBuild.length == 0){
						trHtml += '		class="label3" ';
		 			 }else{
		 				trHtml += '		class="label1" ';
				 	 }
        			trHtml +='flag='+dormObj.id+'> '+dormObj.dormAreaName+' </span></td>'+
        					 '	<td>'+dormObj.type+'</td>';
        		if(dormQueryCatch.isAdmin){
        			trHtml+= '	<td class="rightAlign"><a href="#" class="a edit">编辑</a> <a href="#"'+
        			'		class="a addFloor">添加宿舍楼</a> <a href="#" class="a deletes">删除</a></td>'+
        			'</tr>';
        		}
        		for(var j=0;j<dormObj.subBuild.length;j++){//2
        			trHtml += '<tr class="louId hideLou" id='+dormObj.subBuild[j].id+' flag='+dormObj.id+'>'+
   				 		   	  '	<td class="lou"><input type="checkbox" class="checkbox"> <i'+
   				 		   	  '		class="l"></i> <span';
        			if(dormObj.subBuild[j].subFloor.length == 0){
		 				trHtml += '		class="label3" ';
		 			 }else{
		 				trHtml += '		class="label1" ';
		 			 }
        			trHtml +='flagLou='+dormObj.subBuild[j].id+'>'+dormObj.subBuild[j].dormBuildName+'</span></td>'+
   				 		   	  '	<td>'+dormObj.subBuild[j].type+'</td>';
        			if(dormQueryCatch.isAdmin){
        				trHtml += '	<td class="rightAlign"><a href="#" class="a edit">编辑</a> <a href="#"'+
        				'		class="a addCeng">添加宿舍楼层</a> <a href="#" class="a deletes">删除</a></td>'+
        				'</tr>';
        			}
					 for(var k=0;k<dormObj.subBuild[j].subFloor.length;k++){
						 trHtml += '<tr class="cengId hideCeng" id='+dormObj.subBuild[j].subFloor[k].id+' flagLou='+dormObj.subBuild[j].id+' flagCeng='+dormObj.id+'>';
						 trHtml += '	<td class="ceng"><input type="checkbox" class="checkbox">';
						 if(j < dormObj.subBuild.length-1){
							 trHtml += '<i class="line">';
						 }
						 trHtml +=' </i><i class="l"></i> <span class="label4">'+dormObj.subBuild[j].subFloor[k].dormFloorName+'</span></td>'+
						 		  '	<td>'+dormObj.subBuild[j].subFloor[k].type+'</td>';
						 if(dormQueryCatch.isAdmin){
							 trHtml +='	<td class="rightAlign"><a href="#" class="a edit">编辑</a> <a href="#"'+
							 '		class="a deletes">删除</a></td>'+
							 '</tr>';
						 }
					 }
				 }
        		$('#dormListView').append(trHtml);
        	}
        }
        
        //显示楼,
        $(document).delegate('.label1','click',function(){
        	var flag = $(this).attr('flag');
        	$('tr[flag*='+flag+']').show();
           	$(this).removeClass('label1').addClass('label2');
        });
        //隐藏楼
        $(document).delegate('.label2','click',function(){
        	var flag = $(this).attr('flag');
        	$('tr[flag*='+flag+']').hide();
        	var trArr = $('tr[flag*='+flag+']');
        	for(var i =0;i<trArr.length;i++){
        		var label = $(trArr[i]).find('span').attr('class');
        		if(label == 'label2'){ 
        			$(trArr[i]).find('span').removeClass('label2').addClass('label1');
        		}
        	}
        	$('tr[flagCeng*='+flag+']').hide();
        	$(this).removeClass('label2').addClass('label1');
        });
        
        //显示层,
        $(document).delegate('.label1','click',function(){
        	var flagLou = $(this).attr('flagLou');
        	$('tr[flagLou*='+flagLou+']').show();
        	$(this).removeClass('label1').addClass('label2');
        });
        //隐藏层
        $(document).delegate('.label2','click',function(){
        	var flagLou = $(this).attr('flagLou');
        	$('tr[flagLou*='+flagLou+']').hide();
        	$('tr[flagCeng*='+flagLou+']').hide();
        	$(this).removeClass('label2').addClass('label1');
        });
        //编辑功能
        $(document).delegate('.edit','click',function(){
        	var type = $(this).parent().prev().text();
        	var classType = $(this).parent().parent().find('td').eq(0).find('span').attr('class');
        	if(type == '宿舍区'){
        		var dormAreaId = $(this).parent().parent().attr('id');
        		var dormAreaName = $(this).parent().parent().find('td').eq(0).text();
        		Common.dialog($('#editStuDormId'));
        		
        		$('#editDormAreaId').attr('flag',dormAreaId);
        		$('#editDormAreaId').attr('classType',classType);
        		$('#editDormAreaName').val(dormAreaName);
        	}
        	if(type == '宿舍楼'){
        		var dormBuildId = $(this).parent().parent().attr('id');
        		var dormAreaId = $(this).parent().parent().attr('flag');
        		var dormBuildName = $(this).parent().parent().find('td').eq(0).text();
        		Common.dialog($('#editBuild'));
        		$('.louSelect').html('');
        		$.ajax({
        			type: "POST",
                    url: "/dorm/findDormAreaFullList.do",
                    dataType: "json",
                    success: function(data){
                    	var areaListArr = data.datas;
                    	var seHtml = '<option value="ALL">--请选择--</option>';
                    	for(var i=0;i<areaListArr.length;i++){
                    		if(areaListArr[i].id == dormAreaId){
                    			seHtml += '<option value='+dormAreaId+' selected="selected">'+areaListArr[i].name+'</option>';
                    			continue;
                    		}
                    		seHtml += '<option value='+areaListArr[i].id+'>'+areaListArr[i].name+'</option>';
                    	}
                    	$('.louSelect').append(seHtml);
                    }
        		});
        		$('#editBuildId').attr('flag',dormBuildId);
        		$('#editBuildId').attr('classType',classType);
        		$('#editBuildName').val(dormBuildName);
        	}
        	if(type == '宿舍层'){
        		var dormFloorId = $(this).parent().parent().attr('id');
        		var dormBuildId = $(this).parent().parent().attr('flagLou');
        		var dormAreaId = $(this).parent().parent().attr('flagceng');
        		var dormFloorName = $(this).parent().parent().find('td').eq(0).text();
        		Common.dialog($('#editFloor'));
        		$('.cengSelect').html('');
        		var buildListArr;
        		$.ajax({
        			type: "POST",
                    url: "/dorm/findDormBuildList.do",
                    data:{'dormAreaId':dormAreaId},
                    dataType: "json",
                    success: function(data){
                    	buildListArr = data.datas;
                    	var seHtml = '<option value="ALL">--请选择--</option>';
                    	for(var i=0;i<buildListArr.length;i++){
                    		if(buildListArr[i].id == dormBuildId){
                    			seHtml += '<option value='+dormBuildId+' selected="selected">'+buildListArr[i].name+'</option>';
                    			continue;
                    		}
                    		seHtml += '<option value='+buildListArr[i].id+'>'+buildListArr[i].name+'</option>';
                    	}
                    	$('.cengSelect').append(seHtml);
                    }
        		});
        		$('#editFloorId').attr('flag',dormFloorId);
        		$('#editFloorId').attr('classType',classType);
        		$('#editFloorId').attr('flagCeng',dormAreaId);
        		$('#editFloorName').val(dormFloorName);
        	}
        });
        //编辑宿舍区请求
        $('#saveEditDormArea').on('click',function(){
        	var dormAreaId = $('#editDormAreaId').attr('flag');
        	var classType = $('#editDormAreaId').attr('classType');
        	var dormAreaName = $('#editDormAreaName').val().trim();
        	if(dormAreaName == ''){
        		alert('请填写宿舍区名称！');
        		return;
        	}
        	$.ajax({
        		type: "POST",
                url: "/dorm/updateDormArea.do",
                data: {"dormAreaName":dormAreaName,"dormAreaId":dormAreaId},
                dataType: "json",
                success: function(data){
                	alert('编辑成功！');
                	$('#editDormAreaName').val('');
                	var areaHtml = '	<td class="qu"><input type="checkbox" class="checkbox"> <span'+
		 			 '		class='+classType+' flag='+dormAreaId+'> '+dormAreaName+' </span></td>'+
		 			 '	<td>宿舍区</td>'+
                	'	<td class="rightAlign"><a href="#" class="a edit">编辑</a> <a href="#"'+
        			'		class="a addFloor">添加宿舍楼</a> <a href="#" class="a deletes">删除</a></td>'+
        			'</tr>';
                	$('#'+dormAreaId+'').html(areaHtml);
                }
        	})
        	Common.dialog.close($('#editStuDormId'));
        });
        //编辑宿舍区重置按钮
        $('#resetEditDormArea').on('click',function(){
        	$('#editDormAreaName').val('');
        });
        //编辑宿舍楼重置按钮
        $('#resetEditBuild').on('click',function(){
        	$('#editBuildName').val('');
        	$(".louSelect option:first").attr("selected",true);
        });
        //编辑宿舍楼重置按钮
        $('#resetEditFloor').on('click',function(){
        	$('#editFloorName').val('');
        	$(".cengSelect option:first").attr("selected",true);
        });
        //编辑宿舍楼请求
        $('#saveEditBuild').on('click',function(){
        	var dormBuildId = $('#editBuildId').attr('flag');//楼id
        	var dormOldAreaId = $('#'+dormBuildId).attr('flag');//原先的区id
        	var classType = $('#editBuildId').attr('classType');
        	var dormBuildName = $('#editBuildName').val().trim();//楼名字
        	var dormAreaId = $('.louSelect').val();//更改之后的
        	var cengArr = $('tr[flagLou="'+dormBuildId+'"]');
//        	var objList = {};
//        	for(var i=0;i<cengArr.length;i++){
//        		objList.floorId = $(cengArr[i]).attr('id');
//        		objList.floorName = $(cengArr[i]).find('span').text();
//        	}
        	
        	if(dormBuildName == ''){
        		alert('请填写宿舍楼名称！');
        		return;
        	}
        	if(dormAreaId == 'ALL'){
        		alert('请选择宿舍区！');
        		return;
        	}
        	$.ajax({
        		type: "POST",
                url: "/dorm/updateDormBuild.do",
                data: {"dormBuildId":dormBuildId,"dormBuildName":dormBuildName,"dormAreaId":dormAreaId},
                dataType: "json",
                success: function(data){
                	alert('编辑成功！');
                	$('#editBuildName').val('');
                	var buildHtml = '<tr class="louId " id='+dormBuildId+' flag='+dormAreaId+'>'+
                		'	<td class="lou"><input type="checkbox" class="checkbox"> <i'+
            		'		class="l"></i> <span class='+classType+' flagLou='+dormBuildId+'>'+dormBuildName+'</span></td>'+
            		'	<td>宿舍楼</td>'+
            		'	<td class="rightAlign"><a href="#" class="a edit">编辑</a> <a href="#"'+
            		'		class="a addCeng">添加宿舍楼层</a> <a href="#" class="a deletes">删除</a></td>'+
            		'</tr>';
                	if(dormOldAreaId == dormAreaId){
                		$('#'+dormBuildId+'').find('span').text(dormBuildName);
                	}else if(dormOldAreaId != dormAreaId){
                		//不再之前的区了
                		$('tr[id="'+dormBuildId+'"]').remove();
        				$('tr[flagLou="'+dormBuildId+'"]').remove();
                		//var buildArr = $('tr[flag='+dormAreaId+']');//新区有几个楼
        				if($('tr[flag='+dormOldAreaId+']').length==0){
        					$('#'+dormOldAreaId+'').find('span').removeClass('label1').removeClass('label2').addClass('label3'); 
        				}
        				$('#'+dormAreaId+'').find('span').removeClass('label1').removeClass('label2').removeClass('label3').addClass('label1'); 
        			
        				var trArr = $('.quId');
            			var index;
            			for(var i=0;i<trArr.length;i++){
            				if($(trArr[i]).attr('id') == dormAreaId){
            					index = i;
            				}
            			}
            			if(index == (trArr.length-1)){
            				$('#dormListView').append(buildHtml);
            			}else{
            				var id = $(trArr[index+1]).attr('id');
                			$('#'+id+'').before(buildHtml);
            			}
            			
            			for(var i=0;i<cengArr.length;i++){
            				var insert = dormBuildId
            				$('#'+dormBuildId).after(cengArr[i]);
            				insert = $(cengArr[i]).attr('id');
            			}
        				
            			//给当前同级上边的层增加竖线
            			var targetDom = $('#' + dormBuildId);
            			while(targetDom.prev().attr('class').indexOf('louId') == -1 && targetDom.prev().attr('class').indexOf('quId') == -1){
            				targetDom = targetDom.prev();
            				targetDom.find('input').after('<i class="line"></i>');
            			}
            			//删除下级层的前面的竖线
            			var targetDoms = $('#' + dormBuildId);
            			while(targetDoms.next().attr('class').indexOf('cengId') > -1 ){
            				targetDoms = targetDoms.next();
            				targetDoms.find('.line').remove();
            			}
            			//如果之前的去还剩一个楼，删除层之前的竖线
            			var target = $('#' + dormOldAreaId);
            			
            			var targetBuildArr = $('tr[flag='+dormOldAreaId+']');
            			var targetBuildId = $(targetBuildArr[targetBuildArr.length-1]).attr('id');//找到最后一个楼
            			var targetFloorArr = $('tr[flagLou='+targetBuildId+']');
            			for(var i=0;i<targetFloorArr.length;i++){
            				$(targetFloorArr[i]).find('.line').remove();
            			}
//            			while(true){
//            				if(target.next().attr('class').indexOf('louId') > -1){
//            					target = target.next();
//            				}
//            			}
            			
//            			while(target.next().attr('class').indexOf('louId') > -1 || target.next().attr('class').indexOf('cengId') > -1){
//            				target = target.next();
//            				
//            			}
                	}
                }
        	})
        	Common.dialog.close($('#editBuild'));
        });
        //编辑宿舍层请求
        $('#saveEditFloor').on('click',function(){
        	var dormFloorId = $('#editFloorId').attr('flag');
        	var dormOldBuildId = $('#'+dormFloorId).attr('flagLou');
        	var classType = $('#editFloorId').attr('classType');
        	var dormFloorName = $('#editFloorName').val().trim();
        	var dormBuildId = $('.cengSelect').val();
        	var dormAreaId = $('#editFloorId').attr('flagCeng');
        	//var buildCount = $('#editFloorId').attr('buildCount');
        	var arr = $('tr[flag="'+dormAreaId+'"]');
        	var num=0;
        	for(var i=0;i<arr.length;i++){
        		if($(arr[i]).attr('id') == dormBuildId){
        			num = i;
        		}
        	}
        	//alert(num+'   '+arr.length);
        	if(dormFloorName == ''){
        		alert('请填写宿舍层名称！');
        		return;
        	}
        	if(dormBuildId == 'ALL'){
        		alert('请选择宿舍楼！');
        		return;
        	}
        	$.ajax({
        		type: "POST",
        		url: "/dorm/updateDormFloor.do",
        		data: {"dormFloorId":dormFloorId,"dormFloorName":dormFloorName,"dormBuildId":dormBuildId},
        		dataType: "json",
        		success: function(data){
        			alert('编辑成功！');
        			$('#editFloorName').val('');
        			
        			var floorHtml = '<tr class="cengId " id='+dormFloorId+' flagLou='+dormBuildId+' flagCeng='+dormAreaId+'>'+
        							'	<td class="ceng"><input type="checkbox" class="checkbox">';
        							if(num != (arr.length-1)){
        								floorHtml += '<i class="line">';	
        	        				}
        							floorHtml +=' </i><i class="l"></i> <span class="label4">'+dormFloorName+'</span></td>'+
        							'	<td>宿舍层</td>'+
        							'	<td class="rightAlign"><a href="#" class="a edit">编辑</a> <a href="#"'+
        							'		class="a deletes">删除</a></td>'+
        							'</tr>';
					if(dormOldBuildId == dormBuildId){
						$('#'+dormFloorId+'').find('span').text(dormFloorName);
					}else if(dormOldBuildId != dormBuildId){
        				$('tr[id="'+dormFloorId+'"]').remove();
        				if($('tr[flagLou='+dormOldBuildId+']').length==0){
        					$('#'+dormOldBuildId+'').find('span').removeClass('label1').removeClass('label2').removeClass('label3').addClass('label3'); 
        				}
        				var targetDom = $('#' + dormBuildId);
            			while(true){
            				if(targetDom.next().length == 0
                				|| targetDom.next().attr('class').indexOf('louId') > -1 
                					|| targetDom.next().attr('class').indexOf('quId') > -1 ){
            					$('#'+dormBuildId).find('span').removeClass('label1').removeClass('label2').removeClass('label3').addClass('label2'); 
            					//alert(floorHtml);
            					targetDom.after(floorHtml);
            					break;
            				}
            				targetDom = targetDom.next();
            			}
					}
        		}
        	})
        	Common.dialog.close($('#editFloor'));
        });
        
       //*添加宿舍区/楼/层模块***************************************************************
      //新增宿舍区保存按钮
        $('#saveNewDormArea').on('click',function(){
        	var dormAreaName = $.trim($('#newDormAreaName').val());
        	if(dormAreaName == ""){
        		alert('请填写宿舍区名称！');
        		return;
        	}
        	$.ajax({
        		type: "POST",
                url: "/dorm/newDormArea.do",
                data: {"dormAreaName":dormAreaName},
                dataType: "json",
                success: function(data){
                	alert('新增成功！');
                	$('#newDormAreaName').val('');
                	var trHtml = '<tr class="quId" id='+data.areaId+'>'+
		 			 '	<td class="qu"><input type="checkbox" class="checkbox"> <span'+
		 			 '		class="label3" flag='+data.areaId+'> '+dormAreaName+' </span></td>'+
		 			 '	<td>宿舍区</td>'+
		 			 '	<td class="rightAlign"><a href="#" class="a edit">编辑</a> <a href="#"'+
		 			 '		class="a addFloor">添加宿舍楼</a> <a href="#" class="a deletes">删除</a></td>'+
		 			 '</tr>';
                	$('#dormListView').append(trHtml);
                	
                }
        	})
        	Common.dialog.close($('#newStuDormId'));
        });
        //添加宿舍楼
        
        $(document).delegate('.addFloor','click',function(){
        	var dormAreaId = $(this).parent().parent().attr('id');
        	var dormAreaName = $(this).parent().parent().find('td').eq(0).text();
        	Common.dialog($('#newLou'));
        	$('#belongDromAreaId').attr('flag',dormAreaId);
        	$('#belongDromAreaName').val(dormAreaName);
        });
        //新建宿舍楼重置按钮
        $('#resetDormBuild').on('click',function(){
        	$('#newDormBuildName').val('');
        });
        //添加宿舍楼请求
        $('#saveDormBuild').on('click',function(){
        	var dormAreaId = $('#belongDromAreaId').attr('flag');
        	var dormBuildName = $('#newDormBuildName').val().trim();
        	var dromAreaName = $('#belongDromAreaName').val().trim();
        	var isHidden = ($('#' + dormAreaId).find('.label1,.label3').length > 0) ? true:false;
        	if(dormBuildName == ""){
        		alert('请填写宿舍楼名称');
        		return;
        	}
        	if(dromAreaName == ""){
        		alert('请填写宿舍区名称');
        		return;
        	}
        	$.ajax({
        		type: "POST",
        		url: "/dorm/newDormBuild.do",
        		data: {"dormBuildName":dormBuildName,"dormAreaId":dormAreaId},
        		dataType: "json",
        		success: function(data){
        			alert('新建宿舍楼成功！');
        			$('#newDormBuildName').val('');
        			//首先改变当
        			if(isHidden){
        				$('#' + dormAreaId).find('.label3').removeClass('label3').addClass('label1');
        			}
        			var buildHtml = '<tr class="louId ' + (isHidden?' hideLou':'') + '" id='+data.builId+' flag='+dormAreaId+'>'+
        							'	<td class="lou"><input type="checkbox" class="checkbox"> <i'+
			 		   	  			'		class="l"></i> <span class="label3" flagLou='+data.builId+'>'+dormBuildName+'</span></td>'+
			 		   	  			'	<td>宿舍楼</td>'+
			 		   	  			'	<td class="rightAlign"><a href="#" class="a edit">编辑</a> <a href="#"'+
        							'		class="a addCeng">添加宿舍楼层</a> <a href="#" class="a deletes">删除</a></td>'+
        							'</tr>';
        			var trArr = $('.quId');
        			var index;
        			for(var i=0;i<trArr.length;i++){
        				if($(trArr[i]).attr('id') == dormAreaId){
        					index = i;
        				}
        			}
        			
        			if(index == (trArr.length-1)){
        				$('#dormListView').append(buildHtml);
        			}else{
        				var id = $(trArr[index+1]).attr('id');
            			$('#'+id+'').before(buildHtml);
            			$('#newDormBuildName').val('');
            			$('#belongDromAreaName').val('');
        			}
        			//给当前同级上边的层增加竖线
        			var targetDom = $('#' + data.builId);
        			while(targetDom.prev().attr('class').indexOf('louId') == -1 && targetDom.prev().attr('class').indexOf('quId') == -1){
        				targetDom = targetDom.prev();
        				targetDom.find('input').after('<i class="line"></i>');
        			}
        		}
        	});
        	Common.dialog.close($('#newLou'));
        });
        //添加宿舍层
        $(document).delegate('.addCeng','click',function(){
        	var dormBuildId = $(this).parent().parent().attr('id');
        	var dormAreaId = $(this).parent().parent().attr('flag');
        	//alert(dormAreaId);
        	var dormBuildName = $(this).parent().parent().find('td').eq(0).text();
//        	alert(dormBuildId+dormBuildName);
        	Common.dialog($('#newFloor'));
        	$('#belongBuildId').attr('flag',dormBuildId);
        	$('#belongBuildId').attr('areaId',dormAreaId);
        	$('#belongBuildName').val(dormBuildName);
        });
        //新建宿舍层重置按钮
        $('#resetDormFloor').on('click',function(){
        	$('#newDormFloorName').val('');
        }); 
      //添加宿舍层请求
        $('#saveDormFloor').on('click',function(){
        	var dormBuildId = $('#belongBuildId').attr('flag');
        	var dormAreaId = $('#belongBuildId').attr('areaId');
        	//alert(dormAreaId);
        	var dormFloorName = $('#newDormFloorName').val().trim();
        	var dromBuildName = $('#belongBuildName').val().trim();
        	var isHidden = $('#' + dormBuildId).find('.label1,.label3').length > 0 ? true:false;
        	var arr = $('tr[flag="'+dormAreaId+'"]');
        	var num=0;
        	for(var i=0;i<arr.length;i++){
        		if($(arr[i]).attr('id') == dormBuildId){
        			num = i;
        		}
        	}
        	
        	if(dormFloorName == ""){
        		alert('请填写宿舍层名称');
        		return;
        	}
        	if(dromBuildName == ""){
        		alert('请填写宿舍楼名称');
        		return;
        	}
        	$.ajax({
        		type: "POST",
        		url: "/dorm/newDormFloor.do",
        		data: {"dormFloorName":dormFloorName,"dormBuildId":dormBuildId},
        		dataType: "json",
        		success: function(data){
        			alert('新建宿舍层成功！');
        			$('#newDormFloorName').val('');
        			//改变加减号
        			if(isHidden){
        				$('#' + dormBuildId).find('.label3').removeClass('label3').addClass('label1');
        			}
        			var floorHtml = '<tr class="cengId' + (isHidden?' hideCeng':'') + '" id='+data.floorId+' flagLou='+dormBuildId+' flagCeng='+dormAreaId+'>'+
        							'	<td class="ceng"><input type="checkbox" class="checkbox">';
        				if(num != (arr.length-1)){
        					floorHtml+= '<i class="line">';	
        				}				
        				floorHtml+=' </i><i class="l"></i> <span class="label4">'+dormFloorName+'</span></td>'+
					  				'	<td>宿舍层</td>'+
					  				'	<td class="rightAlign"><a href="#" class="a edit">编辑</a> <a href="#"'+
					  				'		class="a deletes">删除</a></td>'+
					  				'</tr>';
        			//*****
        			var targetDom = $('#' + dormBuildId);
        			while(true){
        				if(targetDom.next().length == 0
            				|| targetDom.next().attr('class').indexOf('louId') > -1 
            					|| targetDom.next().attr('class').indexOf('quId') > -1 ){
        					targetDom.after(floorHtml);
        					break;
        				}
        				targetDom = targetDom.next();
        			}
        		}
        	});
        	Common.dialog.close($('#newFloor'));
        });
        
        //删除操作
        $(document).delegate('.deletes','click',function(){
        	var type = $(this).parent().prev().text();
        	var url = "";
        	var dataType = {};
        	var id = $(this).parent().parent().attr('id');
        	if(confirm("确定删除该"+type+"吗？")){
        		if(type == "宿舍区" ){
    				url= "/dorm/findDormBuildList.do";
    				dataType = {"dormAreaId":id};
    			}else if(type == "宿舍楼"){
    				url= "/dorm/findDormFloorList.do";
    				dataType = {"dormBuildId":id};
    			}else{
    				url= "/dorm/findDormCount.do"
    				dataType = {"id":id};	
    			}
        		$.ajax({
        			type: "POST",
        			url:url,
            		data: dataType,
            		dataType: "json",
            		success: function(data){
            			var num = data.datas.length;
            			if(num == 0){
            				if(type == "宿舍区" ){
        	    				url= "/dorm/deleteDormArea.do";
        	    			}else if(type == "宿舍楼"){
        	    				url= "/dorm/deleteDormBuild.do";
        	    			}else{
        	    				url= "/dorm/deleteDormFloor.do";
        	    			}
        	        		$.ajax({
        	        			type: "POST",
        	        			url:url,
        	            		data: {"id":id},
        	            		dataType: "json",
        	            		success: function(data){
        	            			alert('删除成功！');
                    				if(type == "宿舍区" ){
                        				$('tr[id="'+id+'"]').remove();
                        				$('tr[flag="'+id+'"]').remove();
                        				$('tr[flagCeng="'+id+'"]').remove();
                        			}else if(type == "宿舍楼"){
                        				var areaId = $('#'+id+'').attr('flag');
                        				$('tr[id="'+id+'"]').remove();
                        				$('tr[flagLou="'+id+'"]').remove();
                        				if($('tr[flag='+areaId+']').length==0){
                        					$('#'+areaId+'').find('span').removeClass('label1').removeClass('label2').addClass('label3'); 
                        				}
                        				
                        			}else{
                        				var buildId = $('#'+id+'').attr('flagLou');
                        				$('tr[id="'+id+'"]').remove();
                        				if($('tr[flagLou='+buildId+']').length==0){
                        					$('#'+buildId+'').find('span').removeClass('label1').removeClass('label2').addClass('label3'); 
                        				}
                        			}
        	            		}
        	        		});
            			}else if(num > 0){
            				if(confirm("确定删除该"+type+"吗？此操作会删除该"+type+'下的所有内容！')){
            	        		if(type == "宿舍区" ){
            	    				url= "/dorm/deleteDormArea.do";
            	    			}else if(type == "宿舍楼"){
            	    				url= "/dorm/deleteDormBuild.do";
            	    			}else{
            	    				url= "/dorm/deleteDormFloor.do";
            	    			}
            	        		$.ajax({
            	        			type: "POST",
            	        			url:url,
            	            		data: {"id":id},
            	            		dataType: "json",
            	            		success: function(data){
            	            			alert("删除成功！");
            	            			if(type == "宿舍区" ){
            	            				$('tr[id="'+id+'"]').remove();
            	            				$('tr[flag="'+id+'"]').remove();
            	            				$('tr[flagCeng="'+id+'"]').remove();
            	            			}else if(type == "宿舍楼"){
            	            				var areaId = $('#'+id+'').attr('flag');
            	            				$('tr[id="'+id+'"]').remove();
            	            				$('tr[flagLou="'+id+'"]').remove();
            	            				if($('tr[flag='+areaId+']').length==0){
            	            					$('#'+areaId+'').find('span').removeClass('label1').removeClass('label2').addClass('label3'); 
            	            				}
            	            				
            	            			}else{
            	            				var buildId = $('#'+id+'').attr('flagLou');
            	            				$('tr[id="'+id+'"]').remove();
            	            				if($('tr[flagLou='+buildId+']').length==0){
            	            					$('#'+buildId+'').find('span').removeClass('label1').removeClass('label2').addClass('label3'); 
            	            				}
            	            			}
            	            		}
            	        		});
            				}
            			}
            		}
        		});
        	}
        	
        });
    };


    (function() {
        dorm.init();
    })();

    module.exports = dorm;
})