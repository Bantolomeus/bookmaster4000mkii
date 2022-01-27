let str = s => React.string(s)

module ProgressBar = {
  module Styles = {
    open CssJs
    let container = style(. [
      display(#flex),
      overflow(#hidden),
      lineHeight(px(0)),
      fontSize(px(12)),
      backgroundColor(hex("e9ecef")),
      borderRadius(px(4)),
      //
      flex3(~grow=1., ~shrink=1., ~basis=auto),
      alignSelf(#center),
      marginLeft(px(6)),
      height(px(6)),
    ])

    let filling = _width => style(. [width(pct(_width)), backgroundColor(hex("a8aaad"))])
  }
  @react.component
  let make = (~percent) =>
    <div className=Styles.container> <div className={Styles.filling(percent)} /> </div>
}

module InProgressStyles = {
  open CssJs

  let container = style(. [padding(px(24))])

  let label = style(. [
    fontSize(px(13)),
    color(hex("6c757d")),
    textTransform(#uppercase),
    margin(px(8)),
  ])

  let row = style(. [display(#flex), flexWrap(#wrap)])

  let bookContainer = style(. [
    position(#relative),
    display(#flex),
    flexWrap(#wrap),
    flexDirection(#column),
    minWidth(px(0)),
    wordWrap(#breakWord),
    backgroundColor(hex("fff")),
    backgroundClip(#borderBox),
    border(px(1), #solid, rgba(0, 0, 0, #num(0.125))),
    borderRadius(px(4)),
    width(px(288)),
    margin(px(8)),
  ])

  let bgColor = name => {
    let color = StringToColor.toHex(name)->Js_string2.replace("#", "")
    let lessOpaque = "7d"
    style(. [background(hex(color ++ lessOpaque))])
  }

  let book = style(. [
    position(#relative),
    flex3(~grow=1., ~shrink=1., ~basis=auto),
    minHeight(px(1)),
    padding(px(20)),
    selector(.
      "& > h1",
      [
        fontSize(px(20)),
        fontWeight(#num(500)),
        lineHeight(px(24)),
        margin3(~top=px(0), ~bottom=px(12), ~h=#auto),
      ],
    ),
  ])

  let progress = style(. [display(#flex), selector(. "& > div:first-child", [fontSize(px(12))])])
}

type progressType = int

let _books = [
  {
    "name": "Ensel und Krete",
    "author": "Walter Moers",
    "pagesTotal": 255,
    "currentPage": 100,
    "dateStarted": "04/02/2018",
    "readTime": 0,
  },
  {
    "name": "Harry Potski",
    "author": "Jon Doe",
    "pagesTotal": 255,
    "currentPage": 200,
    "dateStarted": "04/02/2018",
    "readTime": 0,
  },
]

@react.component
let make = () => {
  // let (progress, setProgress) = React.useState(_ => 1)

  // let (books, setBooks) = React.useState(_ => [])
  // React.useEffect1(() => {
  //   Request.make(
  //     ~method=#POST,
  //     ~url="/progress/calculate",
  //     ~responseType=(JsonAsAny: Request.responseType<array<{"name": string}>>),
  //     (),
  //   )->ResponseMapper.resolveRequest(books => setBooks(_ => books))
  //   None
  // }, [])

  let progressInPercent = book =>
    Js_math.min_float(
      book["currentPage"]->Belt.Float.fromInt /. book["pagesTotal"]->Belt.Float.fromInt *. 100.0,
      book["pagesTotal"]->Belt.Float.fromInt,
    )

  open Js.Array2
  <div>
    <div className=InProgressStyles.container>
      <div className=InProgressStyles.label> {str("In Progress")} </div>
      <div className=InProgressStyles.row>
        {
          // todo filter:$ctrl.onlyUnfinishedBooks | orderBy:'name'
          // todo ng-mouseover="$ctrl.toggleInputActiveFor(book, true)"
          // todo ng-mouseleave="$ctrl.toggleInputActiveFor(book, false)"
          _books
          ->map(book => {
            let name = book["name"]
            <div className=InProgressStyles.bookContainer key={name}>
              <div
                className={CssJs.merge(. [InProgressStyles.book, InProgressStyles.bgColor(name)])}>
                <h1> {str(name)} </h1>
                <div className=InProgressStyles.progress>
                  <div>
                    {str(
                      `${book["currentPage"]->Js.Int.toString} of ${book["pagesTotal"]->Js.Int.toString} pages read`,
                    )}
                  </div>
                  <ProgressBar percent={progressInPercent(book)} />
                </div>
              </div>
            </div>
          })
          ->React.array
        }
      </div>

      // <div style="display: flex; flex-wrap: wrap">
      //   <div
      //     ng-repeat="book in $ctrl.books | filter:$ctrl.onlyUnfinishedBooks | orderBy:'name'"
      //     ng-mouseover="$ctrl.toggleInputActiveFor(book, true)"
      //     ng-mouseleave="$ctrl.toggleInputActiveFor(book, false)"
      //     class="card card--progress"
      //   >
      //     <div
      //       ng-if="book.name"
      //       ng-style="{ background: $ctrl.nameToHexColor(book.name) }"
      //       class="card-body card-body--container"
      //     >
      //       <h5 class="card-title">{{ book.name }}</h5>

      //       <div style="display: flex">
      //         <div>
      //           <small
      //             >{{ book.currentPage }} of {{ book.pagesTotal }} pages read</small
      //           >
      //         </div>
      //         <div class="progress progress-bar--container">
      //           <div
      //             class="progress-bar color--side-information"
      //             ng-style="{ width: $ctrl.progressInPercent(book) + '%' }"
      //           ></div>
      //         </div>
      //       </div>

      // todo
      //       <div
      //         ng-if="$ctrl.inputsActiveOn === book"
      //         class="overlay--container--content-centered color--overlay overlay--container--fill-whole-parent"
      //       >
      //         <progress-input
      //           ng-if="
      //             !$ctrl.progressUpdating &&
      //             $ctrl.bookWithRequestConfirmation === null
      //           "
      //           book="book"
      //           on-progress-input="$ctrl.updateBookProgress(bookName, currentPage)"
      //         ></progress-input>

      //         <div ng-if="$ctrl.progressUpdating">updating ..</div>

      //         <div ng-if="$ctrl.bookWithRequestConfirmation === book.name">
      //           <strong>Current page successfully updated.</strong> <br />
      //           <a ui-sref="overall" class="href--semi-styled"
      //             >See Overall progress</a
      //           >
      //         </div>
      //       </div>
      //     </div>
      //   </div>
      // </div>

      // {_books->map(book => {<div key={book["name"]}> {str(book["name"])} </div>})->React.array}
    </div>
    <div> {str("Completed")} </div>
    // <InProgressContainer> {books.filterByIncomplete... => <Card onUpdate={_ => reloadBooks)} />} </InProgressContainer>
    // <InProgressContainer> {books.filterByIncomplete... => <Card state={"hover-and-type" | "submitted-waiting" | "success"} onUpdate />} </InProgressContainer>
    // <CompletedContainer> {books.filterByComplete... => <TextOrLetter />} </CompletedContainer>
  </div>
}
