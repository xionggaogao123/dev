
<%--
  Created by IntelliJ IDEA.
  User: guojing
  Date: 2015/6/26
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- Basic Page Needs-->
    <meta charset="utf-8">
    <link rel="dns-prefetch" href="//source.ycode.cn" />
    <title>复兰科技</title>
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
    <link href="/static_new/css/educationbureau/eduManageList.css?v=2015041602" rel="stylesheet" />

</head>
<body>


<!--#head-->
<!--=================================引入头部============================================-->
<%@ include file="../common_new/head.jsp" %>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

    <div class="col-right">
        <div class="tab-col">

            <div class="tab-head clearfix">
                <ul>
                    <li class="cur"><a href="javascript:;">新建教育局</a></li>
                </ul>
            </div>

            <div class="tab-main txt-info">
                <form id="form">
                    <input type="hidden" id="userIdsStr" name="userIdsStr" value=""/>
                    <input type="hidden" id="schoolIdsStr" name="schoolIdsStr" value=""/>
                    <input type="hidden" id="educationLogo" name="educationLogo" value=""/>
                    <br>
                    <table width="100%">
                        <tr>
                            <th width="15%">
                                教育局名称：
                            </th>
                            <td width="35%">
                                <input type="text" id="eduName" name="eduName"/>
                            </td>
                            <th width="15%">
                                省、直辖市：
                            </th>
                            <td width="35%">
                                <select id="province" name="province">
                                    <option value="">请选择</option>
                                    <c:forEach var="item" items="${regions}">
                                        <option value="${item.id}">${item.name}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th width="15%">
                                市、区：
                            </th>
                            <td width="35%">
                                <select id="city" name="city">
                                    <option value="">请选择</option>
                                </select>
                            </td>
                            <th width="15%">
                                教育局Logo：
                            </th>
                            <td width="35%">
                                <span id="logoImg"><img src="http://placehold.it/170x35" style="width: 170px;height:35px;"></span>
                                <div id="eduLogoPic">
                                    <label for="file" style="cursor: pointer">
                                        上传logo
                                    </label>
                                    <div style="width: 0; height: 0;">
                                        <input id="file" type="file" name="file" value="logo图片" size="1" style="width: 0; height: 0; opacity: 0">
                                    </div>
                                    <img src="/img/loading4.gif" id="picuploadLoading"/>
                                </div>
                            </td>
                            <%--<th width="15%">
                                区、县：
                            </th>
                            <td width="35%">
                                <select id="county" name="county">
                                </select>
                            </td>--%>
                        </tr>
                        <tr>
                            <td colspan="4">
                                <table width="96%" border="0" cellpadding="0" cellspacing="0">
                                    <tr style="height: 40px;" valign="top">
                                        <th align="left" colspan="3">
                                            &nbsp;&nbsp;&nbsp;&nbsp;选择学校：
                                        </th>
                                    </tr>
                                    <tr style="height: 40px;" valign="top">
                                        <td align="left" colspan="3">
                                            &nbsp;&nbsp;&nbsp;&nbsp;筛选学校：
                                            <input type="text" id="schoolName" name="schoolName"/>
                                            <input type="button" class="search-school" value="筛选">
                                        </td>
                                    </tr>
                                    <tr align="center" style="height: 40px;" valign="top">
                                        <td>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="button" class="addAllS" value="全部添加">
                                        </td>
                                        <td width="10%">
                                        </td>
                                        <td width="45%" align="left">
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="button" class="delAllS" value="全部删除">
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="45%" height="304px" align="center" valign="middle">
                                            <select id ="sourceS" name="sourceS" size="20"  multiple="multiple" style="width:230px;height: 300px;">
                                            </select>
                                        </td>
                                        <td width="10%" align="center">
                                            <input type="button" class="addS" value=">>>>">
                                            <br>
                                            <br>
                                            <br>
                                            <input type="button" class="delS" value="<<<<">
                                        </td>
                                        <td width="45%" align="center" valign="middle">
                                            <select id="targetS" name="targetS" size="20"  multiple="multiple" style="width:230px;height: 300px;">
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4">
                                <table width="96%" border="0" cellpadding="0" cellspacing="0">
                                    <tr style="height: 60px;" valign="middle">
                                        <th align="left" colspan="3">
                                            &nbsp;&nbsp;&nbsp;&nbsp;选择教育局用户：
                                        </th>
                                    </tr>
                                    <tr style="height: 40px;" valign="top">
                                        <td align="left" colspan="3">
                                            &nbsp;&nbsp;&nbsp;&nbsp;筛选用户：
                                            <input type="text" id="userName" name="userName"/>
                                            <input type="button" class="search-user" value="筛选">
                                        </td>
                                    </tr>
                                    <tr align="center" style="height: 40px;" valign="top">
                                        <td>
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="button" class="addAllU" value="全部添加">
                                        </td>
                                        <td width="10%">
                                        </td>
                                        <td width="45%" align="left">
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            <input type="button" class="delAllU" value="全部删除">
                                        </td>
                                    </tr>
                                    <tr align="center">
                                        <td width="45%" height="304px" align="center" valign="middle">
                                            <select id ="sourceU" name="sourceU" size="20"  multiple="multiple" style="width:230px;height: 300px;">
                                            </select>
                                        </td>
                                        <td width="10%" align="center">
                                            <input type="button" class="addU" value=">>>>">
                                            <br>
                                            <br>
                                            <br>
                                            <input type="button" class="delU" value="<<<<">
                                        </td>
                                        <td width="45%" align="center" valign="middle">
                                            <select id="targetU" name="targetU" size="20"  multiple="multiple" style="width:230px;height: 300px;">
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>

                        <tr align="center">
                            <td colspan="4">
                                <button type="button" class="save">保存</button>
                                <button type="button" class="cancel">取消</button>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>

        </div>
    </div>

</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
    seajs.use('addEducation',function(addEducation){
        addEducation.init();
    });
</script>
</body>
</html>