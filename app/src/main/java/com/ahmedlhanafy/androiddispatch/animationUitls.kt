package com.ahmedlhanafy.androiddispatch


fun interpolate(
    value: Float,
    input: ClosedFloatingPointRange<Float>,
    output: ClosedFloatingPointRange<Float>,
): Float {
    val (inputMin, inputMax) = input
    val (outputMin, outputMax) = output

    if (outputMin == outputMax) {
        return outputMin
    }
    return if (inputMin == inputMax) {
        if (value <= inputMin) {
            outputMin
        } else outputMax
    } else outputMin + (outputMax - outputMin) * (value - inputMin) / (inputMax - inputMin)
}

private operator fun <T : Comparable<T>> ClosedFloatingPointRange<T>.component1(): T {
    return this.start
}

private operator fun <T : Comparable<T>> ClosedFloatingPointRange<T>.component2(): T {
    return this.endInclusive
}


fun interpolate(
    value: Float,
    inputRange: List<Float>,
    outputRange: List<Float>,
): Float {
    val rangeIndex = findRangeIndex(value, inputRange)

    return interpolate(
        value,
        inputRange[rangeIndex]..inputRange[rangeIndex + 1],
        outputRange[rangeIndex]..outputRange[rangeIndex + 1],
    )
}

private fun findRangeIndex(value: Float, ranges: List<Float>): Int {
    var index = 1
    while (index < ranges.size - 1) {
        if (ranges[index] >= value) {
            break
        }
        index++
    }
    return index - 1
}
