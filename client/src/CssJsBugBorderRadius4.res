/* BROKEN `borderRadius4` bottom-left/-right
broken source: https://github.com/giraud/bs-css/blob/master/bs-css/src/Css_Js_Core.re
check out and write test: https://github.com/giraud/bs-css/tree/master/bs-css
PR with a fix: https://github.com/giraud/bs-css/pull/232
*/
module Styles = {
  open CssJs

  let btnOutlinePrimary = style(. [
    borderRadius4(
      ~bottomLeft=px(2) /* yields bottomRight */,
      ~bottomRight=px(1) /* yields bottomLeft */,
      ~topLeft=px(0),
      ~topRight=px(0),
    ),
  ])
}
