const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const data = {
    children: 3,
    cats: 7,
    samoyeds: 2,
    pomeranians: 3,
    akitas: 0,
    vizslas: 0,
    goldfish: 5,
    trees: 3,
    cars: 2,
    perfumes: 1
}

file.on('line', line => {
    let [aunt, things] = line
        .replace(/Sue (\d+): (.*)/, '$1;$2')
        .split(';')
    const auntObj = {aunt}

    things.split(', ')
        .forEach(t => {
            const [name, count] = t.split(': ')
            auntObj[name] = +count
        })

    find1(auntObj)
    find2(auntObj)
})

function find1(aunt) {
    let matches = true
    for (let key of Object.keys(aunt).filter(k => k !== 'aunt')) {
        matches &= aunt[key] === data[key]
    }
    if (matches) {
        console.log('aunt 1: ' + aunt.aunt)
    }
}

function find2(aunt) {
    let matches = true
    for (let key of Object.keys(aunt).filter(k => k !== 'aunt')) {
        if (key === 'cats' || key === 'trees') {
            matches &= aunt[key] > data[key]
        } else if (key === 'pomeranians' || key === 'goldfish') {
            matches &= aunt[key] < data[key]
        } else {
            matches &= aunt[key] === data[key]
        }
    }
    if (matches) {
        console.log('aunt 2: ' + aunt.aunt)
    }
}

file.on('close', () => {
    console.log('done :)')
})
