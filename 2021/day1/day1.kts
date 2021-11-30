#!/usr/bin/env kscript

var line = ""
while (true) {
    line = readlnOrNull() ?: break

    println(line)
}
