/**
 * Created by qiangm on 2015/7/16.
 */

define(function (require, exports, module) {
    require("jquery");
    require("doT");

    var type = 0;
    /**
     * 获取班级信息
     * @param nb_role
     * @returns {Array}
     */
    var getClassInfo = function (nb_role) {
        type = $.trim($("body").attr("interesttype"));
        var ret = [];
        $.ajax({
            url: "/reverse/getClassInfo",
            type: "get",
            dataType: "json",
            async: false,
            data: {
                type: type
            },
            success: function (data) {
                if (data.rows.length == 0) {
                    if (type == 2) {
                        $('#interest-info').show();
                        $('#interest-all-info').show();
                    } else {
                        $('#interest-all-info').hide();
                    }

                } else {
                    if (type == 2) {
                        $('#interest-info').hide();
                        $('#interest-all-info').show();
                    } else {
                        $('#interest-all-info').hide();
                    }
                    ret = data;
                    var source = document.getElementById("arraystmpl").innerHTML;
                    var template1 = doT.template(source);
                    jQuery("#fc_class_list").html(template1({
                        rows: data.rows,
                        role: isK6ktHelper(nb_role) || isHeadMasterAndTeacher(nb_role) || isTeacher(nb_role) || isLeaderOfSubject(nb_role)
                    }));
                }
            },
            complete: function () {
                $('.fc_class').bind('click', function (e) {
                    window.open($(this).attr('href'));
                });
                $('.t_score').bind('click', function (e) {
                    e.stopPropagation();
                    window.open($(this).attr('href'));
                });
            }
        });
        return ret;
    };

    exports.initPage = function (nb_role) {
        $('.loading').show();
        getClassInfo(nb_role);
        $('.loading').fadeOut(500);
    };
});
