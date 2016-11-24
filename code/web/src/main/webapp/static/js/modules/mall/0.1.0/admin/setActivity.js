/**
 * Created by admin on 2016/6/28.
 */
/**
 * Created by fl on 2016/1/30.
 */
'use strict';
(function () {
    var app = angular.module('myApp', []);
    app.controller('myCtrl', function ($http, $scope) {
        $scope.post = {
            fpostId: '',
            activityImage: '',
            activityMemo: '',
            activityStartTime: '',
            activityEndTime: '',
            inSet: -1,
            cate: 1
        };

        $scope.post.fpostId = $('body').attr('fpostId');

        (function () {
            if ($scope.post.fpostId) {
                getPost();
            }
        })();

        $scope.getPost = function () {
            getPost();
        };

        $scope.submit = function () {
            if (!$scope.post.fpostId || $scope.post.fpostId == '') {//新增
                alert("没有新增功能");
            } else {//修改
                $scope.post.activityStartTime = $('#startTime').val();
                $scope.post.activityEndTime = $('#endTime').val();
                $.ajax({
                    url: '/admin/post/' + $scope.post.fpostId + '.do?',
                    type: 'PUT',
                    data: angular.toJson($scope.post),
                    contentType: 'application/json',
                    success: function (result) {
                        alert(result.message);
                    },
                    error: function (e) {
                        console.log(e);
                        alert('添加失败');
                    }
                });
            }
        };

        $('#suggestImg').diyUpload({
            url: '/mall/images.do',
            success: function (data) {
                $scope.post.activityImage = data.message[0].path;
                console.info(data);
            },
            error: function (err) {
                console.info(err);
                //
            }
        });

        function getPost() {
            if ('' != $scope.post.fpostId && null != $scope.post.fpostId) {
                $http.get('/forum/post' + $scope.post.fpostId + '.do', {
                    params: {}
                }).then(function (response) {
                    $scope.post = response.data.dto;

                });
            } else {
                alert("请输入活动帖子id");
            }
        }

    });

})();




