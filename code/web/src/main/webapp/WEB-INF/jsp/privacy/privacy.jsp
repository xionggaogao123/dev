<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=uft-8">
    <title>隐私保护承诺-复兰教育论坛</title>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="/static/css/newmain.css"/>
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js'></script>
    <script type='text/javascript' src='/static/js/newmain.js'></script>
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/ypxxhead.jsp" %>

<div class="company-container">
    <div class="error-container">
        <div class="login-bar"></div>
        <div class='title-bar-container' style="height:105px;">
            <a href="/">
                <img class="title-logo" src="/static/images/K6KT-LOGO.png">
            </a>
            <ul class="title-ul">
                <li>
                    <a href="/">
                        <div class="title-warp"></div>
                        <div class="warp-content">首页</div>
                    </a>
                </li>
                <li>
                    <a href="/aboutus/k6kt">
                        <div class="title-warp"></div>
                        <div class="warp-content">关于我们</div>
                    </a>
                </li>
                <li>
                    <a href="/contactus/k6kt">
                        <div class="title-warp"></div>
                        <div class="warp-content">联系我们</div>
                    </a>
                </li>
                <li>
                    <a href="/service/k6kt">
                        <div class="title-warp"></div>
                        <div class="warp-content">文明守则</div>
                    </a>
                </li>
                <li class="active">
                    <a href="/privacy/k6kt">
                        <div class="title-warp"></div>
                        <div class="warp-content">隐私保护承诺</div>
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <div class="company-info-container">
        <div class="info-left">
            <div class="company-info-title">隐私保护承诺</div>
            <div class="company-info-content">
                <strong>“复兰教育社区”（下称复兰）非常重视用户信息的保护，鉴于网络的特性以及本公司所为您提供服务的性质，复兰将不可避免地与您产生直接或间接的互动关系，故特此说明复兰对用户个人信息所采取的收集、使用和保护政策，请您务必仔细阅读。同时，一旦您选择使用复兰，即表示您认可并接受本条款现有内容及其可能随时更新的内容： </strong>
                <br/>( 1 )当您在“复兰教育社区”在线进行用户注册登记、使用复兰提供的服务时，在您的同意及确认下，复兰将通过注册表格等形式要求您提供一些个人资料。 
                <br/>( 2 )在未经您同意及确认之前，复兰不会将您所提供的资料利用于其它目的。 
                <br/>( 3 )复兰将对您所提供的资料进行严格的管理及保护，复兰将使用相应的技术，防止您的个人资料丢失、被盗用或遭窜改。
                <br/>( 4 )复兰在符合下列条件之一，可以对收集之个人资料进行必要范围以外之利用：
                <br/>a. 已取得您的书面同意；
                <br/>b. 为免除您在生命、身体或财产方面所面临的急迫危险；
                <br/>c. 为防止他人权益遭受重大危害；
                <br/>d. 为增进公共利益，且无害于您的重大利益；
                <br/>e. 应法院或政府相关监管机关等依法定程序提出相关要求时。
                <br/>( 5 )在复兰为您提供网络交流的服务中，您在复兰上所发布的任何信息都会成为公开的信息。因此，我们提醒并请您慎重考虑是否有必要在这些区域发布相关信息。
                <br/>( 6 )监护人应承担保护未成年人在网络环境下的隐私权的首要责任。 
                <br/>( 7 )免责声明： 对因以下原因造成的您个人信息的公开，复兰将无需承担责任：
                对因以下原因造成的您个人信息的公开，本网站将无需承担责任：
                <br/>a. 由于您将用户密码告知他人或与他人共享注册帐户或因您的任何其他故意或疏忽行为而导致的任何个人资料的泄露； 
                <br/>b. 任何由于计算机系统、黑客攻击、计算机病毒侵入或发作、第三方原因导致的软件或硬件故障、因政府管制而造成的暂时性关闭等影响网络正常经营之不可抗力而造成的个人资料泄露、丢失、被盗用或被篡改等； 
                <br/>c. 本承诺第（4）条下任一原因而导致的用户个人资料的公开。
                <br/>( 8 )复兰随时会对本隐私权保护声明进行修订。复兰会在网页中显著的位置予以发布。
            </div>
        </div>
        <div class="info-right">
            <a href="/aboutus/k6kt" class="company-info-title">关于我们</a>
            <a href="/contactus/k6kt" class="company-info-title">联系我们</a>
            <a href="/service/k6kt" class="company-info-title">文明守则</a>
            <a href="/privacy/k6kt" class="company-info-title active">隐私保护承诺</a>
        </div>
    </div>
</div>
<!-- 页尾 -->
<%@ include file="/WEB-INF/jsp/common/flippedroot.jsp" %>
<!-- 页尾 -->
</body>
</html>