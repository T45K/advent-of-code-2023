package day10

import readInput

fun main() {
    val input = readInput("input/Day10.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Int {
    val board = Board(input)

    val counts = Array(board.col) { Array(board.row) { -1 } }
    val startPoint = board.startPoint
    val queue = ArrayDeque<Pair<Point, Int>>()
    queue.add(startPoint to 0)
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

    return counts.flatMap { it.toList() }.max()
}

private fun part2(input: List<String>): Int {
    val board = Board(input)

    val isIncludedInLoop = Array(board.col) { Array(board.row) { false } }
    val startPoint = board.startPoint
    val queue = ArrayDeque<Point>()
    queue.add(startPoint)
    while (queue.isNotEmpty()) {
        val point = queue.removeFirst()
        isIncludedInLoop[point.x][point.y] = true
        queue.addAll(
            board.next(point)
                .filter { !isIncludedInLoop[it.x][it.y] }
                .map { it }
        )
    }

    val (cornerX, connerY) = (0..<board.col).flatMap { x ->
        (0..<board.row).map { y -> Point(x, y) }
    }.first { (x, y) -> isIncludedInLoop[x][y] }

    val insideDirections: Array<Array<InsideDirection?>> = Array(board.col) { Array(board.row) { null } }
    val isVisited = Array(board.col) { Array(board.row) { false } }
    isVisited[cornerX][connerY] = true

    dfs(board, Point(cornerX + 1, connerY), InsideDirection.RIGHT, insideDirections, isVisited)

    var count = 0
    var isInside: Boolean
    for (col in insideDirections) {
        isInside = false
        for (a in col) {
            if (a == InsideDirection.RIGHT) isInside = true
            else if (a == InsideDirection.LEFT) isInside = false
            else if (isInside) count++
        }
    }
    return count
}

enum class InsideDirection {
    RIGHT, LEFT, UP, DOWN;
}

private tailrec fun dfs(
    board: Board,
    point: Point,
    previousInsideDirection: InsideDirection,
    directionBoard: Array<Array<InsideDirection?>>,
    isVisited: Array<Array<Boolean>>,
) {
    isVisited[point.x][point.y] = true
    val (passToNext, writeBoard) = when (board.value(point)) {
        '|', '-', 'S' -> previousInsideDirection to previousInsideDirection
        'L' -> when (previousInsideDirection) {
            InsideDirection.RIGHT -> InsideDirection.UP to null
            InsideDirection.LEFT -> InsideDirection.DOWN to InsideDirection.LEFT
            InsideDirection.UP -> InsideDirection.RIGHT to null
            InsideDirection.DOWN -> InsideDirection.LEFT to InsideDirection.LEFT
        }

        'J' -> when (previousInsideDirection) {
            InsideDirection.RIGHT -> InsideDirection.DOWN to InsideDirection.RIGHT
            InsideDirection.LEFT -> InsideDirection.UP to null
            InsideDirection.UP -> InsideDirection.LEFT to null
            InsideDirection.DOWN -> InsideDirection.RIGHT to InsideDirection.RIGHT
        }

        '7' -> when (previousInsideDirection) {
            InsideDirection.RIGHT -> InsideDirection.UP to InsideDirection.RIGHT
            InsideDirection.LEFT -> InsideDirection.DOWN to null
            InsideDirection.UP -> InsideDirection.RIGHT to InsideDirection.RIGHT
            InsideDirection.DOWN -> InsideDirection.LEFT to null
        }

        'F' -> when (previousInsideDirection) {
            InsideDirection.RIGHT -> InsideDirection.DOWN to null
            InsideDirection.LEFT -> InsideDirection.UP to InsideDirection.LEFT
            InsideDirection.UP -> InsideDirection.LEFT to InsideDirection.LEFT
            InsideDirection.DOWN -> InsideDirection.RIGHT to null
        }

        else -> throw IllegalArgumentException()
    }

    directionBoard[point.x][point.y] = writeBoard
    val nexts = board.next(point).filter { !isVisited[it.x][it.y] }
    require(nexts.size <= 1)
    if (nexts.isEmpty()) {
        return
    }
    dfs(board, nexts[0], passToNext, directionBoard, isVisited)
}

private class Board(input: List<String>) {
    private val data: List<List<Char>> = input.map { it.toCharArray().toList() }
    val col = data.size
    val row = data[0].size

    val startPoint: Point by lazy {
        (0..<col).flatMap { i -> (0..<row).map { j -> Point(i, j) } }
            .first { (x, y) -> data[x][y] == 'S' }
    }

    private fun createPointIfMovable(x: Int, y: Int): Point? =
        if (x in 0..<col && y in 0..<row) Point(x, y)
        else null

    fun next(point: Point): List<Point> {
        val (x, y) = point
        val toRight = setOf('-', '7', 'J', 'S')
        val toLeft = setOf('-', 'F', 'L', 'S')
        val toUpper = setOf('|', 'F', '7', 'S')
        val toDown = setOf('|', 'L', 'J', 'S')
        return when (value(point)) {
            '|' -> listOfNotNull(
                createPointIfMovable(x - 1, y)?.takeIf { value(it) in toUpper },
                createPointIfMovable(x + 1, y)?.takeIf { value(it) in toDown }
            )

            '-' -> listOfNotNull(
                createPointIfMovable(x, y - 1)?.takeIf { value(it) in toLeft },
                createPointIfMovable(x, y + 1)?.takeIf { value(it) in toRight }
            )

            'L' -> listOfNotNull(
                createPointIfMovable(x - 1, y)?.takeIf { value(it) in toUpper },
                createPointIfMovable(x, y + 1)?.takeIf { value(it) in toRight },
            )

            'J' -> listOfNotNull(
                createPointIfMovable(x - 1, y)?.takeIf { value(it) in toUpper },
                createPointIfMovable(x, y - 1)?.takeIf { value(it) in toLeft },
            )

            '7' -> listOfNotNull(
                createPointIfMovable(x + 1, y)?.takeIf { value(it) in toDown },
                createPointIfMovable(x, y - 1)?.takeIf { value(it) in toLeft },
            )

            'F' -> listOfNotNull(
                createPointIfMovable(x + 1, y)?.takeIf { value(it) in toDown },
                createPointIfMovable(x, y + 1)?.takeIf { value(it) in toRight },
            )

            'S' -> listOfNotNull(
                createPointIfMovable(x - 1, y)?.takeIf { value(it) in toUpper },
                createPointIfMovable(x + 1, y)?.takeIf { value(it) in toDown },
                createPointIfMovable(x, y - 1)?.takeIf { value(it) in toLeft },
                createPointIfMovable(x, y + 1)?.takeIf { value(it) in toRight },
            )

            else -> emptyList()
        }
    }

    fun value(point: Point): Char = data[point.x][point.y]
}

private data class Point(val x: Int, val y: Int)
