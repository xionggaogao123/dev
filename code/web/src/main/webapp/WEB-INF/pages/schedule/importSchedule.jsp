<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib prefix="role" uri="http://fulaan.userRole.com" %>
<html>
<head>
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn"/>
    <title>课表导入</title>
    <meta name="description" content="">
    <meta name="author" content=""/>
    <meta name="copyright" content=""/>
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
    <!-- css files -->
    <!-- Normalize default styles -->
    <%-- <link href="/static/css/homepage.css?v=1" rel="stylesheet"/>--%>
    <link href="/static_new/css/reset.css?v=1" rel="stylesheet"/>
    <!-- jquery artZoom4Liaoba styles -->
    <!-- Custom styles -->
    <link href="/static_new/css/schedule/daorustart.css?v=2015041602" rel="stylesheet"/>

    <%--<script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>--%>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/js/sharedpart.js"></script>
    <%--<script type="text/javascript" src="/static/js/common/role.js"></script>--%>
    <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/doT.min.js"></script>
    <script type="text/javascript"
            src="/static_new/js/modules/core/0.1.0/jquery-upload/jquery.form.min.js?v=20150719"></script>

    <script>
        $(document).ready(function () {
            /*cal('calId');
             leftNavSel();*/
            getTermList();
            $(".daorustart_btn").on("click", function () {
                if ($("#file").val() == "") {
                    alert("请先上传文件");
                }
                else {
                    $("#myform").ajaxSubmit({
                        type: "post",
                        url: "/schedule/import.do",
                        success: function (data) {
                            //提交成功后调用
                            if (data.result == "success")
                                alert("导入成功");
                            else {
                                if (data.line == "") {
                                    alert("导入失败，失败原因：" + data.reason);
                                }
                                else {
                                    alert("导入失败，失败原因：" + data.reason + ",错误位置：" + data.line);
                                }
                            }

                        },
                        error: function (XmlHttpRequest, textStatus, errorThrown) {
                            alert("导入失败");
                        }
                    });
                }
            });
            $(".back").click(function () {
                window.location = "/schedule/viewSchedule.do?version=5k";
            });
            $("#termList").on("change", function () {
                $("#term").val($(this).val());
            });
            $("body").on("click", "#download", function () {
                window.location = '/schedule/exportTemplate.do?term=' + encodeURI(encodeURI($("#termList").val()));
            });
            $("body").on("change", "#termList", function () {
                var term = $(this).val();
                getTableConf(term);
            });
            $("body").on("change", ".timetable-SE", function () {
                var count = Number($(this).val());
                $(".timeAdd").css("display", "none");
                $(".timeAdd1").css("display", "none");
                for (var i = 0; i < count; i++) {
                    $("#time" + (i + 1)).css("display", "inline-block");
                    $("#timeShow" + (i + 1)).css("display", "inline-block");
                }
            });
            $("body").on("click", "#saveConf", function () {
                saveTableConf();
            });
            leftNavSel();
        });
        var courseConfDTO = {};
        function getTableConf(term) {
            $.ajax({
                url: "/schedule/findCourseConf.do",
                type: "get",
                dataType: "json",
                data: {
                    term: term
                },
                success: function (data) {
                    data = data.message;
                    for (var i = 0; i < data.classDays.length; i++) {
                        $(":checkbox[value='" + data.classDays[i] + "'][name='classDays']").prop("checked", true);
                    }
                    $(".timetable-SE").val(data.classCount);
                    for (var i = 0; i < data.classTime.length; i++) {
                        var time = data.classTime[i];
                        $("#time" + (i + 1)).val(time);
                        $("#time" + (i + 1)).css("display", "inline-block");
                        $("#timeShow" + (i + 1)).css("display", "inline-block");
                    }
                    courseConfDTO = data;
                }
            })
        }

        function saveTableConf() {
            var classDays = [];
            $(":checkbox[name='classDays']").each(function () {
                //var chk = $(this).find("[checked]");
                if (this.checked) {
                    classDays.push(Number($(this).val()));
                }
            });
            if (classDays.length == 0) {
                alert("请勾选上课天数");
                return;
            }
            courseConfDTO.classDays = classDays;
            var classCount = Number($(".timetable-SE").val());
            var classTimes = [];
            for (var i = 0; i < classCount; i++) {
                var time = $("#time" + (i + 1)).val();
                if (!time) {
                    alert("请填写上课时间");
                    return;
                }
                classTimes.push(time);
            }
            courseConfDTO.classTime = classTimes;
            courseConfDTO.classCount = classTimes.length;
            $.ajax({
                url: "/schedule/addCourseConf.do",
                type: "post",
                contentType: "application/json",
                data: JSON.stringify(courseConfDTO),
                success: function (data) {
                    if (data.code == "200") {
                        alert("添加成功");
                    }
                    else {
                        alert("添加失败");
                    }
                }
            });
        }

        function getTermList() {
            $.ajax({
                url: "/zouban/common/getTermList.do",
                type: "post",
                dataType: "json",
                success: function (data) {
                    var termList = data.termList;
                    var innerHtml = "";
                    for (var i = 0; i < termList.length; i++) {
                        innerHtml += '<option value="' + termList[i] + '第二学期">' + termList[i] + '第二学期</option>';
                        innerHtml += '<option value="' + termList[i] + '第一学期">' + termList[i] + '第一学期</option>';
                    }
                    $("#termList").append(innerHtml);
                    $("#termList").val($("#curTerm").val());
                    $("#term").val($("#curTerm").val());
                    getTableConf($("#curTerm").val());
                }
            });
        }
        function getData(url, data, callback) {
            //var encodeUrl = encodeURIComponent(url);
            $.ajax({
                type: "GET",
                data: data,
                url: url,
                async: false,
                dataType: "json",
                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                success: function (rep) {
                    if (!rep) {
                        //$("#pageLoading").hide();
                        return rep;
                    }
                    try {
                        $.isPlainObject(rep);
                    } catch (e) {
                        console.log("数据解析错误!");
                        return;
                    }
                    callback.apply(this, arguments);
                }
            });
        }
        ;
        function renderHtml(tmpl, context) {
            var contentEl = $(context);
            return $(tmpl).appendTo(contentEl);
        }
        ;
        function leftNavSel() {

            //
            var version = $("#versionValue").val();
            var versionIdx = 0;
            if (version && version.length == 2) {
                versionIdx = version.substring(0, 1);
            }
            $("#left-nav-version" + versionIdx).addClass('current');
            $("#left-nav-version" + version).css({"color": '#ff0000'});

            $("#left-nav-version" + versionIdx).next("dl").slideDown(400);

            $(".left-nav li.er").click(function (event) {
                $(this).toggleClass('re').siblings().removeClass('re').next("dl").slideUp(400);
                $(".left-nav li.er").children('s').html("&#xe61c;");
                $(".left-nav li.re").children('s').html("&#xe607;");
                $(this).next("dl").slideToggle(400);

            });

            $(".left-nav li#pre").click(function (event) {
                $(this).addClass('current').siblings().removeClass('current');
                $(".left-nav dl a").css({"color": '#666'});
            });


            /*右侧头部*/
            $(".user-cener").hover(function () {
                $(this).children('ul').css({
                    "display": 'block'
                });
            }, function () {
                $(this).children('ul').css({
                    "display": 'none'
                });
            });

            //私信数目
            getData("/letter/count.do", null, function (resp) {
                if (resp) {
                    if (resp > 0) {
                        $('a.user-msg').html('<i>' + resp + '</i>私信');
                    } else {
                        $('a.user-msg').html('私信');
                    }
                }
            })


            //同学排行
            getData('/user/studenttopfive', null, function (resp) {

                if (resp.length == 0) {
                    jQuery("#top5Container").hide();
                }
                else {
                    render({
                        tmpl: '#top5studenttemp',
                        data: resp,
                        context: '#top5studentcontainner',
                        overwrite: 1
                    });
                }
            });
        }
        function render(cfg) {
            var self = this,
                    _data = cfg.data,
                    dom = '',
                    context = cfg.context,
                    callback = cfg.callback,
                    _tmpl = doT.template($(cfg.tmpl).html());
            if (cfg.overwrite) {
                $(context).empty();
            }
            if (_tmpl) {
                dom = self.renderHtml($.trim(_tmpl(_data)), context);
            } else {
                console.log("对应的模块不存在!");
            }
            callback && callback.call(this, {data: _data, dom: dom});
        }


    </script>
</head>
<body>
<!--#head-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->
<!--#content-->
<div id="content" class="clearfix">
    <!--.col-left-->
    <%@ include file="../common_new/col-left.jsp" %>
    <input type="hidden" id="curTerm" value="${term}"/>
    <!--/.col-left-->
    <!--.col-right-->
    <div class="col-right">
        <div class="daorustart_main">
            <span class="back"> < 返回</span>

            <div class="daoru_worker">
                <p>导入人员</p>
            </div>
            <div class="daorustart_step">
                <div class="kb-top1">
                    <p>第一步:</p><em>选择学期</em>
                    <select id="termList" class="termSe">
                    </select>
                </div>
                <p class="first_step">第二步:</p><em>设置课表结构</em>
                <dl>
                    <dd class="sch-dd">
                        <em class="em2">上课天数</em>
                        <input type="checkbox" value="1" name="classDays"><i>星期一</i>
                        <input type="checkbox" value="2" name="classDays"><i>星期二</i>
                        <input type="checkbox" value="3" name="classDays"><i>星期三</i>
                        <input type="checkbox" value="4" name="classDays"><i>星期四</i>
                        <input type="checkbox" value="5" name="classDays"><i>星期五</i>
                        <input type="checkbox" value="6" name="classDays"><i>星期六</i>
                        <input type="checkbox" value="7" name="classDays"><i>星期日</i>
                    </dd>
                    <dd class="sch-dd">
                        <em class="em2">每天节数</em>
                        <select class="timetable-SE">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                            <option value="9">9</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                            <option value="13">13</option>
                            <option value="14">14</option>
                            <option value="15">15</option>
                        </select>
                    </dd>
                    <dd class="sch-dd">
                        <em class="em2">上课时间</em>

                        <div class="sksj">
                            <span id="timeShow1" class="timeAdd1">第一节:</span><input id="time1" class="timeAdd"><br>
                            <span id="timeShow2" class="timeAdd1">第二节:</span><input id="time2" class="timeAdd"><br>
                            <span id="timeShow3" class="timeAdd1">第三节:</span><input id="time3" class="timeAdd"><br>
                            <span id="timeShow4" class="timeAdd1">第四节:</span><input id="time4" class="timeAdd"><br>
                            <span id="timeShow5" class="timeAdd1">第五节:</span><input id="time5" class="timeAdd"><br>
                            <span id="timeShow6" class="timeAdd1">第六节:</span><input id="time6" class="timeAdd"><br>
                            <span id="timeShow7" class="timeAdd1">第七节:</span><input id="time7" class="timeAdd"><br>
                            <span id="timeShow8" class="timeAdd1">第八节:</span><input id="time8" class="timeAdd"><br>
                            <span id="timeShow9" class="timeAdd1">第九节:</span><input id="time9" class="timeAdd"><br>
                            <span id="timeShow10" class="timeAdd1">第十节:</span><input id="time10" class="timeAdd"><br>
                            <span id="timeShow11" class="timeAdd1">第十一节:</span><input id="time11" class="timeAdd"><br>
                            <span id="timeShow12" class="timeAdd1">第十二节:</span><input id="time12" class="timeAdd"><br>
                            <span id="timeShow13" class="timeAdd1">第十三节:</span><input id="time13" class="timeAdd"><br>
                            <span id="timeShow14" class="timeAdd1">第十四节:</span><input id="time14" class="timeAdd"><br>
                            <span id="timeShow15" class="timeAdd1">第十五节:</span><input id="time15" class="timeAdd"><br>
                        </div>
                    </dd>
                    <dd class="save-dd">
                        <button id="saveConf" class="model_download">保存课表结构</button>
                    </dd>
                </dl>
                <div class="kb-top1">
                    <p class="first_step">第三步:</p>
                    <button class="model_download" id="download">下载模板</button>
                </div>
                <div class="kb-top1">
                    <p>第四步:</p>

                    <div class="daorustart_file">
                        <form id="myform" method="post"
                              enctype="multipart/form-data">
                            <input type="file" id="file" name="file"/>
                            <input type="hidden" id="term" name="term"/>
                            <%--<button class="c-right-la">浏览</button>--%>
                        </form>
                    </div>
                    <button class="daorustart_btn">开始导入</button>
                </div>
                <div class="kb-top1">
                    <p>导入说明：</p><br>
                    <span class="notice-sp">1.请勿修改sheet名以及导出模板的名字。</span><br>
                    <span class="notice-sp">2.课程内容格式参照：课程名-老师名，举例"语文-张三"。</span><br>
                    <span class="notice-sp">3.课程名以及老师名字请严格按照平台中的名称，请格外留意老师名后面的数字。</span><br>
                    <span class="notice-sp">4.请勿在发布课表后随便修改课表结果或重新导入课表，该操作会导致老师请假以及代课数据错乱。</span>
                    <span class="notice-sp">5.如您已经导入过课表，再次导入会清除该学期的老师代课记录，请谨慎操作。</span>
                </div>
            </div>
        </div>
    </div>
    <!--/.col-right-->

</div>

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<%--<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('importSchedule');
</script>--%><%--<script src="/static_new/js/sea.js?v=1"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('importSchedule');
</script>--%>
</body>
</html>