package com.mrunal.kitchentimer;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText timeEt;
    private Button startBtn;
    private boolean started;
    private CountDownTimer countDownTimer;
    private static final Handler HANDLER = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeEt = findViewById(R.id.timeEt);
        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(started) {
                    started = false;
                    startBtn.setText(R.string.start);
                    timeEt.setCursorVisible(true);
                    countDownTimer.cancel();
                } else {
                    started = true;
                    startBtn.setText(R.string.stop);
                    timeEt.setCursorVisible(false);
                    timeEt.setFocusable(false);
                    String rawTime = timeEt.getText().toString();
                    String[] tmp = rawTime.split(":");

                    long time = 60*1000;


                    try{
                        time = (Integer.parseInt(tmp[0]) * 60 + Integer.parseInt(tmp[1])) * 1000;
                    } catch(Exception ex) {
                        timeEt.setText(R.string.default_time);
                    }

                    countDownTimer = new CountDownTimer(time, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            long remainingSeconds = millisUntilFinished/1000;
                            long minutes = remainingSeconds/60;
                            long seconds = remainingSeconds%60;
                            timeEt.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : seconds));
                        }

                        @Override
                        public void onFinish() {
                            timeEt.setText(R.string.default_time);
                            startBtn.setText(R.string.start);
                            started = false;
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(R.string.app_name)
                                    .setMessage(R.string.lets_eat)
                                    .show();


                            HANDLER.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    timeEt.setText(R.string.default_time);
                                }
                            }, 1500);
                        }
                    };
                    countDownTimer.start();
                }
            }
        });
    }
}
