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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class PostAdapter extends FirestoreRecyclerAdapter<Argitarapena, PostAdapter.ViewHolder> {
private Context context;

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Argitarapena> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Argitarapena model) {
        holder.textViewDeskribapena.setText(model.getDeskribapena());
        if(model.getUrl_argazkia()!=null){
            if(!model.getUrl_argazkia().isEmpty()){
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDeskribapena;
        ImageView imageViewArgitarapena;

        public ViewHolder(View view) {
            super(view);
            textViewDeskribapena = view.findViewById(R.id.textViewDeskriptionCard);
            imageViewArgitarapena = view.findViewById(R.id.imageViewArgitarapenaCard);
        }
    }
}
