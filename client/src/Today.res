let str = s => React.string(s)

module GlobalStyles = {
  open CssJs
  let label = style(. [
    fontSize(px(13)),
    color(hex("6c757d")),
    textTransform(#uppercase),
    margin(px(8)),
  ])
}

type book = {
  name: string,
  author: string,
  pagesTotal: int,
  currentPage: int,
  dateStarted: string,
  readTime: int,
}

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
          margin(px(0)),
        ],
      ),
    ])

    let formControl = style(. [
      display(#block),
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
      </div>
    </div>
  }
}

module CompletedBooks = {
  module Styles = {
    open CssJs

    let secondary = style(. [
      flexGrow(1.),
      padding(px(24)),
      backgroundColor(hex("f8f9fa")),
      borderTopStyle(#solid),
      borderTopColor(hex("dee2ef")),
      borderTopWidth(px(1)),
    ])

    let completedBooksContainer = style(. [display(#flex), flexWrap(wrap)])

    let highlight = style(. [
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

  @react.component
  let make = (~books) => {
    let isNumber = maybeNumber => Js_re.test_(%re("/\d/"), maybeNumber)

    open Js_array2
    let letters =
      books
      ->map(book => {
        let char = book.name->Js_string2.charAt(0)->Js_string2.toLowerCase
        if char->isNumber {
          "#"
        } else {
          char
        }
      })
      ->Belt_SetString.fromArray // de-duplication
      ->Belt_SetString.toArray

    <div className=Styles.secondary>
      <div className=GlobalStyles.label>
        {str(`Completed • ${Js_array2.length(books)->Js_int.toString}`)}
      </div>
      <div className=Styles.completedBooksContainer>
        {letters
        ->map(letter => {
          <React.Fragment key={letter}>
            <div className=Styles.highlight> <span> {str(letter)} </span> </div>
            {books
            ->filter(book => {
              open Js_string2
              switch letter {
              | "#" => isNumber(book.name->charAt(0))
              | _ => startsWith(book.name->toLowerCase, letter)
              }
            })
            ->map(book => {
              <div className={Styles.bookName} key={book.name}>
                <small> {str(book.name)} </small>
              </div>
            })
            ->React.array}
          </React.Fragment>
        })
        ->React.array}
      </div>
    </div>
  }
}

module Styles = {
  open CssJs
  let container = style(. [height(pct(100.)), display(#flex), flexDirection(column)])
  let inProgressContainer = style(. [padding(px(24))])
  let row = style(. [display(#flex), flexWrap(#wrap)])
}

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

  open Js.Array2
  let inProgressBooks =
    <div className=Styles.inProgressContainer>
      <div className=GlobalStyles.label> {str("In Progress")} </div>
      <div className=Styles.row>
        {books
        ->filter(({pagesTotal, currentPage}) => currentPage < pagesTotal)
        ->sortInPlaceWith((_1, _2) => _1.name < _2.name ? -1 : 1)
        ->map(({name, currentPage, pagesTotal}) =>
          <Book name currentPage pagesTotal onCurrentPageUpdate=loadBooks key=name />
        )
        ->React.array}
      </div>
    </div>

  <div className=Styles.container> {inProgressBooks} <CompletedBooks books /> </div>
}
