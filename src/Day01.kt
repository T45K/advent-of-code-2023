package day01

import readInput

fun main() {
    val input = readInput("input/Day01.txt")
    val answer = input.sumOf {
        val firstDigit = it.find(Char::isDigit).toString()
        val lastDigit = it.findLast(Char::isDigit).toString()
        (firstDigit + lastDigit).toInt()
    }
    println(answer)
}
