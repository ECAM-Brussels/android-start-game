package xyz.lurkin.startgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
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
    private Bitmap mBallImage;
    private int mScore = 0;
    private int mLives = 3;
    private boolean mOver = false;
    private static final String TAG = "GameView";


    public GameView(Context context, AttributeSet aSet) {
        super(context, aSet);

        mPaint = new Paint();
        mPaint.setTextSize(50);
        mBall = new Ball(100, 500, 70, 10, 10);
        mPad = new Pad(100, 50, 200);

        mBallImage = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBallImage = Bitmap.createScaledBitmap(mBallImage, 140, 140, true);

        Log.v(TAG, "Game created");
    }

    public void next() {
        if(!mOver && getWidth()!= 0) {   //nothing to do if the view size is not calculated
            if (mBall.x + mBall.radius > getWidth() || mBall.x - mBall.radius < 0)
                mBall.verticalHit();
            if (mBall.y - mBall.radius < 0) {
                mBall.y = getHeight() - 100;
                mLives--;
                Log.v(TAG, "live lost");
                if (mLives == 0) mOver = true;
            }
            if (mBall.y + mBall.radius > getHeight())
                mBall.horizontalHit();
            if (mBall.y - mBall.radius < mPad.y && mBall.x > mPad.x - mPad.width / 2 && mBall.x < mPad.x + mPad.width / 2) {
                mBall.horizontalHit();
                mScore++;
                Log.v(TAG, "1 point");
            }
            mBall.next();
            mPad.next(mCommand);
            invalidate();
        }
    }

    @Override
    synchronized public void onDraw(Canvas canvas) {
        canvas.drawRect(mPad.x-mPad.width/2,mPad.y-15, mPad.x+mPad.width/2, mPad.y,mPaint);
        canvas.drawBitmap(mBallImage, mBall.x-mBall.radius, mBall.y-mBall.radius, mPaint);
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

    public boolean isOver() {
        return mOver;
    }

    public int getScore() {
        return mScore;
    }

    public int getLives() {
        return mLives;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        Bundle state = new Bundle();

        state.putParcelable("super", superState);

        state.putFloat("ballX", mBall.x);
        state.putFloat("ballY", mBall.y);
        state.putFloat("ballSpeedX", mBall.getSpeed().x);
        state.putFloat("ballSpeedY", mBall.getSpeed().y);
        state.putFloat("ballRadius", mBall.radius);
        state.putFloat("padX", mPad.x);
        state.putFloat("padY", mPad.y);
        state.putFloat("padWidth", mPad.width);
        state.putInt("score", mScore);
        state.putInt("lives", mLives);

        Log.v(TAG, "State Saved");
        Log.v(TAG, "ball speed: ("+mBall.getSpeed().x+", "+mBall.getSpeed().y+")");

        return state;
    }

    @Override
    public void onRestoreInstanceState(Parcelable s) {
        Bundle state = (Bundle) s;

        super.onRestoreInstanceState(state.getParcelable("super"));

        mBall = new Ball(state.getFloat("ballX"), state.getFloat("ballY"), state.getFloat("ballRadius"), state.getFloat("ballSpeedX"), state.getFloat("ballSpeedY"));
        mPad = new Pad(state.getFloat("padX"), state.getFloat("padY"), state.getFloat("padWidth"));
        mScore = state.getInt("score");
        mLives = state.getInt("lives");

        Log.v(TAG, "State Restored");
        Log.v(TAG, "ball speed: ("+mBall.getSpeed().x+", "+mBall.getSpeed().y+")");
    }
}
