package com.example.shareart.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shareart.R;
import com.example.shareart.providers.ImageProvider;
import com.example.shareart.utils.FileUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilaEguneratuActivity extends AppCompatActivity {

    private CircleImageView perfilaArgazkiaAldatu;
    private TextInputEditText editTextErabiltzailea;
    private ImageButton perfilaAldatuBotoia;
    private ImageProvider imageProvider;
    private AlertDialog.Builder alertDialog;

    private File argazkiaFitxeroa;

    private CharSequence options[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfila_eguneratu);
        hasieratu();
    }

    private void hasieratu() {
        // ImageView
        perfilaArgazkiaAldatu = findViewById(R.id.perfilaArgazkiaAldatu);
        // EditText
        editTextErabiltzailea = findViewById(R.id.textInputEditTextErabiltzaileaAldatu);
        // ImageButton
        perfilaAldatuBotoia = findViewById(R.id.imageButtonPerfilaAldatu);
        // OnClickListener
        perfilaArgazkiaAldatu.setOnClickListener(this::argakiaAukeratzekoMetodoa);
        perfilaAldatuBotoia.setOnClickListener(this::perfilaAldatu);
        // AlertDialog
        alertDialog = new AlertDialog.Builder(this).setTitle("Hautatu aukera bat");
        options = new CharSequence[]{"Galeriako argazki bat", "Argazkia atera"};
        // ImageProvider
        imageProvider = new ImageProvider();
    }

    private void argakiaAukeratzekoMetodoa(View view) {
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    galeriaZabaldu();
                } else if (i == 1) {
                    argazkiaAtera();
                }
            }
        }).show();
    }

    /**
     * Mugikorraren galeria zabaltzeko
     */
    private void galeriaZabaldu() {
        Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galeriaIntent.setType("image/");
        someActivityResultLauncher.launch(galeriaIntent);
    }

    /**
     * Argazkia aukeratzeko galeriatik
     */
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    /**
                     * Galeriatik argazkia aukeratu
                     */
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            Intent data = result.getData();
                            Uri imageUri = data.getData();
                            argazkiaFitxeroa = FileUtil.from(PerfilaEguneratuActivity.this, imageUri);
                            perfilaArgazkiaAldatu.setImageBitmap(BitmapFactory.decodeFile(argazkiaFitxeroa.getAbsolutePath()));
                        } catch (Exception ex) {
                            Toast.makeText(PerfilaEguneratuActivity.this, "Errore bat egon da", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private void argazkiaAtera() {

    }

    private void perfilaAldatu(View view) {

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Perfila editatzeko pantailatik irteten")
                .setMessage("Ziur zaude irten nahi zarela pantaila honetatik perfila editatu gabe?")
                .setPositiveButton("Bai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Ez", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}