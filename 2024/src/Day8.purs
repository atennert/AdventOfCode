module Day8 where

import Prelude
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Node.FS.Sync (readTextFile)
import Effect.Console (log)
import Data.List.Types (List(Nil), (:))
import Data.Tuple (Tuple(Tuple), fst, uncurry)
import JS.Map.Primitive (Map, alter, values)
import JS.Map.Primitive.ST as STM
import Data.Set (Set, insert, singleton, size)
import Utils (isInRect, split)
import Data.Maybe (Maybe(Just, Nothing))
import Data.Foldable (foldl)
import Data.List (concat, filter, fromFoldable, length, nub)


day8 ∷ Effect Unit
day8 = do
    input <- readTextFile UTF8 "day8.txt"
    log "Day 8:"
    log <<< show <<< day8_1 $ input
    log <<< show <<< day8_2 $ input

day8_1 :: String -> Int
day8_1 input = length <<< antinodes gen1 <<< parse $ input

day8_2 :: String -> Int
day8_2 input = length <<< (\t@(Tuple area _) -> antinodes (gen2 area) t) <<< parse $ input

type Area = List (List String)
type Position = Tuple Int Int
type Antennas = Map String (Set Position)
type Antinode = Tuple Int Int

parse :: String -> (Tuple Area Antennas)
parse input =
    let area = map (split "") <<< split "\n" $ input
    in Tuple area (antennas 0 area)

antennas :: Int -> Area -> Antennas
antennas _ Nil = STM.run do STM.new
antennas j (x:xs) =
    let ants = antennas (j+1) xs
        addAnt ants' s i = if s /= "."
                    then alter (\mset -> case mset of
                                 Just set -> Just $ insert (Tuple i j) set
                                 Nothing -> Just $ singleton (Tuple i j)) s ants'
                    else ants'
    in fst <<< foldl (\(Tuple a i) s -> Tuple (addAnt a s i) (i+1)) (Tuple ants 0) $ x

antinodes :: (Position -> Position -> List Position) -> (Tuple Area Antennas) -> List (Antinode)
antinodes gen (Tuple area ants) =
    let pairs set =
            let ants' = fromFoldable set
                p' (Nil) = Nil
                p' (_:Nil) = Nil
                p' (p:ps) = concat $ (map (Tuple p) ps) : (p' ps) : Nil
            in p' ants'
        check set = if size set > 1
                    then nub <<< concat <<< map (uncurry gen) <<< pairs $ set
                    else Nil
    in nub <<< filter (isInRect area) <<< concat <<< map check <<< fromFoldable <<< values $ ants

gen1 :: Position -> Position -> List Position
gen1 (Tuple x1 y1) (Tuple x2 y2) =
    let Tuple dx dy = Tuple (x1 - x2) (y1 - y2)
    in (Tuple (x1 + dx) (y1 + dy)) : (Tuple (x2 - dx) (y2 - dy)) : Nil

gen2 :: Area -> Position -> Position -> List Position
gen2 area t1@(Tuple x1 y1) t2@(Tuple x2 y2) =
    let td@(Tuple dx dy) = Tuple (x1 - x2) (y1 - y2)
        negTd = Tuple (-dx) (-dy)
        g' tx td' = let s = addPos tx td'
                   in if isInRect area s
                      then s : (g' s td')
                      else Nil
    in t1 : t2 : (concat $ (g' t1 td) : (g' t2 negTd) : Nil)

addPos :: Position -> Position -> Position
addPos (Tuple x1 y1) (Tuple x2 y2) = Tuple (x1 + x2) (y1 + y2)
