import java.io.FileInputStream;
import java.util.List;
import java.util.Scanner;

import java.util.ArrayList;

public class Day20 {
    public static void main(String[] args) {
        List<Tile> tiles = readTiles();
        int len = (int) Math.sqrt(tiles.size());

        List<Tile> cornerTiles = findCornerTiles(tiles);

        long part1 = cornerTiles.stream()
                .map(Tile::getId)
                .reduce((a, b) -> a*b).get();
        System.out.println(part1);

        char[][] map = buildMap(cornerTiles, len);
        int monsters = findMonsters(map);
        int hashes = 0;
        for (char[] line : map) {
            for (char c : line) {
                if (c == '#') {
                    hashes++;
                }
            }
        }
        System.out.println(hashes - monsters * monster.length);
    }

    static int[][] monster = new int[][] {
        {1,0}, {2,1}, {2,4}, {1,5}, {1,6}, {2,7}, {2,10}, {1,11}, {1,12},
        {2,13}, {2,16}, {1,17}, {0,18}, {1,18}, {1,19}
    };

    static int findMonsters(char[][] map) {
        char[][] tmp = map;
        int len = map.length;
        int monsters = 0;
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 4; i++) {
                int mapMonsters = 0;
                for (int x = 0; x < len - 20; x++) {
                    for (int y = 0; y < len - 3; y++) {
                        boolean isMonster = true;
                        for (int[] pos : monster) {
                            if (tmp[y+pos[0]][x+pos[1]] != '#') {
                                isMonster = false;
                                break;
                            }
                        }
                        if (isMonster) {
                            mapMonsters++;
                        }
                    }
                }
                monsters = Math.max(monsters, mapMonsters);
                tmp = rotate(tmp);
            }
            tmp = flip(tmp);
        }
        return monsters;
    }

    static List<Tile> readTiles() {
        List<Tile> tiles = new ArrayList<>(144);
        try (Scanner in = new Scanner(new FileInputStream("day20/tiles.txt"))) {
            while (in.hasNextLine()) {
                String tileId = in.nextLine().split(" ")[1].replace(":", "");
                char[][] img = new char[10][10];
                int y = 0;
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.isEmpty()) break;

                    img[y] = line.toCharArray();
                    y++;
                }
                tiles.add(new Tile(tileId, img));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tiles;
    }

    static List<Tile> findCornerTiles(List<Tile> tiles) {
        List<Tile> corners = new ArrayList<>(4);
        List<Tile> rotated = new ArrayList<>(144);
        rotated.add(tiles.get(0));

        List<Tile> fitting = new ArrayList<>(4);
        for (int i = 0; i < tiles.size(); i++) {
            Tile refTile = rotated.get(i);
            fitting.clear();
            for (Tile chkTile : tiles) {
                if (refTile.equals(chkTile)) {
                    continue;
                }
                if (refTile.fits(chkTile)) {
                    fitting.add(chkTile);
                    if (!rotated.contains(chkTile)) {
                        rotated.add(chkTile);
                    }
                }
            }
            if (fitting.size() == 2) {
                corners.add(refTile);
            }
        }
        return corners;
    }

    static char[][] buildMap(List<Tile> cornerTiles, int len) {
        Tile tr = null;
        for (Tile tx : cornerTiles) {
            if (tx.top == null && tx.left == null) {
                tr = tx;
                break;
            }
        }

        int sideLen = len * 8;
        char[][] map = new char[sideLen][sideLen];
        int x = 0, y = 0, xb = 0;
        do {
            Tile tb = tr;
            do {
                for (char[] line : tb.getMapPiece()) {
                    x = xb;
                    for (char cell : line) {
                        map[y][x] = cell;
                        x++;
                    }
                    y++;
                }
                tb = tb.bottom;
            } while (tb != null);
            xb += 8;
            y = 0;
            tr = tr.right;
        } while (tr != null);

        return map;
    }

    static void printMap(char[][] map) {
        StringBuilder sb = new StringBuilder();
        for (char[] mapLine : map) {
            sb.append(mapLine).append("\n");
        }
        System.out.println(sb.toString() + "\n\n");
    }

    static char[][] rotate(char[][] img) {
        char[][] newImg = new char[img.length][img.length];
        for (int y = 0, z = img.length - 1; y < img.length; y++, z--) {
            for (int x = 0; x < img.length; x++) {
                newImg[x][z] = img[y][x];
            }
        }
        return newImg;
    }

    static char[][] flip(char[][] img) {
        char[][] newImg = new char[img.length][img.length];
        for (int y = 0, z = img.length - 1; y < img.length; y++, z--) {
            for (int x = 0; x < img.length; x++) {
                newImg[y][x] = img[z][x];
            }
        }
        return newImg;
    }

    static class Tile {
        final int id;
        char[][] img;
        Tile top = null;
        Tile right = null;
        Tile bottom = null;
        Tile left = null;
        
        Tile(String id, char[][] img) {
            this.id = Integer.parseInt(id);
            this.img = img;
        }

        boolean fits(Tile other) {
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < 4; i++) {
                    boolean fits = true;
                    // check top
                    for (int x = 0; x < img.length; x++) {
                        if (this.img[0][x] != other.img[img.length-1][x]) {
                            fits = false;
                            break;
                        }
                    }
                    if (fits) {
                        this.top = other;
                        other.bottom = this;
                        return true;
                    }
                    // check right
                    fits = true;
                    for (int y = 0; y < img.length; y++) {
                        if (this.img[y][img.length-1] != other.img[y][0]) {
                            fits = false;
                            break;
                        }
                    }
                    if (fits) {
                        this.right = other;
                        other.left = this;
                        return true;
                    }
                    // check bottom
                    fits = true;
                    for (int x = 0; x < img.length; x++) {
                        if (this.img[img.length-1][x] != other.img[0][x]) {
                            fits = false;
                            break;
                        }
                    }
                    if (fits) {
                        this.bottom = other;
                        other.top = this;
                        return true;
                    }
                    // check left
                    fits = true;
                    for (int y = 0; y < img.length; y++) {
                        if (this.img[y][0] != other.img[y][img.length-1]) {
                            fits = false;
                            break;
                        }
                    }
                    if (fits) {
                        this.left = other;
                        other.right = this;
                        return true;
                    }
                    other.img = rotate(other.img);
                }
                other.img = flip(other.img);
            }
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Tile)) {
                return false;
            }
            return ((Tile) obj).id == this.id;
        }

        @Override
        public int hashCode() {
            return this.id;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder().append(id + ":\n");
            for (char[] imgLine : img) {
                sb.append(imgLine).append("\n");
            }
            return sb.toString();
        }

        public long getId() {
            return id;
        }

        public char[][] getMapPiece() {
            char[][] copy = new char[img.length-2][img.length-2];
            for (int y = 0; y < copy.length; y++) {
                for (int x = 0; x < copy.length; x++) {
                    copy[y][x] = img[y+1][x+1];
                }
            }
            return copy;
        }
    }
}