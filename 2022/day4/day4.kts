#!/usr/bin/env kscript

var fullOverlaps = 0
var allOverlaps = 0

while (true) {
    val assignments = readlnOrNull() ?: break

    val elfAssignments = assignments.split(',')
    val sectionsElf1 = elfAssignments[0].split('-')
    val sectionsElf2 = elfAssignments[1].split('-')

    val e1s1 = sectionsElf1[0].toInt()
    val e1s2 = sectionsElf1[1].toInt()
    val e2s1 = sectionsElf2[0].toInt()
    val e2s2 = sectionsElf2[1].toInt()

    if (e1s1 <= e2s1 && e1s2 >= e2s2 || e2s1 <= e1s1 && e2s2 >= e1s2) {
        fullOverlaps++
    }
    if (
        +e1s1 <= +e2s1 && +e2s1 <= +e1s2
        || +e1s1 <= +e2s2 && +e2s2 <= +e1s2
        || +e2s1 <= +e1s1 && +e1s1 <= +e2s2
        || +e2s1 <= +e1s2 && +e1s2 <= +e2s2
    ) {
        allOverlaps++
    }
}

println(fullOverlaps)
println(allOverlaps)

