/**
 * Created by admin on 2016/7/18.
 */
define(function (require, exports, module) {
    var Common = require('common');
    require('jquery');
    require('pagination');
    var manageCenter = {};
    var page = 1;
    var sortType = 9;
    var reported = 1;
    var userId;
    var deleteUserId;
    var flag = 1;
    $(document).ready(function () {
        $('.ul-forumset li').click(function () {
            $(this).addClass('li-cur').siblings('.ul-forumset li').removeClass('li-cur');
            $('.jb-s1').show();
            $('.jubao').show();
            $('.jb-s2').hide();
            $('.jubao span:nth-child(1)').addClass('span-curr').siblings('.jubao span').removeClass('span-curr');
        });
        $('.ul-forumset .li1').click(function () {
            $('.right-r').hide();
            $('.right-xx').show();
        });
        $('.ul-forumset .li2').click(function () {
            $('.right-r').hide();
            $('.right-sea').hide();
            $('.right-tz').show();
        });
        $('.ul-forumset .li3').click(function () {
            $('.right-r').hide();
            $('.right-xt').show();
        });
        $('.jubao span').click(function () {
            $(this).addClass('span-curr').siblings('.jubao span').removeClass('span-curr');
        });
        $('.jubao .s1').click(function () {
            $('.jb-s1').show();
            $('.jb-s2').hide();
            getManageList(page, 1);
        });
        $('.jubao .s2').click(function () {
            $('.jb-s2').show();
            $('.jb-s1').hide();
            getManageList(page, 2);
        });
        $('.btn-x12').click(function () {
            deleteUserId = $('#dll').val();
            if ($('#dll').val() == "") {
                alert("请输入用户Id");
                return;
            }
            $('.m-wind-notice').fadeIn();
            $('.bg').fadeIn();


        });

        $('.m-wind-notice .p1 em,.m-wind-notice .btnno').click(function () {
            $('.m-wind-notice').fadeOut();
            $('.bg').fadeOut();
        });
        $('.m-wind-users .p1>em,.m-wind-users .p3>.btn2').click(function () {
            $('.m-wind-users').fadeOut();
            $('.bg').fadeOut();
        });

        $('body').on('click', '#cancelFor,#lll', function () {
            $('.m-wind-forbidden').fadeOut();
            $('.bg').fadeOut();
        });

        $('#bbb').click(function () {
            var together = "";
            var ty = $("input:radio[name='type-forbit']:checked").val();
            if (ty != 0) {
                var banTime = $('#banTime  option:selected').val();
                if (banTime == -1) {
                    alert("请选中一个");
                    return;
                }
                var banReason = $('#banReason').val();
                if (banReason == "") {
                    alert("请输入理由");
                    return;
                }
                together = ty + "," + banReason + "," + banTime;
            } else {
                together = ty;
            }
            $('.m-wind-forbidden').fadeOut();
            $('.bg').fadeOut();
            var param = {};
            param.userId = $('#bbb').attr('value');
            param.together = together;
            Common.getPostData('/forum/userCenter/updateForumSilenced.do', param, function (resp) {
                if (resp.code == "200") {
                    getUserList(page, userId);
                } else {
                    alert(resp.message);
                }
            });

        });

        $('.m-wind-notice .btnok').click(function () {
            $('.m-wind-notice').fadeOut();
            $('.bg').fadeOut();
            //alert(deleteUserId);
            var param = {};
            param.userId = deleteUserId;
            Common.getData('/forum/userCenter/removeUserLogic.do', param, function (resp) {
                if (resp.code == "200") {
                    location.href = "/forum/userCenter/manageCenter.do";
                } else {
                    alert(resp.message);
                }
            })
        });

        $('body').on('click', '#asa', function () {
            if ($(this).is(':checked')) {
                $("[name='checkUser']").prop("checked", true);//全选
            } else {
                $("[name='checkUser']").removeAttr("checked");
            }

        })

        $('body').on('click', '#dluser', function () {
            var fParams = "";
            var item = "";
            if ($('#asa').is(':checked')) {
                $(".checkUser").each(function () {
                    item = $(this).attr('value');
                    if (fParams != "") {
                        fParams = fParams + "," + item;
                    } else {
                        fParams = item;
                    }

                });
            } else {
                $(".checkUser").each(function () {
                    if ($(this).is(':checked')) {
                        item = $(this).attr('value');
                        if (fParams != "") {
                            fParams = fParams + "," + item;
                        } else {
                            fParams = item;
                        }
                    }
                })
            }
            if (fParams == "") {
                alert("请选中一个");
                return;
            }
            $('.m-wind-notice').fadeIn();
            $('.bg').fadeIn();
            deleteUserId = fParams;

        });
        $('.m-wind-users .p3>.btn1').click(function () {
            $('.m-wind-users').fadeOut();
            $('.bg').fadeOut();
            var param = {};
            param.userId = $('#tt').attr('value');
            param.exp = $('#at').val();
            Common.getPostData('/forum/userCenter/updateForumExp.do', param, function (resp) {
                if (resp.code == "200") {
                    getUserList(page, userId);
                } else {
                    alert(resp.message);
                }
            });
        });
        //$('.sea-tab .td6 .span1').click(
        $('body').on('click', '#span1', function () {
            $('.m-wind-users').fadeIn();
            $('.bg').fadeIn();
            var value = $(this).attr('value');
            var val = value.split(",");
            var r = val[1] / 1;
            $("#selectYear option[value='" + r + "']").attr("selected", true);
            $('#at').val(val[2]);
            $('#tt').attr('value', val[0]);
        });

        $('#selectYear').change(function () {
            var param = {};
            param.stars = $(this).val();
            Common.getPostData('/forum/userCenter/getMinLevel.do', param, function (resp) {
                $('#at').val(resp.minLevel);
            });
        });

        $('#ss').change(function () {
            getPostData(page);
        });

        $("input:radio[name='type-forbit']").change(function () {
            var temp = $(this).val();
            if (temp == 0) {
                $('#banTime').attr('disabled', 'disabled');
                $('#banReason').attr('disabled', 'disabled');
            } else {
                $('#banTime').removeAttr('disabled');
                $('#banReason').removeAttr('disabled');
            }
        });

        $('body').on('click', '#span2', function () {
            $('.m-wind-forbidden').fadeIn();
            $('.bg').fadeIn();
            $("#banTime option[value='-1']").attr("selected", true);
            $("#banReason").val("");

            var value = $(this).attr('value');
            var val = value.split(",");
            $('#bbb').attr('value', val[0]);
            $('#dor').html(val[1]);
            $("[name='type-forbit']").removeAttr("checked");
            if (val[2] == 0) {
                $('#dorr').html("正常状态");
                $("input:radio[name='type-forbit']").eq(0).prop("checked", true);
                $('#banTime').attr('disabled', 'disabled');
                $('#banReason').attr('disabled', 'disabled');
            } else if (val[2] == 1) {
                $("input:radio[name='type-forbit']").eq(1).prop("checked", true);
                $('#dorr').html("禁止发言");
                $('#banTime').removeAttr('disabled');
                $('#banReason').removeAttr('disabled');
            } else if (val[2] == 2) {
                $("input:radio[name='type-forbit']").eq(2).prop("checked", true);
                $('#dorr').html("禁止访问");
                $('#banTime').removeAttr('disabled');
                $('#banReason').removeAttr('disabled');
            }
            if (val[3] != -1) {
                var r = val[3] / 1;
                $("#banTime option[value='" + r + "']").attr("selected", true);
            }
            if (val[4] != "") {
                $('#banReason').val(val[4]);
            }

        });

        $('.btn-search1').click(function () {
            //先做处理
            userId = $('#userId').val();
            if (userId == "") {
                alert("请填写用户Id");
                return;
            }
            $('.jb-s1').hide();
            $('.jubao').hide();
            $('.right-sea').show();
            getUserList(page, userId);
        });

        $('.right-sea .p-back span').click(function () {
            $('.right-sea').hide();
            $('.jb-s1').show();
            $('.jubao').show();
        });

        $('#move').click(function () {
            var value = $('input[name="checkPost"]:checked').val();
            if (value == undefined) {
                alert("请选中一个");
                return;
            }
            $('.bg').fadeIn();
            $('.wind-move').fadeIn();
            var val = value.split(",");
            $('#originSection').html(val[1]);
            $('#originSection').attr('value', val[0]);
        });

        $('.wind-move .p1 em,.wind-move .btn2').click(function () {
            $('.wind-move').fadeOut();
            $('.bg').fadeOut();
        });
        $('.p-reback span').click(function () {
            $(this).removeClass('reback').siblings('.p-reback span').addClass('reback');
        });
        $('.p-reback .spa1').click(function () {
            $('.jb-s1').show();
            $('.jb-s2').hide();
            flag = 1;
            getPostData(page);
        });
        $('.p-reback .spa2').click(function () {
            $('.jb-s2').show();
            $('.jb-s1').hide();
            flag = 2;
            getSubPost(page);
        });

        $('body').on('click', '#btn1,#btn2', function () {
            if ($(this).is(':checked')) {
                $("[name='check']").prop("checked", true);//全选
            } else {
                $("[name='check']").removeAttr("checked");
            }

        });

        getManageList(page, reported);

        $('body').on('click', '#dee,#dde', function () {
            var dd = $(this).attr('value');
            var fParams = "";
            var item = "";
            if ($('#btn1').is(':checked')) {
                $(".check").each(function () {
                    item = $(this).attr('value');
                    if (fParams != "") {
                        fParams = fParams + "$" + item;
                    } else {
                        fParams = item;
                    }
                });
            } else {
                $(".check").each(function () {
                    if ($(this).is(':checked')) {
                        item = $(this).attr('value');
                        if (fParams != "") {
                            fParams = fParams + "$" + item;
                        } else {
                            fParams = item;
                        }
                    }
                })
            }
            alert(fParams);
            if (fParams == "") {
                alert("请选中一个");
                return;
            }
            var requestData = {};
            requestData.params = fParams;
            Common.getPostData('/forum/userCenter/removeFReported.do', requestData, function (resp) {
                if (resp.code == "200") {
                    alert("处理成功");
                    if (dd == 1) {
                        getManageList(page, 1);
                    } else {
                        getManageList(page, 2);
                    }
                } else {
                    alert(resp.message);
                }
            })
        });

        //加载板块列表
        getSectionList();
        //加载帖子列表
        getPostData(page);

        $('body').on('click', '#hhj', function () {
            getPostData(page);
        });
        $('body').on('click', '#deal', function () {
            var fParams = "";
            var item = "";
            if ($('#btn1').is(':checked')) {
                $(".check").each(function () {
                    var str1 = $(this).parent().parent().children("td").eq(3).children("p").eq(0).children('input').val();
                    var str2 = $(this).parent().parent().children("td").eq(3).children("p").eq(1).children('textarea').val();
                    if (str2 == "") {
                        str2 = "未留言";
                    }
                    item = $(this).attr('value') + "," + str1 + "," + str2;
                    if (fParams != "") {
                        fParams = fParams + "$" + item;
                    } else {
                        fParams = item;
                    }

                });
            } else {
                $(".check").each(function () {
                    if ($(this).is(':checked')) {
                        var str1 = $(this).parent().parent().children("td").eq(3).children("p").eq(0).children('input').val();
                        var str2 = $(this).parent().parent().children("td").eq(3).children("p").eq(1).children('textarea').val();
                        if (str2 == "") {
                            str2 = "未留言";
                        }
                        item = $(this).attr('value') + "," + str1 + "," + str2;
                        if (fParams != "") {
                            fParams = fParams + "$" + item;
                        } else {
                            fParams = item;
                        }
                    }
                })
            }
            if (fParams == "") {
                alert("请选中一个");
                return;
            }
            var requestData = {};
            requestData.params = fParams;
            Common.getPostData('/forum/userCenter/addFReported.do', requestData, function (resp) {
                if (resp.code == "200") {
                    alert("处理成功");
                    location.href = '/forum/userCenter/manageCenter.do';
                } else {
                    alert(resp.message);
                }
            })

        });

        $('body').on('click', '#remove', function () {
            var fParams = "";
            var item = "";
            if ($('#allSelect').is(':checked')) {
                $(".checkPost").each(function () {
                    item = $(this).attr('value');
                    var yu = item.split(",");
                    if (fParams != "") {
                        fParams = fParams + "," + yu[0];
                    } else {
                        fParams = yu[0];
                    }

                });
            } else {
                $(".checkPost").each(function () {
                    if ($(this).is(':checked')) {
                        item = $(this).attr('value');
                        var yu = item.split(",");
                        if (fParams != "") {
                            fParams = fParams + "," + yu[0];
                        } else {
                            fParams = yu[0];
                        }
                    }
                })
            }
            if (fParams == "") {
                alert("请选中一个");
                return;
            }
            var param = {};
            param.postId = fParams;
            Common.getData('/forum/userCenter/removePostData.do', param, function (resp) {
                if (resp.code == "200") {
                    getSubPost(page);
                } else {
                    alert(resp.message);
                }
            })
        });

        $('body').on('click', '#dlk,#recover', function () {
            var fParams = "";
            var item = "";
            if ($('#allSelect').is(':checked')) {
                $(".checkPost").each(function () {
                    item = $(this).attr('value');
                    var yu = item.split(",");
                    if (fParams != "") {
                        fParams = fParams + "," + yu[0];
                    } else {
                        fParams = yu[0];
                    }

                });
            } else {
                $(".checkPost").each(function () {
                    if ($(this).is(':checked')) {
                        item = $(this).attr('value');
                        var yu = item.split(",");
                        if (fParams != "") {
                            fParams = fParams + "," + yu[0];
                        } else {
                            fParams = yu[0];
                        }
                    }
                })
            }
            if (fParams == "") {
                alert("请选中一个");
                return;
            }
            if (flag == 1) {
                var param = {};
                param.postId = fParams;
                Common.getData('/forum/userCenter/FPostLogic.do', param, function (resp) {
                    if (resp.code == "200") {
                        getPostData(page);
                    } else {
                        alert(resp.message);
                    }
                })
            } else if (flag == 2) {
                var param = {};
                param.postId = fParams;
                Common.getData('/forum/userCenter/recoverFPostLogic.do', param, function (resp) {
                    if (resp.code == "200") {
                        getSubPost(page);
                    } else {
                        alert(resp.message);
                    }
                })
            }


        });


        $('body').on('click', '#allSelect,#aad', function () {
            if ($(this).is(':checked')) {
                $("[name='checkPost']").prop("checked", true);//全选
            } else {
                $("[name='checkPost']").removeAttr("checked");
            }

        });

        $('body').on('click', '#hhl', function () {
            getSubPost(page);
        });

        $('body').on('click', "#confirmss", function () {
            var value = $('#originSection').attr('value');
            $('.wind-move').fadeOut();
            $('.bg').fadeOut();
            var sectionId = $('#ssk option:selected').val();
            var requestData = {};
            requestData.postId = value;
            requestData.fSectionId = sectionId;
            var postIds = "";
            $('#nnh').find(".checkPost").each(function () {
                if ($(this).is(':checked')) {
                    postIds += $(this).parent().parent().find(".postID").val() + "@";
                }
            });
            alert(postIds);
            requestData.postId = postIds;
            Common.getPostData('/forum/updateFPost.do', requestData, function (resp) {
                if (resp.code = "200") {
                    getPostData(page);
                } else {
                    alert(resp.message);
                }
            })
        });

        $('#ssl').change(function () {
            getSubPost(page);
        });

    });

    function getSubPost(page) {
        var isInit = true;
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 15;
        requestData.zan = 1;
        requestData.userId = userId;

        requestData.regular = encodeURI(encodeURI($('#rrl').val()));
        requestData.startTime = $('#startPostTime').val();
        requestData.endTime = $('#endPostTime').val();
        requestData.postSection = $("#ssl  option:selected").val();


        Common.getData("/forum/fSubPosts.do?ti=" + new Date().getTime(), requestData, function (resp) {
            var subPost = resp.subList;
            $('#postPagel').html("");
            $('#ppol').html(resp.subCount);
            if (subPost.length > 0) {
                $('#postPagel').jqPaginator({
                    totalPages: Math.ceil(resp.subCount / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.subCount / requestData.pageSize),//总页数
                    visiblePages: 5,//分多少页
                    currentPage: parseInt(page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getSubPost(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }

            if (subPost.length == 0) {
                $('#ddsl').hide();
                $('#notFound3').show();
            } else {
                $('#ddsl').show();
                $('#notFound3').hide();
            }
            Common.render({
                tmpl: '#llkTml',
                data: subPost,
                context: '#llk',
                overwrite: 1
            });
        })
    }

    function getPostData(page) {
        var isInit = true;
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 15;
        requestData.zan = 1;
        requestData.userId = userId;

        requestData.regular = encodeURI(encodeURI($('#rr').val()));
        requestData.startTime = $('#startTime').val();
        requestData.endTime = $('#endTime').val();
        requestData.postSection = $("#ss  option:selected").val();

        Common.getData("/forum/fPosts.do?ti=" + new Date().getTime(), requestData, function (resp) {
            var posts = resp.list;
            //var subPost = resp.subList;
            $('#postPage').html("");
            //$('#postPagel').html("");
            $('#ppo').html(resp.count);
            //$('#ppol').html(resp.subCount);
            if (posts.length > 0) {
                $('#postPage').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
                    visiblePages: 5,//分多少页
                    currentPage: parseInt(page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getPostData(n);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }

            if (posts.length == 0) {
                $('#dds').hide();
                $('#notFound2').show();
            } else {
                $('#dds').show();
                $('#notFound2').hide();
            }
            Common.render({
                tmpl: '#nnhTml',
                data: posts,
                context: '#nnh',
                overwrite: 1
            });
        })
    }

    function getSectionList() {
        Common.getData("/admin/section.do", {}, function (resp) {
            Common.render({
                tmpl: '#ssTml',
                data: resp,
                context: '#ss',
                overwrite: 1
            });

            Common.render({
                tmpl: '#sslTml',
                data: resp,
                context: '#ssl',
                overwrite: 1
            });

            Common.render({
                tmpl: '#sskTml',
                data: resp,
                context: '#ssk',
                overwrite: 1
            });
        });
    }

    function getUserList(page, userId) {
        var isInit = true;
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 15;
        requestData.userId = userId;
        Common.getData("/forum/userCenter/getForumUser.do?ti=" + new Date().getTime(), requestData, function (resp) {
            var posts = resp.list;
            $('#lom').html("");
            if (posts.length > 0) {
                $('#as').html(resp.count);
                $('#lom').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
                    visiblePages: 5,//分多少页
                    currentPage: parseInt(page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getUserList(n, userId);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }

            if (posts.length == 0) {
                $('#notFound1').show();
                $('#ac').hide();
                $('#af').hide();
            } else {
                $('#notFound1').hide();
                $('#ac').show();
                $('#af').show();
            }
            Common.render({
                tmpl: '#seaTabTml',
                data: posts,
                context: '#seaTab',
                overwrite: 1
            });
        })

    }

    function getManageList(page, reported) {
        var isInit = true;
        var requestData = {};
        requestData.sortType = sortType;
        requestData.page = page;
        requestData.pageSize = 15;
        requestData.reported = reported;
        Common.getData("/forum/userCenter/reported.do?ti=" + new Date().getTime(), requestData, function (resp) {
            var posts = resp.list;
            $('.new-page-links').html("");
            if (posts.length > 0) {

                $('.new-page-links').jqPaginator({
                    totalPages: Math.ceil(resp.count / requestData.pageSize) == 0 ? 1 : Math.ceil(resp.count / requestData.pageSize),//总页数
                    visiblePages: 5,//分多少页
                    currentPage: parseInt(page),//当前页数
                    first: '<li class="first"><a href="javascript:void(0);">首页<\/a><\/li>',
                    prev: '<li class="prev"><a href="javascript:void(0);">&lt;<\/a><\/li>',
                    next: '<li class="next"><a href="javascript:void(0);">&gt;<\/a><\/li>',
                    last: '<li class="last"><a href="javascript:void(0);">末页<\/a><\/li>',
                    page: '<li class="page"><a href="javascript:void(0);">{{page}}<\/a><\/li>',
                    onPageChange: function (n) { //回调函数
                        if (isInit) {
                            isInit = false;
                        } else {
                            getManageList(n, reported);
                            $('body,html').animate({scrollTop: 0}, 20);
                        }
                    }
                });
            }

            if (posts.length == 0) {
                if (reported == 2) {
                    $('#tabJl').hide();
                    $('#poPl').hide();
                } else if (reported == 1) {
                    $('#tabJ').hide();
                    $('#poP').hide();
                }
                $('#notFound').show();
            } else {
                $('#notFound').hide();
            }
            if (reported == 1) {
                Common.render({
                    tmpl: '#tabJTml',
                    data: posts,
                    context: '#tabJ',
                    overwrite: 1
                });
            } else if (reported == 2) {
                Common.render({
                    tmpl: '#tabJlTml',
                    data: posts,
                    context: '#tabJl',
                    overwrite: 1
                });
            }


        })

    }

    module.exports = manageCenter;
});