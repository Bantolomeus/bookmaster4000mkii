type t =
  | Challenge
  | Books

// Find more inspiration on https://github.com/jihchi/rescript-react-realworld-example-app/blob/main/src/App.res
let useRoute: unit => t = () => {
  let url = RescriptReactRouter.useUrl()
  let hash = url.hash->Js.String2.split("/")

  switch hash {
  | ["", "challenge"] => Challenge
  | ["", "books"] => Books
  | _ => Challenge
  }
}
