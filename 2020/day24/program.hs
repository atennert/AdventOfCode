import Control.Arrow ((&&&))
import Data.List (isPrefixOf, sort)
import Data.Set as S (Set, fromList, toList, size, filter, member, union, map)

type Tile = (Int, Int, Int)

offsets = [(1,-1,0), (-1,1,0), (0,-1,1), (0,1,-1), (-1,0,1), (1,0,-1)]

add (a,b,c) (i,j,k) = (a+i, b+j, c+k)

blackTiles :: [Tile] -> [Tile]
blackTiles [] = []
blackTiles (t:ts) | t `elem` ts = blackTiles $ tail ts
                  | otherwise   = t : blackTiles ts

flipping :: Int -> Set Tile -> [Tile]
flipping 0 ts    = toList ts
flipping days ts = flipping (days-1) (flipW2B `union` flipB2W)
  where adjTs t = fromList [add t tx | tx <- offsets]
        adjBlTs = size . S.filter (`member` ts) . adjTs
        flipB2W = S.filter ((`elem` [1,2]) . adjBlTs) ts
        flipW2B = S.filter ((==2) . adjBlTs) $ foldr1 union $ S.map adjTs ts

convert :: [String] -> [Tile]
convert = sort . Prelude.map getTile

getTile :: String -> Tile
getTile [] = (0,0,0)
getTile path | "e"  `isPrefixOf` path = add (1,-1,0) $ getTile $ tail path
             | "w"  `isPrefixOf` path = add (-1,1,0) $ getTile $ tail path
             | "se" `isPrefixOf` path = add (0,-1,1) $ getTile $ drop 2 path
             | "nw" `isPrefixOf` path = add (0,1,-1) $ getTile $ drop 2 path
             | "sw" `isPrefixOf` path = add (-1,0,1) $ getTile $ drop 2 path
             | "ne" `isPrefixOf` path = add (1,0,-1) $ getTile $ drop 2 path

main = interact $ show . compute . convert . lines
  where compute = length . blackTiles &&& length . flipping 100 . fromList . blackTiles
