#!/usr/bin/env kscript

val numbers = readlnOrNull()!!
    .split(',')
    .map(String::toInt)

class Board {
    private val numbers = Array(5) {
        readln()
            .trim()
            .split(Regex("\\s+"))
            .map(String::toInt)
            .map { number -> Pair(number, false) }
            .toTypedArray()
    }

    fun mark(number: Int) {
        numbers.forEachIndexed { i, line ->
            line.forEachIndexed { j, elem ->
                if (elem.first == number) {
                    numbers[i][j] = Pair(number, true)
                }
            }
        }
    }

    fun check(): Boolean {
        return checkRows() || checkColumns()
    }

    private fun checkRows(): Boolean {
        for (i in 0..4) {
            var found = true
            for (j in 0..4) {
                found = found.and(numbers[i][j].second)
            }
            if (found) {
                return true
            }
        }
        return false
    }

    private fun checkColumns(): Boolean {
        for (j in 0..4) {
            var found = true
            for (i in 0..4) {
                found = found.and(numbers[i][j].second)
            }
            if (found) {
                return true
            }
        }
        return false
    }

    fun compute(): Int {
        var sum = 0
        for (i in 0..4) {
            for (j in 0..4) {
                if (!numbers[i][j].second) {
                    sum += numbers[i][j].first
                }
            }
        }
        return sum
    }
}

var boards = mutableListOf<Board>()
while (true) {
    readlnOrNull() ?: break

    boards.add(Board())
}

val finished = mutableListOf<Pair<Board, Int>>()
for (number in numbers) {
    for (board in boards) {
        board.mark(number)
    }

    for (board in boards) {
        if (board.check()) {
            finished.add(Pair(board, number))
        }
    }
    boards.removeAll(finished.map(Pair<Board, Int>::first))
}

println(finished.first().first.compute() * finished.first().second)
println(finished.last().first.compute() * finished.last().second)
