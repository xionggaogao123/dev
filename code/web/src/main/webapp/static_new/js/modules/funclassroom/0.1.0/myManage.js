/**
 *created by xxx on 15/12/17
 */
/**
 *@author xxx
 *@module 功能教室管理
 *@description
 */
/*global config*/
define(['common'], function(require, exports, module) {
	/**
	 *初始化参数
	 */
	var classM = {},
		Common = require('common');
	Array.prototype.remove = function(dx) {
		if (isNaN(dx) || dx > this.length) {
			return false;
		}
		this.splice(dx, 1);

	};
	
	//初始化教室页面参数
	var userData;
	var doubleData
	var toPageTotal;
    var queryData={};
    var firstPageNo=1;
    var pageSize=10;
    var isFirstPage=true;
   
	var classRoomid;
	var className;
	/**
	 * @func init
	 * @desc 页面初始化
	 * @example
	 */
	classM.init = function() {
//
//
//		Common.tab('click',$('.tab-head-new'));
//		$('.new-page-links').jqPaginator({
//			totalPages: 100,
//			visiblePages: 5,
//			currentPage: 1,
//			first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
//            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
//            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
//            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
//            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
//			onPageChange: function(num, type) {
//				// alert("当前第"+num+"页")
//				// $('#text').html('当前第' + num + '页');
//			}
//		});

		$('.addClass').on('click', function(event) {
			Common.dialog($('#addClass'));
			return false;
		});
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#addClass'));
		});
		$('.editClass').on('click', function(event) {
			Common.dialog($('#editClass'));
			return false;
		});
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#editClass'));
		});
		$('.count').on('click', function(event) {
			Common.dialog($('#count'));
			return false;
		});
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#count'));
		});
		
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#editReserv'));
		});

		$('.lookReserv').on('click',function(event) {
			$('#lookReserv').css('display', 'block').siblings().hide();
			return false;
		});
		


		$('.order').on('click', function(event) {

			Common.dialog($('#order'));
			return false;
		});
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#order'));
		});
		
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#tip'));
		});

		//点击管理员预约弹窗
		$('.adminOrder').on('click', function(event) {
			$("#detilStartTime").val("");
			$("#detilEndTime").val("");
			$("#useReson").val("");
			Common.dialog($('#adminOrder'));
			return false;
		});
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#adminOrder'));
		});
		$('.closePop').on('click', function(event) {
			Common.dialog.close($('#manEdit'));
		});
		$('.gray-table-body').on('click', '.index a', function(event) {
			$(this).parents('.gray-table-body').hide().siblings().css('display', 'block');			
		});

		// $('.tip').on('click', function(event) {
		// 	Common.dialog($('#tip'));
		// 	return false;
		// });
		//查看教室预约详情
		$(document).delegate('.look-classMan','click',function(){
			classRoomid=$(this).parent().parent().attr('flag');
			className = $(this).parent().prev().prev().text();
			$('#look-classMan div:eq(0)').html('<a href="#">教室列表</a>&gt;<span>'+className+'</span>');
			reservationList(classRoomid,detilFirstPageNo);
			$('#look-classMan').css('display', 'block').siblings().hide();
			return false;
		});

		//点击编辑弹窗
		$(document).delegate('.manEdit','click', function(event) {
			$("#startTimeTr").val("");
			$("#endTimeTr").val("");
			$("#resonTr").val("");
			$("#userTr").val("");
			var detilId=$(this).parent().parent().attr('flag');
			var startTime=$(this).parent().prev().prev().prev().prev().text().replace(new RegExp('/', 'g'),'-');
			var endTime=$(this).parent().prev().prev().prev().text().replace(new RegExp('/', 'g'),'-');
			var user=$(this).parent().prev().prev().text();
			var resons=$(this).parent().prev().attr('title');
//			alert(detilId+''+startTime+''+endTime+''+user+''+resons);
			$("#userTr").val(user);
			$("#startTimeTr").val(startTime);
			$("#endTimeTr").val(endTime);
			$("#resonTr").val(resons);
			$("#detilId").attr("flag",detilId);
			Common.dialog($('#manEdit'));
		});

		/**
		 * 页面加载后显示教室管理者所管理的教室
		 */
		$(function(){
			showManageRooms(1);
			
			
		});
		
		//显示方法
		function showManageRooms(pageNo){
			var dObj={};
        	if(pageNo==null){
        		dObj.pageNo=firstPageNo;
        	}else{
        		dObj.pageNo=pageNo;
        	}
        	dObj.pageSize=pageSize;
			$.ajax({
				type:"post",
				url:"/functionclassroom/mymanagerooms.do",
				data:dObj,
				dataType:"json",
				success:function(data){
					toPageTotal=data.total/pageSize;
					//alert(data);
					$("#pageFenye").jqPaginator({
        				totalPages:Math.ceil(data.total/pageSize)==0?1:Math.ceil(data.total/pageSize),
        				visiblePages:5,
        				currentPage:parseInt(pageNo),
        				first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
           	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
           	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
           	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
           	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
           	            onPageChange:function(n){
//           	        	 alert('当前第'+n+'页');
           	        	 if(isFirstPage){
           	        		showManageRooms(n);
           	        		isFirstPage=false;
           	        	 }else{
           	        		isFirstPage=true;
           	        		return;
           	        	 }
           	         }
        			});
					queryClassRoom(data.classRoom);
				}
			});
		}
		//我管理的教室表头
//		function classRoomHead(){
//			var cHtml= '<tr >';
//			cHtml=cHtml+'<th class="theadbg">序号</th>';
//			cHtml=cHtml+'<th class="theadbg">教室名称</th>';
//			cHtml=cHtml+'<th class="theadbg">管理者</th>'; 
//			cHtml=cHtml+'<th class="theadbg">操作</th>';   
//			cHtml=cHtml+'</tr>';
//			$("#classRoomHead").html(cHtml);
//		}
		//拼接教室显示内容
		function queryClassRoom(cl){
			userData=cl;
			$("#classRoomHead").html("");
			if(userData.length==0){
				var dHtml= '<tr><td style="border:0" colspan="4">暂无数据</td></tr>';
				$("#classRoomHead").append(dHtml); 
				$("#pageFenye").hide();
			}else{
//			classRoomHead();
//			alert(cl);
				for(var i=0;i<cl.length;i++){
					var subClass=cl[i];
					var name = '';
					if (subClass.userList!=null && subClass.userList.length!=0) {
						for (var j=0;j<subClass.userList.length;j++) {
							name += subClass.userList[j].userName + " ";
						}
					}
					var   tHtml='<tr flag='+subClass.roomId+'> ';
					tHtml=tHtml+ '<td>'+subClass.num+'</td>';
					tHtml=tHtml+ '<td>'+subClass.roomName+'</td>';
					tHtml=tHtml+ '<td>'+name+'</td>';
					tHtml=tHtml+ '<td><a href="#look-classMan" class="abtn look-classMan" >查看</a></td>';
					tHtml=tHtml+'</tr>';
					$("#classRoomHead").append(tHtml);
					$("#pageFenye").show();
				}
			}
		}
		
		//给功能教室添加到该页
		$("#toThisPage").click(function(){
			var toPage = $(this).prev().val();
			if((isNaN(toPage))&&(toPage>toPageTotal)&&(toPage<1)){
				alert("您输入的页数有误,请重新输入!");
			}else{
				showManageRooms(toPage);
			}
			$(this).prev().val("");
		});
		 //初始化预约详情页面参数
	    var detilFirstPageNo=1;
	    var detilPageSize=10;
		var isDetilFisrtPage = true;
		//查看教室预约详情分页
		function reservationList(classRoomid,PageNo){
			var rObj={};
			if(PageNo==null){
        		rObj.pageNo=detilFirstPageNo;
        	}else{
        		rObj.pageNo=PageNo;
        	}
        	rObj.pageSize=detilPageSize;
        	rObj.roomId=classRoomid;
			$.ajax({
				type:"post",
				url:"/functionclassroom/roomdetils.do",
				data:rObj,
				dataType:"json",
				success:function(data){
					//alert(data);
					$("#detilPageFenye").jqPaginator({
        				totalPages:Math.ceil(data.totals/detilPageSize)==0?1:Math.ceil(data.totals/detilPageSize),
        				visiblePages:5,
        				currentPage:parseInt(PageNo),
        				first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
           	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
           	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
           	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
           	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
           	            onPageChange:function(n){
//           	        	 alert('当前第'+n+'页');
           	        	 if(isDetilFisrtPage){
           	        		reservationList(classRoomid,n);
           	        		isDetilFisrtPage=false;
           	        	 }else{
           	        		isDetilFisrtPage=true;
           	        		return;
           	        	 }
           	         }
        			});
					queryRoomDetils(data.roomDetils);
				}
			});
		}


		//拼接教室预约
		function queryRoomDetils(detilList){
			$("#roomDetils").html("");
			if(detilList.length==0){
				var dHtml= '<tr><td style="border:0" colspan="5">暂无数据</td></tr>';
				$("#roomDetils").append(dHtml);
				$('#detilPageFenye').hide();
			}else{
				var subReason;
				var reason;
				for(var i=0;i<detilList.length;i++){
					reason=detilList[i].resons;
					if(reason.length<=11){
						subReason=reason;
					}else{
						subReason=reason.substring(0,10)+'...';
					}
					var dHtml='<tr flag='+detilList[i].id+'>';
					dHtml=dHtml+'<td>'+detilList[i].startTime+'</td>';
					dHtml=dHtml+'<td>'+detilList[i].endTime+'</td>';
					dHtml=dHtml+'<td>'+detilList[i].user+'</td>';
					dHtml=dHtml+'<td style="cursor:pointer" title='+detilList[i].resons+'>'+subReason+'</td>';
					dHtml=dHtml+'<td><a href="#" class="abtn  manEdit">编辑</a><a href="#" class="manDel del">删除</a></td>';
					dHtml=dHtml+' </tr>';
					$("#roomDetils").append(dHtml);
					$('#detilPageFenye').show();
				}
			}
		}
		
		//教室预约到该页添加点击事件
		$("#detilToPage").click(function(){
			var toPage = $(this).prev().val();
			if((isNaN(toPage))&&(toPage>toPageTotal)&&(toPage<1)){
				alert("您输入的页数有误,请重新输入!");
			}else{
				reservationList(classRoomid,toPage);
			}
			$(this).prev().val("");
		});

		//判断数据是否完整
		function complite(d){
			if(d.startTime==""){
				alert("请选择开始时间!");
				return false;
			}else if(d.endTime==""){
				alert("请选择结束时间!");
				return false;
			}else if(d.startTime>d.endTime){
				alert("您选择的时间不正确!");
				return false;
			}else if(d.reasons==""){
				alert("请输入使用说明!");
				return false;
			}
			return true;
			
		}
		var aObj={};
		//管理员增加预约
		$("#addRev").click(function(){
			
			
			aObj.classRoomId=classRoomid;
			aObj.startTime=$("#detilStartTime").val();
			aObj.endTime=$("#detilEndTime").val();
			aObj.reasons=$("#useReson").val().trim();
			
			//判断管理员预约时间是否与其他老师预约时间有冲突
			$.ajax({
				type:"post",
				url:"/functionclassroom/finddoubledetils.do",
				data:aObj,
				dataType:"json",
				success:function(data){
					doubleData=data
					//如果时间没有冲突直接添加
					if(data.flag==0){
						if(complite(aObj)){
							addNewReservation(aObj);
						}
					//有冲突
					}else if(data.flag==1){
						if(complite(aObj)){
							Common.dialog($('#tip'));
							$("#revP").html('<p id="revP">该时间段已有<span>'+data.numberOfPerson+'</span>人预约，是否要继续预约？</p>');
							//拼接冲突预约详情
							queryInReservationDetils(data.roomDtls);
						}
					}
				}
			});
		});
		$("#continueRev").on('click',function(){
			deleteDrev(aObj);
			addNewReservation(aObj);
			Common.dialog.close($('#tip'));
		});
			
		//管理员删除占用预约
		function deleteDrev(aObj){
			//删除冲突预约
			 //获取当前页码
			 var pageNO = $('#look-classMan li[class="page active"]').attr('jp-data');
			 //获取当前页的所有数据条数
			 var num = $('#look-classMan a[class="manDel"]').length;
			 if(num==1&&pageNO!=1){
				 pageNO=pageNO-1;
			 }
			//获取每一个预约记录的id
			//var ddObj={};
			var arrIds = $('#inReservationDetils tr');
			var str= '';
			for(var i=0;i<arrIds.length;i++){
				dtId=$(arrIds[i]).attr('flag');
				
				if(i==(arrIds.length-1)){
					str += dtId;
				}else{
					str += dtId+",";
				}
			}
//			alert(str);
			$.ajax({
				async:false,
				type:"post",
				url:"/functionclassroom/deleteDoublerev.do",
				data:{'str':str},
				dataType:"json",
				success:function(data){
//					alert('删除成功!');
				}
			});
		//	alert($("#myClassName").find('span').text());
//			reservationList(classRoomid,pageNO);
			//删除之后给原先预定的老师发送通知
			var reciver=[];
			var message ="很抱歉,由于管理员 "+userData.administratorName+"在"+aObj.startTime+"到"+aObj.endTime+"预定了"+$("#myClassName").find('span').text()+",您的预约已被取消!";
			var roomDts=doubleData.roomDtls;
			var userId=userData.userId;
			var title="管理员取消教室预约通知";
			
			for(var i=0;i<roomDts.length;i++){
				reciver[i]=roomDts[i].reciver;
			}
		//	alert(reciver.length);
			var reciveInfo = '';
			for(var i=0;i<reciver.length;i++){
				if(i==(reciver.length-1)){
					reciveInfo +=reciver[i];
				}else{
					reciveInfo += reciver[i]+",";
				}
			}
		//	alert(reciveInfo);
			$.ajax({
				async:false,
				type:"post",
				url:"/notice/simple/add.do",
				data:{'userId':userId,'tus':reciveInfo,'title':title,'content':message},
				dataType:"json",
				success:function(data){
					alert('发送成功');
				}
			});
		}
		
		//拼接显示同时预约的信息
		function queryInReservationDetils(roomDtls){
			$("#inReservationDetils").html("");
			var reason;
			for(var i=0;i<roomDtls.length;i++){
				reason=roomDtls[i].reason;
				if(reason.length<=10){
					subReason=reason;
				}else{
					subReason=reason.substring(0,10);
				}
				var dHtml='<tr flag='+roomDtls[i].dId+'>'+
						'<td width="126">'+roomDtls[i].startTime+'</td>'+
						'<td width="126">'+roomDtls[i].endTime+'</td>'+
						'<td width="78">'+roomDtls[i].user+'</td>'+
						'<td style="cursor:pointer" title='+roomDtls[i].reason+'>'+subReason+'</td>'+
						'</tr>';
				$("#inReservationDetils").append(dHtml);
			}
		}
		//增加预约方法
		function addNewReservation(aObj){
			 //获取当前页码
			 var pageNO = $('#look-classMan li[class="page active"]').attr('jp-data');
			 //获取当前页的所有数据条数
			 var num = $('#look-classMan a[class="manDel"]').length;
			 if(num==1&&pageNO!=1){
				 pageNO=pageNO-1;
			 }
			$.ajax({
				type:"post",
				url:"/functionclassroom/addrev.do",
				data:aObj,
				dataType:"json",
				success:function(data){
					alert("添加成功！");
					Common.dialog.close($('#adminOrder'));
					reservationList(aObj.classRoomId,pageNO);
				}
			});
		}

		//点击取消关闭预约弹窗
		$("#addCan").click(function(){
			Common.dialog.close($('#adminOrder'));
		});
		
		//确认编辑预约信息
		$("#manSure").click(function(){
			 //获取当前页码
			var pageNO = $('#look-classMan li[class="page active"]').attr('jp-data');
			var uObj={};
			uObj.detilId=$("#detilId").attr("flag");
			uObj.startTime=$("#startTimeTr").val().replace(new RegExp('-', 'g'),'/');
			uObj.endTime=$("#endTimeTr").val().replace(new RegExp('-', 'g'),'/');
			uObj.reason=$("#resonTr").val();
			var userName=$("#userTr").val();
			if(uObj.endTime<uObj.startTime){
				alert("您选择的结束时间不应小于开始时间,请重新选择!");
				return false;
			}
			$.ajax({
				type:"post",
				url:"/functionclassroom/updaterev.do",
				data:uObj,
				dataType:"json",
				success:function(data){
					alert("修改成功！");
					Common.dialog.close($('#manEdit'));
//					var dHtml = '<td>'+uObj.startTime+'</td>'+
//									  '<td>'+uObj.endTime+'</td>'+
//									  '<td>'+userName+'</td>'+
//									  '<td>'+uObj.reason+'</td>'+
//									  '<td><a href="#" class="abtn  manEdit">编辑</a><a href="#" class="del manDel">删除</a></td>';
//					$('tr[flag='+uObj.detilId+']').html(dHtml);
					reservationList(classRoomid,pageNO);
				}
			});
			
		});
		//点击取消关闭编辑弹窗
		$("#manCan").click(function(){
			Common.dialog.close($('#manEdit'));
		});
			
		//删除一条预约信息
		 $('#manageClass').delegate('.manDel','click',function(){
			 //获取当前页码
			 var pageNO = $('#look-classMan li[class="page active"]').attr('jp-data');
			 //获取当前页的所有数据条数
			 var num = $('#look-classMan a[class="manDel"]').length;
			 if(num==1&&pageNO!=1){
				 pageNO=pageNO-1;
			 }
			 var dObj={};
			 dObj.rsid=$(this).parent().parent().attr("flag");
			 if(confirm("确定删除该条预约?")){
				 $.ajax({
						type:"post",
						url:"/functionclassroom/deleterev.do",
						data:dObj,
						dataType:"json",
						success:function(data){
							alert("删除成功！");
//		没有用					$('tr[flag='+detilId+']').remove();
//		没有用					$(this).parent().parent().remove();
							
							reservationList(classRoomid,pageNO);
						}
					}); 
			 }
		 });
		 
		 $("#updateList").click(function(){
			//获取当前页码
			 var pageno = $('#classMsg li[class="page active"]').attr('jp-data');
			 var pageNO = $('#look-classMan li[class="page active"]').attr('jp-data');
			 showManageRooms(pageno);
			 reservationList(classRoomid,pageNO);
			 $.ajax({
				 type:"post",
					url:"/functionclassroom/findClassRoomName.do",
					data:{"classRoomId":classRoomid},
					dataType:"json",
					success:function(data){ 
						$('#look-classMan div:eq(0)').html('<a href="#">教室列表</a>&gt;<span>'+data+'</span>');
					}
			 });
			 
			 
		 });
		 

//********************************init初始化结束********************************************************
	};
//*****************************************************************************************************	
	(function() {
		classM.init();
	})();

	module.exports = classM;

});
