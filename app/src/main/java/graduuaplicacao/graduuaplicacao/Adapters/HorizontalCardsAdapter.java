package graduuaplicacao.graduuaplicacao.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import graduuaplicacao.graduuaplicacao.R;

public class HorizontalCardsAdapter extends RecyclerView.Adapter<HorizontalCardsAdapter.ViewHolder>  {

    private static final String TAG = "HorizontalCardsAdapter";


    private ArrayList<String> nome = new ArrayList<>();
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<String> hora = new ArrayList<>();
    private Context mContext;

    public HorizontalCardsAdapter(Context mContext, ArrayList<String> nome, ArrayList<String> data, ArrayList<String> hora) {
        this.nome= nome;
        this.data= data;
        this.hora= hora;
        this.mContext = mContext;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imagemPerfil;
        ImageView like;
        TextView titulo, data, hora;

        public ViewHolder(View itemView) {
            super(itemView);

            /*imagemPerfil = itemView.findViewById(R.id.imgPerfilCardLike);*/
            like = itemView.findViewById(R.id.likeCardLike);
            titulo = itemView.findViewById(R.id.txtTituloCardLike);
            data = itemView.findViewById(R.id.txtDataCardLike);
            hora = itemView.findViewById(R.id.txtHoraCardLike);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.style_card_horizontal, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.titulo.setText(nome.get(position));
        holder.hora.setText(hora.get(position));
        holder.data.setText(data.get(position));

    }

    @Override
    public int getItemCount() {
        return nome.size();
    }

}
