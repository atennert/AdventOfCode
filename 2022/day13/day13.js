const fs = require('fs')
const readline = require('readline')

const file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const lines = []

file.on('line', line => {
    if (line !== '') {
        lines.push(JSON.parse(line))
    }
})

file.on('close', () => {
    console.log(getOrderedCount())

    const a = [[2]], b = [[6]]
    lines.push(a, b)
    lines.sort(compare)
    console.log((lines.indexOf(a) + 1) * (lines.indexOf(b) + 1))
})

function getOrderedCount() {
    let ordered = 0
    for (let i = 0; i < lines.length; i += 2) {
        ordered += compare(lines[i], lines[i+1]) < 0 ? i / 2 + 1 : 0
    }
    return ordered
}

function compare(left, right) {
    if (Array.isArray(left) && Array.isArray(right)) {
        for (let i = 0; i < Math.max(left.length, right.length); i++) {
            if (i >= left.length) {
                return -1
            } else if (i >= right.length) {
                return 1
            }
            const result = compare(left[i], right[i])
            if (result !== 0) {
                return result
            }
        }
    } else if (Array.isArray(left)) {
        return compare(left, [right])
    } else if (Array.isArray(right)) {
        return compare([left], right)
    } else {
        return left - right
    }
    return 0
}