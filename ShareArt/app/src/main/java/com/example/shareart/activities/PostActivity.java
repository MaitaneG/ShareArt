package com.example.shareart.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Argitalen pantailaren Activity-a
 */
public class PostActivity extends AppCompatActivity {

    private ImageView argazkiaIgoBotoia;
    private ImageButton argitaratuBotoia;
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

    private File argazkiaFitxeroa;

    private ImageProvider imageProvider;
    private PostProvider postProvider;
    private AuthProvider authProvider;

    private CharSequence options[];

    private String absolutePhotoPath;
    private String photoPath;
    private File ateraArgazkiaFitxeroa;

    /**
     * PostActivity-a sortzen denean
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        hasieratu();
    }

    /**
     * Konponenteak hasieratzen dira
     */
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
        // AlertDialog
        alertDialog = new AlertDialog.Builder(this).setTitle("Hautatu aukera bat");
        options = new CharSequence[]{"Galeriako argazki bat", "Argazkia atera"};
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
        // ImageProvider
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
                    argazkiaAtera();
                }
            }
        }).show();
    }

    /**
     * Mugikorraren galeria zabaltzeko
     */
    private void argazkiaAtera() {
        Intent argazkiaAteraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (argazkiaAteraintent.resolveActivity(getPackageManager()) != null) {
            try {
                File argazkiaFile = null;
                argazkiaFile = argazkiaAteraEtaKargatu();

                if (argazkiaFile != null) {
                    Uri argazkiUri = FileProvider.getUriForFile(PostActivity.this, "com.example.shareart", argazkiaFile);
                    argazkiaAteraintent.putExtra(MediaStore.EXTRA_OUTPUT, argazkiUri);
                    someActivityResultLauncher.launch(argazkiaAteraintent);
                }
            } catch (Exception ex) {
                Toast.makeText(this, "Fitxategiarekin arazo bat egon da. " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File argazkiaAteraEtaKargatu() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File ateraArgazkiaFitxero = null;
        try {
            ateraArgazkiaFitxero = File.createTempFile(new Date() + "_photo", ".jpg", storageDir);
            photoPath = "file:" + ateraArgazkiaFitxero.getAbsolutePath();
            absolutePhotoPath = ateraArgazkiaFitxero.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ateraArgazkiaFitxero;
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
                            argazkiaFitxeroa = FileUtil.from(PostActivity.this, imageUri);
                            argazkiaIgoBotoia.setImageBitmap(BitmapFactory.decodeFile(argazkiaFitxeroa.getAbsolutePath()));
                            argazkiaIgoBotoia.setBackgroundColor(getResources().getColor(R.color.white));
                        } catch (Exception ex) {
                            Toast.makeText(PostActivity.this, "Errore bat egon da", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
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
        imageProvider.saveArgitalpenaFirebasen(PostActivity.this, argazkiaFitxeroa).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageProvider.getArgazkiarenKokapena().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            Argitalpena argitalpena = new Argitalpena();
                            argitalpena.setUrl_argazkia(url);
                            argitalpena.setDeskribapena(deskripzioa);
                            argitalpena.setKategoria(kategoria);
                            argitalpena.setId_user(authProvider.getUid());

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            argitalpena.setData(format.format(new Date()));
                            postProvider.createArgitalpena(argitalpena).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskGorde) {
                                    if (taskGorde.isSuccessful()) {
                                        Toast.makeText(PostActivity.this, "Argazkia ondo argitaratu da", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PostActivity.this, "Arazo bat egon da argazkia argitaratzean", Toast.LENGTH_SHORT).show();
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
                .setTitle("Argitarapen pantailatik irteten")
                .setMessage("Ziur zaude irten nahi zarela pantaila honetatik argitaratu gabe?")
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