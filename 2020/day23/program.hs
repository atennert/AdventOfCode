type Cup = Int

run' :: Int -> Int -> [Cup] -> [Cup]
run' _ 0 cups = takeWhile (/=1) $ tail $ dropWhile (/=1) $ cycle cups
run' m n (cur:a:b:c:cups) = run' m (n-1) newCups
  where dest cur' | cur' > 1  = if (cur'-1) `notElem` [a,b,c] then cur'-1 else dest (cur'-1)
                  | otherwise = dest (1 + m)
        newCups = (\(c,cs) -> cs++c) $ splitAt 1 $ front ++ [dest cur, a, b, c] ++ tail back
        (front,back) = span (/=dest cur) (cur:cups)

run1 :: Int -> [Cup] -> [Cup]
run1 n cups = run' (length cups) n cups

run2 :: Int -> [Cup] -> [Cup]
run2 n cups = take 2 $ run' (length cups) n cups

convert :: [String] -> [Cup]
convert (l:_) = map (\c -> read [c]) l

attachMil :: [Cup] -> [Cup]
attachMil cups = cups ++ [c | c <- [(1 + maximum cups)..1000000]]

compute :: [String] -> [String]
compute x = map (show . ($ convert x)) [run1 100, run2 10000000 . attachMil]

main = interact $ unlines . compute . lines