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

    // todo cleanup css: Look at dev tools what styles get overwritten
    let inputGroup = style(. [
      position(#relative),
      display(#flex),
      flexWrap(#wrap),
      alignItems(#stretch),
      width(pct(100.)),
      selector(.
        "& > input",
        [
          borderTopRightRadius(px(0)),
          borderBottomRightRadius(px(0)),
          position(#relative),
          flex3(~grow=1., ~shrink=1., ~basis=auto),
          width(pct(1.)),
          minWidth(#zero),
          marginBottom(#zero),
          margin(px(0)),
        ],
      ),
    ])

    let formControl = style(. [
      display(#block),
      width(pct(100.)),
      height(px(24)),
      padding2(~v=px(6), ~h=px(12)),
      fontSize(px(16)),
      fontWeight(#num(400)),
      lineHeight(px(24)),
      color(hex("495057")),
      backgroundColor(hex("fff")),
      backgroundClip(#paddingBox),
      border(px(1), #solid, hex("ced4da")),
      borderRadius(px(4)),
      transition("border-color .15s ease-in-out,box-shadow .15s ease-in-out"),
      label("formControl"),
    ])

    let inputGroupAppend = style(. [display(#flex), marginLeft(px(-1))])

    let btnOutlinePrimary = style(. [
      // btn
      cursor(#pointer),
      display(#inlineBlock),
      fontWeight(#num(400)),
      color(hex("212529")),
      textAlign(#center),
      verticalAlign(#middle),
      userSelect(#none),
      backgroundColor(#transparent),
      border(px(1), #solid, #transparent),
      padding2(~v=px(6), ~h=px(12)),
      borderTopRightRadius(px(4)),
      borderBottomRightRadius(px(4)),
      fontSize(px(16)),
      lineHeight(px(24)),
      transition(
        "color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out",
      ),
      whiteSpace(#nowrap),
      textTransform(#none),
      margin(px(0)),
      // btn-outline
      color(hex("fff")),
      backgroundColor(hex("007bff")) /* #6c757d is secondary color */,
      borderColor(hex("007bff")),
      position(relative),
      zIndex(2),
    ])

    let hrefSemiStyled = style(. [textDecoration(#underline), color(black)])
  }

  type overlay = ReceivingInput | WaitingForResponse | SuccessResponse

  @react.component
  let make = (~name, ~currentPage, ~pagesTotal, ~onCurrentPageUpdate) => {
    // let (overlay, setOverlay) = React.useState(() => Some(SuccessResponse))
    let (overlay, setOverlay) = React.useState(() => None)
    let (userInput, setUserInput) = React.useState(() => None)

    let progressInPercent = (~currentPage, ~pagesTotal) =>
      Js_math.min_float(
        currentPage->Belt.Float.fromInt /. pagesTotal->Belt.Float.fromInt *. 100.0,
        pagesTotal->Belt.Float.fromInt,
      )

    let submitToBackend = () => {
      if userInput->Belt_Option.isSome {
        setOverlay(_ => Some(WaitingForResponse))
        setUserInput(_ => None)
        Request.make(
          ~method=#PUT,
          ~url=`/books/${name}`,
          ~body={"currentPage": userInput->Belt_Option.getUnsafe}->Js_json.stringifyAny,
          ~headers=Js_dict.fromArray([("Content-Type", "application/json")]),
          ~responseType=Json,
          (),
        )->ResponseMapper.resolveRequest(_ => {
          setOverlay(_ => Some(SuccessResponse))
          onCurrentPageUpdate()
        })
      }
    }

    <div
      className=Styles.bookContainer
      onMouseEnter={_ =>
        setOverlay(_ => {
          Some(ReceivingInput)
        })}
      onMouseLeave={_ => setOverlay(_ => None)}>
      <div className={CssJs.merge(. [Styles.book, Styles.bgColor(name)])}>
        <h1> {str(name)} </h1>
        <div className=Styles.progress>
          <div>
            {str(`${currentPage->Js.Int.toString} of ${pagesTotal->Js.Int.toString} pages read`)}
          </div>
          <ProgressBar percent={progressInPercent(~currentPage, ~pagesTotal)} />
        </div>
        {switch overlay {
        | Some(ReceivingInput) =>
          <div className=Styles.overlay>
            <div className=Styles.inputGroup>
              <input
                type_="number"
                placeholder="Current page .."
                value={switch userInput {
                | Some(value) => Js_int.toString(value)
                | None => ""
                }}
                onChange={evt => {
                  let value = (evt->ReactEvent.Form.target)["value"]
                  setUserInput(_ => value != "" ? Some(value) : None)
                }}
                onKeyDown={evt => {
                  if ReactEvent.Keyboard.key(evt) == "Enter" {
                    submitToBackend()
                  }
                }}
                min="1"
                className=Styles.formControl
                autoFocus=true
              />
              <div className=Styles.inputGroupAppend>
                <button
                  onClick={_ => submitToBackend()}
                  type_="button"
                  className=Styles.btnOutlinePrimary>
                  {str("Submit")}
                </button>
              </div>
            </div>
          </div>
        | Some(WaitingForResponse) => <div className=Styles.overlay> {str("Waiting...")} </div>
        | Some(SuccessResponse) =>
          <div className=Styles.overlay>
            <strong> {str(" Current page successfully updated.")} </strong>
            <br />
            <Link onClick={Link.location(Link.overall)} className=Styles.hrefSemiStyled>
              {str("See Overall progress")}
            </Link>
          </div>
        | None => <> </>
        }}

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

  let secondaryContainer = style(. [
    backgroundColor(hex("f8f9fa")),
    borderTopStyle(#solid),
    borderTopColor(hex("dee2ef")),
    borderTopWidth(px(1)),
  ])

  // COMPLETED BOOKS COMPONENT
  let completedBooksContainer = style(. [display(#flex), flexWrap(wrap)])

  let letter = style(. [
    lineHeight(px(24)),
    selector(.
      "& > span",
      [
        textTransform(uppercase),
        marginLeft(px(12)),
        marginRight(px(-6)),
        // circle styles
        borderRadius(pct(50.0)),
        backgroundColor(hex("6c757d")),
        padding2(~v=px(4), ~h=px(8)),
        color(hex("f8f9fa")),
      ],
    ),
  ])

  let bookName = style(. [lineHeight(px(24)), selector(. "& > small", [marginLeft(px(16))])])
}

type book = {
  name: string,
  author: string,
  pagesTotal: int,
  currentPage: int,
  dateStarted: string,
  readTime: int,
}

// todo height: 100% ("completed" section soll ganz bis runter spannen)
@react.component
let make = () => {
  let (books, setBooks) = React.useState(_ => [])

  let loadBooks = () => {
    Request.make(
      ~method=#GET,
      ~url="/books",
      ~responseType=(JsonAsAny: Request.responseType<array<book>>),
      (),
    )->ResponseMapper.resolveRequest(books => setBooks(_ => books))
  }

  React.useEffect1(() => {
    loadBooks()
    None
  }, [])

  /* Alte Version:
  div display: flex; flex-wrap: wrap
    div line-height: 24px <!-- egal ob Anfangsbuchstabe oder Book.name, beides ist inline-block auf selber Ebene -->
      span <!-- if Anfangsbuchstabe --> rounded-circle round-evenly margin-left: 12px
      small <!-- if Book --> books-completed
 */

  // erst Liste von Anfangsbuchstaben
  // dann im zweiten Schritt ein lookup fuer jeden der Anfangsbuchstaben
  let completedBooks = () => {
    let letters =
      books
      ->Js_array2.map(book => {
        let char = book.name->Js_string2.charAt(0)->Js_string2.toLowerCase
        if Js_re.test_(%re("/\d/"), char) {
          "#"
        } else {
          char
        }
      })
      ->Belt_SetString.fromArray // de-duplication
      ->Belt_SetString.toArray

    /*
      div display: flex; flex-wrap: wrap
        for letter {
          div line-height: 24px
            span rounded-circle round-evenly margin-left: 12px letter
          for book {
            div line-height: 24px
              small books-completed book.name
          }
        }
 */

    <div className=Styles.completedBooksContainer>
      {letters
      ->Js_array2.map(letter => {
        <React.Fragment key={letter}>
          <div className=Styles.letter> <span> {str(letter)} </span> </div>
          {books
          ->Js_array2.filter(book => {
            // todo #.starsWith(1,2,..) funzt nich
            Js_string2.startsWith(book.name->Js_string2.toLowerCase, letter)
          })
          ->Js_array2.map(book => {
            <div className={Styles.bookName} key={book.name}>
              <small> {str(book.name)} </small>
            </div>
          })
          ->React.array}
        </React.Fragment>
      })
      ->React.array}
    </div>
  }

  /* folgende Version geht auch, aber komplizierter!
  let groupedAlphabetically = books->Js_array2.reduce((acc, book) => {
    let firstLetter = if Js_re.test_(%re("/^\d/"), book.name) {
      "#"
    } else {
      book.name->Js.String2.charAt(0)
    }

    switch acc->Belt.MutableMap.String.get(firstLetter) {
    | Some(row) =>
      row->Js.Array2.push(book)->ignore
    | None =>
      acc->Belt.MutableMap.String.set(firstLetter, [book])
    }
    acc

    // if acc->Belt.MutableMap.String.has(firstLetter) {
    //   acc->Belt.MutableMap.String.get(firstLetter)->Js.Array2.push(book)
    // } else {
    //   acc[firstLetter] = [book]
    // }

    // switch acc[firstLetter] {
    // | Some(_) => acc[firstLetter]->Js.Array2.push(book)
    // | None => acc[firstLetter] = [book]
    // }
    // acc
  }, Belt.MutableMap.String.make())

  Js_console.log(groupedAlphabetically->Belt.MutableMap.String.getUndefined("#"))  // funzt!
 */

  // Ich wollte Logik ähnlich machen wie in der Angular Version, die Zeilen hier drunter sind kopiert von dort
  /*
      const _indexedBooks = books.reduce((acc, book) => {
       let firstLetter;
       if (/^\d/.test(book.name)) {
         firstLetter = "#";
       } else {
         firstLetter = book.name.charAt(0);
       }

       if (acc[firstLetter] === undefined) {
         acc[firstLetter] = [book];
       } else {
         acc[firstLetter].push(book);
       }
       return acc;
     }, {});

     ctrl.indexedBooks = Object.entries(_indexedBooks)
       .sort(([firstLetterA, _], [firstLetterB, __]) =>
         firstLetterA.localeCompare(firstLetterB)
       )
       .map(([firstLetter, books]) => {
         return [
           { type: "letter", value: firstLetter },
           ...books.map((book) => ({ type: "book", value: book })),
         ];
       })
       .flat();
   });
 */

  open Js.Array2
  <div>
    <div className=Styles.container>
      <div className=Styles.label> {str("In Progress")} </div>
      <div className=Styles.row>
        {books
        ->filter(({pagesTotal, currentPage}) => currentPage < pagesTotal)
        ->sortInPlaceWith((_1, _2) => _1.name < _2.name ? -1 : 1)
        ->map(({name, currentPage, pagesTotal}) =>
          <Book name currentPage pagesTotal onCurrentPageUpdate=loadBooks key=name />
        )
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
    <div className={CssJs.merge(. [Styles.container, Styles.secondaryContainer])}>
      // todo make *Completed* a component
      <div className=Styles.label> {str(`Completed • ${length(books)->Js_int.toString}`)} </div>
      {completedBooks()}
    </div>
    // <InProgressContainer> {books.filterByIncomplete... => <Card onUpdate={_ => reloadBooks)} />} </InProgressContainer>
    // <InProgressContainer> {books.filterByIncomplete... => <Card state={"hover-and-type" | "submitted-waiting" | "success"} onUpdate />} </InProgressContainer>
    // <CompletedContainer> {books.filterByComplete... => <TextOrLetter />} </CompletedContainer>
  </div>
}
