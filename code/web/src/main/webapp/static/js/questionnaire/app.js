/**
 * Created by Tony on 2015/1/7.
 */

var app = angular.module('questionnaire', ['ngRoute', 'angularFileUpload', 'ui.date', 'k6kt.filter', 'ui.bootstrap', 'angular-echarts'])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/new', {
            templateUrl: '/questionnaire/newpage'
        }).when('/static/:qId', {
            templateUrl: '/questionnaire/staticpage'
        }).when('/new2', {
            templateUrl: '/questionnaire/newpage2'
        }).when('/answer/:qId', {
            templateUrl: '/questionnaire/answerpage'
        }).when('/allRelevant/:qId',{
            templateUrl:'/questionnaire/relevantpage'
        }).otherwise({
            template: ''
        });
    }])
    .directive('embedSrc', [function () {
        return {
            link: function ($scope, $element, $attrs) {
                $scope.$watch(
                    function () {
                        return $attrs.embedSrc;
                    },
                    function () {
                        var clone = $element.clone().attr('src', $attrs.embedSrc);
                        $element.replaceWith(clone);
                        $element = clone;
                    }
                );
            }
        };
    }]);