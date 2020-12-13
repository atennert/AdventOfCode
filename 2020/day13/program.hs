import Data.List.Split (splitOn)
import Data.Bifunctor (second)

idAndTime :: (Int, [Int]) -> Int
idAndTime (t, ids) = uncurry (*) $ def ids
  where def = foldr1 (\(i1,t1) (i2,t2) -> if t1<t2 then (i1,t1) else (i2,t2)) . map (\id -> (id, id - (t `mod` id)))

convert1 :: [String] -> (Int, [Int])
convert1 [t,ids] = (read t, map read $ filter (/= "x") $ splitOn "," ids)

timestamp :: [(Int, Int)] -> Int
timestamp = findTs 1 0
  where findTs _ t [] = t
        findTs p t ((dt,id):tss) = if (t+dt) `mod` id == 0 then findTs (p*id) t tss else findTs p (t+p) ((dt,id):tss)

convert2 :: [String] -> [(Int, Int)]
convert2 [_,ids] = map (second read) $ filter ((/="x") . snd) $ zip [0..] $ splitOn "," ids

compute :: [String] -> [String]
compute x = map (show . ($ x)) [idAndTime . convert1, timestamp . convert2]

main = interact $ unlines . compute . lines