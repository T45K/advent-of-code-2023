package day15

import readInput

fun main() {
    val input = readInput("input/Day15.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Long = input[0].split(',').sumOf { str ->
    str.toCharArray().map { it.code }.fold(0) { acc, v -> (acc + v) * 17 % 256 }.toLong()
}

private fun part2(input: List<String>): Long {
    val map = mutableMapOf<Int, MutableMap<String, Int>>()
    input[0].split(',').forEach { str ->
        val operation = parse(str)
        val hash = operation.hash()
        val focalLengthByLabel = map.computeIfAbsent(hash) { mutableMapOf() }
        when (operation) {
            is Operation.RemovingOperation -> focalLengthByLabel.remove(operation.label)
            is Operation.ReplacingOperation -> focalLengthByLabel[operation.label] = operation.focalLength
        }
    }
    return map.toList().sumOf { (key, focalLengthByLabel) ->
        (key + 1L) * focalLengthByLabel.values.toList()
            .mapIndexed { index, focalLength -> (index + 1) * focalLength }
            .sum()
    }
}

private fun parse(str: String): Operation =
    when {
        str.contains('-') -> Operation.RemovingOperation(str.substringBefore('-'))
        str.contains('=') -> str.split('=')
            .let { (label, focalLength) -> Operation.ReplacingOperation(label, focalLength.toInt()) }

        else -> throw IllegalArgumentException()
    }

private sealed interface Operation {
    val label: String
    fun hash(): Int = label.toCharArray().map { it.code }.fold(0) { acc, v -> (acc + v) * 17 % 256 }

    data class RemovingOperation(override val label: String) : Operation
    data class ReplacingOperation(override val label: String, val focalLength: Int) : Operation
}
