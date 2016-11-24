/**
 * Created by wangkaidong on 2016/4/8.
 */
(function(){
    var app = angular.module('myApp', []);
    app.controller('myCtrl', function($http,$scope) {
        $scope.eGradeCategory = {id:'',name:'',sort:0};
        getGradeCategory();

        function getGradeCategory(){
            $http.get('/mall/admin/eGradeCategories.do', {
                params: {}
            }).then(function (response) {
                $scope.eGradeCategoryList = response.data;
            });
        }

        $scope.addGradeCategory = function(){
            $scope.eGradeCategory.sort = $('ul').children('li').length + 1;
            $.ajax({
                url: '/mall/admin/eGradeCategories.do',
                type: 'post',
                data: angular.toJson($scope.eGradeCategory),
                contentType: 'application/json',
                success: function (result) {
                    getGradeCategory();
                    alert(result.message);
                },
                error: function(e) {
                    alert('添加失败');
                }
            });
        }

        $scope.delete = function(id){
            $.ajax({
                url: '/mall/admin/deleteEGradeCategory.do',
                type: 'get',
                data: {id:id},
                success: function (result) {
                    getGradeCategory();
                    alert(result.message);
                }
            });
        }
    });
})()