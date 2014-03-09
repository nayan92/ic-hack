package com.sat.sonata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.glass.timeline.TimelineManager;


public class RecogniseMusicActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SITTING", "Entered onCreate of RecogniseMusicActivity");

        super.onCreate(savedInstanceState);

        Intent cameraIntent = new Intent(this, TakePictureActivity.class);
        Log.d("SITTING", "Starting TakePictureActivity from the service");
        startActivityForResult(cameraIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path = data.getStringExtra("result");
        Log.d("SITTING", "The path that was returned is: " + path + "sitting across there like a baws");

//        Intent menuIntent = new Intent(this, MenuActivity.class);
//        menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
    }
}
