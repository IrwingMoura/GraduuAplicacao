package graduuaplicacao.graduuaplicacao.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import graduuaplicacao.graduuaplicacao.DAO.ConfiguracaoFirebase;
import graduuaplicacao.graduuaplicacao.GlideModule.GlideApp;
import graduuaplicacao.graduuaplicacao.Model.Evento;
import graduuaplicacao.graduuaplicacao.R;

public class VerticalCardsAdapter extends RecyclerView.Adapter{

    private final String TAG = getClass().getName();

    private List<Evento> eventos;
    private Context context;
    private ClickListener clickListener;
    private FirebaseAnalytics analytics;
    private DatabaseReference likesRef, likesCheckRef;
    private boolean checker = false;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseUser user = mAuth.getCurrentUser();

    String userID = user.getUid();

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public VerticalCardsAdapter(List<Evento> eventos, Context context) {
        this.eventos = eventos;
        this.context = context;
    }

    public class NossoViewHolder extends RecyclerView.ViewHolder {

        final TextView txtViewNome;
        final TextView txtViewHoraInicio;
        final TextView txtViewData;
        final CheckBox btnLike;
        final Button btnShare;
        final CircleImageView imagemPerfilCard;
        final ImageView fundoImagemCardView;


        public NossoViewHolder(View view) {
            super(view);
            imagemPerfilCard = (CircleImageView) view.findViewById(R.id.imagemPerfilCard);
            txtViewNome = (TextView) view.findViewById(R.id.txtViewNome);
            txtViewHoraInicio = (TextView) view.findViewById(R.id.txtViewHoraInicio);
            txtViewData = (TextView) view.findViewById(R.id.txtViewData);
            btnLike = (CheckBox) view.findViewById(R.id.likeIcon);
            btnShare = (Button) view.findViewById(R.id.btnShare);
            fundoImagemCardView = (ImageView) view.findViewById(R.id.fundoImagemCardView);


            likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // context.startActivity(new Intent(context, EventoAbertoActivity.class));

                    if(clickListener!=null) {
                        clickListener.itemClicked(v,getAdapterPosition());
                    }

                }
            });
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.style_card_view_new, parent, false);

//        Bundle bundle = ((EventosActivity) context).getIntent().getExtras();
//        String idStr = null;
//        if(bundle != null) {
//            idStr = bundle.getString("IDEVENTO");
//        }
//        Integer id = null;
//        if( idStr != null) {
//            id = Integer.valueOf(bundle.getString("IDEVENTO"));
//        }
//        if(id != null) {
////            EventosActivity e = new EventosActivity();
////            e.itemCompartilhado(id);
//            clickListener.itemClicked(null, id);
//
//        }

        NossoViewHolder holder = new NossoViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final NossoViewHolder holder = (NossoViewHolder) viewHolder;

        final Evento evento  = eventos.get(position) ;

        holder.txtViewNome.setText(evento.getNome());
        holder.txtViewHoraInicio.setText("- ÁS " + evento.getHoraInicio());
        holder.txtViewData.setText(evento.getData());
        holder.btnLike.setTag(position);
        holder.btnLike.setChecked(eventos.get(position).isChecked());

//        onClickBotaoLike(holder);


        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = true;
                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nomeEvento = eventos.get(position).getNome();
                    if(checker)

                        if (dataSnapshot.child(userID).hasChild(nomeEvento)) {

                            evento.setCurtiu(false);
                            likesRef.child(userID).child(nomeEvento).removeValue();
                            checker = false;

                        } else {
                            evento.setCurtiu(true);
                            likesRef.child(userID).child(nomeEvento).setValue(evento);
                            checker = false;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        likesCheckRef = FirebaseDatabase.getInstance().getReference().child("Likes").child(userID);

        likesCheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(evento.getNome())) {
                    holder.btnLike.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                compartilharClick(evento.getNome());

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String sharedSubject = ("Venha para o evento " + evento.getNome());
                intent.putExtra(Intent.EXTRA_SUBJECT, sharedSubject);
                intent.putExtra(Intent.EXTRA_TEXT, "Venha participar do evento " + evento.getNome().toUpperCase() + " no dia " + evento.getData() + ". "
                        + "Clique no link a seguir para ser redirecionado:" + buildDynamicLink(evento.getNome(), evento.getDescricao()));
                context.startActivity(Intent.createChooser(intent, "share_using"));
                }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        StorageReference teste = storageReference.child("Users/").child(userID).child("imagem");
        String url = "https://image.freepik.com/icones-gratis/silhueta-usuario-masculino_318-35708.jpg";


        if(evento.getUrlFotoUsuarioCard() != null) {
            GlideApp.with(context).load(evento.getUrlFotoUsuarioCard()).centerCrop().into(holder.imagemPerfilCard);
        } else {
            GlideApp.with(context).load(url).centerCrop().into(holder.imagemPerfilCard);
        }


//        GlideApp.with(context).load("https://images.unsplash.com/photo-1548674466-546e78763edf?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=967&q=80").into(holder.fundoImagemCardView);
//        GlideApp.with(context).load(R.drawable.card1).into(holder.fundoImagemCardView);

        if(evento.getCategoria().equals("Escola de Ciências da Sáude")) {
            GlideApp.with(context).load(R.drawable.roxo).into(holder.fundoImagemCardView);
        }else if(evento.getCategoria().equals("Escola de Educação, Ciência, Letras, Artes e Humanidades")) {
            GlideApp.with(context).load(R.drawable.verde).into(holder.fundoImagemCardView);
        }else if(evento.getCategoria().equals("Escola de Ciência e Tecnologia")) {
            GlideApp.with(context).load(R.drawable.laranja).into(holder.fundoImagemCardView);
        }else if(evento.getCategoria().equals("Escola de Ciências Sociais e Aplicadas")) {
            GlideApp.with(context).load(R.drawable.azul).into(holder.fundoImagemCardView);
        }


        Bundle bundle = new Bundle();

        String idEventoCompartilhado = bundle.getString("IDEVENTO");
        System.out.println(idEventoCompartilhado);

    }

    private String buildDynamicLink(String nome, String descricao) {
        String path = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setDynamicLinkDomain("graduuaplicacao.page.link")
                .setLink(Uri.parse("https://graduuaplicacao.page.link/" + nome))
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder().setTitle(nome).setDescription(descricao).build())
                .setGoogleAnalyticsParameters(new DynamicLink.GoogleAnalyticsParameters.Builder().setSource("Android App").build())
                .buildDynamicLink().getUri().toString();

        return path;
    }


    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    private void compartilharClick(String eventoID) {
        ConfiguracaoFirebase.getSharedRef(eventoID).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    long num = mutableData.getValue(Long.class);
                    mutableData.setValue(num + 1);
                    return  Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }
}
