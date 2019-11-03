package com.example.bottle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HydrationActivity extends AppCompatActivity {
    private static Context mContext;
    SharedPreferences.Editor editor;
    Button   mButton;
    EditText mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext= getApplicationContext();
        SharedPreferences prefs = mContext.getSharedPreferences("MyPref_hydration", 0); // 0 - for private mode
        editor = prefs.edit();

        setContentView(R.layout.activity_hydration);
        mButton=(Button)findViewById(R.id.submit_litters);
        mEdit=(EditText)findViewById(R.id.edit_litters);

        String litters=prefs.getString("litters","3");


       mEdit.setText(litters, TextView.BufferType.EDITABLE);
        final Intent intent = new Intent (this, MainActivity.class);

        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        String litters=mEdit.getText().toString();
                        editor.putString("litters",litters);
                        editor.commit();

                        intent.putExtra("litters", litters);
                        startActivity(intent);
                    }
                });

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString("litters", mEdit.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       // mEdit.setText(savedInstanceState.getString("litters"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //SharedPreferences.Editor editor = prefs.edit();
        //editor.putString("litters",mEdit.getText().toString());
    }
}
