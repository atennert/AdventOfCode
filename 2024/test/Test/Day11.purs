module Test.Day11 where

import Prelude
import Effect (Effect)
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Day11 (day11_1)
import Node.FS.Sync (readTextFile)
import JS.BigInt (fromInt)


day11Tests :: Effect Unit
day11Tests = do
    result1 <- day11_1 <$> readTextFile UTF8 "test/Test/test11-1.txt"
    assertEqual {actual: result1, expected: fromInt 55312}