module Day9 where

import Prelude
import Effect (Effect)
import Node.Encoding (Encoding(UTF8))
import Node.FS.Sync (readTextFile)
import Effect.Console (log)
import Utils (Triple(Triple), splitA, strToInt)
import JS.BigInt (BigInt, fromInt)
import Data.Maybe (Maybe(Just, Nothing))
import Data.Array (foldl, snoc, unsnoc)
import Data.Int (even)

foreign import checksum :: (BigInt -> Block -> Array Block -> Maybe (Triple BigInt Block (Array Block))) -> FS -> BigInt
foreign import sortBlocks :: (Int -> Block) -> Array Block -> Array Block

day9 ∷ Effect Unit
day9 = do
    input <- readTextFile UTF8 "day9.txt"
    log "Day 9:"
    log <<< show <<< day9_1 $ input
    log <<< show <<< day9_2 $ input

day9_1 :: String -> BigInt
day9_1 input = checksum runBlock1 <<< parse $ input

day9_2 :: String -> BigInt
day9_2 input = checksum runBlock2 <<< sortBlocks Space <<< parse $ input

type ID = Int
type Len = Int
data Block =
    Data ID Len
  | Space Len
type FS = Array Block

instance Show Block where
    show (Data id len) = "Data (" <> show id <> ", " <> show len <> ")"
    show (Space len) = "Space (" <> show len <> ")"
derive instance Eq Block

parse :: String -> FS
parse input =
    let p' = trd <<< foldl (\(Triple c i acc) v -> if even c
                                                   then Triple (c+1) (i+1) (snoc acc (Data i v))
                                                   else Triple (c+1) i (snoc acc (Space v))) (Triple 0 0 [])
    in p' <<< map strToInt <<< splitA "" $ input

trd :: forall a b c. Triple a b c -> c
trd (Triple _ _ c) = c


runBlock1 :: BigInt -> Block -> Array Block -> Maybe (Triple BigInt Block (Array Block))
runBlock1 i s@(Space l) tail = if l == 0
                            then Nothing
                            else case unsnoc tail of
                                Just {init, last} -> case last of
                                    Space _ -> runBlock1 i s init
                                    Data id ld -> if ld == 0
                                                  then runBlock1 i s init
                                                  else Just $ Triple ((fromInt id) * i) (Space (l - 1)) (snoc init (Data id (ld - 1)))
                                Nothing -> Nothing
runBlock1 i (Data id l) tail = if l == 0
                            then Nothing
                            else Just $ Triple ((fromInt id) * i) (Data id (l - 1)) tail


runBlock2 :: BigInt -> Block -> Array Block -> Maybe (Triple BigInt Block (Array Block))
runBlock2 _ (Space l) tail = if l == 0
                            then Nothing
                            else Just $ Triple (fromInt 0) (Space (l - 1)) tail
runBlock2 i (Data id l) tail = if l == 0
                            then Nothing
                            else Just $ Triple ((fromInt id) * i) (Data id (l - 1)) tail
