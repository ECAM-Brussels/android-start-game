package xyz.lurkin.startgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private GameView mGameView;
    private TextView mLivesView;
    private TextView mPointsView;
    private Handler frameHandler;
    private static final int FRAME_RATE = 20; //50 frames per second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mGameView = (GameView) findViewById(R.id.gameview);
        mLivesView = (TextView) findViewById(R.id.livesView);
        mPointsView = (TextView) findViewById(R.id.pointsView);

        frameHandler = new Handler();

        frame();
    }

    //Runnable that call the frame() method
    private Runnable frameUpdate = new Runnable() {
        @Override
        public void run() {
            frame();
        }
    };

    private void frame() {
        if(mGameView.isOver()){
            SharedPreferences pref = getSharedPreferences(StartActivity.SHARED_PREF_FILE, Context.MODE_PRIVATE);
            int best = pref.getInt(StartActivity.BEST_SCORE_KEY, 0);
            int score = mGameView.getScore();
            if (score > best)
                pref.edit().putInt(StartActivity.BEST_SCORE_KEY, score).commit();

            Intent i = new Intent(this, StartActivity.class);
            startActivity(i);
            finish();
        }
        else {
            mGameView.next();
            mLivesView.setText(String.format("%d", mGameView.getLives()));
            mPointsView.setText(String.format("%d", mGameView.getScore()));

            //make a new frame() call in FRAME_RATE millisecond
            frameHandler.postDelayed(frameUpdate, FRAME_RATE);
        }
    }

    //hide system UI
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mGameView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        frameHandler.removeCallbacks(frameUpdate);
    }

    @Override
    protected void onResume() {
        super.onResume();

        frame();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }
}
