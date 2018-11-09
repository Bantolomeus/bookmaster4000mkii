import './overall-progress.component.less';

module.exports.default = angular
  .module('overallProgress', [])
  .component('overallProgress', {
    bindings: {},
    controller: OverallProgress,
    template: require('./overall-progress.component.html')
  }).name;

OverallProgress.$inject = ['$resource'];
function OverallProgress($resource) {
  let ctrl = this;
  let Progress = $resource('progress', null, {
    get: {
      method: 'GET',
      isArray: false,
      transformResponse: progress => {
        return {value: progress};
      }
    }
  });
  let Challenge = $resource('challenge');

  ctrl.$onInit = () => {
    ctrl.resource = null;
    ctrl.progress = Progress.get();
    ctrl.challenge = Challenge.get();
  };

  ctrl.deeperRedForMoreNegative = base => {
    let greenBlue = 205 / ((base * -1) / 10);
    return `rgb(255, ${greenBlue}, ${greenBlue})`;
  };
}
