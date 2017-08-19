package Answers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.wordblocks.gdx.Game.Answer;
import java.util.ArrayList;

public class AnswerHelpers {
    static GlyphLayout glyphLayout = new GlyphLayout();

    public static float getFontScaleForNumWords(int count) {
        switch (count) {
            case 1:
                return 1.3f;
            case 2:
                return 1.1f;
            case 3:
            case 6:
                return 0.9f;
            case 4:
            case 7:
            case 8:
                return 0.7f;
            default:
                return 0.65f;
        }
    }

    public static Vector2 getPosition(String word, int index, int total, BitmapFont font, Rectangle view) {
        float scale = getFontScaleForNumWords(total);
        font.getData().setScale(scale, scale);
        Vector2 pos = getCenterPosition(word, index, total, view);
        glyphLayout.setText(font, word);
        pos.x -= glyphLayout.width / 2.0f;
        pos.y += glyphLayout.height / 2.0f;
        return pos;
    }

    public static Vector2 getCenterPosition(String word, int index, int total, Rectangle view) {
        Vector2 pos = new Vector2();
        float scale = getFontScaleForNumWords(total);
        switch (total) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                float div = view.height / (((float) total) * 2.0f);
                pos.x = view.x + (view.width / 2.0f);
                pos.y = view.y + (((float) (((total - index) * 2) - 1)) * div);
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                int newTotal = total / 2;
                if (total % 2 == 1) {
                    newTotal++;
                }
                pos.y = view.y + (((float) (((newTotal - (index % newTotal)) * 2) - 1)) * (view.height / (((float) newTotal) * 2.0f)));
                if (index >= newTotal) {
                    pos.x = view.x + ((3.0f * view.width) / 4.0f);
                    break;
                }
                pos.x = view.x + (view.width / 4.0f);
                break;
        }
        return pos;
    }

    public static void drawLetters(SpriteBatch spriteBatch, BitmapFont fontAnswers, Vector2 centerPos, float scale, String word) {
        fontAnswers.getData().setScale(0.55f * scale, 0.6f * scale);
        for (int i = 0; i < word.length(); i++) {
            Rectangle pos = getBlockPosition(centerPos, scale, i, word);
            float letterX = pos.x + (pos.width / 2.0f);
            float letterY = pos.y + (pos.height / 2.0f);
            CharSequence curLetter = word.substring(i, i + 1);
            glyphLayout.setText(fontAnswers, curLetter);
            fontAnswers.draw((Batch) spriteBatch, curLetter, letterX - (glyphLayout.width / 2.0f), letterY + (glyphLayout.height / 2.0f));
        }
    }

    public static void drawBlocks(ShapeRenderer shapeRenderer, Vector2 centerPos, float scale, String word) {
        for (int i = 0; i < word.length(); i++) {
            Rectangle pos = getBlockPosition(centerPos, scale, i, word);
            shapeRenderer.rect(pos.x, pos.y, pos.width, pos.height);
        }
    }

    public static Rectangle getBlockPosition(Vector2 wordCenter, float scale, int block, String word) {
        float blockWidth = scale * 85.0f;
        int padding = (int) (3.0f * scale);
        float startX = wordCenter.x - ((float) (((int) (((float) word.length()) * blockWidth)) / 2));
        float startY = wordCenter.y - ((float) (((int) (((float) (padding * 2)) + blockWidth)) / 2));
        Rectangle blockPos = new Rectangle();
        blockPos.x = (((float) padding) + startX) + (((float) block) * blockWidth);
        blockPos.y = ((float) padding) + startY;
        blockPos.width = blockWidth - ((float) (padding * 2));
        blockPos.height = blockWidth - ((float) (padding * 2));
        return blockPos;
    }

    public static int getWordIndex(String selectedWord, ArrayList<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            if (((Answer) answers.get(i)).word.equals(selectedWord)) {
                return i;
            }
        }
        return -100;
    }
}
