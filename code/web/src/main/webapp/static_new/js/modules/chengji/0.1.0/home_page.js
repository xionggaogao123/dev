define(['chengji'],function(require,exports,module){

    var chengJi = require('chengji');
    var common = require('common');
    var homepage = {};

    homepage.initPage = function() {
        chengJi.init();

        $('body').on('click', '.grade', function() {
            //alert($(this).children("#gradeId").attr("value"));
            var gradeId = $(this).children("#gradeId").attr("value");
            sessionStorage.setItem("gradeId",gradeId);
            var gradeName = $(this).children("#gradeName").text();
            sessionStorage.setItem("gradeName",gradeName);
            common.goTo("/score/manager/input.do");
        });


        //年级列表
        var data = {};
        common.getData('/score/gradeList.do', data, function (resp) {
            common.render({
                tmpl: '#gradeLeaderTmpl',
                data: resp,
                context: '#gradeLeader',
                overwrite: 1
            });
        })


    }

    module.exports = homepage;

})
