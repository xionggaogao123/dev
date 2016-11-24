<%--
  Created by IntelliJ IDEA.
  User: fl
  Date: 2015/12/24
  Time: 9:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>题库</title>
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
  <link href="/static_new/css/tiku.css" rel="stylesheet" />
  <link href="/static_new/js/modules/ueditor/themes/default/css/ueditor.css" rel="stylesheet" />

  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script type="text/javascript">
    var ids = "${ids}";
    var show = ${param.show};
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
  <!--/.col-left-->
  <!-- 广告部分 -->
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>
  <!-- 广告部分 -->
  <!--.col-right-->
  <div class="col-right">

    <div class="e-contain">
      <div class="e-cont1">
        <span>教师题库</span> > <span id="tm">上传题目</span>
      </div>
      <div class="e-cont12">
        <dl>
          <dd class="dd1">题目序号</dd>
          <dd>
            <button class="btn-add">添加题目</button>
            <button class="btn-del">删除题目</button>
            <button class="btn-wc">完成并上传</button>
          </dd>
          <dd>
            <table class="e-contab">
              <%--<tr class="tr1">--%>
                <%--<td i="0">题号</td>--%>
                <%--<td i="1">一</td>--%>
                <%--<td i="2" class="td-bg">二</td>--%>
                <%--<td i="3">三</td>--%>
                <%--<td i="4">四</td>--%>
                <%--<td i="5">五</td>--%>
                <%--<td i="6">六</td>--%>
                <%--<td i="7">七</td>--%>
                <%--<td i="8">八</td>--%>
                <%--<td i="9">九</td>--%>
              <%--</tr>--%>
              <%--<tr class="tr2">--%>
                <%--<td i="0">完成</td>--%>
                <%--<td i="1" class="gou"></td>--%>
                <%--<td i="2" class="td-bg"></td>--%>
                <%--<td i="3"></td>--%>
                <%--<td i="4"></td>--%>
                <%--<td i="5"></td>--%>
                <%--<td i="6"></td>--%>
                <%--<td i="7"></td>--%>
                <%--<td i="8"></td>--%>
                <%--<td i="9"></td>--%>
              <%--</tr>--%>
            </table>
          </dd>
        </dl>
      </div>
      <div class="e-cont2">
        <span>选择题型</span>
        <select id="quesType">
          <option value="0">--请选择--</option>
          <option value="1">选择题</option>
          <option value="3">判断题</option>
          <option value="4">填空题</option>
          <option value="5">主观题</option>
        </select>
        <span class="e-cont2p">难易程度</span>
        <select class="e-cont2s" id="level">
          <option value="0">--请选择--</option>
          <option value="1">容易</option>
          <option value="2">较容易</option>
          <option value="3">中等</option>
          <option value="4">较难</option>
          <option value="5">难</option>
        </select>
        <span>分值</span>
        <input type="number" id="score" min="1" placeholder="只能填写数字">
      </div>
      <div id="paste">
        <div class="e-cont3 clearfix">
          <div class="e-cont3-1">题目分类属性一
            <span>同步教材和综合知识点都需同时填写</span>
          </div>
          <div class="e-cont3-2">
            <dl>
              <dd class="e-dd1">同步教材版本</dd>
              <dd>
                <span>学段</span>
                <select class="versionTermTypeSelection" subClass="versionSubjectSelection" typeInt="1">
                  <option value="">--请选择--</option>
                  <option value="55d41e47e0b064452581269a">小学</option>
                  <option value="55d41e47e0b064452581269c">初中</option>
                  <option value="55d41e47e0b064452581269e">高中</option>
                </select>
              </dd>
              <dd>
                <span>学科</span>
                <select class="versionSubjectSelection" subClass="bookVertionSelection" typeInt="2">
                <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>教材版本</span>
                <select class="bookVertionSelection" subClass="gradeSelection" typeInt="3">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>年级/课本</span>
                <select class="gradeSelection" subClass="chapterSelection" typeInt="4">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>章</span>
                <select class="chapterSelection" subClass="partSelection" typeInt="5">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>节</span>
                <select class="partSelection" subClass="0" typeInt="6">
                  <option value="">--请选择--</option>
                </select>
              </dd>
            </dl>
          </div>
          <div class="e-cont3-2">
            <dl>
              <dd class="e-dd1">综合知识点</dd>
              <dd>
                <span>学段</span>
                <select class="knowledgeTermtypeSelection" subClass="knowledgeSubjectSelection" typeInt="1">
                  <option value="">--请选择--</option>
                  <option value="55d41e47e0b064452581269a">小学</option>
                  <option value="55d41e47e0b064452581269c">初中</option>
                  <option value="55d41e47e0b064452581269e">高中</option>
                </select>
              </dd>
              <dd>
                <span>学科</span>
                <select class="knowledgeSubjectSelection" subClass="knowledgeAreaSelection" typeInt="2">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>知识面</span>
                <select class="knowledgeAreaSelection" subClass="knowledgePointSelection" typeInt="7">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>知识点</span>
                <select class="knowledgePointSelection" subClass="littleknowledgePointSelection" typeInt="8">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>小知识点</span>
                <select class="littleknowledgePointSelection" subClass="0" typeInt="9">
                  <option value="">--请选择--</option>
                </select>
              </dd>
            </dl>
          </div>
        </div>
      </div>
      <div id="copy" hidden>
        <div class="e-cont3 clearfix">
          <div class="e-cont3-1">
            <l id="pi">题目分类属性二</l>
            <span>同步教材和综合知识点都需同时填写<em class="delProp">删除</em></span>
          </div>
          <div class="e-cont3-2">
            <dl>
              <dd class="e-dd1">同步教材版本</dd>
              <dd>
                <span>学段</span>
                <select class="versionTermTypeSelection" subClass="versionSubjectSelection" typeInt="1">
                  <option value="">--请选择--</option>
                  <option value="55d41e47e0b064452581269a">小学</option>
                  <option value="55d41e47e0b064452581269c">初中</option>
                  <option value="55d41e47e0b064452581269e">高中</option>
                </select>
              </dd>
              <dd>
                <span>学科</span>
                <select class="versionSubjectSelection" subClass="bookVertionSelection" typeInt="2">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>教材版本</span>
                <select class="bookVertionSelection" subClass="gradeSelection" typeInt="3">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>年级/课本</span>
                <select class="gradeSelection" subClass="chapterSelection" typeInt="4">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>章</span>
                <select class="chapterSelection" subClass="partSelection" typeInt="5">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>节</span>
                <select class="partSelection" subClass="0" typeInt="6">
                  <option value="">--请选择--</option>
                </select>
              </dd>
            </dl>
          </div>
          <div class="e-cont3-2">
            <dl>
              <dd class="e-dd1">综合知识点</dd>
              <dd>
                <span>学段</span>
                <select class="knowledgeTermtypeSelection" subClass="knowledgeSubjectSelection" typeInt="1">
                  <option value="">--请选择--</option>
                  <option value="55d41e47e0b064452581269a">小学</option>
                  <option value="55d41e47e0b064452581269c">初中</option>
                  <option value="55d41e47e0b064452581269e">高中</option>
                </select>
              </dd>
              <dd>
                <span>学科</span>
                <select class="knowledgeSubjectSelection" subClass="knowledgeAreaSelection" typeInt="2">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>知识面</span>
                <select class="knowledgeAreaSelection" subClass="knowledgePointSelection" typeInt="7">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>知识点</span>
                <select class="knowledgePointSelection" subClass="littleknowledgePointSelection" typeInt="8">
                  <option value="">--请选择--</option>
                </select>
              </dd>
              <dd>
                <span>小知识点</span>
                <select class="littleknowledgePointSelection" subClass="0" typeInt="9">
                  <option value="">--请选择--</option>
                </select>
              </dd>
            </dl>
          </div>
        </div>
      </div>
      <div class="e-cont4" id="addProp">+增加题目分类属性</div>
      <div class="e-cont5">
        <div>题目内容</div>
        <div id="question">
          <script type="text/plain" id="myEditor"></script>
          <%--<textarea id="myEditor"></textarea>--%>
        </div>
      </div>

      <!--选择题答案-->
      <div class="e-cont5 ans ans1" hidden>
        <div class="clearfix">正确答案<button class="xz-add">增加答案</button></div>
        <div id="xzPaste">
          <div class="an-xzt" num="4">
            <d>
            <label><input type="checkbox" idx="0" value="A">A</label>
            <label><input type="checkbox" idx="1" value="B">B</label>
            <label><input type="checkbox" idx="2" value="C">C</label>
            <label><input type="checkbox" idx="3" value="D">D</label>
            </d>
            <span class="add"></span><span class="cut"></span>
          </div>
        </div>
        <div id="xzCopy" hidden>
          <div class="copy">
            <div class="an-xzt" style="width:700px;" num="4">
              <d>
                <label><input type="checkbox" idx="0" value="A">A</label>
                <label><input type="checkbox" idx="1" value="B">B</label>
                <label><input type="checkbox" idx="2" value="C">C</label>
                <label><input type="checkbox" idx="3" value="D">D</label>
              </d>
              <span class="add"></span><span class="cut"></span>
            </div>
            <span class="del"></span>
          </div>
        </div>

      </div>
      <!--选择题答案-->

      <!--填空题答案-->
      <div class="e-cont5 ans ans4" hidden>
        <div class="clearfix">正确答案<button class="tk-add">增加答案</button>
        </div>
        <div id="tkPaste">
          <div>
            <input type="text" class="tk-input">
          </div>
        </div>
        <div id="tkCopy" hidden>
          <div class="copy">
            <input type="text" class="tk-input1">
            <span class="del"></span>
          </div>
        </div>
      </div>
      <!--填空题答案-->

      <!--判断题答案-->
      <div class="e-cont5 ans ans3" hidden>
        <div class="clearfix">正确答案<button class="pd-add">增加答案</button>
        </div>
        <div id="pdPaste">
          <div>
          <span class="pd-pd">
              <label><input type="radio" name="input-pd" value="对">对</label>
              <label><input type="radio" name="input-pd" value="错">错</label>
          </span>
          </div>
        </div>
        <div id="pdCopy" hidden>
          <div class="copy">
            <span class="pd-pd1">
            <label><input type="radio" name="input-pd" value="对">对</label>
              <label><input type="radio" name="input-pd" value="错">错</label>
            </span>
            <span class="del"></span>
          </div>
        </div>
      </div>
      <!--判断题答案-->

      <!--主观题答案-->
      <div class="ans ans5" hidden>
        <div>正确答案</div>
        <div id="answer">
          <script type="text/plain" id="myEditor1"></script>
          <%--<textarea id="myEditor1"></textarea>--%>
        </div>
      </div>
      <!--主观题答案-->

      <div class="e-cont5">
        <div>答案解析</div>
        <div id="parse">
          <script type="text/plain" id="myEditor2"></script>
          <%--<textarea id="myEditor2"></textarea>--%>
        </div>
      </div>
      <button class="btn-bc">保存</button>
    </div>
    <!--半透明背景-->
    <div class="bg"></div>
    <!--/半透明背景-->
  </div>
  <!--/.col-right-->
</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp"%>
<!--#foot-->

<script id="selectionTmpl" type="text/template">
  <option value="">--请选择--</option>
  {{ for(var i in it) { }}
  <option value="{{=it[i].idStr}}">{{=it[i].value}}</option>
  {{ } }}
</script>

<script id="idListTmpl" type="text/template">
  <tr class="tr1">
    <td i="0">题号</td>
    {{ for(var i in it) { }}
    <td i="{{=i-0+1}}" id="{{=it[i].id}}">{{=it[i].name}}</td>
    {{ } }}
  </tr>
  <tr class="tr2">
    <td i="0">完成</td>
    {{ for(var i in it) { }}
    <td i="{{=i-0+1}}"></td>
    {{ } }}
  </tr>
</script>



<script src="/static_new/js/modules/ueditor/ueditor.config.js"></script>
<script src="/static_new/js/modules/ueditor/ueditor.all.js"></script>
<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('/static_new/js/modules/itempool/0.1.0/editupload');
</script>

</body>
</html>
