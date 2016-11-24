/**
 * Created by fl on 2015/8/26.
 */

define(function(require, exports, module) {
    var statPage={};
    var Common = require('common');

    statPage.init = function() {

        if (sessionStorage.getItem("pg") == 1) {
            var requestData = {};
            requestData.hwid = sessionStorage.getItem("homeworkId");
            requestData.classId = sessionStorage.getItem("classId");
            requestData.className = sessionStorage.getItem("className");
            requestData.classType = sessionStorage.getItem("classType");
            Common.getData('/homework/getsubmitInfo.do', requestData, function (resp) {
                Common.render({
                    tmpl: '#submitInfoTmpl',
                    data: resp.infoDTOMap,
                    context: '#submitInfo',
                    overwrite: 1
                });
                if(resp.size > 0){
                    $('#ck').addClass('up').removeClass('down');
                    $('#ck').text('收起作业统计');
                    $('#hwtj1').show();
                } else {
                    $('#ck').addClass('down').removeClass('up');
                    $('#ck').text('查看作业统计');
                    $('#hwtj1').hide();
                }
            });
        } else {
            $('#hwtj,#hwtj1').hide();
        }
    }

    statPage.sendLetters = function(){
        var requestData = {};
        requestData.homeworkId = sessionStorage.getItem("homeworkId");
        requestData.classId = sessionStorage.getItem("classId");
        requestData.classType = sessionStorage.getItem("classType");
        $.ajax({
            type: "GET",
            data:requestData,
            url: '/homework/sendLetters.do',
            async: true,
            dataType: "json",
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            beforeSend:function(){
                $('.letter').text("私信发送中...")
            },
            success: function(resp){
                if(resp.code == '200'){
                    alert("发送成功")
                    $('.letter').text("发送成功")
                } else {
                    alert("发送失败")
                    $('.letter').text("发送私信")
                }
            }
        });
    }


    module.exports = statPage;
});
