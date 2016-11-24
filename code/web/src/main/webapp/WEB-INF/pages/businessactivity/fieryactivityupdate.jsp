<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<head>
    <title>平台运营活动</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="/static/css/businessactivity/businessactivity.css">
    <script type="text/javascript" src="/static/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/vendor/jquery.ui.widget.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="/static/plugins/jquery-upload/jquery.fileupload.js"></script>
    <script type="text/javascript" src="/static/js/WdatePicker.js"></script>
    <script type="text/javascript" src="/static/js/businessactivity/update.js"></script>
    <script type="text/javascript" src="/static/js/businessactivity/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="/static/js/businessactivity/ueditor/ueditor.all.min.js"> </script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" src="/static/js/businessactivity/ueditor/lang/zh-cn/zh-cn.js"></script>
    <style type="text/css">
        #ueditor_0{
            width: 524px !important;
            height: 256px !important;
        }
    </style>
    <script type="application/javascript">
        //实例化编辑器
        //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
        var ue = UE.getEditor('editor');
        function getContent()
        {
            return ue.getContent();
        }
    </script>
</head>
<body>
	<div class="inform-all">
		<!-- 页头 -->
        <%@ include file="../common_new/head.jsp" %>
		<!-- 页头 -->
		<div class="informm-main">
			<!--左侧导航-->
            <%@ include file="../common_new/col-left.jsp" %>
			<!--===========================编辑火热活动======================================-->
			<div class="inform-main">

				<form id="form" action="/business/edit.do" method="POST">
                    <input type="hidden" id="id" name="id" value="${dto.id}">
					<input type="hidden" id="title" name="title" value="">
					<input type="hidden" id="content" name="content" value=""> 
					<input type="hidden" id="beginTime" name="beginTime" value=""> 
					<input type="hidden" id="endTime" name="endTime" value="">
                    <input type="hidden" id="checkRole" name="checkRole" value="">
                    <input type="hidden" id="picFile" name="picFile" value="">
                    <input type="hidden" id="picName" name="picName" value="">
                    <input type="hidden" id="phonePicFile" name="phonePicFile" value="">
                    <input type="hidden" id="phonePicName" name="phonePicName" value="">
					<input type="hidden" id="docFile" name="docFile" value="">
					<input type="hidden" id="docNames" name="docNames" value="">
                    <input type="hidden" id="takeEffect" name="takeEffect" value="">
                    <input type="hidden" id="eduIdsStr" name="eduIdsStr" value="${eduIdsStr}">
				</form>
				<!--===========================编辑火热活动===============================-->
				<div class="compile-main" id="compile_in">
					<div class="compile-top">
						<span>标题</span> <input id="title_input" class="compile-top-input"type="" name="" value="${dto.title}">
					</div>
					<div class="compile-hr">
						<span>编辑火热活动内容</span>
					</div>
					<div class="inform-BJ">
                        <script id="editor" type="text/plain" style="width:523px;height:400px;"></script>
						<!-- 实例化编辑器 -->
						<script type="text/javascript">
                            var ue = UE.getEditor('editor');
                            $(document).ready(function () {
                                ue.ready(function() {
                                    ue.setContent('${dto.content}');  //赋值给UEditor
                                });
                            });
                            UE.Editor.prototype._bkGetActionUrl=UE.Editor.prototype.getActionUrl;
                            UE.Editor.prototype.getActionUrl=function(action){
                                if(action=="uploadimage"){
                                    return '/business/uploadImage.do';
                                }else{
                                    return this._bkGetActionUrl.call(this, action);
                                }
                            }
		                </script>
					</div>

                    <div class="compile-add">
                        <div style="clear: both; margin-top: 8px; margin-left: 55px;overflow: visible;">
                            <div id="bannerPic" style="overflow: visible;">
                                <label for="file_bannerPic" style="cursor: pointer">
                                    <img src="/img/fileattach.png" /> 添加PC端广告图片
                                </label>
                                <div style="width: 0; height: 0; overflow: visible">
                                    <input id="file_bannerPic" type="file" name="file" value="添加PC端广告图片"
                                           size="1" style="width: 0; height: 0; opacity: 0">
                                </div>
                            </div>
                            <img src="/img/loading4.gif" id="fileuploadLoading" style="display: none;" />
                            <div class="infrom-TP" style="display: none;"></div>
                        </div>
                        <div style="clear: both; margin-top: 8px; margin-left: 55px;overflow: visible;">
                            <div id="bannerPhonePic" style="overflow: visible;">
                                <label for="file_bannerPhonePic" style="cursor: pointer">
                                    <img src="/img/fileattach.png" /> 添加手机端广告图片
                                </label>
                                <div style="width: 0; height: 0; overflow: visible">
                                    <input id="file_bannerPhonePic" type="file" name="file" value="添加手机端广告图片"
                                           size="1" style="width: 0; height: 0; opacity: 0;display: none">
                                </div>
                            </div>
                            <img src="/img/loading4.gif" id="fileuploadLoading1" style="display: none;" />
                            <div class="infrom-PTP" style="display: none;"></div>
                        </div>
                        <div style="clear: both; margin-top: 8px; margin-left: 55px;overflow: visible;">
                            <div id="attacher" style="overflow: visible;">
                                <label for="file_attach" style="cursor: pointer"> <img
                                        src="/img/fileattach.png" /> 添加附件
                                </label>
                                <div style="width: 0; height: 0; overflow: visible">
                                    <input id="file_attach" type="file" name="file" value="添加附件"
                                           size="1" style="width: 0; height: 0; opacity: 0">
                                </div>
                            </div>
                            <img src="/img/loading4.gif" id="fileuploadLoading2" style="display: none;" />
                            <div class="infrom-FJ" style="display: none;"></div>
                        </div>
                    </div>

                    <div style="margin-left: 55px;margin-top: 8px;">
                        选择活动角色:
                        <c:forEach items="${roleList}" var="item">
                            <input type="checkbox" name="role"
                            <c:if test="${roles:roleIsRole(item.key,dto.checkRole)}">checked</c:if>
                            value="${item.key}">${item.value}&nbsp;&nbsp;
                        </c:forEach>
                    </div>
                    <div style="margin-left: 55px;margin-top: 8px;">
                        即时生效: <input type="checkbox" name="effect" <c:if test="${dto.takeEffect==0}">checked</c:if> value="0">
                    </div>
                    <div style="margin-left: 55px;margin-top: 15px;">
                        选择教育局
                    </div>
                    <div style="border: 1px solid #d0d0d0; margin-left: 55px;margin-top: 5px;">
                        <table width="96%" border="0" cellpadding="0" cellspacing="0" style="margin-top: 5px;margin-left: 5px;">
                            <tr style="height: 30px;" valign="top">
                                <td align="left" colspan="3">
                                    筛选教育局
                                    <input type="text" style="border:1px solid #D0D0D0; height: 25px; width: 150px;" id="eduName" name="eduName"/>
                                    <input type="button" class="search-edu input-button" value="筛选">
                                </td>
                            </tr>
                            <tr align="left" style="height: 30px;" valign="top">
                                <td>
                                    <input type="button" class="addAllE input-button" style="margin-left: 25px;" value="全部添加">
                                </td>
                                <td width="10%">
                                </td>
                                <td width="45%">
                                    <input type="button" class="delAllE input-button" style="margin-left: 15px;" value="全部删除">
                                </td>
                            </tr>
                            <tr align="center">
                                <td width="47%" height="204px" align="center" valign="middle">
                                    <select id ="sourceE" name="sourceE" size="20"  multiple="multiple" style="width:200px;height: 200px;
                                        margin-bottom: 8px; font-size: 14px; color: #333333; vertical-align: top; border:1px solid #a9a9a9;">
                                    </select>
                                </td>
                                <td width="5%" align="center">
                                    <input type="button" class="addE input-button" value=">>">
                                    <br>
                                    <br>
                                    <input type="button" class="delE input-button" value="<<">
                                </td>
                                <td width="47%" align="center" valign="middle">
                                    <select id="targetE" name="targetE" size="20"  multiple="multiple" style="width:200px;height: 200px;
                                        margin-bottom: 8px; font-size: 14px; color: #333333; vertical-align: top; border:1px solid #a9a9a9;">
                                        <c:forEach var="item" items="${eduDtos}">
                                            <option value="${item.id}">${item.educationName}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                        </table>
                    </div>
					<div class="inform-bottom">
						<div class="inform-bottom-middle">
							<img src="/img/notic/inform-time.png"><span
								class="infrom-YX">活动时间</span><span class="testB">(选填)</span> <span>开始</span><input
								type="text" id="bTime" value="${dto.startDate}"
								onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"> <span>结束</span><input
								type="text" id="eTime" value="${dto.endDate}"
								onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
						</div>
						<div class="infrom-right">
							<button onclick="submit()">确定</button>
							<button id="QX" onclick="cancel()">取消</button>
						</div>
					</div>


					<!--===========================活动时间说明==================================-->
					<div class="valid-time">
						<span class="valid-time-I">*活动时间</span> <span>活动时间是同步到日历中显示的时间，活动时间过后火热活动自动显示为活动结束；不填写活动时间，默认为活动长期有效</span>
					</div>
					<div class="valid-timeT"></div>
				</div>
			</div>

			<!--===============================編輯取消弹出框=======================================-->
			<div class="inform-popup-I">
				<div class="inform-popup-top-I">
					<span>提示</span>
				</div>
				<div class="inform-popup-middle-I">
					<span>取消后将不保存草稿，您确定要取消编辑吗？</span>
				</div>
				<div class="infrom-popup-bottom-I">
					<button id="infrom-bottom-QX"
						onclick="goIndex()">取消编辑</button>
					<button id="infrom-bottom-BQ"
						onclick="javascript:$('.inform-popup-I').hide();">不取消</button>
				</div>
			</div>
		</div>
	</div>
    <jsp:include page="../common_new/foot.jsp"></jsp:include>
</body>
</html>
