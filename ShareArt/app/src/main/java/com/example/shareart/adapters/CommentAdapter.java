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
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Komentarioa model) {

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
        private ImageView imageViewProfilekoArgakia;

        public ViewHolder(View view) {
            super(view);

            textViewErabiltzaileIzena = view.findViewById(R.id.textViewErabiltzaileIzenaKomentarioa);
            textViewKomentarioa = view.findViewById(R.id.textViewKomentarioa);
            imageViewProfilekoArgakia = view.findViewById(R.id.imageViewPerfilArgazkiaKomentarioa);
        }
    }
}