const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const replacements = new Map()
const revReplacements = new Map()
let molecule = ''
let readMolecule = false

file.on('line', line => {
    if (line === '') {
        readMolecule = true
    }

    if (readMolecule) {
        molecule = line
    } else {
        const [key, value] = line.split(' => ')
        revReplacements.set(value, key)
        const values = replacements.get(key) ?? new Set()
        values.add(value)
        replacements.set(key, values)
    }
})

file.on('close', () => {
    console.log(computeOptions(molecule).size)
    console.log(aStar(molecule, 'e'))
});

function computeOptions(currentState) {
    const results = new Set()
    for (let [key, value] of replacements.entries()) {
        for (let replacement of value) {
            let i = -1
            do {
                i = currentState.indexOf(key, i + 1)
                if (i !== -1) {
                    results.add(currentState.slice(0, i) + currentState.slice(i).replace(key, replacement))
                }
            } while (i !== -1)
        }
    }
    return results
}

function getActions(currentState) {
    const results = new Set()
    for (let [key, replacement] of revReplacements.entries()) {
        let i = -1
        do {
            i = currentState.indexOf(key, i + 1)
            if (i !== -1) {
                results.add(currentState.slice(0, i) + currentState.slice(i).replace(key, replacement))
            }
        } while (i !== -1)
    }
    return results;
}

function aStar(start, goal) {
    let queue = [[start.length, start]]
    const visited = new Set()
    visited.add(start)
    const branch = new Map()
    let found = false

    while (queue.length > 0) {
        const currentState = queue.shift()[1]
        const currentCost = branch.get(currentState) ?? 0

        if (currentState === goal) {
            found = true
            break
        }

        for (let nextState of getActions(currentState)) {
            const branchCost = currentCost + 1
            const queueCost = branchCost + nextState.length

            if (!visited.has(nextState) || branch.get(nextState) > branchCost) {
                visited.add(nextState)
                branch.set(nextState, branchCost)
                queue = queue.filter(([, state]) => state !== nextState)
                queue.push([queueCost, nextState])
                queue.sort(([a], [b]) => a - b)
            }
        }
    }
    return branch.get(goal)
}
