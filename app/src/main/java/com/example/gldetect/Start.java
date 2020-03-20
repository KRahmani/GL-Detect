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
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Start extends AppCompatActivity {
    private static ImageView home_img;
    private static LinearLayout cam_btn,lib_btn;
    // Camera Permission Request Code
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int LIB_PERMISSION_REQUEST_CODE = 2;

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
                        != PackageManager.PERMISSION_GRANTED)
                {
                    // Permission is not granted
                    //request permission for camera
                    ActivityCompat.requestPermissions(Start.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
                else openCamera();
            }
        });
        lib_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    // Permission is not granted
                    //request permission for library
                    ActivityCompat.requestPermissions(Start.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, LIB_PERMISSION_REQUEST_CODE);
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
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }
    private void openLibrary(){
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
                                        new String[]{Manifest.permission.CAMERA},
                                        CAMERA_PERMISSION_REQUEST_CODE);
                                break;
                            case LIB_PERMISSION_REQUEST_CODE:
                                ActivityCompat.requestPermissions(Start.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        LIB_PERMISSION_REQUEST_CODE);
                                break;
                        }

                    }
                });
        alertDialog.show();
    }

}





