#!/usr/bin/env kscript

class Window(private val values: List<Int?> = listOf(null, null, null)) {
    fun add(value: Int): Window = Window(listOf(values[1], values[2], value))

    private fun sum(): Int? = values.reduce { a, b ->
        when {
            a == null -> null
            b == null -> null
            else -> a + b
        }
    }

    fun isSmaller(window: Window): Boolean = if (values.contains(null))
        false
    else
        sum()!! < window.sum()!!
}

var increases1 = 0
var increases2 = 0
var lastDepth: Int? = null
var lastWindow = Window()
while (true) {
    val newDepth = readlnOrNull()?.toInt() ?: break
    val newWindow = lastWindow.add(newDepth)

    lastDepth?.let {
        if (it < newDepth) {
            increases1++
        }
    }
    if (lastWindow.isSmaller(newWindow)) {
        increases2++
    }

    lastDepth = newDepth
    lastWindow = newWindow
}

println(increases1)
println(increases2)
