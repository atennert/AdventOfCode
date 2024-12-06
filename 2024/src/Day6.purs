module Day6 where

import Prelude
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Node.FS.Sync (readTextFile)
import Effect.Console (log)
import Data.List.Types (List(Nil), (:))
import Utils (at, mark, split)
import Data.Tuple (Tuple(Tuple), fst)
import Data.List (concat, elem, elemIndex, filter, length, unzip, zip, (!!), (..))
import Data.Maybe (Maybe(Just, Nothing))
import Data.Foldable (sum)


day6 ∷ Effect Unit
day6 = do
    input <- readTextFile UTF8 "day6.txt"
    log "Day 6:"
    log <<< show <<< day6_1 $ input
    log <<< show <<< day6_2 $ input

day6_1 :: String -> Int
day6_1 input = countX <<< run <<< findPos <<< parse $ input

day6_2 :: String -> Int
day6_2 input = do
  let floor = parse input
  let Tuple initWalk _ = findPos floor
  let initRun = run $ Tuple initWalk floor
  let poss = positions initWalk initRun
  findBlocks initWalk poss floor

data Direction = Up | Down | Left | Right
type Position = Tuple Int Int
type Walk = Tuple Position Direction
type Floor = List (List String)

derive instance Eq Direction

parse :: String -> Floor
parse input = map (split "") <<< split "\n" $ input

findPos :: Floor -> (Tuple Walk Floor)
findPos floor =
    let fy Nil _ = Tuple 0 0
        fy (y:ys) j = case elemIndex "^" y of
            Just i -> Tuple i j
            Nothing -> fy ys (j+1)
    in Tuple (Tuple (fy floor 0) Up) floor


isBlocked :: Floor -> Position -> Boolean
isBlocked floor pos = case at floor pos of
      Just "#" -> true
      _ -> false

walk :: Floor -> Walk -> Walk
walk floor (Tuple p@(Tuple x y) Up) =    if isBlocked floor $ Tuple x (y-1) then Tuple p Right else Tuple (Tuple x (y-1)) Up
walk floor (Tuple p@(Tuple x y) Right) = if isBlocked floor $ Tuple (x+1) y then Tuple p Down else Tuple (Tuple (x+1) y) Right
walk floor (Tuple p@(Tuple x y) Down) =  if isBlocked floor $ Tuple x (y+1) then Tuple p Left else Tuple (Tuple x (y+1)) Down
walk floor (Tuple p@(Tuple x y) Left) =  if isBlocked floor $ Tuple (x-1) y then Tuple p Up else Tuple (Tuple (x-1) y) Left

getAtOr :: forall a. a -> List a -> Int -> a
getAtOr a es i = case es !! i of
            Just t -> t
            Nothing -> a

run :: Tuple Walk Floor -> Floor
run (Tuple w@(Tuple p@(Tuple x y) _) floor) =
    let isOnFloor = y >= 0 && y < (length floor) && x >= 0 && x < (length (getAtOr Nil floor 0))
        updateFloor = mark floor p "X"
    in if isOnFloor
        then run $ Tuple (walk floor w) updateFloor
        else floor

countX :: Floor -> Int
countX floor =
    let f e = if e == "X" then 1 else 0
    in sum <<< map (sum <<< map f) $ floor

positions :: Walk -> Floor -> List Position
positions (Tuple start _) fp =
    let xPos xs = fst <<< unzip <<< filter (\(Tuple _ c) -> c == "X") $ zip (0 .. (length xs)) xs
        yPos ys = concat <<< map (\(Tuple y xs) -> map (\x -> Tuple x y) xs) <<< zip (0 .. (length ys)) <<< map xPos $ ys
    in filter ((/=) start) <<< yPos $ fp

run' :: Tuple Walk Floor -> List Walk -> Maybe Floor
run' (Tuple w@(Tuple (Tuple x y) _) floor) past =
    let isOnFloor = y >= 0 && y < (length floor) && x >= 0 && x < (length (getAtOr Nil floor 0))
        nextStep = walk floor w
        hasLoop = elem w past
    in if isOnFloor
        then if hasLoop
            then Nothing
            else run' (Tuple nextStep floor) (w:past)
        else Just floor

findBlocks :: Walk -> List Position -> Floor -> Int
findBlocks w poss floor =
    let block f pos = mark f pos "#"
        runFloor w' f = run' (Tuple w' f) Nil
        eval m = case m of
            Nothing -> 1
            _ -> 0
    in sum <<< map (eval <<< runFloor w <<< block floor) $ poss
