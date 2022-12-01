const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const ingredients = []
file.on('line', line => {
    let [capacity, durability, flavor, texture, calories] = line
        .replace(/.+: capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)/, '$1,$2,$3,$4,$5')
        .split(',')
        .map(s => +s)

    ingredients.push({capacity, durability, flavor, texture, calories})
})

function analyseCookies(ingredients, teaspoons, distribution, ignoreCalories = true) {
    if (ingredients.length === 0) {
        const calories = distribution.reduce((acc, it) => acc + it.ingredient.calories * it.tsp, 0)
        const cookie = {
            capacity: Math.max(distribution.reduce((acc, it) => acc + it.ingredient.capacity * it.tsp, 0), 0),
            durability: Math.max(distribution.reduce((acc, it) => acc + it.ingredient.durability * it.tsp, 0), 0),
            flavor: Math.max(distribution.reduce((acc, it) => acc + it.ingredient.flavor * it.tsp, 0), 0),
            texture: Math.max(distribution.reduce((acc, it) => acc + it.ingredient.texture * it.tsp, 0), 0)
        }
        return {calories, score: Object.values(cookie).reduce((acc, it) => acc * it, 1)}
    }
    const [ingredient, ...leftIngredients] = ingredients
    const cookies = []
    for (let tsp = (leftIngredients.length === 0 ? teaspoons : 0); tsp <= teaspoons; tsp++) {
        cookies.push(analyseCookies(leftIngredients, teaspoons - tsp, [...distribution, {ingredient, tsp}], ignoreCalories))
    }
    return cookies.filter(c => ignoreCalories || c.calories === 500)
        .reduce((acc, c) => acc.score > c.score ? acc : c, {score: 0, calories: 0})
}

file.on('close', () => {
    const result1 = analyseCookies(ingredients, 100, [])
    console.log("score: " + result1.score)

    const result2 = analyseCookies(ingredients, 100, [], false)
    console.log("score: " + result2.score)
})
