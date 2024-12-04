module Day4 where

import Prelude
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Effect.Console (log)
import Node.FS.Sync (readTextFile)
import Data.List.Types (List(..), (:))
import Data.String.Pattern (Pattern(..))
import Data.String (split) as S
import Data.List (fromFoldable)
import Data.Tuple (Tuple(Tuple))
import Data.Maybe (Maybe(Just))
import Data.String.CodeUnits (toCharArray)
import Data.Foldable (sum)
import Utils (at, coords)

day4 ∷ Effect Unit
day4 = do
    input <- readTextFile UTF8 "day4.txt"
    log "Day 4:"
    log <<< show <<< day4_1 $ input
    log <<< show <<< day4_2 $ input

xmas :: List Char
xmas = fromFoldable <<< toCharArray $ "XMAS"

searchPattern1 :: List (Tuple Int Int)
searchPattern1 =
    (Tuple   1    0)
  : (Tuple   1    1)
  : (Tuple   0    1)
  : (Tuple (-1)   1)
  : (Tuple (-1)   0)
  : (Tuple (-1) (-1))
  : (Tuple   0  (-1))
  : (Tuple   1  (-1))
  : Nil

searchPattern2 :: List (Tuple (Tuple Int Int) (Tuple Int Int))
searchPattern2 =
    Tuple (Tuple   1    1)  (Tuple (-1)   1)
  : Tuple (Tuple (-1)   1)  (Tuple (-1) (-1))
  : Tuple (Tuple (-1) (-1)) (Tuple   1  (-1))
  : Tuple (Tuple   1  (-1)) (Tuple   1    1)
  : Nil

day4_1 :: String -> Int
day4_1 input = count <<< parse $ input

day4_2 :: String -> Int
day4_2 input = count2 <<< parse $ input

parse :: String -> List (List Char)
parse input = map (fromFoldable <<< toCharArray) <<< fromFoldable <<< S.split (Pattern "\n") $ input

count :: List (List Char) -> Int
count m = sum <<< map (findX m) <<< coords $ m

findX :: List (List Char) -> Tuple Int Int -> Int
findX m p =
    case at m p of
      Just 'X' -> sum <<< map (findXmas m xmas p) $ searchPattern1
      _ -> 0

findXmas :: List (List Char) -> List Char -> Tuple Int Int -> Tuple Int Int -> Int
findXmas _ Nil _ _ = 1
findXmas m (x:mas) p@(Tuple px py) d@(Tuple dx dy) =
    case at m p of
      Just c -> if c == x
                then findXmas m mas (Tuple (px + dx) (py + dy)) d
                else 0
      _ -> 0

count2 :: List (List Char) -> Int
count2 m = sum <<< map (findA m) <<< coords $ m

findA :: List (List Char) -> Tuple Int Int -> Int
findA m p =
    case at m p of
      Just 'A' -> sum <<< map (findMas m p) $ searchPattern2
      _ -> 0

findMas :: List (List Char) -> Tuple Int Int -> Tuple (Tuple Int Int) (Tuple Int Int) -> Int
findMas m (Tuple px py) (Tuple (Tuple d1x d1y) (Tuple d2x d2y)) =
    let check c p = case at m p of
                    Just c' -> c == c'
                    _ -> false
        s1 = check 'S' (Tuple (px - d1x) (py - d1y))
        s2 = check 'S' (Tuple (px - d2x) (py - d2y))
        m1 = check 'M' (Tuple (px + d1x) (py + d1y))
        m2 = check 'M' (Tuple (px + d2x) (py + d2y))
    in if s1 && s2 && m1 && m2 then 1 else 0
