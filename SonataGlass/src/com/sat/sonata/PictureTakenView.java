package com.sat.sonata;

import android.content.Context;
import android.graphics.Picture;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by meltuhamy on 09/03/2014.
 */
public class PictureTakenView extends FrameLayout {

    TextView someRandomTextForNow;

    public PictureTakenView(Context context) {
        this(context, null, 0);
    }

    public PictureTakenView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PictureTakenView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        LayoutInflater.from(context).inflate(R.layout.card_picture_taken, this);

        someRandomTextForNow = (TextView) findViewById(R.id.picture_taken_view);
        someRandomTextForNow.setText("Sitting across there");
    }
}
