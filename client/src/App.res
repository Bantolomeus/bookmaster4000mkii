@react.component
let make = () => {
  let route = Route.useRoute()

  <>
    <Header />
    {switch route {
    | Today => <Today />
    | Overall => <Overall />
    }}
  </>
}
