// css docs: https://github.com/giraud/bs-css/blob/master/bs-css/src/Css_Js_Core.rei
module Styles = {
  // Open the Css module, so we can access the style properties below without prefixing them with Css
  open CssJs

  let overallProgressContainer = style(. [textAlign(center), fontWeight(#num(400)), margin(px(64))])

  let header = style(. [
    padding2(~v=px(12), ~h=px(20)),
    marginBottom(px(0)),
    backgroundColor(rgba(0, 0, 0, #num(0.03))),
    border(px(1), solid, rgba(0, 0, 0, #num(0.125))),
    borderRadius4(~topLeft=px(3), ~topRight=px(3), ~bottomLeft=px(0), ~bottomRight=px(0)),
  ])

  let progress = style(. [
    flex3(~grow=1., ~shrink=1., ~basis=auto),
    minHeight(px(1)),
    padding(px(20)),
    borderLeft(px(1), solid, rgba(0, 0, 0, #num(0.125))),
    borderRight(px(1), solid, rgba(0, 0, 0, #num(0.125))),
    selector(.
      "& > h5",
      [fontSize(px(20)), marginTop(px(0)), marginBottom(px(12)), fontWeight(#num(500))],
    ),
    selector(. "& > p", [marginBottom(px(0))]),
  ])

  let background = progressValue => {
    let behindSchedule = Belt.Float.toInt(
      205.0 /. (Belt.Int.toFloat(progressValue) *. -1.0 /. 10.0),
    )

    style(. [
      backgroundColor(
        progressValue < 0 ? rgb(255, behindSchedule, behindSchedule) : rgb(223, 240, 216),
      ),
    ])
  }

  let challengeInfo = style(. [
    color(hex("6c757d")),
    padding2(~v=px(12), ~h=px(20)),
    backgroundColor(rgba(0, 0, 0, #num(0.03))),
    border(px(1), solid, rgba(0, 0, 0, #num(0.125))),
    borderRadius4(~topLeft=px(0), ~topRight=px(0), ~bottomLeft=px(3), ~bottomRight=px(3)),
  ])
}

// todo extract request helper
let resolveRequest = (
  request: Future.t<result<Request.response<'payload>, Request.error>>,
  cb: 'payload => unit,
) => {
  Future.get(request, resolved => {
    switch resolved {
    | Ok({response}) =>
      switch response {
      | Some(response) => cb(response)
      | None => Js.log("could not parse response")
      }
    | Error(err) => Js.log2("request error", err)
    }
  })
}

// todo extract page 1 component  (leave App for routing and dom skelleton)
type progressType = int
type challenge = {"pagesPerDay": int}

@react.component
let make = () => {
  let (progress, setProgress) = React.useState(_ => 1)
  React.useEffect1(() => {
    Request.make(
      ~method=#POST,
      ~url="/progress/calculate",
      ~responseType=(JsonAsAny: Request.responseType<progressType>),
      (),
    )->resolveRequest(response => setProgress(_ => response))
    None
  }, [])

  let (pagesPerDay, setPagesPerDay) = React.useState(_ => 0)
  React.useEffect1(() => {
    Request.make(
      ~url="/challenge",
      ~responseType=(JsonAsAny: Request.responseType<challenge>),
      (),
    )->resolveRequest(response => setPagesPerDay(_ => response["pagesPerDay"]))
    None
  }, [])

  <div>
    <div className=Styles.overallProgressContainer>
      <div className=Styles.header>
        {React.string("Your reading progress compared to the challenge")}
      </div>
      <div className={CssJs.merge(. [Styles.progress, Styles.background(progress)])}>
        <h5> {React.int(progress)} </h5> <p> {React.string("Pages Ahead of Plan")} </p>
      </div>
      <div className=Styles.challengeInfo>
        {React.string(`You should read ${Belt.Int.toString(pagesPerDay)} Pages per Day`)}
      </div>
    </div>
  </div>
}
