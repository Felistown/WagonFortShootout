package WagonFortShootout.java.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashSet;

public class Sounds {

    private static final FileHandle DIRECTORY = Gdx.files.internal("sounds/");
    private static final HashSet<Sound> sounds = new HashSet<Sound>();

    public static Sound getSound(String string) {
        Sound sound = Gdx.audio.newSound(DIRECTORY.child(string));
        sounds.add(sound);
        return sound;
    }

    public static void dispose() {
        for(Sound sound: sounds) {
            sound.dispose();
            Gdx.app.log("Sound", "Sound disposed");
        }
    }
}
