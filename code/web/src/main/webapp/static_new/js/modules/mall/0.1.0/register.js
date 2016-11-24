/**
 * Created by fl on 2016/2/4.
 */
define(function(require,exports,module){
    var Common = require('common');
    var login = $('body').attr('login')=='true';
    var cacheKeyId = '';
    var countdown = 60;

    (function(){
        var rObj = document.getElementById('radio-phone');
        rObj.checked =  'checked';

        $('body').on('click','.sel-phone',function(){
            $('.dd-phone').show();
            $('.dd-img').show();
            $('.dd-mail').hide();
        })

        $('body').on('click','.sel-mail',function(){
            $('.dd-mail').show();
            $('.dd-img').hide();
            $('.dd-phone').hide();
        })

        //获取手机验证码
        $('.register-HQ').click(function(){
            if(getVerificationCode()){
                settime($('.register-HQ'));
            }
        })
        //检查邮箱是否被注册
        $('#email').blur(function(){
            checkEmail();
        })
        //检查用户名
        $('#name').blur(function(){
            checkUserName();
        })
        //检查密码
        $('#pswd').blur(function(){
            checkPassWord();
            checkPassWordCopy();
        })
        //检查两次密码是否一致
        $('#pswdcopy').blur(function(){
            checkPassWordCopy();
        })
        //检查手机号码
        $('#phone').blur(function(){
            checkPhoneNumber();
        })
        //立即注册
        $('.register-LJ').click(function(){
            register();
        })

        $('body').on('click', '#close, .show', function(){
            $('.store-register').fadeToggle();
            $('.bg').fadeToggle();
        })

        $('body').on('click', '#vc', function(){
            $(this).attr('src','/verify/verifyCode.do?time=' + new Date());
        })

        $('#logIn').click(function(){
            logIn();
        })

        $('#password').keydown(function (event) {
            if (event.which == 13) {
                logIn();
            }
        });

        $('body').on('click','#successDiv',function(){
            window.location.href = '/mall/index.do';
        });

        $('#imgObj').click(function(){
            chgUrl();
        })

    })()

    function chgUrl(){
        var imgSrc = $("#imgObj");
        var url =imgSrc.attr("src");

        var timestamp = (new Date()).valueOf();
        url = url.substring(0,21);
        if((url.indexOf("&")>=0)){
            url = url + "×tamp=" + timestamp;
        }else{
            url = url + "?timestamp=" + timestamp;
        }
        imgSrc.attr("src",url);
    }

    function settime(val) {
        if (countdown == 0) {
            val.attr("disabled", false);
            val.text("点击获取");
            countdown = 60;
            return true;
        } else {
            val.attr("disabled", true);
            val.text("重新发送(" + countdown + ")");
            countdown--;
        }
        setTimeout(function() {
            settime(val)
        },1000)
    }

    function wrong(ele, i){
        ele.addClass('register-wrong').removeClass('register-right');
        $('#'+ ele.attr('id') + i).show().siblings('span').hide();
        return false;
    }

    function right(ele, i){
        ele.addClass('register-right').removeClass('register-wrong');
        $('#'+ ele.attr('id') + i).show().siblings('span').hide();
        return true;
    }

    function checkEmail(){
        var ele = $('#email');
        var email = $.trim(ele.val());
        var value=$('input[name=registway]:checked').val();
        if(!email&&value=="1"){
            ele.removeClass('register-wrong');
            $('#'+ ele.attr('id') + 1).show().siblings('span').hide();
            return true;
        }
        var pattern = /^.+@.+\..+$/;
        if(!pattern.test(email)) {
            return wrong(ele, 2);
        } else {
            var pass = false;
            Common.getData("/mall/users.do", {email:email}, function(resp){
                if(1 == resp.isExit){
                    pass = wrong(ele, 3);
                } else {
                    pass =  right(ele, 1);
                }
            })
            return pass;
        }
    }

    function checkUserName(){
        var ele = $('#name');
        var userName = $.trim(ele.val());
        if(userName == ''){
            return false;
        }
        var emailPattern = /^.+@.+\..+$/;
        if(emailPattern.test(userName)) {
            return wrong(ele,3);
        }
        var phonePattern = /^1[34578]\d{9}$/;
        if(phonePattern.test(userName)) {
            return wrong(ele,4);
        }
        if(userName.length < 6){
            return wrong(ele, 5);
        }
        if(userName.length > 20){
            return wrong(ele, 2);
        }
        if(checkName()) {
            return right(ele, 1);
        } else {
            return false;
        }
    }

    function checkPassWord(){
        var ele = $('#pswd');
        var password = $.trim(ele.val());
        if(password.length > 20 || password.length < 6){
            return wrong(ele, 3);
        } else if(password.length >= 10){
            return right(ele, 1);
        } else if(password.length < 10){
            return right(ele, 2);
        } else {
            return false;
        }
    }

    function checkName(){
        var ele = $('#name');
        var name = $.trim(ele.val());
        if(name != ''){
            var pass = false;
            Common.getData("/mall/users.do", {name:name}, function(resp){
                if(1 == resp.isExit){
                    pass = wrong(ele, 6);
                } else {
                    pass = right(ele, 1);
                }
            })
            return pass;
        }
        return true;
    }

    function checkPassWordCopy(){
        var ele = $('#pswdcopy');
        var passwordCopy = $.trim(ele.val());
        if(passwordCopy == ''){
            return false;
        }
        var password = $.trim($('#pswd').val());
        if(passwordCopy != password){
            return wrong(ele, 2);
        } else if(password == ''){
            ele.siblings('span').hide();
            return false;
        }
        ele.siblings('span').hide();
        return right(ele, 1);
    }

    function checkPhoneNumber(){
        var ele = $('#phone');
        var phone = $.trim(ele.val());
        $('#phone').removeClass();
        var value=$('input[name=registway]:checked').val();
        if(!phone&&value=="2"){
            ele.removeClass('register-wrong');
            $('#'+ ele.attr('id') + 1).show().siblings('span').hide();
            return true;
        }
        var pattern = /^1[34578]\d{9}$/;
        if(!pattern.test(phone)) {
            return wrong(ele, 2);
        } else {
            var isExit = 1;
            Common.getData("/mall/users.do", {phone:phone}, function(resp){
                isExit = resp.isExit;
            })
            if(1 == isExit){
                return wrong(ele, 3);
            } else {
                return right(ele, 1);
            }
        }
    }

    function getVerificationCode(){
        if($('#verifyCode').val() == ''){
            var ele = $('#verifyCode');
            return wrong(ele, 2);
        }
        var phone = $('#phone').val();
        var verifyCode = $('#verifyCode').val();
        if(checkPhoneNumber(phone)){
            chgUrl();
            var flag = false;
            Common.getData("/mall/users/messages.do", {mobile:phone, verifyCode:verifyCode}, function(resp){
                if(resp.code == '200'){
                    cacheKeyId = resp.cacheKeyId;
                    $('#YZ1').show();
                    flag = true;
                } else {
                    flag = false;
                    alert(resp.message);
                }

            })
            return flag;
        }
        return false;
    }

    function checkTY(){
        var ele = $('#ty');
        if(ele.prop('checked')){
            return true;
        } else {
            return wrong(ele, 2);
        }
    }


    function register(){
        var code = $.trim($('.register-YZ').val());
        var email = $.trim($('.email').val());
        var value= $('input[name=registway]:checked').val();
        if(checkUserName() && checkPassWord() && checkPassWordCopy()){
            if(value=="1"){
                if(checkPhoneNumber()&&code!=''){
                }else{
                    return ;
                }
            }else if(value=="2"){
                cacheKeyId="";
                code=" ";
                if(checkEmail()){
                }else{
                    return ;
                }
            }
            if(!checkTY()){
                return ;
            }
            $('.waitload').show();

            var requestData = {};
            requestData.cacheKeyId = cacheKeyId;
            requestData.code = code;
            requestData.email = $.trim($('#email').val());
            requestData.userName = $.trim($('#name').val());
            requestData.passWord = $.trim($('#pswd').val());
            requestData.phoneNumber = $.trim($('#phone').val());
            Common.getPostData('/mall/users.do', requestData, function(resp){
                $('.waitload').hide();
                if(resp.code == 200){
                    $('#successDiv').show();
                    setTimeout(function(){
                        window.location.href = '/mall/index.do';
                    },8000);
                }

            })
        }
    }

    function logIn(){
        var requestData = {};
        requestData.name = $.trim($('#account').val());
        requestData.pwd = $.trim($('#password').val());
        requestData.verifyCode = "";
        //if(requestData.verifyCode == ''){
        //    $('.error').hide();
        //    $('#vcEmpty').show();
        //    return false;
        //}
        Common.getPostData('/user/login.do', requestData, function(resp){
            if('200' == resp.code){
                window.location = '/mall/index.do';
            } else {
                if(resp.message == '密码错误'){
                    resp.message = "passwordError";
                }
                $('.error').hide();
                $('#'+resp.message).show();
            }
            $('#vc').prop('src','/verify/verifyCode.do?time=' + new Date());
        })
    }

});


