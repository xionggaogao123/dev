/**
 * Created by wangkaidong on 2016/5/4.
 */
define(function(require, exports, module){
    require("jquery");
    require("doT");
    require('pagination');
    var Common = require('common');
    var pageSize = 10;
    var classList = {};


    classList.init = function(){


        Common.getData('/paike/getAllYear.do',{},function(data){//学期列表
            Common.render({
                tmpl:'#termTmpl',
                data:data,
                context:'#termCtx'
            });
        });

        Common.getData('/course/getGradeList.do',{},function(data){//年级列表
            Common.render({
                tmpl:'#gradeTmpl',
                data:data,
                context:'#gradeCtx'
            });
        });

        getSubjectList();
        getCourseList(1);
    }

    $(document).ready(function(){
        $('body').on('change','#termCtx',function(){
            getSubjectList();
            $('#subjectCtx').trigger('change');
        });
        $('body').on('change','#gradeCtx',function(){
            getSubjectList();
            $('#subjectCtx').trigger('change');
        });
        $('body').on('change','#courseType',function(){
            getSubjectList();
            $('#subjectCtx').trigger('change');
        });
        $('body').on('change','#subjectCtx',function(){
            getCourseList(1);
        });

    });


    function getSubjectList(){//学科列表
        var param = {};
        param.term = $('#termCtx').val();
        param.gradeId = $('#gradeCtx').val();

        if($('#courseType').val() == 6){
            var url = '/paike/findInterestClassList.do';
            Common.getData(url,param,function(data){
                var subjectList = [];
                if(data.length > 0){
                    for(var i = 0;i < data.length;i++){
                        var subject = {};
                        subject.subjectId = data[i].subjectId;
                        subject.subjectName = data[i].courseName;
                        subjectList.push(subject);
                    }
                }
                Common.render({
                    tmpl:'#subjectTmpl',
                    data:subjectList,
                    context:'#subjectCtx',
                    overwrite:1
                });
            });
        }else{
            var url = '/zouban/findXuanKeConf.do';
            param.type = $('#courseType').val();
            Common.getData(url,param,function(data){
                Common.render({
                    tmpl:'#subjectTmpl',
                    data:data.subConfList,
                    context:'#subjectCtx',
                    overwrite:1
                });
            });
        }

    }

    function getCourseList(n){
        var url = '/attendance/courseList.do';
        var param = {};
        param.term = $('#termCtx').val();
        param.grade = $('#gradeCtx').val();
        param.courseType = $('#courseType').val();
        if(param.courseType == 6){
            param.subject = $('#subjectCtx option:selected').text();
        }else{
            param.subject = $('#subjectCtx').val();
        }
        param.page = n;
        param.pageSize = pageSize;
        Common.getData(url,param,function(data){
            pagination(data,true);
            if(data.courseList){
                Common.render({
                    tmpl:'#courseListTmpl',
                    data:data.courseList,
                    context:'#courseListCtx',
                    overwrite:1
                });
            }else{
                $('#courseListCtx').empty();
            }
        });
    }

    function pagination(data,isInit){
        $('.new-page-links').html("");
        var courseList = data.courseList;
        if(courseList && courseList.length > 0) {
            $('.new-page-links').jqPaginator({
                totalPages: Math.ceil(data.count / pageSize) == 0 ? 1 : Math.ceil(data.count / pageSize),//总页数
                visiblePages: 6,//分多少页
                currentPage: parseInt(data.page),//当前页数
                first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                onPageChange: function (n) { //回调函数
                    if (isInit) {
                        isInit = false;
                    } else {
                        getCourseList(n);
                        $('body,html').animate({scrollTop: 0}, 20);
                    }
                }
            });
        }
    }


    toLessonListPage = function (id,courseName,className,teacherName,studentsCount){//考勤
        var url = '/attendance/lesson.do';
        var param = {};
        param.id = id;
        param.className = className;
        param.teacherName = teacherName;
        param.studentsCount = studentsCount;
        param.term = $('#termCtx option:selected').text();
        param.grade = $('#gradeCtx option:selected').text();

        window.location.href = url+'?index=7&version=5&id='+param.id+'&className='+param.className+'&teacherName='+teacherName
            +'&studentsCount='+param.studentsCount+'&term='+param.term+'&grade='+param.grade;
    }

    xunshiScore = function (e,id,courseName,className,teacherName,studentsCount){//巡视打分
        //阻止父元素点击事件
        if(e && e.stopPropagation){//非ie
            e.stopPropagation();
        }else{//ie
            window.event.cancelBubble = true;
        }

        var url = '/attendance/xunshiScore.do';
        var param = {};
        param.id = id;
        param.className = className;
        param.teacherName = teacherName;
        param.studentsCount = studentsCount;
        param.term = $('#termCtx option:selected').text();
        param.grade = $('#gradeCtx option:selected').text();

        window.location.href = url+'?index=7&version=5&id='+param.id+'&className='+param.className+'&teacherName='+teacherName
        +'&studentsCount='+param.studentsCount+'&term='+param.term+'&grade='+param.grade;
    }


    module.exports = classList;
});