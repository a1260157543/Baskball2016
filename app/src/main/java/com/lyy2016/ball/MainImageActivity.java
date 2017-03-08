package com.lyy2016.ball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainImageActivity extends Activity {
    private ImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainimage);
        imageview = (ImageView)findViewById(R.id.imageview);
        Picasso.with(getApplicationContext()).load("https://a1260157543.github.io/images/xp.png")
                .placeholder(R.drawable.load)
                .error(R.drawable.load)
                //.resize(400, 200).centerCrop()
//				  .rotate(90)
                .into(imageview);
    }

    public void fin(View v) {
        finish();
    }

}
