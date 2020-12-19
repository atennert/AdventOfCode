import Data.IntMap (IntMap, fromList, (!))
import Data.List.Split (splitOn)
import Data.List (isPrefixOf)
import Data.Maybe (isJust, fromJust)

data Tree a = Leaf | Node {
  content :: a,
  next    :: [Tree a]
} deriving Show

run1 :: ([Tree String], [String]) -> Int
run1 (rules, msgs) = length $ filter check msgs
  where check msg = any (check' msg) rules
        check' msg    Leaf       = null msg
        check' (m:ms) (Node c n) = [m] == c && any (check' ms) n

convert :: [String] -> ([Tree String], [String])
convert = readRules []
  where readRules rules ("":ms) = (bt (fromList rules) $ initialRules (fromList rules), ms)
        readRules rules (m:ms)  = readRules (rule m : rules) ms
        rule = (\[k,v] -> (read k,v)) . splitOn ": "
        initialRules r = map read $ splitOn " " (r!0)

bt :: IntMap String -> [Int] -> [Tree String]
bt _     [] = [Leaf]
bt rules (r:rs)
    | '"' `elem` (rules!r) = [Node {content=[(rules!r)!!1], next=bt rules rs}]
    | '|' `elem` (rules!r) = rPath 0 ++ rPath 1
    | otherwise = bt rules (newRules (rules!r))
  where r' = splitOn " | " (rules!r)
        newRules ar = (map read . splitOn " ") ar ++ rs
        rPath i = bt rules (newRules (r'!!i))


compute :: [String] -> [String]
compute x = map (show . ($ convert x)) [run1]

main = interact $ unlines . compute . lines