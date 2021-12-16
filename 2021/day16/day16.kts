#!/usr/bin/env kscript

val message = readln().map { it.digitToInt(16) }.toIntArray()

sealed class Packet(val version: Int) {
    abstract fun getVersionSum(): Int

    abstract fun evaluate(): Long
}

class LiteralPacket(version: Int, private val value: Long) : Packet(version) {
    override fun getVersionSum() = version

    override fun evaluate() = value

    override fun toString() = "LiteralPacket: ($value)"
}

class OperatorPacket(version: Int, private val type: Int, private val subPackets: List<Packet>) : Packet(version) {
    override fun getVersionSum() = version + subPackets.sumOf { it.getVersionSum() }

    override fun evaluate(): Long {
        return when (type) {
            0 -> subPackets.sumOf { it.evaluate() }
            1 -> subPackets.foldRight(1) { packet, acc -> packet.evaluate() * acc }
            2 -> subPackets.minOf { it.evaluate() }
            3 -> subPackets.maxOf { it.evaluate() }
            5 -> if (subPackets[0].evaluate() > subPackets[1].evaluate()) 1 else 0
            6 -> if (subPackets[0].evaluate() < subPackets[1].evaluate()) 1 else 0
            7 -> if (subPackets[0].evaluate() == subPackets[1].evaluate()) 1 else 0
            else -> error("unknown type: $type")
        }
    }

    override fun toString()= "OperatorPacket: ($subPackets)"
}

fun readLiteral(message: IntArray, startIndex: Int): Pair<Packet, Int> {
    var prefixIndex = startIndex + 6
    var value = 0L
    do {
        val keepReading = message.readBits(prefixIndex, 1) == 1
        value = value.shl(4)
            .or(message.readBits(prefixIndex + 1, 4).toLong())
        prefixIndex += 5
    } while (keepReading)
    return Pair(LiteralPacket(message.readBits(startIndex, 3), value), prefixIndex)
}

fun readOperator(message: IntArray, startIndex: Int, type: Int): Pair<Packet, Int> {
    val lengthTypeIndex = startIndex + 6
    val useTotalLength = message.readBits(lengthTypeIndex, 1) == 0
    val subPackets = mutableListOf<Packet>()
    var nextStartIndex: Int
    if (useTotalLength) {
        val totalLength = message.readBits(lengthTypeIndex + 1, 15)
        val contentStartIndex = lengthTypeIndex + 16
        nextStartIndex = contentStartIndex
        do {
            val (packet, nsi) = convertMessage(message, nextStartIndex)
            subPackets.add(packet)
            nextStartIndex = nsi
        } while (nextStartIndex < contentStartIndex + totalLength)
    } else { // use subPacketCount count
        val subPacketCount = message.readBits(lengthTypeIndex + 1, 11)
        nextStartIndex = lengthTypeIndex + 12
        for (i in 0 until subPacketCount) {
            val (packet, nsi) = convertMessage(message, nextStartIndex)
            subPackets.add(packet)
            nextStartIndex = nsi
        }
    }
    return Pair(OperatorPacket(message.readBits(startIndex, 3), type, subPackets), nextStartIndex)
}

fun IntArray.readBits(address: Int, length: Int): Int {
    var value = 0
    var size = 0
    for (i in (address / 4) * 4 until address + length step 4) {
        value = value.shl(4).or(this[i / 4])
        size++
    }
    return value.shr(size * 4 - length - (address % 4))
        .and(getMaskOfLength(length))
}

fun getMaskOfLength(length: Int): Int {
    var mask = 0
    for (i in 0 until length) {
        mask = mask.shl(1).or(1)
    }
    return mask
}

fun convertMessage(message: IntArray, startIndex: Int): Pair<Packet, Int> {
    return when (val type = message.readBits(startIndex + 3, 3)) {
        4 -> readLiteral(message, startIndex)
        else -> readOperator(message, startIndex, type)
    }
}

println("version sum: ${convertMessage(message, 0).first.getVersionSum()}")
println("version sum: ${convertMessage(message, 0).first.evaluate()}")
