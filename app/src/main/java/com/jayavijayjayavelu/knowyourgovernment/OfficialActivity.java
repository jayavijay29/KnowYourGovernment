package com.jayavijayjayavelu.knowyourgovernment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by jayavijayjayavelu on 4/4/17.
 */

public class OfficialActivity extends AppCompatActivity {
    private static final String TAG = "Official Activity";
    ImageView imageView;
    TextView tvAddress;
    String googlePlus="";
    String facebook="";
    String twitter="";
    String youtube="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        ((TextView) findViewById(R.id.textView8)).setText(getIntent().getExtras().getString("location"));
        ((TextView) findViewById(R.id.candidatePosition)).setText(getIntent().getExtras().getString("position"));
        ((TextView) findViewById(R.id.candidateName)).setText(getIntent().getExtras().getString("name"));
        ((TextView) findViewById(R.id.candidateParty)).setText("(" + getIntent().getExtras().getString("party") + ")");
        if (getIntent().getExtras().getString("party").equals("Republican"))
            scrollView.setBackgroundColor(Color.RED);
        else if (getIntent().getExtras().getString("party").equals("Democratic"))
            scrollView.setBackgroundColor(Color.BLUE);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            String photoUrl = getIntent().getExtras().getString("photo");
            imageView = (ImageView) findViewById(R.id.imageView2);
            if (photoUrl == (null))
                photoUrl = "https://images-assets.nasa.gov/image/6900952/does_not_exist.jpg";
            loadImage(photoUrl);
        }else {
            imageView = (ImageView) findViewById(R.id.imageView2);
            imageView.setImageResource(R.drawable.placeholder);
        }
        tvAddress = (TextView) findViewById(R.id.candidateAddress);
        tvAddress.setText(getIntent().getExtras().getString("address") +" "+ getIntent().getExtras().getString("city")
            + ", " + getIntent().getExtras().getString("state")+ " "+ getIntent().getExtras().getString("zip"));
        Log.d(TAG, "LinkifyAddress: " + tvAddress.getText());
        Linkify.addLinks(tvAddress, Linkify.MAP_ADDRESSES);
        if (getIntent().getExtras().getString("phone") != null) {
            ((TextView) findViewById(R.id.candidatePhone)).setText(getIntent().getExtras().getString("phone"));
            Linkify.addLinks((TextView) findViewById(R.id.candidatePhone), Linkify.PHONE_NUMBERS);
        }
        if (getIntent().getExtras().getString("email") != null) {
            ((TextView) findViewById(R.id.candidateEmail)).setText(getIntent().getExtras().getString("email"));
            Linkify.addLinks(((TextView) findViewById(R.id.candidateEmail)), Linkify.EMAIL_ADDRESSES);
        }
        if (getIntent().getExtras().getString("website") != null) {
            ((TextView) findViewById(R.id.candidateWebsite)).setText(getIntent().getExtras().getString("website"));
            Linkify.addLinks((TextView) findViewById(R.id.candidateWebsite), Linkify.WEB_URLS);
        }
        if(getIntent().hasExtra("GooglePlus")){
            googlePlus = getIntent().getExtras().getString("GooglePlus");
        }else{
            ImageView iv = (ImageView) findViewById(R.id.imageView8);
            iv.setImageResource(0);
        }
        if(getIntent().hasExtra("Facebook")){
            facebook = getIntent().getExtras().getString("Facebook");
        }else{
            ImageView iv = (ImageView) findViewById(R.id.imageView9);
            iv.setImageResource(0);
        }
        if(getIntent().hasExtra("Twitter")){
            twitter = getIntent().getExtras().getString("Twitter");
        }else{
            ImageView iv = (ImageView) findViewById(R.id.imageView10);
            iv.setImageResource(0);
        }
        if(getIntent().hasExtra("YouTube")){
            youtube = getIntent().getExtras().getString("YouTube");
        }else{
            ImageView iv = (ImageView) findViewById(R.id.imageView7);
            iv.setImageResource(0);
        }
    }

    public void imageClicked(View v){
        String name = getIntent().getExtras().getString("name");
        String position = getIntent().getExtras().getString("position");
        String party = getIntent().getExtras().getString("party");
        String photoURL = getIntent().getExtras().getString("photo");
        String loc = getIntent().getExtras().getString("location");
        Intent intent = new Intent(this,PhotoDetailActivity.class);
        intent.putExtra("location" ,loc);
        intent.putExtra("position",position);
        intent.putExtra("name",name);
        intent.putExtra("party",party);
        intent.putExtra("photoURL",photoURL);
        this.startActivity(intent);
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
    public void twitterClicked(View v) {
        Intent intent = null;
        String name = twitter;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }
    public void googlePlusClicked(View v) {
        String name = googlePlus;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name); startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
        }
    }
    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + facebook;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                urlToUse = "fb://page/" + facebook;
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }
    public void youTubeClicked(View v) {
        String name = youtube;
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }
}
