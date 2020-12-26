import Control.Arrow ((&&&))
import Data.List.Split (chunksOf)

rules :: ((Int, Int) -> Char) -> ((Int, Int) -> [t]) -> Int -> (Int, Int) -> Char
rules seat posCheck tol p | (seat p == 'L') && null   (posCheck p)       = '#'
                          | (seat p == '#') && length (posCheck p) > tol = 'L'
                          | otherwise                                    = seat p

applyRules1 :: (Int, Int) -> [String] -> [(Int, Int)] -> [String]
applyRules1 (lenX, lenY) seats = chunksOf (lenX+1) . map (rules seat (filter isOccupied . posAround) 3)
  where seat (x,y) = (seats!!y)!!x
        isOccupied = (== '#') . seat
        posAround (pX, pY) = [(x,y) | x <- [(max 0 (pX-1))..(min lenX (pX+1))], y <- [(max 0 (pY-1))..(min lenY (pY+1))], (x,y) /= (pX,pY)]

applyRules2 :: (Int, Int) -> [String] -> [(Int, Int)] -> [String]
applyRules2 (lenX, lenY) seats = chunksOf (lenX+1) . map (rules seat (filter isOccupied . posAround) 4)
  where seat (x,y) = (seats!!y)!!x
        isOccupied = head . foldr ((\s l -> if s == '.' then l else (s == '#'):l) . seat) [False]
        posAround (pX, pY) = [
              [(pX,  pY-i) | i <- [1..pY],              pY-i>=0],
              [(pX+i,pY-i) | i <- [1..(min lenX lenY)], pX+i<=lenX, pY-i>=0],
              [(pX+i,pY)   | i <- [1..lenX],            pX+i<=lenX],
              [(pX+i,pY+i) | i <- [1..(min lenX lenY)], pX+i<=lenX, pY+i<=lenY],
              [(pX,  pY+i) | i <- [1..lenY],            pY+i<=lenY],
              [(pX-i,pY+i) | i <- [1..(min lenX lenY)], pX-i>=0,    pY+i<=lenY],
              [(pX-i,pY)   | i <- [1..pX],              pX-i>=0],
              [(pX-i,pY-i) | i <- [1..(min lenX lenY)], pX-i>=0,    pY-i>=0]]

run :: ((Int, Int) -> [String] -> [(Int, Int)] -> [String]) -> [String] -> [String]
run applyRules seats = run' [] seats
  where (lenX,lenY) = (length (head seats) - 1, length seats - 1)
        positions = [(x,y) | y <- [0..lenY], x <- [0..lenX]]
        run' oldSeats seats
              | seats == oldSeats = seats
              | otherwise         = run' seats $ applyRules (lenX, lenY) seats positions

sumOccupied :: [String] -> Int
sumOccupied = length . filter ('#' ==) . unlines

main = interact $ show . compute . lines
  where compute = sumOccupied . run applyRules1 &&& sumOccupied . run applyRules2
