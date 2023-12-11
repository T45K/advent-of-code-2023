package day11

import kotlin.math.max
import kotlin.math.min
import readInput

fun main() {
    val input = readInput("input/Day11.txt")
    val answer = part1(input)
    println(answer)
}

private fun part1(input: List<String>): Long {
    val rows = input.size
    val rowEmptyAcc = Array(rows) {
        if (input[it].contains("#")) 0
        else 1
    }
    for (i in 1..<rows) {
        rowEmptyAcc[i] += rowEmptyAcc[i - 1]
    }

    val cols = input[0].length
    val colEmptyAcc = Array(cols) {
        if (input.any { line -> line[it] == '#' }) 0
        else 1
    }
    for (i in 1..<cols) {
        colEmptyAcc[i] += colEmptyAcc[i - 1]
    }

    val points = (0..<rows).flatMap { i ->
        (0..<cols).mapNotNull { j ->
            if (input[i][j] == '#') Point(i, j)
            else null
        }
    }

    return points.flatMapIndexed { index: Int, (fromX, fromY): Point ->
        points.subList(index + 1, points.size).map { (toX, toY) ->
            val maxX = max(fromX, toX)
            val minX = min(fromX, toX)
            val maxY = max(fromY, toY)
            val minY = min(fromY, toY)
            maxX - minX + rowEmptyAcc[maxX] - rowEmptyAcc[minX] +
                maxY - minY + colEmptyAcc[maxY] - colEmptyAcc[minY]
        }.map { it.toLong() }
    }.sum()
}

private data class Point(val x: Int, val y: Int)
