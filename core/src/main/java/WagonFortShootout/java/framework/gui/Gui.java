package WagonFortShootout.java.framework.gui;

import WagonFortShootout.java.GameLevel;
import WagonFortShootout.java.entity.Entity;
import WagonFortShootout.java.entity.entities.GunEntity;
import WagonFortShootout.java.framework.image.Beam;
import WagonFortShootout.java.weapon.Gun;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Gui {

    private static final Beam beam = new Beam("green", 5, -1, 0.5f);
    private Entity player;
    private Beam.Instance reload;

    public Gui(Entity player) {
        this.player = player;
    }

    public void render(SpriteBatch spriteBatch) {
    //TODO work needs to be done here
        player = GameLevel.player;


        BitmapFont text = new BitmapFont();
        text.getData().setScale(0.5f);
        text.setColor(Color.WHITE);
        Gun.Instance gun = ((GunEntity)player).gun;
        String str = gun.bullets() + "/" + gun.maxBullets();
        text.draw(spriteBatch,str, 5,85);

        int reload = gun.reloadTimer();
        int fire = gun.fireTimer();
        Vector2 pos = player.getPos().add(-1.5f, -1.2f);
        float length = 3;
        float height = 0.5f;
        //TODO add reload bar

    }
}
