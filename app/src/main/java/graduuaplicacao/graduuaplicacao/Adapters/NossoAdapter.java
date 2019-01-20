package graduuaplicacao.graduuaplicacao.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import graduuaplicacao.graduuaplicacao.Activities.EventoAbertoActivity;
import graduuaplicacao.graduuaplicacao.GlideModule.GlideApp;
import graduuaplicacao.graduuaplicacao.GlideModule.MyGlideApp;
import graduuaplicacao.graduuaplicacao.Model.Evento;
import graduuaplicacao.graduuaplicacao.R;

public class NossoAdapter extends RecyclerView.Adapter{

    private List<Evento> eventos;
    private Context context;
    private ClickListener clickListener;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

        Evento evento  = eventos.get(position) ;

        holder.txtViewNome.setText(evento.getNome());
        holder.txtViewHoraInicio.setText("- ÁS " + evento.getHoraInicio());
        holder.txtViewData.setText(evento.getData());
        holder.btnLike.setTag(position);
        holder.btnLike.setChecked(eventos.get(position).isChecked());

        holder.btnLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                holder.btnLike.setChecked(buttonView.isChecked() ? true : false);
                eventos.get(getPosition).setChecked(buttonView.isChecked() ? true : false);
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String sharedSubject = ("Venha para o evento " + eventos.get(position).getNome());
                intent.putExtra(Intent.EXTRA_SUBJECT, sharedSubject);
                intent.putExtra(Intent.EXTRA_TEXT, "Venha participar do evento " + eventos.get(position).getNome().toUpperCase() + " no dia " + eventos.get(position).getData() + ". "
                        + "Clique no link a seguir para ser redirecionado: GERAR LINK DINAMICO DO EVENTO");
                context.startActivity(Intent.createChooser(intent, "share_using"));
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        StorageReference teste = storageReference.child("Users/").child(userID).child("imagem");
        String url = "https://image.freepik.com/icones-gratis/silhueta-usuario-masculino_318-35708.jpg";

        GlideApp.with(context).load(url).centerCrop().into(holder.imagemPerfilCard);

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
}
