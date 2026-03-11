package WagonFortShootout.java.framework.gui;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.effects.Beam;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.entities.Player;
import WagonFortShootout.java.entity.generic.Mount;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Gui {

    private Player player;

    public Gui(Player player) {
        this.player = player;
    }

    public void render(SpriteBatch spriteBatch) {
    //TODO work needs to be done here
        player = GameLevel.player;
        renderHealth(spriteBatch);


        BitmapFont text = new BitmapFont();
        text.getData().setScale(0.5f);
        text.setColor(Color.WHITE);
        Gun.Instance gun = player.gun;
        String str = gun.bullets() + "/" + gun.maxBullets();
        text.draw(spriteBatch,str, 5,85);

        int reload = gun.reloadTimer();
        int fire = gun.fireTimer();
        Vector2 pos = player.getPos().add(-1.5f, -1.2f);
        float length = 3;
        float height = 0.5f;

        if(fire > 0) {
            Beam.beam(pos, new Vector2(pos.x + length * (1 - ((float) fire/ gun.fireRate())), pos.y), height, 1, Color.WHITE);
        } else if(reload > 0) {
            Beam.beam(pos, new Vector2(pos.x + length * (1- ((float) reload / gun.reloadRate())), pos.y), height, 1, Color.WHITE);
        }

    }

    public void renderHealth(SpriteBatch spriteBatch) {
        float length = 3;
        float height = 0.5f;
        for(Entity e: Entity.getAllEntities()) {
            if(e instanceof Mount m && m.getMounter() != null) {
            } else{
                //TODO mount health is not show at all
                Vector2 pos = e.getPos().add(-1.5f, 1.2f);
                Beam.beam(pos, new Vector2(pos.x + length, pos.y), height, 1, Color.RED);
                Beam.beam(pos, new Vector2(pos.x + length * ((float) e.getHealth() / e.MAX_HEALTH), pos.y), height, 1, Color.GREEN);
            }
        }
    }
}
