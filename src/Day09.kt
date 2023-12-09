package day09

import readInput

fun main() {
    val input = readInput("input/Day09.txt")
    val answer = input.map { it.split(" ").map { it.toLong() } }
        .map { predicateNextNumber(it) }
        .reduce(Long::plus)
    println(answer)
}

private fun predicateNextNumber(initialNumbers: List<Long>): Long {
    val numbersList: List<List<Long>> = generateSequence(listOf(initialNumbers)) { acc: List<List<Long>> ->
        val last = acc.last()
        val next = (0..<last.size - 1).map { last[it + 1] - last[it] }
        acc.plus<List<Long>>(next)
    }.first { numbersList -> numbersList.last().all { it == 0L } }

    return numbersList.reversed().fold(0L) { acc, numbers ->
        acc + numbers.last()
    }
}
