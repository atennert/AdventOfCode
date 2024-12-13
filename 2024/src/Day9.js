"use strict"

export const checksum = runBlock => blocks => {
  let result = BigInt(0);

  let tmp = [...blocks];
  let i = BigInt(0);
  while (tmp.length) {
    const head = tmp.shift();

    const runResult = runBlock(i)(head)(tmp);
    if (runResult.value0 !== undefined) {
      const r = runResult.value0.value0,
        newHead = runResult.value0.value1,
        newTail = runResult.value0.value2;

      result += r;
      i++;
      tmp = [newHead, ...newTail];
    }
  }
  return result;
}

export const sortBlocks = mkSpace => blocks => {
  const l = [...blocks];
  const revl = [];

  while (l.length) {
    const last = l.pop();
    if (isSpace(last)) {
      revl.unshift(last);
      continue;
    }

    const len = last.value1;
    let moved = false;
    for (let i = 0; i < l.length; i++) {
      const b = l[i];
      if (!isSpace(b)) {
        continue;
      }

      if (b.value0 === len) {
        l[i] = last;
        revl.unshift(mkSpace(len))
        moved = true;
        break;
      } else if (b.value0 > len) {
        b.value0 -= len;
        l.splice(i, 0, last)
        revl.unshift(mkSpace(len))
        moved = true;
        break;
      }
    }
    if (!moved) {
      revl.unshift(last);
    }
  }
  return revl;
}

const isSpace = x => x.value1 === undefined;
