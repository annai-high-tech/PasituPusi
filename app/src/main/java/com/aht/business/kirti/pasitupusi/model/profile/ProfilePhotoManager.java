package com.aht.business.kirti.pasitupusi.model.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.aht.business.kirti.pasitupusi.model.utils.BitmapUtils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class ProfilePhotoManager {

    public static final int PROFILE_IMAGE_ACTIVITY_REQUEST_CODE_UPDATE = CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;
    public static final int PROFILE_IMAGE_ACTIVITY_REQUEST_CODE_REMOVE = PROFILE_IMAGE_ACTIVITY_REQUEST_CODE_UPDATE + 100;

    private static final int PHOTO_MAX_SIZE = 400;

    private Activity activity;
    private Fragment fragment;

    public ProfilePhotoManager(Activity activity) {
        this.activity = activity;
    }

    public void selectImage(Fragment mFragment) {
        this.fragment = mFragment;
        final CharSequence[] options = { "Take or Choose the Photo", "Remove Photo" };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Select Profile Picture...");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(options[0]))
                {
                    CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setAspectRatio(PHOTO_MAX_SIZE, PHOTO_MAX_SIZE)
                        .start(fragment.getContext(), fragment);
                }
                else if (options[item].equals(options[1])) {
                    fragment.onActivityResult(PROFILE_IMAGE_ACTIVITY_REQUEST_CODE_REMOVE, RESULT_OK, new Intent());
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public Bitmap onSelectImageResult(Intent data) {

        if(data == null ) {
            return null;
        }

        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if(result == null ) {
            return null;
        }

        Uri uri = result.getUri();
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());

        if(bitmap == null ) {
            return null;
        }

        bitmap = BitmapUtils.getResizedBitmap(bitmap, PHOTO_MAX_SIZE);
        return bitmap;

    }

}
