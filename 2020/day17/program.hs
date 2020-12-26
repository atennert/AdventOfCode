import Control.Arrow ((&&&))
import Data.List (nub)

run :: (Eq a) => (a -> [a]) -> Int -> [a] -> Int
run combs 0 active = length active
run combs cycles active = run combs (cycles-1) (remainActive ++ newActive)
  where newActive = map fst $ filter ((==3) . length . snd) $ map (\e -> (e, filter (`elem` active) (combs e))) possiblyNewActive
        possiblyNewActive = filter (not . flip elem active) $ nub $ concatMap combs active
        remainActive = foldr (\b r -> if (\c -> 2<=c && c<=3) $ length $ filter (`elem` active) (combs b) then b:r else r) [] active

combs1 (x,y,z)   = [(x',y',z')    | x' <- [(x-1)..(x+1)], y' <- [(y-1)..(y+1)], z' <- [(z-1)..(z+1)], (x',y',z') /= (x,y,z)]
combs2 (x,y,z,w) = [(x',y',z',w') | x' <- [(x-1)..(x+1)], y' <- [(y-1)..(y+1)], z' <- [(z-1)..(z+1)], w' <- [(w-1)..(w+1)], (x',y',z',w') /= (x,y,z,w)]

convert1 :: [String] -> [(Int, Int, Int)]
convert1 = c' 0
  where c' _ [] = []
        c' i (l:ls) = foldr (\(j,c) r -> if c=='#' then (j,i,0):r else r) [] (zip [0..] l) ++ c' (i+1) ls

convert2 :: [String] -> [(Int, Int, Int, Int)]
convert2 = c' 0
  where c' _ [] = []
        c' i (l:ls) = foldr (\(j,c) r -> if c=='#' then (j,i,0,0):r else r) [] (zip [0..] l) ++ c' (i+1) ls

main = interact $ show . compute . lines
  where compute = run combs1 6 . convert1 &&& run combs2 6 . convert2
