
diceThrows = [(x,y) | x <- [1,2,3], y <- [1,2,3]]
tripleThrows = [(r1+s1+t1, r2+s2+t2) | (r1,r2) <- diceThrows, (s1,s2) <- diceThrows, (t1,t2) <- diceThrows]

rollDice2 :: (Int, Int) -> (Int, Int) -> (Int, Int)
rollDice2 field score
    | fst score >= 21 = (1, 0)
    | snd score >= 21 = (0, 1)
    | otherwise = (foldr1 rollFold tripleThrows) `seq` (foldr1 rollFold tripleThrows)
  where
    nextField roll = (\(p1, p2) (r1, r2) f -> (f p1 r1, f p2 r2)) field roll (\p r -> (p + r) `mod` 10)
    nextScore roll = (\(p1, p2) (r1, r2) f -> (f p1 r1, f p2 r2)) score roll (\p r -> p + r + 1)
    rollFold roll (a,b) = (\(x,y) -> (x+a, y+b)) $ pRoll roll
    pRoll roll = rollDice2 (nextField roll) (nextScore roll)

main = putStrLn . show $ rollDice2 (4 - 1,8 - 1) (0,0)
