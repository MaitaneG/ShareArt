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
import com.example.shareart.models.Argitarapena;
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
    private PostProvider postProvider;
    private AuthProvider authProvider;

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
        postProvider = new PostProvider();
        authProvider=new AuthProvider();
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
        imageProvider.gordeFirebasen(PostActivity.this, argazkiaFitxeroa).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageProvider.lortuArgazkiarenKokapena().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            Argitarapena argitarapena = new Argitarapena();
                            argitarapena.setUrl_argazkia(url);
                            argitarapena.setDeskribapena(deskripzioa);
                            argitarapena.setKategoria(kategoria);
                            argitarapena.setId_user(authProvider.getUid());
                            postProvider.gordeArgitarapenarenInformazioa(argitarapena).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskGorde) {
                                    if (taskGorde.isSuccessful()) {
                                        Toast.makeText(PostActivity.this, "Argazkia ondo igo da", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PostActivity.this, "Arazo bat egon da argazkia igotzen", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    Toast.makeText(PostActivity.this, "Ondo igo da argazkia", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostActivity.this, "Errore bat egon da argazkia igotzean", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Mugikorraren galeria zabaltzeko
     *
     * @param view
     */
    private void galeriaZabaldu(View view) {
        //Intent galeriaIntent = new Intent(Intent.ACTION_GET_CONTENT);
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