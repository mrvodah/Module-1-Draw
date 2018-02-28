package com.example.qklahpita.draw;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_PERMISSION = 1;
    private FloatingActionButton fbCamera;
    private FloatingActionButton fbBrush;
    private FloatingActionMenu fbMenu;
    private GridView gvImages;
    LinearLayout linear;
    GridImageAdapter gridImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupPermission();
        setupUI();
    }

    private void setupPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warning!")
                        .setMessage("Without permission you can not use this app. " +
                                "Do you want to grant permission?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(
                                        MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_PERMISSION
                                );
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
                            }
                        })
                        .show();
            }
        }
    }

    private void setupUI() {
        fbCamera = findViewById(R.id.fb_camera);
        fbBrush = findViewById(R.id.fb_brush);
        fbMenu = findViewById(R.id.fb_menu);
        gvImages = findViewById(R.id.gv_images);
        linear = findViewById(R.id.linear);
        gridImageAdapter = new GridImageAdapter(this);

        fbCamera.setOnClickListener(this);
        fbBrush.setOnClickListener(this);

        gvImages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Warning!")
                        .setMessage("Do you want delete this image?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int pos) {

                                // Xóa file ảnh được chọn và load lại gridview
                                File file = new File(gridImageAdapter.imageModelList.get(i).path);
                                file.delete();
                                gridImageAdapter.imageModelList.remove(i);
                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                onStart();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int pos) {
                                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                return true;
            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, DrawActivity.class);

        if(view.getId() == R.id.fb_camera){
            intent.putExtra("camera_mode", true);
        }
        else
            intent.putExtra("camera_mode", false);



        startActivity(intent);

        fbMenu.close(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        gridImageAdapter = new GridImageAdapter(this);

        if(gridImageAdapter.getCount() == 0){
            linear.setVisibility(LinearLayout.VISIBLE);
        }
        else{
            linear.setVisibility(LinearLayout.INVISIBLE);
        }

        gvImages.setAdapter(gridImageAdapter);
    }
}
