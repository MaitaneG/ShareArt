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
import com.example.shareart.activities.KomentarioakActivity;
import com.example.shareart.activities.UserProfileActivity;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.CommentProvider;
import com.example.shareart.providers.UserProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class PostAdapter extends FirestoreRecyclerAdapter<Argitalpena, PostAdapter.ViewHolder> {
    private Context context;
    private UserProvider userProvider;
    private CommentProvider commentProvider;

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Argitalpena> options, Context context) {
        super(options);
        this.context = context;
        userProvider = new UserProvider();
        commentProvider = new CommentProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Argitalpena model) {
        // Deskripzioa bistaratu
        holder.textViewDeskribapena.setText(model.getDeskribapena());
        // Kategoria
        holder.textViewKategoria.setText(holder.textViewKategoria.getText().toString() + model.getKategoria());
        // Erabiltzailea bistaratu
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
        String post_id = document.getId();
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

                Intent intent = new Intent(context, KomentarioakActivity.class);
                intent.putExtra("postId", post_id);
                context.startActivity(intent);
            }
        });

        holder.textViewData.setText(model.getData());
        getKomentarioKopurua(post_id, holder);
    }

    private void getKomentarioKopurua(String postId, final ViewHolder holder) {
        commentProvider.getKomentarioakByArgitalpen(postId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int zenbat = queryDocumentSnapshots.size();
                holder.textViewKomentarioa.setText(zenbat + "");
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
        TextView textViewKomentarioa;
        TextView textViewData;
        ImageView imageViewArgitarapena;
        ImageView imageViewLike;
        ImageView imageViewKomentatu;


        public ViewHolder(View view) {
            super(view);
            textViewDeskribapena = view.findViewById(R.id.textViewDeskriptionCard);
            textViewErabiltzaileIzena = view.findViewById(R.id.textViewErabiltzaileIzenaCard);
            textViewLikeKopurua = view.findViewById(R.id.textViewLikeCard);
            textViewKategoria = view.findViewById(R.id.textViewKategoriaCard);
            textViewKomentarioa = view.findViewById(R.id.textViewKomentarioaCard);
            textViewData = view.findViewById(R.id.textViewDataCard);
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
