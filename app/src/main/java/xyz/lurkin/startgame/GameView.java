package xyz.lurkin.startgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lur on 11-04-16.
 */
public class GameView extends View {

    private Paint mPaint;
    private Ball mBall;
    private Pad mPad;
    private float mCommand = 100;


    public GameView(Context context, AttributeSet aSet) {
        super(context, aSet);

        mPaint = new Paint();
        mBall = new Ball(100, 100, 10, 10, 10);
        mPad = new Pad(100, 50, 200);
    }

    public void next() {
        if(mBall.x+mBall.radius > getWidth() || mBall.x-mBall.radius < 0)
            mBall.verticalHit();
        if(mBall.y-mBall.radius < 0)
            mBall.y = getHeight()-100;
        if(mBall.y+mBall.radius > getHeight())
            mBall.horizontalHit();
        if(mBall.y < mPad.y && mBall.x > mPad.x-mPad.width/2 && mBall.x < mPad.x+mPad.width/2)
            mBall.horizontalHit();
        mBall.next();
        mPad.next(mCommand);
        invalidate();
    }

    @Override
    synchronized public void onDraw(Canvas canvas) {
        canvas.drawCircle(mBall.x, mBall.y, mBall.radius, mPaint);
        canvas.drawRect(mPad.x-mPad.width/2,mPad.y-15, mPad.x+mPad.width/2, mPad.y,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // int action = event.getActionMasked();
        // action can be:
        //    MotionEvent.ACTION_DOWN
        //    MotionEvent.ACTION_POINTER_DOWN
        //    MotionEvent.ACTION_MOVE
        //    MotionEvent.ACTION_POINTER_UP
        //    MotionEvent.ACTION_UP
        //    ...

        // int index = event.getActionIndex();
        // int id = event.getPointerId(index);
        // index = event.findPointerIndex(id);

        mCommand = event.getX();
        // mCommand = event.getX(index);

        return true;
    }
}
