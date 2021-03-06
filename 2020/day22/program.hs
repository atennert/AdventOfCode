import Control.Arrow ((&&&))
import Data.Bifunctor (bimap)

type Card = Int

play1 :: [Card] -> [Card] -> Int
play1 [] p2 = points p2
play1 p1 [] = points p1
play1 (a:p1) (b:p2) | a > b     = play1 (p1 ++ [a,b]) p2
                    | otherwise = play1 p1 (p2 ++ [b,a])

play2 :: [Card] -> [Card] -> Int
play2 p1 p2 = points $ snd $ play2' p1 p2 []

play2' :: [Card] -> [Card] -> [([Card], [Card])] -> (Int, [Card])
play2' [] p2 _ = (2, p2)
play2' p1 [] _ = (1, p1)
play2' (a:p1) (b:p2) rounds
    | (a:p1, b:p2) `elem` rounds = (1, a:p1)
    | (length p1 >= a) && (length p2 >= b) = if 1 == fst subgame
                                             then play2' (p1 ++ [a,b]) p2 newRounds
                                             else play2' p1 (p2 ++ [b,a]) newRounds
    | a > b     = play2' (p1 ++ [a,b]) p2 newRounds
    | otherwise = play2' p1 (p2 ++ [b,a]) newRounds
  where subgame   = play2' (take a p1) (take b p2) []
        newRounds = rounds ++ [(a:p1, b:p2)]

points :: [Card] -> Int
points = sum . zipWith (*) [1..] . reverse

convert :: [String] -> ([Card], [Card])
convert = bimap c (c . tail) . break null
  where c = map read . tail

main = interact $ show . compute . convert . lines
  where compute = uncurry play1 &&& uncurry play2
