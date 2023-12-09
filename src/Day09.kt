package day09

import readInput

fun main() {
    val input = readInput("input/Day09.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Long =
    input.map { it.split(" ").map(String::toLong) }
        .map(::predicateNextNumber)
        .reduce(Long::plus)

private fun part2(input: List<String>): Long =
    input.map { it.split(" ").map(String::toLong) }
        .map(::predicatePreviousNumber)
        .reduce(Long::plus)

private fun predicateNextNumber(initialNumbers: List<Long>): Long {
    val numbersList: List<List<Long>> = reproduceHistory(initialNumbers)
    return numbersList.reversed().fold(0L) { acc, numbers ->
        acc + numbers.last()
    }
}

private fun predicatePreviousNumber(initialNumbers: List<Long>): Long {
    val numbersList: List<List<Long>> = reproduceHistory(initialNumbers)
    return numbersList.reversed().fold(0L) { acc, numbers ->
        numbers.first() - acc
    }
}

private fun reproduceHistory(initialNumbers: List<Long>): List<List<Long>> =
    generateSequence(listOf(initialNumbers)) { acc: List<List<Long>> ->
        val last = acc.last()
        val next = (0..<last.size - 1).map { last[it + 1] - last[it] }
        acc.plus<List<Long>>(next)
    }.first { numbersList -> numbersList.last().all { it == 0L } }
