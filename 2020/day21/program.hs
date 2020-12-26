import Control.Arrow ((&&&))
import Data.Set (Set)
import Data.Map (Map)
import Data.Map as M (empty, insertWith, foldr, filter, map, union, null, elems)
import Data.Set as S (empty, fromList, union, intersection, toList, member, filter, size)
import Data.List.Split (splitOn)

type Allergen = String
type Ingredient = String

part1 :: ([Ingredient], Map Allergen [Set Ingredient]) -> String
part1 (ingredients, map) = show $ length $ Prelude.filter (not . (`S.member` withAllergent)) ingredients
  where mayhaveAllergent = foldr1 S.intersection
        withAllergent = M.foldr (\i s -> S.union s $ mayhaveAllergent i) S.empty map

part2 :: ([Ingredient], Map Allergen [Set Ingredient]) -> String
part2 (_, map) = combine $ M.elems $ collate M.empty $ filterInAllergents $ getAllergents map
  where mayhaveAllergent = foldr1 S.intersection
        withAllergent = M.foldr (\i s -> S.union s $ mayhaveAllergent i) S.empty map
        getAllergents = M.map (Prelude.map (S.filter (`S.member` withAllergent)))
        filterInAllergents = M.map (foldr1 S.intersection)
        collate nm om | M.null om = nm
                      | otherwise = collate (singleIng nm om) $ cleanUp (singleIng nm om) om
        singleIng nm = M.union nm . M.map (head . S.toList) . M.filter ((== 1) . S.size)
        cleanUp nm = M.map (S.filter (not . (`elem` M.elems nm))) . M.filter ((> 1) . S.size)
        combine = foldr1 (\e1 e2 -> e1 ++ "," ++ e2)

convert :: [String] -> ([Ingredient], Map Allergen [Set Ingredient])
convert = Prelude.foldr (analyze . splitOn " (contains ") ([], M.empty)
  where analyze [ingredients,allergens] (il, map) =
          (splitOn " " ingredients ++ il, Prelude.foldr (\a -> M.insertWith (++) a [makeSet ingredients]) map $ splitOn ", " $ init allergens)
        makeSet = S.fromList . splitOn " "

main = interact $ show . compute . convert . lines
  where compute = part1 &&& part2
