package android.benedetto.com.stopwatch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;

public class StopwatchActivity extends Activity {

    // keep track of seconds since start was pressed
    private int seconds = 0;
    // is the clock running? to I keep counting?
    private boolean running = false;
    private boolean wasRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
    }

    // replacing the onStart with onResume so that the same code covers both the case of
    // a resume AND a start
    @Override
    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    // same as above: onPause is called irrespective of whether the activity is paused or stopped
    // so we can use the same code - remove onStop and replace it with an onPaude
    @Override
    protected void onPause() {
        super.onPause();
        // stop the stopwatch
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("running", running);
        outState.putBoolean("wasRunning", wasRunning);
    }

    public void onClickStart(View view) {
        running = true;
        wasRunning = true;
    }

    public void onClickStop(View view) {
        running = false;
        wasRunning = false;
    }

    public   void onClickReset(View view) {
        running = false;
        wasRunning = false;
        seconds = 0;
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, secs);
                timeView.setText(time);
                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
}

// a g d h f b d
// a g d b e h a g d b e c g d
// onCreate onStart onResume onPause onStop onDestroy onCreate onStart onResume onPause onStop
// onRestart onStart onResume