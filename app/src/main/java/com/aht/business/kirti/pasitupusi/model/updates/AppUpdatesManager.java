package com.aht.business.kirti.pasitupusi.model.updates;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.updates.data.VersionDetail;
import com.aht.business.kirti.pasitupusi.model.updates.enums.UpdateType;
import com.aht.business.kirti.pasitupusi.model.utils.Database;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public final class AppUpdatesManager {

    private AppUpdatesManager() {

    }

    public static void checkUpdates(final int versionCode, final Context context) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection(Database.DATABASE_APP_DATA_COLLECTION).document(Database.DATABASE_APP_DATA_VERSION_DOCUMENT);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                UpdateType updateType = UpdateType.NO_UPDATE;
                VersionDetail versionDetail = null;
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if(document.exists()) {

                        versionDetail = document.toObject(VersionDetail.class);
                    }
                }

                if(versionDetail != null) {
                    if (versionCode < versionDetail.getMinimumVersion()) {
                        updateType = UpdateType.MANDATORY;
                    } else if (versionCode < versionDetail.getCurrentVersion()) {
                        updateType = versionDetail.getUpdateType();
                    }
                }

                showUpdateDialog(updateType, context);

            }
        });

   }

    private static final void showUpdateDialog(UpdateType updateType, Context context) {

        final Context l_context = context;

        if(updateType != UpdateType.NO_UPDATE) {

            String displayMessage = "A new version of the application is available";
            if(updateType != UpdateType.MANDATORY) {
                displayMessage += "\n\nDo you want to update it now?";
            } else {
                displayMessage += "\n\nPlease update it now";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("New Version");
            builder.setIcon(R.drawable.alert);
            builder = builder.setMessage(
                    displayMessage)
                    .setCancelable(false)
                    .setPositiveButton("UPDATE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {

                                    String playStoreUrl = "https://play.google.com/store/apps/details?id=";
                                    String packageName = l_context.getPackageName().replace(".debug", "");
                                    Uri uriUrl = Uri.parse(playStoreUrl + packageName);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);
                                    l_context.startActivity(intent);

                                    //Toast.makeText(l_context, "You clicked on UPDATE", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

            if(updateType != UpdateType.MANDATORY) {
                builder = builder.setNegativeButton("IGNORE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                //Toast.makeText(l_context, "You clicked on IGNORE", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
            }
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

}
