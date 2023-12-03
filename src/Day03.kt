package day03

import kotlin.math.max
import kotlin.math.min
import readInput

fun main() {
    val input = readInput("input/Day03.txt")
    val schematic = Schematic(input)
    val answer = (0..<schematic.rowSize).map { rowIndex ->
        schematic.enumerateNumberRanges(rowIndex)
            .filter { schematic.isAdjacentToSymbols(rowIndex, it) }
            .mapNotNull { schematic.extractNumber(rowIndex, it) }
            .sum()
    }.sum()
    println(answer)
}

private class Schematic(private val rows: List<String>) {
    val rowSize = rows.size
    private val colSize = rows[0].length

    private val symbolMap: List<List<Boolean>> = rows.map { str ->
        str.toCharArray().map { !(it.isDigit() || it == '.') }
    }

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
                symbolMap[x][y]
            }
        }
    }
}

/**
 * To avoid using Iterator<T>.plus(v: Iterator<T>) instead of Iterator<T>.plus(v: T)
 */
private fun List<IntRange>.append(v: IntRange) = this.plus<IntRange>(v)
