package day01

import readInput

fun main() {
    val input = readInput("input/Day01.txt")
    val answer = input.map {
        val firstDigit = it.find(Char::isDigit).toString()
        val lastDigit = it.findLast(Char::isDigit).toString()
        (firstDigit + lastDigit).toInt()
    }.sum()
    println(answer)
}
