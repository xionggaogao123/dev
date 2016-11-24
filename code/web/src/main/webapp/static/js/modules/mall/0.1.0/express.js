define(function (require, exports, module) {
    var express = {};
    require('jquery');
    require('doT');
    common = require('common');

    express.init = function () {
        expressInfo($('#exCompanyNo').val(), $('#expressNo').val());
        $("#ecName").text(getCompanyName($("#ecn").val()));
    }

    expressInfo = function (exCompanyNo, expressNo) {
        if (expressNo) {
            var url = "/mall/order/express.do";
            var data = {exCompanyNo: exCompanyNo, expressNo: expressNo};
            common.getData(url, data, function (rep) {
                var expressList = common.eva('(' + rep.expressList + ')').Traces;
                if (expressList != null && expressList.length > 0) {
                    common.render({tmpl: $('#exInfo'), data: expressList, context: '#expressInfo'});
                }
            });
        }
    }

    exCompanyList = [
        {no: 'BTWL', name: '百世物流'},
        {no: 'EMS', name: 'EMS'},
        {no: 'HHTT', name: '天天快递'},
        {no: 'QFKD', name: '全峰快递'},
        {no: 'SF', name: '顺丰快递'},
        {no: 'STO', name: '申通快递'},
        {no: 'SURE', name: '速尔快递'},
        {no: 'UAPEX', name: '全一快递'},
        {no: 'UC', name: '优速快递'},
        {no: 'YD', name: '韵达快递'},
        {no: 'YTO', name: '圆通速递'},
        {no: 'YZPY', name: '邮政平邮/小包'},
        {no: 'ZJS', name: '宅急送'},
        {no: 'ZTO', name: '中通速递'}
    ]
    //根据编号获取快递公司名称
    getCompanyName = function (no) {
        for (index in exCompanyList) {
            if (exCompanyList[index].no == no) {
                return exCompanyList[index].name;
            }
        }
    }

    module.exports = express;
});
