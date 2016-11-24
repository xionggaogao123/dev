define(function(require,exports,module){
    //,'ajaxForm'

    var chengJi = require('chengji');
    require('ajaxfileupload');


    var Common = require('common');
    var teaEdit={};
    var tableData=[];
    var requestData = {};
    teaEdit.initPage = function(){
        if (GetQueryString("a")!="10000") {


        }

        var examTitle = sessionStorage.getItem("title") + sessionStorage.getItem("examName");
        $("#editScoreTitle").text(examTitle);
        requestData.examId = sessionStorage.getItem("examId");
        requestData.classId = sessionStorage.getItem("classId");
        requestData.subjectId = sessionStorage.getItem("subjectId");

        teaEdit.getScoreList();

        //保存成绩
        $('button.savescore').on('click',function() {
            teaEdit.saveScore();

        })

        //取消
        $('button.quit').on('click',function() {
            if (GetQueryString("a")!="10000") {
                Common.goTo('/score/teacher/input.do');
            } else {
                Common.goTo('/score/teacher/input.do?a=10000');
            }


        })

        //下载模板
        $('button.export').on('click',function() {
            window.location.href = "/score/exportExcel.do?examId=" + requestData.examId + "&classId=" + requestData.classId +
                                    "&subjectId=" + requestData.subjectId + "&examTitle=" + examTitle;

        })

        //$("#file").fileupload({
        //    autoUpload: true,
        //    url: "/score/import.do",
        //    formData:requestData,
        //
        //    done:function(e,result){
        //        alert("上传成功！")
        //    }
        //});

        //导入成绩
        $('button.import').on('click',function() {
            if ($("#file").val() == "") {
                alert("请先选择上传文件");
            } else {
                $.ajaxFileUpload
                (
                    {
                        url: "/score/import.do?examId="+ requestData.examId +"&subjectId="+requestData.subjectId, //用于文件上传的服务器端请求地址
                        secureuri: false, //是否需要安全协议，一般设置为false
                        fileElementId: 'file', //文件上传域的ID
                        dataType: 'json', //返回值类型 一般设置为json

                        success: function (data, status)  //服务器成功响应处理函数
                        {
                            if(data == false) {
                                alert("上传失败！");
                            } else {
                                alert("上传成功！");
                                teaEdit.getScoreList();
                            }
                        },
                        error: function (data, status, e)//服务器响应失败处理函数
                        {
                            alert("上传失败！");
                        }
                    }
                )
            }

        })

        $('body').on('click','.abs',function(){//缺席
            $(this).parent().prev().children('input').val("");
            $(this).parent().next().children('input').val(0);
            $(this).parent().next().children('img').attr("src", "/static_new/images/result_main_II.jpg");

            if ($(this).attr("src") == "/static_new/images/result_que_II.jpg") {
                $(this).attr("src", "/static_new/images/result_que_I.jpg");
                $(this).next().val(1);
                $(this).parent().prev().children('input').hide();
            } else {
                $(this).attr("src", "/static_new/images/result_que_II.jpg");
                $(this).next().val(0);
                $(this).parent().prev().children('input').show();
            }

            //alert($(this).next().val());
        })

        $('body').on('click','.exe',function(){//免考
            $(this).parent().prev().prev().children('input').val("");
            $(this).parent().prev().children('input').val(0);
            $(this).parent().prev().children('img').attr("src", "/static_new/images/result_que_II.jpg");

            if ($(this).attr("src") == "/static_new/images/result_mian_I.jpg") {
                $(this).attr("src", "/static_new/images/result_main_II.jpg");
                $(this).next().val(0);
                $(this).parent().prev().prev().children('input').show();
            } else {
                $(this).attr("src", "/static_new/images/result_mian_I.jpg");
                $(this).next().val(1);
                $(this).parent().prev().prev().children('input').hide();
            }

            //alert($(this).next().val());
        })



    };

    teaEdit.getScoreList = function() {
        //成绩列表
        Common.getData('/score/getEditScoreList.do', requestData, function (resp) {
            tableData = resp.perDTOList;
            sessionStorage.setItem("fullScore", resp.fullScore);
            Common.render({
                tmpl: '#ScoreListTmpl',
                data: tableData,
                context: '#Ipt',
                overwrite: 1
            });
        });
    }
    //保存成绩
    teaEdit.saveScore = function() {
        var fullScore = sessionStorage.getItem("fullScore");
        var flag = true;
        var temp = /^\d+(\.\d+)?$/;
        $("input[name='score']").map(function (index, elem) {
            var score = $(elem).val();
            if (score.trim() != '' && !temp.test(score)) {
                alert("考试分数应为数字！");
                flag = false;
            }else if(parseFloat(score)>fullScore) {
                alert("考试分数应小于满分！");
                flag = false;
            }

        });
        var performanceList = $("input[name='perList']").map(function (index, elem) {
            return $(elem).val();
        }).get().join(',');
        if (flag && performanceList.length != 0){

            var scoreList = $("input[name='score']").map(function (index, elem) {
                return $(elem).val();
            }).get().join(',');
            scoreList += ',0';


            var absList = $("input[name='absList']").map(function (index, elem) {
                return $(elem).val();
            }).get().join(',');

            var exeList = $("input[name='exeList']").map(function (index, elem) {
                return $(elem).val();
            }).get().join(',');


            var data = {};
            data.performanceList = performanceList;
            data.scoreList = scoreList;
            data.absList = absList;
            data.exeList = exeList;
            data.subjectId = sessionStorage.getItem("subjectId");

            //保存成绩
            Common.getData('/score/saveEditScoreList.do', data, function (resp) {
                if (resp) {
                    if (GetQueryString("a")!="10000") {
                        Common.goTo('/score/teacher/input.do');
                    } else {
                        Common.goTo('/score/teacher/input.do?a=10000');
                    }
                }
            });
        }
    }
    function GetQueryString(name)
    {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r!=null)return  unescape(r[2]); return null;
    }
    module.exports = teaEdit;

})
