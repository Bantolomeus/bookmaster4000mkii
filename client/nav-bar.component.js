module.exports.default = angular.module('navBar', []).component('navBar', {
    bindings: {},
    controller: NavBar,
    template: require('./nav-bar.component.html')
}).name;

NavBar.$inject = [];
function NavBar() {
    console.log('navbar');
}
