/**
 * Created by guojing on 2015/8/7.
 */
define('curriculaI',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    require('jquery');
    require('doT');
    require('easing');
    var curriculaI = {},
        Common = require('common');

    var searchData ={};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * curriculaI.init()
     */
    curriculaI.init = function(){

        //

        //设置初始页码
        searchData.page = 1;
        //设置每页数据长度
        searchData.pageSize = 20;

        curriculaI.changeGrade();
        curriculaI.initTerm();

        $("#gradeId").change(function(){
            curriculaI.changeGrade();
        });
        /*$("#classId").change(function(){
        });*/

        curriculaI.initCurriculaData();

        $("#searchData").click(function(){
            searchData.page = 1;
            curriculaI.initCurriculaData();
        });

        $("#exportExcel").click(function(){
            curriculaI.exportExcel();
        });
        $("#exportExcel1").click(function(){
            curriculaI.exportExcel1();
        });

    };

    curriculaI.initTerm = function(){
        Common.getData('/myclass/getGradeAndCategory.do', {}, function(resp){
            var termList = resp.termList;
            $('#term').empty();
            var html = '';
            html += '<option value="-1">全部学期</option>';
            for(var i=termList.length-1; i>=0; i--){
                html += '<option value="'+termList[i].value+'">' + termList[i].name + '</option>';
            }
            $('#term').append(html);
        })
    }

    var option={};
    // 分页初始化
    curriculaI.initPaginator=function (option) {
        var totalPage = '';
        $('.page-paginator').show();
        $('.page-index').empty();
        if (option.total % option.pagesize == 0) {
            totalPage = option.total / option.pagesize;
        } else {
            totalPage = parseInt(option.total / option.pagesize) + 1;
        }
        curriculaI.buildPaginator(totalPage, option.currentpage);
        option.operate(totalPage);
    }
    curriculaI.buildPaginator =function (totalPage, currentPage) {
        if (totalPage > 5) {
            if (currentPage < 4) {
                for (var i = 1; i <= 5; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index').append('<i>···</i>');
            } else if (currentPage >= 4 && currentPage < (totalPage - 2)) {
                $('.page-index').append('<i>···</i>');
                for (var i = currentPage - 2; i <= currentPage + 2; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
                $('.page-index').append('<i>···</i>');
            } else {
                $('.page-index').append('<i>···</i>');
                for (var i = totalPage - 4; i <= totalPage; i++) {
                    if (i == currentPage) {
                        $('.page-index').append('<span class="active">' + i + '</span>');
                    } else {
                        $('.page-index').append('<span>' + i + '</span>');
                    }
                }
            }
        } else {
            for (var i = 1; i <= totalPage; i++) {
                if (i == currentPage) {
                    $('.page-index').append('<span class="active">' + i + '</span>');
                } else {
                    $('.page-index').append('<span>' + i + '</span>');
                }
            }
        }
    }

    curriculaI.changeGrade=function (){
        var selData = {};
        var gradeId= $("#gradeId").val();
        selData.gradeId=gradeId;
        //var gradeName=$("#gradeId").find("option:selected").text();
        Common.getPostData('/myschool/getClassValue.do', selData,function(rep){
                var list = rep.classList;
                var html='<option value="">全部班级</option>';
                $.each(list,function(i,item){
                    html+='<option value='+item.id+'>'+item.className+'</option>';
                });
                $("#classId").html(html);
            }
        );
    }

    curriculaI.initCurriculaData=function (){
        var stateFlag;
        if(searchData.gradeId == $('#gradeId').val()&&searchData.classId==$('#classId').val()){
            stateFlag=false;
        }else{
            stateFlag=true;
            searchData.gradeId=$('#gradeId').val();
            searchData.classId=$('#classId').val();
        }
        //查询参数
        searchData.schoolId=$('#schoolId').val();
        searchData.termType = $('#term').val();
        Common.getData('/myclass/studentInterestClassCount.do', searchData,function(rep){
            $('.sub-info-list').html("");
            Common.render({tmpl: $('#j-tmpl'), data: rep, context: '.sub-info-list'});

            option.total= rep.totalElements;
            option.pagesize= rep.size;
            option.currentpage=rep.number;
            if(stateFlag) {
                option.operate = function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).click(function () {
                            if (searchData.page != $(this).text()) {
                                searchData.page = $(this).text();
                                curriculaI.initCurriculaData();
                            }
                        });
                    });
                    $('.first-page').click(function () {
                        if (searchData.page != 1) {
                            searchData.page = 1;
                            curriculaI.initCurriculaData();
                        }
                    });
                    $('.last-page').click(function () {
                        if (searchData.page != totalPage) {
                            searchData.page = totalPage;
                            curriculaI.initCurriculaData();
                        }
                    });
                }
            }
            curriculaI.initPaginator(option);
        });
    }
    curriculaI.exportExcel=function(){
        var schoolId=$('#schoolId').val();
        var gradeId=$('#gradeId').val();
        var classId=$('#classId').val();
        var termType = $('#term').val();
        window.location.href="/myclass/exportStuInteClassExcel.do?schoolId="+schoolId+"&gradeId="+gradeId+"&classId="+classId+"&termType=" + termType;
    }
    curriculaI.exportExcel1=function(){
        var termType = $('#term').val();
        window.location.href="/myclass/exportInteClassStuExcel.do?termType=" + termType;
    }
    curriculaI.init();
    //module.exports=curriculaI;
});
