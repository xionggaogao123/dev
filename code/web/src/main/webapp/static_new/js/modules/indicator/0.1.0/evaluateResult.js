/**
 * Created by guojing on 2016/11/14.
 */
define('evaluateResult',['jquery','doT','easing','common','initPaginator'],function(require,exports,module){
    /**
     *初始化参数
     */
    var evaluateResult = {},
        Paginator = require('initPaginator'),
        Common = require('common');
    var evaluateData = {};

    evaluateResult.init = function() {
        Common.cal('calId');
        Common.leftNavSel();
        //设置初始页码
        /*if(null != sessionStorage.getItem("evaluateResultPage")){
            evaluateData.page = sessionStorage.getItem("evaluateResultPage")||1;
        }else{
            evaluateData.page = 1;
        }
        if(null != sessionStorage.getItem("evaluateResultName")){
            evaluateData.name = sessionStorage.getItem("evaluateResultName")||"";
        }else{
            evaluateData.name = "";
        }*/
        evaluateData.page = 1;
        //设置每页数据长度
        evaluateData.pageSize = 12;
        evaluateResult.searchiTreeAppliedData();
        $("#searchBtn").click(function(){
            //设置初始页码
            evaluateData.page = 1;
            evaluateData.name=$.trim($('#name').val());
            evaluateResult.searchiTreeAppliedData();
        });

        $('body').on('click','.evaluate-name',function() {
            var appliedId=$(this).attr("id");
            Common.goTo("/evaluate/evaluateResultInfo.do?appliedId="+appliedId);
        });
    };

    var option={};
    //查询
    evaluateResult.searchiTreeAppliedData = function(){
        /*sessionStorage.setItem("evaluateResultPage", evaluateData.page);
        sessionStorage.setItem("evaluateResultName", evaluateData.name);*/
        Common.getData('/evaluate/getITreeAppliedList.do',evaluateData,function(rep){
            var data=rep.message;
            $('.evaluate-data').html('');
            if(data.rows.length==0){
                evaluateData.page=1;
                if(data.total>0) {
                    evaluateResult.searchiTreeAppliedData();
                }
            }else {
                Common.render({tmpl: $('#j-tmpl'), data: data, context: '.evaluate-data'});
                option.total = data.total;
                option.pagesize = data.pageSize;
                option.currentpage = data.page;
                option.operate = function (totalPage) {
                    $('.page-index span').each(function () {
                        $(this).click(function () {
                            if (evaluateData.page != $(this).text()) {
                                evaluateData.page = $(this).text();
                                evaluateResult.searchiTreeAppliedData();
                            }
                        });
                    });
                    $('.first-page').click(function () {
                        if (evaluateData.page != 1) {
                            evaluateData.page = 1;
                            evaluateResult.searchiTreeAppliedData();
                        }
                    });
                    $('.last-page').click(function () {
                        if (evaluateData.page != totalPage) {
                            evaluateData.page = totalPage;
                            evaluateResult.searchiTreeAppliedData();
                        }
                    })
                };
                Paginator.initPaginator(option);
            }
        });
    };

    module.exports=evaluateResult;
});