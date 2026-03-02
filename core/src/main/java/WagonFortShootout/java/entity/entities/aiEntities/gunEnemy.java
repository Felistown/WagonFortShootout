package WagonFortShootout.java.entity.entities.aiEntities;

import WagonFortShootout.java.entity.entities.aiEntity;
import WagonFortShootout.java.framework.ai.Ai;
import WagonFortShootout.java.framework.ai.gunEnemyAi;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class gunEnemy extends aiEntity {

    public gunEnemy(Vector2 pos) {
        super(pos, "image/circle.png", 100,3, 5,"lever_rifle", "gun_enemy");
    }

}
