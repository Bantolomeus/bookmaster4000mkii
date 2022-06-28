// Generated by ReScript, PLEASE EDIT WITH CARE
'use strict';

var Link = require("./Link.js");
var CssJs = require("bs-css-emotion/src/CssJs.js");
var Curry = require("rescript/lib/js/curry.js");
var React = require("react");
var Js_dict = require("rescript/lib/js/js_dict.js");
var $$Request = require("rescript-request/src/Request.js");
var Belt_Option = require("rescript/lib/js/belt_Option.js");
var Caml_option = require("rescript/lib/js/caml_option.js");
var Belt_SetString = require("rescript/lib/js/belt_SetString.js");
var ResponseMapper = require("./ResponseMapper.js");
var StringToColor = require("string-to-color");

function str(s) {
  return s;
}

var label = CssJs.style([
      CssJs.fontSize(CssJs.px(13)),
      CssJs.color(CssJs.hex("6c757d")),
      CssJs.textTransform("uppercase"),
      CssJs.margin(CssJs.px(8))
    ]);

var GlobalStyles = {
  label: label
};

var container = CssJs.style([
      CssJs.display("flex"),
      CssJs.overflow("hidden"),
      CssJs.lineHeight(CssJs.px(0)),
      CssJs.fontSize(CssJs.px(12)),
      CssJs.backgroundColor(CssJs.hex("e9ecef")),
      CssJs.borderRadius(CssJs.px(4)),
      CssJs.flex3(1, 1, CssJs.auto),
      CssJs.alignSelf("center"),
      CssJs.marginLeft(CssJs.px(6)),
      CssJs.height(CssJs.px(6))
    ]);

function filling(_width) {
  return CssJs.style([
              CssJs.width(CssJs.pct(_width)),
              CssJs.backgroundColor(CssJs.darkgrey)
            ]);
}

var Styles = {
  container: container,
  filling: filling
};

function Today$ProgressBar(Props) {
  var percent = Props.percent;
  return React.createElement("div", {
              className: container
            }, React.createElement("div", {
                  className: filling(percent)
                }));
}

var ProgressBar = {
  Styles: Styles,
  make: Today$ProgressBar
};

var bookContainer = CssJs.style([
      CssJs.position("relative"),
      CssJs.display("flex"),
      CssJs.flexWrap("wrap"),
      CssJs.flexDirection("column"),
      CssJs.minWidth(CssJs.px(0)),
      CssJs.wordWrap("breakWord"),
      CssJs.backgroundColor(CssJs.hex("fff")),
      CssJs.backgroundClip("borderBox"),
      CssJs.border(CssJs.px(1), "solid", CssJs.rgba(0, 0, 0, {
                NAME: "num",
                VAL: 0.125
              })),
      CssJs.borderRadius(CssJs.px(4)),
      CssJs.width(CssJs.px(288)),
      CssJs.margin(CssJs.px(8))
    ]);

function bgColor(name) {
  var color = StringToColor(name).replace("#", "");
  return CssJs.style([CssJs.background(CssJs.hex(color + "7d"))]);
}

var book = CssJs.style([
      CssJs.position("relative"),
      CssJs.flex3(1, 1, CssJs.auto),
      CssJs.minHeight(CssJs.px(1)),
      CssJs.padding(CssJs.px(20)),
      CssJs.selector("& > h1", [
            CssJs.fontSize(CssJs.px(20)),
            CssJs.fontWeight({
                  NAME: "num",
                  VAL: 500
                }),
            CssJs.lineHeight(CssJs.px(24)),
            CssJs.margin3(CssJs.px(0), "auto", CssJs.px(12))
          ])
    ]);

var progress = CssJs.style([
      CssJs.display("flex"),
      CssJs.selector("& > div:first-child", [CssJs.fontSize(CssJs.px(12))])
    ]);

var overlay = CssJs.style([
      CssJs.background(CssJs.rgba(255, 255, 255, {
                NAME: "num",
                VAL: 0.8
              })),
      CssJs.display("flex"),
      CssJs.justifyContent("center"),
      CssJs.alignItems("center"),
      CssJs.position("absolute"),
      CssJs.top(CssJs.px(0)),
      CssJs.left(CssJs.px(0)),
      CssJs.width(CssJs.pct(100)),
      CssJs.height(CssJs.pct(100))
    ]);

var inputGroup = CssJs.style([
      CssJs.position("relative"),
      CssJs.display("flex"),
      CssJs.flexWrap("wrap"),
      CssJs.alignItems("stretch"),
      CssJs.width(CssJs.pct(100)),
      CssJs.selector("& > input", [
            CssJs.borderTopRightRadius(CssJs.px(0)),
            CssJs.borderBottomRightRadius(CssJs.px(0)),
            CssJs.position("relative"),
            CssJs.flex3(1, 1, CssJs.auto),
            CssJs.width(CssJs.pct(1)),
            CssJs.minWidth("zero"),
            CssJs.margin(CssJs.px(0))
          ])
    ]);

var formControl = CssJs.style([
      CssJs.display("block"),
      CssJs.height(CssJs.px(24)),
      CssJs.padding2(CssJs.px(6), CssJs.px(12)),
      CssJs.fontSize(CssJs.px(16)),
      CssJs.fontWeight({
            NAME: "num",
            VAL: 400
          }),
      CssJs.lineHeight(CssJs.px(24)),
      CssJs.color(CssJs.hex("495057")),
      CssJs.backgroundColor(CssJs.hex("fff")),
      CssJs.backgroundClip("paddingBox"),
      CssJs.border(CssJs.px(1), "solid", CssJs.hex("ced4da")),
      CssJs.borderRadius(CssJs.px(4)),
      CssJs.transition(undefined, undefined, undefined, "border-color .15s ease-in-out,box-shadow .15s ease-in-out"),
      CssJs.label("formControl")
    ]);

var inputGroupAppend = CssJs.style([
      CssJs.display("flex"),
      CssJs.marginLeft(CssJs.px(-1))
    ]);

var btnOutlinePrimary = CssJs.style([
      CssJs.cursor("pointer"),
      CssJs.display("inlineBlock"),
      CssJs.fontWeight({
            NAME: "num",
            VAL: 400
          }),
      CssJs.color(CssJs.hex("212529")),
      CssJs.textAlign("center"),
      CssJs.verticalAlign("middle"),
      CssJs.userSelect("none"),
      CssJs.backgroundColor("transparent"),
      CssJs.border(CssJs.px(1), "solid", "transparent"),
      CssJs.padding2(CssJs.px(6), CssJs.px(12)),
      CssJs.borderTopRightRadius(CssJs.px(4)),
      CssJs.borderBottomRightRadius(CssJs.px(4)),
      CssJs.fontSize(CssJs.px(16)),
      CssJs.lineHeight(CssJs.px(24)),
      CssJs.transition(undefined, undefined, undefined, "color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out"),
      CssJs.whiteSpace("nowrap"),
      CssJs.textTransform("none"),
      CssJs.margin(CssJs.px(0)),
      CssJs.color(CssJs.hex("fff")),
      CssJs.backgroundColor(CssJs.hex("1999e5")),
      CssJs.borderColor(CssJs.hex("1999e5")),
      CssJs.position(CssJs.relative),
      CssJs.zIndex(2)
    ]);

var hrefSemiStyled = CssJs.style([
      CssJs.textDecoration("underline"),
      CssJs.color(CssJs.black)
    ]);

var Styles$1 = {
  bookContainer: bookContainer,
  bgColor: bgColor,
  book: book,
  progress: progress,
  overlay: overlay,
  inputGroup: inputGroup,
  formControl: formControl,
  inputGroupAppend: inputGroupAppend,
  btnOutlinePrimary: btnOutlinePrimary,
  hrefSemiStyled: hrefSemiStyled
};

function Today$Book(Props) {
  var name = Props.name;
  var currentPage = Props.currentPage;
  var pagesTotal = Props.pagesTotal;
  var onCurrentPageUpdate = Props.onCurrentPageUpdate;
  var match = React.useState(function () {
        
      });
  var setOverlay = match[1];
  var overlay$1 = match[0];
  var match$1 = React.useState(function () {
        
      });
  var setUserInput = match$1[1];
  var userInput = match$1[0];
  var progressInPercent = function (currentPage, pagesTotal) {
    return Math.min(currentPage / pagesTotal * 100.0, pagesTotal);
  };
  var submitToBackend = function (param) {
    if (Belt_Option.isSome(userInput)) {
      Curry._1(setOverlay, (function (param) {
              return /* WaitingForResponse */1;
            }));
      Curry._1(setUserInput, (function (param) {
              
            }));
      return ResponseMapper.resolveRequest($$Request.make("/books/" + name, "PUT", /* Json */4, Caml_option.some(JSON.stringify({
                              currentPage: userInput
                            })), Caml_option.some(Js_dict.fromArray([[
                                "Content-Type",
                                "application/json"
                              ]])), undefined, undefined, undefined, undefined, undefined), (function (param) {
                    Curry._1(setOverlay, (function (param) {
                            return /* SuccessResponse */2;
                          }));
                    return Curry._1(onCurrentPageUpdate, undefined);
                  }));
    }
    
  };
  var tmp;
  if (overlay$1 !== undefined) {
    switch (overlay$1) {
      case /* ReceivingInput */0 :
          tmp = React.createElement("div", {
                className: overlay
              }, React.createElement("div", {
                    className: inputGroup
                  }, React.createElement("input", {
                        className: formControl,
                        autoFocus: true,
                        min: "1",
                        placeholder: "Current page ..",
                        type: "number",
                        value: userInput !== undefined ? userInput.toString() : "",
                        onKeyDown: (function (evt) {
                            if (evt.key === "Enter") {
                              return submitToBackend(undefined);
                            }
                            
                          }),
                        onChange: (function (evt) {
                            var value = evt.target.value;
                            return Curry._1(setUserInput, (function (param) {
                                          if (value !== "") {
                                            return value;
                                          }
                                          
                                        }));
                          })
                      }), React.createElement("div", {
                        className: inputGroupAppend
                      }, React.createElement("button", {
                            className: btnOutlinePrimary,
                            type: "button",
                            onClick: (function (param) {
                                return submitToBackend(undefined);
                              })
                          }, "Submit"))));
          break;
      case /* WaitingForResponse */1 :
          tmp = React.createElement("div", {
                className: overlay
              }, "Waiting...");
          break;
      case /* SuccessResponse */2 :
          tmp = React.createElement("div", {
                className: overlay
              }, React.createElement("strong", undefined, " Current page successfully updated."), React.createElement("br", undefined), React.createElement(Link.make, {
                    className: hrefSemiStyled,
                    onClick: Link.$$location(Link.overall),
                    children: "See Overall progress"
                  }));
          break;
      
    }
  } else {
    tmp = React.createElement(React.Fragment, undefined);
  }
  return React.createElement("div", {
              className: bookContainer,
              onMouseEnter: (function (param) {
                  return Curry._1(setOverlay, (function (param) {
                                return /* ReceivingInput */0;
                              }));
                }),
              onMouseLeave: (function (param) {
                  return Curry._1(setOverlay, (function (param) {
                                
                              }));
                })
            }, React.createElement("div", {
                  className: CssJs.merge([
                        book,
                        bgColor(name)
                      ])
                }, React.createElement("h1", undefined, name), React.createElement("div", {
                      className: progress
                    }, React.createElement("div", undefined, currentPage.toString() + " of " + pagesTotal.toString() + " pages read"), React.createElement(Today$ProgressBar, {
                          percent: progressInPercent(currentPage, pagesTotal)
                        })), tmp));
}

var Book = {
  Styles: Styles$1,
  make: Today$Book
};

var secondary = CssJs.style([
      CssJs.flexGrow(1),
      CssJs.padding(CssJs.px(24)),
      CssJs.backgroundColor(CssJs.hex("f8f9fa")),
      CssJs.borderTopStyle("solid"),
      CssJs.borderTopColor(CssJs.hex("dee2ef")),
      CssJs.borderTopWidth(CssJs.px(1))
    ]);

var completedBooksContainer = CssJs.style([
      CssJs.display("flex"),
      CssJs.flexWrap(CssJs.wrap)
    ]);

var highlight = CssJs.style([
      CssJs.lineHeight(CssJs.px(24)),
      CssJs.selector("& > span", [
            CssJs.textTransform(CssJs.uppercase),
            CssJs.marginLeft(CssJs.px(12)),
            CssJs.marginRight(CssJs.px(-6)),
            CssJs.borderRadius(CssJs.pct(50.0)),
            CssJs.backgroundColor(CssJs.hex("6c757d")),
            CssJs.padding2(CssJs.px(4), CssJs.px(8)),
            CssJs.color(CssJs.hex("f8f9fa"))
          ])
    ]);

var bookName = CssJs.style([
      CssJs.lineHeight(CssJs.px(24)),
      CssJs.selector("& > small", [CssJs.marginLeft(CssJs.px(16))])
    ]);

var Styles$2 = {
  secondary: secondary,
  completedBooksContainer: completedBooksContainer,
  highlight: highlight,
  bookName: bookName
};

function Today$CompletedBooks(Props) {
  var books = Props.books;
  var isNumber = function (maybeNumber) {
    return /\d/.test(maybeNumber);
  };
  var letters = Belt_SetString.toArray(Belt_SetString.fromArray(books.map(function (book) {
                var $$char = book.name.charAt(0).toLowerCase();
                if (isNumber($$char)) {
                  return "#";
                } else {
                  return $$char;
                }
              })));
  return React.createElement("div", {
              className: secondary
            }, React.createElement("div", {
                  className: label
                }, "Completed • " + books.length.toString()), React.createElement("div", {
                  className: completedBooksContainer
                }, letters.map(function (letter) {
                      return React.createElement(React.Fragment, {
                                  children: null,
                                  key: letter
                                }, React.createElement("div", {
                                      className: highlight
                                    }, React.createElement("span", undefined, letter)), books.filter(function (book) {
                                        if (letter === "#") {
                                          return isNumber(book.name.charAt(0));
                                        } else {
                                          return book.name.toLowerCase().startsWith(letter);
                                        }
                                      }).map(function (book) {
                                      return React.createElement("div", {
                                                  key: book.name,
                                                  className: bookName
                                                }, React.createElement("small", undefined, book.name));
                                    }));
                    })));
}

var CompletedBooks = {
  Styles: Styles$2,
  make: Today$CompletedBooks
};

var container$1 = CssJs.style([
      CssJs.height(CssJs.pct(100)),
      CssJs.display("flex"),
      CssJs.flexDirection(CssJs.column)
    ]);

var inProgressContainer = CssJs.style([CssJs.padding(CssJs.px(24))]);

var row = CssJs.style([
      CssJs.display("flex"),
      CssJs.flexWrap("wrap")
    ]);

var Styles$3 = {
  container: container$1,
  inProgressContainer: inProgressContainer,
  row: row
};

function Today(Props) {
  var match = React.useState(function () {
        return [];
      });
  var setBooks = match[1];
  var books = match[0];
  var loadBooks = function (param) {
    return ResponseMapper.resolveRequest($$Request.make("/books", "GET", /* JsonAsAny */5, undefined, undefined, undefined, undefined, undefined, undefined, undefined), (function (books) {
                  return Curry._1(setBooks, (function (param) {
                                return books;
                              }));
                }));
  };
  React.useEffect((function () {
          loadBooks(undefined);
          
        }), []);
  var inProgressBooks = React.createElement("div", {
        className: inProgressContainer
      }, React.createElement("div", {
            className: label
          }, "In Progress"), React.createElement("div", {
            className: row
          }, books.filter(function (param) {
                    return param.currentPage < param.pagesTotal;
                  }).sort(function (_1, _2) {
                  if (_1.name < _2.name) {
                    return -1;
                  } else {
                    return 1;
                  }
                }).map(function (param) {
                var name = param.name;
                return React.createElement(Today$Book, {
                            name: name,
                            currentPage: param.currentPage,
                            pagesTotal: param.pagesTotal,
                            onCurrentPageUpdate: loadBooks,
                            key: name
                          });
              })));
  return React.createElement("div", {
              className: container$1
            }, inProgressBooks, React.createElement(Today$CompletedBooks, {
                  books: books
                }));
}

var make = Today;

exports.str = str;
exports.GlobalStyles = GlobalStyles;
exports.ProgressBar = ProgressBar;
exports.Book = Book;
exports.CompletedBooks = CompletedBooks;
exports.Styles = Styles$3;
exports.make = make;
/* label Not a pure module */
