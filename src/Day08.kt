package day08

import readInput

fun main() {
    val input = readInput("input/Day08.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Long {
    val route = input[0]
    val map = input.subList(2, input.size)
        .map { Navigation.fromLine(it) }
        .associateBy { it.current }
    return findShortestStepCount(route, map, Node("AAA")) { it == Node("ZZZ") }
}

private fun part2(input: List<String>): Long {
    val route = input[0]
    val map = input.subList(2, input.size)
        .map { Navigation.fromLine(it) }
        .associateBy { it.current }
    val shortestPaths = map.keys.filter { it.value.last() == 'A' }
        .map { initialNode -> findShortestStepCount(route, map, initialNode) { it.value.last() == 'Z' } }
    return shortestPaths.reduce(::calcLcm)
}

private fun calcLcm(a: Long, b: Long): Long {
    val gcd = euclideanAlgorithm(a, b)
    return a * b / gcd
}

private fun euclideanAlgorithm(a: Long, b: Long): Long =
    when {
        b > a -> euclideanAlgorithm(b, a)
        b == 0L -> a
        else -> euclideanAlgorithm(b, a % b)
    }

private fun findShortestStepCount(
    route: String,
    map: Map<Node, Navigation>,
    initialNode: Node,
    reachPredicate: (Node) -> Boolean
): Long {
    return generateSequence(0L to initialNode) { (count, current) ->
        val next = if (route[(count % route.length).toInt()] == 'L') {
            map[current]!!.left
        } else {
            map[current]!!.right
        }
        count + 1 to next
    }.first { (_, reachedNode) -> reachPredicate(reachedNode) }
        .let { (count, _) -> count }
}

private data class Navigation private constructor(
    val current: Node,
    val left: Node,
    val right: Node,
) {
    companion object {
        fun fromLine(line: String): Navigation =
            "([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)".toRegex()
                .find(line)!!
                .groupValues
                .let {
                    Navigation(Node(it[1]), Node(it[2]), Node(it[3]))
                }
    }
}

@JvmInline
value class Node(val value: String) {
    init {
        require(value.length == 3)
    }
}
