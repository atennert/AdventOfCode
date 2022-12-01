import Control.Arrow ((&&&))
import Data.List.Split (splitOn)
import Data.List (isPrefixOf)
import Data.Bifunctor (second, first)
import Data.Map (Map, empty, insert, elems)
import Data.Bits (setBit, clearBit, (.|.), (.&.))

run1 :: Map Int Int -> (Int, Int) -> [String] -> Int
run1 mem mask [] = sum $ elems mem
run1 mem mask (l:ls) | "mask" `isPrefixOf` l = run1 mem (convertMask1 $ drop 7 l) ls
                     | otherwise             = run1 write mask ls
  where write         = insert cell value mem
        (cell, value) = second ((.|. fst mask) . (.&. snd mask)) $ readMemCmd l

run2 :: Map Int Int -> (Int, [[Int]]) -> [String] -> Int
run2 mem mask [] = sum $ elems mem
run2 mem mask (l:ls) | "mask" `isPrefixOf` l = run2 mem (convertMask2 $ drop 7 l) ls
                     | otherwise             = run2 write mask ls
  where write             = foldr (`insert` value) mem cells
        (cells, value)    = first getAddresses $ readMemCmd l
        getAddresses base = (\adr -> map (foldr (flip setBit) adr) $ snd mask) $ foldr (flip clearBit) ((.|. base) $ fst mask) (head $ snd mask)

readMemCmd :: String -> (Int, Int)
readMemCmd = (\[m,v] -> (read m, read v)) . splitOn "] = " . drop 4

convertMask1 :: String -> (Int, Int)
convertMask1 ms = (orMask, andMask)
  where orMask  = foldr (\(i,v) r -> if v == '1' then setBit r i else r) 0 $ zip [0..] $ reverse ms
        andMask = foldr (\(i,v) r -> if v == '0' then r else setBit r i) 0 $ zip [0..] $ reverse ms

convertMask2 :: String -> (Int, [[Int]])
convertMask2 ms = (orMask, combinations xMask)
  where orMask = foldr (\(i,v) r -> if v == '1' then setBit r i else r) 0 $ zip [0..] $ reverse ms
        xMask  = foldr (\(i,v) l -> if v == 'X' then i:l else l) [] $ zip [0..] $ reverse ms
        combinations [] = [[]]
        combinations (l:ls) = map (l:) (combinations ls) ++ combinations ls

main = interact $ show . compute . lines
  where compute = run1 empty (0,0) &&& run2 empty (0,[])
