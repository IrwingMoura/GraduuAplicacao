package graduuaplicacao.graduuaplicacao.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import graduuaplicacao.graduuaplicacao.Model.CardsModel;
import graduuaplicacao.graduuaplicacao.R;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ExampleViewHolder> {

    private ArrayList<CardsModel> mListaUsuarios;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setItemOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImagemEvento;
        public TextView mTituloEvento, mDescricaoEvento;
        public ImageView mLixeira;

        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImagemEvento = itemView.findViewById(R.id.imagemEvento);
            mTituloEvento = itemView.findViewById(R.id.tituloEvento);
            mDescricaoEvento = itemView.findViewById(R.id.descricaoEvento);
            mLixeira = itemView.findViewById(R.id.image_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            mLixeira.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

    }

    public CardViewAdapter(ArrayList<CardsModel> usuarios) {
        mListaUsuarios = usuarios;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_preencher_card_view, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int i) {

        CardsModel usuarioAtual = mListaUsuarios.get(i);

        holder.mImagemEvento.setImageResource(usuarioAtual.getmImageSource());
        holder.mTituloEvento.setText(usuarioAtual.getmText1());
        holder.mDescricaoEvento.setText(usuarioAtual.getmText2());


    }

    @Override
    public int getItemCount() {
        return mListaUsuarios.size();
    }
}
