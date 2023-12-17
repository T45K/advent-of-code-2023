package day12

import readInput

fun main() {
    val input = readInput("input/Day12.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Long = input.sumOf { SpringMap.fromLine(it).countCandidates() }
private fun part2(input: List<String>): Long = input.sumOf { SpringMap.fromLine(it, 5).countCandidates() }

private data class SpringMap(
    private val row: String,
    private val contiguousSpringSizes: List<Int>,
) {
    companion object {
        fun fromLine(line: String, repeat: Int = 1): SpringMap {
            val (row, contiguousGroupStr) = line.split(" ")
            return SpringMap(
                (1..repeat).joinToString("?") { row },
                (1..repeat).flatMap { contiguousGroupStr.split(",").map { it.toInt() } }
            )
        }
    }

    fun countCandidates(): Long {
        val contiguousSpringCandidates = contiguousSpringSizes.map { size -> enumerateCandidates(size) }
        val combinationCounts = Array(contiguousSpringSizes.size) { Array(contiguousSpringCandidates[it].size) { 0L } }
        for ((index, candidate) in contiguousSpringCandidates.last().withIndex()) {
            if (candidate.isValidAsLast()) {
                combinationCounts.last()[index] = 1
            }
        }

        for (i in (0..<contiguousSpringSizes.size - 1).reversed()) { // fill in counts backward
            val currentCandidates = contiguousSpringCandidates[i]
            val nextCandidates = contiguousSpringCandidates[i + 1]
            val currentCombinationCounts = combinationCounts[i]
            val nextCombinationCounts = combinationCounts[i + 1]
            for ((indexOfCurrentCandidates, currentCandidate) in currentCandidates.withIndex()) {
                for ((indexOfNextCandidates, nextCandidate) in nextCandidates.withIndex()) {
                    if (nextCandidate.isJustNext(currentCandidate)) {
                        currentCombinationCounts[indexOfCurrentCandidates] += nextCombinationCounts[indexOfNextCandidates]
                    }
                }
            }
        }
        return contiguousSpringCandidates[0].mapIndexed { index, candidate ->
            if (candidate.isValidAsFirst()) combinationCounts[0][index]
            else 0
        }.sum()
    }

    private fun enumerateCandidates(size: Int): List<ContiguousSpring> =
        (0..row.length - size).map { it..<it + size }
            .filter { range ->
                row.substring(range).all { it.isSpringCandidate() } &&
                    when {
                        range.first == 0 -> row[range.next()].isSeparatorCandidate()
                        range.next() == row.length -> row[range.previous()].isSeparatorCandidate()
                        else -> row[range.previous()].isSeparatorCandidate() && row[range.next()].isSeparatorCandidate()
                    }
            }.map { ContiguousSpring(it) }


    private inner class ContiguousSpring(private val range: IntRange) {
        fun isValidAsFirst(): Boolean =
            if (range.first == 0) true
            else !row.substring(0, range.first).contains("#")

        fun isValidAsLast(): Boolean =
            if (range.next() + 1 >= row.length) true
            else !row.substring(range.next() + 1).contains("#")

        fun isJustNext(other: ContiguousSpring): Boolean =
            this.range.first() > other.range.next() &&
                !row.substring(other.range.next(), this.range.first()).contains("#")
    }
}

private fun Char.isSpringCandidate(): Boolean = this == '#' || this == '?'
private fun Char.isSeparatorCandidate(): Boolean = this == '.' || this == '?'

private fun IntRange.previous() = this.first - 1
private fun IntRange.next() = this.last + 1
