package day07

import kotlin.math.pow
import readInput

fun main() {
    val input = readInput("input/Day07.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Long {
    val numberMap = mapOf(
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

    data class Player(
        val hand: String,
        val bit: Long,
    ) {
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

    return input.map { it.split(" ").let { (first, second) -> Player(first, second.toLong()) } }
        .sortedWith(
            compareBy(
                { it.asTypeStrength() },
                { it.asNumberStrength() },
            )
        ).mapIndexed { index, player -> player.bit * (index + 1) }
        .sum()
}

private fun part2(input: List<String>): Long {
    val numberMap = mapOf(
        'J' to 0,
        '2' to 1,
        '3' to 2,
        '4' to 3,
        '5' to 4,
        '6' to 5,
        '7' to 6,
        '8' to 7,
        '9' to 8,
        'T' to 9,
        'Q' to 10,
        'K' to 11,
        'A' to 12,
    )

    data class Player(
        val hand: String,
        val bit: Long,
    ) {
        fun asTypeStrength(): Int {
            val cardKinds = hand.toCharArray().groupBy { it }.mapValues { (_, values) -> values.size }
            val cardCounts = if ('J' in cardKinds && cardKinds.size >= 2) {
                val jCounts = cardKinds['J']!!
                cardKinds.entries.filter { (key, _) -> key != 'J' }
                    .sortedByDescending { (_, value) -> value }
                    .mapIndexed { index, (_, value) ->
                        if (index == 0) value + jCounts
                        else value
                    }
            } else {
                cardKinds.values.map { it }
            }

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

    return input.map { it.split(" ").let { (first, second) -> Player(first, second.toLong()) } }
        .sortedWith(
            compareBy(
                { it.asTypeStrength() },
                { it.asNumberStrength() },
            )
        ).mapIndexed { index, player -> player.bit * (index + 1) }
        .sum()
}

private fun Int.pow(x: Int): Long = this.toDouble().pow(x).toLong()
