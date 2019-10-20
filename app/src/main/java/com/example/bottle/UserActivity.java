package com.example.bottle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {
    Button login, signup;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        login = (Button) findViewById(R.id.btnlogin);
        signup = (Button) findViewById(R.id.btnsignup);
        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }
        public void login(){
            String user =email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            if(user.equals("123@gmail.com") && pass.equals("123")){
                Toast.makeText(this,"Email and Password matched!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this,"Email and Password do not match!", Toast.LENGTH_LONG).show();
            }
        }


    public void signup(){
        Intent intent = new Intent(UserActivity.this, SignupActivity.class);
        startActivity(intent);
//        String user =email.getText().toString().trim();
//        String pass = password.getText().toString().trim();
//        if(user.equals("123@gmail.com") && pass.equals("123")){
//            Toast.makeText(this,"Email and Password matched!", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(UserActivity.this, MainActivity.class);
//            startActivity(intent);
//        }else{
//            Toast.makeText(this,"Email and Password do not match!", Toast.LENGTH_LONG).show();
//        }
    }
    }

