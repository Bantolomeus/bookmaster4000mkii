angular.module('bookmaster', ['ngResource']).controller('home', HomeController);

HomeController.$inject = ['$scope', '$resource'];
function HomeController($scope, $resource) {
    activate();
    function activate() {
        $scope.progress = $resource('/challenge').get(); // naming is supposed to match (issue #66)
    }

    $scope.redRgbForNegativeValue = function(base) {
        if (base < 0) {
            let value = 205 / ((base * -1) / 10);
            return `rgb(255, ${value}, ${value})`;
        } else {
            return 'rgb(223, 240, 216)';
        }
    };
}
