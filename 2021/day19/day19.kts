#!/usr/bin/env kscript

import kotlin.math.abs

class Beacon(var x: Int, var y: Int, var z: Int) {
    fun rotateX() {
        val tmp = y
        y = -z
        z = tmp
    }

    fun rotateY() {
        val tmp = x
        x = -z
        z = tmp
    }

    fun rotateZ() {
        val tmp = x
        x = y
        y = -tmp
    }

    operator fun minus(o: Beacon) = Diff(x - o.x, y - o.y, z - o.z)

    operator fun plus(o: Diff): Beacon {
        x += o.x
        y += o.y
        z += o.z
        return this
    }

    override fun toString() = "[$x,$y,$z]"

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

    override fun equals(other: Any?) = other is Beacon && x == other.x && y == other.y && z == other.z
}

data class Diff(val x: Int, val y: Int, val z: Int)

class Scanner(val id: Int) {
    val beacons = mutableListOf<Beacon>()
    lateinit var pos: Diff

    fun addBeacon(beacon: Beacon) {
        beacons.add(beacon)
    }

    fun rotateX() {
        beacons.forEach(Beacon::rotateX)
    }

    fun rotateY() {
        beacons.forEach(Beacon::rotateY)
    }

    fun rotateZ() {
        beacons.forEach(Beacon::rotateZ)
    }

    override fun toString() = "$id ($beacons)"
}

val scanners = mutableListOf<Scanner>()
input@ while (true) {
    val scannerName = readln().replace(Regex("--- scanner (\\d+) ---"), "$1")
    val scanner = Scanner(scannerName.toInt())
    scanners.add(scanner)
    while (true) {
        val line = readlnOrNull() ?: break@input
        if (line.isEmpty()) {
            break
        }
        val (x, y, z) = line.split(',').map { it.toInt() }
        scanner.addBeacon(Beacon(x, y, z))
    }
}

val alignedScanners = mutableListOf(scanners.removeFirst())
val alignedBeacons = mutableSetOf<Beacon>()
alignedBeacons.addAll(alignedScanners[0].beacons)
alignedScanners[0].pos = Diff(0, 0, 0)

val rotations = listOf(
    { },
    Scanner::rotateZ, Scanner::rotateZ, Scanner::rotateZ,
    Scanner::rotateY,
    Scanner::rotateX, Scanner::rotateX, Scanner::rotateX,
    Scanner::rotateZ,
    Scanner::rotateY, Scanner::rotateY, Scanner::rotateY,
    Scanner::rotateZ,
    Scanner::rotateX, Scanner::rotateX, Scanner::rotateX,
    Scanner::rotateZ,
    Scanner::rotateY, Scanner::rotateY, Scanner::rotateY,
    Scanner::rotateX,
    Scanner::rotateZ, Scanner::rotateZ, Scanner::rotateZ,
)

fun overlap(bs1: List<Beacon>, bs2: List<Beacon>, diff: Diff?, found: Set<Beacon>): Pair<Set<Beacon>, Diff>? {
    if (found.size >= 12) {
        return Pair(found, diff!!)
    }
    if (bs1.isEmpty() || bs2.isEmpty()) {
        return null
    }
    for (b1 in bs1) {
        for (b2 in bs2) {
            if (diff != null && b1 - b2 != diff) {
                continue
            }
            val result = overlap(bs1 - b1, bs2 - b2, b1 - b2, found + b2)
            if (result != null) {
                return result
            }
        }
    }
    return if (found.size >= 12) Pair(found, diff!!) else null
}

val checked = mutableMapOf<Int, MutableSet<Int>>()
while (scanners.isNotEmpty()) {
    var fixedScanner: Scanner? = null
    outer@ for (s in scanners) {
        if (!checked.containsKey(s.id)) {
            checked[s.id] = mutableSetOf()
        }
        println("checking " + s.id)
        for (sAligned in alignedScanners) {
            if (checked[s.id]!!.contains(sAligned.id)) {
                continue
            }
            checked[s.id]!!.add(sAligned.id)
            for (r in rotations) {
                s.apply(r)
                val found = overlap(sAligned.beacons, s.beacons, null, setOf())

                if (found != null) {
                    fixedScanner = s
                    alignedBeacons.addAll(s.beacons.map { it + found.second })
                    s.pos = found.second
                    alignedScanners.add(s)
                    break@outer
                }
            }
        }
    }
    if (fixedScanner == null) {
        error("unable to find scanner")
    } else {
        println("fixed: " + fixedScanner?.id)
        scanners.remove(fixedScanner)
    }
}
println("beacon count: " + alignedBeacons.size)

fun maxDist(): Int {
    var maxDist = 0
    for (s1 in alignedScanners) {
        for (s2 in alignedScanners) {
            val dist = abs(s1.pos.x - s2.pos.x) + abs(s1.pos.y - s2.pos.y) + abs(s1.pos.z - s2.pos.z)
            if (dist > maxDist) {
                maxDist = dist
            }
        }
    }
    return maxDist
}
println("max distance: " + maxDist())