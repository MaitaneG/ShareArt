package com.example.shareart.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shareart.R;
import com.example.shareart.activities.ArgitarapenBakarraActivity;
import com.example.shareart.activities.BesteErabiltzaileProfilaActivity;
import com.example.shareart.activities.KomentarioakActivity;
import com.example.shareart.models.Argitalpena;
import com.example.shareart.models.FCMBody;
import com.example.shareart.models.FCMResponse;
import com.example.shareart.models.Like;
import com.example.shareart.providers.AuthProvider;
import com.example.shareart.providers.CommentProvider;
import com.example.shareart.providers.LikeProvider;
import com.example.shareart.providers.NotificationProvider;
import com.example.shareart.providers.PostProvider;
import com.example.shareart.providers.TokenProvider;
import com.example.shareart.providers.UserProvider;
import com.example.shareart.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends FirestoreRecyclerAdapter<Argitalpena, PostAdapter.ViewHolder> {
    private final Context context;
    private final UserProvider userProvider;
    private final CommentProvider commentProvider;
    private final LikeProvider likeProvider;
    private final AuthProvider authProvider;
    private final PostProvider postProvider;
    private final TokenProvider tokenProvider;
    private final NotificationProvider notificationProvider;

    private ListenerRegistration listenerRegistrationKomentarioa;
    private ListenerRegistration listenerRegistrationLike;


    private TextView textView;
    private String komentatzailea;

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Argitalpena> options, Context context) {
        super(options);
        this.context = context;
        userProvider = new UserProvider();
        commentProvider = new CommentProvider();
        likeProvider = new LikeProvider();
        authProvider = new AuthProvider();
        postProvider = new PostProvider();
        tokenProvider = new TokenProvider();
        notificationProvider = new NotificationProvider();
    }

    public PostAdapter(@NonNull FirestoreRecyclerOptions<Argitalpena> options, Context context, TextView textView) {
        super(options);
        this.context = context;
        userProvider = new UserProvider();
        commentProvider = new CommentProvider();
        likeProvider = new LikeProvider();
        authProvider = new AuthProvider();
        postProvider = new PostProvider();
        tokenProvider = new TokenProvider();
        notificationProvider = new NotificationProvider();
        this.textView = textView;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Argitalpena model) {
        // Deskripzioa bistaratu
        holder.textViewDeskribapena.setText(model.getDeskribapena());
        // Kategoria
        holder.textViewKategoria.setText("#" + model.getKategoria());
        // Erabiltzailea
        erabiltzaileaBistaratu(model.getId_user(), holder);
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

        // Data
        String relativeTime = RelativeTime.getTimeAgo(model.getData());
        holder.textViewData.setText(relativeTime);

        // Komentario kopurua
        getKomentarioKopurua(post_id, holder);

        // Like kopurua
        getLikeKopurua(post_id, holder);

        // Like botoia hasieratu
        getLike(post_id, authProvider.getUid(), holder);

        isVerified(user_id, holder);

        // Argitalpen kopurua
        if (textView != null) {
            int zenbakia = getSnapshots().size();
            textView.setText(String.valueOf(zenbakia));
        }

        // Erabiltzaile izenean klik egitean
        holder.textViewErabiltzaileIzena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user_id.equals("")) {
                    Intent intent = new Intent(context, BesteErabiltzaileProfilaActivity.class);
                    intent.putExtra("erabiltzaileId", user_id);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Itxaron mesedez...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Komentario argazkian klik egitean
        holder.imageViewKomentatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, KomentarioakActivity.class);
                intent.putExtra("postId", post_id);
                context.startActivity(intent);
            }
        });

        // Like bat ematean
        holder.imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Like like = new Like();
                like.setId_argitalpen(post_id);
                like.setId_erabiltzaile(authProvider.getUid());
                like.setData(new Date().getTime());

                likeBatEman(like, holder);
            }
        });

        holder.imageViewArgitarapena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArgitarapenBakarraActivity.class);
                intent.putExtra("postId", post_id);
                context.startActivity(intent);
            }
        });
    }

    private void isVerified(String user_id, ViewHolder holder) {
        userProvider.getErabiltzailea(user_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    if (value.contains("egiaztatua") && value.getBoolean("egiaztatua") != null && value.getBoolean("egiaztatua")) {
                        holder.imageViewEgiaztatua.setVisibility(View.VISIBLE);
                    } else {
                        holder.imageViewEgiaztatua.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void erabiltzaileaBistaratu(String userId, ViewHolder holder) {
        userProvider.getErabiltzailea(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("erabiltzaile_izena")) {
                        holder.textViewErabiltzaileIzena.setText(documentSnapshot.get("erabiltzaile_izena").toString());
                    }
                }
            }
        });
    }

    private void getKomentarioKopurua(String postId, ViewHolder holder) {
        listenerRegistrationKomentarioa = commentProvider.getKomentarioakByArgitalpen(postId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    holder.textViewKomentarioa.setText(value.size() + "");
                }
            }
        });
    }

    private void getLikeKopurua(String post_id, ViewHolder holder) {
        listenerRegistrationLike = likeProvider.getLikesByArgitalpen(post_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    holder.textViewLikeKopurua.setText(value.size() + "");
                }
            }
        });
    }

    private void likeBatEman(Like like, ViewHolder holder) {
        likeProvider.getLikeByArgitalpenAndErabiltzaile(like.getId_argitalpen(), like.getId_erabiltzaile()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int number = queryDocumentSnapshots.size();
                if (number > 0) {
                    String idQuery = queryDocumentSnapshots.getDocuments().get(0).getId();
                    likeProvider.deleteLike(idQuery);
                    holder.imageViewLike.setImageResource(R.drawable.like_ikonoa);
                    getLikeKopurua(like.getId_argitalpen(), holder);
                } else {
                    likeProvider.createLike(like);
                    holder.imageViewLike.setImageResource(R.drawable.like_ikono_azul);
                    getLikeKopurua(like.getId_argitalpen(), holder);
                    notifikazioBatBidali(like.getId_argitalpen());
                }
            }
        });
    }

    private void notifikazioBatBidali(String id_argitalpen) {
        postProvider.getArgitalpenaById(id_argitalpen).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userProvider.getErabiltzailea(authProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        komentatzailea = documentSnapshot.getString("erabiltzaile_izena");
                    }
                });

                tokenProvider.getToken(documentSnapshot.getString("id_user")).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.contains("token")) {
                            String token = documentSnapshot.getString("token");
                            Map<String, String> data = new HashMap<>();
                            data.put("title", "Like berri bat daukazu");
                            data.put("body", komentatzailea + " like bat eman dizu");
                            data.put("postId", id_argitalpen);
                            FCMBody body = new FCMBody(token, "high", "4500s", data);
                            notificationProvider.sendNotification(body).enqueue(new Callback<FCMResponse>() {
                                @Override
                                public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                    if (response.body() != null) {
                                        if (response.body().getSuccess() == 1) {
                                            Toast.makeText(context, "SE HA ENVIADO EL MENSAJE", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<FCMResponse> call, Throwable t) {

                                }
                            });
                        } else {
                            Toast.makeText(context, "NO HAY TOKEN", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void getLike(String post_id, String user_id, ViewHolder holder) {
        likeProvider.getLikeByArgitalpenAndErabiltzaile(post_id, user_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int number = queryDocumentSnapshots.size();
                if (number > 0) {
                    holder.imageViewLike.setImageResource(R.drawable.like_ikono_azul);
                } else {
                    holder.imageViewLike.setImageResource(R.drawable.like_ikonoa);
                }
            }
        });
    }

    public ListenerRegistration getListenerRegistrationKomentarioa() {
        return listenerRegistrationKomentarioa;
    }

    public ListenerRegistration getListenerRegistrationLike() {
        return listenerRegistrationLike;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_argitalpena, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDeskribapena;
        TextView textViewErabiltzaileIzena;
        TextView textViewLikeKopurua;
        TextView textViewKategoria;
        TextView textViewKomentarioa;
        TextView textViewData;
        ImageView imageViewArgitarapena;
        ImageView imageViewLike;
        ImageView imageViewKomentatu;
        ImageView imageViewEgiaztatua;


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
            imageViewEgiaztatua = view.findViewById(R.id.egiaztatuaCardView);
        }
    }
}
