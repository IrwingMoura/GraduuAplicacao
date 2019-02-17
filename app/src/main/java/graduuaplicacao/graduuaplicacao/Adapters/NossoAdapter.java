package graduuaplicacao.graduuaplicacao.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteInvitation;
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
import graduuaplicacao.graduuaplicacao.Activities.EventoAbertoActivity;
import graduuaplicacao.graduuaplicacao.DAO.ConfiguracaoFirebase;
import graduuaplicacao.graduuaplicacao.GlideModule.GlideApp;
import graduuaplicacao.graduuaplicacao.Model.Evento;
import graduuaplicacao.graduuaplicacao.R;

public class NossoAdapter extends RecyclerView.Adapter{

    private final String TAG = getClass().getName();

    private List<Evento> eventos;
    private Context context;
    private ClickListener clickListener;
    private FirebaseAnalytics analytics;
    private DatabaseReference likesRef, eventosCriados;
    private boolean checker = false;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseUser user = mAuth.getCurrentUser();

    String userID = user.getUid();

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public NossoAdapter(List<Evento> eventos, Context context) {
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

        public NossoViewHolder(View view) {
            super(view);
            imagemPerfilCard = (CircleImageView) view.findViewById(R.id.imagemPerfilCard);
            txtViewNome = (TextView) view.findViewById(R.id.txtViewNome);
            txtViewHoraInicio = (TextView) view.findViewById(R.id.txtViewHoraInicio);
            txtViewData = (TextView) view.findViewById(R.id.txtViewData);
            btnLike = (CheckBox) view.findViewById(R.id.likeIcon);
            btnShare = (Button) view.findViewById(R.id.btnShare);

            likesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            eventosCriados = FirebaseDatabase.getInstance().getReference().child("eventosCriados");


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, EventoAbertoActivity.class));

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



    }

//    private void onClickBotaoLike(final NossoViewHolder holder) {
//        holder.btnLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
//                int getPosition = (Integer) buttonView.getTag();
//                holder.btnLike.setChecked(buttonView.isChecked());
//                eventos.get(getPosition).setChecked(buttonView.isChecked());
//                checker = true;
//                likesRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if(checker) {
//                            if(dataSnapshot.child("eventosCriados").hasChild(userID)) {
//
//                                likesRef.child("eventosCriados").child(userID).removeValue();
//                                checker = false;
//
//                            }
//                        }else {
//
//                            likesRef.child("eventosCriados").child(userID).setValue(true);
//                            checker = false;
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//            }
//        });
//    }

    private void onInviteClicked(int position) {
        Intent intent = new AppInviteInvitation.IntentBuilder("aaaa")
                .setDeepLink(Uri.parse(eventos.get(position).getDeepLink()))
                .build();

        context.startActivity(Intent.createChooser(intent, "share_using"));
    }

    private String buildDynamicLink(String evento, String descricao) {
        String path = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setDynamicLinkDomain("graduu.page.link")
                .setLink(Uri.parse("https://graduu.page.link/eventoAberto"))
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder().setTitle(evento).setDescription(descricao).build())
                .setGoogleAnalyticsParameters(new DynamicLink.GoogleAnalyticsParameters.Builder().setSource("Android App").build())
                .buildDynamicLink().getUri().toString();


        return  path;
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
