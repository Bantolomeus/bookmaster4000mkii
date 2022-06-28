%%raw(`
require("./index.css");
`)

/* todo folder structure:
 --index
 --Header (Navbar?)
 --Books
 --Challenge
 --Router
 ----Link
 ----Route
 --Http
 ----ResponseMapper
 ----StringToColor (move!)
*/

module App = {
  @react.component
  let make = () => {
    let route = Route.useRoute()

    open CssJs
    let verticalAlign = style(. [height(#vh(100.)), display(#flex), flexDirection(column)])

    <div className=verticalAlign>
      <Header />
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
