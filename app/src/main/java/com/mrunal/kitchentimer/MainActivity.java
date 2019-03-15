package com.mrunal.kitchentimer;

import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity {

    private EditText timeEt;
    private Button startBtn;
    private static final Handler HANDLER = new Handler();
    private MainActivityViewModel viewModel;

    private void subscribeValidation() {
        final Observer<Boolean> validationObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean) {
                    timeEt.setText(R.string.default_time);
                }
            }
        };
        viewModel.getValidationSuccess().observe(this,validationObserver);
    }

    private void subscribeState() {
        final Observer<State> stateObserver = new Observer<State>() {
            @Override
            public void onChanged(State state) {
                switch(state) {
                    case STARTED:
                        startBtn.setText(R.string.stop);
                        timeEt.setCursorVisible(false);
                        timeEt.setEnabled(false);
                        subscribeRemainingSeconds();
                        break;
                    case STOPPED:
                        startBtn.setText(R.string.start);
                        timeEt.setCursorVisible(true);
                        timeEt.setEnabled(true);
                        unsubscribeRemainingSeconds();
                        break;
                    case FINISHED:
                        unsubscribeRemainingSeconds();
                        timeEt.setText(R.string.default_time);
                        timeEt.setCursorVisible(true);
                        timeEt.setEnabled(true);
                        startBtn.setText(R.string.start);
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
                        break;
                }

            }
        };
        viewModel.getState().observe(this,stateObserver);
    }

    private void unsubscribeRemainingSeconds() {
        viewModel.getRemainingSeconds().removeObservers(this);
    }

    private void subscribeRemainingSeconds() {
        Observer<Long> remainingSecondsObserver = new Observer<Long>() {
            @Override
            public void onChanged(Long remainingSeconds) {
                long minutes = remainingSeconds/60;
                long seconds = remainingSeconds%60;
                timeEt.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : seconds));
            }
        };
        viewModel.getRemainingSeconds().observe(this,remainingSecondsObserver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        timeEt = findViewById(R.id.timeEt);
        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.performClick(timeEt.getText().toString());
            }
        });
        subscribeValidation();
        subscribeState();
        subscribeRemainingSeconds();
    }
}
