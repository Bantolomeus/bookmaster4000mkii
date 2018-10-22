module.exports.default = angular.module('overallProgress', []).component('overallProgress', {
    bindings: {},
    controller: OverallProgress,
    template: require('./overallProgress.component.html')
}).name;

OverallProgress.$inject = ['$resource'];
function OverallProgress($resource) {
    let ctrl = this;

    activate();
    function activate() {
        ctrl.progress = $resource('/challenge').get(); // naming is supposed to match (issue #66)
    }

    ctrl.deeperRedForMoreNegative = function(base) {
        let greenBlue = 205 / ((base * -1) / 10);
        return `rgb(255, ${greenBlue}, ${greenBlue})`;
    };
}
