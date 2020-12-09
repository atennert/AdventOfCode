#!/usr/bin/env runhaskell

isSum :: [Int] -> Int -> Bool
isSum ls e = elem e $ sums ls
  where sums (l:ls) = [l+x | x <- ls] ++ sums ls
        sums [] = []

findNonSum :: [Int] -> (Int, Int)
findNonSum l = (\r -> (r, findSum l r)) $ check $ splitAt 25 l
  where check (l1, l2) | isSum l1 $ head l2 = check (tail l1 ++ [head l2], tail l2)
                       | otherwise          = head l2

findSum :: [Int] -> Int -> Int
findSum lx s = check [] lx
  where check cs (l:ls) | sum cs < s = check (l:cs) ls
                        | sum cs > s = findSum (tail lx) s
                        | otherwise  = minimum cs + maximum cs

compute :: [String] -> [String]
compute x = map (show . ($ map read x)) [findNonSum]

main = interact $ unlines . compute . lines