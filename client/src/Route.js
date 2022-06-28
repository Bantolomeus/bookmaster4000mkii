// Generated by ReScript, PLEASE EDIT WITH CARE
'use strict';

var RescriptReactRouter = require("@rescript/react/src/RescriptReactRouter.js");

function useRoute(param) {
  var url = RescriptReactRouter.useUrl(undefined, undefined);
  var hash = url.hash.split("/");
  if (hash.length !== 2) {
    return /* Challenge */0;
  }
  var match = hash[0];
  if (match !== "") {
    return /* Challenge */0;
  }
  var match$1 = hash[1];
  switch (match$1) {
    case "books" :
        return /* Books */1;
    case "challenge" :
        return /* Challenge */0;
    default:
      return /* Challenge */0;
  }
}

exports.useRoute = useRoute;
/* RescriptReactRouter Not a pure module */
