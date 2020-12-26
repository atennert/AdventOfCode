import Control.Arrow ((&&&))
import Data.List.Split (splitOn)
import Data.List (isSuffixOf)
import Data.Char (isDigit, isHexDigit)
import Data.Maybe (fromJust, isJust)

convert :: [String] -> [String]
convert = foldr merge [""]
  where merge e (l:ls) | null e    = "":l:ls
                       | otherwise = (e ++ " " ++ l) : ls

fields = [
  ("byr", checkNumber (1920, 2002)),
  ("iyr", checkNumber (2010, 2020)),
  ("eyr", checkNumber (2020, 2030)),
  ("hgt", checkHeight),
  ("hcl", \s -> length s == 7 && all isHexDigit (tail s) && head s == '#'),
  ("ecl", (`elem` ["amb","blu","brn","gry","grn","hzl","oth"])),
  ("pid", \s -> length s == 9 && all isDigit s)]

checkNumber :: (Int, Int) -> String -> Bool
checkNumber (mi, ma) s = nums s && mi <= num s && num s <= ma
  where nums = all isDigit
        num  = read

checkHeight :: String -> Bool
checkHeight s = check "cm" (150,193) || check "in" (59,76)
  where check unit vals = isSuffixOf unit s && checkNumber vals (numberPart s)
        numberPart      = take (length s - 2)

getValidPassports1 :: [String] -> Int
getValidPassports1 [] = 0
getValidPassports1 (l:ls) = getValidPassports1 ls + fromEnum hasAllFields
  where hasAllFields = all ((`elem` keys) . fst) fields
        ppFields     = splitOn " " l
        keys         = map (take 3) ppFields

getValidPassports2 :: [String] -> Int
getValidPassports2 [] = 0
getValidPassports2 (l:ls) = getValidPassports2 ls + fromEnum isFine
  where isFine = all (\(k,c) -> isJust (ppf k) && c (fromJust (ppf k))) fields
        ppfs   = map ((\(k,v) -> (k, tail v)) . splitAt 3) $ splitOn " " l
        ppf k  = lookup k ppfs

main = interact $ show . compute . convert . lines
  where compute = getValidPassports1 &&& getValidPassports2
