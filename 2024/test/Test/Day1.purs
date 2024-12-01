module Test.Day1 where

import Prelude
import Test.Assert (assertEqual)

import Day1 (day1_1, day1_2)
import Effect (Effect)
import Node.FS.Sync (readTextFile)
import Node.Encoding (Encoding(UTF8))

day1Tests :: Effect Unit
day1Tests = do
    result1 <- day1_1 <$> readTextFile UTF8 "test/Test/test1-1.txt"
    assertEqual {actual: result1, expected: 11}

    result2 <- day1_2 <$> readTextFile UTF8 "test/Test/test1-1.txt"
    assertEqual {actual: result2, expected: 31}
