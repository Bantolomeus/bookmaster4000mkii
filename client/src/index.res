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
      <link rel="preconnect" href="https://fonts.googleapis.com" />
      <link rel="preconnect" href="https://fonts.gstatic.com" crossOrigin="anonymous" />
      <link
        href="https://fonts.googleapis.com/css2?family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&family=Work+Sans:ital,wght@0,200;0,400;0,800;1,200;1,400;1,800&display=swap"
        rel="stylesheet"
      />
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
