module Day5 where

import Prelude
import Node.FS.Sync (readTextFile)
import Node.Encoding (Encoding(UTF8))
import Effect (Effect)
import Effect.Console (log)
import Data.Tuple (Tuple(Tuple))
import Data.List.Types (List(Nil), (:))
import JS.Map.Primitive (Map, alter, lookup)
import JS.Map.Primitive.ST as STM
import Data.Set (Set, insert, member, singleton)
import Data.List (filter, head, last, length, sortBy, (!!))
import Utils (split, strToInt)
import Data.Maybe (Maybe(Just, Nothing))
import Data.Foldable (or, sum)
import Data.Ordering (Ordering(EQ))


day5 ∷ Effect Unit
day5 = do
    input <- readTextFile UTF8 "day5.txt"
    log "Day 5:"
    log <<< show <<< day5_1 $ input
    log <<< show <<< day5_2 $ input

day5_1 :: String -> Page
day5_1 input = sum <<< map getMiddle <<< filterSorted <<< parse $ input

day5_2 :: String -> Page
day5_2 input = sum <<< map getMiddle <<< sortPages <<< filterUnsorted <<< parse $ input

type Page = Int
type Rules = Map Page (Set Page)

parse :: String -> Tuple Rules (List (List Page))
parse input =
    let p (r:u:_) = Tuple (pr <<< split "\n" $ r) (map (map strToInt) <<< map (split ",") <<< split "\n" $ u)
        p _ = Tuple (pr Nil) Nil
    in p <<< split "\n\n" $ input

pr :: List String -> Rules
pr Nil = STM.run do STM.new
pr (r:rs) = let rules = pr rs
                kv = map strToInt <<< split "|" $ r
                mk = head kv
                mv = last kv
            in case mk of
              Just k -> case mv of
                Just v -> alter (\mset -> case mset of
                            Just set -> Just $ insert v set
                            Nothing -> Just $ singleton v) k rules
                Nothing -> rules
              Nothing -> rules

filterSorted :: Tuple Rules (List (List Page)) -> List (List Page)
filterSorted (Tuple rules pagess) =
    let isSorted pages = isSorted' rules Nil pages
    in filter isSorted pagess

filterUnsorted :: Tuple Rules (List (List Page)) -> Tuple Rules (List (List Page))
filterUnsorted (Tuple rules pagess) =
    let isUnsorted pages = not <<< isSorted' rules Nil $ pages
    in Tuple rules (filter isUnsorted pagess)

isSorted' :: Rules -> List Int -> List Int -> Boolean
isSorted' _ _ Nil = true
isSorted' r Nil (y:ys) = isSorted' r (y:Nil) ys
isSorted' r xs (y:ys) =
    let mSet = lookup y r
        isBadOrdered x = case mSet of
                          Just set -> member x set
                          Nothing -> false
    in if or <<< map isBadOrdered $ xs
        then false
        else isSorted' r (y:xs) ys

sortPages :: Tuple Rules (List (List Page)) -> List (List Page)
sortPages (Tuple rules pagess) = map (sortBy (pageOrder rules)) pagess

pageOrder :: Rules -> Page -> Page -> Ordering
pageOrder rules p1 p2 =
    let mSetP1 = lookup p1 rules
        mSetP2 = lookup p2 rules
        p1ThenP2 = case mSetP1 of
            Just setP1 -> member p2 setP1
            Nothing -> false
        p2ThenP1 = case mSetP2 of
            Just setP2 -> member p1 setP2
            Nothing -> false
    in if p1ThenP2
        then LT
        else if p2ThenP1
            then GT
            else EQ

getMiddle :: List Int -> Int
getMiddle l =
    let iMiddle = (length l) / 2
    in case l !! iMiddle of
        Just a -> a
        Nothing -> 0
