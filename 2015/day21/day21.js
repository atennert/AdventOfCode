const bossInitialHitPoints = 100;
const bossDamage = 8;
const bossArmor = 2;

const weapons = [
    {cost: 8, damage: 4},
    {cost: 10, damage: 5},
    {cost: 25, damage: 6},
    {cost: 40, damage: 7},
    {cost: 74, damage: 8},
]

const armors = [
    {cost: 0, armor: 0},
    {cost: 13, armor: 1},
    {cost: 31, armor: 2},
    {cost: 53, armor: 3},
    {cost: 75, armor: 4},
    {cost: 102, armor: 5},
]

const rings = [
    {cost: 0, damage: 0, armor: 0},
    {cost: 0, damage: 0, armor: 0},
    {cost: 25, damage: 1, armor: 0},
    {cost: 50, damage: 2, armor: 0},
    {cost: 100, damage: 3, armor: 0},
    {cost: 20, damage: 0, armor: 1},
    {cost: 40, damage: 0, armor: 2},
    {cost: 80, damage: 0, armor: 3},
]

let winCost = Infinity
let looseCost = 0

for (let weapon of weapons) {
    for (let armor of armors) {
        for (let ring1 of rings) {
            for (let ring2 of rings.filter(ring => ring !== ring1)) {
                if (simulate(weapon, armor, ring1, ring2)) {
                    winCost = Math.min(winCost, weapon.cost + armor.cost + ring1.cost + ring2.cost)
                } else {
                    looseCost = Math.max(looseCost, weapon.cost + armor.cost + ring1.cost + ring2.cost)
                }
            }
        }
    }
}

console.log(winCost)
console.log(looseCost)

function simulate(weapon, armor, ring1, ring2) {
    const myDamage = weapon.damage + ring1.damage + ring2.damage
    const myArmor = armor.armor + ring1.armor + ring2.armor
    let myHitPoints = 100;
    let bossHitPoints = bossInitialHitPoints;
    while (myHitPoints > 0) {
        bossHitPoints -= myDamage - bossArmor
        if (bossHitPoints <= 0) {
            break;
        }

        myHitPoints -= bossDamage - myArmor
    }
    return myHitPoints > 0
}
