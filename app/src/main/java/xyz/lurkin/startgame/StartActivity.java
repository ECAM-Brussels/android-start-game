package xyz.lurkin.startgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {
    static public final String SHARED_PREF_FILE = "xyz.lurkin.startgame.DATAFILE";
    static public final String BEST_SCORE_KEY = "best_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences pref = getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        int best = pref.getInt(BEST_SCORE_KEY, 0);   //default value = 0

        TextView bestView = (TextView) findViewById(R.id.bestView);
        bestView.setText(String.format("%d", best));
    }

    public void startClick(View v) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        finish();
    }
}
