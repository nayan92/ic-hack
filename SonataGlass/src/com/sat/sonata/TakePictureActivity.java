package com.sat.sonata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.glass.media.CameraManager;

import java.io.File;

public class TakePictureActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SITTING", "Entered onCreate of TakePictureActivity");

        super.onCreate(savedInstanceState);

        // start the actual camera stuff.
        takePicture();

    }

    private static final int TAKE_PICTURE_REQUEST = 1;

    private void takePicture() {
        Log.d("SITTING", "Starting MediaStore.ACTION_IMAGE_CAPTURE intent");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
            Log.d("SITTING", "Entered onActivityResult");
            String picturePath = data.getStringExtra(
                    CameraManager.EXTRA_PICTURE_FILE_PATH);
            processPictureWhenReady(picturePath);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processPictureWhenReady(final String picturePath) {
        Log.d("SITTING", "Processing picture across there.");

        final File pictureFile = new File(picturePath);

        if (pictureFile.exists()) {
            Log.d("SITTING", "Picture exists and its path is: " + picturePath);
            Intent result = new Intent();
            result.putExtra("result", picturePath);
            setResult(0,result);
            finish();
            // The picture is ready; process it. TODO
        } else {
            Log.d("SITTING", "Picture doesnt exist.");

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
                            Log.d("SITTING", "File written.");

                            stopWatching();

                            // Now that the file is ready, recursively call
                            // processPictureWhenReady again (on the UI thread).
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    processPictureWhenReady(picturePath);
                                }
                            });
                        } else {
                            // fire not written.
                            Log.d("SITTING", "File not written.");

                        }
                    }
                }
            };
            observer.startWatching();
        }
    }
}
