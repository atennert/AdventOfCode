module Test.Main where

import Prelude

import Effect (Effect)
import Test.Day1 (day1Tests)
import Test.Day2 (day2Tests)
import Test.Day3 (day3Tests)
import Test.Day4 (day4Tests)

main :: Effect Unit
main = do
  day1Tests
  day2Tests
  day3Tests
  day4Tests
