import jig.Entity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Random;

/*
This class exists for the purpose of rendering the floors and walls of the map.
 */
public class RenderMap extends Entity {

    // grabs a random map and returns it as a 2d array
    public static int[][] getRandomMap(Main dc) throws IOException {
<<<<<<< HEAD
        String directory = "/Users/tylerhiggins/teamgameproj/dungeoncrawl/dungeoncrawl/mapGen/maps/";
=======
        File f = new File("dungeoncrawl/mapGen/maps");
        // Get the absolute path of file f
        String absolute = f.getAbsolutePath();
        String directory = absolute + "/";
>>>>>>> develop
        Random r = new Random();
        int rand = r.nextInt(100);
        String filepath = directory + "map" + rand + ".txt";
        return loadMapFromFile(Paths.get(filepath));
    }

    /*
    Returns a small map 32 x 21 for debugging
     */
    public static int[][] getDebugMap(Main dc) {
        return new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1},
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
                {1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1},
                {1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };
    }

    /*
    Based on Streams from: https://stackoverflow.com/questions/22185683/read-txt-file-into-2d-array
    Uses Java's Stream API to convert a text file to a 2d int array
     */
    static public int[][] loadMapFromFile(Path path) throws IOException {
        return Files.lines(path)                        // Read all lines from the filepath
                .map(line -> line.split("\\s"))   // for each line, get an array chars split by spaces
                .map((sa) -> Stream.of(sa)              // convert char array to a sequential ordered stream
                .mapToInt(Integer::parseInt)            // map the char array to an int stream
                .toArray())                             // convert the int stream to an array
                .toArray(int[][]::new);                 // add the array to a 2d array
    }

    // Draw the 2D map to the screen
    public static void displayMap(Main dc) {
        int x, y;
        for (int i = 0; i < dc.map.length; i++) {
            for (int j = 0; j < dc.map[i].length; j++) {
                x = j * dc.tileH + dc.tileH/2;        // columns
                y = i * dc.tileW + dc.tileW/2;        // rows
                // WALLs
                if (dc.map[i][j] == 1) {
                    if (i+1 >= dc.map.length) {
                        dc.mapTiles[i][j] = new Wall(x, y, "top");
                    }
                    else if (dc.map[i+1][j] == 0) {
                        dc.mapTiles[i][j] = new Wall(x, y, "border");
                    }
                    else if (dc.map[i+1][j] == 1) {
                        dc.mapTiles[i][j] = new Wall(x, y, "top");
                    }
                }
                // FLOORs
                else if (dc.map[i][j] == 0) {
                    if (dc.map[i+1][j] == 1 && dc.map[i][j-1] == 1) {
                        dc.mapTiles[i][j] = new Floor(x, y, "shadow_double");
                    }
                    else if (i+1 < dc.map.length && dc.map[i+1][j] == 1) {
                        dc.mapTiles[i][j] = new Floor(x, y, "shadow");
                    }
                    else if (j-1 >= 0 && dc.map[i][j-1] == 1) {
                        dc.mapTiles[i][j] = new Floor(x, y, "shadow_right");
                    }
                    else if (j-1 > 0 && i+1 < dc.map[i].length && dc.map[i+1][j-1] == 1) {
                        dc.mapTiles[i][j] = new Floor(x, y, "shadow_corner");
                    }
                    else {
                        dc.mapTiles[i][j] = new Floor(x, y, "normal");
                    }
                }
            }
        }
    }

}
