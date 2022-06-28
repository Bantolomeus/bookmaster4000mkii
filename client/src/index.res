%%raw(`
require("./index.css");
`)

module App = {
  @react.component
  let make = () => {
    let route = Route.useRoute()

    open CssJs
    let verticalAlign = style(. [height(#vh(100.)), display(#flex), flexDirection(column)])

    <div className=verticalAlign>
      <Navbar />
      {switch route {
      | Challenge => <Challenge />
      | Books => <Books />
      }}
    </div>
  }
}

switch ReactDOM.querySelector("#root") {
| Some(root) => ReactDOM.render(<App />, root)
| None => Js.log("Error: could not find react element '#root'")
}
