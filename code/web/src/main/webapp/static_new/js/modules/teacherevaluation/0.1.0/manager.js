/**
 * Created by fl on 2016/9/18.
 */
define(function(require,exports,module){
    var Common = require('common');
    require('pagination');
    var teacherId;
    var currentPage;

    $(function(){


        init();
        getEvaluationTeacherEntryBySchoolId(1)
        getUnBeginEvaluations()

        $('body').on('click', '.eva-edit, .push-edit', function(){
            teacherId = $(this).parents('tr').attr('tid');
            getTeacherEvidence();
        })

        $('#saveEvidence').click(function(){
            saveEvidence();
        })

        $('#evals').change(function(){
            getEvaluationTeacherEntryByEvaluationId()
        })

        $('#allCheck').click(function(){
            $('#teachers input:checkbox').prop('checked', $(this).is(':checked'))
            checkCheckbox()
        })

        $('body').on('change', '#teachers input:checkbox', function(){
            checkCheckbox()
        })

        $('body').on('click', '.push-push', function(){
            var tr = $(this).parents('tr');
            var teacherName = tr.find('.t-name').text();
            var msg = '确定推送  ' + teacherName + '  的实证资料到  ' + $('#evals option:selected').text();
            if(confirm(msg)){
                push(tr.attr('tid'))
            }
        })

        $('#allPush').click(function(){
            allPush()
        })
    })

    function getEvaluationTeacherEntryBySchoolId(page){
        currentPage = page;
        var isInit = true;
        var requestData = {
            page: page,
            pageSize: 30
        }
        Common.getDataAsync("/teacherevaluation/teacher/list.do", requestData, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                if(data.list.length > 0) {
                    //分页方法
                    $('.new-page-links').jqPaginator({
                        totalPages: Math.ceil(data.count / data.pageSize) == 0 ? 1 : Math.ceil(data.count / data.pageSize),//总页数
                        visiblePages: 10,//分多少页
                        currentPage: parseInt(page),//当前页数
                        first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                        prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                        next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                        last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                        page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                        onPageChange: function (n) { //回调函数
                            if (isInit) {
                                isInit = false;
                            } else {
                                getEvaluationTeacherEntryBySchoolId(n);
                            }
                        }
                    });
                }
                Common.render({
                    tmpl: '#itemsTmpl',
                    data: data.list,
                    context: '#items',
                    overwrite: 1
                });
            }
        })
    }

    function getTeacherEvidence(){
        Common.getDataAsync("/teacherevaluation/teacher/evidence.do", {teacherId: teacherId}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                $('#evidence').val(data.evidence.split("<br/>").join('\n'));
                $('#teacherName').text(data.teacherName);
                $(".bg").show();
                $(".szzl-edit").show();
            }
        })
    }

    function saveEvidence(){
        var evidence = $.trim($('#evidence').val());
        evidence = evidence.split("\n").join('<br/>');
        Common.postDataAsync("/teacherevaluation/teacher/updateEvidence.do", {evidence: evidence, teacherId: teacherId}, function(resp){
            if(resp.code == '200'){
                $(".bg").hide();
                $(".szzl-edit").hide();
                getEvaluationTeacherEntryBySchoolId(currentPage);
                getEvaluationTeacherEntryByEvaluationId();
                alert("保存成功")
            }
        })
    }

    function getUnBeginEvaluations(){
        Common.getDataAsync("/teacherevaluation/list/manager/unbegin.do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                Common.render({
                    tmpl: '#evalsTmpl',
                    data: data,
                    context: '#evals',
                    overwrite: 1
                });
            }
        })
    }

    function getEvaluationTeacherEntryByEvaluationId(){
        var evaluationId = $('#evals').val();
        if(evaluationId == 'none'){
            $('.push-main').hide();
        } else {
            Common.getDataAsync("/teacherevaluation/teacher/listByEva.do", {evaluationId: evaluationId}, function(resp){
                if(resp.code == '200'){
                    var data = resp.message;
                    Common.render({
                        tmpl: '#teachersTmpl',
                        data: data,
                        context: '#teachers',
                        overwrite: 1
                    });
                    $('.push-main').show();
                }
            })
        }
    }

    function checkCheckbox(){
        var checked = $("#teachers input:checkbox:checked");
        checked.length > 0 ?
            $('#allPush').addClass('green').removeClass('grey') :
            $('#allPush').addClass('grey').removeClass('green');
        return checked.map(function(index,ele) {
            return $(ele).parents('tr').attr('tid');
        }).get().join(',');
    }

    function allPush(){
        var teacherIds = checkCheckbox();
        if('' == teacherIds){
            alert('请至少勾选一个老师再批量推送')
        } else {
            var msg = '确定推送选中老师的实证资料到  ' + $('#evals option:selected').text();
            if(confirm(msg)){
                var evaluationId = $('#evals').val();
                Common.getDataAsync("/teacherevaluation/teacher/pushAllEvidence.do", {evaluationId: evaluationId, teacherIds: teacherIds}, function(resp){
                    if(resp.code == '200'){
                        alert('推送成功')
                    } else {
                        alert(resp.message)
                    }
                })
            }
        }
    }

    function push(teacherId){
        var evaluationId = $('#evals').val();
        Common.getDataAsync("/teacherevaluation/teacher/pushEvidence.do", {evaluationId: evaluationId, teacherId: teacherId}, function(resp){
            if(resp.code == '200'){
                alert('推送成功')
            }
        })

    }
});





function init(){
    $(".tab-head li").click(function(){
        $(".tab-head li").removeClass("cur");
        $(this).addClass("cur");
        $(".tab-main>div").hide();
        var name = $(this).attr("id");
        $("#" + "tab-" + name).show();
    })
    $(".push-push").click(function(){
        $(".bg").show();
        $(".push-sure").show();
    })
    $(".btn-qx,.alert-close,.btn-sure").click(function(){
        $(".bg").hide();
        $(".push-sure").hide();
    })
    $(".btn-qx,.alert-close,.btn-save").click(function(){
        $(".bg").hide();
        $(".szzl-edit").hide();
    })
    //$tr=$(".push-table").find("tr");
    $tr=$("body .push-table").find("tr");
    $tr.hover(function(){
        $tr.removeClass("tr-display");
        $(this).addClass("tr-display");
    },function(){
        $tr.removeClass("tr-display");
    })

}

