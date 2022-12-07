const bossInitialHitPoints = 71;
const bossDamage = 10;
let turnBasedHP = 0

console.log(aStar({HP: 50, mana: 500, bossHP: bossInitialHitPoints, effects: []}))

turnBasedHP = -1
console.log(aStar({HP: 50, mana: 500, bossHP: bossInitialHitPoints, effects: []}))

function getActions(currentState) {
    const actions = []
    if (currentState.mana >= 229 && !currentState.effects.find(e => e.mana > 0 && e.turns > 1)) {
        actions.push({cost: 229, damage: 0, heal: 0, effect: {mana: 101, turns: 5, armor: 0, damage: 0}})
    }
    if (currentState.mana >= 173 && !currentState.effects.find(e => e.damage > 0 && e.turns > 1)) {
        actions.push({cost: 173, damage: 0, heal: 0, effect: {mana: 0, turns: 6, armor: 0, damage: 3}})
    }
    if (currentState.mana >= 113 && !currentState.effects.find(e => e.armor > 0 && e.turns > 1)) {
        actions.push({cost: 113, damage: 0, heal: 0, effect: {mana: 0, turns: 6, armor: 7, damage: 0}})
    }
    if (currentState.mana >= 73) {
        actions.push({cost: 73, damage: 2, heal: 2, effect: undefined})
    }
    if (currentState.mana >= 53) {
        actions.push({cost: 53, damage: 4, heal: 0, effect: undefined})
    }
    return actions
}

function simulate(currentState, action) {
    const newState0 = {
        ...currentState,
        HP: currentState.HP + turnBasedHP
    }
    if (newState0.HP <= 0) {
        return newState0
    }
    const newState1 = {
        ...newState0,
        effects: newState0.effects
            .map(e => ({...e, turns: e.turns - 1}))
            .filter(e => e.turns > 0),
        mana: newState0.mana - action.cost + (newState0.effects.find(e => e.mana > 0)?.mana ?? 0),
        bossHP: newState0.bossHP - action.damage - (newState0.effects.find(e => e.damage > 0)?.damage ?? 0)
    }
    if (action.effect) {
        newState1.effects.push(action.effect)
    }
    if (newState1.bossHP <= 0) {
        return newState1
    }
    return {
        ...newState1,
        effects: newState1.effects
            .map(e => ({...e, turns: e.turns - 1}))
            .filter(e => e.turns > 0),
        HP: newState1.HP + action.heal - bossDamage + (newState1.effects.find(e => e.armor > 0)?.armor ?? 0),
        mana: newState1.mana + (newState1.effects.find(e => e.mana > 0)?.mana ?? 0),
        bossHP: newState1.bossHP - (newState1.effects.find(e => e.damage > 0)?.damage ?? 0)
    }
}

function aStar(start) {
    let queue = [[0, start]]
    const visited = new Set()
    visited.add(JSON.stringify(start))
    const branch = new Map()
    let found

    while (queue.length > 0) {
        const currentState = queue.shift()[1]
        const currentCost = branch.get(JSON.stringify(currentState)) ?? 0

        if (currentState.bossHP <= 0) {
            found = {currentState, currentCost}
            break
        }

        for (let action of getActions(currentState)) {
            const nextState = simulate(currentState, action)
            if (nextState.HP <= 0 && nextState.bossHP > 0) {
                continue
            }
            const branchCost = currentCost + action.cost
            const queueCost = branchCost + nextState.bossHP
            const nextStateKey = JSON.stringify(nextState)

            if (!visited.has(nextStateKey) || branch.get(nextStateKey) > branchCost) {
                visited.add(nextStateKey)
                branch.set(nextStateKey, branchCost)
                queue = queue.filter(([, state]) => JSON.stringify(state) !== nextStateKey)
                queue.push([queueCost, nextState])
                queue.sort(([a], [b]) => a - b)
            }
        }
    }
    return found
}
