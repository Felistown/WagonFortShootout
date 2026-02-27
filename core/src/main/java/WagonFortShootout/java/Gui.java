package WagonFortShootout.java;

import WagonFortShootout.java.effects.Beam;
import WagonFortShootout.java.entity.entities.Player;
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
        BitmapFont text = new BitmapFont();
        text.setColor(Color.WHITE);
        Gun.Instance gun = player.getGun();
        String str = gun.bullets() + "/" + gun.maxBullets();
        text.draw(spriteBatch,str, 5,15);

        int reload = gun.reloadTimer();
        int fire = gun.fireTimer();
        Vector2 pos = player.getPOS().pos().add(-1.5f, -1.2f);
        float length = 3;
        float height = 0.5f;

        if(fire > 0) {
            Beam.beam(pos, new Vector2(pos.x + length * (1 - ((float) fire/ gun.fireRate())), pos.y), height, 1, Color.WHITE);
        } else if(reload > 0) {
            Beam.beam(pos, new Vector2(pos.x + length * (1- ((float) reload / gun.reloadRate())), pos.y), height, 1, Color.WHITE);
        }

    }
}
