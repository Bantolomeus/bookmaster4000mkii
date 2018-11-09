import overallProgressComponent from './overall-progress.component';
import todaysProgressComponent from './todays-progress.component';
import navBarComponent from './nav-bar.component';
import routesConfig from './routes.config';

angular
  .module('bookmaster', [
    'ngResource',
    'ui.router',
    overallProgressComponent.default,
    todaysProgressComponent.default,
    navBarComponent.default
  ])
  .config(routesConfig.default);
