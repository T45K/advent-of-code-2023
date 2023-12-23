package day18

import readInput

fun main() {
    val input = readInput("input/Day18.txt")
    val board = Array(1_000) { Array(1_000) { false } }
    board[500][500] = true
    var point = Point(500, 500)
    input.map { DigPlan.fromLine(it) }.forEach { digPlan ->
        when (digPlan.direction) {
            "R" -> {
                for (i in 1..digPlan.meter) {
                    board[point.x][point.y + i] = true
                }
                point = Point(point.x, point.y + digPlan.meter)
            }

            "L" -> {
                for (i in 1..digPlan.meter) {
                    board[point.x][point.y - i] = true
                }
                point = Point(point.x, point.y - digPlan.meter)
            }

            "U" -> {
                for (i in 1..digPlan.meter) {
                    board[point.x - i][point.y] = true
                }
                point = Point(point.x - digPlan.meter, point.y)
            }

            "D" -> {
                for (i in 1..digPlan.meter) {
                    board[point.x + i][point.y] = true
                }
                point = Point(point.x + digPlan.meter, point.y)
            }
        }
    }
    fillInterior(Point(500, 501), board)
    val answer = board.sumOf { line -> line.count { it } }
    println(answer)
}

private fun fillInterior(point: Point, board: Array<Array<Boolean>>) {
    val queue = ArrayDeque<Point>()
    queue.add(point)
    while (queue.isNotEmpty()) {
        val (x, y) = queue.removeFirst()
        if (board[x][y]) {
            continue
        }
        board[x][y] = true
        listOf(
            1 to 0,
            -1 to 0,
            0 to 1,
            0 to -1,
        ).map { Point(x + it.first, y + it.second) }
            .filter { !board[it.x][it.y] }
            .forEach { queue.add(it) }
    }
}

private data class Point(val x: Int, val y: Int)

private data class DigPlan(val direction: String, val meter: Int, val colorCode: String) {
    companion object {
        fun fromLine(line: String): DigPlan {
            val split = line.split(" ")
            return DigPlan(split[0], split[1].toInt(), split[2].substring(1..<split[2].length))
        }
    }
}
