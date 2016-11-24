/**
 * Created by liwei on 15/9/06.
 */
/**
 * @author 李伟
 * @module  
 * @description
 * 成长档案详情
 */
/* global Config */
define(['common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var growthDetail = {},
        Common = require('common');

    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    }
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * rewardHistory.init()
     */
    growthDetail.init = function(){


//        Common.tab('click',$('.grow-tab-head'))
        
        var reportExamTable = $('#reportExamTable');
        var reportQualityTable = $('#reportQualityTable');
        var growthQualityTable = $('#growthQualityTable');
        
        var goodPerformanceArea = $('#goodPerformanceArea');
        var masterCommentArea = $('#masterCommentArea');
        
        var reportTermSelection = $('#reportTermSelection');
        var reportExamSelection = $('#reportExamSelection');
        var growthTermSelection = $('#growthTermSelection');
        //根据学期和userId查询并填充考试下拉
        function fillReportExamSelection(){
        	$.ajax({
        		type: "POST",
                url: "/growth/getExamListForDetail.do",
                data: {term : reportTermSelection.val() , studentId : PUBLIC_STUDENT_ID},
                dataType: "json",
                success: function(data){
                	if(data.code == 200){
                		var datas = data.message.examList;
                		PUBLIC_GROWTH_ID = data.message.growthId;
                		if(PUBLIC_GROWTH_ID == ""){
                			$('.report-txt').hide();
                		}else{
                			$('.report-txt').show();
                		}
                		reportExamSelection.html('');
                		if(datas.length > 0){
                    		for(var i=0;i<datas.length;i++){
                    			reportExamSelection.append('<option value="' + datas[i].id + '">' + datas[i].name + '</option>');
                    		}
                    		//这里需要根据新变化的考试进行查询
                    		fillExamTable();
                		}
                	}else{
                		alert("查询考试菜单失败 ！请联系管理员");
                	}
                	
                }
        	});
        }
        //根据所选学期查询素质教育部分成绩单并填充
        function fillQualityReportByTerm(){
        	reportQualityTable.empty();
        	$.ajax({
        		type: "POST",
                url: "/growth/getReportQualityList.do",
                data: {term : reportTermSelection.val() , studentId : PUBLIC_STUDENT_ID},
                dataType: "json",
                success: function(data){
                	if(data.code == 200){
                		goodPerformanceArea.val(data.message.goodPerformance);
                		masterCommentArea.val(data.message.mastComment);
                		gpCatch = data.message.goodPerformance;
                        mcCatch = data.message.goodPerformance;
                		var datas = data.message.scoreList;
                		if(datas.length == 0){
                			reportQualityTable.append('<tr><td colspan="4">该学期无素质教育数据</td></tr>');
                		}
                		for(var i=0;i<datas.length;i=i+2){
                			var trHtml;
                			if(i == datas.length - 1){
                    			trHtml = '			 	 <tr>'+
                    			'                            <td class="bg-td">' + datas[i].name + '</td>'+
                    			'                            <td>' + datas[i].score + '</td>'+
                    			'                            <td colspan="2"></td>'+
                    			'                        </tr>';
                			}else{
                    			trHtml = '			 	 <tr>'+
                    			'                            <td class="bg-td">' + datas[i].name + '</td>'+
                    			'                            <td>' + datas[i].score + '</td>'+
                    			'                            <td class="bg-td">' + datas[i+1].name + '</td>'+
                    			'                            <td>' + datas[i+1].score + '</td>' + 
                    			'						 </tr>';
                			}
                			reportQualityTable.append(trHtml);
                		}
                	}else{
                		alert("您查询的素质教育成绩还没有，请先确认是否录入了成绩");
                	}
                }
        	});
        }
        //填充考试成绩
        function fillExamTable(){
        	reportExamTable.empty();
        	$.ajax({
        		type: "POST",
                url: "/growth/getExamScoreList.do",
                data: {examId : reportExamSelection.val() , studentId : PUBLIC_STUDENT_ID},
                dataType: "json",
                success: function(data){
                	if(data.code == 200){
                		var datas = data.message;
                		if(datas.length == 0){
                			reportExamTable.append('<tr><td colspan="4">该次考试无成绩数据</td></tr>');
                		}
                		for(var i=0;i<datas.length;i=i+2){
                			var trHtml;
                			if(i == datas.length - 1){
                    			trHtml = '			 	 <tr>'+
                    			'                            <td class="bg-td">' + datas[i].name + '</td>'+
                    			'                            <td>' + datas[i].score + '</td>'+
                    			'                            <td colspan="2"></td>'+
                    			'                        </tr>';
                			}else{
                    			trHtml = '			 	 <tr>'+
                    			'                            <td class="bg-td">' + datas[i].name + '</td>'+
                    			'                            <td>' + datas[i].score + '</td>'+
                    			'                            <td class="bg-td">' + datas[i+1].name + '</td>'+
                    			'                            <td>' + datas[i+1].score + '</td>' + 
                    			'						 </tr>';
                			}
                			reportExamTable.append(trHtml);
                		}
                	}else{
                		alert("您查询的考试成绩还没有，请先确认是否创建考试并录入成绩");
                	}
                }
        	});
        }
        
        //更新两个文本框的内容
        function updateAreaContent(){
        	$.ajax({
        		type: "POST",
                url: "/growth/updateGpAndMc.do",
                data: {growthId : PUBLIC_GROWTH_ID,
                	   goodPerformance : goodPerformanceArea.val(),
                	   masterComment : masterCommentArea.val()
                	  },
                dataType: "json",
                success: function(data){
                	if(data.code == 200){
                		
                	}else{
                		alert("保存失败 ！请联系管理员");
                	}
                	
                }
        	});
        }
        
        
        //填充素质教育评价单内容
        function fillQualityTable(){
        	$.ajax({
        		type: "POST",
                url: "/growth/getQualityDatas.do",
                data: {studentId : PUBLIC_STUDENT_ID,
                	   term : growthTermSelection.val()
                	   },
                dataType: "json",
                success: function(data){
                	if(data.code == 200){
                		growthQualityTable.empty();
                		var datas = data.message;
                		if(datas == ""){
                			growthQualityTable.html('<tr><td rowspan="6" colspan="6" class="rowspan">该学期无素质教育数据</td></tr>');
                		}else{
                			var levelList = datas.levelList;
                			var levelHtml = '<option value="" selected>-请选择-</option>';
                			for(var i=0;i<levelList.length;i++){
                				levelHtml = levelHtml + '<option value="' + levelList[i] + '"  selected' + levelList[i] + '>' + levelList[i] + '</option>';
                			}
                			var quaList = datas.qualityList;
                			for(var i=0;i<quaList.length;i++){
                				var quaObj = quaList[i];
                				var subList = quaObj.subList;
                				var htmlStr;
                				//拼父行
                				htmlStr = '    <tr>'+
                				'                     <td rowspan="' + (subList.length==0?1:subList.length) + '" class="rowspan">' + quaObj.name + '</td>';
                				
                				if(subList.length==0){
                					htmlStr = htmlStr + '<td colspan="4">该素质评价无子项目</td>';
                				}else{
                					htmlStr = htmlStr + ' <td>' + subList[0].name + '</td>'+
                    				'                     <td class="flushLeft">' + subList[0].requirement + '</td>';
                					if(isMyself){
                						htmlStr = htmlStr + '<td>'
                										  + '<select flag="' + (i + "") + '-0-1" value="' + subList[0].slv + '">'
                										  + levelHtml.replace('selected' + subList[0].slv,'selected="selected"')
                										  + '</select>'
                										  + '</td>';
                    				}else{
                    					htmlStr = htmlStr + '<td>' + subList[0].slv + '</td>';
                    				}
                					if(isMaster){
                						htmlStr = htmlStr + '<td >'  
				                						  + '<select flag="' + (i + "") + '-0-2" value="' + subList[0].tlv + '">'
//														  + $('[value="' + subList[0].tlv + '"]', $(levelHtml).clone()).attr('selected','selected').html()
														  +  levelHtml.replace('selected' + subList[0].tlv,'selected="selected"')
														  + '</select>'
										  				  + '</td>';
                					}else{
                						htmlStr = htmlStr + '<td>' + subList[0].tlv + '</td>';
                					}
                				}
                				if(isMaster){
                					htmlStr = htmlStr + '<td rowspan="' + (subList.length==0?1:subList.length) + '">'  
              					  	  + 					'<select flag="' + (i + "") + '-0-0" value="' + quaObj.level + '">' 
									  + 					levelHtml.replace('selected' + quaObj.level,'selected="selected"')  
									  + 					'</select>' 
									  + 				'</td>';
                				}else{
                					htmlStr = htmlStr +'   <td rowspan="' + (subList.length==0?1:subList.length) + '" >' + quaObj.level + ' </td>';
                				}
                				htmlStr = htmlStr + '</tr>';
                				//拼子行
                				for(var j=1;j<subList.length;j++){
                					htmlStr = htmlStr + '    <tr>'+
                					'                            <td>' + subList[j].name + '</td>'+
                					'                            <td class="flushLeft">' + subList[j].requirement + '</td>';
                					if(isMyself){
                						htmlStr = htmlStr + '<td>'
                										  + '<select flag="' + (i + "") + '-' + (j + "") + '-1" value="' + subList[j].slv + '">'
                										  + levelHtml.replace('selected' + subList[j].slv,'selected="selected"')
                										  + '</select>'
                										  + '</td>';
                    				}else{
                    					htmlStr = htmlStr + '<td>' + subList[j].slv + '</td>';
                    				}
                					if(isMaster){
                						htmlStr = htmlStr + '<td >'  
				                						  + '<select flag="' + (i + "") + '-' + (j + "") + '-2" value="' + subList[j].tlv + '">'
														  + levelHtml.replace('selected' + subList[j].tlv,'selected="selected"')
														  + '</select>'
										  				  + '</td>';
                					}else{
                						htmlStr = htmlStr + '<td>' + subList[j].tlv + '</td>';
                					}
                					htmlStr = htmlStr + '</tr>';
                				}
                				growthQualityTable.append(htmlStr);
                			}
                			//绑定素质教育成绩单中的下拉框change事件，用于保存
                	        $('#growthQualityTable select').change(function(){
                	        	if($(this).val() == ""){
                	        		return;
                	        	}
                	        	$.ajax({
                	        		type: "POST",
                	                url: "/growth/updateQualityLevel.do",
                	                data: {term : growthTermSelection.val(),
                	                	   studentId : PUBLIC_STUDENT_ID,
                	                	   coordinate : $(this).attr('flag'),
                	                	   level : $(this).val()
                	                	  },
                	                dataType: "json",
                	                success: function(data){
                	                	if(data.code == 200){
                	                		
                	                	}else{
                	                		alert("保存失败 ！请联系管理员");
                	                	}
                	                	
                	                }
                	        	});
                	        });
                			
                		}
                	}else{
                		alert("读取素质教育失败 ！请联系管理员");
                	}
                	
                }
        	});
        }
        
        
        
        
        //绑定下拉菜单change事件
        reportTermSelection.change(function(){
        	fillQualityReportByTerm();
        	fillReportExamSelection();
        });
        reportExamSelection.change(function(){
        	fillExamTable();
        });
        growthTermSelection.change(function(){
        	fillQualityTable();
        });
        
        //绑定优秀表现和评语change事件
        var gpCatch;
        var mcCatch;
        goodPerformanceArea.focus(function(){
        	gpCatch = goodPerformanceArea.val();
        });
        masterCommentArea.focus(function(){
        	mcCatch = masterCommentArea.val();
        });
        goodPerformanceArea.blur(function(){
        	if(goodPerformanceArea.val() != gpCatch){
        		updateAreaContent();
        	}
        });
        masterCommentArea.blur(function(){
        	if(masterCommentArea.val() != mcCatch){
        		updateAreaContent();
        	}
        });
        
        
        //最后，在此启动
        fillReportExamSelection();
        fillQualityReportByTerm();
        fillQualityTable();
        
        
    };

    (function(){
    	growthDetail.init();
    })();

    module.exports = growthDetail;
})