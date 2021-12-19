module Styles = {
  // Open the Css module, so we can access the style properties below without prefixing them with Css
  open CssJs

  let card = style(. [
    display(flexBox),
    flexDirection(column),
    alignItems(stretch),
    backgroundColor(white),
    boxShadow(Shadow.box(~y=px(3), ~blur=px(5), rgba(0, 0, 0, #num(0.3)))),
  ])

  let title = style(. [fontSize(rem(1.5)), color(black), marginBottom(px(4))])
}

type response = {"name": string}

@react.component
let make = () => {
  Js.log("doing request")

  Request.make(
    ~url="/books",
    ~responseType=(JsonAsAny: Request.responseType<response>),
    (),
  )->Future.get(Js.log)

  <div style=Styles.card> <div style=Styles.title> {React.string("Hello World")} </div> </div>
}

