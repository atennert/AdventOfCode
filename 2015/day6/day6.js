const fs = require('fs')
const readline = require('readline')

const file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

let lights = []
let lights2 = []
let line = []
let line2 = []
for (let i = 0; i < 1000; i++) {
    line.push(false)
    line2.push(0)
}
for (let i = 0; i < 1000; i++) {
    lights.push([...line])
    lights2.push([...line2])
}

function adjust(x1, y1, x2, y2, adapt, adapt2) {
    for (let y = y1; y <= y2; y++) {
        for (let x = x1; x <= x2; x++) {
            lights[y][x] = adapt(lights[y][x])
            lights2[y][x] = adapt2(lights2[y][x])
        }
    }
}

function getCoordinates(string) {
    const [c1, c2] = string.split(' through ')
    return [...(c1.split(',')), ...(c2.split(','))]
        .map(i => parseInt(i, 10))
}

file.on('line', line => {
    if (line.startsWith('turn on')) {
        line = line.replace('turn on ', '')
        const [x1, y1, x2, y2] = getCoordinates(line)
        adjust(x1, y1, x2, y2, () => true, light => light + 1)
    } else if (line.startsWith('turn off')) {
        line = line.replace('turn off ', '')
        const [x1, y1, x2, y2] = getCoordinates(line)
        adjust(x1, y1, x2, y2, () => false, light => Math.max(0, light - 1))
    } else {// toggle
        line = line.replace('toggle ', '')
        const [x1, y1, x2, y2] = getCoordinates(line)
        adjust(x1, y1, x2, y2, (light) => !light, light => light + 2)
    }
})

file.on('close', () => {
    const activeLights = lights.reduce((acc, line) => {
        return acc + line.reduce((acc, value) => acc + value, 0)
    }, 0)
    const activeLights2 = lights2.reduce((acc, line) => {
        return acc + line.reduce((acc, value) => acc + value, 0)
    }, 0)
    console.log(`active: ${activeLights}`)
    console.log(`brightness: ${activeLights2}`)
})
