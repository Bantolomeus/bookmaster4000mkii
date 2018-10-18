// angular kind of needs jquery to be loaded before itself
window.$ = window.jQuery = window.jquery = require('../node_modules/jquery/dist/jquery.min.js');

// angular
require('../node_modules/angular/angular.min.js');
require('../node_modules/angular-sanitize/angular-sanitize.min.js');
require('../node_modules/@uirouter/angularjs/release/angular-ui-router.min.js');
require('../node_modules/angular-route/angular-route.min.js');

// the rest
require('../node_modules/bootstrap/dist/js/bootstrap.min.js');
require('bootstrap');
require('../node_modules/bootstrap/dist/css/bootstrap.min.css');

// our app
require('./app.js');
