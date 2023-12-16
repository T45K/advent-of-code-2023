package day13

import readInput
import split
import transpose

fun main() {
    val input = readInput("input/Day13.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Int {
    val maps = input.split { it.isBlank() }.map { line -> line.map { it.toCharArray().toList() } }
    return maps.sumOf { map ->
        val horizontalNum = findMirrorLine(map)

        if (horizontalNum != null) {
            return@sumOf horizontalNum * 100
        }

        val transposedMap = map.transpose()
        findMirrorLine(transposedMap)!!
    }
}

private fun part2(input: List<String>): Int {
    val maps = input.split { it.isBlank() }.map { line -> line.map { it.toCharArray().toList() } }
    return maps.sumOf { map ->
        val horizontalNum = findSmudgedMirrorLine(map)

        if (horizontalNum != null) {
            return@sumOf horizontalNum * 100
        }

        val transposedMap = map.transpose()
        findSmudgedMirrorLine(transposedMap)!!
    }
}

private fun findMirrorLine(map: List<List<Char>>): Int? = (1..<map.size).find { num: Int ->
    val index = num - 1
    generateSequence(0) { it + 1 }
        .takeWhile { index - it >= 0 && index + it + 1 < map.size }
        .all { map[index - it] == map[index + 1 + it] }
}

private fun findSmudgedMirrorLine(map: List<List<Char>>): Int? = (1..<map.size).find { num: Int ->
    val index = num - 1
    val differentLineNumbers = generateSequence(0) { it + 1 }
        .takeWhile { index - it >= 0 && index + it + 1 < map.size }
        .filterNot { map[index - it] == map[index + 1 + it] }
        .toList()
    if (differentLineNumbers.size == 1) {
        val differentLineNumber = differentLineNumbers[0]
        map[index - differentLineNumber].zip(map[index + differentLineNumber + 1]).count { (a, b) -> a != b } == 1
    } else {
        false
    }
}
