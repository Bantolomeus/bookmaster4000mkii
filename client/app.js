import overallProgressComponent from './overallProgress.component';
import todaysProgressComponent from './todaysProgress.component';

// todo componentize navigation

angular
    .module('bookmaster', [
        'ngResource',
        'ui.router',
        overallProgressComponent.default,
        todaysProgressComponent.default,
    ])
    .config(Routes);

Routes.$inject = ['$stateProvider'];
function Routes($stateProvider) {
    $stateProvider
        .state({
            name: 'overallProgress',
            url: '/overallProgress',
            component: 'overallProgress'
        })
        .state({
            name: 'todaysProgress',
            url: '/todaysProgress',
            component: 'todaysProgress'
        });
}
