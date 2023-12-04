package day04

import kotlin.math.pow
import readInput

fun main() {
    val input = readInput("input/Day04.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>) = calcScores(input)
    .sumOf { if (it == 0) 0 else 2.pow(it - 1) }

private fun part2(input: List<String>): Long {
    val acc = Array(input.size + 1) { 0 }
    val scores = calcScores(input)
    acc[1]++
    acc[scores[0] + 1]--

    for (i in 1..<scores.size) {
        acc[i] += acc[i - 1]
        acc[i + 1] += acc[i] + 1
        acc[i + scores[i] + 1] -= acc[i] + 1
    }

    return acc.slice(scores.indices).sum().toLong() + scores.size
}

private fun calcScores(input: List<String>) = input.asSequence().map { it.split(":")[1].trim() }
    .map { BingoCard.fromVerticalBarSeparatedLine(it) }
    .map { it.score }
    .toList()

private fun Int.pow(x: Int): Long = this.toDouble().pow(x).toLong()

private data class BingoCard(val numbers: List<Int>, val winningNumbers: List<Int>) {
    val score = numbers.intersect(winningNumbers.toSet()).size

    companion object {
        fun fromVerticalBarSeparatedLine(value: String): BingoCard = value.split("|").let { (left, right) ->
            BingoCard(
                left.split(" ").mapNotNull { it.toIntOrNull() },
                right.split(" ").mapNotNull { it.toIntOrNull() },
            )
        }
    }
}
