/**
 * Created by wangkaidong on 2016/9/14.
 */
define(function (require, exports, module) {
    /**
     *初始化参数
     */
    require('layer');
    var Common = require('common');
    var timetable = {};


    timetable.init = function () {



        teacherList();
        classTimetable($('#adminClassSelect').val());
        teacherTimetable($('#teacherListCtx').val());
        studentTimetable($('#classSelect').val());
        studentClassTimetable();
    }


    $(document).ready(function() {
        $('body').on('click', '.tab-head ul li', function() {//课表tab切换
            $(this).addClass('cur').siblings().removeClass('cur');
            $('.tab-main > div').hide();
            $('#tab-' + $(this).attr('id')).show();
        });

        $('body').on('change', '#adminClassSelect', function() {
            classTimetable($(this).val());
        });

        $('body').on('change', '#teacherListCtx', function() {
            teacherTimetable($(this).val());
        });

        $('body').on('change', '#classSelect', function() {
            studentTimetable($(this).val());
        });
        $('body').on('change', '#studentsCtx', function() {
            studentClassTimetable();
        });
    });


    var TIMETABLECONF = {
        classTime : [1, 2, 3, 4, 5, 6, 7, 8],
        classDays : [1, 2, 3, 4, 5]
    };

    //用户学科数据
    var studentData=[{courseName:"化学,生物",userName:"苏志晴",sex:"女",className:"G1605"},
        {courseName:"数学,信息技术",userName:"谭笑",sex:"女",className:"G1605"},
        {courseName:"语文,英语",userName:"刘姿孜",sex:"女",className:"G1605"},
        {courseName:"创造发明,物理",userName:"李逸轩",sex:"男",className:"G1605"},
        {courseName:"化学,生物",userName:"彭子依",sex:"女",className:"G1605"},
        {courseName:"语文,英语",userName:"袁择玉",sex:"女",className:"G1605"},
        {courseName:"数学,信息技术",userName:"刘名煜",sex:"女",className:"G1605"},
        {courseName:"创造发明,信息技术",userName:"张樱才",sex:"女",className:"G1605"},
        {courseName:"创造发明,信息技术",userName:"袁昊鹏",sex:"男",className:"G1605"},
        {courseName:"数学,信息技术",userName:"杨谨瑜",sex:"女",className:"G1605"},
        {courseName:"创造发明,物理",userName:"刘怡琪",sex:"女",className:"G1605"},
        {courseName:"化学,生物",userName:"盛伊乐",sex:"女",className:"G1605"},
        {courseName:"化学,生物",userName:"朱方舟",sex:"女",className:"G1605"},
        {courseName:"数学,信息技术",userName:"唐垚峰",sex:"男",className:"G1605"},
        {courseName:"语文,信息技术",userName:"侯雨欣",sex:"女",className:"G1605"},
        {courseName:"创造发明,物理",userName:"苏思玮",sex:"男",className:"G1605"},
        {courseName:"创造发明,英语",userName:"倪霜",sex:"女",className:"G1605"},
        {courseName:"数学,信息技术",userName:"余志伟",sex:"男",className:"G1605"},
        {courseName:"创造发明,物理",userName:"王雨珺",sex:"女",className:"G1605"},
        {courseName:"化学,生物",userName:"黄渝茜",sex:"女",className:"G1605"},
        {courseName:"数学,信息技术",userName:"陈思宇",sex:"男",className:"G1605"},
        {courseName:"化学,生物",userName:"韦未雨",sex:"女",className:"G1605"},
        {courseName:"创造发明,物理",userName:"马正兴",sex:"男",className:"G1605"},
        {courseName:"创造发明,英语",userName:"谭仪杰",sex:"男",className:"G1605"},
        {courseName:"数学,信息技术",userName:"姚亦杰",sex:"女",className:"G1605"},
        {courseName:"化学,生物",userName:"刘慧玲",sex:"女",className:"G1605"},
        {courseName:"创造发明,物理",userName:"李佳蕾",sex:"女",className:"G1605"},
        {courseName:"语文,英语",userName:"阳安",sex:"男",className:"G1605"},
        {courseName:"创造发明,物理",userName:"胡田子绚",sex:"女",className:"G1605"},
        {courseName:"化学,生物",userName:"曾雨晴",sex:"女",className:"G1605"},
        {courseName:"语文,英语",userName:"刘丝语",sex:"女",className:"G1605"},
        {courseName:"数学,信息技术",userName:"莫自豪",sex:"男",className:"G1605"},
        {courseName:"化学,英语",userName:"尹嘉莉",sex:"女",className:"G1605"},
        {courseName:"化学,生物",userName:"彭哲轩",sex:"男",className:"G1605"},
        {courseName:"创造发明,物理",userName:"李旭力",sex:"男",className:"G1605"},
        {courseName:"数学,信息技术",userName:"苏丹",sex:"女",className:"G1605"},
        {courseName:"化学,生物",userName:"李香影",sex:"女",className:"G1605"},
        {courseName:"创造发明,物理",userName:"周依依",sex:"女",className:"G1605"},
        {courseName:"数学,信息技术",userName:"何天立",sex:"男",className:"G1605"},
        {courseName:"语文,英语",userName:"陈树蕙",sex:"女",className:"G1605"},
        {courseName:"数学,信息技术",userName:"袁雨康",sex:"男",className:"G1605"},
        {courseName:"创造发明,物理",userName:"朱飞扬",sex:"男",className:"G1605"},
        {courseName:"化学,生物",userName:"翁浩雄",sex:"男",className:"G1605"},
        {courseName:"语文,英语",userName:"曾子萱",sex:"女",className:"G1605"},
        {courseName:"创造发明,物理",userName:"高晟予",sex:"男",className:"G1605"},
        {courseName:"语文,英语",userName:"刘宇昕",sex:"女",className:"G1605"},
        {courseName:"化学,生物",userName:"欧阳荣剑",sex:"男",className:"G1605"},
        {courseName:"数学,信息技术",userName:"朱柄洁",sex:"女",className:"G1609"},
        {courseName:"语文,英语",userName:"刘孜",sex:"女",className:"G1609"},
        {courseName:"化学,生物",userName:"王艳琳",sex:"女",className:"G1609"},
        {courseName:"创造发明,物理",userName:"龙洁仪",sex:"女",className:"G1609"},
        {courseName:"数学,信息技术",userName:"谭琦惠",sex:"女",className:"G1609"},
        {courseName:"化学,生物",userName:"刘轩",sex:"女",className:"G1609"},
        {courseName:"创造发明,物理",userName:"李佳豪",sex:"男",className:"G1609"},
        {courseName:"数学,信息技术",userName:"龙霏霏",sex:"女",className:"G1609"},
        {courseName:"语文,英语",userName:"陈杰",sex:"女",className:"G1609"},
        {courseName:"数学,信息技术",userName:"曾添",sex:"女",className:"G1609"},
        {courseName:"创造发明,物理",userName:"汪雪瑶",sex:"女",className:"G1609"},
        {courseName:"语文,英语",userName:"杨云",sex:"女",className:"G1609"},
        {courseName:"化学,生物",userName:"马晴川",sex:"男",className:"G1609"},
        {courseName:"化学,生物",userName:"文宇佳",sex:"男",className:"G1609"},
        {courseName:"语文,英语",userName:"邹思宁",sex:"女",className:"G1609"},
        {courseName:"数学,信息技术",userName:"陈铭敏",sex:"女",className:"G1609"},
        {courseName:"创造发明,物理",userName:"王轲杰",sex:"男",className:"G1609"},
        {courseName:"数学,信息技术",userName:"吴皓天",sex:"男",className:"G1609"},
        {courseName:"化学,生物",userName:"江竟东",sex:"男",className:"G1609"},
        {courseName:"语文,英语",userName:"陈耀斌",sex:"男",className:"G1609"},
        {courseName:"创造发明,物理",userName:"张润京",sex:"男",className:"G1609"},
        {courseName:"化学,生物",userName:"凌雨森",sex:"男",className:"G1609"},
        {courseName:"数学,信息技术",userName:"陈丰业",sex:"男",className:"G1609"},
        {courseName:"创造发明,物理",userName:"侯文星",sex:"男",className:"G1609"},
        {courseName:"语文,英语",userName:"肖添翼",sex:"男",className:"G1609"},
        {courseName:"创造发明,物理",userName:"张奥成",sex:"男",className:"G1609"},
        {courseName:"语文,英语",userName:"梁承涵",sex:"男",className:"G1609"},
        {courseName:"数学,信息技术",userName:"彭丽娜",sex:"女",className:"G1609"},
        {courseName:"化学,生物",userName:"唐铱",sex:"女",className:"G1609"},
        {courseName:"数学,信息技术",userName:"旷皓文",sex:"男",className:"G1609"},
        {courseName:"语文,英语",userName:"田雨洋",sex:"女",className:"G1609"},
        {courseName:"化学,生物",userName:"王凌霄",sex:"女",className:"G1609"},
        {courseName:"创造发明,物理",userName:"刘乐",sex:"女",className:"G1609"},
        {courseName:"化学,生物",userName:"王尧翔",sex:"男",className:"G1609"},
        {courseName:"语文,英语",userName:"万承明",sex:"男",className:"G1609"},
        {courseName:"创造发明,物理",userName:"徐可航",sex:"女",className:"G1609"},
        {courseName:"数学,信息技术",userName:"汤子龙",sex:"男",className:"G1609"},
        {courseName:"创造发明,物理",userName:"王昱昭",sex:"男",className:"G1609"},
        {courseName:"数学,信息技术",userName:"冯嘉琪",sex:"女",className:"G1609"},
        {courseName:"语文,英语",userName:"刘采怡",sex:"女",className:"G1609"},
        {courseName:"化学,生物",userName:"倪顺",sex:"男",className:"G1609"},
        {courseName:"化学,生物",userName:"唐诗凯",sex:"男",className:"G1609"},
        {courseName:"数学,信息技术",userName:"吴峥扬",sex:"男",className:"G1609"},
        {courseName:"语文,英语",userName:"吴峙霖",sex:"男",className:"G1609"},
        {courseName:"创造发明,物理",userName:"尹玉玲",sex:"女",className:"G1609"},
        {courseName:"数学,信息技术",userName:"骆奕志",sex:"男",className:"G1610"},
        {courseName:"创造发明,物理",userName:"刘沁鑫",sex:"女",className:"G1610"},
        {courseName:"语文,英语",userName:"白璨闻",sex:"女",className:"G1610"},
        {courseName:"化学,生物",userName:"黄笑",sex:"女",className:"G1610"},
        {courseName:"数学,信息技术",userName:"李胤潜",sex:"男",className:"G1610"},
        {courseName:"语文,英语",userName:"成思颍",sex:"女",className:"G1610"},
        {courseName:"化学,生物",userName:"袁锐萌",sex:"女",className:"G1610"},
        {courseName:"创造发明,物理",userName:"吴鹏辉",sex:"男",className:"G1610"},
        {courseName:"语文,英语",userName:"颜茜楠",sex:"女",className:"G1610"},
        {courseName:"化学,生物",userName:"殷柯奕",sex:"女",className:"G1610"},
        {courseName:"创造发明,物理",userName:"范俊哲",sex:"男",className:"G1610"},
        {courseName:"数学,信息技术",userName:"黄添鑫",sex:"男",className:"G1610"},
        {courseName:"化学,生物",userName:"晏皓彤",sex:"女",className:"G1610"},
        {courseName:"创造发明,物理",userName:"文天卓",sex:"男",className:"G1610"},
        {courseName:"数学,信息技术",userName:"陈天乐",sex:"男",className:"G1610"},
        {courseName:"语文,英语",userName:"李楚桐",sex:"女",className:"G1610"},
        {courseName:"语文,英语",userName:"肖欢",sex:"女",className:"G1610"},
        {courseName:"数学,信息技术",userName:"罗玄昊",sex:"男",className:"G1610"},
        {courseName:"创造发明,物理",userName:"邓紫妍",sex:"女",className:"G1610"},
        {courseName:"化学,生物",userName:"郭菡滢",sex:"女",className:"G1610"},
        {courseName:"创造发明,物理",userName:"陈泽庚",sex:"男",className:"G1610"},
        {courseName:"数学,信息技术",userName:"刘人玮",sex:"男",className:"G1610"},
        {courseName:"语文,英语",userName:"李云帆",sex:"男",className:"G1610"},
        {courseName:"化学,生物",userName:"王倩",sex:"女",className:"G1610"},
        {courseName:"化学,生物",userName:"贺玉洁",sex:"女",className:"G1610"},
        {courseName:"语文,英语",userName:"周静",sex:"女",className:"G1610"},
        {courseName:"数学,信息技术",userName:"邓雅心",sex:"女",className:"G1610"},
        {courseName:"创造发明,物理",userName:"童康文",sex:"男",className:"G1610"},
        {courseName:"语文,英语",userName:"欧阳璞玉",sex:"女",className:"G1610"},
        {courseName:"数学,信息技术",userName:"丁能",sex:"男",className:"G1610"},
        {courseName:"创造发明,物理",userName:"唐圣江",sex:"男",className:"G1610"},
        {courseName:"化学,生物",userName:"易家雄",sex:"男",className:"G1610"},
        {courseName:"化学,生物",userName:"伍玉炜",sex:"女",className:"G1610"},
        {courseName:"创造发明,物理",userName:"于佳卉",sex:"女",className:"G1610"},
        {courseName:"数学,信息技术",userName:"范琼予",sex:"女",className:"G1610"},
        {courseName:"语文,英语",userName:"张智媗",sex:"女",className:"G1610"},
        {courseName:"化学,生物",userName:"陈姝言",sex:"女",className:"G1610"},
        {courseName:"创造发明,物理",userName:"朱筱龙",sex:"男",className:"G1610"},
        {courseName:"数学,信息技术",userName:"颜进超",sex:"男",className:"G1610"},
        {courseName:"语文,英语",userName:"颜子薇",sex:"女",className:"G1610"},
        {courseName:"数学,信息技术",userName:"余承巍",sex:"男",className:"G1610"},
        {courseName:"语文,英语",userName:"段牧子",sex:"女",className:"G1610"},
        {courseName:"创造发明,物理",userName:"甘周熠",sex:"男",className:"G1610"},
        {courseName:"化学,生物",userName:"蒋羿",sex:"男",className:"G1610"},
        {courseName:"语文,英语",userName:"王竞琛",sex:"男",className:"G1610"},
        {courseName:"数学,信息技术",userName:"王诏坤",sex:"男",className:"G1610"},
        {courseName:"化学,生物",userName:"熊雨涵",sex:"女",className:"G1610"}];

    var COURSES = {
        G1605 : [
            [{cName : '英语', tName : '杜静'}, {cName : '数学', tName : '杨海燕'}, {cName : '语文', tName : '高同英'}, {cName : '数学', tName : '杨海燕'}, {cName : '英语', tName : '杜静'}],
            [{cName : '语文', tName : '高同英'}, {cName : '口语', tName : '外教'}, {cName : '语文', tName : '高同英'}, {cName : '物理', tName : '周运生'}, {cName : '语文', tName : '高同英'}],
            [{cName : '化学', tName : '魏加录'}, {cName : '生物', tName : '刘丽华'}, {cName : '英语', tName : '杜静'}, {cName : '音乐', tName : '熊立东'}, {cName : '历史', tName : '江广文'}],
            [{cName : '体育', tName : ''}, {cName : '美术', tName : '史静'}, {cName : '化学', tName : '魏加录'}, {cName : '英语', tName : '杜静'}, {cName : '信息', tName : ''}],
            [{cName : '历史', tName : '江广文'}, {cName : '语文', tName : '高同英'}, {cName : '数学', tName : '杨海燕'}, {cName : '生物', tName : '刘丽华'}, {cName : '信息', tName : ''}],
            [{cName : '地理', tName : '周卫东'}, {cName : '政治', tName : '李素红'}, {cName : '地理', tName : '周卫东'}, {cName : '化学', tName : '魏加录'}, {cName : '数学', tName : '杨海燕'}],
            [{cName : '数学', tName : '杨海燕'}, {cName : '选修二', tName : ''}, {cName : '体育', tName : ''}, {cName : '选修三', tName : ''}, {cName : '物理', tName : '周运生'}],
            [{cName : '政治', tName : '李素红'}, {cName : '选修二', tName : ''}, {cName : '物理', tName : '周运生'}, {cName : '选修三', tName : ''}, {cName : '', tName : ''}]
        ],
        G1609 : [
            [{cName : '数学', tName : '张耀华'}, {cName : '语文', tName : '陈跃年'}, {cName : '外语', tName : '杜静'}, {cName : '数学', tName : '张耀华'}, {cName : '数学', tName : '张耀华'}],
            [{cName : '外语', tName : '杜静'}, {cName : '数学', tName : '张耀华'}, {cName : '生物', tName : '贺迎飞'}, {cName : '生物', tName : '贺迎飞'}, {cName : '历史', tName : '江广文'}],
            [{cName : '化学', tName : '徐宜武'}, {cName : '物理', tName : '李惠'}, {cName : '物理', tName : '李惠'}, {cName : '语文', tName : '陈跃年'}, {cName : '政治', tName : '李素红'}],
            [{cName : '体育', tName : ''}, {cName : '政治', tName : '李素红'}, {cName : '体育', tName : ''}, {cName : '化学', tName : '徐宜武'}, {cName : '信息', tName : ''}],
            [{cName : '地理', tName : '周卫东'}, {cName : '口语外语', tName : '老外1'}, {cName : '数学', tName : '张耀华'}, {cName : '外语', tName : '杜静'}, {cName : '信息', tName : ''}],
            [{cName : '语文', tName : '陈跃年'}, {cName : '美术', tName : '史静'}, {cName : '化学', tName : '徐宜武'}, {cName : '物理', tName : '李惠'}, {cName : '外语', tName : '杜静'}],
            [{cName : '语文', tName : '陈跃年'}, {cName : '选修二', tName : ''}, {cName : '历史', tName : '江广文'}, {cName : '选修三', tName : ''}, {cName : '语文', tName : '陈跃年'}],
            [{cName : '音乐', tName : '张依屏'}, {cName : '选修二', tName : ''}, {cName : '地理', tName : '周卫东'}, {cName : '选修三', tName : ''}, {cName : '', tName : ''}]
        ],
        G1610 : [
            [{cName : '外语', tName : '骆杏元'}, {cName : '数学', tName : '张耀华'}, {cName : '语文', tName : '杨玲'}, {cName : '外语', tName : '骆杏元'}, {cName : '语文', tName : '杨玲'}],
            [{cName : '数学', tName : '张耀华'}, {cName : '外语', tName : '骆杏元'}, {cName : '语文', tName : '杨玲'}, {cName : '数学', tName : '张耀华'}, {cName : '数学', tName : '张耀华'}],
            [{cName : '政治', tName : '刘瑜'}, {cName : '美术', tName : '史静'}, {cName : '数学', tName : '张耀华'}, {cName : '语文', tName : '杨玲'}, {cName : '口语外语', tName : '老外1'}],
            [{cName : '生物', tName : '刘丽华'}, {cName : '信息', tName : ''}, {cName : '外语', tName : '骆杏元'}, {cName : '生物', tName : '刘丽华'}, {cName : '历史', tName : '田雨'}],
            [{cName : '体育', tName : ''}, {cName : '信息', tName : ''}, {cName : '体育', tName : ''}, {cName : '音乐', tName : '张依屏'}, {cName : '化学', tName : '徐宜武'}],
            [{cName : '物理', tName : '曾团芳'}, {cName : '历史', tName : '田雨'}, {cName : '地理', tName : '匡焕新'}, {cName : '物理', tName : '曾团芳'}, {cName : '政治', tName : '刘瑜'}],
            [{cName : '化学', tName : '徐宜武'}, {cName : '选修二', tName : ''}, {cName : '化学', tName : '徐宜武'}, {cName : '选修三', tName : ''}, {cName : '地理', tName : '匡焕新'}],
            [{cName : '语文', tName : '杨玲'}, {cName : '选修二', tName : ''}, {cName : '物理', tName : '曾团芳'}, {cName : '选修三', tName : ''}, {cName : '', tName : ''}]
        ]
    };

    var ZBTeachers = ['严朝晖', '张耀华', '杜静', '周运生', '徐宜武', '刘丽华', '杜新宇', '刘鑫灿'];

    var ZBSubjectList = [
        [
            {cName: '语文', tName: '严朝晖', classroom: 'A101'},
            {cName: '数学', tName: '张耀华', classroom: 'A102'},
            {cName: '化学', tName: '徐宜武', classroom: 'A103'},
            {cName: '创造发明', tName: '刘鑫灿', classroom: 'A104'}
        ],
        [
            {cName: '英语', tName: '杜静', classroom: 'A101'},
            {cName: '物理', tName: '周运生', classroom: 'A102'},
            {cName: '生物', tName: '刘丽华', classroom: 'A103'},
            {cName: '信息技术', tName: '杜新宇', classroom: 'A104'}
        ]
    ];


    function template(tmpl, ctx, data) {
        Common.render({
            tmpl: tmpl,
            context: ctx,
            data: data,
            overwrite: 1
        });
    }


    function classTimetable(className) {
        var data = {
            conf : TIMETABLECONF,
            course : COURSES[className]
        };

        template('#timetableTmpl', '#classTableCtx', data);
    }


    function teacherList() {
        template('#teacherListTmpl', '#teacherListCtx', ZBTeachers);
    }


    function teacherTimetable(teacherName) {
        var courseList = [];
        
        for (var i = 0; i < 8; i++) {
            var line = [];
            for (var j = 0; j < 5; j++) {
                var course = {cName : '', tName : '', classroom : ''};
                if ((i == 6 || i == 7) && j == 1) {
                    for (var c in ZBSubjectList[0]) {
                        if (ZBSubjectList[0][c].tName == teacherName) {
                            course = ZBSubjectList[0][c];
                        }
                    }
                } else if ((i == 6 || i == 7) && j == 3) {
                    for (var c in ZBSubjectList[1]) {
                        if (ZBSubjectList[1][c].tName == teacherName) {
                            course = ZBSubjectList[1][c];
                        }
                    }
                } else {
                    for (var c in COURSES) {
                        if (COURSES[c][i][j].tName == teacherName) {
                            course = COURSES[c][i][j];
                        }
                    }
                }

                line.push(course);
            }
            courseList.push(line);
        }

        var data = {
            conf: TIMETABLECONF,
            course: courseList
        };

        template('#timetableTmpl', '#teacherTableCtx', data);
    }


    function studentTimetable(className){
        var student=[];
        for(var i=0;i<studentData.length;i++){
            if(studentData[i].className==className){
                student.push({userName:studentData[i].userName,studentId:i});
            }
        }
        template('#studentsTmpl','#studentsCtx',student);
        $("#studentsCtx").val(student[0].studentId);
        studentClassTimetable();
    }

    function studentClassTimetable(){
        var sValue=$('#studentsCtx').find("option:selected").text();
        var courseName;
        var className;


        for(var j=0;j<studentData.length;j++){
            if(studentData[j].userName==sValue){
                courseName=studentData[j].courseName;
                className=studentData[j].className;
                break;
            }
        }
        var sp=courseName.split(',');

        var courseList = [];
        for (var i = 0; i < 8; i++) {
            var line = [];
            for (var j = 0; j < 5; j++) {
                var course = {cName : '', tName : '', classroom : ''};
                if ((i == 6 || i == 7) && j == 1) {
                    for (var c in ZBSubjectList[0]) {
                        if (ZBSubjectList[0][c].cName == sp[0]) {
                            course = ZBSubjectList[0][c];
                        }
                    }
                } else if ((i == 6 || i == 7) && j == 3) {
                    for (var c in ZBSubjectList[1]) {
                        if (ZBSubjectList[1][c].cName == sp[1]) {
                            course = ZBSubjectList[1][c];
                        }
                    }
                } else {
                    course = COURSES[className][i][j];
                }

                line.push(course);
            }
            courseList.push(line);
        }

        var data = {
            conf: TIMETABLECONF,
            course: courseList
        };
        template('#timetableTmpl', '#studentTableCtx', data);
    }



    module.exports = timetable;
});