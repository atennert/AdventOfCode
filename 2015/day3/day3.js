const fs = require('fs')

fs.readFile('input.txt', 'utf-8', (err, data) => {
    if (err) {
        console.log(err)
        return
    }

    let x = 0
    let y = 0
    const houses = new Set()
    houses.add(`${x}-${y}`)
    for (let c of data) {
        switch (c) {
            case '^':
                y -= 1
                break
            case 'v':
                y +=1
                break
            case '>':
                x += 1
                break
            case '<':
                x -= 1
                break
        }
        houses.add(`${x}-${y}`)
    }
    console.log(houses.size)
})

fs.readFile('input.txt', 'utf-8', (err, data) => {
    if (err) {
        console.log(err)
        return
    }

    let x = [0, 0]
    let y = [0, 0]
    let i = 1;
    const houses = new Set()
    houses.add(`0-0`)
    for (let c of data) {
        i = (i + 1) % 2
        switch (c) {
            case '^':
                y[i] -= 1
                break
            case 'v':
                y[i] +=1
                break
            case '>':
                x[i] += 1
                break
            case '<':
                x[i] -= 1
                break
        }
        houses.add(`${x[i]}-${y[i]}`)
    }
    console.log(houses.size)
})