module Test.Day8 where

import Prelude
import Effect (Effect)
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Node.FS.Sync (readTextFile)
import Day8 (day8_1, day8_2)

day8Tests :: Effect Unit
day8Tests = do
    result1 <- day8_1 <$> readTextFile UTF8 "test/Test/test8-1.txt"
    assertEqual {actual: result1, expected: 14}

    result2 <- day8_2 <$> readTextFile UTF8 "test/Test/test8-1.txt"
    assertEqual {actual: result2, expected: 34}
