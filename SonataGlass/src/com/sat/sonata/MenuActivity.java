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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.glass.media.CameraManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Activity showing the options menu.
 */
public class MenuActivity extends Activity {

    private String imagePath;
    private MenuActivity parent = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SITTING","Creating menuActivity");
        //imagePath = savedInstanceState.getString("image");
        //Log.d("SITTING", imagePath);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stopwatch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.
        switch (item.getItemId()) {
            case R.id.stop:
                Log.d("SITTING", "Inside stop case");
                //stopService(new Intent(this, SonataService.class));
                return true;
            case R.id.recognise:
                String imagePath = getIntent().getStringExtra("image");
                getIntent().removeExtra("image");
                Log.d("SITTING", imagePath);

                HttpPost postRequest = new HttpPost("http://129.31.195.224:8080/picUpload");
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                try{
                    
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    Bitmap bitmap;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    bitmap = BitmapFactory.decodeFile(imagePath, options);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                    byte[] data = bos.toByteArray();
                    ByteArrayBody bab = new ByteArrayBody(data, "music.jpg");

                    reqEntity.addPart("music", bab);

                    postRequest.setEntity(reqEntity);
                    HttpPost[] posts = new HttpPost[1];
                    posts[0] = postRequest;

                    GetImageTask getImageTask = new GetImageTask();
                    getImageTask.execute(posts);

                }
                catch(Exception e){
                    Log.v("Exception in Image", ""+e);
//                    reqEntity.addPart("picture", new StringBody(""));
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static final int TAKE_PICTURE_REQUEST = 1;

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
            String picturePath = data.getStringExtra(
                    CameraManager.EXTRA_PICTURE_FILE_PATH);
            processPictureWhenReady(picturePath);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processPictureWhenReady(final String picturePath) {
        final File pictureFile = new File(picturePath);

        if (pictureFile.exists()) {
            // The picture is ready; process it.
        } else {
            // The file does not exist yet. Before starting the file observer, you
            // can update your UI to let the user know that the application is
            // waiting for the picture (for example, by displaying the thumbnail
            // image and a progress indicator).

            final File parentDirectory = pictureFile.getParentFile();
            FileObserver observer = new FileObserver(parentDirectory.getPath()) {
                // Protect against additional pending events after CLOSE_WRITE is
                // handled.
                private boolean isFileWritten;

                @Override
                public void onEvent(int event, String path) {
                    if (!isFileWritten) {
                        // For safety, make sure that the file that was created in
                        // the directory is actually the one that we're expecting.
                        File affectedFile = new File(parentDirectory, path);
                        isFileWritten = (event == FileObserver.CLOSE_WRITE
                                && affectedFile.equals(pictureFile));

                        if (isFileWritten) {
                            stopWatching();

                            // Now that the file is ready, recursively call
                            // processPictureWhenReady again (on the UI thread).
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    processPictureWhenReady(picturePath);
                                }
                            });
                        }
                    }
                }
            };
            observer.startWatching();
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        // Nothing else to do, closing the Activity.
        finish();
    }

    public class GetImageTask extends AsyncTask {

        @Override
        protected String doInBackground(Object[] params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = (HttpPost)params[0];
            post.setHeader("type", "image/jpeg");
            StringBuilder s = new StringBuilder();
            try {
                HttpResponse response = httpClient.execute(post);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String sResponse;
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String responseString = s.toString();
            String strippedResponseString = responseString.trim();
            if(strippedResponseString.isEmpty()){
                // Error, empty
                Log.d("SITTING", "This isn't a musical file, jimmy.");
            } else {
                Log.d("SITTING", "INSIDE recognise CASE");
                Log.d("SITTING","Just before creating the asyncplayer");
                AsyncPlayer ap = new AsyncPlayer("MyTest");
                Log.d("SITTING","Just after creating the asyncplayer");
                Log.d("SITTING","URL from SERVER: "+strippedResponseString);
                ap.play(parent, Uri.parse(strippedResponseString), false, AudioManager.STREAM_MUSIC);
            }

            return strippedResponseString;
        }

    }

}
