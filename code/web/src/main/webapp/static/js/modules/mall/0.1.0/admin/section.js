/**
 * Created by fl on 2016/3/3.
 */
'use strict';
(function () {
    var app = angular.module('myApp', []);
    app.controller('myCtrl', function ($http, $scope) {
        $scope.section = {
            fSectionId: '',
            parentId: '',
            level: 1,
            count: 0,
            totalCount: 0,
            memoName: "",
            memo: "",
            sectionName: "",
            sort: 1,
            image: ""
        };
        var level = 1;
        var pId = "";
        getSection(level, pId);

        function getSection(level, pId) {
            $http.get('/admin/section.do', {
                params: {
                    level: level, parentId: pId
                }
            }).then(function (response) {
                if (level == 1) {
                    $scope.sectionList = response.data;
                } else if (level == 2) {
                    $scope.sectionListl = response.data;
                }

            });
        }

        var sort = 0;

        function getSort() {
            var self = $('.list-group').next('ul');
            sort = self.children().length + 1;
        }

        $scope.addSection = function (level) {
            var self = angular.element(window.event.target);
            var ul = self.next('ul');
            $scope.section.level = 1;
            $scope.section.sort = ul.children().length + 1;
            $.ajax({
                url: '/admin/section.do?',
                type: 'post',
                data: angular.toJson($scope.section),
                contentType: 'application/json',
                success: function (result) {
                    getSection(level, "");
                    alert(result.message);
                },
                error: function (e) {
                    alert('添加失败');
                }
            });
        }

        $scope.deleteSection = function (id, level, pId) {
            $http.delete('/admin/section/' + id + '.do', {
                params: {}
            }).then(function (response) {
                getSection(level, pId);
                alert(response.data.message);
            });
        }

        $scope.edit = function () {
            var self = angular.element(window.event.target);
            self.siblings("span[name='nameSpan']").hide();
            self.siblings("input[name='nameInput']").show().focus();
            self.hide();
            self.siblings("span[name='submit']").show();
        }

        $scope.updateSection = function (fSectionId, name, parentId, level, sort) {
            var self = angular.element(window.event.target);
            var url = '/admin/section/' + fSectionId + '.do';
            var param = {
                fSectionId: fSectionId,
                name: name,
                parentId: parentId,
                level: level,
                sort: sort
            };
            $.ajax({
                url: url,
                data: JSON.stringify(param),
                type: 'PUT',
                contentType: 'application/json',
                dataType: 'json',
                success: function (data) {
                    if (data.code == '200') {
                        this.name = name;
                        self.siblings("span[name='nameSpan']").show();
                        self.siblings("input[name='nameInput']").hide();
                        self.siblings("span[name='edit']").show();
                        self.hide();
                    } else {
                        alert("更新失败！");
                    }
                }
            });
        }

        $scope.up = function () {
            var self = angular.element(window.event.target);
            var li = self.parents('li');
            var selfId = li.attr('id');
            var selfSort = li.prevAll().length + 1;

            if (selfSort == 1) {
                return;
            } else {
                var prev = li.prev();
                var prevId = prev.attr('id');
                var prevSort = selfSort;
                selfSort--;
                changeSort(selfId, selfSort, prevId, prevSort);
            }

        }

        $scope.down = function () {
            var self = angular.element(window.event.target);
            var li = self.parents('li');
            var selfId = li.attr('id');
            var selfSort = li.prevAll().length + 1;

            if (selfSort == li.siblings().length) {
                return;
            } else {
                var next = li.next();
                var nextId = next.attr('id');
                var nextSort = selfSort;
                selfSort++;
                changeSort(selfId, selfSort, nextId, nextSort);
            }
        }

        /**
         * 调整排序*/
        function changeSort(selfId, selfSort, anotherId, anotherSort) {
            var url = '/admin/section/changeSort.do';
            var param = {selfId: selfId, selfSort: selfSort, anotherId: anotherId, anotherSort: anotherSort};
            $.ajax({
                url: url,
                type: 'POST',
                data: param,
                dataType: 'json',
                success: function () {
                    getSection(1, "");
                }
            });
        }

        /**
         * 上传图片
         * */
        function uploadImg(id, url) {
            $('#upload').show();
            $('#img').fileupload({
                url: '/mall/images.do',
                paramName: 'Filedata',
                done: function (e, response) {
                    var imgUrl = response.result.message[0].path;
                    $.ajax({
                        url: url,
                        type: 'POST',
                        data: {id: id, imgUrl: imgUrl},
                        dataType: 'json',
                        success: function (resp) {
                            $('#upload').hide();
                            alert(resp.message);
                        }
                    });
                },
                progressall: function (e, data) {
                    $(this).closest('.tp-bm-box').find('.vote-vedio-container ul').html('正在上传...');
                }
            });
        }

        $scope.uploadImg = function (id) {
            uploadImg(id, '/admin//section/uploadImg.do');
        }

        $scope.uploadImgAppSrc = function (id) {
            uploadImg(id, '/admin//section/uploadImgApp.do');
        }

        $scope.uploadImgBigAppSrc = function (id) {
            uploadImg(id, '/admin//section/uploadImgBigApp.do');
        }
        $scope.closeImg = function () {
            $('#upload').hide();
        }

        var parentId = "";
        var name = "";
        $scope.addSecondaryCategory = function (id) {
            parentId = id;
            $('#addSecondary').show();
            name = $('#aS').val();
            getSection(2, parentId);
        }

        $('#aS').blur(function () {
            if ($('#aS').val() == "") {
                $('#tip').show();
            } else {
                name = $('#aS').val();
                $('#tip').hide();
            }
        });

        $scope.closeDialog = function () {
            $('#addSecondary').hide();
        }

        $scope.submit = function () {
            var level = 2;
            $scope.section.parentId = parentId;
            $scope.section.name = name;
            $scope.section.level = 2;
            $.ajax({
                url: '/admin/section.do?',
                type: 'post',
                data: angular.toJson($scope.section),
                contentType: 'application/json',
                success: function (result) {
                    getSection(level, parentId);
                    alert(result.message);
                    //$('#addSecondary').hide();
                },
                error: function (e) {
                    alert('添加失败');
                }
            });
        }

    });
})()


    

