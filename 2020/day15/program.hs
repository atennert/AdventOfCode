import Data.IntMap (IntMap, fromList, insert, size, member, (!))
import Data.List.Split (splitOn)

run :: Int -> (Int, IntMap [Int]) -> Int
run max (l, m) = run' (size m) l m
  where run' i l m
            | i >= max         = l
            | head (m!l) == -1 = run' (i+1) 0 (replace 0)
            | otherwise        = run' (i+1) mSub (replace mSub)
          where replace k | member k m = insert k [last (m!k), i] m
                          | otherwise  = insert k [-1,i] m
                mSub = (\[a,b] -> b-a) $ m!l

convert :: [String] -> (Int, IntMap [Int])
convert (s:_) = (\l -> (fst $ head l, fromList l)) $ reverse $ zipWith (\i x -> (read x,[-1,i])) [0..] (splitOn "," s)

compute :: [String] -> [String]
compute x = map (show . ($ convert x)) [run 2020, run 30000000]

main = interact $ unlines . compute . lines