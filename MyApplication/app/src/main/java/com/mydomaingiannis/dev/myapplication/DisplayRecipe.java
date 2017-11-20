package com.mydomaingiannis.dev.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplayRecipe extends AppCompatActivity {
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;
    private TextView recipeTextView;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url="";
    Bundle extras;
    private static final String TAG= DisplayRecipe.class.getName();
    private static final String REQUESTTAG = "recipeTextView" ;
    private  String selectedRecipe;

    private TextView urlTitleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);

       // mNetworkImageView  = (NetworkImageView) findViewById(R.id.displayImage);
        //NetworkImageView mNetworkImageView = (NetworkImageView) findViewById(R.id.displayImage);
        recipeTextView = (TextView) findViewById(R.id.displayRecipe);


        urlTitleView = (TextView) findViewById(R.id.recipeTitle);

        extras = getIntent().getExtras();
        if (extras != null) {
            selectedRecipe = extras.getString("selectedRecipe");
            url = extras.getString("selectedRecipeUrl");
            urlTitleView.append(selectedRecipe);
            urlTitleView.append("\n");


        }
        sendRequestAndPrintResponse();
        urlTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    private void sendRequestAndPrintResponse(){
        mRequestQueue = Volley.newRequestQueue(this);
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.i(TAG,"Response :"+ response.toString());
                        //NEW FILTER->>>
                make(response.toString());


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
    private void loadUrl(String url){

        /*mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getImageLoader();
        //Image URL - This can point to any image file supported by Android
        mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView,
                0, android.R.drawable.alert_light_frame));
        mNetworkImageView.setImageUrl(url, mImageLoader);*/
        new DownloadImageTask((ImageView) findViewById(R.id.displayImage))
                .execute(url);
    }

    private void make(String response){
        StringBuilder builderSuntagi = new StringBuilder();
        Pattern patternSuntagi = Pattern.compile("rightcontent(?s)(.*)rightcontent");
        Matcher matcherSuntagi = patternSuntagi.matcher(response);
        while (matcherSuntagi.find()) {
            builderSuntagi.append(matcherSuntagi.group(1));
        }
        String suntagi2 = builderSuntagi.toString();
        recipeTextView.setMovementMethod(new ScrollingMovementMethod());
        //t.append(suntagi2);

        StringBuilder builderImg = new StringBuilder();
        Pattern patternImg = Pattern.compile("src=\"(.*)\" alt");
        Matcher matcherImg = patternImg.matcher(suntagi2);
        while (matcherImg.find()) {
            builderImg.append(matcherImg.group(1));
            break;
        }
        String imgUrl = builderImg.toString();
        loadUrl(imgUrl);
        StringBuilder builderSuntagi2 = new StringBuilder();

        Pattern patternSuntagi2 = Pattern.compile("<p>(?s)(.+?)</p>");
        Matcher matcherSuntagi2 = patternSuntagi2.matcher(suntagi2);
        while (matcherSuntagi2.find()) {
            if (!matcherSuntagi2.group(1).toString().contains("img")) {
                builderSuntagi2.append(matcherSuntagi2.group(1).toString());
            }
        }
        String suntagiTeliki = builderSuntagi2.toString();
        suntagiTeliki = android.text.Html.fromHtml(suntagiTeliki).toString();

        recipeTextView.append(suntagiTeliki);
    }
    }

