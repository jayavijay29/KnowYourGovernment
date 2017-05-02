package com.jayavijayjayavelu.knowyourgovernment;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by jayavijayjayavelu on 4/16/17.
 */

public class PhotoDetailActivity extends AppCompatActivity {
    private static final String TAG = "PhotoDetail Activity";
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photodetail);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
        ((TextView) findViewById(R.id.textView8)).setText(getIntent().getExtras().getString("location"));
        ((TextView) findViewById(R.id.candidatePosition)).setText(getIntent().getExtras().getString("position"));
        ((TextView) findViewById(R.id.candidateName)).setText(getIntent().getExtras().getString("name"));
        String party = getIntent().getExtras().getString("party");
        if (party.equals("Republican"))
            linearLayout.setBackgroundColor(Color.RED);
        else if (party.equals("Democratic"))
            linearLayout.setBackgroundColor(Color.BLUE);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            String photoUrl = getIntent().getExtras().getString("photoURL");
            imageView = (ImageView) findViewById(R.id.imageView2);
            if (photoUrl == (null))
                photoUrl = "https://images-assets.nasa.gov/image/6900952/does_not_exist.jpg";
            loadImage(photoUrl);
        }else{
            imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setImageResource(R.drawable.placeholder);
        }
    }
    private void loadImage(String photoUrl) {
        Log.d(TAG, "loadImage: " + photoUrl);
        Picasso picasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.d(TAG, "onImageLoadFailed: ");
                        picasso.load(R.drawable.missingimage)
                                .into(imageView);
                    }
                })
                .build();

        picasso.load(photoUrl)
                .error(R.drawable.missingimage)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }
}
