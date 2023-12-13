package day12

import readInput

fun main() {
    val input = readInput("input/Day12.txt")
    val answer = input.sumOf { Spring.fromLine(it).countCandidates() }
    println(answer)
}

private data class Spring(
    private val row: String,
    private val contiguousGroup: List<Int>,
) {
    companion object {
        fun fromLine(line: String): Spring {
            val (row, contiguousGroupStr) = line.split(" ")
            return Spring(row, contiguousGroupStr.split(",").map { it.toInt() })
        }
    }

    fun countCandidates(): Int = enumerateRows().count { match(it) }

    private fun enumerateRows(): List<String> = mutableListOf<String>().apply {
        val charArray = row.toCharArray()
        val undefinedIndices = charArray.indices.filter { charArray[it] == '?' }
        fun recursive(i: Int) {
            if (i == undefinedIndices.size) {
                add(charArray.joinToString(""))
            } else {
                val index = undefinedIndices[i]
                charArray[index] = '.'
                recursive(i + 1)
                charArray[index] = '#'
                recursive(i + 1)
            }
        }
        recursive(0)
    }.toList()

    private fun match(candidate: String): Boolean =
        candidate.split(".").filter { it.isNotBlank() }.map { it.length } == contiguousGroup
}
