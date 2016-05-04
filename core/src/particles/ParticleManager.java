package particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by a2558 on 5/2/2016.
 */
public class ParticleManager {
    private ParticleManager(){
        effects = new ArrayList<ParticleEffect>();
    }

    private static ParticleManager inst;

    public static ParticleManager Inst(){
        if (inst == null)
            inst = new ParticleManager();
        return inst;
    }
    private ArrayList<ParticleEffect> effects;

    public void createEffect(String file, Vector2 position){
        ParticleEffect pe = new ParticleEffect();
        pe.load(Gdx.files.internal("particles/" + file), Gdx.files.internal(""));
        pe.setPosition(position.x, position.y);
        pe.start();
        effects.add(pe);
    }

    public void update(){
        for (ParticleEffect p : effects) {
            p.update(Gdx.graphics.getDeltaTime());
        }
    }
    public void render(SpriteBatch batch){
        for (ParticleEffect p : effects) {
            p.draw(batch);
        }
    }

    public void reset() {
        for (ParticleEffect p : effects) {
            p.dispose();
        }
        effects.clear();
    }
}
