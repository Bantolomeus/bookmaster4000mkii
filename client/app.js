angular.module('breaker', []).controller('home', HomeController);

HomeController.$inject = ['$scope'];
function HomeController($scope) {
  activate();
  function activate() {
    let uri = '/challenge';
    axios.get(uri).then(response => {
      console.log('request received');
      console.log(response);
      $scope.response = response.data;
      $scope.$apply();
    });
  }

  $scope.square = () => {
    $scope.squareOutput = $scope.squareInput * $scope.squareInput;
  };
}
