<%--
  Created by IntelliJ IDEA.
  User: wang_xinxin
  Date: 2016/2/29
  Time: 15:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
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
  <link rel="shortcut icon" href="" type="image/x-icon" />
  <link rel="icon" href="" type="image/x-icon" />
  <link href="/static_new/css/teachermanage/furtheredu.css" rel="stylesheet" />
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
</head>
<body>

<!--#head-->
<%@ include file="../common_new/head.jsp"%>
<!--/#head-->

<!--#content-->
<div id="content" class="clearfix">

  <!--.col-left-->
  <%--<div class="col-left">--%>
  <%@ include file="../common_new/col-left.jsp" %>
  <!--广告-->
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>
  <%--</div>--%>
  <!--/.col-left-->

  <!--.col-right-->
  <div class="col-right">

    <!--.tab-col-->
    <div class="tab-col">
      <div class="main-top">
        <div class="main-top2 clearfix ">
          <span class="span-top span1">教师管理</span>
        </div>
      </div>
      <div class="cont-main1-2">
        <div class="add-title">教师个人信息</div>
        <table class="table-infor">
          <tr>
            <th colspan="4">基本信息</th>
          </tr>
          <tr>
            <td rowspan="4" class="td1">
              <img class="img-upload-pic" src="../../../static_new/images/origin_pic.png" width="87px" height="118px;" >
            </td>
            <td class="td2">用户名</td>
            <td colspan="2">${resumeDTO.name}</td>
          </tr>
          <tr>
            <td >权限</td>
            <td colspan="2" class="td3">
              ${resumeDTO.role}
            </td>
          </tr>
          <tr>
            <td >职务</td>
            <td colspan="2" class="td3">
              ${resumeDTO.postiondec}
            </td>
          </tr>
          <tr>
            <td >姓名</td>
            <td colspan="2" class="td3">
              ${resumeDTO.username}
            </td>
          </tr>
          <tr>
            <td>性别</td>
            <td colspan="2" class="td3">
              <c:if test="${resumeDTO.sex==0}">
                女
              </c:if>
              <c:if test="${resumeDTO.sex==1}">
                男
              </c:if>
            </td>
          </tr>
          <tr>
            <td >出生日期</td>
            <td>
              ${resumeDTO.birth}
            </td>
            <td class="td3">籍贯</td>
            <td class="td4">
              ${resumeDTO.place}
            </td>
          </tr>
          <tr>
            <td>民族</td>
            <td class="td2">
              <c:if test="${resumeDTO.national==0}">
                汉族
              </c:if>
              <c:if test="${resumeDTO.national==1}">
                其他
              </c:if>
            </td>
            <td>身份证类型</td>
            <td class="td4">
              <c:if test="${resumeDTO.card==0}">
                身份证
              </c:if>
              <c:if test="${resumeDTO.card==1}">
                护照
              </c:if>
            </td>
          </tr>
          <tr>
            <td>身份证件号码</td>
            <td class="td2">
              ${resumeDTO.cardnumber}
            </td>
            <td>身份证件有效期</td>
            <td class="td4">
              ${resumeDTO.vail}
            </td>
          </tr>
          <tr>
            <td>婚姻状况</td>
            <td class="td2">
              <c:if test="${resumeDTO.maritalstatus==0}">
                未婚
              </c:if>
              <c:if test="${resumeDTO.maritalstatus==1}">
                已婚
              </c:if>
              <c:if test="${resumeDTO.maritalstatus==2}">
                离异
              </c:if>
            </td>
            <td>政治面貌</td>
            <td class="td4">
              <c:if test="${resumeDTO.political==0}">
                中共党员
              </c:if>
              <c:if test="${resumeDTO.political==1}">
                共青团员
              </c:if>
              <c:if test="${resumeDTO.political==2}">
                民主党派
              </c:if>
              <c:if test="${resumeDTO.political==3}">
                无党派人士
              </c:if>
              <c:if test="${resumeDTO.political==4}">
                群众
              </c:if>
              <c:if test="${resumeDTO.political==5}">
                其他
              </c:if>
            </td>
          </tr>
          <tr>
            <td>户口所在地</td>
            <td class="td2">
              ${resumeDTO.registerplace}
            </td>
            <td>家庭住址</td>
            <td class="td4">
              ${resumeDTO.adress}
            </td>
          </tr>
          <tr>
            <td>现住址</td>
            <td class="td2">
              ${resumeDTO.nowadress}
            </td>
            <td>邮政编码</td>
            <td class="td4">
              ${resumeDTO.zipcode}
            </td>
          </tr>
          <tr>
            <td >学段</td>
            <td class="td2">
              <c:if test="${resumeDTO.area==0}">
                小学
              </c:if>
              <c:if test="${resumeDTO.area==1}">
                初中
              </c:if>
              <c:if test="${resumeDTO.area==2}">
                高中
              </c:if>
            </td>
            <td>紧急联系人</td>
            <td class="td4">
              ${resumeDTO.contact}
            </td>
          </tr>
          <tr>
            <td>联系人电话</td>
            <td class="td2">
              ${resumeDTO.contactphone}
            </td>
            <td>电子信箱</td>
            <td class="td4">
              ${resumeDTO.email}
            </td>
          </tr>
          <tr>
            <td>初始学历</td>
            <td class="td2">
              <c:if test="${resumeDTO.education==0}">
                大专
              </c:if>
              <c:if test="${resumeDTO.education==1}">
                本科
              </c:if>
              <c:if test="${resumeDTO.education==2}">
                硕士
              </c:if>
              <c:if test="${resumeDTO.education==3}">
                博士
              </c:if>
            </td>
            <td>专业</td>
            <td class="td4">
              ${resumeDTO.major}
            </td>
          </tr>
          <tr>
            <td>取得时间</td>
            <td class="td2">
              ${resumeDTO.deutime}
            </td>
            <td>初始学位</td>
            <td class="td4">
              <c:if test="${resumeDTO.degree==0}">
                学士
              </c:if>
              <c:if test="${resumeDTO.degree==1}">
                硕士
              </c:if>
              <c:if test="${resumeDTO.degree==2}">
                博士
              </c:if>
              <c:if test="${resumeDTO.degree==3}">
                名誉博士
              </c:if>
            </td>
          </tr>
          <tr>
            <td>取得时间</td>
            <td class="td2">
              ${resumeDTO.degtime}
            </td>
            <td>参加工作时间</td>
            <td class="td4">
              ${resumeDTO.jobtime}
            </td>
          </tr>
          <tr>
            <td>来校年月</td>
            <td class="td2">
              ${resumeDTO.schooltime}
            </td>
            <td>从教年月</td>
            <td class="td4">
              ${resumeDTO.teachtime}
            </td>
          </tr>
          <tr>
            <td>从教学科</td>
            <td class="td2">
              <c:if test="${resumeDTO.teachSubject==0}">
                语文
              </c:if>
              <c:if test="${resumeDTO.teachSubject==1}">
                数学
              </c:if>
              <c:if test="${resumeDTO.teachSubject==2}">
                英语
              </c:if>
              <c:if test="${resumeDTO.teachSubject==3}">
                历史
              </c:if>
              <c:if test="${resumeDTO.teachSubject==4}">
                地理
              </c:if>
              <c:if test="${resumeDTO.teachSubject==5}">
                政治
              </c:if>
              <c:if test="${resumeDTO.teachSubject==6}">
                化学
              </c:if>
              <c:if test="${resumeDTO.teachSubject==7}">
                生物
              </c:if>
              <c:if test="${resumeDTO.teachSubject==8}">
                物理
              </c:if>
              <c:if test="${resumeDTO.teachSubject==9}">
                美术
              </c:if>
              <c:if test="${resumeDTO.teachSubject==10}">
                音乐
              </c:if>
              <c:if test="${resumeDTO.teachSubject==11}">
                体育
              </c:if>
              <c:if test="${resumeDTO.teachSubject==12}">
                计算机
              </c:if>
            </td>
            <td>普通话等级</td>
            <td class="td4">
              <c:if test="${resumeDTO.mandarinlevel==0}">
                一级甲等
              </c:if>
              <c:if test="${resumeDTO.mandarinlevel==1}">
                一级乙等
              </c:if>
              <c:if test="${resumeDTO.mandarinlevel==2}">
                二级甲等
              </c:if>
              <c:if test="${resumeDTO.mandarinlevel==3}">
                二级乙等
              </c:if>
              <c:if test="${resumeDTO.mandarinlevel==4}">
                三级甲等
              </c:if>
              <c:if test="${resumeDTO.mandarinlevel==5}">
                三级乙等
              </c:if>
            </td>
          </tr>
          <tr>
            <td>教职工号</td>
            <td class="td2">
              ${resumeDTO.teachernumber}
            </td>
            <td>编制</td>
            <td class="td4">
              <c:if test="${resumeDTO.organization==1}">
                教师
              </c:if>
              <c:if test="${resumeDTO.organization==2}">
                职工
              </c:if>
              <c:if test="${resumeDTO.organization==3}">
                临时工
              </c:if>
              <c:if test="${resumeDTO.organization==4}">
                实习生
              </c:if>
            </td>
          </tr>
        </table>

        <div class="table-div">
          <div class="table-div-title">职后学历</div>
          <table class="table-all table-all-1">
            <tr>
              <th class="th1">职后最高学历</th>
              <th class="th2">职后最高学位</th>
              <th class="th3">所学专业</th>
              <th class="th4">获取时间</th>
              <th class="th5">公开</th>
            </tr>
            <c:forEach items="${educationEntryList}" var="item">
              <tr>
                <td>
                  ${item.education}
                </td>
                <td>${item.degree}</td>
                <td>${item.major}</td>
                <td>${item.time}</td>
                <td>
                  <c:if test="${item.open==1}">
                    是
                  </c:if>
                  <c:if test="${item.open==0}">
                    否
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">工作简历</div>
          <table class="table-all table-all-2">
            <tr>
              <th class="th7">工作时间</th>
              <th class="th3">工作单位</th>
              <th class="th4">所任职务</th>
              <th class="th5">公开</th>
            </tr>
            <c:forEach items="${jobEntryList}" var="item">
              <tr>
                <td>
                    ${item.jobtime}
                </td>
                <td>${item.organization}</td>
                <td>${item.position}</td>
                <td>
                  <c:if test="${item.open==1}">
                    是
                  </c:if>
                  <c:if test="${item.open==0}">
                    否
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">职称</div>
          <table class="table-all table-all-3">
            <tr>
              <th class="th1">名称</th>
              <th class="th2">获取时间</th>
              <th class="th3">岗位类别</th>
              <th class="th4">岗位等级</th>
              <th class="th7">聘任时间</th>
              <th class="th5">公开</th>
            </tr>
            <c:forEach items="${titleEntryList}" var="item">
              <tr>
                <td>
                    ${item.name}
                </td>
                <td>${item.time}</td>
                <td>${item.jobType}</td>
                <td>${item.level}</td>
                <td>${item.appointmentTime}</td>
                <td>
                  <c:if test="${item.open==1}">
                    是
                  </c:if>
                  <c:if test="${item.open==0}">
                    否
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">行政职务</div>
          <table class="table-all table-all-4">
            <tr>
              <th class="th1">任职时间</th>
              <th class="th2">任职单位</th>
              <th class="th3">所在职务</th>
              <th class="th5">公开</th>
            </tr>
            <c:forEach items="${postionEntryList}" var="item">
              <tr>
                <td>${item.jobtime}</td>
                <td>${item.organization}</td>
                <td>${item.position}</td>
                <td>
                  <c:if test="${item.open==1}">
                    是
                  </c:if>
                  <c:if test="${item.open==0}">
                    否
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">社会兼职</div>
          <table class="table-all table-all-4">
            <tr>
              <th class="th1">任职时间</th>
              <th class="th2">任职单位</th>
              <th class="th3">所在职务</th>
              <th class="th5">公开</th>
            </tr>
            <c:forEach items="${partTimeEntryList}" var="item">
              <tr>
                <td>${item.time}</td>
                <td>${item.unit}</td>
                <td>${item.position}</td>
                <td>
                  <c:if test="${item.open==1}">
                    是
                  </c:if>
                  <c:if test="${item.open==0}">
                    否
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">获得成果（包括荣誉、论文发表或出版、课题与项目）</div>
          <table class="table-all table-all-5">
            <tr>
              <th class="th1">成果说明</th>
              <th class="th2">级别</th>
              <th class="th3">获得时间</th>
              <th class="th5">公开</th>
            </tr>
            <c:forEach items="${resultEntryList}" var="item">
              <tr>
                <td>${item.introduce}</td>
                <td>${item.level}</td>
                <td>${item.time}</td>
                <td>
                  <c:if test="${item.open==1}">
                    是
                  </c:if>
                  <c:if test="${item.open==0}">
                    否
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">证书</div>
          <table class="table-all table-all-6">
            <tr>
              <th class="th1">获得时间</th>
              <th class="th2">证书名称</th>
              <th class="th3">成绩</th>
              <th class="th5">公开</th>
            </tr>
            <c:forEach items="${certificateEntryList}" var="item">
              <tr>
                <td>${item.time}</td>
                <td>${item.name}</td>
                <td>${item.record}</td>
                <td>
                  <c:if test="${item.open==1}">
                    是
                  </c:if>
                  <c:if test="${item.open==0}">
                    否
                  </c:if>
                </td>
              </tr>
            </c:forEach>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">继续教育中心</div>
          <table class="table-all table-all-7">
            <c:forEach items="${continueEducationEntryList}" var="item">
            <tr>
              <th class="th1">时间</th>
              <th class="th2">培训机构</th>
              <th class="th3">培训课程</th>
              <th class="th4">证书</th>
              <th class="th7">成绩</th>
              <th class="th5">公开</th>
            </tr>
            <tr>
              <td>${item.time}</td>
              <td>${item.institutions}</td>
              <td>
                <c:forEach items="${courseProjectList}" var="cp">
                  <c:if test="${cp.id==item.course}">${cp.course}</c:if>
                </c:forEach>
              </td>
              <td>${item.certificate}</td>
              <td>${item.record}</td>
              <td rowspan="3">
                <c:if test="${item.open==1}">
                  是
                </c:if>
                <c:if test="${item.open==0}">
                  否
                </c:if>
              </td>
            </tr>
            <tr>
              <th>地点</th>
              <th>培训课时</th>
              <th colspan="3">培训内容</th>
            </tr>
            <tr>
              <td>${item.address}</td>
              <td>${item.classHour}</td>
              <td colspan="3">${item.content} </td>
            </tr>
            </c:forEach>
          </table>
        </div>
        <div class="table-div">
          <div class="table-div-title">自我介绍</div>
          <div>
            ${resumeDTO.introduction}
          </div>
        </div>
      </div>
    </div>
  </div>
  <!--/.col-right-->
</div>
<!--/#content-->

<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->
</body>
</html>
