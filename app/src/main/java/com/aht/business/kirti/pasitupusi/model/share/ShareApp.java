package com.aht.business.kirti.pasitupusi.model.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;

import com.aht.business.kirti.pasitupusi.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ShareApp {

    public void shareText(Activity activity, String message) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = message;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing " + activity.getResources().getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    public boolean shareReport(Activity activity, View view, String message) {

        boolean status = false;

        try {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            // configure the view to store the image in the cache
            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            view.buildDrawingCache();

            if (view.getDrawingCache() == null)
                return status; // verify for null

            // Using the cache, create the bitmap that will have the image of current view
            Bitmap snapshot = Bitmap.createBitmap(view.getDrawingCache());

            if (isExternalStorageWritable()) {
                String fileName = "reports/snapshot.jpg";
                File file = new File(activity.getExternalCacheDir(), fileName);

                if (file.getParentFile().exists())
                    file.delete();

                if (!file.getParentFile().exists())
                    file.mkdirs();

                if (!file.exists())
                    file.createNewFile();

                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                snapshot.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();

                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing " + activity.getResources().getString(R.string.app_name));
                sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                sharingIntent.setType("image/*");
                activity.startActivity(Intent.createChooser(sharingIntent, "Share using"));

                status = true;
            } else {
                status = false;
            }

            view.setDrawingCacheEnabled(false);
            view.destroyDrawingCache();
        }
        catch (Exception e) {
            status = false;
        }

        return status;
    }


    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
