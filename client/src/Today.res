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

    let filling = _width => style(. [width(pct(_width)), backgroundColor(darkgrey)])
  }
  @react.component
  let make = (~percent) =>
    <div className=Styles.container> <div className={Styles.filling(percent)} /> </div>
}

module Book = {
  module Styles = {
    open CssJs

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

    let overlay = style(. [
      background(rgba(255, 255, 255, #num(0.8))),
      display(#flex),
      justifyContent(#center),
      alignItems(#center),
      // fill whole parent
      position(#absolute),
      top(px(0)),
      left(px(0)),
      width(pct(100.)),
      height(pct(100.)),
    ])
  }

  @react.component
  let make = (~name, ~currentPage, ~pagesTotal) => {
    let (hover, setHover) = React.useState(() => false)

    let progressInPercent = (~currentPage, ~pagesTotal) =>
      Js_math.min_float(
        currentPage->Belt.Float.fromInt /. pagesTotal->Belt.Float.fromInt *. 100.0,
        pagesTotal->Belt.Float.fromInt,
      )

    <div
      className=Styles.bookContainer
      onMouseOver={_ => setHover(_ => true)}
      onMouseLeave={_ => setHover(_ => false)}>
      <div className={CssJs.merge(. [Styles.book, Styles.bgColor(name)])}>
        <h1> {str(name)} </h1>
        <div className=Styles.progress>
          <div>
            {str(`${currentPage->Js.Int.toString} of ${pagesTotal->Js.Int.toString} pages read`)}
          </div>
          <ProgressBar percent={progressInPercent(~currentPage, ~pagesTotal)} />
        </div>
        {hover ? <div className=Styles.overlay> {str("")} </div> : <> </>}

        // todo
        // {switch Optional<updateStatus> {
        // | WaitingForResponse => expression
        // | ReceivingInput => expression
        // | SuccessResponse => expression
        // | NoHover => expression
        // }}

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
      </div>
    </div>
  }
}

type progressType = int

type book = {
  name: string,
  author: string,
  pagesTotal: int,
  currentPage: int,
  dateStarted: string,
  readTime: int,
}

let _books: array<book> = [
  {
    name: "Ensel und Krete",
    author: "Walter Moers",
    pagesTotal: 255,
    currentPage: 100,
    dateStarted: "04/02/2018",
    readTime: 0,
  },
  {
    name: "Harry Potski",
    author: "Jon Doe",
    pagesTotal: 255,
    currentPage: 200,
    dateStarted: "04/02/2018",
    readTime: 0,
  },
]

module Styles = {
  open CssJs

  let container = style(. [padding(px(24))])

  let label = style(. [
    fontSize(px(13)),
    color(hex("6c757d")),
    textTransform(#uppercase),
    margin(px(8)),
  ])

  let row = style(. [display(#flex), flexWrap(#wrap)])
}

@react.component
let make = () => {
  // todo get stuff from real backend
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

  open Js.Array2
  <div>
    <div className=Styles.container>
      <div className=Styles.label> {str("In Progress")} </div>
      <div className=Styles.row>
        {_books
        ->filter(({pagesTotal, currentPage}) => currentPage <= pagesTotal)
        ->sortInPlaceWith((_1, _2) => _1.name < _2.name ? -1 : 1) // ascending
        ->map(({name, currentPage, pagesTotal}) => <Book name currentPage pagesTotal key=name />)
        ->React.array}
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
