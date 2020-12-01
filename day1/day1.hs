import Data.Maybe

getNumbers :: String -> [Int]
getNumbers s = map read (lines s)

findResult2 :: [Int] -> Int
findResult2 (l:ls) = fromMaybe (findResult2 ls) runCheck
  where checkSum _ [] = Nothing
        checkSum a (j:js) = if (a + j) == 2020 then Just (a * j) else checkSum a js
        runCheck = checkSum l ls

findResult3 :: [Int] -> Int
findResult3 (l:ls) = fromMaybe (findResult3 ls) runCheck1
  where checkSum2 _ _ [] = Nothing
        checkSum2 s a (j:js) = if (a + j) == s then Just (a * j) else checkSum2 s a js

        checksum1 _ [] = Nothing
        checksum1 a jl@(_:js) = if isJust (runCheck2 a jl) then Just (a * fromJust (runCheck2 a jl)) else checksum1 a js

        runCheck2 s (j:js) = checkSum2 (2020 - s) j js
        runCheck1 = checksum1 l ls

compute :: ([Int] -> Int) -> String -> String
compute f = show . f . getNumbers

results :: String -> [String]
results x = map (`compute` x) [findResult2, findResult3]

main = interact $ unlines . results
