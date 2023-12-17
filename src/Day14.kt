package day14

import readInput
import transpose

fun main() {
    val input = readInput("input/Day14.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Long = input.map { it.toCharArray().toList() }.transpose()
    .let(::flush)
    .let(::calcLoad)

private fun part2(input: List<String>): Long {
    val init = input.map { it.toCharArray().toList() }
    val boardCountMap = mutableMapOf<List<List<Char>>, Int>()
    var current = init.rotateRight().rotateRight()
    var count = 0
    while (true) {
        count++
        current = current
            .let(List<List<Char>>::rotateRight)
            .let(::flush) // north
            .let(List<List<Char>>::rotateRight)
            .let(::flush) // west
            .let(List<List<Char>>::rotateRight)
            .let(::flush) // south
            .let(List<List<Char>>::rotateRight)
            .let(::flush) // east
        if (current in boardCountMap) {
            val firstIndexOfLoop = boardCountMap[current]!!
            val loopCount = count - firstIndexOfLoop
            val modCount = (1_000_000_000 - firstIndexOfLoop + 1) % loopCount
            val board = boardCountMap.toList().find { (_, value) -> value == firstIndexOfLoop - 1 + modCount }!!.first
            return calcLoad(board.rotateRight())
        }
        boardCountMap[current] = count
    }
}

private fun calcLoad(board: List<List<Char>>): Long =
    board.sumOf { line ->
        line.foldIndexed(0) { index, acc, c ->
            if (c == 'O') acc + line.size - index
            else acc
        }.toLong()
    }

private fun flush(board: List<List<Char>>): List<List<Char>> =
    board.map { line ->
        line.joinToString("").split("#").joinToString("#") { split ->
            val rockCount = split.count { it == 'O' }
            "O".repeat(rockCount) + ".".repeat(split.length - rockCount)
        }.toCharArray().toList()
    }

private fun <T> List<List<T>>.rotateRight(): List<List<T>> =
    if (this.isEmpty()) this
    else (this[0].indices).map { j ->
        (this.indices).map { i -> this[this.size - i - 1][j] }
    }
