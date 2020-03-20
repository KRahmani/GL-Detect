package com.example.gldetect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Start extends AppCompatActivity {
    private static ImageView home_img;
    private static LinearLayout cam_btn,lib_btn;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int LIB_PERMISSION_REQUEST_CODE = 2;
    private static final String FOLDER_NAME="GL Detect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        home_img= (ImageView) findViewById(R.id.home_icn);
        cam_btn=(LinearLayout)findViewById(R.id.cam_btn);
        lib_btn=(LinearLayout)findViewById(R.id.lib_btn);
        final Context context = this;
        home_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context,MainActivity.class);
                startActivity(intent);
            }
        });
        cam_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED)
                {
                    // Permission is not granted
                    //request permission for camera and external storage writing
                    ActivityCompat.requestPermissions(Start.this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                             CAMERA_PERMISSION_REQUEST_CODE);
                }
                else openCamera();
            }
        });
        lib_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED)
                {
                    // Permission is not granted
                    //request permission for library
                    ActivityCompat.requestPermissions(Start.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            LIB_PERMISSION_REQUEST_CODE);
                }
                else
                    openLibrary();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    // permission was granted,
                    openCamera();

                else
                    // permission denied
                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.CAMERA))
                        showAlert(CAMERA_PERMISSION_REQUEST_CODE);
                return;
            }
            case LIB_PERMISSION_REQUEST_CODE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    // permission was granted,
                    openLibrary();

                else
                    // permission denied
                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.READ_EXTERNAL_STORAGE))
                        showAlert(LIB_PERMISSION_REQUEST_CODE);
                return;
            }
        }

    }

    private void openCamera() {
        //createFolder();
        createExternalStoragePublicPicture();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }
    private void openLibrary(){
        //createFolder();
        createExternalStoragePublicPicture();
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivity(intent);
    }

    private void showAlert(final int permisionCode) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.warningTitle));
        alertDialog.setMessage(getString(R.string.warning));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        Toast.makeText(Start.this,
                                getString(R.string.denied),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch(permisionCode) {
                            case CAMERA_PERMISSION_REQUEST_CODE:
                                ActivityCompat.requestPermissions(Start.this,
                                        new String[]{Manifest.permission.CAMERA,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        CAMERA_PERMISSION_REQUEST_CODE);
                                break;
                            case LIB_PERMISSION_REQUEST_CODE:
                                ActivityCompat.requestPermissions(Start.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        LIB_PERMISSION_REQUEST_CODE);
                                break;
                        }

                    }
                });
        alertDialog.show();
    }

    public void createFolder(){

        File f= new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES)+"/"+FOLDER_NAME);
        boolean exists=f.exists();

        if(!exists){
            Toast.makeText(this, " doesn't exists", Toast.LENGTH_SHORT).show();
            f.mkdirs();
            f.setExecutable(true);
            f.setReadable(true);
            f.setWritable(true);
        }

        else
            Toast.makeText(this, " already exits.", Toast.LENGTH_SHORT).show();

    }

    void createExternalStoragePublicPicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory.  Note that you should be careful about
        // what you place here, since the user often manages these files.  For
        // pictures and other media owned by the application, consider
        // Context.getExternalMediaDir().
        File path = new File(Environment.getExternalStorageDirectory()+"/"+FOLDER_NAME);
        File file = new File(path, "exemplePicture.jpg");
        if (file.exists()) return;

        try {
            // Make sure the Pictures directory exists.
            path.mkdirs();

            // Very simple code to copy a picture from the application's
            // resource into the external file.  Note that this code does
            // no error checking, and assumes the picture is small (does not
            // try to copy it in chunks).  Note that if external storage is
            // not currently mounted this will silently fail.
            InputStream is = getResources().openRawResource(R.raw.exemple);
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[is.available()];
            is.read(data);
            os.write(data);
            is.close();
            os.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(this,
                    new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }

}

