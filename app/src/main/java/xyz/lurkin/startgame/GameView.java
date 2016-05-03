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
        SavedState ss = new SavedState(superState);

        ss.state.putFloat("ballX", mBall.x);
        ss.state.putFloat("ballY", mBall.y);
        ss.state.putFloat("ballSpeedX", mBall.getSpeed().x);
        ss.state.putFloat("ballSpeedY", mBall.getSpeed().y);
        ss.state.putFloat("ballRadius", mBall.radius);
        ss.state.putFloat("padX", mPad.x);
        ss.state.putFloat("padY", mPad.y);
        ss.state.putFloat("padWidth", mPad.width);
        ss.state.putInt("score", mScore);
        ss.state.putInt("lives", mLives);

        Log.v(TAG, "State Saved");
        Log.v(TAG, "ball speed: ("+mBall.getSpeed().x+", "+mBall.getSpeed().y+")");

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mBall = new Ball(ss.state.getFloat("ballX"), ss.state.getFloat("ballY"), ss.state.getFloat("ballRadius"), ss.state.getFloat("ballSpeedX"), ss.state.getFloat("ballSpeedY"));
        mPad = new Pad(ss.state.getFloat("padX"), ss.state.getFloat("padY"), ss.state.getFloat("padWidth"));
        mScore = ss.state.getInt("score");
        mLives = ss.state.getInt("lives");

        Log.v(TAG, "State Restored");
        Log.v(TAG, "ball speed: ("+mBall.getSpeed().x+", "+mBall.getSpeed().y+")");
    }

    static class SavedState extends BaseSavedState {
        Bundle state;

        SavedState(Parcelable superState) {
            super(superState);
            state = new Bundle();
        }

        private SavedState(Parcel in) {
            super(in);
            state = in.readBundle();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(state);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
