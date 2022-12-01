package day10

import kotlin.test.Test
import kotlin.test.assertEquals

class Advent10Test {
    @Test
    fun check1() {
        val input =
                ".#..#\n" +
                ".....\n" +
                "#####\n" +
                "....#\n" +
                "...##"
        // 3,4 -> 8
        val visibleAsteroids = getVisibleAsteroids(input).first.size

        assertEquals(8, visibleAsteroids)
    }

    @Test
    fun check2() {
        val input =
                "......#.#.\n" +
                "#..#.#....\n" +
                "..#######.\n" +
                ".#.#.###..\n" +
                ".#..#.....\n" +
                "..#....#.#\n" +
                "#..#....#.\n" +
                ".##.#..###\n" +
                "##...#..#.\n" +
                ".#....####"
        // 5,8 -> 33
        val visibleAsteroids = getVisibleAsteroids(input).first.size

        assertEquals(33, visibleAsteroids)
    }

    @Test
    fun check3() {
        val input =
                "#.#...#.#.\n" +
                ".###....#.\n" +
                ".#....#...\n" +
                "##.#.#.#.#\n" +
                "....#.#.#.\n" +
                ".##..###.#\n" +
                "..#...##..\n" +
                "..##....##\n" +
                "......#...\n" +
                ".####.###."
        // 1,2 -> 35
        val visibleAsteroids = getVisibleAsteroids(input).first.size

        assertEquals(35, visibleAsteroids)
    }

    @Test
    fun check4() {
        val input = ".#..#..###\n" +
                "####.###.#\n" +
                "....###.#.\n" +
                "..###.##.#\n" +
                "##.##.#.#.\n" +
                "....###..#\n" +
                "..#.#..#.#\n" +
                "#..#.#.###\n" +
                ".##...##.#\n" +
                ".....#.#.."
        // 6,3 -> 41
        val visibleAsteroids = getVisibleAsteroids(input).first.size

        assertEquals(41, visibleAsteroids)
    }

    @Test
    fun check5() {
        val input = ".#..##.###...#######\n" +
                "##.############..##.\n" +
                ".#.######.########.#\n" +
                ".###.#######.####.#.\n" +
                "#####.##.#.##.###.##\n" +
                "..#####..#.#########\n" +
                "####################\n" +
                "#.####....###.#.#.##\n" +
                "##.#################\n" +
                "#####.##.###..####..\n" +
                "..######..##.#######\n" +
                "####.##.####...##..#\n" +
                ".#####..#.######.###\n" +
                "##...#.##########...\n" +
                "#.##########.#######\n" +
                ".####.#.###.###.#.##\n" +
                "....##.##.###..#####\n" +
                ".#.#.###########.###\n" +
                "#.#.#.#####.####.###\n" +
                "###.##.####.##.#..##"
        // 11,13 -> 210
        val visibleAsteroids = getVisibleAsteroids(input).first.size

        assertEquals(210, visibleAsteroids)
    }

    @Test
    fun checkVaporize() {
        val input =
                ".#....#####...#..\n" +
                "##...##.#####..##\n" +
                "##...#...#.#####.\n" +
                "..#.........###..\n" +
                "..#.#.....#....##"
    }
}
