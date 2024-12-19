module Day12 where

import Prelude
import Effect (Effect)
import Node.FS.Sync (readTextFile)
import Node.Encoding (Encoding(UTF8))
import Effect.Console (log)
import Utils (Triple(Triple), atA, ext, markA, sizeRectA, splitA)
import Data.Foldable (sum)
import Data.Tuple (Tuple(Tuple), fst, uncurry)
import Data.Array (delete, elem, elemIndex, filter, findIndex, foldl, length, replicate, uncons, union, zip, (!!), (:))
import Data.Maybe (Maybe(Just, Nothing), isJust)
import Data.Tuple.Nested (over1)

day12 ∷ Effect Unit
day12 = do
    input <- readTextFile UTF8 "day12.txt"
    log "Day 12:"
    log <<< show <<< day12_1 $ input
    log <<< show <<< day12_2 $ input

day12_1 :: String -> Int
day12_1 input = sum <<< map (uncurry (*)) <<< map (over1 length) <<< uncurry areaPeri <<< ext mkTracker <<< parse $ input

day12_2 :: String -> Int
day12_2 input = sum <<< map (uncurry (*)) <<< map (over1 sides) <<< uncurry areaPeri <<< ext mkTracker <<< parse $ input

type Tracker = Array (Array Boolean)
type Fields = Array (Array String)
type Position = Tuple Int Int
data Dir = Up | Down | Left | Right
type AreaPeri = Tuple (Array (Triple Int Int Dir)) Int

derive instance Eq Dir

parse :: String -> Fields
parse input = map (splitA "") <<< splitA "\n" $ input

mkTracker :: Fields -> Tracker
mkTracker a = let Tuple lenX lenY = sizeRectA a
              in replicate lenY $ replicate lenX false

areaPeri :: Fields -> Tracker -> Array AreaPeri
areaPeri fields tracker =
    let w p = walk fields tracker p
    in case next tracker of
        Just p -> (\(Tuple r t') -> r:(areaPeri fields t')) $ w p
        Nothing -> []

next :: Tracker -> Maybe Position
next tracker = let xm line = elemIndex false line
                   ym = findIndex (isJust <<< xm) tracker
                in case ym of
                    Just y -> case tracker !! y of
                        Just line -> case xm line of
                            Just x -> Just $ Tuple x y
                            Nothing -> Nothing
                        Nothing -> Nothing
                    Nothing -> Nothing

walk :: Fields -> Tracker -> Position -> Tuple AreaPeri Tracker
walk fields tracker p@(Tuple x y) =
    case atA tracker p of
        Just true -> Tuple (Tuple [] 0) tracker
        _ -> let ps = [Tuple (x+1) y, Tuple x (y+1), Tuple (x-1) y, Tuple x (y-1)]
                 s p' = case atA fields p' of
                     Just c -> c
                     _ -> ""
                 myS = s p
                 needsChecking p' = case atA tracker p' of
                     Just false -> s p' == myS
                     _ -> false
                 filteredPs = filter needsChecking $ ps
                 peri = map (\(Tuple _ d) -> Triple x y d) <<< filter ((/=) myS <<< s <<< fst) $ zip ps [Right, Down, Left, Up]
            in foldl (\(Tuple pa' t') p' -> over1 (add' pa') $ walk fields t' p') (Tuple (Tuple peri 1) (markA tracker p true)) filteredPs

add' :: forall a. Eq a => Tuple (Array a) Int -> Tuple (Array a) Int -> Tuple (Array a) Int
add' (Tuple a1 b1) (Tuple a2 b2) = Tuple (union a1 a2) (b1 + b2)

sides :: Array (Triple Int Int Dir) -> Int
sides a = case uncons a of
    Just {head, tail} ->
        let filter' f x y d l = if elem (Triple x y d) l
                                then f x y d $ delete (Triple x y d) l
                                else l
            filterUp x y d l = filter' filterUp x (y-1) d l
            filterDown x y d l = filter' filterDown x (y+1) d l
            filterLeft x y d l = filter' filterLeft (x-1) y d l
            filterRight x y d l = filter' filterRight (x+1) y d l
        in case head of
            Triple x y Up    -> 1 + (sides <<< filterRight x y Up <<< filterLeft x y Up $ tail)
            Triple x y Down  -> 1 + (sides <<< filterRight x y Down <<< filterLeft x y Down $ tail)
            Triple x y Left  -> 1 + (sides <<< filterDown x y Left <<< filterUp x y Left $ tail)
            Triple x y Right -> 1 + (sides <<< filterDown x y Right <<< filterUp x y Right $ tail)
    _ -> 0
