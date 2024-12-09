module Test.Day7 where

import Prelude
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Node.FS.Sync (readTextFile)
import Day7 (day7_1, day7_2)
import JS.BigInt (fromInt)

day7Tests :: Effect Unit
day7Tests = do
    result1 <- day7_1 <$> readTextFile UTF8 "test/Test/test7-1.txt"
    assertEqual {actual: result1, expected: fromInt 3749}

    result2 <- day7_2 <$> readTextFile UTF8 "test/Test/test7-1.txt"
    assertEqual {actual: result2, expected: fromInt 11387}
