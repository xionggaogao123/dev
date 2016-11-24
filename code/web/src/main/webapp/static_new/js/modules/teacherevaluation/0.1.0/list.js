/**
 * Created by fl on 2016/8/1.
 */
define(function(require,exports,module){

    var Common = require('common');
    var GLOBAL = {
        evid : ''
    };

    $(function(){



        getEvaluations();

        $('body').on('click', '#evaluations #config', function(){
            var evid = $(this).parents('li').attr('evid');
            window.open("/teacherevaluation/" + evid + "/config.do");
        })

        $('body').on('click', '#evaluations #item', function(){
            var evid = $(this).parents('li').attr('evid');
            window.open("/teacherevaluation/" + evid + "/items.do");
        })

        $('body').on('click', '#evaluations #edit', function(){
            var li = $(this).parents('li');
            GLOBAL.evid = li.attr('evid');
            showWind();
            $('#evaluation-name').val(li.find('.eva-name').text());
        })

        $('body').on('click', '#evaluations #delete', function(){
            if(confirm("确认删除！")){
                var evid = $(this).parents('li').attr('evid');
                deleteEvaluation(evid);
            }
        })

        $('body').on('click', '#evaluations #sign', function(){
            if($(this).hasClass('grey')){
                return;
            }
            var msg = $(this).text();
            if(confirm("确认" + msg + "！")){
                var evid = $(this).parents('li').attr('evid');
                sign(evid, $(this));
            }
        })

        $('#save').click(function(){
            if(GLOBAL.evid == ''){
                addEvaluation();
            } else {
                updateEvaluationName()
            }
        })

        $('#year').change(function(){
            getEvaluations();
        })

    })

    function sign(evid, node){
        Common.postDataAsync("/teacherevaluation/" + evid + "/sign.do", {}, function(resp){
            if(resp.code == '500'){
                alert('操作失败 ：' + resp.message);
            } else {
                var state = Number(resp.message);
                if(state == -1){
                    node.text('报名');
                } else {
                    node.text('取消报名');
                }
            }
        })
    }

    function deleteEvaluation(evid){
        Common.postDataAsync("/teacherevaluation/" + evid + "/deletion.do", {}, function(resp){
            if(resp.code == '200'){
                alert('删除成功');
                getEvaluations();
            } else {
                alert('删除失败');
            }
        })
    }

    function updateEvaluationName(){
        var name = $.trim($('#evaluation-name').val());
        if(name == ''){
            alert("评比名称不能为空");
            return false;
        }
        Common.postDataAsync("/teacherevaluation/" + GLOBAL.evid + "/put.do", {name : name}, function(resp){
            if(resp.code == '200'){
                alert('操作成功');
                getEvaluations();
                hideWind();
            } else {
                alert('操作失败');
            }
        })
    }

    function addEvaluation(){
        var name = $.trim($('#evaluation-name').val());
        if(name == ''){
            alert("评比名称不能为空");
            return false;
        }
        var year = $('#year').val();
        Common.postDataAsync("/teacherevaluation/" + year + ".do", {name : name}, function(resp){
            if(resp.code == '200'){
                alert('操作成功');
                getEvaluations();
                hideWind();
            } else {
                alert('操作失败');
            }
        })
    }

    function getEvaluations(){
        var year = $('#year').val();
        Common.getDataAsync("/teacherevaluation/" + year + "/list.do", {}, function(resp){
            if(resp.code == '200'){
                Common.render({
                    tmpl : '#evaluationsTmpl',
                    data : resp.message,
                    context : '#evaluations',
                    overwrite : 1
                })
            }
        })
    }

    $(function(){
        $(".eva-add").click(function(){
            showWind();
        })
        $(".alert-close,.alert-btn-qx").click(function(){
            hideWind();
        })
    })

    function showWind(){
        $(".bg").show();
        $(".eva-add-alert").show();
    }

    function hideWind(){
        $(".bg").hide();
        $(".eva-add-alert").hide();
        $('#evaluation-name').val('');
        GLOBAL.evid = '';
    }





});