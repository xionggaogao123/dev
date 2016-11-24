/**
 * Created by fl on 2016/3/3.
 */
'use strict';
(function () {
    var app = angular.module('myApp', []);
    app.controller('myCtrl', function ($http, $scope) {
        $scope.eGoodsCategory = {
            id: '',
            parentId: '',
            level: 1,
            sort: 1,
            image: "",
            mobileImage: "",
            mobileCategoryImg: ""
        };
        var level = 1;
        var pId = "";
        getGoodsCategory(level, pId);

        function getGoodsCategory(level, pId) {
            $http.get('/admin/eGoodsCategorys.do', {
                params: {
                    level: level, parentId: pId
                }
            }).then(function (response) {
                if (level == 1) {
                    $scope.eGoodsCategoryList = response.data;
                } else if (level == 2) {
                    $scope.eGoodsCategoryListl = response.data;
                }

            });
        }

        var sort = 0;

        function getSort() {
            var self = $('.list-group').next('ul');
            sort = self.children().length + 1;
        }

        $scope.addGoodsCategory = function (level) {
            var self = angular.element(window.event.target);
            var ul = self.next('ul');
            $scope.eGoodsCategory.level = 1;
            $scope.eGoodsCategory.sort = ul.children().length + 1;
            $.ajax({
                url: '/admin/eGoodsCategorys.do?',
                type: 'post',
                data: angular.toJson($scope.eGoodsCategory),
                contentType: 'application/json',
                success: function (result) {
                    getGoodsCategory(level, "");
                    alert(result.message);
                },
                error: function (e) {
                    alert('添加失败');
                }
            });
        }

        $scope.deleteGoodsCategory = function (id, level, pId) {
            $http.delete('/admin/eGoodsCategorys/' + id + '.do', {
                params: {}
            }).then(function (response) {
                getGoodsCategory(level, pId);
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

        $scope.updateGoodsCategory = function (id, name, parentId, level, sort) {
            var self = angular.element(window.event.target);
            var url = '/admin/eGoodsCategorys/' + id + '.do';
            var param = {
                id: id,
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
            var url = '/admin/eGoodsCategorys/changeSort.do';
            var param = {selfId: selfId, selfSort: selfSort, anotherId: anotherId, anotherSort: anotherSort};
            $.ajax({
                url: url,
                type: 'POST',
                data: param,
                dataType: 'json',
                success: function () {
                    getGoodsCategory(1, "");
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
            uploadImg(id, '/admin//eGoodsCategorys/uploadImg.do');
        }

        $scope.uploadMobileImg = function (id) {
            uploadImg(id, '/admin//eGoodsCategorys/uploadMoblieImg.do');
        }

        $scope.uploadMobileCategoryImg = function (id) {
            uploadImg(id, '/admin//eGoodsCategorys/uploadMoblieCategoryImg.do');
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
            getGoodsCategory(2, parentId);
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
            $scope.eGoodsCategory.parentId = parentId;
            $scope.eGoodsCategory.name = name;
            $scope.eGoodsCategory.level = 2;
            $.ajax({
                url: '/admin/eGoodsCategorys.do?',
                type: 'post',
                data: angular.toJson($scope.eGoodsCategory),
                contentType: 'application/json',
                success: function (result) {
                    getGoodsCategory(level, parentId);
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


    

