// not sure why this has an apostrophe
type location'

type onClickAction =
  | Location(location')
  | CustomFn(unit => unit)

let customFn = fn => CustomFn(fn)
let location = location => Location(location)

// not sure what's happening here with the %identity
external make: string => location' = "%identity"
external toString: location' => string = "%identity"

let overall = make("/#/overall")
let today = make("/#/today")

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

@react.component
let make = (~className="", ~style=ReactDOM.Style.make(), ~onClick, ~children) => {
  let href = switch onClick {
  | Location(location) => Some(location->toString)
  | CustomFn(_fn) => None
  }
  <a className ?href style onClick={handleClick(onClick)}> children </a>
}

module Button = {
  @react.component
  let make = (~className="", ~style=ReactDOM.Style.make(), ~onClick, ~disabled=false, ~children) =>
    <button className style onClick={handleClick(onClick)} disabled> children </button>
}
