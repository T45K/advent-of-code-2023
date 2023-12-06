package day06

import readInput

fun main() {
    val input = readInput("input/Day06.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Long {
    val times = split(input[0], "Time:")
    val distances = split(input[1], "Distance:")
    return times.zip(distances).map { (time, distance) ->
        (1..<time).count { holdingTime -> holdingTime * (time - holdingTime) > distance }
    }.fold(1) { acc, i -> acc * i }
}

private fun part2(input: List<String>): Int {
    val time = concat(input[0], "Time:")
    val distance = concat(input[1], "Distance:")
    return (1..<time).count { holdingTime -> holdingTime * (time - holdingTime) > distance }
}

private fun split(line: String, delimiter: String): List<Long> =
    line.substringAfterExcludingDelimiter(delimiter)
        .split(" ")
        .filter { it.isNotBlank() }
        .map { it.toLong() }

private fun concat(line: String, delimiter: String): Long =
    line.substringAfterExcludingDelimiter(delimiter)
        .split(" ")
        .filter { it.isNotBlank() }
        .joinToString("")
        .toLong()

private fun String.substringAfterExcludingDelimiter(delimiter: String): String {
    val index = this.indexOf(delimiter)
    return this.substring(index + delimiter.length)
}
