module Test.Day6 where

import Prelude
import Effect (Effect)
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Node.FS.Sync (readTextFile)
import Day6 (day6_1, day6_2)

day6Tests :: Effect Unit
day6Tests = do
    result1 <- day6_1 <$> readTextFile UTF8 "test/Test/test6-1.txt"
    assertEqual {actual: result1, expected: 41}

    result2 <- day6_2 <$> readTextFile UTF8 "test/Test/test6-1.txt"
    assertEqual {actual: result2, expected: 6}
