<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/11/25
  Time: 11:03
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
  <%--<script language="javascript" src="js/jquery-1.6.4.min.js" >--%>
  <%--</script>--%>
  <%--<script type="text/javascript" src="js/questionnair.js"></script>--%>
</head>
<body class="newquest-body">

<!--#content-->
<div id="content" class="clearfix">
  <div class="n-cont">
    <div class="n-cont1">

      <span id="publish">完成并发布</span>
      <%--<span for="file_attach">更换文件</span>--%>

      <%--<div id="attacher">--%>
        <%--<label for="file_attach1" style="cursor: pointer;">--%>
          <%--更换文件--%>
        <%--</label>--%>

        <%--<div style="width: 0; height: 0; overflow: visible">--%>
          <%--<input id="file_attach1" type="file" name="file" value="添加附件" size="1"--%>
                 <%--style="width: 0; height: 0; opacity: 0">--%>
        <%--</div>--%>
      <%--</div>--%>

      <em onclick="window.open('about:blank','_self'); window.close();">取消</em>
      <h1>新建问卷</h1>
    </div>

    <div class="n-cont2">
      <label class="n-cont2a" for="file_attach">
        <h1>上传问卷</h1><br/>
        <span>*注：文档名称将自动获取为问卷名称,支持doc或docx</span>
      </label>
      <img src="/img/loading4.gif" id="fileuploadLoading" style="display:none;"/>
      <div style="width: 0; height: 0; overflow: visible">
        <input id="file_attach" type="file" name="file" value="添加附件" size="1"
               style="width: 0; height: 0; opacity: 0">
      </div>
    </div>

    <div class="tip uploading" hidden="">
      <div class="header">提示</div>
      <div class="content">
        <i class="fa fa-spinner fa-spin"/>
        问卷上传中...
      </div>
    </div>
    <div class="tip uploaded" hidden="">
      <div class="header">提示</div>
      <div class="content">
        问卷上传成功！
      </div>
    </div>


    <%--<iframe src="/upload/exam/55b9eef22dacaf17b4fa3079.pdf" width="800" height="600"></iframe>--%>
    <div class="n-cont2" id="pdf" hidden="hidden"></div>

    <div class="n-cont3">
      <div class="n-cont3a">
        <div class="n-cont3a-1">
          问卷填写与配置
        </div>
        <div class="n-cont3a-2">
          问卷名称
          <input type="text" id="qName">
        </div>
        <div class="n-cont3a-2">
          截止日期
          <%--<input type="text">--%>
          <input class="result_col_II" type="text"  name="date" id="qDate"
                 onfocus="WdatePicker()" readonly
                 value=""/>
        </div>
        <div class="n-cont3a-3">
          <i>调查范围</i>
                        <span class="arealist" id="classanddepart">
                            <%--<span><input type="checkbox" name="classanddepart">全校</span>--%>
                            <%--<span><input type="checkbox" name="classanddepart">初三（1）</span>--%>
                            <%--<span><input type="checkbox" name="classanddepart">我的部门</span>--%>
                        </span>
          <span class="ajlist"></span>
          <i>参加人员</i>
                        <span class="joinlist">
                            <span><input type="checkbox" name="role" id="allRole">全部</span>
                            <span><input type="checkbox" name="role" id="parent">家长</span>
                            <span><input type="checkbox" name="role" id="student">学生</span>
                            <span><input type="checkbox" name="role" id="teacher">老师</span>
                        </span>
        </div>
        <div id="forcopy" index="1">
          <table class="n-cont3a-4">
            <tr>
              <td>题号</td>
              <td><i class="index">1</i><i class="idelete"></i>
                <i class="iarrow-d"></i></td>
            </tr>
            <tr>
              <td>类型</td>
              <td>
                <em class="gretd em-dax">单选</em>
                <em class="em-dux">多选</em>
                <em class="em-wd">问答</em>
                <em class="em-df">打分</em>
              </td>
            </tr>
            <tr class="tr3">
              <td>选项</td>
              <td>
                <span index="4" class="abcd">A；B；C；D；</span>
                <i class="ijian"></i>
                <i class="iadd"></i>

              </td>
            </tr>
            <tr class="tr4">
              <td>最低分</td>
              <td><input type="number" value="0" class="min"></td>
            </tr>
            <tr class="tr5">
              <td>最高分</td>
              <td><input type="number" value="10" class="max"></td>
            </tr>
          </table>
        </div>
        <%--<table class="n-cont3a-4">--%>
          <%--<tr>--%>
            <%--<td>题号</td>--%>
            <%--<td>1--%>
              <%--<i class="idelete"></i>--%>
              <%--<i class="iarrow-u" style="background: url(/images/up.png);float: right;display: inline-block;width: 18px;height: 18px;margin:11px 15px 0 0;cursor: pointer;"></i></td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td>类型</td>--%>
            <%--<td>--%>
              <%--<em class="gretd em-dax">单选</em>--%>
              <%--<em class="em-dux">多选</em>--%>
              <%--<em class="em-wd">问答</em>--%>
              <%--<em class="em-df">打分</em>--%>
            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td>选项</td>--%>
            <%--<td>--%>
              <%--<span>A；B；C；D；</span>--%>
              <%--<i class="ijian"></i>--%>
              <%--<i class="iadd"></i>--%>

            <%--</td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td>最低分</td>--%>
            <%--<td><input type="text" value="0"></td>--%>
          <%--</tr>--%>
          <%--<tr>--%>
            <%--<td>最高分</td>--%>
            <%--<td><input type="text" value="10"></td>--%>
          <%--</tr>--%>
        <%--</table>--%>


      </div>
      <div id="copy" hidden="hidden">
        <table class="n-cont3a-4">
          <tr>
            <td>题号</td>
            <td><i class="index">1</i><i class="idelete"></i>
              <i class="iarrow-d"></i></td>
          </tr>
          <tr>
            <td>类型</td>
            <td>
              <em class="gretd em-dax">单选</em>
              <em class="em-dux">多选</em>
              <em class="em-wd">问答</em>
              <em class="em-df">打分</em>
            </td>
          </tr>
          <tr class="tr3">
            <td>选项</td>
            <td>
              <span index="4" class="abcd">A；B；C；D；</span>
              <i class="ijian"></i>
              <i class="iadd"></i>

            </td>
          </tr>
          <tr class="tr4">
            <td>最低分</td>
            <td><input type="number" value="0" class="min"></td>
          </tr>
          <tr class="tr5">
            <td>最高分</td>
            <td><input type="number" value="10" class="max"></td>
          </tr>
        </table>
      </div>
      <div class="n-cont3b">
        <button class="btn-tjtm">添加题目</button>
        <button class="btn-pltj">批量添加</button>
        <div class="div-pltj">
          <div class="triangle"></div>
          <table class="tab-pltj">
            <tr>
              <td>添加个数</td>
              <td><input type="number" id="number" placeholder="点击输入题目个数" onfocus="this.placeholder=''" onblur="this.placeholder='点击输入题目个数'"></td>
            </tr>
            <div id="copypltj">
            <tr>
              <td>类型</td>
              <td>
                <em class="gretd em-dax">单选</em>
                <em class="em-dux">多选</em>
                <em class="em-wd">问答</em>
                <em class="em-df">打分</em>
              </td>
            </tr>
            <tr>
              <td>选项</td>
              <td>
                <span index="4">A；B；C；D；</span>
                <i class="ijian"></i>
                <i class="iadd"></i>

              </td>
            </tr>
            <tr>
              <td>最低分</td>
              <td><input type="number" value="0"></td>
            </tr>
            <tr>
              <td>最高分</td>
              <td><input type="number" value="10"></td>
            </tr>
            </div>
            <tr>
              <td colspan="2">
                <button id="confirm">确定</button>
              </td>
            </tr>
          </table>
          <%--<div class="div-pltj-c"></div>--%>
        </div>
      </div>
    </div>
    <div style="clear:both"></div>
  </div>
</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->


<script id="classanddepart_tmpl" type="text/template">
  <span><input type="checkbox" name="classanddepart" id="allClass">全校</span>
  {{for (var i in it){ }}
  <span><input type="checkbox" name="classanddepart" classid="{{=it[i].id}}">{{=it[i].className}}</span>
  {{} }}
</script>



<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/questionnaire/0.1.0/add_new');
</script>
<script src="/static_new/js/modules/questionnaire/0.1.0/pdfobject.js"></script>

</body>
</html>
