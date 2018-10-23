import overallProgressComponent from './overall-progress.component';
import todaysProgressComponent from './todays-progress.component';
import navBarComponent from './nav-bar.component';

angular
    .module('bookmaster', [
        'ngResource',
        'ui.router',
        overallProgressComponent.default,
        todaysProgressComponent.default,
        navBarComponent.default
    ])
    .config(Routes);

Routes.$inject = ['$stateProvider', '$urlRouterProvider'];
function Routes($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state({
            name: 'overall',
            url: '/overall',
            component: overallProgressComponent.default
        })
        .state({
            name: 'today',
            url: '/today',
            component: todaysProgressComponent.default
        });
    $urlRouterProvider.otherwise('/overall');
}
