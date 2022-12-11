#!/usr/bin/env kscript

operator fun <T> List<T>.component6(): T = get(5)

class Monkey {
    val items = mutableListOf<Long>()
    lateinit var operation: (Long) -> Long
    lateinit var test: (Long) -> Boolean
    lateinit var trueTarget: () -> Int
    lateinit var falseTarget: () -> Int
    var inspections = 0L
}

val monkeys = mutableListOf(Monkey())
val monkeys2 = mutableListOf(Monkey())
var base = 1

while (true) {
    val line = readlnOrNull()?.trim() ?: break
    if (line.isEmpty()) {
        monkeys.add(Monkey())
        monkeys2.add(Monkey())
        continue
    }
    val monkey = monkeys.last()
    val monkey2 = monkeys2.last()

    when {
        line.startsWith("Starting") -> {
            monkey.items.addAll(line.split(' ')
                .drop(2)
                .map { it.replace(",", "").toLong() })
            monkey2.items.addAll(monkey.items)
        }
        line.startsWith("Operation") -> {
            val (_, _, _, _, op, num) = line.split(' ')
            monkey.operation = {
                val n = if (num == "old") it else num.toLong()
                if (op == "+") it + n else it * n
            }
            monkey2.operation = monkey.operation
        }
        line.startsWith("Test") -> {
            val num = line.split(' ').last().toInt()
            base *= num
            monkey.test = { it % num == 0L}
            monkey2.test = monkey.test
        }
        line.startsWith("If true") -> {
            val monkeyId = line.split(' ').last().toInt()
            monkey.trueTarget = { monkeyId }
            monkey2.trueTarget = monkey.trueTarget
        }
        line.startsWith("If false") -> {
            val monkeyId = line.split(' ').last().toInt()
            monkey.falseTarget = { monkeyId }
            monkey2.falseTarget = monkey.falseTarget
        }
        else -> {}
    }
}

fun run(monkeys: MutableList<Monkey>, rounds: Int, relief: (Long) -> Long): Long {
    for (i in 1..rounds) {
        for (monkey in monkeys) {
            while (monkey.items.isNotEmpty()) {
                val item = monkey.items.removeFirst()
                monkey.inspections++
                val newItem = relief(monkey.operation(item))
                val targetMonkey = if (monkey.test(newItem)) monkey.trueTarget() else monkey.falseTarget()
                monkeys[targetMonkey].items.add(newItem)
            }
        }
    }
    monkeys.sortByDescending { it.inspections }
    return monkeys[0].inspections * monkeys[1].inspections
}

println(run(monkeys, 20) { it / 3 })

println(run(monkeys2, 10000) { it % base })
