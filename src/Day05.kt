package day05

import readInput

fun main() {
    val input = readInput("input/Day05.txt")
    val seeds = parseSeeds(input)
    val mappingRulesList = listOf(
        parseMappingRules(input, "seed-to-soil map:"),
        parseMappingRules(input, "soil-to-fertilizer map:"),
        parseMappingRules(input, "fertilizer-to-water map:"),
        parseMappingRules(input, "water-to-light map:"),
        parseMappingRules(input, "light-to-temperature map:"),
        parseMappingRules(input, "temperature-to-humidity map:"),
        parseMappingRules(input, "humidity-to-location map:"),
    )
    val answer = seeds.minOf { seed ->
        mappingRulesList.fold(seed) { acc, rules -> rules.convert(acc) }
    }
    println(answer)
}

private fun parseSeeds(input: List<String>): List<Long> = input[0]
    .substringAfter("seeds: ")
    .split(" ")
    .map { it.toLong() }

private fun parseMappingRules(input: List<String>, ruleName: String): List<MappingRule> {
    val startIndex = input.indexOf(ruleName)
    return input.drop(startIndex + 1)
        .takeWhile { it.isNotBlank() }
        .map {
            val split = it.split(" ")
            MappingRule(split[0].toLong(), split[1].toLong(), split[2].toLong())
        }
}

private fun List<MappingRule>.convert(value: Long): Long =
    this.firstOrNull { value in it }?.convert(value) ?: value

private data class MappingRule(
    val dest: Long,
    val source: Long,
    val range: Long,
) {
    operator fun contains(value: Long): Boolean =
        value in source..<(source + range)

    fun convert(value: Long): Long {
        require(value in this)
        val diff = value - source
        return dest + diff
    }
}
