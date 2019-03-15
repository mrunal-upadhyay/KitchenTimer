package com.mrunal.kitchentimer;

import android.os.CountDownTimer;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<State> state = new MutableLiveData<>();
    private MutableLiveData<Boolean> validationSuccess = new MutableLiveData<>();
    private MutableLiveData<Long> remainingSeconds = new MutableLiveData<>();
    private long time = 60*1000;
    private CountDownTimer countDownTimer;


    public LiveData<State> getState() {
        return state;
    }

    public LiveData<Boolean> getValidationSuccess() {
        return validationSuccess;
    }

    public LiveData<Long> getRemainingSeconds() {
        return remainingSeconds;
    }

    public void performValidation(String rawTime) {
        String[] tmp = rawTime.split(":");
        try{
            time = (Integer.parseInt(tmp[0]) * 60 + Integer.parseInt(tmp[1])) * 1000;
        } catch(Exception ex) {
            validationSuccess.postValue(false);
        }
        validationSuccess.postValue(true);
    }

    public void performClick(String rawTime){
        if(state.getValue() != State.STARTED) {
            performValidation(rawTime);
            countDownTimer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingSeconds.postValue(millisUntilFinished/1000);

//                long minutes = remainingSeconds/60;
//                long seconds = remainingSeconds%60;
                }

                @Override
                public void onFinish() {
                    state.postValue(State.FINISHED);
//                timeEt.setText(R.string.default_time);
//                startBtn.setText(R.string.start);
//                state = false;
//                new AlertDialog.Builder(MainActivity.this)
//                        .setTitle(R.string.app_name)
//                        .setMessage(R.string.lets_eat)
//                        .show();
//
//
//                HANDLER.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        timeEt.setText(R.string.default_time);
//                    }
//                }, 1500);
                }
            };
            countDownTimer.start();
            state.postValue(State.STARTED);
        } else {
            if(countDownTimer!=null) {
                countDownTimer.cancel();
                state.postValue(State.STOPPED);
            }
        }
    }


}
