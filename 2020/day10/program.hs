import Control.Arrow ((&&&))
import Data.List (sort)

-- my initial solution for part 1
findJoltDiffs :: [Int] -> Int
findJoltDiffs (l:lx) = uncurry (*) $ count (0,0) l lx
  where count counts _ [] = counts
        count (mi,ma) la (l:ls) | la+1 == l = count (mi+1,ma) l ls
                                | la+3 == l = count (mi,ma+1) l ls
                                | otherwise = count (mi,ma) l ls

-- my initial solution for part 2
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

-- part 2 inspired by https://dev.to/qviper/advent-of-code-2020-python-solution-day-10-30kd
combinations' :: [Int] -> Int
combinations' = c'' 0 [1, 0, 0]
  where c'' _ (b:_) []  = b
        c'' e [b,u,f] (l:ls)
            | e+1 == l  = c'' (e+1) [b+u+f, b, u] ls
            | otherwise = c'' (e+1) [    0, b, u] (l:ls)

convert :: [String] -> [Int]
convert = (\x -> x ++ [last x + 3]) . sort . map read

main = interact $ show . compute . convert . lines
  where compute = findJoltDiffs &&& combinations' -- combinations . (0:)
