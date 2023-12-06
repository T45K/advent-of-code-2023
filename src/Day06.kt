package day06

import readInput

fun main() {
    val input = readInput("input/Day06.txt")
    val times = input[0].substringAfterExcluding("Time:").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    val distances = input[1].substringAfterExcluding("Distance:").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    val answer = times.zip(distances).map { (time, distance) ->
        (1..<time).count { holdingTime -> holdingTime * (time - holdingTime) > distance }
    }.fold(1) { acc, i -> acc * i }
    println(answer)
}

private fun String.substringAfterExcluding(delimiter: String): String {
    val index = this.indexOf(delimiter)
    return this.substring(index + delimiter.length)
}
