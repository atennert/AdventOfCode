
transform val sn = (val * sn) `rem` 20201227

loopSize :: [Int] -> [(Int, Int)]
loopSize = map (\k -> (k, transform' k 1))
  where transform' key val | key == val = 0
                           | otherwise  = 1 + transform' key (transform val 7)

encKey :: [(Int, Int)] -> Int
encKey [(_,ls),(pk,_)] = transform' ls 1
  where transform' 0 val = val
        transform' c val = transform' (c-1) (transform val pk)

convert :: [String] -> [Int]
convert = map read

compute :: [String] -> [String]
compute x = Prelude.map (show . ($ convert x)) [encKey . loopSize]

main = interact $ unlines . compute . lines