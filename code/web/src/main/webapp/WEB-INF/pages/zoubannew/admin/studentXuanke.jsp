<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <title>3+3走班</title>
    <!-- css files -->
    <!-- Normalize default styles -->
    <link href="/static_new/css/reset.css" rel="stylesheet"/>
    <link href="/static_new/css/zouban/zoubannew.css" rel="stylesheet"/>
    <link href="/static_new/js/modules/core/0.1.0/layer/skin/layer.css" rel="stylesheet">
    <script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>
</head>

<body term="${term}" gradeId="${gradeId}" gradeName="${gradeName}" xuankeId="${xuankeId}">

<!--#head-->
<%@ include file="../../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <!--.col-left-->
    <%@ include file="../../common_new/col-left.jsp" %>
    <!--/.col-left-->

    <!--.col-right-->
    <div class="col-right">

        <!--.tab-col右侧-->
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li id="ZBXKSZ" class="cur"><a href="javascript:;">学生选课</a><em></em></li>
                </ul>
            </div>

            <div class="tab-main">
                <div class="zouban-title clearfix">
                    <div class="title-left">
                        <h3 class="xkzhsz-title">${term}<span>${gradeName}</span></h3>
                    </div>
                    <div class="title-right">
                        <a href="/zouban/baseConfig.do?term=${term}&gradeId=${gradeId}" class="m-back">&lt;&nbsp;返回走班教务管理</a>
                    </div>
                </div>
                <ul class="tstep-tab clearfix">
                    <li id="XKSZ" class="m-active"><a href="javascript:;">选课设置</a></li>
                    <li id="DRJG"><a href="javascript:;">导入学生选课结果</a></li>
                    <li id="XKJD"><a href="javascript:;">学生选课进度</a></li>
                    <li id="TZXK"><a href="javascript:;">调整选课结果</a></li>
                    <li id="DCJG"><a href="javascript:;">导出结果</a></li>
                </ul>
                <div class="set-div">
                    <!--================================第二步选课说明start===============================-->
                    <div class="xkset-con" id="tab-XKSZ">
                        <div class="clearfix" style="margin: 20px 0;">
                            <span style="display: inline-block;float: left;height:30px;line-height: 30px;font-size: 16px;">选课设置</span>
                            <button class="btn-gongbu">
                                <c:if test="${isRelease == 1}">取消</c:if>公布
                            </button>
                        </div>
                        <div class="wrap-border">
                            <div class="btn-wrap">
                                <span>学生选课时间：<em id="begin"></em>~<em id="end"></em></span>
                                <button class="btn-settime">设置选课时间</button>
                                <button id="saveInfo" class="xk-save">保存</button>
                            </div>
                            <dl class="xk-shuom">
                                <dt>选课说明</dt>
                                <textarea id="info"></textarea>
                            </dl>
                            <h3 style="text-align: center">开放选课组合</h3>
                            <table class="xkzhsz-table">
                                <thead>
                                <tr>
                                    <th style="width:22%;">组合</th>
                                    <th style="width:13%;">物理</th>
                                    <th style="width:13%;">化学</th>
                                    <th style="width:13%;">生物</th>
                                    <th style="width:13%;">政治</th>
                                    <th style="width:13%;">历史</th>
                                    <th style="width:13%;">地理</th>
                                </tr>
                                </thead>
                                <tbody id="subjectGroupsContext">
                                </tbody>
                                <script id="subjectGroupsTmpl" type="text/template">
                                    {{~ it:group:i }}
                                    <tr>
                                        <td style="background:#ececec;">
                                            <label><input type="checkbox" {{? group.isPublic==true}}checked{{?}}
                                                          name="subjectgroup" sgid="{{=group.id}}">&nbsp;{{=group.groupName}}</label>
                                        </td>
                                        {{~ group.chooseState:state:j }}
                                        <td><input type="checkbox" {{? state==1}}checked{{?}} disabled></td>
                                        {{~ }}
                                    </tr>
                                    {{~ }}
                                </script>

                                <tr>
                                    <td style="background:#ececec;">共计20种组合</td>
                                    <td>含物理10种</td>
                                    <td>含化学10种</td>
                                    <td>含生物10种</td>
                                    <td>含政治10种</td>
                                    <td>含历史10种</td>
                                    <td>含地理10种</td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <!--================================第二步选课说明end===============================-->
                    <!--================================第二步导入学生选课结果start===============================-->
                    <div class="drjg-con" id="tab-DRJG">
                        <div class="drjg-title">
                            <span>${term}</span>
                            <span>${gradeName}</span>

                        </div>
                        <div class="drjg-main">
                            <input type="file" id="file" name="file">

                            <div class="model-btn">
                                <button class="download">下载模板</button>
                                <button class="buu">开始导入</button>
                            </div>
                            <dl>
                                <dt>导入数据说明：</dt>
                                <dd>1.导入数据请严格按照模板，请勿改变用户名和班级名</dd>
                                <dd>2.等级考和合格考一栏输入完整学科名，不需要加分隔符号，例”物理化学生物“</dd>
                            </dl>
                        </div>
                    </div>
                    <!--================================第二步导入学生选课结果end===============================-->
                    <!--================================第二步学生选课进度start===============================-->
                    <div class="xsxkjd-con" id="tab-XKJD">
                        <span class="xsxkjd-title">未选课人数<em>0/0</em></span>
                        <table class="xsxkjg-table">
                            <thead>
                            <tr>
                                <th style="width:25%;">学科</th>
                                <th style="width:25%;">等级考人数</th>
                                <th style="width:25%;">合格考人数</th>
                                <th style="width:25%;">查看</th>
                            </tr>
                            </thead>
                            <tbody id="subjectStuNumContext">
                            </tbody>
                            <script id="subjectStuNumTmpl" type="text/template">
                                {{~ it:value:i }}
                                <tr>
                                    <td style="background:#ececec;">{{=value.subjectName}}</td>
                                    <td>{{=value.advUserCount}}</td>
                                    <td>{{=value.simUserCount}}</td>
                                    <td><a href="javascript:;" class="drjg-chakan" subid="{{=value.subjectId}}">查看</a>
                                    </td>
                                </tr>
                                {{~ }}
                            </script>
                        </table>
                        <table class="xsxkjg-table-2">
                            <thead>
                            <tr>
                                <th style="width:35%;">选课组合</th>
                                <th style="width:32%;">人数</th>
                                <th style="width:33%;">操作</th>
                            </tr>
                            </thead>
                            <tbody id="subGroupNumContext">
                            </tbody>
                            <script id="subGroupNumTmpl" type="text/template">
                                {{~ it:value:i }}
                                <tr>
                                    <td style="background:#ececec;">
                                        <label>{{=value.name}}</label>
                                    </td>
                                    <td>{{=value.value}}</td>
                                    <td>
                                        <a href="javascript:;" class="drjg-look" sgid="{{=value.id}}">查看</a>
                                    </td>
                                </tr>
                                {{~ }}
                            </script>
                        </table>
                    </div>
                    <!--================================第二步学生选课进度end===============================-->
                    <!--================================第二步调整选课结果start===============================-->
                    <div class="tzxkjg-con" id="tab-TZXK">
                        <div class="drjg-title">
                            <span>行政班</span>
                            <select class="adminClassContext1">
                                <%--<option>1班</option>--%>
                            </select>
                            <script id="adminClassTmpl1" type="text/template">
                                {{~ it:value:i }}
                                <option value="{{=value.id}}">{{=value.className}}</option>
                                {{~ }}
                            </script>
                            <span>选课状态</span>
                            <select id="choosetype">
                                <option value="1">完成选课</option>
                                <option value="0">未完成选课</option>
                            </select>
                        </div>
                        <table class="secstep-table">
                            <thead>
                            <tr>
                                <th style="width: 5%"></th>
                                <th style="width:30%;">学生姓名</th>
                                <th style="width:35%;">选课组合</th>
                                <th style="width:30%;">操作</th>
                            </tr>
                            </thead>
                            <tbody id="stuAdvContext"></tbody>
                            <script id="stuAdvTmpl" type="text/template">
                                {{~ it:value:i }}
                                <tr>
                                    <td>{{=i + 1}}</td>
                                    <td style="background:#ececec; height: 40px;">{{=value.username}}</td>
                                    <td style="height: 40px;">{{=value.advName}}</td>
                                    <td style="height: 40px;"><a href="javascript:;" class="tzxkjg-tz"
                                                                 uid="{{=value.userid}}"
                                                                 unm="{{=value.username}}">调整选课</a></td>
                                </tr>
                                {{~ }}
                            </script>
                        </table>
                    </div>
                    <!--================================第二步调整选课结果end===============================-->
                    <!--================================第二步导出结果start===============================-->
                    <div class="dcjg-con" id="tab-DCJG">
                        <div class="dcjg-btn">
                            <a href="/zouban/studentXuanke/exportResult.do?type=1&xuankeId=${xuankeId}&gradeId=${gradeId}&term=${term}"
                               class="dc-byclass">按行政班导出学生选课结果</a>
                            <a href="/zouban/studentXuanke/exportResult.do?type=2&xuankeId=${xuankeId}&gradeId=${gradeId}&term=${term}"
                               class="dc-bysubject">按学科组合导出学生选课结果</a>
                        </div>
                    </div>
                    <!--================================第二步导出结果end===============================-->
                    <div class="look-link-one">
                        <a class="m-fanhui back-xkjd">&lt;返回</a>
                        <span class="looklink-time">${term}</span>

                        <div class="select-btn">
                            <span>科目</span>
                            <select class="subjectContext" disabled>
                                <%--<option>政治</option>--%>
                            </select>
                            <script id="subjectTmpl" type="text/template">
                                {{~ it:value:i }}
                                <option value="{{=value.subjectId}}">{{=value.subjectName}}</option>
                                {{~ }}
                            </script>
                            <span>行政班</span>
                            <select class="adminClassContext">
                                <option>全部</option>
                            </select>
                            <script id="adminClassTmpl" type="text/template">
                                <option value="">全部</option>
                                {{~ it:value:i }}
                                <option value="{{=value.id}}">{{=value.className}}</option>
                                {{~ }}
                            </script>
                        </div>
                        <table class="secstep-table">
                            <thead>
                            <tr>
                                <th style="width: 5%;"></th>
                                <th style="width:20%;">课程</th>
                                <th style="width:20%;">学生姓名</th>
                                <th style="width:20%;">性别</th>
                                <th style="width:20%;">所属行政班</th>
                            </tr>
                            </thead>
                            <tbody id="stuChooseContext">
                            </tbody>
                            <script id="stuChooseTmpl" type="text/template">
                                {{~ it:value:i }}
                                <tr>
                                    <td>{{=i + 1}}</td>
                                    <td style="background:#ececec; height: 40px;">{{=value.courseName}}</td>
                                    <td style="height: 40px;">{{=value.username}}</td>
                                    <td style="height: 40px;">{{?value.sex==1}}男{{??}}女{{?}}</td>
                                    <td style="height: 40px;">{{=value.className}}</td>
                                </tr>
                                {{~ }}
                            </script>
                        </table>
                    </div>
                    <!--================================第二步查看table1 end===============================-->
                    <!--================================第二步查看table2 start===============================-->
                    <div class="look-link-two">
                        <a class="m-fanhui back-xkjd">&lt;返回</a>
                        <span class="subject-type" id="subjectGroupName">物理、化学、生物</span>
                        <table class="newTable" style="width: 300px;">
                            <thead>
                            <tr>
                                <th style="width: 8%"></th>
                                <th style="width:42%;">学生姓名</th>
                                <th style="width:50%;">行政班级</th>
                            </tr>
                            </thead>
                            <tbody id="subjectGroupStudentContext">
                            </tbody>
                            <script id="subjectGroupStudentTmpl" type="text/template">
                                {{~ it:value:i }}
                                <tr>
                                    <td>{{=i + 1}}</td>
                                    <td style="background:#ececec;">{{=value.name}}</td>
                                    <td>{{=value.value}}</td>
                                </tr>
                                {{~ }}
                            </script>
                        </table>
                    </div>
                    <!--================================第二步查看table2 end===============================-->
                </div>
            </div>
            <!--/.tab-col右侧-->
            <!--================================设置选课时间弹窗start===============================-->
            <div class="settime-alert">
                <div class="zb-set-title clearfix">
                    <p>学生选课时间</p>
                    <span class="zb-set-close">X</span>
                </div>
                <div class="settime-alert-main">
                    <label><span>开始时间</span><input class="Wdate" type="text" id="opentime"
                                                   onfocus="WdatePicker({dateFmt:'yyyy/MM/dd HH:mm'})" readonly></label>
                    <label><span>结束时间</span><input class="Wdate" type="text" id="endtime"
                                                   onfocus="WdatePicker({dateFmt:'yyyy/MM/dd HH:mm'})" readonly></label>
                </div>
                <div class="alert-btn">
                    <button class="alert-btn-sure" id="updateXuanKeTime">确定</button>
                    <button class="alert-btn-qx">取消</button>
                </div>
            </div>
            <!--================================设置选课时间弹窗end===============================-->
            <!--================================调整选课弹窗start===============================-->
            <div class="tzxk-alert">
                <div class="zb-set-title clearfix">
                    <p>调整选课</p>
                    <span class="zb-set-close">X</span>
                </div>
                <div class="tzxk-alert-main" id="xuankeResultAdminContext">
                    <span class="tzxk-name">高一(1)/蔡毅恒</span>
                    <dl>
                        <dt>已选课程</dt>
                        <dd>
                            <em>等级考课程:</em>
                            <span>政治</span>
                            <span>地理</span>
                            <span>生物</span>
                        </dd>
                        <dd>
                            <em>合格考课程:</em>
                            <span>物理</span>
                            <span>化学</span>
                            <span>历史</span>
                        </dd>
                    </dl>
                    <span class="sbuject-xg">修改课程</span>
                    <table class="newTable" style="width: 400px !important;">
                        <tr>
                            <th style="width:25%;">科目名称</th>
                            <th style="width:25%;">等级考</th>
                            <th style="width:25%;">合格考</th>
                            <th style="width:25%;">说明</th>
                        </tr>
                        <tr>
                            <td>政治</td>
                            <td><span class="m-selected">已选</span></td>
                            <td><span class="m-selecte">我选</span></td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>物理</td>
                            <td><span class="m-selected">已选</span></td>
                            <td><span class="m-selecte">我选</span></td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>地理</td>
                            <td><span class="m-selected">已选</span></td>
                            <td><span class="m-selecte">我选</span></td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>历史</td>
                            <td><span class="m-selected">已选</span></td>
                            <td><span class="m-selecte">我选</span></td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>生物</td>
                            <td><span class="m-selected">已选</span></td>
                            <td><span class="m-selecte">我选</span></td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td>化学</td>
                            <td><span class="m-selected">已选</span></td>
                            <td><span class="m-selecte">我选</span></td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                </div>

                <script id="xuankeResultAdminTmpl" type="text/template">
                    <span class="tzxk-name">{{=it.className}}/{{=it.stuName}}</span>
                    <dl>
                        <dt>已选课程</dt>
                        <dd>
                            <em>等级考课程:</em>
                            {{~ it.advChoose:value:i }}
                            <span>{{=value}}</span>
                            {{~ }}
                        </dd>
                        <dd>
                            <em>合格考课程:</em>
                            {{~ it.simChoose:value:i }}
                            <span>{{=value}}</span>
                            {{~ }}
                        </dd>
                    </dl>
                    <span class="sbuject-xg">修改课程</span>
                    <div class="table-wrap">
                        <table class="newTable" style="width: 400px !important;">
                            <tr>
                                <th style="width:34%;">科目名称</th>
                                <th style="width:33%;">等级考</th>
                                <th style="width:33%;">合格考</th>
                            </tr>
                            {{~ it.subConfList:value:i }}
                            <tr class="sub" subid="{{=value.subjectId}}">
                                <td>{{=value.subjectName}}</td>
                                {{? $.inArray(value.subjectName, it.advChoose) > -1}}
                                <td class="select"><span class="m-selected for-select">已选</span></td>
                                <td class="select"><span class="m-selecte for-select">我选</span></td>
                                {{??}}
                                <td class="select"><span class="m-selecte for-select">我选</span></td>
                                <td class="select"><span class="m-selected for-select">已选</span></td>
                                {{?}}
                            </tr>
                            {{~ }}
                        </table>
                    </div>
                </script>
                <div class="alert-btn">
                    <button class="alert-btn-sure" id="teacherXK">确定</button>
                    <button class="alert-btn-qx">取消</button>
                </div>
            </div>
            <!--================================调整选课弹窗end===============================-->
        </div>
        <!--/.col-right-->

    </div>
    <!--/#content-->
</div>
</div>
<div class="zhiban-meng"></div>
<!--#foot-->
<%@ include file="../../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js"></script>
<script>
    seajs.use('/static_new/js/modules/zouban/2.0/studentXuanKe.js');
</script>

</body>
</html>