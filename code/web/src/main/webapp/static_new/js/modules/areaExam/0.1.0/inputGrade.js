/**
 * Created by hxl on 15/11/3.
 */
/**
 * @module  区域联考成绩录入操作
 * @description
 */

/* global Config */
define(['common',"ajaxfileupload","pagination"],function(require,exports,module){
    /**
     *初始化参数
     */
    var inputGrade = {},
        Common = require('common');
        require('ajaxfileupload');
        require('pagination');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }
    //公共变量
    var isInit=true;
    var PUBLIC_PAGE_SIZE = 20;
    
    /**
     * @func dialog
     * @desc 模拟弹窗
     * @param {jQuery obj} jQuery
     * @example
     * xcdj.dialog(jQuery)
     */

    inputGrade.dialog = function(obj){
        $('.bg-dialog').width($(document).width()).height($(document).height()).show();
        $('.pop-wrap').hide();
        obj.show();
        $('body').scrollTop(0);
    };
    
    function closeDialog(obj){
        obj.closest('.pop-wrap').hide();
        $('.bg-dialog').hide();
    }
    
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * roll.init()
     */
    inputGrade.init = function(){


        Common.tab('click',$('.grow-tab-head'))

        $('.gray-table').on('click','.edit-roll',function(){
        	inputGrade.dialog($('#editSchoolRoll'));
            return false;
        });

        $('.gray-table').on('click','.change-roll',function(){
        	inputGrade.dialog($('#changeSchoolRoll'));
            return false;
        });
        $('.gray-table').on('click','.look',function(){
        	inputGrade.dialog($('#status'));
            return false;
        });
        $('.gray-table').on('click','.deleteCon',function(){
        	inputGrade.dialog($('#delConExam'));
            return false;
        });
        $('.toEduDepart').on('click','.subTo',function(){
        	inputGrade.dialog($('#subStatus'));
            return false;
        });
        $('.toEduDepart').on('click','.inputScores',function(){
        	inputGrade.dialog($('#importExamScore'));
            return false;
        }); 
        //取消
        $('body').on('click','.close-dialog-class',function(){
        	closeDialog($(this));
        });
        
      //加载成绩列表        
      loadScoreList(1);
      function loadScoreList(pageNo){
    	      var areaExamId = $(".inputScores").attr("examId");
        	  $.ajax({
        		  type: "POST",
                  url: "/regional/scoreList.do",
                  data: {areaExamId:areaExamId, pageNo:pageNo,pageSize:PUBLIC_PAGE_SIZE},
                  dataType: "json",
                  success: function(data){
                  if(data.code != 200){
                	 alert("数据加载失败！");
                  }
                  //分页
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
        	            	loadScoreList(n);
        	            }else{ 
        	            	isInit = true; 
        	            	return;
        	            }
        	           }
        	        });
                  
                  var dataonelist = data.datas[0];
                  var subList = dataonelist.subList;
                  var title='<th>姓名</th><th width="80">班级</th><th>总分</br>('+dataonelist.titleFullScore+')</th>';
                  for(var n=0;n<subList.length;n++){
                	  title += '<th>'+subList[n].subName+'</br>('+subList[n].fullScore+')</th>';
                  } 
                  $('.th').empty();
                  $('.th').html(title);
                  var scoredata = data.datas;
                  $('#subScoreList').empty();//清空之前的
                  for(var j=0;j<scoredata.length;j++){
                	  var subList = scoredata[j].subList;
                   	  var str = '<tr><td>'+scoredata[j].studentName+'</td>'+'<td>'+scoredata[j].className+'</td>'+'<td>'+scoredata[j].scoreSum+'</td>';
                      var substr = '';
                      for(var i=0;i<subList.length;i++){
                       	substr += '<td>'+subList[i].subscore+'</td>';
                      }
                      str = str + substr+'<tr>';
                      $('#subScoreList').append(str);
                  } 
                  
                  },
                  error:function(){
                	  alert("数据加载失败！");
                  }
        	  });
       }
        
        /**
         * 以下是区域联考成绩导入 
         */
        	$(".toEduDepart").delegate("#inputBtn","click",function(){
        		$("#uploadExamItemScoreInput").val("");
        		$("#importExamItemScoreBtn").attr("areaExamId", $(this).closest("tr").attr("examId"));
        		//自己模拟弹窗
        		$("#bgbgbgbg").show();
//        		$("#importExamItemScoreWindow").show();
        		inputGrade.dialog($('#importExamItemScoreWindow'));
        	});
        	
        	//绑定导入窗口取消按钮
        	$("#cancelImportExamItenScore").click(function(){
        		$("#importExamItemScoreWindow").hide();
        	});
        	//绑定弹窗叉号按钮
        	$('body').on('click','.closePop',function(){
            	closeDialog($(this));
            });
        	//导出模板
        	$("#exportExamItemScoreModelBtn").click(function(){
        		$("#eId").val($(".inputScores").attr("examId"));
            	$("#exportExamItemScoreModelForm").submit();
            });
          	//导入按钮导入方法开始
            $("#importExamItemScoreBtn").click(function(){
            	if($("#uploadExamItemScoreInput").val() == null || $("#uploadExamItemScoreInput").val() == ""){
            		alert("请先选择要上传的文件！");
            		return;
            	}
            	$("#bgbgbgbg").hide();
        		$("#importExamItemScoreWindow").hide(); 
 //           	var examId = $(this).attr("examId");
            	//新增等待框
            	
            	Common.dialog($('#waitWindow_score'));
            	
//                Common.dialog($('#waitWindow_score'));
//                $('.bg-dialog').width($(document).width()).height($(document).height()).show(); 
//                document.getElementById("bgbgbgbg").style.display = "block";
            	$.ajaxFileUpload({
                    url: '/regional/import.do', //用于文件上传的服务器端请求地址
                    secureuri: false, //是否需要安全协议，一般设置为false
                    fileElementId: 'uploadExamItemScoreInput', //文件上传域的ID
                    dataType: 'json', //返回值类型 一般设置为json
                    param: {},
                    success: function (data, status)  //服务器成功响应处理函数
                    {
                        if (data.code != 200) {
                            $("#overWindowH_score").html("区域联考分数导入失败！");
                            $("#overWindowH_scoreMsg").html(data.message);
                            Common.dialog($('#overWindow_score'));
                        } else {
                        	$("#overWindowH_score").html("区域联考分数导入成功！");
                            Common.dialog($('#overWindow_score'));
                            inputSuccessStatus();
                        } 
                    },
                    error: function (data, status, e)//服务器响应失败处理函数
                    {
                    	 $("#overWindowH_score").html("区域联考分数导入失败！");
                    	 $("#overWindowH_scoreMsg").html(data.message);
                         Common.dialog($('#overWindow_score'));
                    }
                });
            });
          //导入成功确定按钮
            $(".pop-wrap").on("click","#overWindowBtn_score",function(){
            	location.reload();
            });
            
          //提交到教育局
            $('#subStatus').on('click','.active',function(){
            	  var jointExamId = $('.subTo').attr("jointExamId");
            	  
            	$("#bgbgbgbg").hide();
          		$("#importExamItemScoreWindow").hide(); 
   //           	var examId = $(this).attr("examId");
              	//新增等待框
              	
              	Common.dialog($('#waitWindow_submit_to_edu'));
            	  
            	  $.ajax({
            		  type: "POST",
                      url: "/regional/toEduDepart.do",
                      data: {id : jointExamId},
                      dataType: "json",
                      success: function(data){
                         if (data.code != 200) {
                             $("#overWindowH_score").html("提交成绩到教育局失败！");
                             $("#overWindowH_scoreMsg").html(data.message);
                             Common.dialog($('#overWindow_score'));
                          } else {
                             $("#overWindowH_score").html("提交成绩到教育局成功！");
                             Common.dialog($('#overWindow_score'));
                             location.reload();
                          } 
                      		
                      	//提交成功后刷新页面
                      	
                      },
                      error:function(){
                    	  alert("提交失败，请联系管理员！");
                      }
            	  });
            	 
            }); 
          //再次导入改为未提交
           function inputSuccessStatus(){
        	   var jointExamId = $('.subTo').attr("jointExamId");
         	  $.ajax({
         		  type: "POST",
                   url: "/regional/toEduDeparts.do",
                   data: {id : jointExamId},
                   dataType: "json",
                   success: function(data){
                   	if(data.code == 200){
                   		console.log("导入成功！未提交到教育局");
                   	}else{
                   		console.log("导入失败！状态未变");
                   	}
                   },
                   error:function(){
                 	  console.log("提交失败，请联系管理员！");
                   }
         	  });
           } 
    };

    (function(){
    	inputGrade.init();
    })();

    module.exports = inputGrade;
})

