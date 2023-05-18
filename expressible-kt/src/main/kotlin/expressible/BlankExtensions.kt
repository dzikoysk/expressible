@file:Suppress("unused")

package expressible

fun expressible.Blank.toUnit() =
    Unit

fun Unit.toBlank(): expressible.Blank =
    expressible.Blank.BLANK