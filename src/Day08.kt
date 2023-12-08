package day08

import readInput

fun main() {
    val input = readInput("input/Day08.txt")
    val route = input[0]
    val map = input.subList(2, input.size)
        .associate { line ->
            "([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)".toRegex()
                .find(line)!!
                .groupValues
                .let {
                    it[1] to (it[2] to it[3])
                }
        }
    val answer = generateSequence(0 to "AAA") { (count, current) ->
        val next = if (route[count % route.length] == 'L') {
            map[current]!!.first
        } else {
            map[current]!!.second
        }
        count + 1 to next
    }.first { it.second == "ZZZ" }
        .first
    println(answer)
}
