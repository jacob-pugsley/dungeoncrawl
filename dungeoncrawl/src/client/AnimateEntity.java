package client;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.Animation;



// TODO most likely each character will contain an instance of this class
// TODO

/**
 * A class representing a characters movement.
 */
public class AnimateEntity extends Entity {
    public Animation animation;
    private String action;
    private String sprite;
    private String spritesheet;
    private int speed;


    /*
    Creates an Animated Entity
    @param x entities starting x position
    @param y entities starting y position
    @param speed The speed the animation plays at, 100 is pretty balanced
    @param sprite The sprite that is being animated
     */
    public AnimateEntity(float x, float y, int speed, String sprite) {
        super(x, y);
//        System.out.printf("Animate Entity %s, %s\n", x, y);
        //this.action = action;
        this.speed = speed;
        this.sprite = sprite;
        this.spritesheet = getSpritesheet();
    }

    /*
    Get the correct spritesheet for the specified sprite
    */
    public String getSpritesheet() {
        String spritesheet = null;
        switch (sprite) {
            case "knight_leather": {
                spritesheet = Main.KNIGHT_LEATHER;
                break;
            }
            case "knight_iron": {
                spritesheet = Main.KNIGHT_IRON;
                break;
            }
            case "knight_gold": {
                spritesheet = Main.KNIGHT_GOLD;
                break;
            }
            case "mage_leather": {
                spritesheet = Main.MAGE_LEATHER;
                break;
            }
            case "mage_improved": {
                spritesheet = Main.MAGE_IMPROVED;
                break;
            }
            case "archer_leather": {
                spritesheet = Main.ARCHER_LEATHER;
                break;
            }
            case "tank_leather": {
                spritesheet = Main.TANK_LEATHER;
                break;
            }
            case "tank_iron": {
                spritesheet = Main.TANK_IRON;
                break;
            }
            case "tank_gold": {
                spritesheet = Main.TANK_GOLD;
                break;
            }
            case "skeleton_basic": {
                spritesheet = Main.SKELETON_BASIC;
                break;
            }
            case "skeleton_leather": {
                spritesheet = Main.SKELETON_LEATHER;
                break;
            }
            case "skeleton_chain": {
                spritesheet = Main.SKELETON_CHAIN;
                break;
            }
            case "ice_elf": {
                spritesheet = Main.ICE_ELF;
                break;
            }
        }
        return spritesheet;
    }

    /*
     Selects and starts the appropriate animation sequence for the specified sprites action
    */
    public Animation selectAnimation(String action) {
        int row = 0;        // sprite sheet y
        int startx = 0;
        int endx = 0;
        int spritesize = 64;
        switch (action) {
            case "spell_up": {
                row = 0;
                startx = 0;
                endx = 6;
                break;
            }
            case "spell_left": {
                row = 1;
                startx = 0;
                endx = 6;
                break;
            }
            case "spell_down": {
                row = 2;
                startx = 0;
                endx = 6;
                break;
            }
            case "spell_right": {
                row = 3;
                startx = 0;
                endx = 6;
                break;
            }
            case "jab_up": {
                row = 4;
                startx = 0;
                endx = 7;
                break;
            }
            case "jab_left": {
                row = 5;
                startx = 0;
                endx = 7;
                break;
            }
            case "jab_down": {
                row = 6;
                startx = 0;
                endx = 7;
                break;
            }
            case "jab_right": {
                row = 7;
                startx = 0;
                endx = 7;
                break;
            }
            case "walk_up": {
                row = 8;
                startx = 0;
                endx = 8;
                break;
            }
            case "walk_left": {
                row = 9;
                startx = 0;
                endx = 8;
                break;
            }
            case "walk_down": {
                row = 10;
                startx = 0;
                endx = 8;
                break;
            }
            case "walk_right": {
                row = 11;
                startx = 0;
                endx = 8;
                break;
            }
            case "slash_up": {
                row = 12;
                startx = 0;
                endx = 5;
                break;
            }
            case "slash_left": {
                row = 13;
                startx = 0;
                endx = 5;
                break;
            }
            case "slash_down": {
                row = 14;
                startx = 0;
                endx = 5;
                break;
            }
            case "slash_right": {
                row = 15;
                startx = 0;
                endx = 5;
                break;
            }
            case "shoot_up": {
                row = 16;
                startx = 0;
                endx = 12;
                break;
            }
            case "shoot_left": {
                row = 17;
                startx = 0;
                endx = 12;
                break;
            }
            case "shoot_down": {
                row = 18;
                startx = 0;
                endx = 12;
                break;
            }
            case "shoot_right": {
                row = 19;
                startx = 0;
                endx = 12;
                break;
            }
            case "die": {
                row = 20;
                startx = 0;
                endx = 5;
                break;
            }
        }
        animation = new Animation(ResourceManager.getSpriteSheet(spritesheet, spritesize, spritesize), startx, row, endx, row, true, speed, true);
        animation.setLooping(true);
        addAnimation(animation);
        return animation;
    }

//     stop the animation
    public void stop() {
        animation.stop();
    }

    // resume the animation
    public void start() {
        animation.start();
    }

    // check if the animation is active
    public boolean isActive() {
        return !animation.isStopped();
    }

    @Override
    public float getX() {
        return super.getX();
    }

    @Override
    public float getY() {
        return super.getY() + 24;
    }

    /*
    draw all the animations for all the characters on seperate rows for testing
    @param dc The games main class
    */
//    public static void testAllCharacterAnimations(client.Main dc) {
//        String[] sprites = new String[] {
//                "knight_leather", "knight_iron", "knight_gold",
//                "mage_leather", "mage_improved",
//                "archer_leather",
//                "tank_leather", "tank_iron", "tank_gold",
//                "skeleton_basic", "skeleton_leather", "skeleton_chain",
//                "ice_elf"};
//
//        String[] limited_sprites = new String[] {
//                "knight_iron", "knight_gold",
//                "mage_leather",
//                "archer_leather",
//                "tank_leather",
//                "skeleton_basic",
//                "ice_elf"};
//
//        int row = 2;
//        for (int i = 0; i < limited_sprites.length; i++) {
//            displayAllAnimations(dc, limited_sprites[i], row );
//            row += 2;
//        }


    /*
    draw all of a single characters animations in a single row for testing
    @param dc The games main class
    @param sprite The sprite to display animations for
    @param row The screen row to draw the coordinates at
    */
//    public static void displayAllAnimations(client.Main dc, String sprite, int row) {
//        String[] variations = new String[]{
//                "spell_up", "spell_left", "spell_down", "spell_right",
//                "jab_up", "jab_left", "jab_down", "jab_right",
//                "walk_up", "walk_left", "walk_down", "walk_right",
//                "slash_up", "slash_left", "slash_down", "slash_right",
//                "shoot_up", "shoot_left", "shoot_down", "shoot_right",
//                "die"};
//        for (int i = 0; i < variations.length; i++) {
//            dc.animations.add(new client.AnimateEntity((i + 3) * dc.tilesize + dc.tilesize / 2, row * dc.tilesize, variations[i], 100, sprite));
//        }
//    }
}