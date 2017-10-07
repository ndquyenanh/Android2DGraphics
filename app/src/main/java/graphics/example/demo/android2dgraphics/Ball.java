package graphics.example.demo.android2dgraphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by sev_user on 27-Nov-14.
 */
public class Ball {

    float gamePanelWidth = 0;
    float getGamePanelHeight = 0;

    float x, y;
    float diameter;

    float vx, vy;

    Bitmap mBitmap;

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Ball(Resources resources, float gamePanelWidth, float getGamePanelHeight, float x, float y) {

        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ball);
        diameter = mBitmap.getWidth();

        this.gamePanelWidth = gamePanelWidth;
        this.getGamePanelHeight = getGamePanelHeight;
        this.x = x;
        this.y = y;
    }

    public Ball(Resources resources, float x, float y) {
        this.x = x;
        this.y = y;
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ball);
    }

    public void drawBall(Canvas canvas) {
        canvas.drawBitmap(mBitmap, x, y, null);
    }

    public void moveBall() {
        x += vx;
        y += vy;
        collision();
    }

    public void setVelocity(float vx, float vy) {
        this.vx = vx;
        this.vy = vy;
    }

    void collision() {
        if (x <= 0 || x >= gamePanelWidth - diameter) {
            vx = -vx;
        }

        if (y <= 0 || y >= getGamePanelHeight - diameter) {
            vy = -vy;
        }
    }
}
