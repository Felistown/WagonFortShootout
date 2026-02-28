package WagonFortShootout.java.entity.entities;

import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Enemy extends Entity {
    public Enemy(Vector2 pos) {
        super(pos, new Sprite(new Texture("circle.png")), 1, 5,"semi_rifle");
    }
}
