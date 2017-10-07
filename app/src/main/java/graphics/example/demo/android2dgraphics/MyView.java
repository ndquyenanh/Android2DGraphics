package graphics.example.demo.android2dgraphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sev_user on 27-Nov-14.
 */
public class MyView extends View {

    int x = 0;
    int y = 0;
    int vx = 1;
    int vy = 1;
    int diameter = 30;

    int count = 10;

    TimeCountThread mTimeCountThread = new TimeCountThread();

    OnCallback mOnCallback;

    public void setmOnCallback(OnCallback onCallback){
        mOnCallback = onCallback;
    }

    public interface OnCallback{
        void removeTextview();
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     * @see #View(android.content.Context, android.util.AttributeSet, int)
     */
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTimeCountThread.start();
    }

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public MyView(Context context) {
        super(context);

        mTimeCountThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint cpPaint = new Paint();
        cpPaint.setStrokeWidth(20);
        cpPaint.setTextSize(50);
        cpPaint.setColor(Color.RED);

        if (count > 0) {
            canvas.drawText("Time remain: " + count, 45, 220, cpPaint);

            Path cPath = new Path();
            cPath.addCircle(200, 200, 200, Path.Direction.CW);

            canvas.drawPath(cPath,cpPaint);

            Paint bPaint = new Paint();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            canvas.drawBitmap(bitmap, x, y, bPaint);
            moveBall();
        } else {
            mOnCallback.removeTextview();
            canvas.drawText("You lost", 225, 220, cpPaint);
        }
    }

    void moveBall() {

        x += vx;
        y += vy;

        if (x <= 0 || x >= getWidth() - diameter) {
            vx = -vx;
        }

        if (y <= 0 || y >= getHeight() - diameter) {
            vy = -vy;
        }

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                Log.v("Move", "  X = " + x);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                invalidate();
                Log.v("Move", "X = " + x);
                break;
        }
        return true;
    }

    class TimeCountThread extends Thread {

        /**
         * Constructs a new {@code Thread} with no {@code Runnable} object and a
         * newly generated name. The new {@code Thread} will belong to the same
         * {@code ThreadGroup} as the {@code Thread} calling this constructor.
         *
         * @see ThreadGroup
         * @see Runnable
         */
        TimeCountThread() {
        }

        @Override
        public void run() {
            super.run();

            while (count > 0) {

                try {
                    count--;
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
