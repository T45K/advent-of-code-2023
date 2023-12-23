package day19

import readInput
import split
import substringBetween

fun main() {
    val input = readInput("input/Day19.txt")
    val (workflowsInput, partsRatingsInput) = input.split { it.isBlank() }
    val workflowByName = workflowsInput.map { Workflow.fromString(it) }.associateBy { it.name }
    val partsRatings = partsRatingsInput.map { PartsRating.fromString(it) }
    fun Workflow.apply(partsRating: PartsRating): Result =
        when (val result = this.rules.first { it.isTarget(partsRating) }.result) {
            Accepted, Rejected -> result
            is NextWorkflow -> workflowByName[result.name]!!.apply(partsRating)
        }

    val answer = partsRatings.filter { workflowByName["in"]!!.apply(it) == Accepted }
        .sumOf { it.calcSum().toLong() }
    println(answer)
}

private data class Workflow(val name: String, val rules: List<Rule>) {
    companion object {
        fun fromString(str: String): Workflow {
            val name = str.substringBefore('{')
            val rules = str
                .substringBetween('{', '}')
                .split(',').map { Rule.fromString(it) }
            return Workflow(name, rules)
        }
    }
}

private data class Rule(val isTarget: (PartsRating) -> Boolean, val result: Result) {
    companion object {
        // like x<100:A or A
        fun fromString(str: String): Rule = when {
            str == "A" -> Rule({ true }, Accepted)
            str == "R" -> Rule({ true }, Rejected)
            !str.contains(":") -> Rule({ true }, NextWorkflow(str))
            else -> {
                val (isTarget, result) = str.split(":")
                if (str.contains('<')) {
                    val (ratingName, boundary) = isTarget.split('<')
                    Rule({ it.getRatingByName(ratingName) < boundary.toInt() }, result.toResult())
                } else {
                    val (ratingName, boundary) = isTarget.split('>')
                    Rule({ it.getRatingByName(ratingName) > boundary.toInt() }, result.toResult())
                }
            }
        }
    }
}

private data class PartsRating(val x: Int, val m: Int, val a: Int, val s: Int) {
    companion object {
        fun fromString(str: String): PartsRating {
            val (x, m, a, s) = str.substring(1, str.length - 1).split(',')
                .map { it.substringAfter('=') }
                .map { it.toInt() }
            return PartsRating(x, m, a, s)
        }
    }

    fun getRatingByName(name: String): Int = when (name) {
        "x" -> this.x
        "m" -> this.m
        "a" -> this.a
        "s" -> this.s
        else -> throw IllegalArgumentException()
    }

    fun calcSum(): Int = x + m + a + s
}

private sealed interface Result

private data object Accepted : Result
private data object Rejected : Result
private data class NextWorkflow(val name: String) : Result

private fun String.toResult(): Result = when (this) {
    "A" -> Accepted
    "R" -> Rejected
    else -> NextWorkflow(this)
}
