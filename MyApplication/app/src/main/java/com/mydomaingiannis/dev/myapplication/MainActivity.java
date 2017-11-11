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
import java.util.ArrayList;
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
    private EditText test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         test = ((EditText) findViewById(R.id.test));
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

    void make(String response){
        //checks if response is whole.
       /* for (char x: response.toCharArray()){
            System.out.print(x);
        }*/
                                                                                //DOTALL matches mewlines aswell
        /*Pattern pattern = Pattern.compile("^. <h2 class=\"title\">(.+)</h2> *$",Pattern.DOTALL);
        //Pattern pattern = Pattern.compile("<h2 class=\"title\">");
        Matcher matcher = pattern.matcher(response);
        int matches=0;
        //System.out.println(response);
            while(matcher.find()) {
                System.out.println(matches++);
                System.out.println(matcher.group(1));
                System.out.println("------------------------------");
            }
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

        ArrayList<String> urls = new ArrayList<String>();
        ArrayList<String> titles = new ArrayList<String>();  //mexri kai auti ti grammi einai idio, apo dw k kati alla3an polla
        String tempurl;
        String temptitle;

        for (int i=0;i<epitelous.size();i++) {
            Pattern pattern3 = Pattern.compile("href=(.*)/");
            Matcher matcher3 = pattern3.matcher(epitelous.get(i));
            while (matcher3.find()) {
                tempurl = matcher3.group(1).toString();
                tempurl = tempurl.replace("\\",""); //anti na to kopsw olo, afinw tous eidikous xaraktires
                tempurl = tempurl.replace("\"",""); //k tous kanw replace me to keno meta, prin valw to apotelesma sti lista
                tempurl = tempurl.replace("'","");
                urls.add(tempurl);
            }
        }

        for (int i=0;i<epitelous.size();i++) {
            Pattern pattern3 = Pattern.compile("title=(.*)\"");
            Matcher matcher3 = pattern3.matcher(epitelous.get(i));
            while (matcher3.find()) {
                temptitle = matcher3.group(1).toString();
                temptitle = temptitle.replace("\\","");
                temptitle = temptitle.replace("\"","");
                titles.add(temptitle);
            }
        }

        for (int i=0;i<urls.size();i++) {
            test.append(urls.get(i) + "\n");
        }

        test.append("-------------------------------------------------\n");

        for (int i=0;i<titles.size();i++) {
            test.append(titles.get(i) + "\n");
        }

    }
}
