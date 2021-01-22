package com.mwenda.notify;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "defcon";
    TextView textTitle,textNumber,textTitle2,textNumber2;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);

        prepareViews();

//        fetchData();
    }

    private void fetchData() {
        //fetches data from endpoint
        Log.d(TAG, "fetchData: fetching data....");

        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();


        AndroidNetworking.get("http://192.168.43.70/banksys/unread.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        pDialog.dismiss();
//                        Log.d(TAG, "onResponse: response->"+response.toString());
                        JSONArray jsonArrayResult = response.optJSONArray("result");
                        for(int i=0;i<jsonArrayResult.length();i++){
                            //loop to get the category information
                            JSONObject jsonObjectCategory = jsonArrayResult.optJSONObject(i);
                            String categoryId = jsonObjectCategory.optString("id");
                            String categoryTitle = jsonObjectCategory.optString("name");
                            String categoryUnread = jsonObjectCategory.optString("unread");

                            JSONArray jsonArrayArticles = jsonObjectCategory.optJSONArray("articles");
                            for(int k=0;k<jsonArrayArticles.length();k++){
                                //loop through to get the articles in each category
                                JSONObject jsonObjectArticles = jsonArrayArticles.optJSONObject(k);

                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pDialog.dismiss();
                        Log.d(TAG, "onErrorBody->"+anError.getErrorBody());
                        Log.d(TAG, "getErrorDetail->"+anError.getErrorDetail());
                        Log.d(TAG, "getStackTrace->"+anError.getStackTrace());
                        Log.d(TAG, "getMessage->"+anError.getMessage());

                    }
                });
    }

    private void prepareViews() {
        textTitle=(TextView)findViewById(R.id.textTitle);
        textNumber=(TextView)findViewById(R.id.textNumber);
        textTitle2=(TextView)findViewById(R.id.textTitle2);
        textNumber2=(TextView)findViewById(R.id.textNumber2);

        AndroidNetworking.initialize(getApplicationContext());

    }
}