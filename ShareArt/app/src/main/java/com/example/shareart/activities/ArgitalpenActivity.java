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
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.ImageProvider;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Argitalen pantailaren Activity-a
 */
public class ArgitalpenActivity extends AppCompatActivity {

    private ImageView argazkiaIgoBotoia;
    private LinearLayout argitaratuBotoia;
    private ProgressBar progressBar;
    private AlertDialog.Builder alertDialog;
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
    private ImageView imageViewAbstraktua;
    private Toolbar toolbar;

    private File argazkiaFitxeroa;
    private Uri imageUri;

    private ImageProvider imageProvider;
    private PostProvider postProvider;
    private AuthProvider authProvider;

    private CharSequence[] options;

    private static final int CAMERA_PERMISSION_CODE = 100;

    /**
     * PostActivity-a sortzen denean
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argitalpen);

        hasieratu();
    }

    /**
     * Konponenteak hasieratzen dira
     */
    private void hasieratu() {
        // ImageButton
        argazkiaIgoBotoia = findViewById(R.id.ImageViewArgazkiaIgo);
        argitaratuBotoia = findViewById(R.id.buttonArgitaratu);
        // ImageView
        imageViewNatura = findViewById(R.id.imageViewNatura);
        imageViewHiperrealismoa = findViewById(R.id.imageViewHiperrealismo);
        imageViewErretratoa = findViewById(R.id.imageViewErretratoak);
        imageViewGraffiti = findViewById(R.id.imageViewGraffiti);
        imageViewKomikia = findViewById(R.id.imageViewKomikia);
        imageViewIlustrazioa = findViewById(R.id.imageViewIlustrazioa);
        imageViewKarikatura = findViewById(R.id.imageViewKarikatura);
        imageViewNaturaHila = findViewById(R.id.imageViewNaturaHila);
        imageViewAbstraktua=findViewById(R.id.imageViewAbstraktua);
        // TextView
        textViewKategoria = findViewById(R.id.textViewAukeratutakoKategoria);
        // EditText
        editTextDeskripzioa = findViewById(R.id.textInputEditTextDeskripzioa);
        // ProgressBar
        progressBar = findViewById(R.id.indeterminateBarPost);
        progressBar.setVisibility(View.INVISIBLE);
        // AlertDialog
        alertDialog = new AlertDialog.Builder(this).setTitle("Hautatu aukera bat");
        options = new CharSequence[]{"Galeriako argazki bat", "Argazkia atera"};
        // Tresna-barra
        toolbar = findViewById(R.id.ToolBar);
        toolbar.setTitle("Argitaratu zure marrazkia");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // OnClickListener
        argazkiaIgoBotoia.setOnClickListener(this::argakiaAukeratzekoMetodoa);
        argitaratuBotoia.setOnClickListener(this::argitaratu);
        imageViewNatura.setOnClickListener(this::kategoriaAldatu);
        imageViewHiperrealismoa.setOnClickListener(this::kategoriaAldatu);
        imageViewErretratoa.setOnClickListener(this::kategoriaAldatu);
        imageViewGraffiti.setOnClickListener(this::kategoriaAldatu);
        imageViewKomikia.setOnClickListener(this::kategoriaAldatu);
        imageViewIlustrazioa.setOnClickListener(this::kategoriaAldatu);
        imageViewKarikatura.setOnClickListener(this::kategoriaAldatu);
        imageViewNaturaHila.setOnClickListener(this::kategoriaAldatu);
        imageViewAbstraktua.setOnClickListener(this::kategoriaAldatu);
        // Providers
        imageProvider = new ImageProvider();
        postProvider = new PostProvider();
        authProvider = new AuthProvider();
    }

    /**
     * Argazkiaren kategoria aldatzeko, baldin eta zer botoi klikatzen den
     *
     * @param view
     */
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
            case R.id.imageViewAbstraktua:
                textViewKategoria.setText("Abstraktua");
        }
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
     * Mugikorraren galeria zabaltzeko
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
        galeriaIntent.setType("image/*");
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
                            imageUri = data.getData();
                            if (imageUri != null) {
                                argazkiaFitxeroa = FileUtil.from(ArgitalpenActivity.this, imageUri);
                                argazkiaIgoBotoia.setImageBitmap(BitmapFactory.decodeFile(argazkiaFitxeroa.getAbsolutePath()));
                            }
                        } catch (Exception ex) {
                            Toast.makeText(ArgitalpenActivity.this, "Errore bat egon da", Toast.LENGTH_SHORT).show();
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
                                argazkiaFitxeroa = FileUtil.from(ArgitalpenActivity.this, imageUri);
                                argazkiaIgoBotoia.setImageBitmap(BitmapFactory.decodeFile(argazkiaFitxeroa.getAbsolutePath()));
                                argazkiaIgoBotoia.setBackgroundColor(getResources().getColor(R.color.white));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    /**
     * Argazkia argitaratzeko
     *
     * @param view
     */
    private void argitaratu(View view) {
        String deskripzioa = editTextDeskripzioa.getText().toString();
        String kategoria = textViewKategoria.getText().toString();

        if (argazkiaFitxeroa == null) {
            Toast.makeText(this, "Argazkia aukeratu behar duzu", Toast.LENGTH_SHORT).show();
        } else {
            if (deskripzioa.isEmpty() || kategoria.isEmpty()) {
                Toast.makeText(this, "Deskripzoa eta kategoria zehaztu behar duzu.", Toast.LENGTH_SHORT).show();
            } else {
                gordeArgazkia(deskripzioa, kategoria);
                Intent intent = new Intent(ArgitalpenActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    /**
     * Argazkia gordetzeko Firestore-en
     * eta bere informazioa ere gordetzeko
     */
    private void gordeArgazkia(String deskripzioa, String kategoria) {
        progressBar.setVisibility(View.VISIBLE);
        imageProvider.saveArgitalpenaFirebasen(ArgitalpenActivity.this, argazkiaFitxeroa).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageProvider.getArgazkiarenKokapena().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            Argitalpena argitalpena = new Argitalpena();
                            argitalpena.setUrl_argazkia(url.trim());
                            argitalpena.setDeskribapena(deskripzioa.trim());
                            argitalpena.setKategoria(kategoria.trim());
                            argitalpena.setId_user(authProvider.getUid());

                            argitalpena.setData(new Date().getTime());
                            postProvider.createArgitalpena(argitalpena).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskGorde) {
                                    if (taskGorde.isSuccessful()) {
                                        Toast.makeText(ArgitalpenActivity.this, "Argazkia ondo argitaratu da", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ArgitalpenActivity.this, "Arazo bat egon da argazkia argitaratzean", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            atzeraAlertDialog();
        }
        return true;
    }

    private void atzeraAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Argitarapen pantailatik irteten")
                .setMessage("Ziur zaude joan nahi zarela argitaratu gabe?")
                .setPositiveButton("Bai", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("Ez", null)
                .show();
    }

    /**
     * Atzera botoia ematean alerta bat agertzea
     */
    @Override
    public void onBackPressed() {
        atzeraAlertDialog();
    }

    private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(ArgitalpenActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(ArgitalpenActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(ArgitalpenActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            argazkiaAtera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ArgitalpenActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                argazkiaAtera();
            } else {
                Toast.makeText(ArgitalpenActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}