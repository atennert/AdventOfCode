module Test.Day6 where

import Prelude
import Effect (Effect)
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Node.FS.Sync (readTextFile)
import Day6 (block, day6_1, day6_2, findPos, parse, positions, run, runFloor)
import Data.Tuple (Tuple(Tuple))
import Data.List.Types (List(..))
import Data.Maybe (Maybe(..))

day6Tests :: Effect Unit
day6Tests = do
    result1 <- day6_1 <$> readTextFile UTF8 "test/Test/test6-1.txt"
    assertEqual {actual: result1, expected: 41}

    floor <- parse <$> readTextFile UTF8 "test/Test/test6-1.txt"
    let Tuple initWalk _ = findPos $ floor
--    let initRun = run $ Tuple initWalk floor
--    let poss = positions initWalk initRun
--    assertEqual {
--      actual: runFloor initWalk <<< block floor $ (Tuple 1 8),
--      expected: Nothing
--    }

    result2 <- day6_2 <$> readTextFile UTF8 "test/Test/test6-1.txt"
    assertEqual {actual: result2, expected: 6}
