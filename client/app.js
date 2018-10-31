import overallProgressComponent from './overall-progress.component';
import todaysProgressComponent from './todays-progress.component';
import navBarComponent from './nav-bar.component';
import routes from './routes.js';

angular
    .module('bookmaster', [
        'ngResource',
        'ui.router',
        overallProgressComponent.default,
        todaysProgressComponent.default,
        navBarComponent.default
    ])
    .config(routes.default);
