module Styles = {
  open CssJs

  let overallProgressContainer = style(. [textAlign(center), fontWeight(#num(400)), margin(px(64))])

  let header = style(. [
    padding2(~v=px(12), ~h=px(20)),
    marginBottom(px(0)),
    backgroundColor(rgba(0, 0, 0, #num(0.03))),
    border(px(1), solid, rgba(0, 0, 0, #num(0.125))),
    borderRadius4(~topLeft=px(3), ~topRight=px(3), ~bottomLeft=px(0), ~bottomRight=px(0)),
  ])
}

type progressType = int

@react.component
let make = () => {
  let (progress, setProgress) = React.useState(_ => 1)
  React.useEffect1(() => {
    Request.make(
      ~method=#POST,
      ~url="/progress/calculate",
      ~responseType=(JsonAsAny: Request.responseType<progressType>),
      (),
    )->ResponseMapper.resolveRequest(response => setProgress(_ => response))
    None
  }, [])

  <div className=Styles.overallProgressContainer> {React.string("sa books")} </div>
}
