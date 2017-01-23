package victory.engine.world;

import java.io.File;
import java.util.Scanner;

import victory.engine.graphics.Screen;
import victory.engine.graphics.ScreenController;
import victory.engine.graphics.Sprite;
import victory.engine.graphics.SpriteSheet;

public class Map implements ScreenController {
    short[] tilemap; // data. This array is of shorts instead of bytes because
                        // of a need to keep all tile indices positive. How I
                        // wish java did unsigned data...
    public final int MAP_WIDTH, MAP_HEIGHT;
    public final int TILE_WIDTH, TILE_HEIGHT;
    private final SpriteSheet tileset;
    public CollisionMap cmap;

    /**
     * Generates new Map (with collisions!).
     * 
     * @param w
     *            width of the map
     * @param h
     *            height of the map
     */
    public Map(int w, int h) {
        MAP_WIDTH = w;
        MAP_HEIGHT = h;
        TILE_WIDTH = TILE_HEIGHT = 16;
        tileset = new SpriteSheet("tiles.png");
        tilemap = new short[w * h];
        generateMap();
        cmap = new CollisionMap(this, null);
    }

    /**
     * Generates a new Map (with collisions!) using a certain spritesheet.
     * 
     * @param w
     *            width of the map
     * @param h
     *            height of the map
     * @param set
     *            tileset/spritesheet to use.
     */
    public Map(int w, int h, SpriteSheet set, String url) {
        MAP_WIDTH = w;
        MAP_HEIGHT = h;
        TILE_WIDTH = TILE_HEIGHT = 16;
        tilemap = new short[w * h];
        for (int i = 0; i < tilemap.length; i++) {
            tilemap[i] = 0;
        }
        load(url);
        tileset = set;
        cmap = new CollisionMap(this, url+".col");
    }

    /**
     * Loads a CSV file into a map.
     * 
     * @param url
     */
    public void load(String url) {
        Scanner s;
        try {
            s = new Scanner(new File(url)).useDelimiter(",|\n|\t|\r|\f| ");
        } catch (Exception e) {
            s = null;
        }
        for (int i = 0; i < MAP_WIDTH * MAP_HEIGHT; ++i) {
            if (s == null) {
                tilemap[i] = 0x00;
            } else if (s.hasNext()) {
                int t;
                try{
                    t = Integer.parseInt(s.next());
                }catch(Exception e){
                    t = 0x00;
                }
                t = (t < 0x00) ? 0 : t;
                t = (t > 0xFF) ? 0 : t;
                tilemap[i] = (short) t;
            } else {
                tilemap[i] = 0x00;
            }
        }
        s.close();
    }

    /**
     * The width of the map, in tiles.
     * 
     * @return width
     */
    public int getWidth() {
        return MAP_WIDTH;
    }

    /**
     * The height of the map, in tiles.
     * 
     * @return height
     */
    public int getHeight() {
        return MAP_HEIGHT;
    }

    /**
     * Generates the tilemap for this Map.
     */
    public void generateMap() {
        int[] heightmap = new int[MAP_WIDTH];
        int[] temp = new int[MAP_WIDTH / 16];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = (int) (Math.random() * 24) + 12;
        }
        heightmap = generateHeightMap(temp);
        /*
         * for(int i = 0; i < MAP_WIDTH; i++){ heightmap[i] =
         * (int)(Math.random() * 6) + 13; // give us a really simple heightmap }
         */
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (y >= heightmap[x]) {
                    setTile(x, y, (short) 0x11);
                } else {
                    setTile(x, y, (short) 0x00);
                }
            }
        }
    }

    /**
     * Animates the tiles belonging to the bottom row of the tileset.
     */
    public void animate() {
        for (int i = 0; i < MAP_WIDTH * MAP_HEIGHT; ++i) {
            if (tilemap[i] >= 0xF0 && tilemap[i] <= 0xFF) {
                switch (tilemap[i]) {
                case 0xF0:
                    tilemap[i] = 0xF1;
                    break;
                case 0xF1:
                    tilemap[i] = 0xF2;
                    break;
                case 0xF2:
                    tilemap[i] = 0xF3;
                    break;
                case 0xF3:
                    tilemap[i] = 0xF0;
                    break;
                //
                case 0xF4:
                    tilemap[i] = 0xF5;
                    break;
                case 0xF5:
                    tilemap[i] = 0xF6;
                    break;
                case 0xF6:
                    tilemap[i] = 0xF7;
                    break;
                case 0xF7:
                    tilemap[i] = 0xF4;
                    break;
                //
                case 0xF8:
                    tilemap[i] = 0xF9;
                    break;
                case 0xF9:
                    tilemap[i] = 0xFA;
                    break;
                case 0xFA:
                    tilemap[i] = 0xFB;
                    break;
                case 0xFB:
                    tilemap[i] = 0xF8;
                    break;
                //
                case 0xFC:
                    tilemap[i] = 0xFD;
                    break;
                case 0xFD:
                    tilemap[i] = 0xFE;
                    break;
                case 0xFE:
                    tilemap[i] = 0xFF;
                    break;
                case 0xFF:
                    tilemap[i] = 0xFC;
                    break;
                }
            }
        }
    }

    /**
     * Generates a heightmap for use in generateMap(), recursively
     * 
     * @param h
     *            initial array
     * @return
     */
    public int[] generateHeightMap(int[] h) {
        if (h.length == MAP_WIDTH)
            return h;
        if (h.length % 2 != 0)
            return null;
        if (h.length > MAP_WIDTH)
            return null;
        int[] result = new int[h.length * 2];
        for (int i = 0; i < h.length; i++) {
            result[i * 2] = h[i];
        }
        for (int i = 1; i < result.length; i += 2) {
            if (i != result.length - 1) {
                result[i] = (int) Math.abs(Math.random()
                        * (result[i - 1] - result[i + 1]))
                        + Math.min(result[i - 1], result[i + 1]);
            } else {
                result[i] = (int) Math.abs(Math.random()
                        * (result[i - 1] - result[0]))
                        + Math.min(result[i - 1], result[0]);
            }
        }
        return generateHeightMap(result);
    }

    /**
     * Sets a tile on the map.
     * 
     * @param x
     * @param y
     * @param t
     */
    public void setTile(int x, int y, short t) {
        if (x >= 0 && y >= 0 && x < MAP_WIDTH && y < MAP_HEIGHT) {
            tilemap[x + (MAP_WIDTH * y)] = t;
            boolean perm = !(t == 0x00);
            if (cmap != null) {
                cmap.setAt(x, y, perm);
            }
        } else {
            return;
        }

    }

    public short getTile(int x, int y) {
        if (x >= 0 && y >= 0 && x < MAP_WIDTH && y < MAP_HEIGHT) {
            return tilemap[x + (y * MAP_WIDTH)];
        } else {
            return 0x00;
        }
    }

    @Override
    public void draw(int sx, int sy, Screen s) {
        Sprite drawSprite = new Sprite(16, 16, tileset);
        for (int y = (-sy/16); y < MAP_HEIGHT; y++) {
            for (int x = (-sx/16); x < MAP_WIDTH; x++) {
                if ((x * TILE_WIDTH) + sx >= -TILE_WIDTH
                        && (x * TILE_WIDTH) + sx < s.getScreenWidth()
                        && (y * TILE_HEIGHT) + sy >= -TILE_HEIGHT
                        && (y * TILE_HEIGHT) + sy < s.getScreenHeight()) {
                    drawSprite.setIndex(tilemap[x + (MAP_WIDTH * y)]
                            % TILE_WIDTH, tilemap[x + (MAP_WIDTH * y)]
                            / TILE_HEIGHT);
                    drawSprite.draw(sx + (x * TILE_WIDTH), sy + (y * TILE_HEIGHT), s);
                }
                if(x * TILE_WIDTH + sx > s.getScreenWidth()) break;
            }
            if(y * TILE_HEIGHT + sy > s.getScreenHeight()) break;
        }
    }
}
