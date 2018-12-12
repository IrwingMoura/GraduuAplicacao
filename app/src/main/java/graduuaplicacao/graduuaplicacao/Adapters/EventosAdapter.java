package graduuaplicacao.graduuaplicacao.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import graduuaplicacao.graduuaplicacao.Model.Evento;
import graduuaplicacao.graduuaplicacao.R;

public class EventosAdapter extends ArrayAdapter<Evento>{


    private ArrayList<Evento> eventos;
    private Context context;


    public EventosAdapter(Context c, ArrayList<Evento> objects) {

        super(c, 0, objects);
        this.context = c;
        this.eventos = objects;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        View view = null;

        if(eventos != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lista_eventos, parent, false);

            TextView txtViewNome = (TextView) view.findViewById(R.id.txtViewNome);
            TextView txtViewDescricao = (TextView) view.findViewById(R.id.txtViewDescricao);
            TextView txtViewHoraInicio = (TextView) view.findViewById(R.id.txtViewHoraInicio);
            TextView txtViewHoraFim = (TextView) view.findViewById(R.id.txtViewHoraFim);
            TextView txtViewData = (TextView) view.findViewById(R.id.txtViewData);
            TextView txtViewApresentador = (TextView) view.findViewById(R.id.txtViewApresentador);
            TextView txtViewFrequencia = (TextView) view.findViewById(R.id.txtViewFrequencia);
            TextView txtViewLocal = (TextView) view.findViewById(R.id.txtViewLocal);
            TextView txtViewCategoria = (TextView) view.findViewById(R.id.txtViewCategoria);

            Evento evento2 = eventos.get(position);

            txtViewNome.setText(evento2.getNome());
            txtViewDescricao.setText(evento2.getDescricao());
            txtViewHoraInicio.setText(evento2.getHoraInicio().toString());
            txtViewHoraFim.setText(evento2.getHoraFim().toString());
            txtViewData.setText(evento2.getData().toString());
            txtViewApresentador.setText(evento2.getApresentador().toString());
            txtViewFrequencia.setText(evento2.getFrequencia());
            txtViewLocal.setText(evento2.getLocal());
            txtViewCategoria.setText(evento2.getCategoria());

        }

        return view;
    }
}