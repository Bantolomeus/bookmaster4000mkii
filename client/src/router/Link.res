// Copied from https://github.com/jihchi/rescript-react-realworld-example-app/blob/main/src/component/Link.res

// not sure what this does - why does it have an apostrophe?
type location'

type onClickAction =
  | Location(location')
  | CustomFn(unit => unit)

let customFn = fn => CustomFn(fn)
let location = location => Location(location)

// type converters  (not changing the actual value)
external make: string => location' = "%identity"
external toString: location' => string = "%identity"

// feels like the routes are a bit all over the place
//  (get rid of `location'` and instead let `make` return a Route.t ?)
//  (maybe this is also interesting: https://rescript-lang.org/blog/release-9-0#cleaner-polyvariant-syntax)
let challenge = make("/#/challenge")
let books = make("/#/books")

let push: location' => unit = location => location->toString->RescriptReactRouter.push

let handleClick = (onClick, event) => {
  switch onClick {
  | Location(location) =>
    event->ReactEvent.Mouse.preventDefault
    location->toString->RescriptReactRouter.push
  | CustomFn(fn) => fn()
  }
  ignore()
}

let activeClass = "active"

@react.component
let make = (~className="", ~style=ReactDOM.Style.make(), ~onClick, ~children) => {
  let {hash} = RescriptReactRouter.useUrl()

  open Js_string2
  let isActive = switch onClick {
  | Location(location) => includes(location->toString->toLowerCase, hash->toLowerCase)
  | _ => false
  }

  let href = switch onClick {
  | Location(location) => Some(location->toString)
  | CustomFn(_fn) => None
  }

  <a
    className={isActive ? className ++ ` ${activeClass}` : className}
    ?href
    style
    onClick={handleClick(onClick)}>
    children
  </a>
}

// keep for later
module Button = {
  @react.component
  let make = (~className="", ~style=ReactDOM.Style.make(), ~onClick, ~disabled=false, ~children) =>
    <button className style onClick={handleClick(onClick)} disabled> children </button>
}
