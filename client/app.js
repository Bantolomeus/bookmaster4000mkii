angular.module('bookmaster', ['ngResource']).controller('home', HomeController);

HomeController.$inject = ['$scope', '$resource'];
function HomeController($scope, $resource) {
    activate();
    function activate() {
        $scope.progress = $resource('/challenge').get(); // naming is supposed to match (issue #66)
    }

    $scope.deeperRedForMoreNegative = function(base) {
        let greenBlue = 205 / ((base * -1) / 10);
        return `rgb(255, ${greenBlue}, ${greenBlue})`;
    };
}
