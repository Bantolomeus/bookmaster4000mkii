module.exports.default = angular
  .module('progressInput', [])
  .component('progressInput', {
    bindings: {onProgressInput: '&', book: '<'},
    controller: ProgressInput,
    template: require('./progress-input.component.html')
  }).name;

ProgressInput.$inject = ['$timeout', '$window'];
function ProgressInput($timeout, $window) {
  let ctrl = this;

  ctrl.$onInit = () => {
    $timeout(function() {
      // timeout for Angular scope.apply trigger
      let element = $window.document.getElementById('progress-input');
      if (element) {
        element.focus();
      }
    });
  };
}
