import Control.Arrow ((&&&))
import Data.List.Split (splitOn, splitOneOf)
import Data.List (elemIndices)

convertEntry :: String -> (Int, Int, Char, String)
convertEntry entry =
  let [rule,pw]          = splitOn ": " entry
      [int1,int2,letter] = splitOneOf "- " rule
  in (read int1, read int2, head letter, pw)

countValid :: ((Int, Int, Char, String) -> Bool) -> [(Int, Int, Char, String)] -> Int
countValid f = length . filter id . map f

isPasswordValid1 :: (Int, Int, Char, String) -> Bool
isPasswordValid1 (minCount, maxCount, letter, pw) =
    minCount <= occurenceCount && occurenceCount <= maxCount
  where occurenceCount = length $ elemIndices letter pw

isPasswordValid2 :: (Int, Int, Char, String) -> Bool
isPasswordValid2 (pos1, pos2, letter, pw) =
    ((pos1 - 1) `elem` occurences) `xor` ((pos2 - 1) `elem` occurences)
  where occurences = elemIndices letter pw

xor :: Bool -> Bool -> Bool
xor True x  = not x
xor False x = x

main = interact $ show . compute . map convertEntry . lines
  where compute = countValid isPasswordValid1 &&& countValid isPasswordValid2
