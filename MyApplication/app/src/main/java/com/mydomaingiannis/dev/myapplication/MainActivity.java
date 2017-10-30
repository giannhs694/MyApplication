package com.mydomaingiannis.dev.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {


    private static final String REQUESTTAG = "test" ;
    private Button btnSendRequest;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String info="";
    private String url="http://www.xrysessyntages.com/?"+info+"=&lang=el" ;
    private static final String TAG= MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendRequest = (Button) findViewById(R.id.btnSendRequest);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send request -> print response (using Volley library)
                sendRequestAndPrintResponse();
            }
        });
    }

    private void sendRequestAndPrintResponse() {
        info = ((EditText) findViewById(R.id.searchField)).getText().toString();
        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG,"Response :"+ response.toString());
                EditText test = ((EditText) findViewById(R.id.test));
                test.setText(response.toString());
                //int len = response.length();
                Log.i(TAG,  url  );

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Error :" + error.toString());
            }
        });
        mStringRequest.setTag(REQUESTTAG);
        mRequestQueue.add(mStringRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRequestQueue.cancelAll(REQUESTTAG);
    }

    @Override
    protected void onStop() {
        super.onStop();
    mRequestQueue.cancelAll(REQUESTTAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRequestQueue.cancelAll(REQUESTTAG);
    }
}
