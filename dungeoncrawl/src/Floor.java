import jig.Entity;
import jig.ResourceManager;

public class Floor extends Entity {

    public Floor(final float x, final float y, String type) {
//public Floor(final float x, final float y) {
        super(x, y);
//    addImageWithBoundingBox(ResourceManager.getImage(Game.FLOOR));
        switch (type) {
            case "normal": {
                addImageWithBoundingBox(ResourceManager.getImage(Main.FLOOR));
                break;
            }
            case "shadow": {
                addImageWithBoundingBox(ResourceManager.getImage(Main.SHADOW_FLOOR));
                break;
            }
            case "shadow_right": {
                addImageWithBoundingBox(ResourceManager.getImage(Main.SHADOW_FLOOR_R));
                break;
            }
            case "iso": {
                addImageWithBoundingBox(ResourceManager.getImage(Main.ISOFLOOR));
                break;
            }
        }
    }

}
