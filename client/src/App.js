// Generated by ReScript, PLEASE EDIT WITH CARE
'use strict';

var CssJs = require("bs-css-emotion/src/CssJs.js");
var React = require("react");
var Future = require("rescript-future/src/Future.js");
var $$Request = require("rescript-request/src/Request.js");

var overallProgressContainer = CssJs.style([
      CssJs.textAlign(CssJs.center),
      CssJs.fontWeight({
            NAME: "num",
            VAL: 400
          }),
      CssJs.margin(CssJs.px(64))
    ]);

var header = CssJs.style([
      CssJs.padding2(CssJs.px(12), CssJs.px(20)),
      CssJs.marginBottom(CssJs.px(0)),
      CssJs.backgroundColor(CssJs.rgba(0, 0, 0, {
                NAME: "num",
                VAL: 0.03
              })),
      CssJs.border(CssJs.px(1), CssJs.solid, CssJs.rgba(0, 0, 0, {
                NAME: "num",
                VAL: 0.125
              })),
      CssJs.borderRadius4(CssJs.px(3), CssJs.px(3), CssJs.px(0), CssJs.px(0))
    ]);

var progress = CssJs.style([
      CssJs.flex3(1, 1, CssJs.auto),
      CssJs.minHeight(CssJs.px(1)),
      CssJs.padding(CssJs.px(20)),
      CssJs.borderLeft(CssJs.px(1), CssJs.solid, CssJs.rgba(0, 0, 0, {
                NAME: "num",
                VAL: 0.125
              })),
      CssJs.borderRight(CssJs.px(1), CssJs.solid, CssJs.rgba(0, 0, 0, {
                NAME: "num",
                VAL: 0.125
              })),
      CssJs.selector("& > h5", [
            CssJs.fontSize(CssJs.px(20)),
            CssJs.marginTop(CssJs.px(0)),
            CssJs.marginBottom(CssJs.px(12)),
            CssJs.fontWeight({
                  NAME: "num",
                  VAL: 500
                })
          ]),
      CssJs.selector("& > p", [CssJs.marginBottom(CssJs.px(0))])
    ]);

function background(progressValue) {
  var behindSchedule = 205.0 / (progressValue * -1.0 / 10.0) | 0;
  return CssJs.style([CssJs.backgroundColor(progressValue < 0 ? CssJs.rgb(255, behindSchedule, behindSchedule) : CssJs.rgb(223, 240, 216))]);
}

var challengeInfo = CssJs.style([
      CssJs.color(CssJs.hex("6c757d")),
      CssJs.padding2(CssJs.px(12), CssJs.px(20)),
      CssJs.backgroundColor(CssJs.rgba(0, 0, 0, {
                NAME: "num",
                VAL: 0.03
              })),
      CssJs.border(CssJs.px(1), CssJs.solid, CssJs.rgba(0, 0, 0, {
                NAME: "num",
                VAL: 0.125
              })),
      CssJs.borderRadius4(CssJs.px(0), CssJs.px(0), CssJs.px(3), CssJs.px(3))
    ]);

var Styles = {
  overallProgressContainer: overallProgressContainer,
  header: header,
  progress: progress,
  background: background,
  challengeInfo: challengeInfo
};

function App(Props) {
  var match = React.useState(function () {
        return 1;
      });
  var progress$1 = match[0];
  var match$1 = React.useState(function () {
        return 0;
      });
  console.log("doing request");
  Future.get($$Request.make("/books", undefined, /* JsonAsAny */5, undefined, undefined, undefined, undefined, undefined, undefined, undefined), (function (prim) {
          console.log(prim);
          
        }));
  return React.createElement("div", undefined, React.createElement("div", {
                  className: overallProgressContainer
                }, React.createElement("div", {
                      className: header
                    }, "Your reading progress compared to the challenge"), React.createElement("div", {
                      className: CssJs.merge([
                            progress,
                            background(progress$1)
                          ])
                    }, React.createElement("h5", undefined, progress$1), React.createElement("p", undefined, "Pages Ahead of Plan")), React.createElement("div", {
                      className: challengeInfo
                    }, "You should read " + String(match$1[0]) + " Pages per Day")));
}

var make = App;

exports.Styles = Styles;
exports.make = make;
/* overallProgressContainer Not a pure module */