package io.github.u1tramarinet.easysettings.utils

internal fun Int.fromValueToPercent(range: IntRange): Int {
    return ((this - range.first) * 100 / (range.last - range.first)).coerceIn(0, 100)
}

internal fun Int.fromPercentToValue(range: IntRange): Int {
    return (range.last - range.first) * this / 100 + range.first
}