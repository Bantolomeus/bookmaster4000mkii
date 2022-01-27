@react.component
let make = () => {
  let route = Route.useRoute()

  <>
    <Header />
    {switch route {
    | Overall => <Overall />
    | Today => <Today />
    }}
  </>
}
