package com.mydomaingiannis.dev.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MainActivity extends AppCompatActivity {


    private static final String REQUESTTAG = "test" ;
    private Button btnSendRequest;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private static String info="";
    private String url="http://www.xrysessyntages.com/?s=";
    private static final String TAG= MainActivity.class.getName();
    private ListView lv ;
    private ArrayList<Recipe> recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipes = new ArrayList<Recipe>();
        btnSendRequest = (Button) findViewById(R.id.btnSendRequest);
        lv = (ListView)findViewById(R.id.lista);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send request -> print response (using Volley library)
                sendRequestAndPrintResponse();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> lv, View myView, int myItemInt, long mylng) {
                String selectedFromList =(String) (lv.getItemAtPosition(myItemInt));
                Intent myIntent = new Intent(getApplicationContext(), DisplayRecipe.class);
                   // myIntent.putExtra("recipes",recipes);
                    myIntent.putExtra("selectedRecipe",selectedFromList);
                    for(Recipe x: recipes){
                        if(x.getTitle()==selectedFromList){
                            myIntent.putExtra("selectedRecipeUrl",x.getRecipeUrl());
                        }
                    }
                startActivity(myIntent);
                // Log.i(TAG,selectedFromList);
            }
        });
    }

    private void sendRequestAndPrintResponse() {
        info = ((EditText) findViewById(R.id.searchField)).getText().toString();
        System.out.println(info);
        mRequestQueue = Volley.newRequestQueue(this);
    url += info +"&lang=el";
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // Log.i(TAG,"Response :"+ response.toString());

                make(response.toString());
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
    protected void onStop() {
        super.onStop();
    mRequestQueue.cancelAll(REQUESTTAG);
    }



    private void make(String response){
        //checks if response is whole.
       /* for (char x: response.toCharArray()){
            System.out.print(x);
        }*/
                                                                                //DOTALL matches mewlines aswell
        /*Pattern pattern = Pattern.compile("^. <h2 class=\"title\">(.+)</h2> *$",Pattern.DOTALL);

*/
        StringBuilder builder = new StringBuilder();
        Pattern pattern = Pattern.compile("regularwrapper(?s)(.*)regularwrapper");
        Matcher matcher = pattern.matcher(response);
        while (matcher.find()) {
            builder.append(matcher.group(1));
        }
        String katevato2 = builder.toString();

        ArrayList<String> epitelous = new ArrayList<String>();

        Pattern pattern2 = Pattern.compile("<a(.*)>");
        Matcher matcher2 = pattern2.matcher(katevato2);
        while (matcher2.find()) {
            epitelous.add(matcher2.group(1).toString());
        }

        for (int i=0;i<epitelous.size();i++) {
            if (!epitelous.get(i).contains("title")) {
                epitelous.remove(i);
            }
        }

        for (int i=0;i<epitelous.size();i++) {
            if (epitelous.get(i).contains("rel")) {
                epitelous.remove(i);
            }
        }



        for (int i=0;i<epitelous.size();i++) {
            Pattern pattern3 = Pattern.compile("href=\"(.*)/");
            Matcher matcher3 = pattern3.matcher(epitelous.get(i));
            Pattern pattern4 = Pattern.compile("title=\"(.*)\"");
            Matcher matcher4 = pattern4.matcher(epitelous.get(i));
            while (matcher4.find() &  matcher3.find()) {

                    recipes.add(new Recipe(matcher4.group(1).toString(),matcher3.group(1).toString()));

            }
        }




        ArrayAdapter<String> arrayTitleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        for(Recipe recipeItem : recipes){
            arrayTitleAdapter.add(recipeItem.getTitle());
        }
        lv.setAdapter(arrayTitleAdapter);


    }
}
