package com.mydomaingiannis.dev.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private Button btnSendRequest;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "http://www.xrysessyntages.com/";
    private static final String TAG= MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendRequest = (Button) findViewById(R.id.btnSendRequest);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send request -> print respone (using Volley library)
                sendRequestAndPrintResponse();
            }
        });
    }

    private void sendRequestAndPrintResponse() {
        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"Response :"+ response.toString());
                int len = response.length();
                Log.i(TAG,  Integer.toString(len)  );

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);
    }
}
