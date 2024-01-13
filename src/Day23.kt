package day23

import readInput

val moving = listOf(
    Triple(-1, 0, '^'),
    Triple(1, 0, 'v'),
    Triple(0, -1, '<'),
    Triple(0, 1, '>'),
)

fun main() {
    val input = readInput("input/Day23.txt")
    val map = input.map { it.toCharArray().toList() }
    val begin = Point(0, 1)
    val answer = dfs(begin, Point(-1, -1), 0, map)
    println(answer)
}

fun dfs(current: Point, prev: Point, steps: Int, map: List<List<Char>>): Int {
    if (current == Point(map.size - 1, map[0].size - 2)) {
        return steps
    }

    return moving.mapNotNull { (xMove, yMove, downhill) ->
        val next = Point(current.x + xMove, current.y + yMove)
        when {
            next == prev -> null
            !(next.x in map.indices && next.y in map[0].indices) -> null
            !(map[next.x][next.y] == '.' || map[next.x][next.y] == downhill) -> null
            else -> dfs(next, current, steps + 1, map)
        }
    }.max()
}


data class Point(val x: Int, val y: Int)
