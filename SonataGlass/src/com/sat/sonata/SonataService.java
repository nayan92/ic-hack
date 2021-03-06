/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sat.sonata;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;
import com.google.android.glass.timeline.TimelineManager;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Service owning the LiveCard living in the timeline.
 */
public class SonataService extends Service {

    private static final String TAG = "SonataService";
    private static final String LIVE_CARD_TAG = "stopwatch";

    private ChronometerDrawer mCallback;

    private TimelineManager mTimelineManager;
    private LiveCard mLiveCard;



    @Override
    public void onCreate() {
        super.onCreate();
        mTimelineManager = TimelineManager.from(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final int TAKE_PICTURE_REQUEST = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (mLiveCard == null) {
//            Log.d(TAG, "Publishing LiveCard");
//            mLiveCard = mTimelineManager.createLiveCard(LIVE_CARD_TAG);

            Intent recogniseMusicIntent = new Intent(this, RecogniseMusicActivity.class);
            recogniseMusicIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(recogniseMusicIntent);


//            Intent cameraIntent = new Intent(this, TakePictureActivity.class);
//
//            cameraIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            Log.d("SITTING", "Starting TakePictureActivity from the service");
//            startActivity(cameraIntent);
//
//            RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.card_picture_taken);
//            views.setTextViewText(R.id.picture_taken_view,"Sitting across there");
//            mLiveCard.setViews(views);

//            Intent menuIntent = new Intent(this, MenuActivity.class);
//            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
//
//            mLiveCard.publish(PublishMode.REVEAL);
//            Log.d(TAG, "Done publishing LiveCard");
//        } else {
//            // TODO(alainv): Jump to the LiveCard when API is available.
//        }

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
//        if (mLiveCard != null && mLiveCard.isPublished()) {
//            Log.d(TAG, "Unpublishing LiveCard");
//            if (mCallback != null) {
//                mLiveCard.getSurfaceHolder().removeCallback(mCallback);
//            }
//            mLiveCard.unpublish();
//            mLiveCard = null;
//        }
        super.onDestroy();
    }
}
