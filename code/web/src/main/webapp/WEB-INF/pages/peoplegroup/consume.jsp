<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="roles" uri="http://fulaan.userRole.com" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <!-- Basic Page Needs-->
  <meta charset="utf-8">
  <link rel="dns-prefetch" href="//source.ycode.cn" />
  <title>校园消费</title>
  <meta name="description" content="">
  <meta name="author" content="" />
  <meta name="copyright" content="" />
  <meta name="keywords" content="">
  <meta name="viewport" content="width=device-width,initial-scale=1, maximum-scale=1">
  <!-- css files -->
  <!-- Normalize default styles -->
  <link href="/static_new/css/reset.css" rel="stylesheet" />
  <%--<link rel="stylesheet" type="text/css" href="/static_new/css/peoplegroup/population.css">--%>
  <link rel="stylesheet" type="text/css" href="/static_new/css/peoplegroup/consume.css">
  <script language="javascript" type="text/javascript" src="/static/js/WdatePicker.js"></script>
  <script type="text/javascript" src="/static_new/js/modules/core/0.1.0/jquery.min.js?v=1"></script>
  <script type="text/javascript" src="/static/plugins/echarts/angular-echarts.js"></script>
  <script type="text/javascript" src="/static/plugins/echarts/echarts-all.js"></script>
</head>
<body>

<!--#head-->
<%@ include file="../common_new/head.jsp" %>

<!--#content-->
<div id="content" class="clearfix">
  <!--.col-left-->
  <%@ include file="../common_new/col-left.jsp" %>
  <!--/.col-left-->
  <!--广告-->
  <c:choose>
    <c:when test="${roles:isStudentOrParent(sessionValue.userRole)}">
      <jsp:include page="/WEB-INF/pages/common/right_2.jsp"></jsp:include>
    </c:when>
    <c:otherwise>
      <jsp:include page="/WEB-INF/pages/common/right.jsp"></jsp:include>
    </c:otherwise>
  </c:choose>

  <!--.col-right-->
  <div class="col-right">

    <%--<!--.banner-info-->--%>
    <%--<img src="http://placehold.it/770x100" class="banner-info" />--%>
    <%--<!--/.banner-info-->--%>

      <!--.tab-col右侧-->
      <div class="tab-col">
        <div class="tab-head clearfix">
          <ul>
            <li class="cur"><a href="javascript:;">校园消费</a><em></em></li>
          </ul>
        </div>
        <div class="tab-main">
          <div class="consume-main">
            <div class="consume-top">
              <div>
                <em>年级</em>
                <select id="grade">
                  <option value="">全年级</option>
                  <c:forEach items="${gradelist}" var="grade">
                    <option value="${grade.id}">${grade.name}</option>
                  </c:forEach>
                </select>
              </div>
              <div>
                <em>班级</em>
                <select id="class">
                  <option value="">全部</option>
                </select>
              </div>
              <div>
                <em>姓名</em>
                <select id="user">
                  <option value="">全部</option>
                </select>
              </div>
              <div class="consume-DV">
                <em>起止时间</em>
                <input onclick="WdatePicker()">-<input onclick="WdatePicker()">
              </div>
              <span>查询</span>
            </div>
            <div class="consume-info">
              <!--=================年级消费统计start=================-->
              <div>
                <div id="grand-echart" style="width: 760px;height:360px;"></div>
                <script type="text/javascript">
                  var myChart = echarts.init(document.getElementById('grand-echart'));
                  var option = {
                    title : {
                      text: '年级消费统计',
                      subtext: '单位：元',
                      x:'center'

                    },
                    tooltip : {
                      trigger: 'axis'
                    },
                    legend: {
                      data:['男生','女生'],
                      y:'bottom',
                      x:'center'
                    },
                    toolbox: {
                      show : true,
                      feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                      }
                    },
                    calculable : true,
                    xAxis : [
                      {
                        type : 'category',
                        data : ['一年级','二年级','三年级','四年级','五年级']
                      }
                    ],
                    yAxis : [
                      {
                        type : 'value'
                      }
                    ],
                    series : [
                      {
                        name:'男生',
                        type:'bar',
                        data:[2.0, 4.9, 7.0, 23.2, 25.6],
                        markPoint : {
                          data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                          ]
                        },
                        markLine : {
                          data : [
                            {type : 'average', name: '平均值'}
                          ]
                        }
                      },
                      {
                        name:'女生',
                        type:'bar',
                        data:[2.6, 5.9, 9.0, 26.4, 28.7],
                        markPoint : {
                          data : [
                            {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183, symbolSize:18},
                            {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
                          ]
                        },
                        markLine : {
                          data : [
                            {type : 'average', name : '平均值'}
                          ]
                        }
                      }
                    ]
                  };

                  myChart.setOption(option);
                </script>

              </div>
              <!--==================年级消费统计end==================-->
              <!--=================一年级消费统计start=================-->
              <div>
                <div id="class-echart" style="width: 760px;height:360px;"></div>
                <script type="text/javascript">
                  var myChart = echarts.init(document.getElementById('class-echart'));
                  var option = {
                    title : {
                      text: '一年级消费统计',
                      subtext: '单位：元',
                      x:'center'

                    },
                    tooltip : {
                      trigger: 'axis',
                      axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                      }
                    },
                    legend: {
                      data:['男生','女生'],
                      x:'right'
                    },
                    toolbox: {
                      show : true,
                      orient: 'vertical',
                      x: 'right',
                      y: 'center',
                      feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                      }
                    },
                    calculable : true,
                    xAxis : [
                      {
                        type : 'category',
                        data : ['一年级一班','一年级二班','一年级三班','一年级四班','一年级五班','一年级六班']
                      }
                    ],
                    yAxis : [
                      {
                        type : 'value'
                      }
                    ],
                    series : [

                      {
                        name:'男生',
                        type:'bar',
                        stack: '广告',
                        barWidth:'40px',
                        data:[120, 132, 101, 134, 90, 230]
                      },
                      {
                        name:'女生',
                        type:'bar',
                        stack: '广告',
                        barWidth:'40px',
                        data:[220, 182, 191, 234, 290, 330]
                      }
                    ]
                  };

                  myChart.setOption(option);
                </script>

              </div>
              <!--==================一年级消费统计end==================-->
              <!--=================一年级一班消费统计start=================-->
              <div>
                <div id="expense-echart" style="width: 760px;height:400px;"></div>
                <script type="text/javascript">
                  var myChart = echarts.init(document.getElementById('expense-echart'));
                  var option = {
                    title : {
                      text: '一年级一班消费情况',
                      subtext: '2016年1月至2016年12月\n单位：元',
                      x:'center'
                    },
                    tooltip : {
                      trigger: 'axis'
                    },
                    legend: {
                      data:['男生','女生'],
                      y:'bottom',
                      x:'center'
                    },
                    toolbox: {
                      show : true,
                      feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                      }
                    },
                    calculable : true,
                    xAxis : [
                      {
                        type : 'category',
                        boundaryGap : false,
                        data : ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec']
                      }
                    ],
                    yAxis : [
                      {
                        type : 'value',
                        axisLabel : {
                          formatter: '{value} '
                        }
                      }
                    ],
                    series : [
                      {
                        itemStyle : { normal: {label : {show: true}}},
                        name:'男生',
                        type:'line',
                        data:[11, 11, 15, 13, 12, 13, 10,23,5,10,16,44],
                        markPoint : {
                          data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                          ]
                        },
                        markLine : {
                          data : [
                            {type : 'average', name: '平均值'}
                          ]
                        }
                      },
                      {
                        itemStyle : { normal: {label : {show: true}}},
                        name:'女生',
                        type:'line',
                        data:[1, 3, 2, 5, 3, 2, 0,6,77,13,15,66],
                        markPoint : {
                          data : [
                            {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
                          ]
                        },
                        markLine : {
                          data : [
                            {type : 'average', name : '平均值'}
                          ]
                        }
                      }
                    ]
                  };


                  myChart.setOption(option);
                </script>

              </div>
              <!--==================一年级一班消费统计end==================-->
              <!--=================一年级一班2016年1月消费统计start=================-->
              <div>
                <div id="classOn-echart" style="width: 760px;height:400px;"></div>
                <script type="text/javascript">
                  var myChart = echarts.init(document.getElementById('classOn-echart'));
                  var option = {
                    title : {
                      text: '一年级一班2016年1月消费统计',
                      subtext: '单位：元',
                      x:'center'
                    },
                    tooltip : {
                      trigger: 'axis',
                      axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                      }
                    },
                    legend: {
                      data:['食堂消费','超市消费','水费','电费','搜索引擎'],
                      y:'bottom'
                    },
                    toolbox: {
                      show : true,
                      orient: 'vertical',
                      x: 'right',
                      y: 'center',
                      feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                      }
                    },
                    calculable : true,
                    xAxis : [
                      {
                        type : 'category',
                        data : ['1-1','1-2','1-3','1-4','1-5','1-6','1-7']
                      }
                    ],
                    yAxis : [
                      {
                        type : 'value'
                      }
                    ],
                    series : [
                      {
                        name:'食堂消费',
                        type:'bar',
                        data:[320, 332, 301, 334, 390, 330, 320]
                      },

                      {
                        name:'超市消费',
                        type:'bar',
                        stack: '广告',
                        data:[150, 232, 201, 154, 190, 330, 410]
                      },
                      {
                        name:'水费',
                        type:'bar',
                        data:[862, 1018, 964, 1026, 1679, 1600, 1570],

                      },
                      {
                        name:'电费',
                        type:'bar',
                        stack: '搜索引擎',
                        data:[62, 82, 91, 84, 109, 110, 120]
                      }
                    ]
                  };

                  myChart.setOption(option);
                </script>

              </div>
              <!--==================一年级一班2016年1月消费统计end==================-->
              <!--=================个人消费情况统计start=================-->
              <div>
                <div id="personal-echart" style="width: 760px;height:400px;"></div>
                <script type="text/javascript">
                  var myChart = echarts.init(document.getElementById('personal-echart'));
                  var option = {
                    title : {
                      text: '马亮-消费情况统计',
                      subtext: '2016年1月至2016年12月\n单位：元',
                      x:'center'
                    },
                    tooltip : {
                      trigger: 'axis'
                    },
                    legend: {
                      data:['本月消费','上月消费'],
                      y:'bottom',
                      x:'center'
                    },
                    toolbox: {
                      show : true,
                      feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {show: true, type: ['line', 'bar']},
                        restore : {show: true},
                        saveAsImage : {show: true}
                      }
                    },
                    calculable : true,
                    xAxis : [
                      {
                        type : 'category',
                        boundaryGap : false,
                        data : ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec']
                      }
                    ],
                    yAxis : [
                      {
                        type : 'value',
                        axisLabel : {
                          formatter: '{value} '
                        }
                      }
                    ],
                    series : [
                      {
                        itemStyle : { normal: {label : {show: true}}},
                        name:'本月消费',
                        type:'line',
                        data:[11, 11, 15, 13, 12, 13, 10,23,5,10,16,44],
                        markPoint : {
                          data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                          ]
                        },
                        markLine : {
                          data : [
                            {type : 'average', name: '平均值'}
                          ]
                        }
                      },
                      {
                        itemStyle : { normal: {label : {show: true}}},
                        name:'上月消费',
                        type:'line',
                        data:[1, 3, 2, 5, 3, 2, 0,6,77,13,15,66],
                        markPoint : {
                          data : [
                            {name : '周最低', value : -2, xAxis: 1, yAxis: -1.5}
                          ]
                        },
                        markLine : {
                          data : [
                            {type : 'average', name : '平均值'}
                          ]
                        }
                      }
                    ]
                  };


                  myChart.setOption(option);
                </script>

              </div>
              <!--==================个人消费情况统计end==================-->
              <!--=================个人每月消费情况统计start=================-->
              <div>
                <div id="Gpersonal-echart" style="width: 760px;height:400px;"></div>
                <script type="text/javascript">
                  var myChart = echarts.init(document.getElementById('Gpersonal-echart'));
                  var option = {
                    title : {
                      text: '马亮一月一日消费统计',
                      /*subtext: '单位：元',*/
                      x:'center'
                    },
                    tooltip : {
                      trigger: 'item',
                      formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                      y: 'bottom',
                      data:['超市','食堂','图书','电费','水费','其他']
                    },
                    toolbox: {
                      show : true,
                      feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {
                          show: true,
                          type: ['pie', 'funnel'],
                          option: {
                            funnel: {
                              x: '25%',
                              width: '50%',
                              funnelAlign: 'left',
                              max: 1548
                            }
                          }
                        },
                        restore : {show: true},
                        saveAsImage : {show: true}
                      }
                    },
                    series : [
                      {
                        name:'消费',
                        type:'pie',
                        radius : '55%',
                        center: ['50%', '60%'],
                        data:[
                          {value:335, name:'超市'},
                          {value:310, name:'食堂'},
                          {value:234, name:'图书'},
                          {value:135, name:'电费'},
                          {value:1548, name:'水费'},
                          {value:148, name:'其他'}
                        ]
                      }
                    ]
                  };

                  myChart.setOption(option);
                </script>

              </div>
              <!--==================个人每月消费情况统计end==================-->
            </div>
          </div>
        </div>
      </div>
      <!--/.tab-col右侧-->

  </div>
  <!--/.col-right-->

</div>
<!--/#content-->
</div>
</div>
<!--#foot-->
<%@ include file="../common_new/foot.jsp" %>
<!--#foot-->

<!-- Javascript Files -->
<!-- initialize seajs Library -->
<script src="/static_new/js/sea.js"></script>
<!-- Custom js -->
<script src="/static_new/js/modules/core/0.1.0/config.js?v=2015041602"></script>
<script>
  seajs.use('consume');
</script>


</body>
</html>