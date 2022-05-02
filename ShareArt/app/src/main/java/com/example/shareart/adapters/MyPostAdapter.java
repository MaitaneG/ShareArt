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
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.activities.ArgitarapenBakarraActivity;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.ImageProvider;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends FirestoreRecyclerAdapter<Argitalpena, MyPostAdapter.ViewHolder> {
    private final Context context;
    private final PostProvider postProvider;
    private final AuthProvider authProvider;
    private final ImageProvider imageProvider;

    public MyPostAdapter(@NonNull FirestoreRecyclerOptions<Argitalpena> options, Context context) {
        super(options);
        this.context = context;
        postProvider = new PostProvider();
        authProvider = new AuthProvider();
        imageProvider = new ImageProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Argitalpena model) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String post_id = document.getId();

        holder.textViewDeskribapena.setText(model.getDeskribapena());
        holder.textViewKategoria.setText("#" + model.getKategoria());

        String relativeTime = RelativeTime.getTimeAgo(model.getData());
        holder.textViewData.setText(relativeTime);

        if (model.getUrl_argazkia() != null) {
            if (!model.getUrl_argazkia().isEmpty()) {
                Picasso.with(context).load(model.getUrl_argazkia()).into(holder.imageViewArgitalpena);
            }
        }

        if (!authProvider.getUid().equals(model.getId_user())) {
            holder.imageViewEzabatuArgitalpena.setVisibility(View.INVISIBLE);
        }

        holder.imageViewEzabatuArgitalpena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Argitalpena ezabatzen")
                        .setMessage("Ziur zaude argitalpena ezabatu nahi duzula?")
                        .setPositiveButton("Bai", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                postProvider.deleteArgitalpena(model.getId());
                                imageProvider.deleteArgazkia(model.getUrl_argazkia());
                            }
                        })
                        .setNegativeButton("Ez", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        holder.imageViewArgitalpena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArgitarapenBakarraActivity.class);
                intent.putExtra("postId", post_id);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_nire_argitalpenak, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView imageViewArgitalpena;
        private final ImageView imageViewEzabatuArgitalpena;
        private final TextView textViewDeskribapena;
        private final TextView textViewKategoria;
        private final TextView textViewData;

        public ViewHolder(View view) {
            super(view);

            imageViewArgitalpena = view.findViewById(R.id.imageViewNireArgitalpena);
            imageViewEzabatuArgitalpena = view.findViewById(R.id.imageViewEzabatuArgitalpena);
            textViewDeskribapena = view.findViewById(R.id.textViewDeskribapenaNirea);
            textViewKategoria = view.findViewById(R.id.textViewKategoriaNirea);
            textViewData = view.findViewById(R.id.textViewDataNirea);
        }
    }
}
