package com.example.shareart.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.activities.UserProfileActivity;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class PostAdapter extends FirestoreRecyclerAdapter<Argitalpena, PostAdapter.ViewHolder> {
    private Context context;
    private UserProvider userProvider;

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Argitalpena> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Argitalpena model) {
        // Deskripzioa bistaratu
        holder.textViewDeskribapena.setText(model.getDeskribapena());
        // Kategoria
        holder.textViewKategoria.setText(holder.textViewKategoria.getText().toString() + model.getKategoria());
        // Erabiltzailea bistaratu
        userProvider = new UserProvider();
        userProvider.getErabiltzailea(model.getId_user()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("erabiltzaileIzena")) {
                        holder.textViewErabiltzaileIzena.setText(documentSnapshot.get("erabiltzaileIzena").toString());
                    }

                }
            }
        });
        // Argazkia bistaratu
        if (model.getUrl_argazkia() != null) {
            if (!model.getUrl_argazkia().isEmpty()) {
                Picasso.with(context).load(model.getUrl_argazkia()).into(holder.imageViewArgitarapena);
            }
        }
        // Momentuko dokumentua
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String user_id = document.getString("id_user");
        // OnClickListener
        holder.textViewErabiltzaileIzena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user_id.equals("")) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra("erabiltzaileId", user_id);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Itxaron mesedez...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imageViewKomentatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(context);
                editText.setHint("Jarri komentario bat");
                editText.setPadding(20 ,35,25,35);

                // EditText-en marginak zehazteko
                LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(36,36,36,0);

                editText.setLayoutParams(layoutParams);
                RelativeLayout container =new RelativeLayout(context);
                RelativeLayout.LayoutParams relativeParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

                container.setLayoutParams(relativeParams);

                container.addView(editText);
                new AlertDialog.Builder(context)
                        .setView(container)
                        .setPositiveButton("Komentatu", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String komentarioa = editText.getText().toString();
                            }
                        })
                        .setNegativeButton("Itxi", null)
                        .show();

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewDeskribapena;
        TextView textViewErabiltzaileIzena;
        TextView textViewLikeKopurua;
        TextView textViewKategoria;
        ImageView imageViewArgitarapena;
        ImageView imageViewLike;
        ImageView imageViewKomentatu;


        public ViewHolder(View view) {
            super(view);
            textViewDeskribapena = view.findViewById(R.id.textViewDeskriptionCard);
            textViewErabiltzaileIzena = view.findViewById(R.id.textViewErabiltzaileIzenaCard);
            textViewLikeKopurua = view.findViewById(R.id.textViewLikeCard);
            textViewKategoria = view.findViewById(R.id.textViewKategoriaCard);
            imageViewArgitarapena = view.findViewById(R.id.imageViewArgitarapenaCard);
            imageViewLike = view.findViewById(R.id.imageViewLike);
            imageViewKomentatu = view.findViewById(R.id.imageViewKomentatu);

            imageViewLike.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageViewLike:
                    imageViewLike.setImageResource(R.drawable.like_ikonoa_azul);
                    break;
            }
        }
    }
}
