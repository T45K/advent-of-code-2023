package day22

import readInput

fun main() {
    val input = readInput("input/Day22.txt")
    val bricksInAir = input.mapIndexed { index, s ->
        val (left, right) = s.split("~").map { it.split(",").map(String::toInt) }
        Brick(index, left[0]..right[0], left[1]..right[1], left[2]..right[2])
    }
    val maxX = bricksInAir.maxOf { it.xRange.last }
    val maxY = bricksInAir.maxOf { it.yRange.last }
    val maxZ = bricksInAir.maxOf { it.zRange.last }
    val stackedBrickArrangement: Array<Array<Array<ID?>>> =
        Array(maxZ + 1) { Array(maxX + 1) { Array(maxY + 1) { null } } }

    val stackedBricks = bricksInAir.sortedBy { it.zRange.first }.map { brickInAir ->
        val zIndex = findZIndexToPut(brickInAir.xRange, brickInAir.yRange, stackedBrickArrangement)
        fillIDs(
            brickInAir.id,
            brickInAir.xRange,
            brickInAir.yRange,
            zIndex..(zIndex + brickInAir.zRange.last - brickInAir.zRange.first),
            stackedBrickArrangement,
        )
        Brick(
            brickInAir.id,
            brickInAir.xRange,
            brickInAir.yRange,
            zIndex..(zIndex + brickInAir.zRange.last - brickInAir.zRange.first),
        )
    }

    val necessaryBricks = stackedBricks.fold(emptySet<ID>()) { necessaryBricks, stackedBrick ->
        val belowIDs = enumerateIDs(
            stackedBrick.xRange,
            stackedBrick.yRange,
            stackedBrickArrangement[stackedBrick.zRange.first - 1]
        ).toSet()

        necessaryBricks + (if (belowIDs.size == 1) belowIDs else emptySet())
    }
    val answer = bricksInAir.size - necessaryBricks.size
    println(answer)
}

private fun findZIndexToPut(xRange: IntRange, yRange: IntRange, stackedBricks: Array<Array<Array<ID?>>>): Int {
    val highestBrickExistZIndex = (1..<stackedBricks.size).reversed().firstOrNull {
        val plain: Array<Array<ID?>> = stackedBricks[it]
        val brickExists = enumerateIDs(xRange, yRange, plain).any()
        brickExists
    }
    return if (highestBrickExistZIndex != null) highestBrickExistZIndex + 1 else 1
}

private fun enumerateIDs(xRange: IntRange, yRange: IntRange, plain: Array<Array<ID?>>): Sequence<ID> =
    xRange.asSequence().flatMap { x -> yRange.asSequence().map { y -> x to y } }.mapNotNull { (x, y) -> plain[x][y] }

private fun fillIDs(
    id: ID,
    xRange: IntRange,
    yRange: IntRange,
    zRange: IntRange,
    stackedBricks: Array<Array<Array<ID?>>>
) {
    for (z in zRange) {
        for (x in xRange) {
            for (y in yRange) {
                stackedBricks[z][x][y] = id
            }
        }
    }
}

private data class Brick(val id: ID, val xRange: IntRange, val yRange: IntRange, val zRange: IntRange)

typealias ID = Int
