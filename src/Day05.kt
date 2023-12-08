package day05

import readInput

fun main() {
    val input = readInput("input/Day05.txt")
    val answer = part2(input)
    println(answer)
}

private fun part1(input: List<String>): Long {
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
    return seeds.minOf { seed ->
        mappingRulesList.fold(seed) { acc, rules -> rules.convert(acc) }
    }
}

private fun part2(input: List<String>): Long {
    val seeds = parseSeeds(input)
    val mappingRulesList = listOf(
        parseMappingRules(input, "seed-to-soil map:"),
        parseMappingRules(input, "soil-to-fertilizer map:"),
        parseMappingRules(input, "fertilizer-to-water map:"),
        parseMappingRules(input, "water-to-light map:"),
        parseMappingRules(input, "light-to-temperature map:"),
        parseMappingRules(input, "temperature-to-humidity map:"),
        parseMappingRules(input, "humidity-to-location map:"),
    ).reversed()
    val seedRanges = (0..<seeds.size / 2).map { seeds[2 * it]..<(seeds[2 * it] + seeds[2 * it + 1]) }
    return generateSequence(0L) { it + 1 }.first { destNumber ->
        val candidate = mappingRulesList.fold(destNumber) { acc, rules -> rules.reverseConvert(acc) }
        seedRanges.any { candidate in it }
    }
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

private fun List<MappingRule>.reverseConvert(value: Long): Long =
    this.firstOrNull { value in it.dest..<(it.dest + it.range) }
        ?.let { it.source + value - it.dest }
        ?: value

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
