
index = [0, 0, 0, 0, 0]
slopes = [(1,1), (3,1), (5,1), (7,1), (1,2)]

countTrees1 :: [String] -> Int
countTrees1 lx =
  let len = length . head $ lx
  in countTrees1' len 0 lx

countTrees1' :: Int -> Int -> [String] -> Int
countTrees1' _ _ []          = 0
countTrees1' len posX (l:ls) =
  let isTree = l!!(posX `mod` len) == '#'
      value = fromEnum isTree
  in countTrees1' len (posX+3) ls + value

countTrees2 :: [String] -> Int
countTrees2 lx =
  let len = length . head $ lx
  in product (countTrees2' len (index,0) lx)

countTrees2' :: Int -> ([Int], Int) -> [String] -> [Int]
countTrees2' _ _ [] = [0, 0, 0, 0, 0]
countTrees2' len (xs,y) (l:ls) =
  let use = map (\(_,sy) -> y `mod` sy == 0) slopes
      isTree = zipWith (\x u -> u && l!!(x `mod` len) == '#') xs use
      values = map fromEnum isTree
      addL = zipWith (+)
      newXs = zipWith3 (\x s u -> if u then x+s else x) xs (map fst slopes) use
  in countTrees2' len (newXs, y+1) ls `addL` values

compute :: [String] -> [String]
compute x = map (`count` x) [countTrees1, countTrees2]
  where count f = show . f

main = interact $ unlines . compute . lines
