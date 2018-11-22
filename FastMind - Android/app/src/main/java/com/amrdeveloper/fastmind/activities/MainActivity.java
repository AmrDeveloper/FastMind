package com.amrdeveloper.fastmind.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.amrdeveloper.fastmind.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSingleGame(View view){
        // TODO : Make User Level = 0 , Score = 0
        Intent intent = new Intent(this,SinglePlayActivity.class);
        startActivity(intent);
        finish();
    }

    public void continueSingleGame(View view){
        Intent intent = new Intent(this,SinglePlayActivity.class);
        startActivity(intent);
    }
    public void feedActivity(View view){
        Intent intent = new Intent(this,FeedActivity.class);
        startActivity(intent);
    }

    public void rankActivity(View view){
        Intent intent = new Intent(this,RankActivity.class);
        startActivity(intent);
    }

    public void aboutActivity(View view){
        Intent intent = new Intent(this,AboutActivity.class);
        startActivity(intent);
    }

    public void logoutAction(View view){
        //TODO : Update Current User Information
        //TODO : Delete Current User Data From SharePreferences
        //TODO : Finish This Activity and back to LoginActivity to reLogin
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
