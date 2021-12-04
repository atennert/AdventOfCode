const md5 = require('blueimp-md5')

const input = 'iwrupvqb'

function compute(zeroes) {
    let counter = 0
    let result = ''
    while (true) {
        result = md5(`${input}${counter}`)
        if (result.startsWith(zeroes)) {
            break
        }
        counter++
    }
    console.log(result)
    return counter
}

console.log(compute('00000'))
console.log(compute('000000'))