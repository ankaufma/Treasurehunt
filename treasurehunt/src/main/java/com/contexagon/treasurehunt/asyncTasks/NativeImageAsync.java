package com.contexagon.treasurehunt.asyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import com.contexagon.treasurehunt.database.CouchbaseLite;
import com.couchbase.lite.Attachment;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Revision;
import com.couchbase.lite.support.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ankaufma on 17.02.2016.
 */
public class NativeImageAsync extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public NativeImageAsync(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... imageName) {
        String image = imageName[0];
        Revision rev = CouchbaseLite.document.getCurrentRevision();
        Attachment att = rev.getAttachment(image);
        Bitmap bmp = null;
        if (att != null) {
            try {
                InputStream in = att.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                try {
                    while ((line = br.readLine()) != null) {
                        total.append(line);
                    }
                    byte[] decodedByte = Base64.decode(total.toString(), 0);
                    bmp = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (CouchbaseLiteException cble) {

            }
        }
        return bmp;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
