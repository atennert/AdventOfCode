package day1

fun getAccumulatedFuels(mass: Int): List<Int> {
    val fuelList = mutableListOf<Int>()
    var nextMass = mass
    do {
        nextMass = getRequiredFuelForModule(nextMass)
        if(nextMass <= 0) {
            break
        }
        fuelList.add(nextMass)
    } while (true)
    return fuelList
}

fun main() {
    val totalFuel = loadInts("1/input.txt")
        .map(::getAccumulatedFuels)
        .map {it.sum()}
        .sum()
    println(totalFuel)
}
