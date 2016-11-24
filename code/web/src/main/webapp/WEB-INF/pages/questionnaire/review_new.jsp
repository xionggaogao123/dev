<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/12/1
  Time: 11:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>复兰科技-问卷调查</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <!-- jquery artZoom4Liaoba styles -->
  <!-- Custom styles -->
  <link href="/static_new/css/questionnair.css" rel="stylesheet" />
  <%--<script language="javascript" src="js/jquery-1.6.4.min.js" ></script>--%>
  <%--<script type="text/javascript" src="js/questionnair.js"></script>--%>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <%--pdf 预览 控件--%>
  <script src="/static/js/exercise/plugins/util.js"></script>
  <script src="/static/js/exercise/plugins/api.js"></script>
  <script src="/static/js/exercise/plugins/metadata.js"></script>
  <script src="/static/js/exercise/plugins/canvas.js"></script>
  <script src="/static/js/exercise/plugins/webgl.js"></script>
  <script src="/static/js/exercise/plugins/pattern_helper.js"></script>
  <script src="/static/js/exercise/plugins/font_loader.js"></script>
  <script src="/static/js/exercise/plugins/annotation_helper.js"></script>

  <%--<script type="text/javascript" src="/static/js/jquery-2.1.1.min.js"></script>--%>
  <script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash.js"></script>
  <script type="text/javascript" src="/static/plugins/flexpaper/flexpaper_flash_debug.js"></script>
  <%--<script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>--%>
  <%--<script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>--%>
  <%--<script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>--%>
  <%--<script type="text/javascript" src="/static/js/sharedpart.js"></script>--%>
</head>
<body class="newquest-body">

<!--#content-->
<div id="content" class="clearfix">
  <div class="w-cont">
    <div class="w-cont1">


      <%--<span>提交</span>--%>
      <em onclick="window.open('about:blank','_self'); window.close();">取消</em>
      <h1 id="name" title="">问卷名</h1>
    </div>
    <iframe src="" width="550" height="800" style="display:none;margin:15px 0 0 10px;"></iframe>
    <div class="configuration_main_left" id="showPdf" style="display:none;margin-top: 10px;float: left;height: 800px;width:570px!important">
      <a id="viewerPlaceHolder" style="width:645px;height:950px;display:inline" class="configuration_main_left_I" href=""></a>
    </div>
    <%--<div class="w-cont2">--%>
      <%--<iframe src="" width="570" height="800"></iframe>--%>
    <%--</div>--%>
    <div class="w-cont3">
      <ul id="list">
        <%--<li>--%>
          <%--<p>1.单选</p>--%>
          <%--<p>--%>
            <%--<input type="radio" name="radio-dx">A--%>
            <%--<input type="radio" name="radio-dx">B--%>
            <%--<input type="radio" name="radio-dx">C--%>
            <%--<input type="radio" name="radio-dx">D--%>
          <%--</p>--%>
        <%--</li>--%>
        <%--<li>--%>
          <%--<p>2.多选</p>--%>
          <%--<p>--%>
            <%--<input type="checkbox" name="checkbox-dx">A--%>
            <%--<input type="checkbox" name="checkbox-dx">B--%>
            <%--<input type="checkbox" name="checkbox-dx">C--%>
            <%--<input type="checkbox" name="checkbox-dx">D--%>

          <%--</p>--%>
        <%--</li>--%>
        <%--<li>--%>
          <%--<p>3.问答题</p>--%>
          <%--<p>--%>
            <%--<textarea></textarea>						</p>--%>
        <%--</li>--%>
        <%--<li>--%>
          <%--<p>4.打分</p>--%>
          <%--<p>分数范围：0~10--%>
            <%--<select>--%>
              <%--<option>0</option>--%>
              <%--<option>1</option>--%>
              <%--<option>2</option>--%>
              <%--<option>3</option>--%>
              <%--<option>4</option>--%>
              <%--<option>5</option>--%>
              <%--<option>6</option>--%>
              <%--<option>7</option>--%>
              <%--<option>8</option>--%>
              <%--<option>9</option>--%>
              <%--<option>10</option>--%>
            <%--</select>--%>
          <%--</p>--%>
        <%--</li>--%>

      </ul>
      <div style="clear:both"></div>
    </div>
    <div style="clear:both"></div>

  </div>
</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->

<script id="list_tmpl" type="text/template">
  {{ var all = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];}}
  {{for (var i in it){ }}
  {{? it[i]>0 && it[i]<10000 }}
  <li index="{{=i}}" ty="1">
    <p>{{=i-0+1}}.单选</p>
    <p>
      {{for(var j=0; j< it[i]; j++) { }}
      <input type="radio" name="radio-dx{{=i}}" disabled index="{{=j}}">{{=all[j]}}
      {{} }}
    </p>
  </li>
  {{?? it[i]<0 }}
  <li index="{{=i}}" ty="2">
    <p>{{=i-0+1}}.多选</p>
    <p>
      {{for(var j=0;j< 0-it[i]; j++){ }}
      <input type="checkbox" name="checkbox-dx" disabled  index="{{=j}}">{{=all[j]}}
      {{ } }}
    </p>
  </li>
  {{?? it[i]==0}}
  <li index="{{=i}}" ty="3">
    <p>{{=i-0+1}}.问答题</p>
    <p>
      <textarea readonly></textarea>
    </p>
  </li>
  {{?? it[i]>10000}}
  {{var max = it[i] % 10000;}}
  {{var min = Math.floor(it[i]/10000-10000);}}
  <li index="{{=i}}" ty="4">
    <p>{{=i-0+1}}.打分</p>
    <p>分数范围：{{=min}}~{{=max}}
      <select disabled>
        {{for(var j=min; j<=max; j++){ }}
        <option  index="{{=j}}">{{=j}}</option>
        {{ } }}
      </select>
    </p>
  </li>
  {{?}}
  {{} }}
</script>

<%--<script>--%>
  <%--function getMin(num){--%>
    <%--return num%10000;--%>
  <%--}--%>
<%--</script>--%>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/questionnaire/0.1.0/review_new');
</script>


</body>
</html>
