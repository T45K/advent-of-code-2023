package day15

import readInput

fun main() {
    val input = readInput("input/Day15.txt")
    val answer = input[0].split(',').sumOf { str ->
        str.toCharArray().map { it.code.toLong() }.fold(0L) { acc, v -> (acc + v) * 17 % 256 }
    }
    println(answer)
}
