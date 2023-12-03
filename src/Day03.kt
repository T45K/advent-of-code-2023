package day03

import kotlin.math.max
import kotlin.math.min
import readInput

fun main() {
    val input = readInput("input/Day03.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Long {
    val schematic = Schematic(input)
    return (0..<schematic.rowSize).sumOf { rowIndex ->
        schematic.enumerateNumberRanges(rowIndex)
            .filter { schematic.isAdjacentToSymbols(rowIndex, it) }
            .mapNotNull { schematic.extractNumber(rowIndex, it) }
            .sum()
    }
}

private fun part2(input: List<String>): Long {
    val schematic = Schematic(input)
    return (0..<schematic.rowSize).flatMap { rowIndex ->
        schematic.enumerateNumberRanges(rowIndex)
            .mapNotNull { range -> schematic.findAdjacentGear(rowIndex, range)?.let { Triple(it, rowIndex, range) } }
    }.groupBy({ (point, _, _) -> point }) { (_, rowIndex, range) -> rowIndex to range }
        .values
        .filter { it.size == 2 }
        .sumOf { (range1, range2) ->
            schematic.extractNumber(range1.first, range1.second)!! *
                schematic.extractNumber(range2.first, range2.second)!!
        }
}

private class Schematic(private val rows: List<String>) {
    val rowSize = rows.size
    private val colSize = rows[0].length

    private fun Char.isSymbol() = !(this.isDigit() || this == '.')

    fun extractNumber(rowIndex: Int, range: IntRange): Long? {
        require(
            rowIndex in 0..<rowSize &&
                0 <= range.first && range.last < colSize
        )
        return rows[rowIndex].substring(range).toLongOrNull()
    }

    fun enumerateNumberRanges(rowIndex: Int): List<IntRange> =
        rows[rowIndex].foldIndexed(emptyList<IntRange>() to null as IntRange?) { index, (acc, tmp), c ->
            if (c.isDigit()) {
                if (tmp == null) acc to index..index
                else acc to tmp.first..index
            } else {
                if (tmp == null) acc to null
                else acc.append(tmp) to null
            }
        }.let { (acc, tmp) ->
            if (tmp != null) acc.append(tmp)
            else acc
        }

    fun isAdjacentToSymbols(rowIndex: Int, range: IntRange): Boolean {
        require(
            rowIndex in 0..<rowSize &&
                0 <= range.first && range.last < colSize
        )
        return (max(rowIndex - 1, 0)..min(rowIndex + 1, rowSize - 1)).any { x ->
            (max(range.first - 1, 0)..min(range.last + 1, colSize - 1)).any { y ->
                rows[x][y].isSymbol()
            }
        }
    }

    fun findAdjacentGear(rowIndex: Int, range: IntRange): Point? {
        require(
            rowIndex in 0..<rowSize &&
                0 <= range.first && range.last < colSize
        )
        for (x in max(rowIndex - 1, 0)..min(rowIndex + 1, rowSize - 1)) {
            for (y in max(range.first - 1, 0)..min(range.last + 1, colSize - 1)) {
                if (rows[x][y] == '*') {
                    return Point(x, y)
                }
            }
        }
        return null
    }
}

private data class Point(val x: Int, val y: Int)

/**
 * To avoid using Iterator<T>.plus(v: Iterator<T>) instead of Iterator<T>.plus(v: T)
 */
private fun List<IntRange>.append(v: IntRange) = this.plus<IntRange>(v)
