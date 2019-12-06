package server;

import java.util.ArrayList;
import java.util.Random;

/*
This class spawns AI players and items locations, these can be sent to the client and then rendered
 */
public class AI {

    /**
     * update the position of the AI player
     */
    public static void updatePosition() {
        // TODO run dijkstra
        for (Msg ai : Server.enemies) {
//            ai.wx += 2;
//            ai.wy -= 16;
            continue;
            // move random
            // if ai close enough pathfind with dijkstra
        }
    }


    /**
     * Randomly creates coordinates, type, and ID for the AI characters to spawn
     * The result is saved to an arraylist of strings which is then returned
     */
    public static ArrayList<Msg> spawnEnemies(int[][] map, int count) {
        int tilesize = 32;
        int offset = tilesize/2;
        int doubleOffset = offset/2;
        ArrayList<Msg> enemies = new ArrayList<>(count);
        int maxcol =  map.length - 2;
        int maxrow = map[0].length - 2;
        Random rand = new Random();
        while( count > 0 ){
            int col = rand.nextInt(maxcol);
            int row = rand.nextInt(maxrow);
            while(row < 2 || col < 2 || map[col][row] == 1){
                col = rand.nextInt(maxcol) - 1;
                row = rand.nextInt(maxrow) - 1;
            }
            float wx = (tilesize * row) - offset;
            float wy = (tilesize * col) - tilesize - doubleOffset;
            int id = (int)System.nanoTime();

            if (id < 0) {
                id  = -id;
            }
            Msg message = new Msg(id, "skeleton_basic",wx,wy,150);   // "x y id"
//            System.out.println("Created "+message.id);
            enemies.add(message);
//            dc.characters.add(new Character(dc, wx, wy, "skeleton_basic", (int) System.nanoTime(), this, true));

            //create a random item at the given position
            count--;
        }
        return enemies;
    }

    public static ArrayList<Msg> spawnDebugEnemies(int[][] map) {
        ArrayList<Msg> enemies = new ArrayList<>(1);
        float wx = (32 * 18) - 16;
        float wy = (32 * 18) - 16 - 8;
        Msg message = new Msg(333, "skeleton_basic",wx,wy,150);   // "x y id"
        enemies.add(message);
        return enemies;
    }
}
