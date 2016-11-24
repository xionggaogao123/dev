/**
 * Created by admin on 2016/9/14.
 */
define(function (require, exports, module) {
    /**
     *初始化参数
     */
    require('jquery');
    require('doT');
    require('rome');
    require('layer');
    var Common = require('common');
    var zhuzhouxuanke = {};

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

    var subjectAttribute=[{subjectName:"数学",subjectId:1},
        {subjectName:"化学",subjectId:2},
        {subjectName:"语文",subjectId:3},
        {subjectName:"创造发明",subjectId:4},
        {subjectName:"物理",subjectId:5},
        {subjectName:"生物",subjectId:6},
        {subjectName:"信息技术",subjectId:7},
        {subjectName:"英语",subjectId:8},];

    var classes=[{className:"G1605",id:"1"},
        {className:"G1609",id:"2"},
        {className:"G1610",id:"3"}];

    zhuzhouxuanke.init = function () {


    }



    $(function () {
        $(".tstep-tab li").click(function () {
            $(".tstep-tab li").removeClass("m-active");
            $(this).addClass("m-active");
            $(".set-div>div").hide();
            var name = $(this).attr("id");
            $("#" + "tab-" + name).show();
        })
        $('.tstep-tab li').eq(0).trigger('click');

        $('body').on('click', '.drjg-chakan', function () {
            $(".xsxkjd-con").hide();
            $(".look-link-one").show();
            var subjectId = $(this).attr('subid');
            var subjectName = $(this).attr('subnm');

            //alert(subjectId);
            $('.subjectContext').val(subjectId);
            getStudentData(subjectName);
        })

        $('.back-xkjd').click(function () {
            $(".set-div>div").hide();
            $("#tab-XKJD").show();
        })

        $(".buu").on("click", function () {
            if ($("#file").val() == "") {
                layer.alert("请先选择上传文件");
            } else if (!($("#file").val().indexOf('.xls') > 0 || $("#file").val().indexOf('.xlsx') > 0)) {
                layer.alert('请选择Excel文件');
            } else {
                var index = layer.load(2, {shade: [0.3, '#000']});
                setTimeout(function() {
                    $('#file').val('');
                    layer.alert("上传成功！");
                    layer.close(index);
                }, 1000);
            }
        });
        getXuanKeConf();
        getClassic();

        $('.adminClassContext1 option:eq(0)').attr('selected','selected');

        $('.adminClassContext1').trigger('change');
    })

    function getXuanKeConf(){

        var data=[];

        for(var j=0;j<subjectAttribute.length;j++){
            var subjectName=subjectAttribute[j].subjectName;
            var count=0;
            for(var i=0,l=studentData.length;i<l;i++){
                  var courseName=studentData[i].courseName;
                  if(courseName.indexOf(subjectName)>-1){
                      count=count+1;
                  }
            }
            var m=j+1;
            data.push({subjectName:subjectName,userCount:count,subjectId:m});
        }


        Common.render({
            tmpl: '#subjectStuNumTmpl',
            data: data,
            context: '#subjectStuNumContext',
            overwrite: 1
        });
        Common.render({
            tmpl: '#subjectTmpl',
            data: data,
            context: '.subjectContext',
            overwrite: 1
        });
    }

    function getClassic(){
        Common.render({
            tmpl: '#adminClassTmpl',
            data: classes,
            context: '.adminClassContext',
            overwrite: 1
        });
        $('.adminClassContext').change(function(){
            var tempList=[];
            if($(this).val()==0){
                var subjectName=$('.subjectContext').find("option:selected").text();
                for(var i=0,l=studentData.length;i<l;i++){
                    if(studentData[i].courseName.indexOf(subjectName)>-1){
                        tempList.push({courseName:subjectName,userName:studentData[i].userName,
                            sex:studentData[i].sex,className:studentData[i].className});
                    }
                }
            }else{
                var className=$(this).find("option:selected").text();
                var subjectName=$('.subjectContext').find("option:selected").text();
                for(var i=0,l=studentData.length;i<l;i++){
                    if(studentData[i].courseName.indexOf(subjectName)>-1&&studentData[i].className==className){
                        tempList.push({courseName:subjectName,userName:studentData[i].userName,
                            sex:studentData[i].sex,className:className});
                    }
                }
            }
            Common.render({
                tmpl: '#stuChooseTmpl',
                data: tempList,
                context: '#stuChooseContext',
                overwrite: 1
            });


        });
        Common.render({
            tmpl: '#adminClassTmpl1',
            data: classes,
            context: '.adminClassContext1',
            overwrite: 1
        });

        $('.adminClassContext1').change(function(){
            //var tempList=[];
            var xuesheng=[];
            var className=$(this).find("option:selected").text();
            for(var i=0,l=studentData.length;i<l;i++){
                if(studentData[i].className==className){
                    xuesheng.push(studentData[i]);
                }
            }

            Common.render({
                tmpl: '#stuAdvTmpl',
                data: xuesheng,
                context: '#stuAdvContext',
                overwrite: 1
            });
        });
    }

    function getStudentData(subjectName){
        var tempList=[];
        for(var i=0,l=studentData.length;i<l;i++){
            if(studentData[i].courseName.indexOf(subjectName)>-1){
                tempList.push({courseName:subjectName,userName:studentData[i].userName,
                    sex:studentData[i].sex,className:studentData[i].className});
            }
        }
        Common.render({
            tmpl: '#stuChooseTmpl',
            data: tempList,
            context: '#stuChooseContext',
            overwrite: 1
        });
        $('.adminClassContext').val("0");
    }

    module.exports = zhuzhouxuanke;
})