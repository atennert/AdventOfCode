const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const registers = new Map([
    ['a', 0],
    ['b', 0],
])

const program = []

function handleHlf(line) {
    const [, r] = line.split(' ')
    registers.set(r, Math.floor(registers.get(r) / 2))
}

function handleTpl(line) {
    const [, r] = line.split(' ')
    registers.set(r, Math.floor(registers.get(r) * 3))
}

function handleInc(line) {
    const [, r] = line.split(' ')
    registers.set(r, Math.floor(registers.get(r) + 1))
}

function handleJmp(line) {
    const [, offset] = line.split(' ')
    return parseInt(offset, 10)
}

function handleJie(line) {
    const [, r, offset] = line.split(' ')
    return registers.get(r.replace(',', '')) % 2 === 0 ? parseInt(offset, 10) : 1
}

function handleJio(line) {
    const [, r, offset] = line.split(' ')
    return registers.get(r.replace(',', '')) === 1 ? parseInt(offset, 10) : 1
}

file.on('line', line => {
    program.push(line)
})

function runProgram() {
    for (let i = 0; i < program.length;) {
        const line = program[i]
        if (line.startsWith('hlf')) {
            handleHlf(line)
            i++
        } else if (line.startsWith('tpl')) {
            handleTpl(line)
            i++
        } else if (line.startsWith('inc')) {
            handleInc(line)
            i++
        } else if (line.startsWith('jmp')) {
            i += handleJmp(line)
        } else if (line.startsWith('jie')) {
            i += handleJie(line)
        } else if (line.startsWith('jio')) {
            i += handleJio(line)
        }
    }
}

file.on('close', () => {
    runProgram();
    console.log(registers.get('b'))

    registers.set('a', 1)
    registers.set('b', 0)
    runProgram();
    console.log(registers.get('b'))
});
