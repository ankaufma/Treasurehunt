package com.contexagon.treasurehunt.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import com.contexagon.treasurehunt.R;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ankaufma on 15.02.2016.
 */
public class RESTRequestAsyncTask extends AsyncTask<String, Void, String> {
    private Context mcontext;
    private String json;

    public RESTRequestAsyncTask(Context context) {
        mcontext = context;
    }

    protected String doInBackground(String... params) {
        String cburl = mcontext.getResources().getString(R.string.serverUrl) + ":4984";
        String cbdb = mcontext.getResources().getString(R.string.database);
        String siteId = params[0];
        json = null;
        try {
            URL url = new URL(cburl + "/" + cbdb + "/" +siteId);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String temp;
            while((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            json = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    protected void onPostExecute(String result) {
        json = result;
    }
}
