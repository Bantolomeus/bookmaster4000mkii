module.exports.default = angular.module('todaysProgress', []).component('todaysProgress', {
    bindings: {},
    controller: TodaysProgress,
    template: require('./todays-progress.component.html')
}).name;

TodaysProgress.$inject = ['$resource'];
function TodaysProgress($resource) {
    let ctrl = this;

    ctrl.$onInit = () => {
        ctrl.books = $resource('/books').query();
    };

    ctrl.nameToHexColor = name => {
        let hash = 0;
        for (let i = 0; i < name.length; i++) {
            hash = name.charCodeAt(i) + ((hash << 5) - hash);
        }
        let color = '#';
        for (let i = 0; i < 3; i++) {
            let value = (hash >> (i * 8)) & 0xFF;
            color += (`00${value.toString(16)}`).substr(-2);
        }
        let transparency10Percent = `33`; // cudos to @lopspower https://gist.github.com/lopspower/03fb1cc0ac9f32ef38f4
        return `${color}${transparency10Percent}`;
    }
}
