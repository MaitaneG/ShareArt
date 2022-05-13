package com.example.shareart.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Perfila eguneratzeko pantailaren Activity-a
 */
public class ProfilaEguneratuActivity extends AppCompatActivity {

    private CircleImageView perfilaArgazkiaAldatu;
    private TextInputEditText editTextErabiltzailea;
    private TextInputEditText editTextDeskribapena;
    private LinearLayout perfilaAldatuBotoia;
    private AlertDialog.Builder alertDialog;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private File argazkiaFitxeroa;
    private Uri imageUri;

    private CharSequence[] options;

    private ImageProvider imageProvider;
    private UserProvider userProvider;
    private AuthProvider authProvider;

    private String argazkiZaharraUrl;

    private static final int CAMERA_PERMISSION_CODE = 100;

    /**
     * Activity-a sortzen denean
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profila_eguneratu);
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
        editTextDeskribapena=findViewById(R.id.textInputEditTextDeskribapenaAldatu);
        // ImageButton
        perfilaAldatuBotoia = findViewById(R.id.buttonPerfilaAldatu);
        // Toolbar
        toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profila eguneratu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        userProvider.getErabiltzailea(authProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("erabiltzaile_izena")) {
                        editTextErabiltzailea.setText(documentSnapshot.getString("erabiltzaile_izena"));
                    }

                    if (documentSnapshot.contains("argazkia_profila_url")) {
                        argazkiZaharraUrl = documentSnapshot.getString("argazkia_profila_url");

                        if (argazkiZaharraUrl != null) {
                            if (!argazkiZaharraUrl.isEmpty()) {
                                Picasso.with(ProfilaEguneratuActivity.this).load(argazkiZaharraUrl).into(perfilaArgazkiaAldatu);
                            }else {
                                argazkiZaharraUrl="";
                            }
                        }else{
                            argazkiZaharraUrl="";
                        }
                    }

                    if (documentSnapshot.contains("deskribapena")){
                        if (documentSnapshot.getString("deskribapena") != null) {
                            if (!documentSnapshot.getString("deskribapena").isEmpty()) {
                                editTextDeskribapena.setText(documentSnapshot.getString("deskribapena"));
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
                    checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                }
            }
        }).show();
    }

    /**
     * Argazkia momentuan atera
     */
    private void argazkiaAtera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        getCameraImage.launch(imageUri);
    }

    /**
     * Mugikorraren galeria zabaltzeko
     */
    private void galeriaZabaldu() {
        Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galeriaIntent.setType("image/");
        getGalleryImage.launch(galeriaIntent);
    }

    /**
     * Argazkia aukeratzeko galeriatik
     */
    ActivityResultLauncher<Intent> getGalleryImage = registerForActivityResult(
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
                            if (imageUri != null) {
                                argazkiaFitxeroa = FileUtil.from(ProfilaEguneratuActivity.this, imageUri);
                                perfilaArgazkiaAldatu.setImageBitmap(BitmapFactory.decodeFile(argazkiaFitxeroa.getAbsolutePath()));
                            }
                        } catch (Exception ex) {
                            Toast.makeText(ProfilaEguneratuActivity.this, "Errore bat egon da", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    ActivityResultLauncher<Uri> getCameraImage = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        try {
                            if (imageUri != null) {
                                argazkiaFitxeroa = FileUtil.from(ProfilaEguneratuActivity.this, imageUri);
                                perfilaArgazkiaAldatu.setImageBitmap(BitmapFactory.decodeFile(argazkiaFitxeroa.getAbsolutePath()));

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    /**
     * Perfila aldatu, informazioa bakarrik edo informazioa eta argazkia
     *
     * @param view
     */
    private void perfilaAldatu(View view) {
        String erabiltzailea = editTextErabiltzailea.getText().toString().trim();
        String deskribapena = editTextDeskribapena.getText().toString().trim();

        if (erabiltzailea.isEmpty()) {
            Toast.makeText(this, "Erabiltzaile izena jarri behar duzu", Toast.LENGTH_SHORT).show();
        } else {
            if (argazkiaFitxeroa == null) {
                eguneratuInformazioa(erabiltzailea,deskribapena);
            } else {
                argazkiaEguneratu(erabiltzailea,deskribapena);
            }
            Intent intent = new Intent(ProfilaEguneratuActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    /**
     * Informazioa bakarrik eguneratu (argazkia ez)
     *
     * @param erabiltzaileIzena
     */
    private void eguneratuInformazioa(String erabiltzaileIzena, String deskribapena) {
        progressBar.setVisibility(View.VISIBLE);

        Erabiltzailea erabiltzailea = new Erabiltzailea();
        erabiltzailea.setId(authProvider.getUid());
        erabiltzailea.setErabiltzaile_izena(erabiltzaileIzena.trim());
        erabiltzailea.setDeskribapena(deskribapena.trim());
        erabiltzailea.setArgazkia_profila_url(argazkiZaharraUrl.trim());

        userProvider.updateErabiltzailea(erabiltzailea).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfilaEguneratuActivity.this, "Erabiltzailea ondo eguneratu da", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ProfilaEguneratuActivity.this, "Errore bat egon da profila eguneratzerakoan", Toast.LENGTH_SHORT).show();
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
    private void argazkiaEguneratu(String erabiltzaileIzena, String deskribapena) {
        progressBar.setVisibility(View.VISIBLE);
        imageProvider.saveProfilArgazkiaFirebasen(ProfilaEguneratuActivity.this, argazkiaFitxeroa).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageProvider.getArgazkiarenKokapena().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Erabiltzailea erabiltzailea = new Erabiltzailea();
                            erabiltzailea.setArgazkia_profila_url(url);
                            erabiltzailea.setErabiltzaile_izena(erabiltzaileIzena);
                            erabiltzailea.setDeskribapena(deskribapena);
                            erabiltzailea.setId(authProvider.getUid());

                            userProvider.updateErabiltzailea(erabiltzailea).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (argazkiZaharraUrl!=null && !argazkiZaharraUrl.isEmpty()){
                                            imageProvider.deleteArgazkia(argazkiZaharraUrl);
                                        }
                                        Toast.makeText(ProfilaEguneratuActivity.this, "Erabiltzailea ondo eguneratu da", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(ProfilaEguneratuActivity.this, "Errore bat egon da profila eguneratzerakoan", Toast.LENGTH_SHORT).show();
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

    private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(ProfilaEguneratuActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(ProfilaEguneratuActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(ProfilaEguneratuActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            argazkiaAtera();
        }
    }

    /**
     * Atzera botoia ematean alerta bat agertzea
     */
    @Override
    public void onBackPressed() {
        alert();
    }

    private void alert(){
        new AlertDialog.Builder(this)
                .setTitle("Perfila editatzeko pantailatik irteten")
                .setMessage("Ziur zaude joan nahi zarela perfila editatu gabe?")
                .setPositiveButton("Bai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Ez", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            alert();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ProfilaEguneratuActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                argazkiaAtera();
            } else {
                Toast.makeText(ProfilaEguneratuActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}