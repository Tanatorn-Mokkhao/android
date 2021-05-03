package com.example.androind_v1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class MainActivity extends AppCompatActivity  {

    String serverResponse;
    ListView listView;
//    Button insertBtn;
//    Button btn_update;
//    EditText editTextSearch;
//    Button btnSearch;
    EditText editTextSearch;
    String[] values;
    String[] Id;
    String[] pass;
    String[] user;
    String delete;
    String test;
    String username, password,idSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.ListView);
        editTextSearch =(EditText) findViewById(R.id.searchEdt);
        Button searchBtn = (Button) findViewById(R.id.search);
        DownloadTextTask task = new DownloadTextTask();
        task.execute("http://192.168.56.1/mobile/connection.php");
        Button bunInsert = (Button) findViewById(R.id.Insert);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("AlertDialog");
                builder.setMessage("username :"+ user[position] + "\n" + "password :" + pass[position]);
                builder.setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(MainActivity.this,Update_activity.class);
                                intent.putExtra("id",Id[position]);
                                intent.putExtra("username",user[position]);
                                intent.putExtra("password",pass[position]);
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton("delete",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                delete = Id[position];
                                new PostMethodDelete().execute();
                                DownloadTextTask task = new DownloadTextTask();
                                task.execute("http://192.168.56.1/mobile/connection.php");
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        bunInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,Insert_activity.class);
            startActivity(intent);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text;
                text = editTextSearch.getText().toString();
                Toast.makeText(getApplicationContext(),"Onclick", Toast.LENGTH_SHORT).show();
                DownloadTextTask task = new DownloadTextTask();
                task.execute("http://192.168.56.1/mobile/search.php?username="+text);
            }
        });
    }
    private String downloadText(String strUrl) {
        String strResult = "";

        try {
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            strResult = readStream(con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strResult;
    }
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    private class DownloadTextTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadText(urls[0]);
            } catch (Exception e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            String name="";
            JSONObject contactObject;
            JSONArray jsonArray = null;
//            String c = "";
//            String d = "";
//            String e = "";
            try {
                jsonArray = new JSONArray(result);
                values = new String[jsonArray.length()];

                Id = new String[jsonArray.length()];
                pass = new String[jsonArray.length()];
                user = new String[jsonArray.length()];

                Toast.makeText(getApplicationContext(),Integer.toString(jsonArray.length()),Toast.LENGTH_SHORT).show();

                for(int i = 0; i < jsonArray.length(); i++)
                {
                    contactObject = jsonArray.optJSONObject(i);
                    //JSONObject contactObject = jsonArray.optJSONObject(i);
//                    c += contactObject.optString("username") + "-";
                    values[i] = contactObject.optString("id")  +"\n" + contactObject.optString("username") + "\n" +contactObject.optString("pass");

                    Id[i] = contactObject.optString("id");
                    pass[i] = contactObject.optString("pass");
                    user[i] = contactObject.optString("username");
                }

            } catch (Exception ex) {}
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,values);
            listView.setAdapter(adapter);
        }
    }






    public  class PostMethodDelete extends AsyncTask<String, Void, String>{
        String serverResponse;
        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://192.168.56.1/mobile/delete.php");
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
                    obj.put("id", delete);
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