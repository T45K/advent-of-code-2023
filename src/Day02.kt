@file:OptIn(ExperimentalContracts::class)

package day02

import kotlin.contracts.ExperimentalContracts
import readInput

fun main() {
    val input = readInput("input/Day02.txt")
    val answer = input.mapNotNull { Game.parse(it) }
        .filter { game ->
            game.trials.values.all { it.red <= RED }
                && game.trials.values.all { it.green <= GREEN }
                && game.trials.values.all { it.blue <= BLUE }
        }
        .sumOf { it.id }
    println(answer)
}

private const val RED = 12
private const val GREEN = 13
private const val BLUE = 14

private data class Game(val id: Int, val trials: Trials) {
    companion object {
        fun parse(str: String): Game? =
            "Game ([0-9]+): (.*)".toRegex().find(str.trim())
                ?.groups
                ?.let {
                    val id = it[1]!!.value.toInt()
                    val trials = Trials.parse(it[2]!!.value)
                    Game(id, trials)
                }
    }
}

private data class Trials(val values: List<Trial>) {
    init {
        require(values.isNotEmpty())
    }

    companion object {
        fun parse(str: String): Trials =
            str.split(";")
                .map { Trial.parse(it.trim()) }
                .let { Trials(it) }
    }
}

private data class Trial(
    val red: Int,
    val green: Int,
    val blue: Int,
) {
    companion object {
        fun parse(str: String): Trial {
            val red = "([0-9]+) red".toRegex().find(str)?.groups?.get(1)?.value?.toIntOrNull() ?: 0
            val green = "([0-9]+) green".toRegex().find(str)?.groups?.get(1)?.value?.toIntOrNull() ?: 0
            val blue = "([0-9]+) blue".toRegex().find(str)?.groups?.get(1)?.value?.toIntOrNull() ?: 0
            return Trial(red, green, blue)
        }
    }
}
