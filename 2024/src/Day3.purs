module Day3 where

import Prelude
import Effect (Effect)
import Node.Encoding (Encoding(UTF8))
import Node.FS.Sync (readTextFile)
import Effect.Console (log)
import Data.String.Regex (Regex, match, parseFlags)
import Data.String.Regex.Flags (global)
import Data.Maybe (Maybe(Just, Nothing))
import Utils (strToInt)
import Data.Foldable (sum)
import Data.String.Regex.Unsafe (unsafeRegex)
import Data.Array.NonEmpty.Internal (NonEmptyArray)
import Data.Array.NonEmpty ((!!))
import Data.List.Types (List(Nil), (:))
import Data.List (fromFoldable)

data Compute a =
    Zero
  | Mul a a
  | Do
  | Dont

day3 ∷ Effect Unit
day3 = do
    input <- readTextFile UTF8 "day3.txt"
    log "Day 3:"
    log <<< show <<< day3_1 $ input
    log <<< show <<< day3_2 $ input

day3_1 :: String -> Int
day3_1 input = sum <<< map compute <<< parse $ input

day3_2 :: String -> Int
day3_2 input = sum <<< map compute <<< filterDo <<< parse $ input

regInstructions :: Regex
regInstructions = unsafeRegex "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)" (parseFlags "gm")

regMultiply :: Regex
regMultiply = unsafeRegex "\\d+" global

parse :: String -> List (Compute Int)
parse input =
    let matches = match regInstructions input
        parseMatch (Just m) = case m of
          "do()" -> Do
          "don't()" -> Dont
          _ -> case match regMultiply m of
              Just a -> Mul (strToInt $ at a 0) (strToInt $ at a 1)
              _ -> Zero
        parseMatch Nothing = Zero
    in case matches of
      Just a -> map parseMatch $ fromFoldable a
      Nothing -> Nil

compute :: Compute Int -> Int
compute (Mul a b) = a * b
compute _ = 0

at :: NonEmptyArray (Maybe String) -> Int -> String
at a i = case a !! i of
    Just (Just s) -> s
    _ -> ""

filterDo :: forall a. List (Compute a) -> List (Compute a)
filterDo (x:xs) = case x of
    Dont -> filterDont xs
    Do -> filterDo xs
    _ -> x : (filterDo xs)
filterDo _ = Nil

filterDont :: forall a. List (Compute a) -> List (Compute a)
filterDont (x:xs) = case x of
    Dont -> filterDont xs
    Do -> filterDo xs
    _ -> filterDont xs
filterDont _ = Nil
