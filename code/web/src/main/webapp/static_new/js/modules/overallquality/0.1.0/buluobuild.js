/**
 * Created by guojing on 2016/8/29.
 */
define('buluobuild',['jquery','doT','easing','common'],function(require,exports,module){
    /**
     *初始化参数
     */
    var buluobuild = {},
        Common = require('common');

    var searchParam={};
    /**
     * @func init
     * @desc 页面初始化
     * @example
     * buluobuild.init()
     */
    buluobuild.init = function(){

        $(".yx-dh").click(function(){
           var type=$(this).attr("type");
            buluobuild.propChange(type);
        });
    }
    buluobuild.propChange = function(type){
        var seaParam={};
        seaParam.type=type;
        seaParam.gradeId=$("#gradeId").val();
        seaParam.classId=$("#classId").val()||"";
        Common.getData("/qualityitem/propChange.do", seaParam, function(rep){
            if(rep.classId!=""){
                alert(rep.msg);
                if(rep.flag){
                    window.location.reload();
                }
            }
        });
    }
    module.exports=buluobuild;
});
