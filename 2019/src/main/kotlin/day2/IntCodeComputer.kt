package day2

import java.io.BufferedReader
import java.io.InputStreamReader

enum class ParameterMode(val code: Char) {
    POSITION('0'),
    IMMEDIATE('1'),
    RELATIVE('2');

    companion object {
        operator fun get(stringCode: Char): ParameterMode {
            return values().single { it.code == stringCode }
        }
    }
}

enum class OpCode(val code: String) {
    ADD("01"),
    MULTIPLY("02"),
    READ("03"),
    PRINT("04"),
    JUMP_IF_TRUE("05"),
    JUMP_IF_FALSE("06"),
    LESS_THAN("07"),
    EQUALS("08"),
    SET_RELATIVE_BASE("09"),
    STOP("99");

    companion object {
        operator fun get(stringCode: String): OpCode {
            return values().single { it.code == stringCode }
        }
    }
}

class Instruction(instructionCode: String) {
    val opCode: OpCode
    val parameterModes: Array<ParameterMode>

    init {
        val correctedCode = instructionCode.padStart(5, '0')
        opCode = OpCode[correctedCode.takeLast(2)]
        val parameterModeString = correctedCode.take(3).reversed()

        parameterModes = parameterModeString.map { ParameterMode[it] }.toTypedArray()
    }
}

class IntCodeComputer(private val inputFcn: () -> String, private val outputFcn: (String, Int) -> Unit) {

    private var relativeBase = 0

    fun runIntCode(input: List<String>): List<String> {
        var code = input
        var codePos = 0
        var nextInstruction = Instruction(code[codePos])

        do {
            val result = executeOperation(nextInstruction, code, codePos)
            code = result.first
            codePos += result.second
            nextInstruction = Instruction(code[codePos])
        } while (nextInstruction.opCode != OpCode.STOP)

        return code
    }

    fun executeOperation(instruction: Instruction, code: List<String>, position: Int): Pair<List<String>, Int> {
        return when (instruction.opCode) {
            OpCode.ADD -> runBinaryOperation(code, instruction, position) { a, b -> a + b }
            OpCode.MULTIPLY -> runBinaryOperation(code, instruction, position) { a, b -> a * b }
            OpCode.PRINT -> print(code, instruction, position)
            OpCode.READ -> read(code, instruction, position)
            OpCode.JUMP_IF_TRUE -> jumpIfZero(false, code, instruction, position)
            OpCode.JUMP_IF_FALSE -> jumpIfZero(true, code, instruction, position)
            OpCode.LESS_THAN -> runBinaryOperation(code, instruction, position) { a, b -> if (a < b) 1 else 0 }
            OpCode.EQUALS -> runBinaryOperation(code, instruction, position) { a, b -> if (a == b) 1 else 0 }
            OpCode.SET_RELATIVE_BASE -> setRelativeBase(code, instruction, position)
            else -> throw IllegalArgumentException("Unknown op code $instruction")
        }
    }

    fun getParameter(code: List<String>, position: Int, mode: ParameterMode): Pair<List<String>, String> {
        val readPosition = when (mode) {
            ParameterMode.POSITION -> code.getOrElse(position) { "0" }.toInt()
            ParameterMode.IMMEDIATE -> position
            ParameterMode.RELATIVE -> relativeBase + code.getOrElse(position) { "0" }.toInt()
        }
        val newCode = checkMemoryAccess(code, readPosition)
        return Pair(newCode, newCode[readPosition])
    }

    fun setParameter(code: List<String>, position: Int, mode: ParameterMode, value: String): List<String> {
        val writePosition = when (mode) {
            ParameterMode.POSITION -> code.getOrElse(position) { "0" }.toInt()
            ParameterMode.IMMEDIATE -> position
            ParameterMode.RELATIVE -> relativeBase + code.getOrElse(position) { "0" }.toInt()
        }
        val newCode = checkMemoryAccess(code, writePosition).toMutableList()
        newCode[writePosition] = value
        return newCode
    }

    fun runBinaryOperation(
        code: List<String>,
        instruction: Instruction,
        position: Int,
        operation: (Long, Long) -> Long
    ): Pair<List<String>, Int> {
        val (code1, firstValue) = getParameter(code, position + 1, instruction.parameterModes[0])
        val (code2, secondValue) = getParameter(code1, position + 2, instruction.parameterModes[1])

        val result = operation(firstValue.toLong(), secondValue.toLong()).toString()
        val newCode = setParameter(code2, position + 3, instruction.parameterModes[2], result)
        return Pair(newCode, 4)
    }

    fun read(code: List<String>, instruction: Instruction, position: Int): Pair<List<String>, Int> {
        val input = inputFcn()

        val newCode = setParameter(code, position + 1, instruction.parameterModes[0], input)
        return Pair(newCode, 2)
    }

    fun print(code: List<String>, instruction: Instruction, position: Int): Pair<List<String>, Int> {
        val (newCode, value) = getParameter(code, position + 1, instruction.parameterModes[0])
        outputFcn(value, position)
        return Pair(newCode, 2)
    }

    fun jumpIfZero(eq: Boolean, code: List<String>, instruction: Instruction, position: Int): Pair<List<String>, Int> {
        val (code1, firstValue) = getParameter(code, position + 1, instruction.parameterModes[0])
        val (code2, secondValue) = getParameter(code1, position + 2, instruction.parameterModes[1])

        val isZero = firstValue.toLong() == 0.toLong()
        val positionOffset = when {
            eq xor isZero -> 3
            else -> secondValue.toInt() - position
        }
        return Pair(code2, positionOffset)
    }

    fun setRelativeBase(code: List<String>, instruction: Instruction, position: Int): Pair<List<String>, Int> {
        val (newCode, parameter) = getParameter(code, position + 1, instruction.parameterModes[0])
        relativeBase += parameter.toInt()
        return Pair(newCode, 2)
    }

    fun checkMemoryAccess(code: List<String>, position: Int): List<String> {
        return if (position >= code.size) {
            val newCode = code.toMutableList()
            repeat(position - code.size + 1) { newCode.add("0") }
            newCode
        } else {
            code
        }
    }
}

fun loadCode(inputFile: String): List<String> {
    val inputStream = ClassLoader.getSystemResource(inputFile).openStream()
    val bufferedInput = BufferedReader(InputStreamReader(inputStream))
    val code = bufferedInput.readText()
        .split(',')
        .map(String::trim)
    bufferedInput.close()
    return code
}
