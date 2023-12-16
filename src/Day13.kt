package day13

import readInput
import split
import transpose

fun main() {
    val input = readInput("input/Day13.txt")
    val maps = input.split { it.isBlank() }.map { line -> line.map { it.toCharArray().toList() } }
    val answer = maps.sumOf { map ->
        val horizontalNum = findMirrorLine(map)

        if (horizontalNum != null) {
            return@sumOf horizontalNum * 100
        }

        val transposedMap = map.transpose()
        findMirrorLine(transposedMap)!!
    }
    println(answer)
}

private fun findMirrorLine(map: List<List<Char>>): Int? = (1..<map.size).find { num: Int ->
    val index = num - 1
    generateSequence(0) { it + 1 }
        .takeWhile { index - it >= 0 && index + it + 1 < map.size }
        .all { map[index - it] == map[index + 1 + it] }
}
