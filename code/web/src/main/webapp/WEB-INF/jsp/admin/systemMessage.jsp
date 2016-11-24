<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>系统消息管理</title>
</head>
<body>
<div id="wrapper">

    <!-- Sidebar -->
    <%@include file="side_bar.jsp" %>
    <!-- /#sidebar-wrapper -->

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <div class="container">
            <div style="color:#E43838">
                警告：此页面为复兰科技内部页面，仅限复兰科技内部使用
            </div>
            <table class="table table-bordered table-hover">
                <tr>
                    <td>
                        <div>
                            <h2>发送系统消息</h2>
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
    <!-- /#page-content-wrapper -->

</div>


<link href="/static/css/side_bar.css" rel="stylesheet">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap-theme.min.css">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css">

<!-- Latest compiled and minified JavaScript -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js"></script>

<!-- (Optional) Latest compiled and minified JavaScript translation files -->
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/i18n/defaults-*.min.js"></script>

<script src="//cdn.bootcss.com/lodash.js/4.16.2/lodash.js"></script>

<link href="/static/dist/popup/x0popup.min.css" rel="stylesheet">

<script src="/static/dist/popup/x0popup.min.js"></script>

<script language="javascript" type="text/javascript">
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
</body>
</html>
