const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const weights = []

file.on('line', line => {
    weights.push(parseInt(line, 10))
})

file.on('close', () => {
    const sum = weights.reduce((sum, weight) => sum + weight, 0)
    weights.reverse()
    console.log(compute1(sum / 3, weights, []))
    console.log(compute2(sum / 4, weights, [], {l: Infinity, qe: Infinity}))
});

function compute1(target, weights, used) {
    const currentSum = used.reduce((sum, weight) => sum + weight, 0);
    if (currentSum > target) {
        return {l: Infinity}
    } else if (currentSum === target) {
        return {l: used.length, qe: used.reduce((prod, w) => prod * w, 1)}
    }
    for (let weight of weights) {
        const result = compute1(target, weights.filter(w => w !== weight), [...used, weight])
        if (result.l !== Infinity) {
            return result
        }
    }
    return {l: Infinity}
}

function compute2(target, weights, used, found) {
    const currentSum = used.reduce((sum, weight) => sum + weight, 0);
    const qe = used.reduce((prod, w) => prod * w, 1)
    if (currentSum > target) {
        return found
    } else if (currentSum === target && used.length <= found.l && qe < found.qe) {
        return {l: used.length, qe}
    }
    if (found.l <= used.length) {
        return found
    }
    let newFound = found
    for (let weight of weights) {
        newFound = compute2(target, weights.filter(w => w !== weight), [...used, weight], newFound)
    }
    return newFound
}
