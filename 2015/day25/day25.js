
let currentValue = 20151125
let row = 1, col = 1

const targetRow = 3010, targetCol = 3019

while (row !== targetRow || col !== targetCol) {
    if (row === 1) {
        row = col + 1
        col = 1
    } else {
        row--
        col++
    }
    currentValue = (currentValue * 252533) % 33554393
}

console.log(currentValue)
