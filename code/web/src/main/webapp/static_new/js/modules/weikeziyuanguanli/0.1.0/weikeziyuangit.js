/**
 * Created by liwei on 15/9/06.
 */
/**
 * @author 李伟
 * @module  学籍管理操作
 * @description
 * 学籍管理操作
 */
/* global Config */
define('weikeziyuangit',['common','pagination'],function(require,exports,module){
    /**
     *初始化参数
     */
    var weikeziyuangit = {},
        Common = require('common');
    			require('pagination');
    Array.prototype.remove = function(dx){
        if(isNaN(dx)||dx>this.length){return false;}
        this.splice(dx,1);
    };

	var $termType,$subject,$grade,$type;
    
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * weikeziyuangit.init()
     */
    weikeziyuangit.init = function(){



		$termType=$("#termTypeSel");
		$subject = $('#subjectSel');
		$grade=$('#gradeSel');
		$type = $('#typeSel');

		var typeInt=$termType.attr("typeInt");

		weikeziyuangit.queryAndFill(typeInt);
		$termType.change(function(){
			typeInt=$termType.attr("typeInt");
			weikeziyuangit.queryAndFill(typeInt);
		});

		$subject.change(function(){
			typeInt=$subject.attr("typeInt");
			weikeziyuangit.queryAndFill(typeInt);
		});


		//初始查询
		weikeziyuangit.queryList(1);

        /**
         * 查询填充列表相关
         */
		$(".search-btn").click(function(){
			weikeziyuangit.queryList(1);
		});


		//绑定添加至资源库按钮的跳转方法
		$('body').on('click','.edit-page-btn',function(){
			var id = $(this).closest("tr").attr("id");
			var videoId = $(this).closest("tr").attr("videoId");
			location.href = "/cloud/editCloudLesson.do?id=" + id + "&videoId=" + videoId;
		});

      	//资源管理的查看
        playerLinks.init('.show-me-btn',function(e){
			var id = e.closest("tr").attr("id");
			var videoId = e.closest("tr").attr("videoId"),url;
        	$.ajax({
        		async:false,
        		type: "POST",
                url: "/cloud/getCloudLesson.do",
                data: {id : id , videoId : videoId},
                dataType: "json",
                success: function(data){
                	if(data.dto){
						url = data.dto.path;
                	}else{
						alert("获取课件地址出错，请联系管理员！");
						return;
					}
                }
        	});
        	return url;
        });
    };

	//查询填充上方的东西
	weikeziyuangit.queryAndFill = function(typeInt){

		var selData={};
        var html="<option value=''>全部</option>";
		var schoolType = $termType.val();
		var subjectId = $subject.val()|| null;
		if (subjectId == 53 || subjectId == 47 || subjectId == 51 || subjectId == 52) {
			$grade.html(html);
		}
		if(typeInt==1){
			$subject.html(html);
		}
		if(typeInt==1||typeInt==2){
			$grade.html(html);
			$type.html(html);
			selData.schoolType=schoolType;
			selData.subject=subjectId;
			Common.getPostData('/cloud/infos1.do', selData,function(data){
				if(typeInt==1){
					var subjectList = data.subject;
					for(var i=0;i<subjectList.length;i++){
						var subObj = subjectList[i];
						$subject.append("<option value='"+ subObj.id +"'>" + subObj.value +"</option>");
					}
				}

				var gradeList = data.grade;
				if (gradeList.length >0) {
					for (var i = 0; i < gradeList.length; i++) {
						var subObj = gradeList[i];
						$grade.append("<option value='"+ subObj.id +"'>" + subObj.value +"</option>");
					}
				}

				var pointList = data.type;
				if (pointList.length > 0) {
					for (var i = 0; i < pointList.length; i++) {
						var subObj = pointList[i];
						$type.append("<option value='"+ subObj.id +"'>" + subObj.name +"</option>");
					}
				}
			});
		}
	};


	var searchData={};
	searchData.pageSize=12;
	weikeziyuangit.queryList = function(pageNo){
		searchData.schoolType = $termType.val();
		var schoolTypeName=$termType.find("option:selected").text();
		searchData.isSaved=1;
		searchData.grade = $grade.val();
		searchData.subject = $subject.val();
		searchData.classTypeId = $type.val();
		searchData.searchName = "";
		searchData.pageNo = pageNo;
		Common.getPostData('/cloud/queryCloudList.do', searchData,function(data){
			var courseList = data.datas;
			var list=courseList.list;
			if(list.length == 0){
				$("#coursewareList").html('');
				$('.new-page-links').html('');
				return;
			}
			$('.new-page-links').jqPaginator({
				totalPages: Math.ceil(courseList.count/searchData.pageSize) == 0?1:Math.ceil(courseList.count/searchData.pageSize),//总页数
				visiblePages: 10,//分多少页
				currentPage: parseInt(data.page),//当前页数
				first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
				prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
				next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
				last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
				page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
				onPageChange: function (n) { //回调函数
					//点击页码的查询
					//alert('当前第' + n + '页');
					if(searchData.pageNo!=n){
						weikeziyuangit.queryList(n);
					}
				}
			});

			$("#coursewareList").empty();
			for(var i=0;i<list.length;i++){
				var course = list[i];
				var fromStr = course.from;
				if(fromStr == "资源板块前端"){
					fromStr = "用户上传";
				}
				if(fromStr == "资源板块后端"){
					fromStr = "管理员上传";
				}

				var termSubject="";
				if(schoolTypeName=="全部"){
					schoolTypeName=course.schoolTypeName;
				}
				if(schoolTypeName=="全部"||schoolTypeName==""){
					termSubject=course.subjectName;
				}else{
					termSubject=schoolTypeName + '/' + course.subjectName;
				}
				var htmlStr = '  <tr id="' + course.id + '" videoId="' + course.videoId + '">'+
					'                    <td>'+
					'                        <img src="' + course.imageUrl + '"/>'+
					'                        <em>' + course.name + '</em>'+
					'                    </td>'+
					'                    <td>' + course.uploadTime + '</td>'+
					'                    <td>' + termSubject+'</td>'+
					'                    <td>' + course.typeName+ '</td>'+
					'                    <td>'+
					'                        <span>创建人：' + course.createrName + '</span>'+
					'                        <span>来源：' + fromStr + '</span>'+
					'                    </td>'+
					'                    <td>'+
					'                        <a href="' + course.name + '.m3u8" class="zy-orange-sm-btn show-me-btn">查看</a>'+
					'                        <a href="javascript:;" class="zy-orange-sm-btn edit-page-btn">编辑</a>'+
					'                        <a href="javascript:;" class="zy-gray-sm-btn del-btn">删除</a>'+
					'                    </td>'+
					'                </tr>';
				$("#coursewareList").append(htmlStr);
			}

			//删除方法绑定
			$(".del-btn").click(function(){
				if(!confirm("删除后不能恢复，是否确认？")){
					return;
				}
				var delData={};
				delData.id = $(this).closest("tr").attr("id");
				delData.videoId = $(this).closest("tr").attr("videoId");
				Common.getPostData('/cloud/delCloudLesson.do', delData,function(data){
					alert("操作完成!");
					weikeziyuangit.queryList(searchData.pageNo);
				});
			});
		});

	}

    module.exports = weikeziyuangit;
})