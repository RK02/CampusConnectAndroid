package com.campusconnect.activity;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.campusconnect.R;
import com.squareup.picasso.Picasso;

public class ZoomPictureActivity extends AppCompatActivity {
    String url;ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    ImageView p_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_picture);
         p_image = (ImageView) findViewById(R.id.picture);
        try {
            url = getIntent().getStringExtra("PICTURE");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (url.isEmpty() || url != null) {
            Picasso.with(ZoomPictureActivity.this).load(url).into(p_image);
        }
        else{
            Picasso.with(ZoomPictureActivity.this).load(R.mipmap.default_image).into(p_image);
        }

    }

}
