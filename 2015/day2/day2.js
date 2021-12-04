const fs = require('fs')
const readline = require('readline')

const file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

let totalPaper = 0
let totalRibbon = 0
file.on('line', line => {
    const input = line.split('x');
    const lens = [parseInt(input[0], 10),
        parseInt(input[1], 10),
        parseInt(input[2], 10)]
    lens.sort((a, b) => a - b)
    totalPaper += 3 * lens[0] * lens[1]
        + 2 * lens[1] * lens[2]
        + 2 * lens[0] * lens[2]
    totalRibbon += lens[0] + lens[0] + lens[1] + lens[1]
        + lens[0] * lens[1] * lens[2]
})

file.on('close', () => {
    console.log(`${totalPaper} square feet paper`)
    console.log(`${totalRibbon} feet ribbon`)
})
