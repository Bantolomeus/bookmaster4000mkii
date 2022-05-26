module Styles = {
  open CssJs
  let header = style(. [display(#flex), padding(px(16))])
  let link = style(. [marginLeft(px(8))])
}

// todo proper styling for menu bar
@react.component
let make = () => {
  <div className=Styles.header>
    <Link className=Styles.link onClick={Link.location(Link.overall)}>
      {React.string("Overall")}
    </Link>
    <Link className=Styles.link onClick={Link.location(Link.today)}> {React.string("Today")} </Link>
  </div>
}
