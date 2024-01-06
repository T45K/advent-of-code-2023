package day20

import readInput

fun main() {
    val input = readInput("input/Day20.txt")
    val parsedInput = input.map { line ->
        val (moduleName, destinations) = line.split(" -> ")
        moduleName to destinations.split(", ")
    }
    val conjunctionNames = parsedInput.map { (moduleName, _) -> moduleName }
        .filter { it.first() == '&' }
        .map { it.substring(1) }

    val inputModuleNamesByConjunctionName = parsedInput.flatMap { (moduleName, destinations) ->
        conjunctionNames.filter { conjunctionName -> destinations.contains(conjunctionName) }
            .map { it to moduleName.substring(1) }
    }.groupBy({ it.first }, { it.second })

    val moduleByName = parsedInput.associate { (moduleName, destinations) ->
        val module = when {
            moduleName == "broadcaster" -> Broadcaster(destinations = destinations)
            moduleName.first() == '%' -> FlipFlop(moduleName.substring(1), destinations)
            moduleName.first() == '&' -> {
                val name = moduleName.substring(1)
                Conjunction(
                    name,
                    inputModuleNamesByConjunctionName[name] ?: emptyList(),
                    destinations,
                )
            }

            else -> throw IllegalArgumentException()
        }
        module.name to module
    }

    val answer = (1..1_000).fold(0 to 0) { (lowPulseCount, highPulseCount), _ ->
        var tmpLowPulseCount = 0
        var tmpHighPulseCount = 0
        val queue = ArrayDeque<Emission>()
        queue.add(Emission(Pulse.LOW, "button", "broadcaster"))

        while (queue.isNotEmpty()) {
            val emission = queue.removeFirst()
            if (emission.pulse == Pulse.LOW) {
                tmpLowPulseCount++
            } else {
                tmpHighPulseCount++
            }
            queue.addAll(moduleByName[emission.toModule]?.receive(emission.pulse, emission.fromModule) ?: emptyList())
        }

        (lowPulseCount + tmpLowPulseCount) to (highPulseCount + tmpHighPulseCount)
    }.let { it.first.toLong() * it.second }
    println(answer)
}

data class Emission(val pulse: Pulse, val fromModule: String, val toModule: String)

enum class Pulse {
    HIGH, LOW
}

sealed interface Module {
    val name: String

    fun receive(pulse: Pulse, fromModule: String): List<Emission>
}

class FlipFlop(override val name: String, private val outputModules: List<String>) : Module {
    private var isOn = false

    override fun receive(pulse: Pulse, fromModule: String): List<Emission> = when (pulse) {
        Pulse.HIGH -> emptyList()
        Pulse.LOW ->
            if (isOn) {
                isOn = false
                outputModules.map { Emission(Pulse.LOW, this.name, it) }
            } else {
                isOn = true
                outputModules.map { Emission(Pulse.HIGH, this.name, it) }
            }
    }
}

class Conjunction(
    override val name: String,
    inputModules: List<String>,
    private val destinations: List<String>
) : Module {
    private val latestPulseByInputModule: MutableMap<String, Pulse> =
        inputModules.associateWith { Pulse.LOW }.toMutableMap()

    override fun receive(pulse: Pulse, fromModule: String): List<Emission> {
        latestPulseByInputModule[fromModule] = pulse
        val emittedPulse = if (latestPulseByInputModule.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
        return destinations.map { Emission(emittedPulse, this.name, it) }
    }
}

class Broadcaster(override val name: String = "broadcaster", private val destinations: List<String>) : Module {
    override fun receive(pulse: Pulse, fromModule: String): List<Emission> =
        destinations.map { Emission(pulse, this.name, it) }
}
