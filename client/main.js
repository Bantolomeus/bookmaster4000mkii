// angular kind of needs jquery to be loaded before itself
window.$ = window.jQuery = window.jquery = require('../node_modules/jquery/dist/jquery.min.js');

// angular
// require('../node_modules/angular/angular.min.js'); // for easier debugging
// require('../node_modules/angular/angular.js');
require('../node_modules/angular-ui-router/release/angular-ui-router.min.js'); // comes shipped with AngularJs
require('../node_modules/angular-sanitize/angular-sanitize.min.js');
require('../node_modules/angular-resource/angular-resource.min.js');

// the rest
require('../node_modules/bootstrap/dist/js/bootstrap.min.js');
require('bootstrap');
require('../node_modules/bootstrap/dist/css/bootstrap.min.css');

// our app
require('./app.js');
