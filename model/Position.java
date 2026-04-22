package model;

public record Position(int x, int y) {

    public Position translate(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    public int distanceManhattan(Position other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public double distanceEuclidean(Position other) {
        int dx = x - other.x;
        int dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean isAdjacent(Position other) {
        return distanceManhattan(other) == 1;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
