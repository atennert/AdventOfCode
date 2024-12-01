module Test.Main where

import Prelude

import Effect (Effect)
import Test.Day1 (day1Tests)

main :: Effect Unit
main = do
  day1Tests

