const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const relations = new Map()
file.on('line', line => {
    let [person1, happiness, person2] = line
        .replace('lose ', '-')
        .replace(/(\S+) [A-Za-z ]*(-?\d+)\D* (\S+)./, '$1,$2,$3')
        .split(',')

    if (!relations.has(person1)) {
        relations.set(person1, {})
    }
    relations.get(person1)[person2] = +happiness
})

function sumHappiness(seating) {
    let sum = 0
    for (let i = 0; i < seating.length; i++) {
        const person1 = seating[i]
        const person2 = seating[(i + 1) % seating.length]
        sum += relations.get(person1)[person2] + relations.get(person2)[person1]
    }
    return sum
}

function compute(persons, seating) {
    return (seating.length === persons.length)
        ? sumHappiness(seating)
        : Math.max(...persons.filter(p => !seating.includes(p))
            .map(p => compute(persons, [...seating, p])))
}

file.on('close', () => {
    const persons = [...relations.keys()]
    console.log("Happiness: " + compute(persons, []))

    relations.set('me', {})
    persons.forEach(p => {
        relations.get(p)['me'] = 0
        relations.get('me')[p] = 0
    })
    console.log("Happiness with me: " + compute([...persons, 'me'], []))
})
