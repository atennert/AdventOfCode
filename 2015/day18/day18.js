const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const lights = []
let max = 0

file.on('line', line => {
    lights.push(line.split('').map(c => c === '#'))
    max = lights.length - 1
})

file.on('close', () => {
    let newLights = lights
    for (let i = 0; i < 100; i++) {
        newLights = animateLights(newLights, false)
    }
    console.log(countActiveLights(newLights))

    newLights = lights
    newLights[0][0] = newLights[max][0] = newLights[0][max] = newLights[max][max] = true
    for (let i = 0; i < 100; i++) {
        newLights = animateLights(newLights, true)
    }
    console.log(countActiveLights(newLights))
});

function animateLights(lights, isCornerStuck) {
    return lights.map((line, i) => {
        return line.map((light, j) => computeNewLightState(light, i, j, lights, isCornerStuck))
    })
}

function computeNewLightState(isOn, i, j, lights, isCornerStuck) {
    const surroundingFields = [
        [i-1, j-1], [i-1, j], [i-1, j+1],
        [i,   j-1],           [i,   j+1],
        [i+1, j-1], [i+1, j], [i+1, j+1],
    ]
    const surroundingLights = surroundingFields
        .map(([i, j]) => getField(lights, i, j, isCornerStuck))
        .reduce((sum, light) => sum + (light ? 1 : 0), 0)

    if (isOn) {
        return surroundingLights === 2 || surroundingLights === 3 || isStuck(isCornerStuck, i, j)
    } else {
        return surroundingLights === 3
    }
}

function getField(lights, i, j, isCornerStuck) {
    if (isStuck(isCornerStuck, i, j)) {
        return true
    }
    return i < 0 || j < 0 || i > max || j > max ? false : lights[i][j]
}

function isStuck(isCornerStuck, i, j) {
    return isCornerStuck && (i === 0 || i === max) && (j === 0 || j === max);
}

function countActiveLights(lights) {
    return lights.reduce((sum, line) => {
        return sum + line.reduce((sum, light) => sum + (light ? 1 : 0), 0)
    }, 0)
}