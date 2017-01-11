<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>任务列表</title>
    <link rel="stylesheet" type="text/css" href="/static/css/forum/forum.css">
    <%--<link rel="stylesheet" type="text/css" href="/static/css/main.css"/>--%>
    <link href="/static/css/forum/award.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="/static/js/modules/forum/jquery-1.11.1.js"></script>
    <script type="text/javascript" src="/static/js/modules/forum/award/awardRotate.js"></script>

    <script type="text/javascript">
        var turnplate = {
            restaraunts: [],				//大转盘奖品名称
            colors: [],					//大转盘奖品区块对应背景颜色
            outsideRadius: 192,			//大转盘外圆的半径
            textRadius: 155,				//大转盘奖品位置距离圆心的距离
            insideRadius: 68,			//大转盘内圆的半径
            startAngle: 0,				//开始角度

            bRotate: false				//false:停止;ture:旋转
        };


        $(document).ready(function () {
            //动态添加大转盘的奖品与奖品区域背景颜色
            turnplate.restaraunts = ["好心情", "好心情", "5点经验值", "5点经验值", "5点经验值", "10点经验值", "10点经验值 ", "20点经验值", "100点经验值", "100经验值翻倍"];
            turnplate.colors = ["#FFF4D6", "#FFFFFF", "#FFF4D6", "#FFFFFF", "#FFF4D6", "#FFFFFF", "#FFF4D6", "#FFFFFF", "#FFF4D6", "#FFFFFF"];


            var rotateTimeOut = function () {
                $('#wheelcanvas').rotate({
                    angle: 0,
                    animateTo: 2160,
                    duration: 8000,
                    callback: function () {
                        alert('网络超时，请检查您的网络设置！');
                    }
                });
            };


            //旋转转盘 item:奖品位置; txt：提示语;
            var rotateFn = function (item, txt) {
                var angles = item * (360 / turnplate.restaraunts.length) - (360 / (turnplate.restaraunts.length * 2));
                if (angles < 270) {
                    angles = 270 - angles;
                } else {
                    angles = 360 - angles + 270;
                }
                $('#wheelcanvas').stopRotate();
                $('#wheelcanvas').rotate({
                    angle: 0,
                    animateTo: angles + 1800,
                    duration: 8000,
                    callback: function () {


                        var param = {};
                        if (txt == "好心情") {

                            $('#yu').fadeOut();
                            $('.monkey-sad').fadeIn();
                            param.count = 0;
                        } else if (txt == "5点经验值") {
                            $('#kkl').html("5");
                            $('#yu').fadeOut();
                            $('.monkey-hap').fadeIn();
                            param.count = 5;
                        } else if (txt == "10点经验值") {
                            $('#kkl').html("10");
                            $('#yu').fadeOut();
                            $('.monkey-hap').fadeIn();
                            param.count = 10;
                        } else if (txt == "20点经验值") {
                            $('#kkl').html("20");
                            $('#yu').fadeOut();
                            $('.monkey-hap').fadeIn();
                            param.count = 20;
                        } else if (txt == "100点经验值") {
                            $('#kkl').html("100");
                            $('#yu').fadeOut();
                            $('.monkey-hap').fadeIn();
                            param.count = 100;
                        } else if (txt = "100经验值翻倍") {
                            $('#kkl').html("200");
                            $('#yu').fadeOut();
                            $('.monkey-hap').fadeIn();
                            param.count = 200;
                        }
                        turnplate.bRotate = !turnplate.bRotate;
                        $.ajax({
                            type: "post",
                            data: param,
                            url: '/forum/userCenter/welfare.do',
                            async: false,
                            dataType: "json",
                            traditional: true,
                            success: function (result) {
                                if (result.code == "200") {

                                } else {
                                    alert(result.message);
                                }

                            }
                        });
                    }
                });
            };

            $('body').on('click', '.pointer', function () {
                if (turnplate.bRotate)return;
                turnplate.bRotate = !turnplate.bRotate;
                //获取随机数(奖品个数范围内)
                var item = rnd(1, turnplate.restaraunts.length);
                //奖品数量等于10,指针落在对应奖品区域的中心角度[252, 216, 180, 144, 108, 72, 36, 360, 324, 288]
                rotateFn(item, turnplate.restaraunts[item - 1]);
                console.log(item);
            });


        });

        function rnd(n, m) {
            var random = Math.floor(Math.random() * (m - n + 1) + n);
            return random;

        }

        //页面所有元素加载完毕后执行drawRouletteWheel()方法对转盘进行渲染
        window.onload = function () {
            drawRouletteWheel();
        };

        function drawRouletteWheel() {
            var canvas = document.getElementById("wheelcanvas");
            if (canvas.getContext) {
                //根据奖品个数计算圆周角度
                var arc = Math.PI / (turnplate.restaraunts.length / 2);
                var ctx = canvas.getContext("2d");
                //在给定矩形内清空一个矩形
                ctx.clearRect(0, 0, 422, 422);
                //strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式
                ctx.strokeStyle = "#FFBE04";
                //font 属性设置或返回画布上文本内容的当前字体属性
                ctx.font = '16px Microsoft YaHei';
                for (var i = 0; i < turnplate.restaraunts.length; i++) {
                    var angle = turnplate.startAngle + i * arc;
                    ctx.fillStyle = turnplate.colors[i];
                    ctx.beginPath();
                    //arc(x,y,r,起始角,结束角,绘制方向) 方法创建弧/曲线（用于创建圆或部分圆）
                    ctx.arc(211, 211, turnplate.outsideRadius, angle, angle + arc, false);
                    ctx.arc(211, 211, turnplate.insideRadius, angle + arc, angle, true);
                    ctx.stroke();
                    ctx.fill();
                    //锁画布(为了保存之前的画布状态)
                    ctx.save();

                    //----绘制奖品开始----
                    ctx.fillStyle = "#E5302F";
                    var text = turnplate.restaraunts[i];
                    var line_height = 17;
                    //translate方法重新映射画布上的 (0,0) 位置
                    ctx.translate(211 + Math.cos(angle + arc / 2) * turnplate.textRadius, 211 + Math.sin(angle + arc / 2) * turnplate.textRadius);

                    //rotate方法旋转当前的绘图
                    ctx.rotate(angle + arc / 2 + Math.PI / 2);

                    /** 下面代码根据奖品类型、奖品名称长度渲染不同效果，如字体、颜色、图片效果。(具体根据实际情况改变) **/
                    if (text.indexOf("M") > 0) {//流量包
                        var texts = text.split("M");
                        for (var j = 0; j < texts.length; j++) {
                            ctx.font = j == 0 ? 'bold 20px Microsoft YaHei' : '16px Microsoft YaHei';
                            if (j == 0) {
                                ctx.fillText(texts[j] + "M", -ctx.measureText(texts[j] + "M").width / 2, j * line_height);
                            } else {
                                ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
                            }
                        }
                    } else if (text.indexOf("M") == -1 && text.length > 6) {//奖品名称长度超过一定范围
                        text = text.substring(0, 6) + "||" + text.substring(6);
                        var texts = text.split("||");
                        for (var j = 0; j < texts.length; j++) {
                            ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
                        }
                    } else {
                        //在画布上绘制填色的文本。文本的默认颜色是黑色
                        //measureText()方法返回包含一个对象，该对象包含以像素计的指定字体宽度
                        ctx.fillText(text, -ctx.measureText(text).width / 2, 0);
                    }

                    //添加对应图标
                    if (text.indexOf("闪币") > 0) {
                        var img = document.getElementById("shan-img");
                        img.onload = function () {
                            ctx.drawImage(img, -15, 10);
                        };
                        ctx.drawImage(img, -15, 10);
                    } else if (text.indexOf("谢谢参与") >= 0) {
                        var img = document.getElementById("sorry-img");
                        img.onload = function () {
                            ctx.drawImage(img, -15, 10);
                        };
                        ctx.drawImage(img, -15, 10);
                    }
                    //把当前画布返回（调整）到上一个save()状态之前
                    ctx.restore();
                    //----绘制奖品结束----
                }
            }
        }
    </script>
</head>
<body style="background: #f7f7f7">
<%--<%@ include file="head.jsp" %>--%>
<div id="yu" style="display: none;">
    <img src="/static/images/forum/1.png" id="shan-img" style="display:none;"/>
    <img src="/static/images/forum/2.png" id="sorry-img" style="display:none;"/>
    <div class="award-x">×</div>

    <div class="banner-f">
        <div class="banner">
            <div class="turnplate"
                 style="background-image:url(/static/images/forum/turnplate-bg.png);background-size:100% 100%;">
                <canvas class="item" id="wheelcanvas" width="422px" height="422px"></canvas>
                <img class="pointer" src="/static/images/forum/turnplate-pointer.png"/>
            </div>
        </div>
    </div>
</div>
<div class="monkey-hap" style="display: none">
    中奖啦！<br>恭喜你中<i><span id="kkl"></span></i>点经验值
    <span class="monkey-x"></span>
</div>
<div class="monkey-sad" style="display: none">
    获得好心情一枚，<br>再接再厉哟！
    <span class="monkey-x"></span>
</div>
<div class="bg" style="z-index:9"></div>
<input id="postValue" type="hidden" value="${post}">
<input id="welfareValue" type="hidden" value="${welfare}">
<input id="signInValue" type="hidden" value="${signIn}">
<input id="imf" type="hidden" value="${imf}">
<div class="container">
    <div class="mission-top">
        <h1>任务列表</h1>
        <p>放松时刻，来做个任务吧！任务可以帮助你更快的熟悉论坛，同时这里也是获得积分、升级的快速通道。</p>
    </div>
    <div class="mission-list">
        <div class="mission-li clearfix">
            <img src="/static/images/forum/img_mission_flower.png">
            <h2>每日签到</h2>
            <p>签到获得1个经验值和1个积分，连续签到会得到更多的经验值与积分奖励哦！</p>
            <button class="btn-gray" id="sign"><span id="span1">每日签到</span></button>
        </div>
        <div class="mission-li clearfix">
            <img src="/static/images/forum/img_mission_golden.png">
            <h2>每日发帖/回帖赢经验</h2>
            <p>今天顺利吗？跟大家分享一下属于你的故事吧！分享完毕请记得回来领取活动币奖励哦！</p>
            <button class="btn-gray" id="post"><span id="span2">领取任务</span></button>
        </div>
        <div class="mission-li clearfix">
            <img src="/static/images/forum/img_mission_person.png">
            <h2>设置头像</h2>
            <p>上传个人头像，即可获得10经验和10积分奖励</p>
            <button class="btn-gray" id="head"><span id="span3">领取任务</span></button>
        </div>
        <div class="mission-li clearfix">
            <img src="/static/images/forum/img_mission_gift.png">
            <h2>福利</h2>
            <p>社区福利上线，每日完成此任务有机会获得积分奖励！</p>
            <button class="btn-gray" id="welfare"><span id="span4">领取任务</span></button>
        </div>
    </div>
    <div class="mission-notice" id="closedDialog" style="display:none">
        <p>提示 <i>×</i></p>
        <div><a id="gtoPost">前往论坛版块</a></div>
    </div>
</div>
<%--<%@ include file="../mall/footer.jsp" %>--%>
<%--<%@ include file="login.jsp" %>--%>
<script type="text/javascript">
    var post = $('#postValue').val();
    var welfare = $('#welfareValue').val();
    var signIn = $('#signInValue').val();
    var imf = $('#imf').val();
    $('body').on('click', '.monkey-x', function () {
        $(this).parent().fadeOut();
        $('.bg').fadeOut();
        location.href = "/forum/task.do";
    });
    $('body').on('click', '.award-x', function () {
        $('#yu').fadeOut();
        $('.bg').fadeOut();
        location.href = "/forum/task.do";
    })
    $(document).ready(function () {

        $('.mission-notice p i').click(function () {
            $('#closedDialog').hide();
        });

        $('#gtoPost').click(function () {
            location.href = "/forum";
        });

        if (signIn == "true") {
            $('#sign').addClass('btn-pass').removeClass('btn-gray');
            $('#span1').html('已签到');
        } else {
            $('body').on('click', '#sign', function () {
                  //签到
                $.ajax({
                    type: "post",
                    data: {},
                    url: '/forum/userCenter/signIn.do',
                    async: false,
                    dataType: "json",
                    traditional: true,
                    success: function (rep) {
                        if (rep.code == "200") {
                            window.location.href = window.location.href;
                        } else {
                            alert(rep.message);
                        }
                    }
                });
//                $('#closedDialog').show();
            })
        }

        if (post == "true") {
            $('#post').addClass('btn-pass').removeClass('btn-gray');
            $('#span2').html('任务完成');
        } else {
            $('body').on('click', '#post', function () {
                $('#closedDialog').show();
            })
        }

        if (imf == "true") {
            $('#head').addClass('btn-pass').removeClass('btn-gray');
            $('#span3').html('任务完成');
        } else {
            $('body').on('click', '#head', function () {
                location.href = "/forum/userCenter/user.do";
            })
        }

        if (welfare == "true") {
            $('#welfare').addClass('btn-pass').removeClass('btn-gray');
            $('#span4').html('任务完成');
        } else if (welfare == "welfare") {
            $('#welfare').addClass('btn-pass').removeClass('btn-gray');
            $('#span4').html('先签到！');
        } else if (welfare == "welC") {
            $('#welfare').addClass('btn-pass').removeClass('btn-gray');
            $('#span4').html('今天不能抽奖！');
        } else {

            $('body').on('click', '#welfare', function () {
                //window.open('/forum/userCenter/award.do');
                $('#yu').show();
                $('.bg').show();
            });
        }
    })
</script>

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<%--<script src="/static/js/sea.js"></script>--%>
<%--<!-- Custom js -->--%>
<%--<script src="/static/js/modules/core/0.1.0/config.js?v=2015041602"></script>--%>
<%--<script>--%>
<%--seajs.use('/static/js/modules/forum/forumTask.js');--%>
<%--</script>--%>
</body>
</html>
