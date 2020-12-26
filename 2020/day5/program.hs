import Control.Arrow ((&&&))
import Data.List (sort)

getSeat :: String -> (Int, Int)
getSeat = (\(r,c) -> (conv 'B' r, conv 'R' c)) . splitAt 7
  where conv x = snd . foldr (\l (e,s) -> if l == x then (e*2,s+e) else (e*2,s)) (1,0)

getSeatId :: (Int, Int) -> Int
getSeatId (row, col) = row * 8 + col

findEmptySeat :: [Int] -> Int
findEmptySeat (a:b:ls) | a+1 == b  = findEmptySeat (b:ls)
                       | otherwise = a+1

getLast :: [String] -> Int
getLast = maximum . map (getSeatId . getSeat)

getMySeat :: [String] -> Int
getMySeat = findEmptySeat . sort . map (getSeatId . getSeat)

main = interact $ show . compute . lines
  where compute = getLast &&& getMySeat
