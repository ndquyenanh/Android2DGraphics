package graphics.example.demo.android2dgraphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sev_user on 27-Nov-14.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    Ball mBall;
    GameThread mGameThread;

    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mBall = new Ball(getResources(), 0, 0, getWidth(), getHeight());
        mBall.setVelocity(5, 5);

        if (!mGameThread.isAlive()) {
            mGameThread = new GameThread();
            mGameThread.setRun(true);
            mGameThread.start();
        }
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mGameThread.isAlive()) {
            mGameThread.setRun(false);
        }
    }

    void changeGamePanel() {
        synchronized (mBall) {
            mBall.moveBall();
        }
    }

    void drawGamePanel(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        synchronized (mBall) {
            mBall.drawBall(canvas);
        }
    }

    Ball newBall;
    float lastX, lastY;
    List<Ball> mBalls = new ArrayList<>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();

                synchronized (mBalls) {

                    newBall = new Ball(getResources(), lastX, lastY);
                    newBall.setPosition(lastX, lastY);
                    newBall.setVelocity(0, 0);
                    mBalls.add(newBall);
                }

                return true;
        }
        return super.onTouchEvent(event);
    }

    class GameThread extends Thread {

        SurfaceHolder mHolder;
        boolean isRun = false;

        /**
         * Constructs a new {@code Thread} with no {@code Runnable} object and a
         * newly generated name. The new {@code Thread} will belong to the same
         * {@code ThreadGroup} as the {@code Thread} calling this constructor.
         *
         * @see ThreadGroup
         * @see Runnable
         */
        GameThread() {
            mHolder = getHolder();
        }

        public void setRun(boolean run) {
            isRun = run;
        }

        @Override
        public void run() {
            super.run();

            Canvas canvas = null;
            while (isRun) {

                canvas = mHolder.lockCanvas();

                if (canvas != null) {
                    changeGamePanel();
                    drawGamePanel(canvas);
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
