package xyz.lurkin.startgame;

/**
 * Created by lur on 11-04-16.
 */
import android.graphics.PointF;
import android.util.Log;

public class Ball extends PointF{
    public float radius;
    private PointF speed;
    private static final String TAG = "Ball";

    public Ball(float x, float y, float radius, float speedX, float speedY) {
        super(x,y);

        this.radius = radius;
        speed = new PointF(speedX,speedY);
    }

    public void next() {
        x += speed.x;
        y += speed.y;
    }

    public PointF getSpeed() {
        return speed;
    }

    public void verticalHit() {
        speed.x = -speed.x;
        Log.v(TAG, "verticalHit");
    }

    public void horizontalHit() {
        speed.y = -speed.y;
        Log.v(TAG, "horizontalHit");
    }


}
