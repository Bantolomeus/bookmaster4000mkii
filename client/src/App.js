// Generated by ReScript, PLEASE EDIT WITH CARE
'use strict';

var Route = require("./Route.js");
var Today = require("./Today.js");
var React = require("react");
var Header = require("./Header.js");
var Overall = require("./Overall.js");

function App(Props) {
  var route = Route.useRoute(undefined);
  return React.createElement(React.Fragment, undefined, React.createElement(Header.make, {}), route ? React.createElement(Today.make, {}) : React.createElement(Overall.make, {}));
}

var make = App;

exports.make = make;
/* Route Not a pure module */