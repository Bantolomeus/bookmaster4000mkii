import overallProgressComponent from './overall-progress.component';
import todaysProgressComponent from './todays-progress.component';

module.exports.default = Routes;

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
