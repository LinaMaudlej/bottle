package com.example.bottle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import android.app.NotificationManager;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    //our menu : hydration and user
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf= getMenuInflater();
        inf.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.user:
                Toast.makeText(this," showing your profile settings ",Toast.LENGTH_SHORT).show();
                Intent intent_user = new Intent(this,UserActivity.class );
                this.startActivity(intent_user);
                return true;
            case R.id.hydration:
                    Toast.makeText(this," showing your hydration goal settings ",Toast.LENGTH_SHORT).show();
                    Intent intent_hydration = new Intent(this, HydrationActivity.class);
                    this.startActivity(intent_hydration);
                    return true;
        }
        return true;
    }

    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    public static final int NOTIFICATION_ID = 101;
//safe to drink
    private static int allowed_days_drink =4;
//days,hours,minutes
    float level_past = -1;
    int day_past = -1;// the last day the person drank in it
    int minutes_past=-1;

    //saving parameters in local storage
    SharedPreferences.Editor editor;
    private static Context mContext;

    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }



//hydration goal
    private static int allowed_minutes_notification =15;
    private static double  cup_litters=0.25;
    private static double goal_littersPerDay=2.5;
    Intent intent = getIntent();

    private int drank_littersPerDay=0;
//notifcation
    public static final String CHANNEL_NAME = "Notification Channel";
    int importance = NotificationManager.IMPORTANCE_DEFAULT;
//function apps
    private static final String APP_ID = "q1XJyymP9b7EfWjZjzuCQTrsmLmaPUmXKNt/Jq5EorXltx8yYQ1Dag==";

    public MainActivity() throws IOException {
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext= getApplicationContext();
        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0); // 0 - for private mode
         editor = pref.edit();
        setContentView(R.layout.activity_main);
        Bundle p=getIntent().getExtras();
       // String hydration_litters = intent.getStringExtra("hydration_litters");
        //String hydration_litters= (String)p.getString("hydration_litters");
        //goal_littersPerDay=Double.parseDouble(hydration_litters);

        TextView text_temp = (TextView) findViewById(R.id.temp);
        String temperature ="0";//from arduino
        double temperature_double = Double.parseDouble(temperature);
        text_temp.setText("Water Temperature: " + temperature + "°C"); //set text for text view

        TextView text_level = (TextView) findViewById(R.id.level);
        String water_litters = "2";//from arduino, qde el qnene feha, in litters
        double water_litters_double = Double.parseDouble(water_litters);
        double bottle_size = 2; //in litters
        double level_double = (water_litters_double/bottle_size);
        float level = (float)level_double; // 0 <= level <= 1
        text_level.setText("Water Level: " + level*100 + "%"); //set text for text view

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        if(day_past == -1){
            update_day(day);
        }
        pref.getFloat("level_past",level_past);
        pref.getInt("day_past",day_past);

        TextView text_expire = (TextView) findViewById(R.id.expire);
        //
        //Not safe to drink when water temprature is greater than 40C or
        //the water did not fill for more than allowed_days_drink -1
        if(temperature_double > 40){
            text_expire.setText("Don't Drink - Refill it again!!"); //set text for text view
            text_expire.setTextColor(getResources().getColor(R.color.colorAccent));

        }else {
            if (level <= level_past && day >= (day_past +  allowed_days_drink)) { // not the same data > 4
                    text_expire.setText("Don't Drink - Refill it again!!"); //set text for text view
                text_expire.setTextColor(getResources().getColor(R.color.colorAccent));


            } else {
                text_expire.setText("Safe to Drink"); //set text for text view
                text_expire.setTextColor(getResources().getColor(R.color.colorGreen));
            }
        }
        if(level > level_past){
            update_day(day);

        }


        int hours=cal.get(Calendar.HOUR);
        int minutes= cal.get(Calendar.MINUTE);
        int left_minutes_in_halfday = (60 - minutes) + (20 - hours)*60;
        pref.getInt("minutes_past",minutes_past);
        double number_of_notifications=(goal_littersPerDay - drank_littersPerDay)/(cup_litters * left_minutes_in_halfday);
        //allowing notfication every 15 minutes from 8:00 am to 20pm
        if(number_of_notifications > 0 &&
                minutes>=minutes_past+allowed_minutes_notification &&
                8 <= hours && hours <=20
         ){
            sendNotification();
            update_minutes(minutes);
        }

        if(level < level_past){
            update_minutes(minutes);
        }
        update_level(level);


        //notification
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

//        double lat = 40.712774, lon = -74.006091;
//        String units = "imperial";
//        //String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
//         //       lat, lon, units, APP_ID);
//        String url = String.format("https://tempratureparam.azurewebsites.net/api/temprature?code=q1XJyymP9b7EfWjZjzuCQTrsmLmaPUmXKNt/Jq5EorXltx8yYQ1Dag==&temprature=%f",
//                lat, APP_ID);
//        TextView textView = (TextView) findViewById(R.id.textView2);
//        new GetWeatherTask(textView).execute(url);


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

    private void update_level(float level) {
        level_past = level;
        editor.putFloat("level_past", level_past); // Storing Float
        editor.commit(); // commit changes
    }
    private void update_day(int day) {
        day_past = day;
        editor.putInt("day_past", day_past); // Storing Int
        editor.commit(); // commit changes
    }
    private void update_minutes(int minutes) {
        minutes_past = minutes;
        editor.putInt("minutes_past", minutes_past); // Storing Int
        editor.commit(); // commit changes
    }


}
