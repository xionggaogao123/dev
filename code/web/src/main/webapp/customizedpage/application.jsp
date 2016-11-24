
<!DOCTYPE html>
<html>
<head>
    <%@ page language="java" contentType="text/html; charset=UTF-8"
             pageEncoding="UTF-8"%>
    <link rel="stylesheet" href="/static/css/application.css" type="text/css">
    <title>复兰科技-教师申请试用</title>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script>
       
        function checkform(){
            //正则校验
            var rePhone = /^1\d{10}$/;
            var reEmail= /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
            var reCount=/^\d{1,5}$/;

            var userName=$("#userName").val();
            var sex=$("#sex").val();
            var cellPhoneNumber=$("#cellPhoneNumber").val();
            var email=$("#email").val();
            var address=$("#address").val();
            var schoolName=$("#schoolName").val();
            var grade=$("#grade").val();
            var clazz=$("#clazz").val();
            var subject=$("#subject").val();
            var stuNumber=$("#stuNumber").val();
            var teacherCount=$("#teacherCount").val();
            var knowFromWhere=$("#knowFromWhere").val();

            if(userName==null ||$.trim(userName)==""){
                alert("请输入用户名");
                return;
            }
            	
            if(sex==null || $.trim(sex)==""){
                alert("请输入性别");
                return;
            } 
            	
            if(sex!="男" && sex!="女"){
                alert("性别只能为'男'或者'女'");
                return ;
            }
            
            if(cellPhoneNumber==null || $.trim(cellPhoneNumber)==""){
                alert("请输入手机号");
                return ;
            }
            
            if(!rePhone.test(cellPhoneNumber)){
                alert("手机号不合法");
                return ;
            }
            
            
             if(email==null || $.trim(email)==""){
                alert("请输入邮箱");
                return ;
            }
            
            
            
             if(!reEmail.test(email)){
                alert("邮箱不合法");
                return ;
            }
            
             if(address==null || $.trim(address)==""){
                alert("请输入地址");
                return ;
            }
            
             if(schoolName==null || $.trim(schoolName)==""){
                alert("请输入学校名称");
                return ;
            }
            
            
             if(grade==null || $.trim(grade)==""){
                alert("请输入年级");
                return ;
            }
            
            
             if(clazz==null || $.trim(clazz)==""){
                alert("请输入班级");
                return ;
            }
            
            
             if(subject==null || $.trim(subject)==""){
                alert("请输入老师所带科目");
                return ;
            }
            
            
             if(stuNumber==null || $.trim(stuNumber)==""){
                alert("请输入学生账号人数");
                return ;
            }
            
             if(!reCount.test(stuNumber)){
                alert("学生账号人数不合法");
                return ;
            }
            
             if(teacherCount==null || $.trim(teacherCount)==""){
                alert("请输入老师账号人数");
                return ;
            }
            
             if(!reCount.test(teacherCount)){
                alert("教师账号人数不合法");
                return ;
            }
            
             if(knowFromWhere==null || $.trim(knowFromWhere)==""){
                alert("请输入从何处了解到我们k6kt");
                return ;
            }
           
            alert("您的申请已成功提交，我们的工作人员会尽快与您联系。");
            document.getElementById("teacherfrom").submit();
        }
    </script>
</head>
<body>
<div class="all">
    <!--==============================头部==========================-->
    <div class="teacher_main_title">
        <div class="teacher_main_title_center">
            <div class="teacher_main_title_center_left">
                <img src="../../img/application/Mb.png">
                <span class="">400-820-6735</span>
            </div>
            <div class="teacher_main_title_center_right">
                <div class="teacher_main_title_center_right_I">
                    <div>
                        <span>关注我们：</span>
                        <a  href="http://weibo.com/FulaanTechnology">
                            <img src="../../img/application/WEIBO.png">
                        </a>
                        <a href="http://t.qq.com/FulaanTechnology">
                            <img src="../../img/application/WEIBO2.png">
                        </a>
                        <a>
                            <img src="../../img/application/weixin.png">
                        </a>
                    </div>
                </div>
                <div class="teacher_main_title_center_right_II">
                    <a href="/mobile">
                        <span>手机客户端下载</span>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <!--==============================中间图片=======================-->
    <div class="teacher_main_info">
        <span class="teacher_main_info_center">教师申请试用</span>
        <div  class="teacher_main_info_bottom">
        </div>
        <div class="teacher_main_info_bottom_info">
            <ul>
                <li>
                    <img src="/img/application/teacher_IM_03.png">
                    <dl>
                        <dt>
                        <div> 快速</div>
                        <div>全校所有师生账号一天内开通，</div>
                        <div>登陆实线全部功能</div>
                        </dt>
                    </dl>
                </li>
                <li>
                    <img src="/img/application/teacher_IM_05.png">
                    <dl>
                        <dt>
                        <div> 便捷</div>
                        <div>学校硬件零投入，</div>
                        <div>无需学校派专人维护</div>
                        </dt>
                    </dl>
                </li>
                <li>
                    <img src="/img/application/teacher_IM_05.png">
                    <dl>
                        <dt>
                        <div> 安全</div>
                        <div>实名制社区，必须本校成员</div>
                        <div>才能登陆查看和发布内容</div>
                        </dt>
                    </dl>
                </li>
                <li  style="border-right: none">
                    <img src="/img/application/teacher_IM_05.png">
                    <dl>
                        <dt>
                        <div> 绿色</div>
                        <div>展示校园文化，提高教学效率</div>
                        <div>促进家校互动</div>
                        </dt>
                    </dl>
                </li>
            </ul>
        </div>

    </div>
    <!--=======================表单=======================-->
    <div class="teacher_T">
        <form  id="teacherfrom" action="/user/application/form.do" method="post">
        <div class="teacher_main">
            <!--=======================表单左边=======================-->
            <div class="teacher_main_I">
                填写表单项（都是必填项）：
            </div>
            <div class="teacher_main_I_LT">
                <dl>
                    <dt>姓名：</dt>
                    <dd>
                        <input name="userName" id="userName">
                    </dd>
                </dl>
                <dl>
                    <dt>性别（男，女）：</dt>
                    <dd>
                        <input name="sex" id="sex"  >
                    </dd>
                </dl>
                <dl>
                    <dt>联系手机：</dt>
                    <dd>
                        <input name="cellPhoneNumber" id="cellPhoneNumber">
                    </dd>
                </dl>
                <dl>
                    <dt>邮件地址（建议QQ邮箱）</dt>
                    <dd>
                        <input name="email" id="email">
                    </dd>
                </dl>
                <dl>
                    <dt>省份城市：</dt>
                    <dd>
                        <input name="address" id="address">
                    </dd>
                </dl>
                <dl>
                    <dt>学校名称：</dt>
                    <dd>
                        <input name="schoolName" id="schoolName">
                    </dd>
                </dl>
            </div>
            <!--=======================表单右边=======================-->
            <div class="teacher_main_I_RT">
                <dl>
                    <dt>年级：</dt>
                    <dd>
                        <input name="grade" id="grade">
                    </dd>
                </dl>
                <dl>
                    <dt>班级：</dt>
                    <dd>
                        <input name="clazz" id="clazz">
                    </dd>
                </dl>
                <dl>
                    <dt>本人所带科目：</dt>
                    <dd>
                        <input name="subject" id="subject">
                    </dd>
                </dl>
                <dl>
                    <dt>申请学生账号人数：</dt>
                    <dd>
                        <input name="stuNumber" id="stuNumber">
                    </dd>
                </dl>
                <dl>
                    <dt>申请教师账号人数：</dt>
                    <dd>
                        <input name="teacherCount" id="teacherCount">
                    </dd>
                </dl>
                <dl>
                    <dt>从何处了解到K6KT翻转课堂？</dt>
                    <dd>
                        <textarea name="knowFromWhere" id="knowFromWhere"></textarea>
                    </dd>
                </dl>
            </div>
            <div onclick="checkform()" class="teacher_main_I_RT_BO"><a style="color: black" href="javascript:void(0)">提交</a></div>
        </div>
        </form>
    </div>
</div>
<!--=================================底部==================================-->
<div id="footer">

    <div id="footer-w">
        <span style="float: left">版权所有：上海复兰信息科技有限公司          <a href="http://www.fulaan-tech.com" target="_blank">www.fulaan-tech.com</a>        沪ICP备14004857号</span>
        <span style="float: right"><a href="/aboutus/k6kt">关于我们</a>  |  <a href="/contactus/k6kt">联系我们</a>   |  <a href="/service/k6kt">服务条款 </a> |  <a href="/privacy/k6kt">隐私保护 </a> |  <a href='http://wpa.qq.com/msgrd?v=1&uin=2803728882&site=qq&menu=yes' target="_blank">
            <img src="../../img/application/QQService.png"></a></span>
    </div>
</div>
</body>
</html>