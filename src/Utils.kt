import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path(name).readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> =
    this.fold(emptyList<List<T>>() to emptyList<T>()) { (all, tmp), v ->
        if (predicate(v)) {
            if (tmp.isEmpty()) all to emptyList()
            else all append tmp to emptyList()
        } else {
            all to (tmp + v)
        }
    }.let { (all, tmp) ->
        if (tmp.isEmpty()) all
        else all append tmp
    }

infix fun <T> List<List<T>>.append(list: List<T>): List<List<T>> = this.plus<List<T>>(list)

fun <T> List<List<T>>.transpose(): List<List<T>> =
    if (this.isEmpty()) this
    else (this[0].indices).map { j ->
        (this.indices).map { i -> this[i][j] }
    }

fun String.substringBetween(fromDelimiter: String, untilDelimiter: String): String =
    this.substringAfter(fromDelimiter).substringBefore(untilDelimiter)

fun String.substringBetween(fromDelimiter: Char, untilDelimiter: Char): String =
    this.substringAfter(fromDelimiter).substringBefore(untilDelimiter)
