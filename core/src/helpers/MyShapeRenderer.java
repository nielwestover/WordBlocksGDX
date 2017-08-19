package helpers;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MyShapeRenderer extends ShapeRenderer {
    public void roundedRect(float x, float y, float width, float height, float radius) {
        super.rect(x + radius, y + radius, width - (2.0f * radius), height - (2.0f * radius));
        super.rect(x + radius, y, width - (2.0f * radius), radius);
        super.rect((x + width) - radius, y + radius, radius, height - (2.0f * radius));
        super.rect(x + radius, (y + height) - radius, width - (2.0f * radius), radius);
        super.rect(x, y + radius, radius, height - (2.0f * radius));
        super.arc(x + radius, y + radius, radius, 180.0f, 90.0f);
        super.arc((x + width) - radius, y + radius, radius, 270.0f, 90.0f);
        super.arc((x + width) - radius, (y + height) - radius, radius, 0.0f, 90.0f);
        super.arc(x + radius, (y + height) - radius, radius, 90.0f, 90.0f);
    }
}
