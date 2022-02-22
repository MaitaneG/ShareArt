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
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareart.R;
import com.example.shareart.providers.ImageProvider;
import com.example.shareart.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class PostActivity extends AppCompatActivity {

    private ImageView argazkiaIgoBotoia;
    private ImageButton argitaratuBotoia;
    private File argazkiaFitxeroa;
    private ProgressBar progressBar;
    private TextInputEditText editTextDeskripzioa;
    private TextView textViewKategoria;
    private ImageView imageViewNatura;
    private ImageView imageViewHiperrealismoa;
    private ImageView imageViewErretratoa;
    private ImageView imageViewGraffiti;
    private ImageView imageViewKomikia;
    private ImageView imageViewIlustrazioa;
    private ImageView imageViewKarikatura;
    private ImageView imageViewNaturaHila;

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
        // ImageView
        imageViewNatura = findViewById(R.id.imageViewNatura);
        imageViewHiperrealismoa = findViewById(R.id.imageViewHiperrealismo);
        imageViewErretratoa = findViewById(R.id.imageViewErretratoak);
        imageViewGraffiti = findViewById(R.id.imageViewGraffiti);
        imageViewKomikia = findViewById(R.id.imageViewKomikia);
        imageViewIlustrazioa = findViewById(R.id.imageViewIlustrazioa);
        imageViewKarikatura = findViewById(R.id.imageViewKarikatura);
        imageViewNaturaHila = findViewById(R.id.imageViewNaturaHila);
        // TextView
        textViewKategoria = findViewById(R.id.textViewAukeratutakoKategoria);
        // EditText
        editTextDeskripzioa = findViewById(R.id.textInputEditTextDeskripzioa);
        // ProgressBar
        progressBar = findViewById(R.id.indeterminateBarPost);
        progressBar.setVisibility(View.INVISIBLE);
        // OnClickListener
        argazkiaIgoBotoia.setOnClickListener(this::galeriaZabaldu);
        argitaratuBotoia.setOnClickListener(this::argitaratu);
        imageViewNatura.setOnClickListener(this::kategoriaAldatu);
        imageViewHiperrealismoa.setOnClickListener(this::kategoriaAldatu);
        imageViewErretratoa.setOnClickListener(this::kategoriaAldatu);
        imageViewGraffiti.setOnClickListener(this::kategoriaAldatu);
        imageViewKomikia.setOnClickListener(this::kategoriaAldatu);
        imageViewIlustrazioa.setOnClickListener(this::kategoriaAldatu);
        imageViewKarikatura.setOnClickListener(this::kategoriaAldatu);
        imageViewNaturaHila.setOnClickListener(this::kategoriaAldatu);
        // ImageProvider
        imageProvider = new ImageProvider();
    }

    private void kategoriaAldatu(View view) {
        switch (view.getId()) {
            case R.id.imageViewNatura:
                textViewKategoria.setText("Natura");
                break;
            case R.id.imageViewHiperrealismo:
                textViewKategoria.setText("Hiperrealismo");
                break;
            case R.id.imageViewErretratoak:
                textViewKategoria.setText("Erretratoak");
                break;
            case R.id.imageViewGraffiti:
                textViewKategoria.setText("Graffiti");
                break;
            case R.id.imageViewKomikia:
                textViewKategoria.setText("Komikia");
                break;
            case R.id.imageViewIlustrazioa:
                textViewKategoria.setText("Ilustrazioa");
                break;
            case R.id.imageViewKarikatura:
                textViewKategoria.setText("Karikaturak");
                break;
            case R.id.imageViewNaturaHila:
                textViewKategoria.setText("Natura hila");
                break;
        }
    }

    private void argitaratu(View view) {
        String deskripzioa = editTextDeskripzioa.getText().toString();
        String kategoria = textViewKategoria.getText().toString();

    }

    private void gordeArgazkia() {
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