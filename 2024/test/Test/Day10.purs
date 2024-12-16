module Test.Day10 where

import Prelude
import Day10 (day10_1, day10_2)
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Node.FS.Sync (readTextFile)
import Effect (Effect)

day10Tests :: Effect Unit
day10Tests = do
    result1 <- day10_1 <$> readTextFile UTF8 "test/Test/test10-1.txt"
    assertEqual {actual: result1, expected: 36}

    result2 <- day10_2 <$> readTextFile UTF8 "test/Test/test10-1.txt"
    assertEqual {actual: result2, expected: 81}
