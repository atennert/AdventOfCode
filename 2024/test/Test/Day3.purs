module Test.Day3 where

import Prelude
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Node.FS.Sync (readTextFile)
import Day3 (day3_1, day3_2)

day3Tests :: Effect Unit
day3Tests = do
    result1 <- day3_1 <$> readTextFile UTF8 "test/Test/test3-1.txt"
    assertEqual {actual: result1, expected: 161}

    result2 <- day3_2 <$> readTextFile UTF8 "test/Test/test3-2.txt"
    assertEqual {actual: result2, expected: 48}
