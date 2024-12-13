module Utils where

import Prelude
import Data.Maybe (Maybe(Just, Nothing))
import Data.Int (fromString)
import Data.Tuple (Tuple(Tuple))
import Data.List.Types (List(Nil), (:))
import Data.List (concat, fromFoldable, length, updateAt, (!!), (..))
import Data.String (split) as S
import Data.String.Pattern (Pattern(..))
import JS.BigInt (BigInt, fromString, fromInt) as B


strToInt :: String -> Int
strToInt n = case fromString n of
    Just x -> x
    Nothing -> 0

strToBigInt :: String -> B.BigInt
strToBigInt n = case B.fromString n of
    Just x -> x
    Nothing -> B.fromInt 0

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

split :: String -> String -> List String
split sep = fromFoldable <<< S.split (Pattern sep)

splitA :: String -> String -> Array String
splitA sep = S.split (Pattern sep)

mark :: forall a. List (List a) -> Tuple Int Int -> a -> List (List a)
mark l (Tuple x y) e = case l !! y of
    Just ly -> case updateAt x e ly of
        Just newLy -> case updateAt y newLy l of
            Just newL -> newL
            Nothing -> l
        Nothing -> l
    Nothing -> l

sizeRect :: forall a. List (List a) -> Tuple Int Int
sizeRect Nil = Tuple 0 0
sizeRect ll@(l:_) = Tuple (length l) (length ll)

isInRect :: forall a. List (List a) -> Tuple Int Int -> Boolean
isInRect rect (Tuple x y) =
    let (Tuple lenX lenY) = sizeRect rect
    in x >= 0 && x < lenX && y >= 0 && y < lenY