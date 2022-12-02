const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

let input;

file.on('line', line => {
    input = parseInt(line, 10)
})

file.on('close', () => {
    console.log('house number 1:', findHouseNumber(input, 10, Infinity))
    console.log('house number 2:', findHouseNumber(input, 11, 50))
});

function findHouseNumber(input, presentCount, maxHouses) {
    let houseNumber = 1
    for (let sum = 0; sum < input; houseNumber++) {
        const factors = findFactors(houseNumber, maxHouses)
        sum = [...factors].reduce((sum, factor) => sum + factor * presentCount, 0)
    }
    return houseNumber - 1
}

function findFactors(n, max) {
    const factors = new Set()
    for (let i = 1; i <= n; i++) {
        if (n % i === 0) {
            if ((n / i) <= max) {
                factors.add(i)
            }
            if (i <= max) {
                factors.add(n / i)
            }
        }
    }
    return factors
}
