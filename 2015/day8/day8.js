const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

let stringLiteralsCount = 0
let inMemoryCount = 0
let encodedCount = 0
file.on('line', line => {
    stringLiteralsCount += line.length

    for (let i = 1; i < line.length - 1; i++) {
        inMemoryCount++
        if (line[i] === '\\') {
            i += line[i + 1] === 'x' ? 3 : 1
        }
    }

    encodedCount += 2 // for additional ""
    line.split('')
        .map(c => c === '\\' || c === '"' ? 2 : 1)
        .forEach(v => encodedCount += v)
})

file.on('close', () => {
    console.log(`diff: ${stringLiteralsCount - inMemoryCount}`)
    console.log(`diff: ${encodedCount - stringLiteralsCount}`)
})
