package particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.wordblocks.gdx.BuildConfig;
import java.util.ArrayList;
import java.util.Iterator;
import utils.Utils;

public class ParticleManager {
    private static ParticleManager inst;
    private ArrayList<WBParticleEffect> effects = new ArrayList();

    public class WBParticleEffect extends ParticleEffect {
        public boolean isBackground;
    }

    private ParticleManager() {
    }

    public static ParticleManager Inst() {
        if (inst == null) {
            inst = new ParticleManager();
        }
        return inst;
    }

    public void createEffect(String file, Vector2 position, boolean isBackground) {
        WBParticleEffect pe = new WBParticleEffect();
        pe.load(Gdx.files.internal("particles/" + file), Gdx.files.internal(BuildConfig.FLAVOR));
        pe.setPosition(position.x, position.y);
        pe.isBackground = isBackground;
        pe.start();
        this.effects.add(pe);
    }

    public void update() {
        for (int i = this.effects.size() - 1; i >= 0; i--) {
            ((WBParticleEffect) this.effects.get(i)).update(Gdx.graphics.getDeltaTime());
            if (((WBParticleEffect) this.effects.get(i)).isComplete()) {
                ((WBParticleEffect) this.effects.get(i)).dispose();
                this.effects.remove(i);
            }
        }
    }

    public void fastUpdate() {
        for (int i = this.effects.size() - 1; i >= 0; i--) {
            if (((WBParticleEffect) this.effects.get(i)).isBackground) {
                ((WBParticleEffect) this.effects.get(i)).update(0.017f);
            }
        }
    }

    public void renderBackground(SpriteBatch batch) {
        Iterator it = this.effects.iterator();
        while (it.hasNext()) {
            WBParticleEffect p = (WBParticleEffect) it.next();
            if (p.isBackground) {
                p.draw(batch);
            }
        }
    }

    public void renderForeground(SpriteBatch batch) {
        Iterator it = this.effects.iterator();
        while (it.hasNext()) {
            WBParticleEffect p = (WBParticleEffect) it.next();
            if (!p.isBackground) {
                p.draw(batch);
            }
        }
    }

    public void reset() {
        Iterator it = this.effects.iterator();
        while (it.hasNext()) {
            ((ParticleEffect) it.next()).dispose();
        }
        this.effects.clear();
    }

    public void startRandomBackgroundEffect() {
        reset();
        switch (Utils.getRandomInt(3)) {
            case 0:
                createEffect("fire.txt", new Vector2(200.0f, 939.6476f), true);
                break;
            case 1:
                createEffect("snow.txt", new Vector2(0.0f, 2133.0f), true);
                break;
            case 2:
                createEffect("stars.txt", new Vector2(600.0f, 1422.0f), true);
                break;
        }
        for (int i = 0; i < 1000; i++) {
            fastUpdate();
        }
    }
}
