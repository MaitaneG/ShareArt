package com.example.shareart.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shareart.R;
import com.example.shareart.models.Erabiltzailea;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.ImageProvider;
import com.example.shareart.providers.UserProvider;
import com.example.shareart.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Perfila eguneratzeko pantailaren Activity-a
 */
public class PerfilaEguneratuActivity extends AppCompatActivity {

    private CircleImageView perfilaArgazkiaAldatu;
    private TextInputEditText editTextErabiltzailea;
    private ImageButton perfilaAldatuBotoia;
    private AlertDialog.Builder alertDialog;
    private ProgressBar progressBar;

    private File argazkiaFitxeroa;

    private CharSequence options[];

    private ImageProvider imageProvider;
    private UserProvider userProvider;
    private AuthProvider authProvider;

    private String argazkiZaharraUrl;

    /**
     * Activity-a sortzen denean
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfila_eguneratu);
        hasieratu();
    }

    /**
     * Konponenteak hasieratzen dira
     */
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
        // Providers
        imageProvider = new ImageProvider();
        userProvider = new UserProvider();
        authProvider = new AuthProvider();
        // Erabiltzailea hasieratu
        erabiltzaileaLortu();
        // Progress bar
        progressBar = findViewById(R.id.indeterminateBarProfila);
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Momentuko erabiltzailearen informazioa lortu
     */
    private void erabiltzaileaLortu() {
        userProvider.getErabiltzailea(authProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("erabiltzaileIzena")) {
                        editTextErabiltzailea.setText(documentSnapshot.getString("erabiltzaileIzena"));
                    }

                    if (documentSnapshot.contains("argazkiaProfilaUrl")) {
                        argazkiZaharraUrl = documentSnapshot.getString("argazkiaProfilaUrl");

                        if (argazkiZaharraUrl != null) {
                            if (!argazkiZaharraUrl.isEmpty()) {
                                Picasso.with(PerfilaEguneratuActivity.this).load(argazkiZaharraUrl).into(perfilaArgazkiaAldatu);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Argazkia lortzeko metodoa bistaratu
     *
     * @param view
     */
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

    /**
     * Argazkia momentuan atera
     */
    private void argazkiaAtera() {

    }

    /**
     * Perfila aldatu, informazioa bakarrik edo informazioa eta argazkia
     *
     * @param view
     */
    private void perfilaAldatu(View view) {
        String erabiltzailea = editTextErabiltzailea.getText().toString();

        if (erabiltzailea.isEmpty()) {
            Toast.makeText(this, "Erabiltzaile izena jarri behar duzu", Toast.LENGTH_SHORT).show();
        } else {
            if (argazkiaFitxeroa == null) {
                eguneratuInformazioa(erabiltzailea);
            } else {
                argazkiaEguneratu(erabiltzailea);
            }
            Intent intent = new Intent(PerfilaEguneratuActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    /**
     * Informazioa bakarrik eguneratu (argazkia ez)
     *
     * @param erabiltzaileIzena
     */
    private void eguneratuInformazioa(String erabiltzaileIzena) {
        progressBar.setVisibility(View.VISIBLE);

        Erabiltzailea erabiltzailea = new Erabiltzailea();
        erabiltzailea.setId(authProvider.getUid());
        erabiltzailea.setErabiltzaileIzena(erabiltzaileIzena);
        erabiltzailea.setArgazkiaProfilaUrl(argazkiZaharraUrl);

        userProvider.update(erabiltzailea).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PerfilaEguneratuActivity.this, "Erabiltzailea ondo eguneratu da", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PerfilaEguneratuActivity.this, "Errore bat egon da profila eguneratzerakoan", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Argazkia eta informazioa eguneratu
     *
     * @param erabiltzaileIzena
     */
    private void argazkiaEguneratu(String erabiltzaileIzena) {
        progressBar.setVisibility(View.VISIBLE);
        imageProvider.gordeProfilArgazkiaFirebasen(PerfilaEguneratuActivity.this, argazkiaFitxeroa).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageProvider.lortuArgazkiarenKokapena().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Erabiltzailea erabiltzailea = new Erabiltzailea();
                            erabiltzailea.setArgazkiaProfilaUrl(url);
                            erabiltzailea.setErabiltzaileIzena(erabiltzaileIzena);
                            erabiltzailea.setId(authProvider.getUid());

                            userProvider.update(erabiltzailea).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PerfilaEguneratuActivity.this, "Erabiltzailea ondo eguneratu da", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(PerfilaEguneratuActivity.this, "Errore bat egon da profila eguneratzerakoan", Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    /**
     * Atzera botoia ematean alerta bat agertzea
     */
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