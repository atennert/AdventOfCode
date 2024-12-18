module Day11 where

import Prelude
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Effect.Console (log)
import Node.FS.Sync (readTextFile)
import Utils (splitA, strToBigInt)
import JS.BigInt (BigInt, fromInt, toString)
import Data.String (length, splitAt) as S
import Data.Int (even)
import Data.Foldable (foldl)
import Data.Tuple (Tuple(Tuple), fst)
import JS.Map.Primitive (Map, insert, lookup, empty)
import JS.Map.Primitive.ST as STM
import Data.Maybe (Maybe(Just, Nothing))

day11 ∷ Effect Unit
day11 = do
    input <- readTextFile UTF8 "day11.txt"
    log "Day 11:"
    log <<< show <<< day11_1 $ input
    log <<< show <<< day11_2 $ input

day11_1 :: String -> BigInt
day11_1 input = fst <<< foldl (\(Tuple b m) a -> onFst (+) b $ run 25 a m) (Tuple bi0 empty) <<< parse $ input

day11_2 :: String -> BigInt
day11_2 input = fst <<< foldl (\(Tuple b m) a -> onFst (+) b $ run 75 a m) (Tuple bi0 empty) <<< parse $ input


parse :: String -> Array BigInt
parse input = map strToBigInt <<< splitA " " $ input

bi0 = fromInt 0
bi1 = fromInt 1
bi2024 = fromInt 2024

onFst :: forall a b. (a -> a -> a) -> a -> Tuple a b -> Tuple a b
onFst f b' (Tuple b'' l'') = Tuple (f b' b'') l''

type Log = Map String BigInt

key :: Int -> BigInt -> String
key a b = show a <> "-" <> show b

run :: Int -> BigInt -> Log -> Tuple BigInt Log
run 0 _ l = Tuple bi1 l
run a bi l = case lookup (key a bi) l of
                  Just v -> Tuple v l
                  Nothing -> if bi == bi0
                            then let (Tuple r l') = run (a-1) bi1 l
                                  in Tuple r $ insert (key a bi) r l'
                            else let strX = toString bi
                                     lenX = S.length strX
                                     {before, after} = S.splitAt (lenX / 2) strX
                                     run'' s l' = run (a-1) (strToBigInt s) l'
                                     run' (Tuple b' l') s = onFst (+) b' $ run'' s l'
                                 in if even lenX
                                    then let (Tuple r l') = foldl run' (Tuple bi0 l) [before, after]
                                          in Tuple r $ insert (key a bi) r l'
                                    else let (Tuple r l') = run (a-1) (bi * bi2024) l
                                          in Tuple r $ insert (key a bi) r l'
