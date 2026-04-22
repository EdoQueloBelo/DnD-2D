package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Snapshot dello stato di gioco passato a AI e Renderer.
 * Non contiene logica, solo dati.
 */
public class GameState {
    public final GameMap map;
    public final Player  player;
    public final List<Enemy> enemies;
    public int turn;

    public GameState(GameMap map, Player player) {
        this.map     = map;
        this.player  = player;
        this.enemies = new ArrayList<>();
        this.turn    = 0;
    }

    public void addEnemy(Enemy e)    { enemies.add(e); }
    public void removeEnemy(Enemy e) { enemies.remove(e); }

    public List<Enemy> livingEnemies() {
        return enemies.stream().filter(Entity::isAlive).toList();
    }
}
