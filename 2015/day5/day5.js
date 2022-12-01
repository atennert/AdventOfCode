const fs = require('fs')
const readline = require('readline')

const file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

let nice = 0
let nice2 = 0
file.on('line', line => {
    let vowels = 0
    for (let c of line) {
        if ('aeiou'.includes(c)) {
            vowels++
        }
        if (vowels >= 3) break
    }

    let doubleLetter = false
    for (let i = 0; i < line.length - 1; i++) {
        if (line[i] === line[i+1]) {
            doubleLetter = true
            break
        }
    }

    let forbidden = false
    for (let s of ['ab', 'cd', 'pq', 'xy']) {
        if (line.includes(s)) {
            forbidden = true
        }
    }

    let doubleDouble = false
    for (let i = 0; i < line.length - 3; i++) {
        if (line.includes(`${line[i]}${line[i+1]}`, i+2)) {
            doubleDouble = true
            break
        }
    }

    let doubleLetter2 = false
    for (let i = 0; i < line.length - 2; i++) {
        if (line[i] === line[i+2]) {
            doubleLetter2 = true
            break
        }
    }

    if (!forbidden && doubleLetter && vowels >= 3) {
        nice++
    }
    if (doubleLetter2 && doubleDouble) {
        nice2++
    }
})

file.on('close', () => {
    console.log(`nice: ${nice}`)
    console.log(`nice2: ${nice2}`)
})
