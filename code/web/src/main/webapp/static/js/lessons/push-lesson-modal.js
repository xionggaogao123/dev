/**
 * Created by Niu Xin on 14/11/4.
 */
var app = angular.module('CloudClassPage', ['ui.bootstrap']);

app.controller('ModalCtrl', function ($scope, $modal, $log) {

    $scope.modal = null;

    $scope.open = function (size) {

        $scope.modal = $modal.open({
            templateUrl: 'pushModal.html',
            controller: 'ModalInstanceCtrl',
            size: size
        });

        $scope.modal.result.then(function (selectedItem) {
            $('#teacherDirUl').appendTo('#treeContainer');
            $scope.modal = null;
        }, function () {
            $('#teacherDirUl').appendTo('#treeContainer');
            $scope.modal = null;
        });
    };

    $scope.close = function () {
        $scope.modal.close();
    }
});

// Please note that $modalInstance represents a modal window (instance) dependency.
// It is not the same as the $modal service used above.

app.controller('ModalInstanceCtrl', function ($scope, $modalInstance) {
    $scope.ok = function () {
        $modalInstance.close();
    };
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});

app.directive('loadTree', function ($compile) {
    return function (scope, element, attrs) {
        $('#teacherDirUl').appendTo($(element));
    };
});

function openPushDialog() {
    var scope = angular.element('[ng-controller=ModalCtrl]').scope();
    scope.open();
}

function closePushDialog() {
    var scope = angular.element('[ng-controller=ModalCtrl]').scope();
    scope.close();
}