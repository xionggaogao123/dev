/**
 * Created by liwei on 15/7/12.
 */
/**
 * @author 李伟
 * @module  器材登记操作
 * @description
 * 器材登记操作
 */
/* global Config */
define(['common', 'ajaxfileupload','treeview','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var qcdj = {},
        Common = require('common');
        require('treeview');
        require('ajaxfileupload'); 
        require('pagination');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }
    //树数据
    var treeData;
    var targetNode;
    var isInitTree = false;
    var isFirst = true;
    
  //重组数据库整棵树
    function rebuildTree(){
    	var treeJsonStr = JSON.stringify($.fn.zTree.getZTreeObj('treeDemo').getNodes())
    	$.ajax({
    		type: "POST",
            url: "/equipmentClassification/rebuildAllEquipmentClassification.do", 
            data: {treeJsonStr : treeJsonStr},
            dataType: "json",
            success: function(data){
            	if(!isFirst){
            		alert("操作成功！");
            		isFirst = false;
            	} 
//            	loadAndShowTree();
            }
    	}); 
    };
    /***
     * @func treeview
     * @desc 树节点
     * @example
     * qcdj.treeview([{},{}])
     */

    qcdj.treeview = function(zNodes){
        var setting = {
            edit: {
                enable: true,
                showRemoveBtn: false,
                showRenameBtn: false
            },
            view: {
                showIcon: showIconForTree
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                beforeRename : beforeRename,
                onClick : clickTreeNodeFunction,
                onDrop: zTreeOnDrop,
                beforeDrop: zTreeBeforeDrop,
                beforeDrag: zTreeBeforeDrag
            }

        };
      //拖拽之前调用方法，组织拖拽根节点
        function zTreeBeforeDrag(treeId, treeNodes) {
            return !(treeNodes[0].id == "0"); 
        };
      //拖拽放下之前
        function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) {
            return !(targetNode == null || (moveType != "inner" && !targetNode.parentTId));
        };
      //拖拽后
        function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
        	rebuildTree();
        };
        
        
        
        function showIconForTree(treeId, treeNode) {
            return !treeNode.isParent;
        };

        var newCount = 1, className = 'dark';

        function add(e) {
            var zTree = $.fn.zTree.getZTreeObj('treeDemo'),
                isParent = e.data.isParent,
                nodes = zTree.getSelectedNodes(),
                treeNode = nodes[0];
            if(treeNode){
            	if(isParent &&treeNode.pId == null){
                	alert("无法新增根节点！");
                	return;
                }
            }else{
            	alert("请先选择一个节点！");
            	return;
            }
            var addTreeNodeObj = {};
            if(isParent){
            	addTreeNodeObj.equipmentClassificationParentId = treeNode.pId;
            }else{
            	addTreeNodeObj.equipmentClassificationParentId = treeNode.id;
            }
            $.ajax({
        		type: "POST",
                url: "/equipmentClassification/addEquipmentClassification.do",
                data: addTreeNodeObj,
                dataType: "json",
                success: function(data){
//                	$("#treeDemo").html("");
                	loadAndShowTree();
                	
//                	 if (treeNode) {
//                         treeNode = zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, isParent:isParent, name:'新子节点'+ (newCount++),flag:"q1231321313"});
//                     } else {
//                         treeNode = zTree.addNodes(null, {id:(100 + newCount), pId:0, isParent:isParent, name:'新父节点' + (newCount++)}); 
//                     }
//                     if (treeNode) {
//                         zTree.editName(treeNode[0]);
//                     } else {
//                         alert("叶子节点被锁定，无法增加子节点");
//                     }
                }
        	}); 
            
           
            return false;
        };

        function remove(e) {
            var zTree = $.fn.zTree.getZTreeObj('treeDemo'),
                nodes = zTree.getSelectedNodes(),
                treeNode = nodes[0];
            if (nodes.length == 0) {
                alert('请先选择一个节点');
                return;
            }
            if(treeNode.pId == null){
            	alert("无法删除根根节点！");
            	return;
            }
            if(confirm("确认删除节点：" + treeNode.name + "和其所有子节点吗？")){
//                var callbackFlag = $('#callbackTrigger').attr('checked');
//                zTree.removeNode(treeNode, callbackFlag);
            	$.ajax({
            		type: "POST",
                    url: "/equipmentClassification/deleteEquipmentClassification.do",
                    data: {equipmentClassificationId : treeNode.id},
                    dataType: "json",
                    success: function(data){
                    	$("#equipmentClassificationIndex").html("");
                    	$("#equipmentClassificationName").val("");
                    	$("#equipmentClassificationPostscript").val("");
                    	$("#equipmentList").empty();
                    	zTree.removeNode(treeNode, null);
                    }
            	});
            }
            return false;
        };

        function beforeRename(treeId, treeNode, newName) {
            if (newName.length == 0) {
                alert("节点名称不能为空.");
                var zTree = $.fn.zTree.getZTreeObj("treeDemo");
                setTimeout(function(){zTree.editName(treeNode)}, 10);
                return false;
            }
            return true;
        }
        
        function clickTreeNodeFunction(){
        	var zTree = $.fn.zTree.getZTreeObj('treeDemo'),
            nodes = zTree.getSelectedNodes(),
            treeNode = nodes[0];
        	if (treeNode) {
        		if(treeNode.pId == null){
        			return;
        		}
        		targetNode = treeNode;
        		$("#equipmentClassificationIndex").html(treeNode.ranking);
        		$("#equipmentClassificationName").val(treeNode.name);
        		$("#equipmentClassificationPostscript").val(treeNode.postscript);
        		$("#updateEquipmentClassificationBtn").data("flag",treeNode.id); 
        		queryAndShowEquipment(treeNode.id);
        	}
        }
        //增加各种事件
        if(!isInitTree){
            $('.add-folder').on('click', {isParent:true}, add);
            $('.add-file').on('click', {isParent:false}, add);
            // $("#edit").bind("click", edit);
            $('.minus-file').on('click', remove);
            isInitTree = !isInitTree;
        }
        $.fn.zTree.init($('#treeDemo'), setting, zNodes);
        
    };

  //清空分类属性显示相应方法
    function clearPropertyClassificationView(){
    	$("#equipmentClassificationIndex").html("");
		$("#equipmentClassificationName").val("");
		$("#equipmentClassificationPostscript").val("");
    }
    
    //加载树
    
    function loadAndShowTree(){
    	$.ajax({
    		type: "POST",
            url: "/equipmentClassification/queryEquipmentClassification.do",
            data: {},
            dataType: "json",
            success: function(data){
            	treeData = data;
            	clearPropertyClassificationView();
            	qcdj.treeview(treeData);
            	rebuildTree();
            }
    	}); 
    }
    
    /**
     * @func dialog
     * @desc 模拟弹窗
     * @param {jQuery obj} jQuery
     * @example
     * qcdj.dialog(jQuery)
     */

    qcdj.dialog = function(obj){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
        obj.find('.pop-btn span').click(function(){
            $(this).closest('.pop-wrap').hide();
            $('.bg-dialog').hide();
        });
        $('body').scrollTop(0);
    };

    //公共变量
    var $checkedItem = null;
    var queryCatch;
    var isInit = true;
    var pageSize = 10;
    //页面初始查询
//    queryAndShowEquipment();
    //查询equipment方法
    function queryAndShowEquipment(equipmentClassificationId,pageNo){
    	var queryObj = {};
    	queryObj.equipmentClassificationId = equipmentClassificationId;
    	queryObj.pageNo = pageNo==null?1:pageNo;
    	queryObj.pageSize = pageSize;
    	$.ajax({
    		type: "POST",
            url: "/equipment/queryEquipment.do",
            data: queryObj,
            dataType: "json",
            success: function(data){
            	queryCatch = data.datas;
            	$('#pageForView').jqPaginator({
      	            totalPages: Math.ceil(data.pagejson.total/pageSize) == 0?1:Math.ceil(data.pagejson.total/pageSize),//总页数  
      	            visiblePages: 10,//分多少页
      	            currentPage: parseInt(data.pagejson.pageNo),//当前页数
      	            first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
      	            prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
      	            next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
      	            last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
      	            page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
      	            onPageChange: function (n) { //回调函数
      	            	//点击页码的查询
//      	            	alert('当前第' + n + '页'); 
      	            	if(isInit){
      	            		queryAndShowEquipment($("#updateEquipmentClassificationBtn").data("flag"),n);
      	            		isInit = false;
      	            	}else{
      	            		isInit = true; 
      	            	}
      	            }
      	        });
            	$("#equipmentList").empty();
            	for(var i=0;i<queryCatch.length;i++){
            		var p = queryCatch[i];
            		var htmlStr = "<tr flag='" + p.equipmentId + "'><td><label><!--<input type='checkbox'/> -->" + (i+1) + "</label></td><td>" + p.equipmentName +
            					"</td><td>" + p.equipmentOrgin +"</td><td>" + p.equipmentBrand + "</td><td>" + p.equipmentSpecifications + "</td>" +
            					"<td>" + p.equipmentUserName + "</td><td><a href='#' class='icon-view' ></a>|<a href='#' class='icon-edit'></a>|" +
            					"<a href='#' class='icon-del'></a></td></tr>";
            		$("#equipmentList").append(htmlStr);
            	}
            }
    	}); 
    }
    
    
    
    
    
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * jiangCheng.init()
     */
    qcdj.init = function(){
//=============================================init方法开始===================================



        
        //treeview 数据格式
//        var zNodes =[
//            { id:1, pId:0, name:"父节点1 - 展开", open:true},
//            { id:11, pId:1, name:"父节点11 - 折叠"},
//            { id:111, pId:11, name:"叶子节点111",flag:"我就是flag"},
//            { id:112, pId:11, name:"叶子节点112",flag:"我就是flag"},
//            { id:113, pId:11, name:"叶子节点113",flag:"我就是flag"},
//            { id:114, pId:11, name:"叶子节点114",flag:"我就是flag"},
//            { id:12, pId:1, name:"父节点12 - 折叠",flag:"我就是flag"},  
//            { id:121, pId:12, name:"叶子节点121",flag:"我就是flag"},
//            { id:122, pId:12, name:"叶子节点122"},
//            { id:123, pId:12, name:"叶子节点123"},
//            { id:124, pId:12, name:"叶子节点124"}, 
//            { id:13, pId:1, name:"父节点13 - 没有子节点", isParent:true},
//            { id:2, pId:0, name:"父节点2 - 折叠"},
//            { id:21, pId:2, name:"父节点21 - 展开", open:true},
//            { id:211, pId:21, name:"叶子节点211"},
//            { id:212, pId:21, name:"叶子节点212"},
//            { id:213, pId:21, name:"叶子节点213"},
//            { id:214, pId:21, name:"叶子节点214"},
//            { id:22, pId:2, name:"父节点22 - 折叠"},
//            { id:221, pId:22, name:"叶子节点221"},
//            { id:222, pId:22, name:"叶子节点222"},
//            { id:223, pId:22, name:"叶子节点223"},
//            { id:224, pId:22, name:"叶子节点224"},
//            { id:23, pId:2, name:"父节点23 - 折叠"},
//            { id:231, pId:23, name:"叶子节点231"},
//            { id:232, pId:23, name:"叶子节点232"},
//            { id:233, pId:23, name:"叶子节点233"},
//            { id:234, pId:23, name:"叶子节点234"},
//            { id:3, pId:0, name:"父节点3 - 没有子节点", isParent:true}
//        ];
       //加载树
        loadAndShowTree();
        //update分类信息确认按钮绑定事件
        $("#updateEquipmentClassificationBtn").click(function(){
        	if($("#equipmentClassificationIndex").html() == ""){
        		alert("请先选择一个分类！");
        		return;
        	}
        	var updateObj = {};
        	updateObj.equipmentClassificationName = $("#equipmentClassificationName").val();
        	updateObj.equipmentClassificationPostscript = $("#equipmentClassificationPostscript").val();
        	updateObj.equipmentClassificationId = $(this).data("flag"); 
        	$.ajax({
        		type: "POST",
                url: "/equipmentClassification/updateEquipmentClassification.do",
                data: updateObj,
                dataType: "json", 
                success: function(data){
                	if(data.code == "200"){
                		loadAndShowTree();
                		alert("保存成功！");
                	}else{
                		alert("保存失败！");
                	}
                }
        	});
        });
        //新增
        $('#addEquipmentBtn').on('click',function(){
        	$("#addEquipmentNumber").val("");
        	$("#addEquipmentName").val("");
        	$("#addEquipmentSpecifications").val("");
        	$("#addEquipmentOrgin").val("");
        	$("#addEquipmentBrand").val("");
        	$("#addEquipmentUserName").val("");
            qcdj.dialog($('#addQicaiId'));
        });
        $("#addEquipmentConfirmBtn").click(function(){
        	var addEquipmentObj = {};
        	addEquipmentObj.equipmentNumber = $("#addEquipmentNumber").val();
        	addEquipmentObj.equipmentName = $("#addEquipmentName").val();
        	addEquipmentObj.equipmentSpecifications = $("#addEquipmentSpecifications").val();
        	addEquipmentObj.equipmentOrgin = $("#addEquipmentOrgin").val();
        	addEquipmentObj.equipmentBrand = $("#addEquipmentBrand").val();
        	addEquipmentObj.equipmentClassificationId = $("#updateEquipmentClassificationBtn").data("flag");
        	addEquipmentObj.equipmentUserName = $("#addEquipmentUserName").val();
        	$.ajax({
        		type: "POST",
                url: "/equipment/addEquipment.do",
                data: addEquipmentObj,
                dataType: "json",
                success: function(data){
                	queryAndShowEquipment($("#updateEquipmentClassificationBtn").data("flag")); 
                }
        	}); 
        });
        
        //查看
        $("#equipmentList").delegate('.icon-view','click',function(){
        	var targetObj;
        	for(var i=0;i<queryCatch.length;i++){
        		if($(this).parent().parent().attr("flag") == queryCatch[i].equipmentId){
        			targetObj = queryCatch[i];
        		}
        	}
        	$("#viewEquipmentNumber").val(targetObj.equipmentNumber);
        	$("#viewEquipmentName").val(targetObj.equipmentName);
        	$("#viewEquipmentSpecifications").val(targetObj.equipmentSpecifications);
        	$("#viewEquipmentOrgin").val(targetObj.equipmentOrgin);
        	$("#viewEquipmentBrand").val(targetObj.equipmentBrand);
        	$("#viewEquipmentUserName").val(targetObj.equipmentUserName);
        	qcdj.dialog($('#viewQicaiId'));
        });
        //编辑
        $("#equipmentList").delegate('.icon-edit','click',function(){
        	var targetObj;
        	for(var i=0;i<queryCatch.length;i++){
        		if($(this).parent().parent().attr("flag") == queryCatch[i].equipmentId){
        			targetObj = queryCatch[i];
        		}
        	}
        	$("#editConfirmBtn").data("flag",targetObj.equipmentId);
        	$("#editEquipmentNumber").val(targetObj.equipmentNumber);
        	$("#editEquipmentName").val(targetObj.equipmentName);
        	$("#editEquipmentSpecifications").val(targetObj.equipmentSpecifications);
        	$("#editEquipmentOrgin").val(targetObj.equipmentOrgin);
        	$("#editEquipmentBrand").val(targetObj.equipmentBrand);
        	$("#editEquipmentUserName").val(targetObj.equipmentUserName);
        	qcdj.dialog($('#editQicaiId'));
        });
        //删除
        $("#equipmentList").delegate('.icon-del','click',function(){
        	if(confirm("是否确认删除？")){
        		$.ajax({
            		type: "POST",
                    url: "/equipment/delEquipment.do",
                    data: {equipmentId : $(this).parent().parent().attr("flag")},
                    dataType: "json",
                    success: function(data){
                    	queryAndShowEquipment($("#updateEquipmentClassificationBtn").data("flag")); 
                    }
            	});
        	}
        });
        
        Common.tab('click',$('.tab-head-new'))

        $('#impId').on('click',function(){
            qcdj.dialog($('#impgzId'));
        });
         
        //绑定编辑equipment确定按钮方法
        $("#editConfirmBtn").click(function(){ 
        	var editObj = {};
        	editObj.equipmentId = $("#editConfirmBtn").data("flag");
        	editObj.equipmentNumber = $("#editEquipmentNumber").val();
        	editObj.equipmentName = $("#editEquipmentName").val();
        	editObj.equipmentSpecifications = $("#editEquipmentSpecifications").val();
        	editObj.equipmentOrgin = $("#editEquipmentOrgin").val();
        	editObj.equipmentBrand = $("#editEquipmentBrand").val();
        	editObj.equipmentUserName = $("#editEquipmentUserName").val();
        	$.ajax({
        		type: "POST",
                url: "/equipment/editEquipment.do",
                data: editObj,
                dataType: "json", 
                success: function(data){
                	queryAndShowEquipment($("#updateEquipmentClassificationBtn").data("flag")); 
                }
        	}); 
        });
   
        
      //导出模板
        $("#exportModelBtn").click(function(){
        	$("#exportModelForm").submit();
        });
        //导入按钮弹窗
        $("#showImportWindowBtn").click(function(){
        	$("#uploadFile").val("");
        	qcdj.dialog($('#importWindow'));
        });
        //导入按钮导入方法开始
        $("#beginImportBtn").click(function(){
        	if($("#updateEquipmentClassificationBtn").data("flag") == null){
        		alert("请先选择一个分类！");
        		return;
        	}
        	$.ajaxFileUpload({
                url: '/equipment/import.do', //用于文件上传的服务器端请求地址
                secureuri: false, //是否需要安全协议，一般设置为false
                fileElementId: 'uploadFile', //文件上传域的ID
                dataType: 'json', //返回值类型 一般设置为json
                param: {equipmentClassificationId : $("#updateEquipmentClassificationBtn").data("flag")},
                success: function (data, status)  //服务器成功响应处理函数
                {
                    if (data.code != 200) {
                        alert(data.message);
                        
                    } else {
                    	queryAndShowEquipment($("#updateEquipmentClassificationBtn").data("flag"));
                        alert("导入成功");
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    alert("导入失败");
                }
            });
        });

        
//============================================init方法结束=======================================
    };

    (function(){
       qcdj.init();
    })();




    module.exports = qcdj;
})
