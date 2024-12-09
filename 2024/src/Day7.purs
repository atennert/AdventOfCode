module Day7 where

import Prelude
import Effect (Effect)
import Node.FS.Sync (readTextFile)
import Node.Encoding (Encoding(..))
import Effect.Console (log)
import Data.Tuple (Tuple(Tuple))
import Data.List (concat)
import Data.List.Types (List(Nil), (:))
import Utils (split, strToBigInt)
import Data.Foldable (elem, sum)
import JS.BigInt (BigInt, fromInt, toString)
import Data.String (joinWith)


day7 ∷ Effect Unit
day7 = do
    input <- readTextFile UTF8 "day7.txt"
    log "Day 7:"
    log <<< show <<< day7_1 $ input
    log <<< show <<< day7_2 $ input

type Base = Tuple BigInt (List BigInt)

day7_1 :: String -> BigInt
day7_1 input = sum <<< compute gen1 <<< parse $ input

day7_2 :: String -> BigInt
day7_2 input = sum <<< compute gen2 <<< parse $ input

parse :: String -> List Base
parse input =
     let lines = split "\n"
         p' line = split ": " line
         p'' (x:xs:_) = Tuple (strToBigInt x) (map strToBigInt <<< split " " $ xs)
         p'' _ = Tuple (fromInt 0) Nil
     in map (p'' <<< p') <<< lines $ input

compute :: (List BigInt -> List BigInt) -> List Base -> List BigInt
compute _ Nil = Nil
compute f ((Tuple r parts):xs) =
    if elem r <<< f $ parts
    then r : compute f xs
    else compute f xs

gen1 :: List BigInt -> List BigInt
gen1 Nil = Nil
gen1 r@(_:Nil) = r
gen1 (a:b:xs) = concat (gen1 ((a + b):xs) : gen1 ((a * b):xs) : Nil)

gen2 :: List BigInt -> List BigInt
gen2 Nil = Nil
gen2 r@(_:Nil) = r
gen2 (a:b:xs) =
    let c = strToBigInt <<< joinWith "" $ [toString a, toString b]
    in concat (gen2 ((a + b):xs) : gen2 ((a * b):xs) : gen2 (c:xs) : Nil)
