package com.example.shareart.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shareart.R;
import com.example.shareart.providers.ImageProvider;
import com.example.shareart.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class PostActivity extends AppCompatActivity {

    private ImageView argazkiaIgoBotoia;
    private ImageButton argitaratuBotoia;
    private File argazkiaFitxeroa;
    private ProgressBar progressBar;

    private ImageProvider imageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        hasieratu();
    }

    private void hasieratu() {
        // ImageButton
        argazkiaIgoBotoia = findViewById(R.id.ImageViewArgazkiaIgo);
        argitaratuBotoia = findViewById(R.id.ImageButtonArgitaratu);

        // ProgressBar
        progressBar = findViewById(R.id.indeterminateBarPost);
        progressBar.setVisibility(View.INVISIBLE);

        // OnClickListener
        argazkiaIgoBotoia.setOnClickListener(this::galeriaZabaldu);
        argitaratuBotoia.setOnClickListener(this::argitaratu);

        imageProvider = new ImageProvider();
    }

    private void argitaratu(View view) {
        imageProvider.save(PostActivity.this, argazkiaFitxeroa).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PostActivity.this, "Ondo igo da argazkia", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostActivity.this, "Errore bat egon da argazkia igotzean", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void galeriaZabaldu(View view) {
        //Intent galeriaIntent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galeriaIntent.setType("image/");
        someActivityResultLauncher.launch(galeriaIntent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            Intent data = result.getData();
                            Uri imageUri = data.getData();
                            argazkiaFitxeroa = FileUtil.from(PostActivity.this, imageUri);
                            argazkiaIgoBotoia.setImageBitmap(BitmapFactory.decodeFile(argazkiaFitxeroa.getAbsolutePath()));
                            argazkiaIgoBotoia.setBackgroundColor(getResources().getColor(R.color.white));
                        } catch (Exception ex) {
                            Toast.makeText(PostActivity.this, "Errore bat egon da", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
}