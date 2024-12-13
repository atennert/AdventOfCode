module Test.Main where

import Prelude

import Effect (Effect)
import Test.Day1 (day1Tests)
import Test.Day2 (day2Tests)
import Test.Day3 (day3Tests)
import Test.Day4 (day4Tests)
import Test.Day5 (day5Tests)
import Test.Day6 (day6Tests)
import Test.Day7 (day7Tests)
import Test.Day8 (day8Tests)
import Test.Day9 (day9Tests)

main :: Effect Unit
main = do
  day1Tests
  day2Tests
  day3Tests
  day4Tests
  day5Tests
  day6Tests
  day7Tests
  day8Tests
  day9Tests
