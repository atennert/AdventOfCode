const fs = require('fs')
const readline = require('readline')

let file = readline.createInterface({
    input: fs.createReadStream('input.txt'),
    output: process.stdout,
    terminal: false
})

const raceTime = 2503

const reindeer = []
file.on('line', line => {
    let [speed, travelTime, restTime] = line
        .replace(/[A-Za-z ]+ (\d+) km\/s for (\d+) [A-Za-z, ]+ (\d+) \w+./, '$1,$2,$3')
        .split(',')
        .map(s => +s)

    reindeer.push({speed, travelTime, restTime})
})

file.on('close', () => {
    let distances = reindeer.map(r => (Math.floor(raceTime / (r.travelTime + r.restTime)) * r.travelTime
        + Math.min(raceTime % (r.travelTime + r.restTime), r.travelTime)) * r.speed)
    console.log("max distance: " + Math.max(...distances))

    const points = reindeer.map(() => 0)
    for (let t = 1; t <= raceTime; t++) {
        distances = reindeer.map(r => (Math.floor(t / (r.travelTime + r.restTime)) * r.travelTime
            + Math.min(t % (r.travelTime + r.restTime), r.travelTime)) * r.speed)
        const maxDist = Math.max(...distances)
        for (let i = 0; i < reindeer.length; i++) {
            if (distances[i] === maxDist) {
                points[i] = points[i] + 1
            }
        }
    }
    console.log("max points: " + Math.max(...points))
})
