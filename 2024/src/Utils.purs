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
import Data.Array (length, uncons, updateAt, (!!)) as A


data Triple a b c = Triple a b c
instance (Show a, Show b, Show c) => Show (Triple a b c) where
    show (Triple a b c) = "Triple (" <> show a <> ", " <> show b <> ", " <> show c <> ")"
derive instance (Eq a, Eq b, Eq c) =>  Eq (Triple a b c)

ext :: forall a b. (a -> b) -> a -> Tuple a b
ext f a = Tuple a (f a)

ext2 :: forall a b c. (Tuple a b -> c) -> Tuple a b -> Triple a b c
ext2 f t@(Tuple a b) = Triple a b (f t)

strToInt :: String -> Int
strToInt n = case fromString n of
    Just x -> x
    Nothing -> 0

strToBigInt :: String -> B.BigInt
strToBigInt n = case B.fromString n of
    Just x -> x
    Nothing -> B.fromInt 0

at :: forall a. List (List a) -> Tuple Int Int -> Maybe a
at m (Tuple x y) =
    case m !! y of
      Just l -> l !! x
      Nothing -> Nothing

atA :: forall a. Array (Array a) -> Tuple Int Int -> Maybe a
atA m (Tuple x y) =
    case m A.!! y of
      Just l -> l A.!! x
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

markA :: forall a. Array (Array a) -> Tuple Int Int -> a -> Array (Array a)
markA l (Tuple x y) e = case l A.!! y of
    Just ly -> case A.updateAt x e ly of
        Just newLy -> case A.updateAt y newLy l of
            Just newL -> newL
            Nothing -> l
        Nothing -> l
    Nothing -> l

sizeRect :: forall a. List (List a) -> Tuple Int Int
sizeRect Nil = Tuple 0 0
sizeRect ll@(l:_) = Tuple (length l) (length ll)

sizeRectA :: forall a. Array (Array a) -> Tuple Int Int
sizeRectA arr = case A.uncons arr of
    Just { head } -> Tuple (A.length head) (A.length arr)
    Nothing -> Tuple 0 0

isInRect :: forall a. List (List a) -> Tuple Int Int -> Boolean
isInRect rect (Tuple x y) =
    let Tuple lenX lenY = sizeRect rect
    in x >= 0 && x < lenX && y >= 0 && y < lenY
