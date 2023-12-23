package day17

import readInput
import java.util.PriorityQueue

fun main() {
    val input = readInput("input/Day17.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Int {
    val map = input.map { it.toCharArray().map(Char::digitToInt) }
    val distances = Array(map.size) { Array(map[0].size) { Distance() } }
    val queue = PriorityQueue<Triple<Point, DirectionAndCount, Int>>(compareBy { it.third })
    queue.add(Triple(Point(0, 1), DirectionAndCount(Direction.RIGHT, 1), map[0][1]))
    queue.add(Triple(Point(1, 0), DirectionAndCount(Direction.DOWN, 1), map[1][0]))
    while (queue.isNotEmpty()) {
        val (currentPoint, directionAndCount, currentDistance) = queue.poll()
        val (x, y) = currentPoint
        if (x == map.size - 1 && y == map[0].size - 1) {
            return currentDistance
        }
        if (distances[x][y][directionAndCount] <= currentDistance) {
            continue
        }
        distances[x][y][directionAndCount] = currentDistance
        val (fromDirection, count) = directionAndCount
        for (toDirection in Direction.entries) {
            if (toDirection.opposite() == fromDirection) {
                continue
            }
            val nextPoint = nextPoint(currentPoint, toDirection)
            if (nextPoint !in map) {
                continue
            }
            if (toDirection != fromDirection) {
                val nextDistance = distances[x][y].min(fromDirection) + map[nextPoint.x][nextPoint.y]
                val nextDirectionAndCount = DirectionAndCount(toDirection, 1)
                if (distances[nextPoint.x][nextPoint.y][nextDirectionAndCount] > nextDistance) {
                    queue.add(Triple(nextPoint, nextDirectionAndCount, nextDistance))
                }
            }
            if (toDirection == fromDirection && count < 3) {
                val nextDistance = currentDistance + map[nextPoint.x][nextPoint.y]
                val nextDirectionAndCount = DirectionAndCount(toDirection, count + 1)
                if (distances[nextPoint.x][nextPoint.y][nextDirectionAndCount] > nextDistance) {
                    queue.add(Triple(nextPoint, nextDirectionAndCount, nextDistance))
                }
            }
        }
    }
    throw IllegalStateException()
}

private fun part2(input: List<String>): Int {
    val map = input.map { it.toCharArray().map(Char::digitToInt) }
    val distances = Array(map.size) { Array(map[0].size) { Distance() } }
    val queue = PriorityQueue<Triple<Point, DirectionAndCount, Int>>(compareBy { it.third })
    queue.add(
        Triple(
            Point(0, 4),
            DirectionAndCount(Direction.RIGHT, 4),
            (1..4).map { nextPoint(Point(0, 0), Direction.RIGHT, it) }.sumOf { map[it.x][it.y] },
        )
    )
    queue.add(
        Triple(
            Point(4, 0),
            DirectionAndCount(Direction.DOWN, 4),
            (1..4).map { nextPoint(Point(0, 0), Direction.DOWN, it) }.sumOf { map[it.x][it.y] }
        )
    )
    while (queue.isNotEmpty()) {
        val (currentPoint, directionAndCount, currentDistance) = queue.poll()
        val (x, y) = currentPoint
        if (x == map.size - 1 && y == map[0].size - 1) {
            return currentDistance
        }
        if (distances[x][y][directionAndCount] <= currentDistance) {
            continue
        }
        distances[x][y][directionAndCount] = currentDistance
        val (fromDirection, count) = directionAndCount
        for (toDirection in Direction.entries) {
            if (toDirection.opposite() == fromDirection) {
                continue
            }
            if (toDirection != fromDirection) {
                val nextPoint = nextPoint(currentPoint, toDirection, 4)
                if (nextPoint !in map) {
                    continue
                }
                val nextDistance = distances[x][y].min(fromDirection) +
                    (1..4).map { nextPoint(currentPoint, toDirection, it) }.sumOf { map[it.x][it.y] }
                val nextDirectionAndCount = DirectionAndCount(toDirection, 4)
                if (distances[nextPoint.x][nextPoint.y][nextDirectionAndCount] > nextDistance) {
                    queue.add(Triple(nextPoint, nextDirectionAndCount, nextDistance))
                }
            }
            if (toDirection == fromDirection && count < 10) {
                val nextPoint = nextPoint(currentPoint, toDirection)
                if (nextPoint !in map) {
                    continue
                }
                val nextDistance = currentDistance + map[nextPoint.x][nextPoint.y]
                val nextDirectionAndCount = DirectionAndCount(toDirection, count + 1)
                if (distances[nextPoint.x][nextPoint.y][nextDirectionAndCount] > nextDistance) {
                    queue.add(Triple(nextPoint, nextDirectionAndCount, nextDistance))
                }
            }
        }
    }
    throw IllegalStateException()
}

private operator fun List<List<Int>>.contains(point: Point): Boolean =
    point.x in this.indices && point.y in this[0].indices

private enum class Direction {
    UP, RIGHT, DOWN, LEFT;

    fun opposite(): Direction = when (this) {
        UP -> DOWN
        RIGHT -> LEFT
        DOWN -> UP
        LEFT -> RIGHT
    }
}

private data class Point(val x: Int, val y: Int)

private fun nextPoint(point: Point, direction: Direction, step: Int = 1): Point = when (direction) {
    Direction.UP -> Point(point.x - step, point.y)
    Direction.RIGHT -> Point(point.x, point.y + step)
    Direction.DOWN -> Point(point.x + step, point.y)
    Direction.LEFT -> Point(point.x, point.y - step)
}

private class Distance {
    private val values = mutableMapOf<Direction, MutableMap<Int, Int>>()

    operator fun get(directionAndCount: DirectionAndCount): Int {
        val (direction, count) = directionAndCount
        return values[direction]?.get(count) ?: Int.MAX_VALUE
    }

    operator fun set(directionAndCount: DirectionAndCount, distance: Int) {
        val (direction, count) = directionAndCount
        values.computeIfAbsent(direction) { mutableMapOf() }[count] = distance
    }

    fun min(direction: Direction): Int {
        return (1..10).minOf { get(DirectionAndCount(direction, it)) }
    }
}

private data class DirectionAndCount(val direction: Direction, val count: Int)
