package com.contexagon.treasurehunt.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.contexagon.treasurehunt.controller.Controller;
import com.contexagon.treasurehunt.R;

import java.io.InputStream;

/**
 * Created by ankaufma on 17.02.2016.
 */
public class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageAsyncTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = Controller.contextHolder.getResources().getString(R.string.serverUrl)+":9000/assets/images/games/"+urls[0];
        Bitmap png = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            png = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return png;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
