const fs = require('fs')

function analyse(data, filterRed = false) {
    if (typeof data === 'number') {
        return data
    } else if (Array.isArray(data)) {
        return data.reduce((acc, d) => acc + analyse(d, filterRed), 0)
    } else if (typeof data === 'object') {
        return (filterRed && Object.values(data).includes('red')) ? 0
            : Object.values(data)
                .reduce((acc, d) => acc + analyse(d, filterRed), 0)
    }
    return 0
}

fs.readFile('input.txt', 'utf-8', (err, data) => {
    if (err) {
        console.log(err)
        return
    }

    const json = JSON.parse(data)
    const sum = analyse(json)
    const sumNoRed = analyse(json, true)

    console.log(`sum of numbers: ${sum}`)
    console.log(`sum of numbers without red: ${sumNoRed}`)
})