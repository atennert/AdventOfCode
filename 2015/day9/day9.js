const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const distances = new Map()
file.on('line', line => {
    let [places, d] = line.split(' = ')
    let [p1, p2] = places.split(' to ')

    if (!distances.has(p1)) {
        distances.set(p1, {})
    }
    if (!distances.has(p2)) {
        distances.set(p2, {})
    }
    distances.get(p1)[p2] = +d
    distances.get(p2)[p1] = +d
})

function findDistance(places, cf) {
    if (distances.size === places.length) {
        return 0
    }
    let currentPlace = places.at(-1)
    return cf(...Object.keys(currentPlace)
        .filter(place => !places.includes(distances.get(place)))
        .map(place => currentPlace[place] + findDistance([...places, distances.get(place)], cf)))
}

file.on('close', () => {
    let shortestDistance = Math.min(...Array.from(distances.values(),
        place => findDistance([place], Math.min)))

    let longestDistance = Math.max(...Array.from(distances.values(),
        place => findDistance([place], Math.max)))

    console.log(`shortest distance: ${shortestDistance}`)
    console.log(`longest distance: ${longestDistance}`)
})
