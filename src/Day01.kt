package day01

import readInput

fun main() {
    val input = readInput("input/Day01.txt")
    val searchTargets = (1..9).map { it.toString() } + digitMap.keys
    val answer = input.sumOf { line ->
        val firstDigit = line.findAnyOf(searchTargets)!!.second.toDigit()
        val lastDigit = line.findLastAnyOf(searchTargets)!!.second.toDigit()
        firstDigit * 10 + lastDigit
    }
    println(answer)
}

private fun String.toDigit(): Int = when {
    this in digitMap -> digitMap[this]!!
    else -> this.toInt()
}

private val digitMap = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
)
