package com.example.androind_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class Update_activity extends AppCompatActivity {
    EditText userEdt,passEdt;
    Button submitBtn;
    String username,password,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_activity);
        userEdt =  (EditText) findViewById(R.id.userEdit);
        Intent intent   = getIntent();
        id = intent.getStringExtra("id");
        username = intent.getStringExtra("username");
        userEdt.setText(username);
        /////////////////////////////////////////////////
        passEdt =  (EditText) findViewById(R.id.passEdit);
        password = intent.getStringExtra("password");
        passEdt.setText(password);


        submitBtn = (Button)findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),id + "\n" + userEdt.getText().toString(), Toast.LENGTH_SHORT).show();
                new PostMethod().execute();
                Intent intent = new Intent(Update_activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public  class PostMethod extends AsyncTask<String, Void, String>{
        String serverResponse;
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://192.168.56.1/mobile/update.php");
                urlConnection = (HttpURLConnection)url.openConnection();

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("id", id);
                    obj.put("username", userEdt.getText().toString());
                    obj.put("password", passEdt.getText().toString());
                    writer.write((getPostDataString(obj)));
                    Log.e("JSON Input", obj.toString());
                    writer.flush();
                    writer.close();
                    os.close();
                } catch (JSONException e){
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    serverResponse = readStream(urlConnection.getInputStream());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected  void onPostExecute(String s){
            super.onPostExecute(s);
            Log.e("Response", "" + serverResponse);
        }
        public String getPostDataString(JSONObject params) throws Exception{
            StringBuilder result = new StringBuilder();
            boolean first = true;
            Iterator<String> itr = params.keys();
            while(itr.hasNext()){
                String key = itr.next();
                Object value = params.get(key);
                if(first) {
                    first = false;
                } else {
                    result.append("&");
                }
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));
            }
            return result.toString();
        }
        private String readStream(InputStream inputStream) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();

            try{
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while((line = reader.readLine()) != null){
                    response.append(line);
                }
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return response.toString();
        }
    }

}