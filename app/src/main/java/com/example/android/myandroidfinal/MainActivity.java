package com.example.android.myandroidfinal;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "MainActivity";

    private long timeCountInMilliSeconds = 1 * 60000;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setTimer();
    }

    private enum TimerStatus {
        STARTED,
        STOPPED,
    }

    int breakCount = 0;
    boolean breakOrWork = false;
    private TimerStatus timerStatus = TimerStatus.STOPPED;
    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private ImageView imageViewTomato, imageViewWork, imageViewBreak;
    // private ImageView imageViewPomodora1, imageViewPomodora2, imageViewPomodora3, imageViewPomodora4;
    private CountDownTimer countDownTimer;
    private SharedPreferences settings;

    //hamburger menu
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    ImageView imageViewPic;
    TextView textViewName,textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        // method call to initialize the listeners
        initListeners();
        //method call to initialize the settings menu item
        setTimer();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_task:
                        Intent intent1 = new Intent(MainActivity.this, TaskListActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_timer:

                        break;

                    case R.xml.settings:
                        Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });

    }


    private void setTimer() {
        Long work = Long.valueOf(Integer.parseInt("25") * 60000);
        textViewTime.setText(hmsTimeFormatter(work));
    }


    private void initViews() {
        progressBarCircle = findViewById(R.id.progressBarCircle);
        textViewTime = findViewById(R.id.textViewTime);
        imageViewReset = findViewById(R.id.imageViewReset);
        imageViewStartStop = findViewById(R.id.imageViewStartStop);
        imageViewTomato = findViewById(R.id.imageViewTomato);
        imageViewBreak = findViewById(R.id.imageViewBreak);
        imageViewWork = findViewById(R.id.imageViewWork);
    }

    private void initListeners() {
        imageViewReset.setOnClickListener((View.OnClickListener) this);
        imageViewStartStop.setOnClickListener((View.OnClickListener) this);
        imageViewTomato.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewReset:
                reset();
                break;
            case R.id.imageViewStartStop:
                workstartStop();
                break;
            case R.id.imageViewTomato:
                visibleButton();
                workstartStop();
                break;
        }
    }

    public void visibleButton() {
        imageViewStartStop.setVisibility(View.VISIBLE);
        imageViewTomato.setVisibility(View.GONE);
    }

    /**
     * method to reset count down timer
     */
    private void reset() {
        breakCount = 0;
        stopCountDownTimer();
        //startCountDownTimer();
        textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
        // call to initialize the progress bar values
        setProgressBarValues();
        //hiding break and work icon
        imageViewBreak.setVisibility(View.GONE);
        imageViewWork.setVisibility(View.GONE);
        // changing stop icon to start icon
        imageViewStartStop.setImageResource(R.mipmap.icon_start);
        // changing the timer status to stopped
        timerStatus = TimerStatus.STOPPED;
    }


    /**
     * method to start and stop count down timer
     */
    private void workstartStop() {

        breakOrWork = true;
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            WorkSetTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the work icon
            imageViewWork.setVisibility(View.VISIBLE);
            // showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            imageViewStartStop.setImageResource(R.mipmap.icon_pause);
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();


        } else {
            // changing stop icon to start icon
            imageViewStartStop.setImageResource(R.mipmap.icon_start);
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();

        }

    }

    /**
     * method to initialize the values for count down timer work
     */
    private void WorkSetTimerValues() {
        int time;
        time = Integer.parseInt("25");
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
    }

    /**
     * method to initialize the values for count down timer
     */
    private void BreakSetTimerValues() {
        int time;

        // fetching value from edit text and type cast to integer
        time = Integer.parseInt("5");

        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {
                //The count check end of the task
                breakCount++;
                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // hiding the work icon
                imageViewWork.setVisibility(View.GONE);
                // hiding the break icon
                imageViewBreak.setVisibility(View.GONE);
                // changing stop icon to start icon
                imageViewStartStop.setImageResource(R.mipmap.icon_start);
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED;
                //Vibration

                //checking work and break times
                checkBreakOrWork();
            }
        }.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            scheduleNotification(getNotification("Time is over!!"), timeCountInMilliSeconds);
        }
    }

    /**
     * method to set the alarm
     *
     * @param notification
     * @param delay
     */
    private void scheduleNotification(Notification notification, long delay) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = System.currentTimeMillis() + delay;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);

    }


    /**
     * method to creating notification
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification getNotification(String content) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification builder = new Notification.Builder(this, "default")
                .setContentTitle("Pomodoro")
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.icontomato).getNotification();
        return builder;
    }


    /**
     * method check break and work duration
     */
    public void checkBreakOrWork() {
        if (breakCount != 8) {
            if (breakOrWork) {
                breakAlert();
                // System.out.println("************************ break");
            } else if (!breakOrWork) {
                workAlert();
                //  System.out.println("************************ work");
            } else {
                Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Pomodoro is over", Toast.LENGTH_LONG).show();
            reset();

        }

    }

    /**
     * method to show alert take a break
     */
    public void breakAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("Good job! Would you like  to take a break");

        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                workstartStop();
            }
        });

        alertDialogBuilder.setNegativeButton("Take a break", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                breakStartStop();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * method to show alert take a break
     */
    public void workAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("The break is over! Now working time");
        alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                workstartStop();
            }
        });

        alertDialogBuilder.setNegativeButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    /**
     * method to start and stop count down timer break
     */
    private void breakStartStop() {
        breakOrWork = false;
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            BreakSetTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the break icon
            imageViewBreak.setVisibility(View.VISIBLE);
            // showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            imageViewStartStop.setImageResource(R.mipmap.icon_pause);
            // making edit text not editable
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();

        } else {
            breakCount = 0;
            // changing stop icon to start icon
            imageViewStartStop.setImageResource(R.mipmap.icon_start);
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();

        }

    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;

    }


}
