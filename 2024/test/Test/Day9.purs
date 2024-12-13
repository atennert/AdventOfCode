module Test.Day9 where

import Prelude
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Node.FS.Sync (readTextFile)
import Day9 (Block(Data, Space), Triple(Triple), day9_1, day9_2, runBlock1)
import JS.BigInt (fromInt)
import Data.Maybe (Maybe(Just))

day9Tests :: Effect Unit
day9Tests = do
    assertEqual {
      actual: runBlock1 (fromInt 1) (Data 1 1) [Space 0],
      expected: Just $ Triple (fromInt 1) (Data 1 0) [Space 0]
    }

    assertEqual {
      actual: runBlock1 (fromInt 0) (Space 1) [Data 1 1],
      expected: Just $ Triple (fromInt 0) (Space 0) [Data 1 0]
    }

    result1 <- day9_1 <$> readTextFile UTF8 "test/Test/test9-1.txt"
    assertEqual {actual: result1, expected: fromInt 1928}

    result2 <- day9_2 <$> readTextFile UTF8 "test/Test/test9-1.txt"
    assertEqual {actual: result2, expected: fromInt 2858}
