package com.jayavijayjayavelu.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    public static ArrayList<CivicPositionNames> civicPositionNames = new ArrayList<>();
    public static ArrayList<CivicDetails> candidateDetails = new ArrayList<>();
    private Locator locator;
    static public Double mainLat;
    static public Double mainLon;
    TextView tv;
    public int pincode;
    RecyclerView recyclerView;
    CivicPositionNames civicPN;
    Map<String, String> states = new HashMap<String, String>();
    CivicDetails civicD;
    static public String address = "";
    public int pos;
    public static MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        states.put("Alabama","AL");
        states.put("Alaska","AK");
        states.put("Alberta","AB");
        states.put("American Samoa","AS");
        states.put("Arizona","AZ");
        states.put("Arkansas","AR");
        states.put("Armed Forces (AE)","AE");
        states.put("Armed Forces Americas","AA");
        states.put("Armed Forces Pacific","AP");
        states.put("British Columbia","BC");
        states.put("California","CA");
        states.put("Colorado","CO");
        states.put("Connecticut","CT");
        states.put("Delaware","DE");
        states.put("District Of Columbia","DC");
        states.put("Florida","FL");
        states.put("Georgia","GA");
        states.put("Guam","GU");
        states.put("Hawaii","HI");
        states.put("Idaho","ID");
        states.put("Illinois","IL");
        states.put("Indiana","IN");
        states.put("Iowa","IA");
        states.put("Kansas","KS");
        states.put("Kentucky","KY");
        states.put("Louisiana","LA");
        states.put("Maine","ME");
        states.put("Manitoba","MB");
        states.put("Maryland","MD");
        states.put("Massachusetts","MA");
        states.put("Michigan","MI");
        states.put("Minnesota","MN");
        states.put("Mississippi","MS");
        states.put("Missouri","MO");
        states.put("Montana","MT");
        states.put("Nebraska","NE");
        states.put("Nevada","NV");
        states.put("New Brunswick","NB");
        states.put("New Hampshire","NH");
        states.put("New Jersey","NJ");
        states.put("New Mexico","NM");
        states.put("New York","NY");
        states.put("Newfoundland","NF");
        states.put("North Carolina","NC");
        states.put("North Dakota","ND");
        states.put("Northwest Territories","NT");
        states.put("Nova Scotia","NS");
        states.put("Nunavut","NU");
        states.put("Ohio","OH");
        states.put("Oklahoma","OK");
        states.put("Ontario","ON");
        states.put("Oregon","OR");
        states.put("Pennsylvania","PA");
        states.put("Prince Edward Island","PE");
        states.put("Puerto Rico","PR");
        states.put("Quebec","PQ");
        states.put("Rhode Island","RI");
        states.put("Saskatchewan","SK");
        states.put("South Carolina","SC");
        states.put("South Dakota","SD");
        states.put("Tennessee","TN");
        states.put("Texas","TX");
        states.put("Utah","UT");
        states.put("Vermont","VT");
        states.put("Virgin Islands","VI");
        states.put("Virginia","VA");
        states.put("Washington","WA");
        states.put("West Virginia","WV");
        states.put("Wisconsin","WI");
        states.put("Wyoming","WY");
        states.put("Yukon Territory","YT");
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            if(address.equals("")) {
                locator = new Locator(this);
            }else{
                setData(mainLat,mainLon);
            }
        } else {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                ((TextView) findViewById(R.id.locationText)).setText("No Data For Location");
                final TextView tv = new TextView(MainActivity.this);
                tv.setText("Data cannot be accessed/loaded without Internet Connection.");
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(15);
                tv.setPadding(50, 20, 0, 30);
                alertDialog.setView(tv);
                alertDialog.setTitle("No Network Connection");
                alertDialog.setIcon(android.R.drawable.ic_menu_search);
                alertDialog.show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("YESS");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
        
    }
    public void setData(double lat, double lon) {
        mainLat = lat;
        mainLon = lon;
        Log.d(TAG, "setData: Lat: " + lat + ", Lon: " + lon);
        address = doAddress(mainLat, mainLon);
        tv = ((TextView) findViewById(R.id.locationText));
        tv.setText(address);

    }

    private String doAddress(double latitude, double longitude) {
        List<Address> addresses = null;
        final AsyncCivicInfo asyncCivicInfo = new AsyncCivicInfo(MainActivity.this);
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            try {

                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String sb = new String();
                for (Address ad : addresses) {
                    for (int i = 0; i < ad.getMaxAddressLineIndex(); i++){
                        if(ad.getAddressLine(i).contains(ad.getPostalCode())) {
                            pincode=Integer.parseInt(ad.getPostalCode());
                            sb = ad.getAddressLine(i);
                            asyncCivicInfo.dataURL=asyncCivicInfo.dataURL+ad.getPostalCode();
                            asyncCivicInfo.execute();
                        }
                    }
                }
                return sb;
            } catch (IOException e) {

            }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }

    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final AsyncCivicInfo asyncCivicInfo = new AsyncCivicInfo(MainActivity.this);
        switch (item.getItemId()) {
            case R.id.searchLocation:
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("Enter a State, City or Zip Code:");
                    final EditText inputLocation = new EditText(MainActivity.this);
                    inputLocation.getId();
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    inputLocation.setLayoutParams(lp);
                    alertDialog.setView(inputLocation);
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (!inputLocation.getText().toString().trim().equals("")){
                                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            try {
                                List<Address> addresses = null;
                                String tex = inputLocation.getText().toString().trim();
                                tex = tex.substring(0, 1).toUpperCase() + tex.substring(1).toLowerCase();
                                addresses = geocoder.getFromLocationName(tex, 1);
                                if (!addresses.isEmpty()) {
                                    if (addresses.get(0).hasLatitude()) {
                                        Double la = addresses.get(0).getLatitude();
                                        Double lo = addresses.get(0).getLongitude();
                                        addresses = geocoder.getFromLocation(la, lo, 1);
                                        if (addresses.get(0).getPostalCode() != null && addresses.get(0).getCountryCode().equals("US")) {
                                            if (addresses.get(0).getPostalCode().equals(tex)
                                                    || addresses.get(0).getLocality().equals(tex)
                                                    || addresses.get(0).getAdminArea().equals(tex)
                                                    || states.containsValue(tex.toUpperCase())) {
                                                String sb = new String();
                                                for (Address ad : addresses) {
                                                    for (int i = 0; i < ad.getMaxAddressLineIndex(); i++) {
                                                        if (ad.getPostalCode() != null &&
                                                                ad.getAddressLine(i).contains(ad.getPostalCode())) {
                                                            mainLon = ad.getLongitude();
                                                            mainLat = ad.getLatitude();
                                                            sb = ad.getAddressLine(i);
                                                            pincode = Integer.parseInt(ad.getPostalCode());
                                                            asyncCivicInfo.dataURL = asyncCivicInfo.dataURL + ad.getPostalCode();
                                                            asyncCivicInfo.execute();
                                                        } else {
                                                            //locator = new Locator(MainActivity.this);
                                                        }
                                                    }
                                                    ((TextView) findViewById(R.id.locationText)).setText(sb);
                                                }
                                            } else {
                                                Toast.makeText(MainActivity.this, "No Proper Location Found", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(MainActivity.this, "No Proper Location Found", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "No Proper Location Found", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "No Proper Location Found", Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    }).show();
                    return true;
                }else{
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    ((TextView) findViewById(R.id.locationText)).setText("No Data For Location");
                    final TextView tv = new TextView(MainActivity.this);
                    tv.setText("Location cannot be found without Network Connection.");
                    tv.setTextColor(Color.BLACK);
                    tv.setTextSize(15);
                    tv.setPadding(50,20,0,30);
                    alertDialog.setView(tv);
                    alertDialog.setTitle("No Network Connection");
                    alertDialog.setIcon(android.R.drawable.ic_menu_search);
                    alertDialog.show();
                    return true;
                }
            case R.id.aboutPage:
                invalidateOptionsMenu();
                Intent i1 = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(i1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void doLocationName(String lookLoc) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = null;
            addresses = geocoder.getFromLocationName(lookLoc, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        recyclerView=(RecyclerView)findViewById(R.id.recyclerList);
        pos = recyclerView.getChildLayoutPosition(v);
        civicPN = civicPositionNames.get(pos);
        civicD = candidateDetails.get(pos);
        Intent intent = new Intent(this,OfficialActivity.class);
        intent.putExtra("position",civicPN.getPositionName());
        intent.putExtra("name",civicD.getName());
        intent.putExtra("party",civicD.getParty());
        intent.putExtra("photo",civicD.getPhoto());
        intent.putExtra("address",civicD.getAddress());
        intent.putExtra("city",civicD.getCity());
        intent.putExtra("state",civicD.getState());
        intent.putExtra("zip",civicD.getZip());
        intent.putExtra("phone",civicD.getPhone());
        intent.putExtra("email",civicD.getEmail());
        intent.putExtra("website",civicD.getUrl());
        String address1 = ((TextView) findViewById(R.id.locationText)).getText().toString();
        intent.putExtra("location", address1);
        String channels[][] = civicD.getChannels();
        if(channels!=null) {
            for (int i = 0; i < channels.length; i++) {
                intent.putExtra(channels[i][0], channels[i][1]);
            }
        }
        this.startActivity(intent);
    }
}
