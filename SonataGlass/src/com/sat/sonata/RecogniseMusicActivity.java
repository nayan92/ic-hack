package com.sat.sonata;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;


public class RecogniseMusicActivity extends Activity {

    private static final String TAG = "SonataService";
    private TimelineManager timelineManager;
    private LiveCard mLiveCard;
    private static final String LIVE_CARD_TAG = "recognise";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        timelineManager = TimelineManager.from(this);

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
        Log.d("SITTING", "The path that was returned is: " + path + " sitting across there like a baws");
        mLiveCard = timelineManager.createLiveCard(LIVE_CARD_TAG);

        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.card_picture_taken);
        views.setTextViewText(R.id.picture_taken_view, "Tap to continue");
        mLiveCard.setViews(views);

        Intent menuIntent = new Intent(this, MenuActivity.class);
        menuIntent.putExtra("image", path);

        mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
        mLiveCard.publish(LiveCard.PublishMode.REVEAL);
    }

    @Override
    protected void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            Log.d(TAG, "Unpublishing LiveCard");
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        super.onDestroy();
    }
}
