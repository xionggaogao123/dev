<%@ page language="java" session="false" contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="layout" uri="http://www.dreamlu.net/tags/jsp-layout.tld" %>
<%-- 填充head --%>
<layout:override name="head">
    <title>复兰后台管理</title>
</layout:override>
<%-- 填充content --%>
<layout:override name="content">
    <div id="page-content-wrapper">
        <div class="container">
            <div style="color:#E43838">
                警告：此页面为复兰科技内部页面，仅限复兰科技内部使用
            </div>
            <table class="table table-bordered table-hover">
                <tr>
                    <td>
                        <div>
                            <h2>极光推送</h2>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div>
                            <select id="select" title="选择用户" class="selectpicker">
                                <option value="1">全部用户</option>
                                <option value="2">指定用户</option>
                            </select>
                        </div>
                    </td>
                </tr>
                <tr hidden id="userNameDiv">
                    <td>
                        <div>
                            <div class="alert alert-warning alert-dismissible" role="alert">
                                <button type="button" class="close" data-dismiss="alert"><span
                                        aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                <strong>Warning!</strong>多个用户用#分隔
                            </div>
                            <div class="input-group">
                                <span class="input-group-addon">@</span>
                                <input type="text" class="form-control" placeholder="Username" id="userName">
                            </div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div>
                            <textarea id="text" class="form-control" rows="4"></textarea>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div>
                            <button id="send" class="btn btn-primary">发送</button>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</layout:override>
<%-- 填充script --%>
<layout:override name="script">
    <link href="/static/dist/tipbox/mdialog.css">
    <script src="/static/dist/tipbox/zepto.min.js"></script>
    <script type="text/javascript" src="/static/dist/tipbox/mdialog.js"></script>
    <script>
        var selectNum = 1;
        $(document).ready(function () {
            $('#select').change(function () {
                selectNum = $(this).children('option:selected').val();
                if (selectNum == 2) {
                    $('#userNameDiv').show();
                } else {
                    $('#userNameDiv').hide();
                }
            });

            $('#send').click(function () {
                var message = _.trim($('#text').val());

                //成功
//                new TipBox({type:'success',str:'操作成功',hasBtn:true});

                if (_.isEmpty(message)) {
                    x0p('Message', '消息为空');
                    return;
                }
                x0p('Confirmation', '确定发送吗？', 'warning', function (button, text) {
                    if (button == 'warning') {
                        var data = {};
                        if (selectNum == 2) {
                            data.userName = $('#userName').val();
                        }
                        data.message = $('#text').val();
                        data.type = selectNum;
                        $.ajax({
                            url: "/admin/SystemMsgManage.do",
                            data: data,
                            type: 'post',
                            success: function (data) {
                                alert(data.message);
                            },
                            error: function () {
                                alert('异常');
                            }
                        });
                    }
                });

            });
        });
    </script>
</layout:override>
<%@ include file="_layout.jsp" %>
