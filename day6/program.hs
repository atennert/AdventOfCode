import Data.List (nub,intersect)

combineGroupEntries :: [String] -> [String]
combineGroupEntries = foldr merge [""]
  where merge e (l:ls) | null e    = "":l:ls
                       | otherwise = (e ++ l) : ls

separateGroups :: [String] -> [[String]]
separateGroups = foldr merge [[]]
  where merge e (l:ls) | null e    = []:l:ls
                       | otherwise = (e:l) : ls

getAnyonesYes :: [String] -> Int
getAnyonesYes = sum . map (length . nub) . combineGroupEntries

getEveryonesYes :: [String] -> Int
getEveryonesYes = sum . map (length . foldr1 intersect) . separateGroups

compute :: [String] -> [String]
compute x = map (`run` x) [getAnyonesYes, getEveryonesYes]
  where run f = show . f

main = interact $ unlines . compute . lines
