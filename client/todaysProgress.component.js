module.exports.default = angular.module('todaysProgress', []).component('todaysProgress', {
    bindings: {},
    controller: TodaysProgress,
    template: require('./todaysProgress.component.html')
}).name;

TodaysProgress.$inject = [];
function TodaysProgress() {
    console.log('todaysProgress');
}
