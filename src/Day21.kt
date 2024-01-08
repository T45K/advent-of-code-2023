package day21

import readInput

fun main() {
    val input = readInput("input/Day21.txt")
    val map = input.map { it.toCharArray().toList() }.toList()
    val cols = map.size
    val rows = map[0].size
    val startPoint = (0..<cols).flatMap { i ->
        (0..<rows).map { j -> i to j }
    }.first { (i, j) -> map[i][j] == 'S' }
    val steps = Array(cols) { Array(rows) { Array(2) { -1 } } }

    val queue = ArrayDeque<Pair<Int, Pair<Int, Int>>>()
    steps[startPoint.first][startPoint.second][0] = 0
    queue.add(0 to startPoint)
    while (queue.isNotEmpty()) {
        val (step, point) = queue.removeFirst()
        if (step == 64) {
            continue
        }

        val (i, j) = point

        val next = step + 1
        if (i - 1 in 0..<cols && map[i - 1][j] != '#' && steps[i - 1][j][next % 2] == -1) {
            steps[i - 1][j][next % 2] = next
            queue.add(next to (i - 1 to j))
        }
        if (i + 1 in 0..<cols && map[i + 1][j] != '#' && steps[i + 1][j][next % 2] == -1) {
            steps[i + 1][j][next % 2] = next
            queue.add(next to (i + 1 to j))
        }
        if (j - 1 in 0..<rows && map[i][j - 1] != '#' && steps[i][j - 1][next % 2] == -1) {
            steps[i][j - 1][next % 2] = next
            queue.add(next to (i to j - 1))
        }
        if (j + 1 in 0..<rows && map[i][j + 1] != '#' && steps[i][j + 1][next % 2] == -1) {
            steps[i][j + 1][next % 2] = next
            queue.add(next to (i to j + 1))
        }
    }
    val answer = (0..<cols).flatMap { i ->
        (0..<rows).map { j -> i to j }
    }.count { (i, j) -> steps[i][j][0] >= 0 }
    println(answer)
}
