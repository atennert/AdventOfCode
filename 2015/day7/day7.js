const fs = require('fs')
const readline = require('readline')

function run(lastResult, resolve) {
    let file = readline.createInterface({
        input: fs.createReadStream('input.txt'),
        output: process.stdout,
        terminal: false
    })

    function parseCommand(command) {
        switch (command.length) {
            case 1:
                return {c: 'ASSIGN', a: command[0]}
            case 2:
                return {c: command[0], a: command[1]}
            default:
                return {c: command[1], a: command[0], b: command[2]}
        }
    }

    let commands = new Map()
    file.on('line', line => {
        let [command, result] = line.split(' -> ')
        command = command.split(' ')

        if (lastResult !== undefined && result === 'b') {
            command[0] = lastResult
        }

        commands.set(result, parseCommand(command))
    })

    function getParameter(parameter) {
        if (isNaN(parameter)) {
            const source = commands.get(parameter)
            return source['r'] === undefined
                ? undefined
                : +source['r']
        } else {
            return +parameter
        }
    }

    function solve(command, a, b) {
        switch (command['c']) {
            case 'AND':
                command['r'] = a & b
                break
            case 'OR':
                command['r'] = a | b
                break
            case 'LSHIFT':
                command['r'] = (a << b) & 0xFFFF
                break
            case 'RSHIFT':
                command['r'] = a >> b
                break
            case 'ASSIGN':
                command['r'] = a
                break
            case 'NOT':
                command['r'] = (~a) & 0xFFFF
                break
        }
    }

    function computeResultRound() {
        commands.forEach((command) => {
            if (command['r'] !== undefined) {
                return
            }
            let a = getParameter(command['a'], command)
            if (a === undefined) {
                return
            }
            let b = undefined
            if (command['b'] !== undefined) {
                b = getParameter(command['b'], command)
                if (b === undefined) {
                    return
                }
            }
            solve(command, a, b)
        })
    }

    file.on('close', () => {
        commands.set('$result', {c: 'ASSIGN', a: 'a'})
        while (commands.get('$result')['r'] === undefined) {
            computeResultRound()
        }
        console.log("a: " + commands.get('$result')['r'])
        resolve(commands.get('$result')['r'])
    })
}

new Promise((resolve => run(undefined, resolve)))
    .then(lastResult => run(lastResult, () => {}))