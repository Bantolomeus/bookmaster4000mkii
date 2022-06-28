module Styles = {
  open CssJs
  let fontStyles = style(. [
    display(#flex),
    padding(px(16)),
    selector(. "& > *", [marginRight(px(24))]),
    selector(.
      "& > a",
      [fontFamily(#custom("Libre Baskerville")), important(color(white)), textDecoration(none)],
    ),
    selector(. `& > .${Link.activeClass}`, [textDecoration(underline)]),
  ])
  let background = style(. [
    backgroundColor(hex("0093E9")),
    backgroundImage(
      linearGradient(deg(2.), [(#percent(0.), hex("0093E9")), (#percent(100.), hex("80D0C7"))]),
    ),
  ])
  let shadow = style(. [boxShadow(Shadow.box(~y=zero, ~x=zero, ~blur=px(7), black))])
  let icon = style(. [cursor(#default)])
}

@react.component
let make = () => {
  <div className={CssJs.merge(. [Styles.fontStyles, Styles.background, Styles.shadow])}>
    <span className=Styles.icon> {React.string(`📚`)} </span>
    <Link onClick={Link.location(Link.overall)}> {React.string("Overall")} </Link>
    <Link onClick={Link.location(Link.today)}> {React.string("Today")} </Link>
  </div>
}
