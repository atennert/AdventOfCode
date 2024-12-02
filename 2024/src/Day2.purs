module Day2 where

import Prelude
import Effect (Effect)
import Data.Unit (Unit)
import Node.FS.Sync (readTextFile)
import Data.Show (show)
import Node.Encoding (Encoding(UTF8))
import Effect.Console (log)
import Data.List.Types (List(Nil), (:))
import Data.List (concat, deleteAt, foldl, fromFoldable, length, (..))
import Data.String (split)
import Data.String.Pattern (Pattern(Pattern))
import Data.Ord (abs)
import Data.Foldable (find, sum)
import Data.Maybe (Maybe(Just, Nothing))
import Utils (strToInt)

day2 ∷ Effect Unit
day2 = do
    input <- readTextFile UTF8 "day2.txt"
    log "Day 2:"
    log <<< show <<< day2_1 $ input
    log <<< show <<< day2_2 $ input

day2_1 :: String -> Int
day2_1 input = sum <<< map checkSafe <<< parse $ input

day2_2 :: String -> Int
day2_2 input = sum <<< map checkSafe2 <<< parse $ input

parse :: String -> List (List Int)
parse input =
    let parseLine line = foldl (\l e -> ((strToInt e): l)) Nil <<< split (Pattern " ") $ line
    in fromFoldable <<< map parseLine $ split (Pattern "\n") input

data Direction = Up | Down

checkSafe :: List Int -> Int
checkSafe l@(a:b:_) =
    let dir = if a < b then Up else Down
    in checkSafe' dir l
checkSafe _ = 1

checkSafe' :: Direction -> List Int -> Int
checkSafe' dir (a:b:xs) =
    let checkDir = case dir of
                                Up -> a < b
                                Down -> a > b
        dist = abs (a - b)
        checkDist = dist > 0 && dist < 4
    in if checkDir && checkDist
        then checkSafe' dir (b:xs)
        else 0
checkSafe' _ _ = 1

checkSafe2 :: List Int -> Int
checkSafe2 xs =
    let filterIndex i = case deleteAt i xs of
                                  Just l -> l
                                  Nothing -> xs
        options = map filterIndex (0 .. length xs)
    in case find ((<) 0) $ concat ((map (checkSafe' Up) options) : (map (checkSafe' Down) options) : Nil) of
        Just x -> x
        Nothing -> 0
