package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Griglia di gioco. Gestisce tiles, walkability, line-of-sight semplice
 * e ricerca di posizioni di copertura vicine.
 */
public class GameMap {
    private final int width;
    private final int height;
    private final TileType[][] tiles;

    public GameMap(int width, int height) {
        this.width  = width;
        this.height = height;
        this.tiles  = new TileType[height][width];
        // inizializza tutto a FLOOR
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                tiles[y][x] = TileType.FLOOR;
    }

    public int getWidth()  { return width;  }
    public int getHeight() { return height; }

    public TileType getTile(int x, int y) { return tiles[y][x]; }
    public TileType getTile(Position p)   { return tiles[p.y()][p.x()]; }

    public void setTile(int x, int y, TileType t) { tiles[y][x] = t; }

    public boolean inBounds(Position p) {
        return p.x() >= 0 && p.x() < width && p.y() >= 0 && p.y() < height;
    }

    public boolean isWalkable(Position p) {
        return inBounds(p) && tiles[p.y()][p.x()] != TileType.WALL;
    }

    public boolean isCover(Position p) {
        return inBounds(p) && tiles[p.y()][p.x()] == TileType.COVER;
    }

    /**
     * Line-of-sight via Bresenham. Restituisce true se non ci sono WALL tra from e to.
     */
    public boolean hasLineOfSight(Position from, Position to) {
        int x0 = from.x(), y0 = from.y();
        int x1 = to.x(),   y1 = to.y();
        int dx = Math.abs(x1 - x0), dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (x0 == x1 && y0 == y1) return true;
            TileType t = tiles[y0][x0];
            if (t == TileType.WALL || t == TileType.COVER) return false;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x0 += sx; }
            if (e2 <  dx) { err += dx; y0 += sy; }
        }
    }

    /**
     * Restituisce tutte le posizioni di COVER adiacenti (manhattan ≤ 2) a pos
     * che abbiano LoS verso target (per poter attaccare poi).
     */
    public List<Position> nearbyCoverPositions(Position pos, Position target) {
        List<Position> result = new ArrayList<>();
        int radius = 3;
        for (int dy = -radius; dy <= radius; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                Position candidate = new Position(pos.x() + dx, pos.y() + dy);
                if (!isWalkable(candidate)) continue;
                // deve essere adiacente a una COVER tile
                boolean nearCover = false;
                for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                    Position adj = new Position(candidate.x()+d[0], candidate.y()+d[1]);
                    if (inBounds(adj) && isCover(adj)) { nearCover = true; break; }
                }
                if (nearCover && hasLineOfSight(candidate, target)) result.add(candidate);
            }
        }
        return result;
    }

    /**
     * 4-direzioni: N S E W.
     */
    public List<Position> walkableNeighbors(Position p) {
        List<Position> out = new ArrayList<>();
        for (int[] d : new int[][]{{0,-1},{0,1},{1,0},{-1,0}}) {
            Position n = p.translate(d[0], d[1]);
            if (isWalkable(n)) out.add(n);
        }
        return out;
    }
}
