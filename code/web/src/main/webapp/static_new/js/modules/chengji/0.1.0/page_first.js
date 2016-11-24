define(['chengji'],function(require,exports,module){

    var chengJi = require('chengji');
    var common = require('common');
    var pagefirst = {};
    var tableData=[];
    var tmpl = '';
    var context = '';

    pagefirst.initPage = function() {
        chengJi.init();
        $("#manager_BJ").hide();
        //创建考试
        $("#manager_CJ").on('click', function() {
            pagefirst.createExam();
        })
        $("#manager_BJ").on('click', function() {
            pagefirst.editExam();
        })

        //删除
        $('body').on('click','.examine_delete',function(){
            var data = {};
            data.examId = $(this).attr("examId");
            var msg = "您确定要删除吗？";
            if (confirm(msg)==true) {

                common.getData('/score/managerDeleteExam.do', data, function (resp) {
                    if (resp) {
                        pagefirst.getExamList();
                    }

                });
            }
        });

        //编辑回显数据
        $('body').on('click','.examine_bianji',function(){
            var msg = "编辑本次考试，将会清除各个班级已有的打分记录，确认编辑吗？";
            if (confirm(msg) == true) {
                $("#manager_BJ").show();
                $("#manager_CJ").hide();
                var data = {};
                data.examId = $(this).attr("examId");
                sessionStorage.setItem("editExamId", data.examId);

                common.getData('/score/getGradeExamInfo.do', data, function (resp) {
                    $("#examName").val(resp.examName);
                    $("#date").val(resp.date);
                    $("#type").find("option").each(function () {
                        if ($(this).text() == resp.type) {
                            $(this).attr("selected", "selected");
                        }
                    });
                    var checkBoxAll = $("input:checkbox");
                    var subjectList = resp.subjectList;
                    //for (var i in subjectList) {
                        $.each(checkBoxAll, function (k, checkbox) {
                            $(checkbox).prop("checked", false);
                            for (var i in subjectList) {
                                //获取复选框的value属性
                                var checkValue = $(checkbox).val();
                                if (subjectList[i] == checkValue) {
                                    $(checkbox).prop("checked", true);
                                    break;
                                }
                            }
                        })
                    //}

                });
            }
        });

        var requestData = {};
        requestData.gradeId = sessionStorage.getItem("gradeId");
        //取得科目列表
        common.getData('/score/subjectList.do', requestData, function(resp) {
            common.render({
                tmpl: '#subjectListTmpl',
                data: resp,
                context: '#subjectList',
                overwrite: 1
            });
        })
        //取得已建考试列表
        pagefirst.getExamList();
    }

    pagefirst.createExam = function() {
        var data={};
        //保存考试信息
        var name = $("#examName").val();
        var date = $("#date").val();
        var gradeId = sessionStorage.getItem("gradeId");
        var type = $("#type").find("option:selected").text();
        var subList=$("input:checkbox:checked").map(function(index,elem) {
                        return $(elem).val();
                    }).get().join(',');

        if(name == "" || date == "" || gradeId == "" || type == "" || subList == "") {
            $("#warningMessage").text("请完善考试信息");
        } else {
            $("#warningMessage").text("");
            data.gradeId = gradeId;
            data.name = name;
            data.date = date;
            data.type = type;
            data.subList = subList;
            common.getData('/score/createGradeExam.do', data, function (resp) {
                if (resp) {
                    //取得已建考试列表
                    pagefirst.getExamList();
                }
            })
            $("#examName").val('');
            $("#date").val('');
            $("input:checkbox:checked").map(function(index,elem) {
                $(elem).prop('checked', false);
            });

        }

    }

    pagefirst.editExam = function() {
        var data={};
        //保存考试信息
        var name = $("#examName").val();
        var date = $("#date").val();
        var type = $("#type").find("option:selected").text();
        var subList=$("input:checkbox:checked").map(function(index,elem) {
            return $(elem).val();
        }).get().join(',');

        if(name == "" || date == "" || type == "" || subList == "") {
            $("#warningMessage").text("请完善考试信息");
        } else {
            $("#warningMessage").text("");
            data.name = name;
            data.date = date;
            data.type = type;
            data.subList = subList;
            data.examId = sessionStorage.getItem("editExamId");
            common.getData('/score/managerEditExam.do', data, function (resp) {
                if (resp) {
                    //取得已建考试列表

                }
            })
            pagefirst.getExamList();
            $("#manager_CJ").show();
            $("#manager_BJ").hide();
            alert("编辑成功！");
            $("#examName").val('');
            $("#date").val('');
            $("input:checkbox:checked").map(function(index,elem) {
                $(elem).prop('checked', false);
            });
        }

    }

    pagefirst.getExamList = function() {
        $("#title").text(sessionStorage.getItem("gradeName") + "考试列表");
        var data={};
        data.gradeId = sessionStorage.getItem("gradeId");
        if(data.gradeId) {
            common.getData('/score/gradeExamList.do', data, function (resp) {
                //common.render({
                //    tmpl: '#examListTmpl',
                //    data: resp.gradeExamList,
                //    context: '#examList',
                //    overwrite: 1
                //});
                tableData = resp.gradeExamList;
                tmpl = '#examListTmpl';
                context = '#examList';
                pagefirst.splitPage(1,15);
            })
        }
    }

    pagefirst.splitPage = function(page,pageSize) {
        var totalNums = tableData.length;//总行数
        var totalPage = Math.ceil(totalNums/pageSize);//总页数
        var begin = (page-1)*pageSize;//页起始位置(包括)
        var end = page*pageSize;//页结束位置(不包括)
        end = end>totalNums?totalNums:end;
        var showData=[];
        for(var i=begin,j=0;i<end;i++,j++) {
            showData[j] = tableData[i];
        }
        common.render({
            tmpl: tmpl,
            data: showData,
            context: context,
            overwrite: 1
        });
//生成分页工具条
        var pageBar = "第"+page+"页/共"+totalPage+"页"+" ";
        if(page>1){
            pageBar += "<a href=\"javascript:splitPage("+1+","+pageSize+");\">首页</a> ";
        }else{
            pageBar += "首页 ";
        }
        if(page>1){
            pageBar += "<a href=\"javascript:splitPage("+(page-1)+","+pageSize+");\">上一页</a> ";
        }else{
            pageBar += "上一页 ";
        }
        if(page<totalPage){
            pageBar += "<a href=\"javascript:splitPage("+(page+1)+","+pageSize+");\">下一页</a> ";
        }else{
            pageBar += "下一页 ";
        }
        if(page<totalPage){
            pageBar += "<a href=\"javascript:splitPage("+(totalPage)+","+pageSize+");\">尾页</a> ";
        }else{
            pageBar += "尾页 ";
        }
        document.getElementById("page_bar").innerHTML = pageBar;
    }

    module.exports = pagefirst;

})
