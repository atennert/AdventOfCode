module Utils where

import Prelude
import Data.Maybe (Maybe(Just, Nothing))
import Data.Int (fromString)
import Data.Tuple (Tuple(Tuple))
import Data.List.Types (List(Nil))
import Data.List (concat, length, (!!), (..))


strToInt :: String -> Int
strToInt n = case fromString n of
    Just x -> x
    Nothing -> 0

at :: forall a. List (List a) -> (Tuple Int Int) -> Maybe a
at m (Tuple x y) =
    case m !! y of
      Just l -> l !! x
      Nothing -> Nothing

coords :: forall a. List (List a) -> List (Tuple Int Int)
coords m =
    let ys = 0 .. ((length m) - 1)
        xs y = case m !! y of
                Just l -> 0 .. ((length l) - 1)
                Nothing -> Nil
    in concat <<< map (\y -> map (\x -> Tuple x y) $ xs y) $ ys
