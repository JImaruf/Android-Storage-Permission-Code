package com.example.androidstoragepermission;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE =1001 ;//request  code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(checkPermission())
        {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

        }

        else
        {
            requestPermission();
        }


    }


    private void requestPermission()
    {
        if(SDK_INT>= Build.VERSION_CODES.R)
        {
            try{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri =Uri.fromParts("package",this.getPackageName(),null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);

            }
            catch (Exception e)
            {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);

                storageActivityResultLauncher.launch(intent);
            }


        }

        else{
            ActivityCompat.requestPermissions(this,new String[] {
                    READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    public  boolean checkPermission(){
        if(SDK_INT>=Build.VERSION_CODES.R)
        {
            return Environment.isExternalStorageManager();

        }
        else
        {
            int write= ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            return write== PackageManager.PERMISSION_GRANTED && read==PackageManager.PERMISSION_GRANTED;

        }
    }
    private ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(SDK_INT>=Build.VERSION_CODES.R)
            {
                if(Environment.isExternalStorageManager())
                {
                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();


                    // perform() what u want after getting permission
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }

            }

            else
            {


            }
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length>0)
            {
                boolean write = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1]==PackageManager.PERMISSION_GRANTED;


                if(write&&read)
                {
                    Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    // perform() what u want after getting permission
                }

                else
                {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}