module Styles = {
  open CssJs

  let header = style(. [display(#flex), padding(px(16))])

  let link = style(. [marginLeft(px(8))])
}

@react.component
let make = () => {
  <div className=Styles.header>
    <Link className=Styles.link onClick={Link.overall->Link.location}>
      {"Overall"->React.string}
    </Link>
    <Link className=Styles.link onClick={Link.today->Link.location}> {"Today"->React.string} </Link>
  </div>
}
