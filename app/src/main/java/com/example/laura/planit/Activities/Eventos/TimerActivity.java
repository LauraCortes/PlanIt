package com.example.laura.planit.Activities.Eventos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.laura.planit.R;
import com.example.laura.planit.Services.NotificationService;

/**
 * Created by Laura on 18/09/2016.
 */
public class TimerActivity extends Activity implements View.OnClickListener {
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private Button startB;
    public TextView text;
    private final long startTime = 2 * 1000;
    private final long interval = 1 * 1000;

    private final Context context= this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        startB = (Button) this.findViewById(R.id.button);
        startB.setOnClickListener(this);
        text = (TextView) this.findViewById(R.id.timer);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        text.setText(text.getText() + String.valueOf(startTime / 1000));
    }

    @Override
    public void onClick(View v) {
        if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
            startB.setText("STOP");
        } else {
            countDownTimer.cancel();
            timerHasStarted = false;
            startB.setText("RESTART");
        }
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            text.setText("Time's up!");
            Intent intent= new Intent(context, NotificationService.class);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            text.setText("" + millisUntilFinished / 1000);
        }
    }
}
