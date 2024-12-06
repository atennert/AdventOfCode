module Test.Day5 where

import Prelude
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Node.FS.Sync (readTextFile)
import Day5 (day5_1, day5_2)

day5Tests :: Effect Unit
day5Tests = do

    result1 <- day5_1 <$> readTextFile UTF8 "test/Test/test5-1.txt"
    assertEqual {actual: result1, expected: 143}

    result2 <- day5_2 <$> readTextFile UTF8 "test/Test/test5-1.txt"
    assertEqual {actual: result2, expected: 123}
