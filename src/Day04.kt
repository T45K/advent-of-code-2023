package day04

import kotlin.math.pow
import readInput

fun main() {
    val input = readInput("input/Day04.txt")
    val answer = input.asSequence().map { it.split(":")[1].trim() }
        .map { it.split("|") }
        .map { (numbersStr, winningNumbersStr) -> separateNumbers(numbersStr) to separateNumbers(winningNumbersStr) }
        .map { (numbers, winningNumbers) -> numbers.intersect(winningNumbers.toSet()).size }
        .sumOf { if (it == 0) 0 else 2.pow(it - 1) }
    println(answer)
}

private fun separateNumbers(str: String): List<Int> =
    str.split(" ").mapNotNull { it.toIntOrNull() }

private fun Int.pow(x: Int): Long = this.toDouble().pow(x).toLong()
