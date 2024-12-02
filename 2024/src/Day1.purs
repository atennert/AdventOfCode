module Day1 where

import Prelude
import Data.String.Pattern (Pattern(Pattern))
import Data.Functor (map)
import Data.Foldable (foldl)
import Data.Tuple (Tuple(Tuple))
import Data.List.Types (List(Nil), (:))
import Data.List (fromFoldable, sort)
import Data.Ord (class Ord, abs)
import Data.Maybe (Maybe(Just, Nothing))
import Data.String (split)
import Data.Semiring ((*), (+))
import Data.Ring ((-))
import Node.FS.Sync (readTextFile)
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Data.Function (($))
import Data.Eq (class Eq)
import JS.Map.Primitive (Map, alter, lookup)
import JS.Map.Primitive.ST as STM
import Data.Unit (Unit)
import Effect.Console (log)
import Utils (strToInt)


day1 ∷ Effect Unit
day1 = do
    input <- readTextFile UTF8 "day1.txt"
    log "Day 1:"
    log $ show $ day1_1 input
    log $ show $ day1_2 input

day1_1 :: String -> Int
day1_1 input = getDistance $ sortLists $ getPreparedLists input

day1_2 :: String -> Int
day1_2 input = similarity $ convert2 $ getPreparedLists input

getPreparedLists :: String -> Tuple (List Int) (List Int)
getPreparedLists input = prepareLists (map (split (Pattern "   ")) (split (Pattern "\n") input))

prepareLists :: Array (Array String) -> Tuple (List Int) (List Int)
prepareLists input = foldl combine (Tuple Nil Nil) (map fromFoldable input)

combine :: Tuple (List Int) (List Int) -> List String -> Tuple (List Int) (List Int)
combine (Tuple l1 l2) (a1:a2:_) = Tuple ((strToInt a1) : l1) ((strToInt a2) : l2)
combine t _ = t

sortLists :: forall a. Ord a => Tuple (List a) (List a) -> (Tuple (List a) (List a))
sortLists (Tuple xs ys) = Tuple (sort xs) (sort ys)

getDistance :: Tuple (List Int) (List Int) -> Int
getDistance (Tuple (x:xs) (y:ys)) = (abs (x - y)) + getDistance (Tuple xs ys)
getDistance _ = 0

count :: List Int -> Map Int Int
count Nil = STM.run do STM.new
count (x:xs) =
    let m = count xs
    in alter (\mv -> case mv of
        Just v -> Just (v + 1)
        Nothing -> Just 1) x m

convert2 :: Tuple (List Int) (List Int) -> Tuple (List Int) (Map Int Int)
convert2 (Tuple l1 l2) = Tuple l1 (count l2)

similarity :: Tuple (List Int) (Map Int Int) -> Int
similarity (Tuple Nil _) = 0
similarity (Tuple (x:xs) m) =
    let s = similarity (Tuple xs m)
    in case lookup x m of
        Just y -> (y * x) + s
        Nothing -> s
