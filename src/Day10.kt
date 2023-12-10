package day10

import readInput

fun main() {
    val input = readInput("input/Day10.txt")
    val board = Board(input)

    val counts = Array(board.col) { Array(board.row) { -1 } }
    val startPoint = board.startPoint
    val queue = ArrayDeque<Pair<Point, Int>>()
    queue.addLast(startPoint to 0)
    while (queue.isNotEmpty()) {
        val (point, count) = queue.removeFirst()
        if (counts[point.x][point.y] != -1) {
            continue
        }
        counts[point.x][point.y] = count
        queue.addAll(
            board.next(point)
                .filter { counts[it.x][it.y] == -1 }
                .map { it to count + 1 }
        )
    }

    val answer = counts.flatMap { it.toList() }.max()
    println(answer)
}

private class Board(input: List<String>) {
    private val data: List<List<Char>> = input.map { it.toCharArray().toList() }
    val col = data.size
    val row = data[0].size

    val startPoint: Point by lazy {
        (0..<col).flatMap { i -> (0..<row).map { j -> Point(i, j) } }
            .first { it.isStartPoint() }
    }

    private fun createPointIfMovable(x: Int, y: Int): Point? =
        if (x in 0..<col && y in 0..<row) Point(x, y)
        else null

    fun next(point: Point): List<Point> {
        val (x, y) = point
        val toRight = setOf('-', '7', 'J')
        val toLeft = setOf('-', 'F', 'L')
        val toUpper = setOf('|', 'F', '7')
        val toDown = setOf('|', 'L', 'J')
        return when (point.value()) {
            '|' -> listOfNotNull(
                createPointIfMovable(x - 1, y)?.takeIf { it.value() in toUpper },
                createPointIfMovable(x + 1, y)?.takeIf { it.value() in toDown }
            )

            '-' -> listOfNotNull(
                createPointIfMovable(x, y - 1)?.takeIf { it.value() in toLeft },
                createPointIfMovable(x, y + 1)?.takeIf { it.value() in toRight }
            )

            'L' -> listOfNotNull(
                createPointIfMovable(x - 1, y)?.takeIf { it.value() in toUpper },
                createPointIfMovable(x, y + 1)?.takeIf { it.value() in toRight },
            )

            'J' -> listOfNotNull(
                createPointIfMovable(x - 1, y)?.takeIf { it.value() in toUpper },
                createPointIfMovable(x, y - 1)?.takeIf { it.value() in toLeft },
            )

            '7' -> listOfNotNull(
                createPointIfMovable(x + 1, y)?.takeIf { it.value() in toDown },
                createPointIfMovable(x, y - 1)?.takeIf { it.value() in toLeft },
            )

            'F' -> listOfNotNull(
                createPointIfMovable(x + 1, y)?.takeIf { it.value() in toDown },
                createPointIfMovable(x, y + 1)?.takeIf { it.value() in toRight },
            )

            'S' -> listOfNotNull(
                createPointIfMovable(x - 1, y)?.takeIf { it.value() in toUpper },
                createPointIfMovable(x + 1, y)?.takeIf { it.value() in toDown },
                createPointIfMovable(x, y - 1)?.takeIf { it.value() in toLeft },
                createPointIfMovable(x, y + 1)?.takeIf { it.value() in toRight },
            )

            else -> emptyList()
        }
    }

    private fun Point.value() = data[x][y]
    private fun Point.isStartPoint() = data[x][y] == 'S'
}

private data class Point(val x: Int, val y: Int)
