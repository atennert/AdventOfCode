import Data.List (isPrefixOf, findIndices)
import Data.List.Split (splitOn)

runCmd :: String -> (Int, Int, Int, [Int]) -> (Int, Int, Int, [Int])
runCmd cmd (a, i, p, is)
    | fixedKey == "acc" = (a + fixedVal, i + 1, p, i:is)
    | fixedKey == "jmp" = (a, i + fixedVal, p, i:is)
    | fixedKey == "nop" = (a, i + 1, p, i:is)
  where [key, val] = splitOn " " cmd
        fixedVal = read $ filter (/= '+') val
        fixedKey | i == p && key == "jmp" = "nop"
                 | i == p && key == "nop" = "jmp"
                 | otherwise              = key

run :: (Int, Int, Int, [Int]) -> [String] -> (Int, Bool)
run state cmds = run' state
  where run' (a, i, p, is)
          | i `elem` is     = (a, True)
          | i < length cmds = run' $ runCmd (cmds!!i) (a, i, p, is)
          | otherwise       = (a, False)

runFixing :: [String] -> (Int, Bool)
runFixing cmds = run' $ nextP (-1)
  where run' p | snd $ run (0, 0, p, []) cmds = run' $ nextP p
               | otherwise                    = run (0, 0, p, []) cmds
        nextP p = head $ filter (> p) jmpNops
        jmpNops = findIndices (\e -> isPrefixOf "jmp" e || isPrefixOf "nop" e) cmds

compute :: [String] -> [String]
compute x = map (show . ($ x)) [run (0, 0, -1, []), runFixing]

main = interact $ unlines . compute . lines