package com.example.shareart.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.activities.BesteErabiltzaileProfilaActivity;
import com.example.shareart.models.Komentarioa;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.CommentProvider;
import com.example.shareart.providers.UserProvider;
import com.example.shareart.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class CommentAdapter extends FirestoreRecyclerAdapter<Komentarioa, CommentAdapter.ViewHolder> {
    private final Context context;
    private final UserProvider userProvider;
    private final CommentProvider commentProvider;
    private final AuthProvider authProvider;

    public CommentAdapter(@NonNull FirestoreRecyclerOptions<Komentarioa> options, Context context) {
        super(options);
        this.context = context;
        userProvider = new UserProvider();
        commentProvider = new CommentProvider();
        authProvider = new AuthProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Komentarioa model) {
        // Momentuko dokumentua
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String comment_id = document.getId();

        holder.textViewKomentarioa.setText(model.getMezua());

        // Erabiltzailea bistaratu
        erabiltzaileaBistaratu(model.getId_erabiltzailea(), holder);

        // Data
        String relativeTime = RelativeTime.getTimeAgo(model.getData());
        holder.textViewData.setText(relativeTime);

        // Erabltzailearen argazkian klik egitean
        holder.imageViewProfilekoArgakia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                erabiltzailePerfilaIkusi(model.getId_erabiltzailea());
            }
        });

        // Erabiltzailearen izenean klik egitean
        holder.textViewErabiltzaileIzena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!model.getId_erabiltzailea().equals("")) {
                    erabiltzailePerfilaIkusi(model.getId_erabiltzailea());
                } else {
                    Toast.makeText(context, "Itxaron mesedez...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (model.getId_erabiltzailea().equals(authProvider.getUid())) {
            holder.cardViewKomentarioa.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    new AlertDialog.Builder(context)
                            .setTitle("Komentarioa ezabatzen")
                            .setMessage("Ziur zaude komentarioa ezabatu nahi duzula?")
                            .setPositiveButton("Bai", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    commentProvider.deleteKomentarioa(model.getId());
                                }
                            })
                            .setNegativeButton("Ez", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;
                }
            });
        }
    }

    private void erabiltzaileaBistaratu(String erabiltzaileId, ViewHolder holder) {
        userProvider.getErabiltzailea(erabiltzaileId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("erabiltzaile_izena")) {
                        holder.textViewErabiltzaileIzena.setText(documentSnapshot.getString("erabiltzaile_izena"));
                    }

                    if (documentSnapshot.contains("argazkia_profila_url")) {
                        if (documentSnapshot.getString("argazkia_profila_url") != null) {
                            if (!documentSnapshot.getString("argazkia_profila_url").isEmpty()) {
                                Picasso.with(context).load(documentSnapshot.getString("argazkia_profila_url")).into(holder.imageViewProfilekoArgakia);
                            }
                        }
                    }
                }
            }
        });
    }

    private void erabiltzailePerfilaIkusi(String idErabiltzaile) {
        Intent intent = new Intent(context, BesteErabiltzaileProfilaActivity.class);
        intent.putExtra("erabiltzaileId", idErabiltzaile);
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_komentarioa, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewErabiltzaileIzena;
        private final TextView textViewKomentarioa;
        private final TextView textViewData;
        private final ImageView imageViewProfilekoArgakia;
        private final CardView cardViewKomentarioa;

        public ViewHolder(View view) {
            super(view);

            textViewErabiltzaileIzena = view.findViewById(R.id.textViewErabiltzaileIzenaKomentarioa);
            textViewKomentarioa = view.findViewById(R.id.textViewKomentarioa);
            textViewData = view.findViewById(R.id.textViewDataKomentarioa);
            imageViewProfilekoArgakia = view.findViewById(R.id.imageViewPerfilArgazkiaKomentarioa);
            cardViewKomentarioa = view.findViewById(R.id.cardViewKomentarioa);
        }
    }
}