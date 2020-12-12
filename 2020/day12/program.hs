import Data.List (elemIndices)

run1 :: [(Char, Int)] -> (Int, Int)
run1 = run1' 'E' (0,0)
  where run1' _   pos   [] = pos
        run1' dir (x,y) ((nDir, val):ls)
            | nDir == 'N' = run1' dir (x, y+val) ls
            | nDir == 'S' = run1' dir (x, y-val) ls
            | nDir == 'E' = run1' dir (x+val, y) ls
            | nDir == 'W' = run1' dir (x-val, y) ls
            | nDir == 'L' = chDir "NWSE"
            | nDir == 'R' = chDir "NESW"
            | nDir == 'F' = run1' dir (x, y) ((dir,val):ls)
          where chDir l = (\d -> run1' d (x, y) ls) $ (l!!) $ (`mod` 4) $ (+ (val `div` 90)) $ head $ elemIndices dir l

run2 :: [(Char, Int)] -> (Int, Int)
run2 = run2' (0,0) (10, 1)
  where run2' pos   _     [] = pos
        run2' (x,y) (v,w) ((nDir, val):ls)
            | nDir == 'N' = run2' (x, y) (v, w+val) ls
            | nDir == 'S' = run2' (x, y) (v, w-val) ls
            | nDir == 'E' = run2' (x, y) (v+val, w) ls
            | nDir == 'W' = run2' (x, y) (v-val, w) ls
            | nDir == 'L' = chDir [(v,w), (-w,v), (-v,-w), (w,-v)]
            | nDir == 'R' = chDir [(v,w), (w,-v), (-v,-w), (-w,v)]
            | nDir == 'F' = run2' (x + val*v, y + val*w) (v, w) ls
          where chDir l = (\wp -> run2' (x, y) wp ls) $ (l!!) $ (val `div` 90) `mod` 4

manhatten :: (Int, Int) -> Int
manhatten (a,b) = abs a + abs b

convert :: [String] -> [(Char, Int)]
convert = map (\l -> (head l, read $ tail l))

compute :: [(Char, Int)] -> [String]
compute x = map (show . manhatten . ($ x)) [run1, run2]

main = interact $ unlines . compute . convert . lines
