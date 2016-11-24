<%@ page import="com.pojo.lesson.DirType" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<link rel="stylesheet" type="text/css" href="/static/css/class-course.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/zTreeStyle/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="/static/js/jquery-ui-1.11.1.custom/jquery-ui.min.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/tree.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="/static/css/bootstrap-combined.school.css"/>
<style>
.main-container{
	width:auto;
}
</style>
<script type="text/javascript" src="/static/js/jquery.min.js"></script>

<script src="/static/js/ztree/jquery.ztree.all-3.5.min.js"></script>
<script src="/static/js/lessons/coursesManage.js"></script>
<script src="/static/js/jquery-ui-1.11.1.custom/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/js/bootstrap-paginator.min.js"></script>
<script type='text/javascript' src='/static/plugins/bootstrap/js/bootstrap.min.js?v=1'></script>
<script>
    var dirType = '<%=DirType.CLASS_LESSON%>';
    var classId = '${param.classid}';
    $(function () {
        navRoot();
        $.fn.zTree.init($("#student-course-ul"), {
            async: {
                enable: true,
                url: '/myschool/subjectdir.do',
                autoParam: ['id'],
                otherParam: {classId: '${param.classid}'},
                dataFilter: function (treeId, parent, data) {
                    if (data[0] && data[0].id) {
                        //dirTree数据
                        return data;
                    }
                    //classes数据，转化为treeNode
                    for (i in data) {
                        var classSubjectObj = data[i], dirs = classSubjectObj.dirs;
                        classSubjectObj = classSubjectObj.classSubject;
                        classSubjectObj.children = dirs;
                        classSubjectObj.name = classSubjectObj.value;
                        classSubjectObj.type = 'classSubject';
                        data[i] = classSubjectObj;
                    }
                    return data;
                }
            },
            callback: $.extend({
                onClick: function (event, treeId, node) {
                    if (node.type == 'classSubject') {
                        navClassSubject(node.id, true);
                    } else {
                        nav(node.id, true);
                    }
                }
            }, asyncTreeCallback)
        });
    });

    function navRoot() {
        loadCourse('/myschool/classsubject.do', {
            classId: '${param.classid}'
        });
    }
</script>
<div class="course-left-container">
    <ul id="student-course-ul" class="ztree dir-tree">
    </ul>
</div>
<div class="course-right-container">
</div>
