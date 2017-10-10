/**
 * Created by liwei on 15/5/6.
 */
seajs.config({
    // base: 'http://source.ycode.cn/officalSite/js/',
    alias: {
        'jquery': 'modules/core/0.1.0/jquery.min.js?v=1',
        'doT': 'modules/core/0.1.0/doT.min.js?v=1',
        'easing': 'modules/core/0.1.0/jquery.easing.min.js?v=1',
        'echarts': 'http://echarts.baidu.com/build/dist/echarts-all.js',
        'paginator': 'modules/core/0.1.0/bootstrap/js/bootstrap-paginator.min.js?v=1',
        'ajaxfileupload': 'modules/core/0.1.0/ajaxfileupload.js?v=1',
        'social': '/static/js/modules/forum/share.js?v=1',
        'widget': 'modules/core/0.1.0/jquery-upload/vendor/jquery.ui.widget.js?v=1',
        'transport': 'modules/core/0.1.0/jquery-upload/jquery.iframe-transport.js?v=1',
        'fileupload': 'modules/core/0.1.0/jquery-upload/jquery.fileupload.js?v=1',
        'experienceScore': 'modules/core/0.1.0/experienceScore.js?v=1',
        'fancybox': 'modules/core/0.1.0/fancyBox/jquery.fancybox.js?v=1',
        'fancybox_seajs': 'modules/core/0.1.0/fancyBox/fancybox_seajs.js?v=1',
        'common': 'modules/core/0.1.0/common.js?v=1',
        'imgZoom': 'modules/imgZoom/0.1.0/jquery.artZoom4Liaoba.js?v=2',
        'cal': 'modules/calendar/0.1.0/WdatePicker.js?v=1',
        'chengji': 'modules/chengji/0.1.0/chengji.js?v=1',
        'schoolsecurity': 'modules/schoolsecurity/0.1.0/schoolsecurity.js?v=1',
        'itempool': 'modules/itempool/0.1.0/itempool.js?v=1',
        'department': 'modules/department/0.1.0/department.js?v=1',
        'moralculturemanage': 'modules/moralculture/0.1.0/moralculturemanage.js?v=1',
        'teachermoralculture': 'modules/moralculture/0.1.0/teachermoralculture.js?v=1',
        'studentmoralculture': 'modules/moralculture/0.1.0/studentmoralculture.js?v=1',
        'studentmoralculturescore': 'modules/moralculture/0.1.0/studentmoralculturescore.js',
        'teacherClass': 'modules/myclass/0.1.0/teacherClass.js?v=1',
        'classManage1': 'modules/myclass/0.1.0/classManage.js?v=1',
        'ajaxForm': 'modules/core/0.1.0/jquery-upload/jquery.form.min.js',
        'attendance': 'modules/myclass/0.1.0/attendance.js?v=1',
        'depFile': 'modules/myschool/0.1.0/departmentFile.js?v=1',
        'jiangcheng': 'modules/jiangcheng/0.1.0/jiangcheng.js?v=1',
        'salaryItem': 'modules/salary/0.1.0/salaryItem.js?v=1',
        'bxgl': 'modules/bxgl/0.1.0/bxgl.js?v=1',
        'dialog': 'modules/dialog/0.1.0/jquery-ui.min.js?v=1',
        'treeview': 'modules/treeView/0.2.0/jquery.ztree.all-3.5.min.js?v=1',
        'qcdj': 'modules/qcdj/0.1.0/qcdj.js?v=1',
        'xcdj': 'modules/xcdj/0.1.0/xcdj.js?v=1',
        'salary': 'modules/salary/0.1.0/salary.js?v=1',
        'kwgl': 'modules/kwgl/0.1.0/kwgl.js?v=1',
        'homepage': "modules/homepage/0.1.0/homepage.js?v=1",
        'activityMain': 'modules/friendcircle/0.1.0/activityMain.js?v=1',
        'friendList': 'modules/friendcircle/0.1.0/friendList.js?v=1',
        'friendSearch': 'modules/friendcircle/0.1.0/friendSearch.js?v=1',
        'friendMessage': 'modules/friendcircle/0.1.0/friendMessage.js?v=1',
        'friendReplyMsg': 'modules/friendcircle/0.1.0/friendReplyMsg.js?v=1',
        'role': 'modules/friendcircle/0.1.0/role.js?v=1',
        'initPaginator': 'modules/friendcircle/0.1.0/initPaginator.js?v=1',
        'leftBar': 'modules/friendcircle/0.1.0/leftBar.js?v=1',
        'rome': 'modules/friendcircle/0.1.0/rome.js?v=1',
        'activityView': 'modules/friendcircle/0.1.0/activityView.js?v=1',
        'studentScore': 'modules/interestclass/0.1.0/studentScore.js?v=1',
        'content_zoom': 'modules/interestclass/0.1.0/content_zoom.js?v=1',
        'curricula': 'modules/myclass/0.1.0/curricula.js?v=1',
        'curriculaI': 'modules/myschool/0.1.0/curriculaI.js?v=2',
        'pagination': 'modules/pagination/0.1.0/jqPaginator.min.js?v=1',
        // 'grid':'modules/community/plugins/drag/jquery.gridly.js',
        // 'sample':'modules/community/plugins/drag/sample.js',
        'competition': 'modules/competition/0.1.0/competition.js?v=1',
        'viewSalary': 'modules/salary/0.1.0/viewSalary.js?v=1',
        'documentCreate': 'modules/docflow/0.1.0/documentCreate.js?v=1',
        'documentCreateEdu': 'modules/docflow/0.1.0/documentCreateEdu.js?v=1',
        'documentList': 'modules/docflow/0.1.0/documentList.js?v=1',
        'documentListEdu': 'modules/docflow/0.1.0/documentListEdu.js?v=1',
        'documentDetail': 'modules/docflow/0.1.0/documentDetail.js?v=1',
        'documentDetailEdu': 'modules/docflow/0.1.0/documentDetailEdu.js?v=1',
        'documentCheck': 'modules/docflow/0.1.0/documentCheck.js?v=1',
        'documentModify': 'modules/docflow/0.1.0/documentModify.js?v=1',
        'documentModifyEdu': 'modules/docflow/0.1.0/documentModifyEdu.js?v=1',
        'myDocumentModify': 'modules/docflow/0.1.0/myDocumentModify.js?v=1',
        'microlesson': 'modules/microlesson/0.1.0/microlesson.js?v=1',
        'micromatch': 'modules/microlesson/0.1.0/micromatch.js?v=1',
        'matchdetail': 'modules/microlesson/0.1.0/matchdetail.js?v=1',
        'lessondetail': 'modules/microlesson/0.1.0/lessondetail.js?v=1',
        'interactlesson': 'modules/interactlesson/0.1.0/interactlesson.js?v=1',
        'matchresult': 'modules/microlesson/0.1.0/matchresult.js?v=1',
        'editmatch': 'modules/microlesson/0.1.0/editmatch.js?v=1',
        'educationSchools': '/static/js/modules/managecount/0.1.0/educationSchools.js?v=1',
        'totalpage': '/static/js/modules/managecount/0.1.0/totalpage.js?v=1',
        'teachertotalpage': '/static/js/modules/managecount/0.1.0/teachertotalpage.js?v=1',
        'funUseCount': '/static/js/modules/managecount/0.1.0/funUseCount.js?v=1',
        'teacherFunUseCount': '/static/js/modules/managecount/0.1.0/teacherFunUseCount.js?v=1',
        'funUseDetail': '/static/js/modules/managecount/0.1.0/funUseDetail.js?v=1',
        'manageCountUtils': '/static/js/modules/managecount/0.1.0/manageCountUtils.js?v=1',
        'roll': 'modules/roll/0.1.0/roll.js?v=1',
        'rewardHistory': 'modules/roll/0.1.0/rewardHistory.js?v=1',
        'nolesson': 'modules/zouban/0.1.0/student/nolesson.js?v=1',
        'cloudresource': 'modules/cloudresource/0.1.0/cloudresource.js?v=1',
        'lessonpublished': 'modules/zouban/0.1.0/student/lessonpublished.js?v=1',
        'lessonselect': 'modules/zouban/0.1.0/student/lessonselect.js?v=1',
        'interestlessonselect': 'modules/zouban/0.1.0/student/interestlessonselect.js?v=1',
        'clashcheck': 'modules/zouban/0.1.0/admin/clashcheck.js?v=1',
        'checklist': 'modules/zouban/0.1.0/admin/checklist.js?v=1',
        'arranging': 'modules/zouban/0.1.0/admin/arranging.js?v=1',
        'bianban': 'modules/zouban/0.1.0/manage/bianban.js?v=1',
        'lessoninstall': 'modules/zouban/0.1.0/manage/lessoninstall.js?v=1',
        'zoubanindex': 'modules/zouban/0.1.0/zoubanindex.js?v=1',
        'gezhiindex': 'modules/zouban/0.1.0/gezhiindex.js?v=1',
        'zhuzhouindex': 'modules/zouban/0.1.0/zhuzhouindex.js?v=1',
        'changZhengIndex': 'modules/zouban/0.1.0/changZhengIndex.js?v=1',
        'chooselesson': 'modules/zouban/0.1.0/manage/chooselesson.js?v=1',
        'interestChooselesson': 'modules/zouban/0.1.0/manage/interestChooselesson.js?v=1',
        'classrule': 'modules/zouban/0.1.0/manage/classrule.js?v=1',
        'gezhiClassrule': 'modules/zouban/0.1.0/manage/gezhiClassrule.js?v=1',
        'zhuzhouTeacher': 'modules/zouban/0.1.0/manage/zhuzhouTeacher.js?v=1',
        'publishcourse': 'modules/zouban/0.1.0/admin/publishcourse.js?v=1',
        'chargeteacher': 'modules/zouban/0.1.0/charge-teacher/charge-teacher.js?v=1',
        'teacher': 'modules/zouban/0.1.0/charge-teacher/teacher.js?v=1',
        'zhuzhou-teacher': 'modules/zouban/0.1.0/charge-teacher/zhuzhou-teacher.js?v=1',
        'courseteacher': 'modules/zouban/0.1.0/teacher/course-teacher.js?v=1',
        'zhuzhou-course-teacher': 'modules/zouban/0.1.0/teacher/zhuzhou-course-teacher.js?v=1',
        'migrate': 'modules/zouban/0.1.0/jquery-migrate-1.1.0.js',
        'adjust': 'modules/zouban/0.1.0/admin/adjust.js?v=1',
        'adjustV2': 'modules/zouban/0.1.0/admin/adjustV2.js?v=1',
        'addresource': 'modules/ziyuanguanli/0.1.0/addresource.js?v=1',
        'gitresource': 'modules/ziyuanguanli/0.1.0/gitresource.js?v=1',
        'family': 'modules/family/0.1.0/family.js?v=1',
        'ziyuanguanli': 'modules/ziyuanguanli/0.1.0/ziyuanguanli.js?v=1',
        'addziyuan': 'modules/ziyuanguanli/0.1.0/addziyuan.js?v=1',
        'ziyuanGit': 'modules/ziyuanguanli/0.1.0/ziyuanGit.js?v=1',
        'addtiku': 'modules/tiku/0.1.0/addtiku.js?v=1',
        'webuploader': 'modules/webuploader/webuploader.min.js',
        'imguploader': 'modules/webuploader/imgUpload.js?v=1',
        'fileuploader': 'modules/webuploader/fileUpload.js?v=1',
        'addToGit': 'modules/ziyuanguanli/0.1.0/addToGit.js?v=1',
        'gitresourseedit': 'modules/ziyuanguanli/0.1.0/gitresourseedit.js?v=1',
        'editZiyuan': 'modules/ziyuanguanli/0.1.0/editZiyuan.js?v=1',
        'tikulist': 'modules/tiku/0.1.0/tikulist.js?v=1',
        'editTiku': 'modules/tiku/0.1.0/editTiku.js?v=1',
        'areaExam': 'modules/areaExam/0.1.0/areaExam.js?v=1',
        'educationAreaExam': 'modules/areaExam/0.1.0/educationAreaExam.js?v=1',
        'newRegional': 'modules/regionalExam/0.1.0/newRegional.js?v=1',
        'inputGrade': 'modules/areaExam/0.1.0/inputGrade.js?v=1',
        'rankingList': 'modules/exam/0.1.0/rankingList.js?v=1',
        'chakan': 'modules/exam/0.1.0/chakan.js?v=1',
        'studentlist': 'modules/exam/0.1.0/studentlist.js?v=1',
        'lanclass': 'modules/lanclass/0.1.0/lanclass.js?v=1',
        'lessonclass': 'modules/lanclass/0.1.0/lessonclass.js?v=1',
        'vote': 'modules/vote/0.1.0/vote.js?v=1',
        'uploadify': 'modules/core/0.1.0/jquery.uploadify.min.js',
        'sharedpart': 'modules/core/0.1.0/sharedpart.js',
        'eduManageList': 'modules/educationbureau/0.1.0/eduManageList.js?v=1',
        'addEducation': 'modules/educationbureau/0.1.0/addEducation.js?v=1',
        'editEducation': 'modules/educationbureau/0.1.0/editEducation.js?v=1',
        'dorm': 'modules/dorm/0.1.0/dorm.js?v=1',
        'dormList': 'modules/dorm/0.1.0/dormList.js',
        'studentIn': 'modules/dorm/0.1.0/studentIn.js?v=1',
        'classManage': 'modules/funclassroom/0.1.0/classManage.js',
        'classAppoint': 'modules/funclassroom/0.1.0/classAppoint.js',
        'myManage': 'modules/funclassroom/0.1.0/myManage.js',
        'myAppointment': 'modules/funclassroom/0.1.0/myAppointment.js',
        'enterSchool': 'modules/guard/0.1.0/enterSchool.js?v=1',
        'outSchool': 'modules/guard/0.1.0/outSchool.js?v=1',
        'visiting': 'modules/guard/0.1.0/visiting.js?v=1',
        'ecart': 'modules/mall/0.1.0/ecart.js?v=1',
        'eaddress': 'modules/mall/0.1.0/eaddress.js?v=1',
        'orderpage': 'modules/mall/0.1.0/orderpage.js?v=1',
        'yunying': "modules/yunying/0.1.0/yunying.js?v=1",
        'letter': "modules/yunying/0.1.0/letter.js?v=1",
        'replyletter': "modules/yunying/0.1.0/replyletter.js?v=1",
        'schedule': "modules/schedule/0.1.0/schedule.js?v=1",
        'importSchedule': "modules/schedule/0.1.0/importSchedule.js?v=1",
        'teacherLeave': "modules/teacherLeave/0.1.0/teacherLeave.js?v=1",
        'manageLeave': "modules/teacherLeave/0.1.0/manageLeave.js?v=1",
        'furtheredu': "modules/teachermanage/0.1.0/furtheredu.js?v=1",
        'userBasic': "modules/user/0.1.0/userBasic.js?v=1",
        'projectManage': "modules/quality/0.1.0/projectManage.js?v=1",
        'growthRecord': "modules/growRecord/0.1.0/growthRecord.js?v=1",
        'growthDetail': "modules/growRecord/0.1.0/growthDetail.js?v=1",
        'addProject': "modules/quality/0.1.0/addProject.js?v=1",
        'edubxgl': "modules/bxgl/0.1.0/edubxgl.js?v=1",
        'expressTemplate': "modules/mall/0.1.0/admin/expressTemplate.js?v=1",
        'express': "modules/mall/0.1.0/express.js?v=1",
        'countMain': '/static/js/modules/managecount/0.1.0/countMain.js?v=1',
        'orderDetail': "modules/mall/0.1.0/orderdetail.js?v=1",
        'uploadVideo': 'modules/mall/0.1.0/admin/uploadVideo.js?v=1',
        'userAccount': "modules/user/0.1.0/userAccount.js?v=1",
        'select2': "modules/select2/select2.min.js?v=1",
        'collection': 'modules/mall/0.1.0/collection.js?v=1',
        'history': 'modules/mall/0.1.0/history.js?v=1',
        'user': 'modules/mall/0.1.0/user.js?v=1',
        'addressManage': 'modules/mall/0.1.0/addressManage.js?v=1',
        'birthday': 'modules/birthday/birthday.js?v=1',
        'classList': 'modules/zouban/0.1.0/attendance/classlist.js?v=1',
        'lesson': 'modules/zouban/0.1.0/attendance/lesson.js?v=1',
        'zoubanAttendance': 'modules/zouban/0.1.0/attendance/attendance.js?v=1',
        'xunshiScore': 'modules/zouban/0.1.0/attendance/xunshiscore.js?v=1',
        'goodsManage': 'modules/mall/0.1.0/admin/goodsManage.js?v=1',
        'ueditor': 'modules/forum/ueditor.all.js?v=1',
        'tou': '/wap/js/TouchSlide.1.1.js?v=1',
        'fulanlesson': '/wap/js/fulanIndex.js?v=1'
    },
    preload: [
        Function.prototype.bind ? '' : 'es5-safe',
        this.JSON ? '' : 'json',
        'jquery',
        'doT'
    ]
});