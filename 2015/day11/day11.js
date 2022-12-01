const fs = require('fs')

const alphabet = 'abcdefghijklmnopqrstuvwxyz'

function increment(pw) {
    let newPw = ''
    let i = pw.length - 1
    for (; i >= 0; i--) {
        const ic = alphabet.indexOf(pw[i])
        let newC = (ic === 8 || ic === 11 || ic === 14)
            ? alphabet[ic + 2]
            : alphabet[(ic + 1) % alphabet.length]

        newPw = newC + newPw

        if (newC !== 'a') {
            break
        }
    }
    return pw.slice(0, i) + newPw
}

function increases(pw) {
    for (let i = 0; i < pw.length - 2; i++) {
        const i1 = alphabet.indexOf(pw[i + 2])
        const i2 = alphabet.indexOf(pw[i + 1])
        const i3 = alphabet.indexOf(pw[i])
        if (i1 === i2 + 1 && i2 === i3 + 1) {
            return true
        }
    }
    return false
}

function overlaps(pw) {
    let overlapCount = 0
    for (let i = 0; i < pw.length - 1; i++) {
        if (pw[i] === pw[i + 1]) {
            overlapCount++
            i++
        }
    }
    return overlapCount > 1
}

fs.readFile('input.txt', 'utf-8', (err, pw) => {
    if (err) {
        console.log(err)
        return
    }

    for (let run of [1, 2]) {
        do {
            pw = increment(pw)
        } while (!increases(pw) || !overlaps(pw))
        console.log(`Password ${run}: ${pw}`)
    }
})
