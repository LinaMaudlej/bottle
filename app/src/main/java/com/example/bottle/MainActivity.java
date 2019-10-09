package com.example.bottle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import android.app.NotificationManager;
import android.view.View;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    public static final int NOTIFICATION_ID = 101;

    public static final String CHANNEL_NAME = "Notification Channel";
    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    private static final String APP_ID = "q1XJyymP9b7EfWjZjzuCQTrsmLmaPUmXKNt/Jq5EorXltx8yYQ1Dag==";

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text_temp = (TextView) findViewById(R.id.temp);
        String temperature ="  0";
        text_temp.setText("Water Temperature:" + temperature); //set text for text view

        TextView text_level = (TextView) findViewById(R.id.level);
        String level ="  5%";
        text_level.setText("Water Level:" + level); //set text for text view

        TextView text_expire = (TextView) findViewById(R.id.expire);
        boolean flag_expire=true;
        if(flag_expire) {
            text_expire.setText("Don't Drink - Refill it again!!"); //set text for text view
        }else{
            text_expire.setText(" Safe to Drink "); //set text for text view
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);

//Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true);
            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true);
            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);
            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
            notificationChannel.setVibrationPattern(new long[]{
                    500,
                    500,
                    500,
                    500,
                    500
            });
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

      }

        double lat = 40.712774, lon = -74.006091;
        String units = "imperial";
        //String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
         //       lat, lon, units, APP_ID);
        String url = String.format("https://tempratureparam.azurewebsites.net/api/temprature?code=q1XJyymP9b7EfWjZjzuCQTrsmLmaPUmXKNt/Jq5EorXltx8yYQ1Dag==&temprature=%f",
                lat, APP_ID);
        TextView textView = (TextView) findViewById(R.id.textView2);
        new GetWeatherTask(textView).execute(url);

        Button b1 = (Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });



    }

    private void sendNotification(){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setContentTitle("Smart Bottle Reminder");
        notificationBuilder.setContentText("keep your hydration GOAL - DRINK DRINK !!");
        notificationBuilder.setSmallIcon(R.drawable.notfica);
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notfica));
        Notification notification = notificationBuilder.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        notificationBuilder.setVibrate(new long[] {
                500,
                500,
                500,
                500
        });
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1001, intent, 0);
        notificationBuilder.setContentIntent(pendingIntent);

    }


    String name_lina="lina";



}
