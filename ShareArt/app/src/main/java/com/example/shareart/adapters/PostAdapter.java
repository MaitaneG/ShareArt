package com.example.shareart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.models.Argitarapena;
import com.example.shareart.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class PostAdapter extends FirestoreRecyclerAdapter<Argitarapena, PostAdapter.ViewHolder> {
    private Context context;
    private UserProvider userProvider;

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Argitarapena> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Argitarapena model) {
        // Deskripzioa bistaratu
        holder.textViewDeskribapena.setText(model.getDeskribapena());
        // Kategoria
        holder.textViewKategoria.setText(holder.textViewKategoria.getText().toString() + " " + model.getKategoria());
        // Erabiltzailea bistaratu
        userProvider = new UserProvider();
        userProvider.getErabiltzailea(model.getId_user()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                holder.textViewErabiltzaileIzena.setText(documentSnapshot.get("erabiltzaileIzena").toString());
            }
        });
        // Argazkia bistaratu
        if (model.getUrl_argazkia() != null) {
            if (!model.getUrl_argazkia().isEmpty()) {
                Picasso.with(context).load(model.getUrl_argazkia()).into(holder.imageViewArgitarapena);
            }
        }
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


        public ViewHolder(View view) {
            super(view);
            textViewDeskribapena = view.findViewById(R.id.textViewDeskriptionCard);
            textViewErabiltzaileIzena = view.findViewById(R.id.textViewErabiltzaileIzenaCard);
            textViewLikeKopurua = view.findViewById(R.id.textViewLikeCard);
            imageViewArgitarapena = view.findViewById(R.id.imageViewArgitarapenaCard);
            imageViewLike = view.findViewById(R.id.imageViewLike);
            textViewKategoria = view.findViewById(R.id.textViewKategoriaCard);

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
