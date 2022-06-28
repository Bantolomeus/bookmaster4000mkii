let resolveRequest = (
  request: Future.t<result<Request.response<'payload>, Request.error>>,
  cb: 'payload => unit,
) => {
  Future.get(request, resolved => {
    switch resolved {
    | Ok({response}) =>
      switch response {
      | Some(response) => cb(response)
      | None => Js.log("could not parse response")
      }
    | Error(err) => Js.log2("request error", err)
    }
  })
}

let make = Request.make