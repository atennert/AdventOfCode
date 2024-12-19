module Test.Day12 where

import Prelude
import Effect (Effect)
import Day12 (day12_1, day12_2)
import Test.Assert (assertEqual)
import Node.Encoding (Encoding(UTF8))
import Node.FS.Sync (readTextFile)

day12Tests :: Effect Unit
day12Tests = do
    result1 <- day12_1 <$> readTextFile UTF8 "test/Test/test12-1.txt"
    assertEqual {actual: result1, expected: 140}

    result2 <- day12_1 <$> readTextFile UTF8 "test/Test/test12-2.txt"
    assertEqual {actual: result2, expected: 772}

    result3 <- day12_1 <$> readTextFile UTF8 "test/Test/test12-3.txt"
    assertEqual {actual: result3, expected: 1930}

    result4 <- day12_2 <$> readTextFile UTF8 "test/Test/test12-1.txt"
    assertEqual {actual: result4, expected: 80}

    result5 <- day12_2 <$> readTextFile UTF8 "test/Test/test12-2.txt"
    assertEqual {actual: result5, expected: 436}

    result6 <- day12_2 <$> readTextFile UTF8 "test/Test/test12-3.txt"
    assertEqual {actual: result6, expected: 1206}
