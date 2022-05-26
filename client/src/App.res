@react.component
let make = () => {
  let route = Route.useRoute()

  open CssJs
  let verticalAlign = style(. [height(#vh(100.)), display(#flex), flexDirection(column)])

  <div className=verticalAlign>
    <Header />
    {switch route {
    | Overall => <Overall />
    | Today => <Today />
    }}
  </div>
}
