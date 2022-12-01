#!/usr/bin/env kscript

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs

class Command(input: String) {
    var on = input.startsWith("on")
    var xMin: Int
    var xMax: Int
    var yMin: Int
    var yMax: Int
    var zMin: Int
    var zMax: Int

    init {
        val (xs, ys, zs) = input
            .replace(
                Regex("(on|off) x=(-?\\d+\\.\\.-?\\d+),y=(-?\\d+\\.\\.-?\\d+),z=(-?\\d+\\.\\.-?\\d+)"),
                "$2,$3,$4"
            )
            .split(',')
        xMin = xs.split("..")[0].toInt()
        xMax = xs.split("..")[1].toInt()
        yMin = ys.split("..")[0].toInt()
        yMax = ys.split("..")[1].toInt()
        zMin = zs.split("..")[0].toInt()
        zMax = zs.split("..")[1].toInt()
    }

    fun isIn(x: Int, y: Int, z: Int) = x in xMin..xMax && y in yMin..yMax && z in zMin..zMax
}

val commands = Files.readAllLines(Paths.get(args[0]))
    .map { Command(it) }
val commands1 = commands
    .filterNot { it.xMin > 50 || it.xMax < -50 || it.yMin > 50 || it.yMax < -50 || it.zMin > 50 || it.zMax < -50 }

var minX = commands1.minOf { it.xMin }
var maxX = commands1.maxOf { it.xMax }
var minY = commands1.minOf { it.yMin }
var maxY = commands1.maxOf { it.yMax }
var minZ = commands1.minOf { it.zMin }
var maxZ = commands1.maxOf { it.zMax }

val grid = Array(abs(maxX - minX) + 1) { Array(abs(maxY - minY) + 1) { Array(abs(maxZ - minZ) + 1) { false } } }

for (c in commands1) {
    for (x in c.xMin..c.xMax) {
        for (y in c.yMin..c.yMax) {
            for (z in c.zMin..c.zMax) {
                grid[x - minX][y - minY][z - minZ] = c.on
            }
        }
    }
}

println(grid.sumOf { x -> x.sumOf { y -> y.sumOf { z -> if (z) 1L else 0L } } })

//2758514936282235
minX = commands.minOf { it.xMin }
maxX = commands.maxOf { it.xMax }
minY = commands.minOf { it.yMin }
maxY = commands.maxOf { it.yMax }
minZ = commands.minOf { it.zMin }
maxZ = commands.maxOf { it.zMax }

var litCubes = 0L
val rCommands = commands.reversed()
for (x in minX..maxX) {
    for (y in minY..maxY) {
        for (z in minZ..maxZ) {
            for (c in rCommands) {
                if (c.isIn(x, y, z)) {
                    if (c.on) {
                        litCubes++
                    }
                    break
                }
            }
        }
    }
}

println(litCubes)