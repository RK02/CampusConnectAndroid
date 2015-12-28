package com.campusconnect.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.TtsSpan;

import com.campusconnect.R;
import com.campusconnect.utility.PinchZoom.GestureImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by RK on 04/11/2015.
 */
public class EventPhotoFullScreenActivity extends AppCompatActivity {

    String url;
    GestureImageView event_photo_fs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_photo_full_screen);
        event_photo_fs = (GestureImageView) findViewById(R.id.iv_event_photo_fs);

        try {
            url = getIntent().getStringExtra("PICTURE");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (url.isEmpty() || url != null) {
            Picasso.with(EventPhotoFullScreenActivity.this).load(url).into(event_photo_fs);
        }
        else{
            Picasso.with(EventPhotoFullScreenActivity.this).load(R.mipmap.default_image).into(event_photo_fs);
        }

    }

}
