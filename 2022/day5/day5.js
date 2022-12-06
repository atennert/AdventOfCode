const fs = require('fs')
const readline = require('readline')

const file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

let readMoves = false
const stacks1 = [
    'STHFWR'.split(''),
    'SGDQW'.split(''),
    'BTW'.split(''),
    'DRWTNQZJ'.split(''),
    'FBHGLVTZ'.split(''),
    'LPTCVBSG'.split(''),
    'ZBRTWGP'.split(''),
    'NGMTCJR'.split(''),
    'LGBW'.split(''),
]
const stacks2 = [
    'STHFWR'.split(''),
    'SGDQW'.split(''),
    'BTW'.split(''),
    'DRWTNQZJ'.split(''),
    'FBHGLVTZ'.split(''),
    'LPTCVBSG'.split(''),
    'ZBRTWGP'.split(''),
    'NGMTCJR'.split(''),
    'LGBW'.split(''),
]

file.on('line', line => {
    if (line === '') {
        readMoves = true
        return
    }
    if (!readMoves) {
        return
    }

    const [amount, from, to] = line.match(/\d+/g).map(n => +n)

    for (let i = 0; i < amount; i++) {
        stacks1[to - 1].push(stacks1[from - 1].pop())
    }
    stacks2[to - 1].push(...stacks2[from - 1].splice(-amount))
})

file.on('close', () => {
    console.log(stacks1)
    console.log(stacks2)
})
