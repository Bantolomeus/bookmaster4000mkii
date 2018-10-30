module.exports.default = angular.module('todaysProgress', []).component('todaysProgress', {
    bindings: {},
    controller: TodaysProgress,
    template: require('./todays-progress.component.html')
}).name;

TodaysProgress.$inject = ['$resource', '$timeout', '$window'];
function TodaysProgress($resource, $timeout, $window) {
    let ctrl = this;
    let books = $resource('/books/:bookName', null, {
        'addBookProgress': {method: 'PUT', isArray: false}
    });

    ctrl.$onInit = () => {
        ctrl.books = books.query();
        ctrl.progressUpdating = false;
    };

    ctrl.toggleInputActiveFor = (book, shouldBeActive, elementId) => {
        if (shouldBeActive) {
            ctrl.inputsActiveOn = book;
        } else {
            ctrl.inputsActiveOn = null;
        }

        if (elementId) {
            $timeout(function() {
                // timeout for Angular scope.apply trigger
                let element = $window.document.getElementById(elementId);
                if (element) {
                    element.focus();
                }
            });
        }
    };

    ctrl.nameToHexColor = name => {
        let hash = 0;
        for (let i = 0; i < name.length; i++) {
            hash = name.charCodeAt(i) + ((hash << 5) - hash);
        }
        let color = '#';
        for (let i = 0; i < 3; i++) {
            let value = (hash >> (i * 8)) & 0xff;
            color += `00${value.toString(16)}`.substr(-2);
        }
        let transparency10Percent = `33`; // cudos to @lopspower https://gist.github.com/lopspower/03fb1cc0ac9f32ef38f4
        return `${color}${transparency10Percent}`;
    };

    ctrl.updateBookProgress = (book, currentPage) => {
        ctrl.progressUpdating = true;
        books.addBookProgress(
            {bookName: book},
            {currentPage: currentPage},
            () => ctrl.progressUpdating = false)
    };
}
