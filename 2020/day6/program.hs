import Data.List (nub,intersect)

normalize :: (String -> [t] -> [t]) -> [String] -> [[t]]
normalize combine = foldr merge [[]]
  where merge e (l:ls) | null e    = []:l:ls
                       | otherwise = combine e l : ls

getAnyonesYes :: [String] -> Int
getAnyonesYes = sum . map (length . nub) . normalize (++)

getEveryonesYes :: [String] -> Int
getEveryonesYes = sum . map (length . foldr1 intersect) . normalize (:)

compute :: [String] -> [String]
compute x = map (show . ($ x)) [getAnyonesYes, getEveryonesYes]

main = interact $ unlines . compute . lines
