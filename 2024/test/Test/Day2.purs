module Test.Day2 where

import Prelude
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Day2 (day2_1, day2_2)
import Effect (Effect)
import Node.FS.Sync (readTextFile)

day2Tests :: Effect Unit
day2Tests = do
    result1 <- day2_1 <$> readTextFile UTF8 "test/Test/test2-1.txt"
    assertEqual {actual: result1, expected: 2}

    result2 <- day2_2 <$> readTextFile UTF8 "test/Test/test2-1.txt"
    assertEqual {actual: result2, expected: 4}
