module Day10 where

import Prelude
import Effect (Effect)
import Effect.Console (log)
import Node.FS.Sync (readTextFile)
import Node.Encoding (Encoding(UTF8))
import Utils (Triple(Triple), ext, ext2, sizeRectA, splitA, strToInt, atA)
import Data.Tuple (Tuple(Tuple), fst)
import Data.Array (concat, filter, foldl, length, modifyAt, nub, replicate, (:))
import Data.Maybe (Maybe(Just, Nothing), isJust)


day10 ∷ Effect Unit
day10 = do
    input <- readTextFile UTF8 "day10.txt"
    log "Day 10:"
    log <<< show <<< day10_1 $ input
    log <<< show <<< day10_2 $ input

type Position = Tuple Int Int
type Area = Array (Array Int)
type Tracker a = Array (Array (Maybe a))

day10_1 :: String -> Int
day10_1 input = scores <<< ext2 prepTracker <<< ext findStarts <<< parse $ input

day10_2 :: String -> Int
day10_2 input = scores2 <<< ext2 prepTracker <<< ext findStarts <<< parse $ input

parse :: String -> Area
parse input = map (map strToInt <<< splitA "") <<< splitA "\n" $ input

findStarts :: Area -> Array Position
findStarts arr =
    let fs' y ar = fst <<< foldl (\(Tuple acc x) a -> if a == 0 then Tuple ((Tuple x y):acc) (x+1) else Tuple acc (x+1)) (Tuple [] 0) $ ar
    in concat <<< fst <<< foldl (\(Tuple acc y) ar -> Tuple ((fs' y ar):acc) (y+1)) (Tuple [] 0) $ arr

prepTracker :: forall a. Tuple Area (Array Position) -> Tracker a
prepTracker (Tuple area _) =
    let (Tuple lenX lenY) = sizeRectA area
    in replicate lenY $ replicate lenX Nothing

nextLevel :: Int -> Area -> Tuple Int Int -> Array (Tuple Int Int)
nextLevel level area (Tuple x y) =
    let ps = [Tuple (x+1) y, Tuple x (y+1), Tuple (x-1) y, Tuple x (y-1)]
        isLevel p = case atA area p of
            Just e -> level == e
            Nothing -> false
    in filter isLevel $ ps

scores :: Triple Area (Array Position) (Tracker (Array Position)) -> Int
scores (Triple area ps tracker) =
    let check (Tuple rs t') p = (\(Tuple r t'') -> Tuple (r:rs) t'') <<< dfs area t' $ p
    in length <<< concat <<< fst <<< foldl check (Tuple [] tracker) $ ps

dfs :: Area -> (Tracker (Array Position)) -> Position -> Tuple (Array Position) (Tracker (Array Position))
dfs area tracker p@(Tuple x y) =
    let value = case atA area p of
            Just e -> e
            Nothing -> (-1) -- doesn't happen
        upTrack t ps = case modifyAt y (\xs -> case modifyAt x (\e -> case e of
                                                            Just ar -> Just (nub <<< concat $ [ps, ar])
                                                            Nothing -> Just ps) xs of
                                      Just ts -> ts
                                      Nothing -> []) t of
            Just tss -> tss
            Nothing -> []
        tm = case atA tracker p of
            Just e -> e
            Nothing -> Nothing
        tVal = case tm of
            Just t -> t
            Nothing -> []
        run (Tuple acc t') np = (\(Tuple acc'' t'') -> Tuple (nub <<< concat $ [acc, acc'']) t'') $ dfs area t' np
    in if value == 9
        then Tuple [p] $ upTrack tracker [p]
        else if isJust tm
            then Tuple tVal tracker
            else foldl run (Tuple [] tracker) $ nextLevel (value + 1) area p

scores2 :: Triple Area (Array Position) (Tracker Int) -> Int
scores2 (Triple area ps tracker) =
    let check (Tuple rs t') p = (\(Tuple r t'') -> Tuple (r+rs) t'') <<< dfs2 area t' $ p
    in fst <<< foldl check (Tuple 0 tracker) $ ps

dfs2 :: Area -> (Tracker Int) -> Position -> Tuple Int (Tracker Int)
dfs2 area tracker p@(Tuple x y) =
    let value = case atA area p of
            Just e -> e
            Nothing -> (-1) -- doesn't happen
        upTrack t ps = case modifyAt y (\xs -> case modifyAt x (\e -> case e of
                                                            Just ar -> Just (ps + ar)
                                                            Nothing -> Just ps) xs of
                                      Just ts -> ts
                                      Nothing -> []) t of
            Just tss -> tss
            Nothing -> []
        tm = case atA tracker p of
            Just e -> e
            Nothing -> Nothing
        tVal = case tm of
            Just t -> t
            Nothing -> 0
        run (Tuple acc t') np = (\(Tuple acc'' t'') -> Tuple (acc + acc'') t'') $ dfs2 area t' np
    in if value == 9
        then Tuple 1 $ upTrack tracker 1
        else if isJust tm
            then Tuple tVal tracker
            else foldl run (Tuple 0 tracker) $ nextLevel (value + 1) area p
