const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const containers = []
file.on('line', line => containers.push(+line))

function combine(used, containers, resultFn, addFn) {
    const sum = used.length > 0 ? used.reduce((acc, c) => acc + c) : 0
    const result = resultFn()
    if (sum === 150) {
        used.forEach(c => addFn(result, c))
        return [result]
    }
    if (sum > 150 || containers.length === 0) {
        return []
    }
    const [c, ...rest] = containers
    combine(used, rest, resultFn, addFn).forEach(r => addFn(result, r))
    combine([...used, c], rest, resultFn, addFn).forEach(r => addFn(result, r))
    return result
}

file.on('close', () => {
    const combos = combine([], containers, () => new Set(), (s, r) => s.add(r));
    console.log('combinations: ' + combos.size)

    const sortedCombos = combine([], containers, () => [], (s, r) => s.push(r))
        .sort((a, b) => a.length - b.length)
    const minContainers = sortedCombos[0].length
    console.log("min container count combos: " + sortedCombos.filter(cb => cb.length === minContainers).length)
})
