package day4

fun main() {
    val possiblePWs = mutableListOf<Int>()
    for (pw in 236491..713787) {
        if (isAMatch(pw)) {
            possiblePWs.add(pw)
        }
    }
    println(possiblePWs.size)
}

fun isAMatch(pw: Int): Boolean {
    val pwAsString = pw.toString()

    return twoAdjacentDigitsAreSame(pwAsString) &&
            hasNoDecrease(pwAsString)
}

fun twoAdjacentDigitsAreSame(pw: String): Boolean {
    val lastPair = pw.fold(Pair('#', 1)) { acc, c ->
        when {
            c == acc.first -> Pair(c, acc.second + 1)
            acc.second == 2 -> return@twoAdjacentDigitsAreSame true
            else -> Pair(c, 1)
        }
    }
    return lastPair.second == 2
}

fun hasNoDecrease(pw: String): Boolean {
    pw.toByteArray()
        .reduce { b1, b2 ->
            if (b1 > b2) {
                return@hasNoDecrease false
            }
            b2
        }
    return true
}
