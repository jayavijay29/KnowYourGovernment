package com.jayavijayjayavelu.knowyourgovernment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jayavijayjayavelu on 4/1/17.
 */

public class AsyncCivicInfo extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;
    public ArrayList<ArrayList> totalInfoList;
    ArrayList<CivicInfo> civicInfos = new ArrayList<>();
    ArrayList<CivicPositionNames> civicPositions = new ArrayList<>();
    ArrayList<CivicDetails> civicDetails = new ArrayList<>();
    ArrayList<ArrayList> totalValues = new ArrayList<ArrayList>();
    private int count;
    public String dataURL =
            "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyCyryzz4slA2JRve_vqT84ryOkJ7m6Aet4&address=";
    private static final String TAG = "AsyncCivicLoader";
    private Context context;
    RecyclerView recyclerView;
    CivicAdapter civicAdapter;

    public AsyncCivicInfo(MainActivity ma) {
        mainActivity = ma;
        this.context = ma;
    }

    @Override
    protected void onPreExecute() {
        //Toast.makeText(mainActivity, "Loading Stock Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        Uri dataUri = Uri.parse(dataURL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {

        }
        return sb.toString();
    }
    @Override
    protected void onPostExecute(String s) {
        totalInfoList = parseJSON(s);
        MainActivity main = new MainActivity();
        main.civicPositionNames = civicPositions;
        main.candidateDetails =civicDetails;
        recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recyclerList);
        civicAdapter = new CivicAdapter(main.civicPositionNames, main.candidateDetails, mainActivity);
        recyclerView.setAdapter(civicAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
    }
    private ArrayList<ArrayList> parseJSON(String s) {

        try {

            //FOR GETTING THE CITY LOCATION
            JSONObject jObjMain = new JSONObject(s);
            count = jObjMain.length();
            JSONObject jLocation = new JSONObject(jObjMain.getString("normalizedInput"));
            for (int i = 0; i < jObjMain.length(); i++) {
                String city = jLocation.getString("city");
                String state = jLocation.getString("state");
                String zip = jLocation.getString("zip");
                civicInfos.add(new CivicInfo(city,state,zip));
            }

            //FOR GETTING THE POSITIONS AND THE INDICES
            JSONArray jObjMain2 = new JSONArray(jObjMain.getString("offices"));
            JSONObject jObjIndex;
            for(int i =0;i<jObjMain2.length();i++){
                jObjIndex = jObjMain2.getJSONObject(i);
                String positionName = jObjIndex.getString("name");
                JSONArray officialIndices =jObjIndex.getJSONArray("officialIndices");
                int numberPoisitions = officialIndices.length();
                for(int j=0;j<numberPoisitions;j++)
                    civicPositions.add(new CivicPositionNames(positionName));
            }
            //System.out.println("---> "+civicPositions.size());

            //FOR GETTING THE DETAILS OF THE CIVIC PERSON
            JSONArray jObjMain3 = new JSONArray(jObjMain.getString("officials"));
            JSONObject jObjIndex2;
            for(int i =0;i<civicPositions.size();i++){
                jObjIndex2 = jObjMain3.getJSONObject(i);
                String name = jObjIndex2.getString("name");
                //System.out.println(name);

                String address = "";
                String city = null;
                String state = null;
                String zip = null;
                if(jObjIndex2.has("address")) {
                    JSONArray jsonAddress = jObjIndex2.getJSONArray("address");
                    JSONObject jsonAddressObj = jsonAddress.getJSONObject(0);
                    address = address + jsonAddressObj.getString("line1");
                    if (jsonAddressObj.has("line2")) {
                        address = address + ", " + jsonAddressObj.getString("line2");
                        if (jsonAddressObj.has("line3"))
                            address = address + ", " + jsonAddressObj.getString("line3");
                    }
                    city = jsonAddressObj.getString("city");
                    state = jsonAddressObj.getString("state");
                    zip = jsonAddressObj.getString("zip");
                }
                //System.out.println(address);

                String party;
                if(jObjIndex2.has("party"))
                    party = jObjIndex2.getString("party");
                else
                    party = "Unknown";
                //System.out.println("--> "+party);

                String phone = null;
                if(jObjIndex2.has("phones")) {
                    JSONArray jsonPhone = jObjIndex2.getJSONArray("phones");
                    if (!jsonPhone.get(0).toString().isEmpty())
                        phone = jsonPhone.get(0).toString();
                }
                //System.out.println(phone);

                String url = null;
                if(jObjIndex2.has("urls")) {
                    JSONArray jsonUrl = jObjIndex2.getJSONArray("urls");
                    if (!jsonUrl.get(0).toString().isEmpty())
                        url = jsonUrl.get(0).toString();
                }
                //System.out.println(url);

                String email = null;
                if(jObjIndex2.has("emails")) {
                    JSONArray jsonEmail = jObjIndex2.getJSONArray("emails");
                    if (!jsonEmail.get(0).toString().isEmpty())
                        email = jsonEmail.get(0).toString();
                }
                //System.out.println(email);

                String photo= null;
                if(jObjIndex2.has("photoUrl"))
                    photo = jObjIndex2.getString("photoUrl");
                //System.out.println(photo);

                String channels[][]=null;
                if(jObjIndex2.has("channels")) {
                    JSONArray jsonChannels = jObjIndex2.getJSONArray("channels");
                    channels= new String[jsonChannels.length()][2];
                    JSONObject jsonChannelsObj;
                    for(int j=0;j<jsonChannels.length();j++) {
                        jsonChannelsObj = jsonChannels.getJSONObject(j);
                        channels[j][0] = jsonChannelsObj.getString("type");
                        channels[j][1] = jsonChannelsObj.getString("id");
                    }
                }
                //System.out.println(channels);
                civicDetails.add(new CivicDetails(name,address,city, state, zip, party,phone,url,email,photo,channels));
            }
            totalValues.add(civicInfos);
            totalValues.add(civicPositions);
            totalValues.add(civicDetails);
            return totalValues;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
