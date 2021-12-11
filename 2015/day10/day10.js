const fs = require('fs')

fs.readFile('input.txt', 'utf-8', (err, data) => {
    if (err) {
        console.log(err)
        return
    }

    function run(rounds) {
        let arr = [[+data[0]]]
        for (let c of data.split('').splice(1)) {
            if (arr.at(-1).at(-1) === +c) {
                arr.at(-1).push(+c)
            } else {
                arr.push([+c])
            }
        }

        for (let i = 0; i < rounds; i++) {
            let newArr = []
            for (let a of arr) {
                if (newArr.length > 0 && newArr.at(-1)[0] === a.length) {
                    newArr.at(-1).push(a.length)
                } else {
                    newArr.push([a.length])
                }

                if (a[0] === a.length) {
                    newArr.at(-1).push(a[0])
                } else {
                    newArr.push([a[0]])
                }
            }
            arr = newArr
        }
        return arr.map(x => x.length).reduce((a, b) => a + b)
    }

    console.log('length after 40: ' + run(40))
    console.log('length after 50: ' + run(50))
})
