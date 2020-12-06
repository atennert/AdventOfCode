package day1

fun getRequiredFuelForModule(moduleMass: Int): Int {
    return moduleMass / 3 - 2
}

fun getRequiredFuelForModules(moduleMasses: List<Int>): Int {
    return moduleMasses
        .map(::getRequiredFuelForModule)
        .sum()
}

fun main() {
    val requiredFuel = getRequiredFuelForModules(loadInts("1/input.txt"))
    println(requiredFuel)
}
