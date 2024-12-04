module Test.Day4 where

import Prelude
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Node.FS.Sync (readTextFile)
import Day4 (day4_1, day4_2)

day4Tests :: Effect Unit
day4Tests = do
    result1 <- day4_1 <$> readTextFile UTF8 "test/Test/test4-1.txt"
    assertEqual {actual: result1, expected: 18}

    result2 <- day4_2 <$> readTextFile UTF8 "test/Test/test4-1.txt"
    assertEqual {actual: result2, expected: 9}
