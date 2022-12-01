const fs = require('fs')

fs.readFile('input.txt', 'utf-8', (err, data) => {
    if (err) {
        console.log(err)
        return
    }
    let floor = 0
    let counter = 0
    let basement = 0
    for (let c of data) {
        counter++
        floor = floor + (c === '(' ? 1 : -1)
        if (floor === -1 && basement === 0) {
            basement = counter
        }
    }
    console.log(`Floor: ${floor}`)
    console.log(`Basement: ${basement}`)
})