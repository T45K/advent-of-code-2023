package day14

import readInput
import transpose

fun main() {
    val input = readInput("input/Day14.txt")
    val answer = input.map { it.toCharArray().toList() }.transpose()
        .sumOf { list ->
            list.foldIndexed(list.size to 0L) { index, (weight, acc), v ->
                when (v) {
                    '#' -> (list.size - index - 1) to acc
                    'O' -> (weight - 1) to (acc + weight)
                    else -> weight to acc
                }
            }.second
        }
    println(answer)
}
