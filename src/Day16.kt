package day16

import readInput

fun main() {
    val input = readInput("input/Day16.txt")
    val board = input.map { it.toCharArray().toList() }
    val routes = Array(board.size) { Array(board[0].size) { 0 } }
    dfs(routes, Point(0, 0), RIGHT, board)
    val answer = routes.sumOf { array -> array.count { it > 0 } }
    println(answer)
}

private fun dfs(routes: Array<Array<Int>>, point: Point, direction: Int, board: List<List<Char>>) {
    val (x, y) = point
    val pointIsInBoard = x in routes.indices && y in routes[0].indices
    if (!pointIsInBoard) {
        return
    }
    val isAlreadyVisited = routes[x][y] and direction != 0
    if (isAlreadyVisited) {
        return
    }

    routes[x][y] = routes[x][y] or direction
    val goUp = { dfs(routes, Point(x - 1, y), UP, board) }
    val goRight = { dfs(routes, Point(x, y + 1), RIGHT, board) }
    val goDown = { dfs(routes, Point(x + 1, y), DOWN, board) }
    val goLeft = { dfs(routes, Point(x, y - 1), LEFT, board) }

    when {
        direction == UP && board[x][y] in listOf('.', '|') -> goUp()
        direction == UP && board[x][y] == '/' -> goRight()
        direction == UP && board[x][y] == '\\' -> goLeft()
        direction == UP && board[x][y] == '-' -> {
            goRight()
            goLeft()
        }

        direction == RIGHT && board[x][y] in listOf('.', '-') -> goRight()
        direction == RIGHT && board[x][y] == '/' -> goUp()
        direction == RIGHT && board[x][y] == '\\' -> goDown()
        direction == RIGHT && board[x][y] == '|' -> {
            goUp()
            goDown()
        }

        direction == DOWN && board[x][y] in listOf('.', '|') -> goDown()
        direction == DOWN && board[x][y] == '/' -> goLeft()
        direction == DOWN && board[x][y] == '\\' -> goRight()
        direction == DOWN && board[x][y] == '-' -> {
            goRight()
            goLeft()
        }

        direction == LEFT && board[x][y] in listOf('.', '-') -> goLeft()
        direction == LEFT && board[x][y] == '/' -> goDown()
        direction == LEFT && board[x][y] == '\\' -> goUp()
        direction == LEFT && board[x][y] == '|' -> {
            goUp()
            goDown()
        }
    }
}

private data class Point(val x: Int, val y: Int)

private const val UP = 1
private const val RIGHT = 2
private const val DOWN = 4
private const val LEFT = 8
