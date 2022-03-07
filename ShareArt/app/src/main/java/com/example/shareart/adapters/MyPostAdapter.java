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
import com.example.shareart.activities.KomentarioakActivity;
import com.example.shareart.activities.UserProfileActivity;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.models.Like;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.CommentProvider;
import com.example.shareart.providers.LikeProvider;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.providers.UserProvider;
import com.example.shareart.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends FirestoreRecyclerAdapter<Argitalpena, MyPostAdapter.ViewHolder> {
    private Context context;
    private PostProvider postProvider;

    public MyPostAdapter(@NonNull FirestoreRecyclerOptions<Argitalpena> options, Context context) {
        super(options);
        this.context = context;
        postProvider = new PostProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Argitalpena model) {
        holder.textViewDeskribapena.setText(model.getDeskribapena());
        holder.textViewKategoria.setText("#" + model.getKategoria());

        String relativeTime = RelativeTime.getTimeAgo(model.getData());
        holder.textViewData.setText(relativeTime);

        if (model.getUrl_argazkia() != null) {
            if (!model.getUrl_argazkia().isEmpty()) {
                Picasso.with(context).load(model.getUrl_argazkia()).into(holder.imageViewArgitalpena);
            }
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
                            }
                        })
                        .setNegativeButton("Ez", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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
        private CircleImageView imageViewArgitalpena;
        private ImageView imageViewEzabatuArgitalpena;
        private TextView textViewDeskribapena;
        private TextView textViewKategoria;
        private TextView textViewData;

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
