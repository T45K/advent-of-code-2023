package day07

import kotlin.math.pow
import readInput

fun main() {
    val input = readInput("input/Day07.txt")
    val answer = input.map { it.split(" ").let { (first, second) -> Player(first, second.toLong()) } }
        .sortedWith(
            compareBy(
                { it.asTypeStrength() },
                { it.asNumberStrength() },
            )
        ).mapIndexed { index, player -> player.bit * (index + 1) }
        .sum()
    println(answer)
}

private data class Player(
    val hand: String,
    val bit: Long,
) {
    companion object {
        private val numberMap = mapOf(
            '2' to 0,
            '3' to 1,
            '4' to 2,
            '5' to 3,
            '6' to 4,
            '7' to 5,
            '8' to 6,
            '9' to 7,
            'T' to 8,
            'J' to 9,
            'Q' to 10,
            'K' to 11,
            'A' to 12,
        )
    }

    fun asTypeStrength(): Int {
        val cardCounts = hand.toCharArray().groupBy { it }.values.map { it.size }
        return when {
            cardCounts.size == 1 -> 6 // Five of a kind
            cardCounts.size == 2 && (cardCounts.max() == 4) -> 5 // Four of a kind
            cardCounts.size == 2 -> 4 // Full house
            cardCounts.size == 3 && (cardCounts.max() == 3) -> 3 // Three of a kind
            cardCounts.size == 3 -> 2 // Two pair
            cardCounts.size == 4 -> 1 // One pair
            else -> 0 // High card
        }
    }

    fun asNumberStrength(): Long =
        numberMap[hand[0]]!! * 13.pow(4) +
            numberMap[hand[1]]!! * 13.pow(3) +
            numberMap[hand[2]]!! * 13.pow(2) +
            numberMap[hand[3]]!! * 13.pow(1) +
            numberMap[hand[4]]!! * 13.pow(0)
}

private fun Int.pow(x: Int): Long = this.toDouble().pow(x).toLong()
