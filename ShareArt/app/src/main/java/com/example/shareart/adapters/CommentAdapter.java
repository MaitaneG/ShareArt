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
import com.example.shareart.models.Komentarioa;
import com.example.shareart.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class CommentAdapter extends FirestoreRecyclerAdapter<Komentarioa, CommentAdapter.ViewHolder> {
    private Context context;
    private UserProvider userProvider;

    public CommentAdapter(@NonNull FirestoreRecyclerOptions<Komentarioa> options, Context context) {
        super(options);
        this.context = context;
        userProvider = new UserProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Komentarioa model) {
        // Momentuko dokumentua
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String comment_id = document.getId();

        holder.textViewKomentarioa.setText(model.getMezua());

        // Erabiltzailea bistaratu
        userProvider.getErabiltzailea(model.getIdErabiltzailea()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("erabiltzaileIzena")) {
                        holder.textViewErabiltzaileIzena.setText(documentSnapshot.getString("erabiltzaileIzena"));
                    }

                    if (documentSnapshot.contains("argazkiaProfilaUrl")) {
                        if (documentSnapshot.getString("argazkiaProfilaUrl") != null) {
                            if (!documentSnapshot.getString("argazkiaProfilaUrl").isEmpty()) {
                                Picasso.with(context).load(documentSnapshot.getString("argazkiaProfilaUrl")).into(holder.imageViewProfilekoArgakia);
                            }
                        }
                    }
                }
            }
        });

        holder.textViewErabiltzaileIzena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!model.getIdErabiltzailea().equals("")) {
                    erabiltzailePerfilaIkusi(model.getIdErabiltzailea());
                } else {
                    Toast.makeText(context, "Itxaron mesedez...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imageViewProfilekoArgakia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                erabiltzailePerfilaIkusi(model.getIdErabiltzailea());
            }
        });

        holder.textViewData.setText(model.getData());
    }

    private void erabiltzailePerfilaIkusi(String idErabiltzaile){
        Intent intent = new Intent(context, UserProfileActivity.class);
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

        private TextView textViewErabiltzaileIzena;
        private TextView textViewKomentarioa;
        private TextView textViewData;
        private ImageView imageViewProfilekoArgakia;

        public ViewHolder(View view) {
            super(view);

            textViewErabiltzaileIzena = view.findViewById(R.id.textViewErabiltzaileIzenaKomentarioa);
            textViewKomentarioa = view.findViewById(R.id.textViewKomentarioa);
            textViewData = view.findViewById(R.id.textViewDataKomentarioa);
            imageViewProfilekoArgakia = view.findViewById(R.id.imageViewPerfilArgazkiaKomentarioa);
        }
    }
}