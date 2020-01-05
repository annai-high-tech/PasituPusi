package com.aht.business.kirti.pasitupusi.model.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ProfilePhotoManager {

    private Activity activity;
    private Fragment fragment;

    private String document_img1="";

    public ProfilePhotoManager(Activity activity) {
        this.activity = activity;
    }

    public void selectImage(Fragment mFragment) {
        this.fragment = mFragment;
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Profile Picture!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    fragment.startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");

                    fragment.startActivityForResult(Intent.createChooser(intent, "Select File"), 2);
                }
                else if (options[item].equals("Cancel")) {
                    //fragment.startActivityForResult(new Intent(), 3);
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public Bitmap onSelectFromGalleryResult(Intent data) {

        if(data == null ) {
            return null;
        }

        Uri selectedImage = data.getData();
        String[] filePath = { MediaStore.Images.Media.DATA };

        if(selectedImage == null ) {
            return null;
        }

        Bitmap bitmap = null;

        if(selectedImage.toString().contains("file://"))
        {
            bitmap = BitmapFactory.decodeFile(selectedImage.getPath());
        } else {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.getApplicationContext().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(bitmap == null ) {
            return null;
        }

        bitmap = getResizedBitmap(bitmap, 400);
        return bitmap;

    }

    public boolean saveProfilePhoto(Bitmap bitmap) {

        return true;
    }

    public Bitmap onCaptureImageResult(Intent data) {

        if(data == null ) {
            return null;
        }

        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

        if(bitmap == null ) {
            return null;
        }

        bitmap = getResizedBitmap(bitmap, 400);
        return bitmap;
    }

    private String bitmapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return document_img1;
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
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
