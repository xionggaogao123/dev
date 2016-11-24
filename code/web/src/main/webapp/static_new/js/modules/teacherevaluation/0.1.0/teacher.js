/**
 * Created by fl on 2016/9/18.
 */
define(function(require,exports,module){
    var Common = require('common');

    $(function(){


        init();
        getTeacherStatementAndEvidence();

        $('#save').click(function(){
            saveStatement(true);
        })

        $('#saveAndPush').click(function(){
            getUnBeginEvaluations();
        })


        $("#push").click(function(){
            saveAndPushStatement()
        })
    })

    function getTeacherStatementAndEvidence(){
        Common.getDataAsync("/teacherevaluation/teacher/statementAndEvidence.do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                $('#statement').val(data.statement.split("<br/>").join('\n'));
                var evidence = data.evidence.split("<br/>").join('\n');
                $('#evidence').val(evidence == '' ? '暂无实证资料' : evidence);
            }
        })
    }

    function getUnBeginEvaluations(){
        Common.getDataAsync("/teacherevaluation/list/unbegin.do", {}, function(resp){
            if(resp.code == '200'){
                var data = resp.message;
                if(data.length > 0){
                    Common.render({
                        tmpl: '#itemsTmpl',
                        data: data,
                        context: '#items',
                        overwrite: 1
                    });

                    $(".bg").show();
                    $(".push-select").show();
                } else {
                    $(".bg").show();
                    $(".push-fail").show();
                    saveStatement(false);
                }
            }
        })
    }

    function saveStatement(showMsg){
        var statement = $.trim($('#statement').val());
        statement = statement.split("\n").join('<br/>');
        Common.postDataAsync("/teacherevaluation/teacher/updateStatement.do", {statement: statement}, function(resp){
            if(resp.code == '200' && showMsg){
                alert("保存成功")
            }
        })
    }

    function saveAndPushStatement(){
        var evaluationId = $("input[name='push-name']:checked").val();
        if(evaluationId == null){
            alert("请先选择要推送的考核")
            return false;
        }

        var statement = $('#statement').val();
        statement = statement.split("\n").join('<br/>');
        Common.postDataAsync("/teacherevaluation/teacher/updateAndPushStatement.do", {statement: statement, evaluationId: evaluationId}, function(resp){
            if(resp.code == '200'){
                $(".push-select").hide();
                $(".push-success").show();
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
    $(".btn-back").click(function(){
        $(".push-success").hide();
        $(".bg").hide();
    })
    $(".btn-know, .push-fail .alert-close").click(function(){
        $(".bg").hide();
        $(".push-fail").hide();
    })
    $(".btn-qx, .push-select .alert-close").click(function(){
        $(".bg").hide();
        $(".push-select").hide();
    })

}

