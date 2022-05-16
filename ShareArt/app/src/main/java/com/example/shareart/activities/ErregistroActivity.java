package com.example.shareart.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.shareart.R;
import com.example.shareart.models.Erabiltzailea;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Erregistro pantailaren Activity-a
 */
public class ErregistroActivity extends AppCompatActivity {

    private LinearLayout erregistratuBotoia;
    private TextInputEditText erabiltzaileaEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText pasahitzaEditText;
    private TextInputEditText pasahitzaBaieztatuEditText;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private AuthProvider authProvider;
    private UserProvider userProvider;

    /**
     * Activity-a sortzen denean
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erregistro);
        hasieratu();
    }

    /**
     * Konponenteak hasieratzen dira
     */
    private void hasieratu() {
        // Botoiak
        erregistratuBotoia = findViewById(R.id.buttonErregistratu);
        // EditText
        erabiltzaileaEditText = findViewById(R.id.TextInputEditTextErabiltzaileaErregistratu);
        emailEditText = findViewById(R.id.TextInputEditTextEmailErregistratu);
        pasahitzaEditText = findViewById(R.id.TextInputEditTextPasahitzaErregistratu);
        pasahitzaBaieztatuEditText = findViewById(R.id.TextInputEditTextBaieztatuPasahitzaErregistratu);
        // FireBase
        authProvider = new AuthProvider();
        userProvider = new UserProvider();
        // ProgressBar
        progressBar = (ProgressBar) findViewById(R.id.indeterminateBarRegister);
        progressBar.setVisibility(View.INVISIBLE);
        // Tresna-barra
        toolbar = findViewById(R.id.ToolBar);
        toolbar.setTitle("Erregistratu orain");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        erregistratuBotoia.setOnClickListener(this::erregistratu);
    }

    /**
     * Email-a ondo idatzita dagoen konprobatzen du
     *
     * @param email
     * @return
     */
    private boolean emailaKonprobatu(String email) {
        String expression = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Errgistratzen da
     *
     * @param view
     */
    private void erregistratu(View view) {
        String erabiltzaileIzena = erabiltzaileaEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String pasahitza = pasahitzaEditText.getText().toString().trim();
        String pasahitzaBaeieztatu = pasahitzaBaieztatuEditText.getText().toString().trim();

        if (erabiltzaileIzena.isEmpty() || email.isEmpty() || pasahitza.isEmpty() || pasahitzaBaeieztatu.isEmpty()) {
            Toast.makeText(ErregistroActivity.this, "Gako guztiak bete behar dira.", Toast.LENGTH_SHORT).show();
        } else if (!emailaKonprobatu(email)) {
            Toast.makeText(ErregistroActivity.this, "Emailaren formatua gaizki dago.", Toast.LENGTH_SHORT).show();
        } else {
            if (!pasahitza.equals(pasahitzaBaeieztatu)) {
                Toast.makeText(ErregistroActivity.this, "Pasahitzak ez dira berdinak.", Toast.LENGTH_SHORT).show();
            } else {
                if (pasahitza.length() < 8) {
                    Toast.makeText(ErregistroActivity.this, "Pasahitza oso motza da. Gutxienez 8 karaketere izan behar ditu.", Toast.LENGTH_SHORT).show();
                } else {
                    sortuErabiltzailea(erabiltzaileIzena, email, pasahitza);
                }
            }
        }
    }

    /**
     * Erabiltzailea sortzen du FireBase Authentification-en eta Firebaseko datubasean
     *
     * @param erabiltzaileIzena
     * @param email
     * @param pasahitza
     */
    private void sortuErabiltzailea(String erabiltzaileIzena, String email, String pasahitza) {
        progressBar.setVisibility(View.VISIBLE);
        authProvider.erregistratu(email, pasahitza).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    String id = authProvider.getUid();
                    Erabiltzailea erabiltzailea = new Erabiltzailea();
                    erabiltzailea.setId(id.trim());
                    erabiltzailea.setErabiltzaile_izena(erabiltzaileIzena.trim());
                    erabiltzailea.setEmail(email.trim());

                    erabiltzailea.setSortze_data(new Date().getTime());

                    userProvider.createErabiltzailea(erabiltzailea);
                    Toast.makeText(ErregistroActivity.this, "Erabiltzailea ondo sortu da", Toast.LENGTH_SHORT).show();

                    egiaztatzekoMezua();
                } else {
                    Toast.makeText(ErregistroActivity.this, "Arazo bat egon da erabiltzailea sortzean", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void egiaztatzekoMezua() {
        new AlertDialog.Builder(this)
                .setMessage(Html.fromHtml("Zure korreoa egiaztatu nahi duzu?<br><br><i>Ez baduzu orain egiten geroago egin ahal dezakezu konfigurazio pantailan</i>"))
                .setPositiveButton("Ongi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ErregistroActivity.this, KonfigurazioaActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Orain ez", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(ErregistroActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * Alerta bat agertzea atzera joatean
     */
    private void atzeraAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Erregistro pantailatik irteten")
                .setMessage("Ziur zaude joan nahi zarela erregistratu gabe?")
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
     * Hasierako menura joateko botoia klikatzean
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            atzeraAlertDialog();
        }
        return true;
    }

    /**
     * Atzera botoia ematean alerta bat agertzea
     */
    @Override
    public void onBackPressed() {
        atzeraAlertDialog();
    }
}