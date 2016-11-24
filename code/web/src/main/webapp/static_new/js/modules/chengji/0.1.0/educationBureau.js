/**
 * Created by fl on 2015/9/8.
 */
define(['chengji'],function(require,exports,module){

    var chengJi = require('chengji');
    var Common = require('common');
    var educationBureau = {};


    educationBureau.init = function(){
        chengJi.init();

        var requestData = {};
        Common.getData('/score/getSchoolList.do', requestData, function (resp) {
            Common.render({
                tmpl: '#schoolList_tmpl',
                data: resp.schoolDTOList,
                context: '#schoolList',
                overwrite: 1
            });

        });

        $("body").on('click','.school', function(){
            sessionStorage.setItem("schoolId", $(this).attr("schoolId"));
            if (GetQueryString("a")!="10000") {
                Common.goTo("/score/header.do");
            } else {
                Common.goTo("/score/header.do?a=10000");
            }
        })

        function GetQueryString(name)
        {
            var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if(r!=null)return  unescape(r[2]); return null;
        }


    }



    module.exports = educationBureau;

})

