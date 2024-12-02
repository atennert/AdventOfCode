module Utils where

import Prelude
import Data.Maybe (Maybe(Just, Nothing))
import Data.Int (fromString)


strToInt :: String -> Int
strToInt n = case fromString n of
    Just x -> x
    Nothing -> 0
