import Data.List (sort)

findJoltDiffs :: [Int] -> Int
findJoltDiffs (l:lx) = uncurry (*) $ count (0,0) l lx
  where count counts _ [] = counts
        count (mi,ma) la (l:ls) | la+1 == l = count (mi+1,ma) l ls
                                | la+3 == l = count (mi,ma+1) l ls
                                | otherwise = count (mi,ma) l ls

combinations :: [Int] -> Int
combinations [] = 1
combinations [x] = 1
combinations [x,y] = 1
combinations [i,j,k] = 1
combinations lx@(i:j:k:l:ls)
    | i+3 == l  = tribonacci lChain * combinations (drop lChain lx)
    | i+3 >= k  = 2 * combinations (j:k:l:ls)
    | otherwise = combinations (j:k:l:ls)
  where x e (l:ls) = if e+1 == l then 1 + x l ls else 0
        lChain = x i (j:k:l:ls) - 1

tribonacci :: Int -> Int
tribonacci 1 = 2
tribonacci 2 = 4
tribonacci 3 = 7
tribonacci n = tribonacci (n - 1) + tribonacci (n - 2) + tribonacci (n - 3)

prepareData :: [String] -> [Int]
prepareData = (\x -> x ++ [last x + 3]) . (0:) . sort . map read

compute :: [String] -> [String]
compute x = map (show . ($ prepareData x)) [findJoltDiffs, combinations]

main = interact $ unlines . compute . lines