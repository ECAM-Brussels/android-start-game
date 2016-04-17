package xyz.lurkin.startgame;

import android.graphics.PointF;

/**
 * Created by lur on 17-04-16.
 */
public class Pad extends PointF {
    public float width;

    public Pad(float x, float y, float width) {
        super(x,y);

        this.width = width;
    }

    public void next(float command) {
        x = command;
    }
}
