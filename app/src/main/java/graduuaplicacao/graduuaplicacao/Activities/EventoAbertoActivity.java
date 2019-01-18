package graduuaplicacao.graduuaplicacao.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import graduuaplicacao.graduuaplicacao.R;

public class EventoAbertoActivity extends AppCompatActivity {

    TextView nome, apresentador, categoria, data, descricao, frequencia, horaInicio, horaFim, local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_aberto);

        nome = (TextView) findViewById(R.id.nomeEventoAberto);
        apresentador = (TextView) findViewById(R.id.apresentadorEventoAberto);
        categoria = (TextView) findViewById(R.id.categoriaEventoAberto);
        data = (TextView) findViewById(R.id.dataEventoAberto);
        descricao = (TextView) findViewById(R.id.descricaoEventoAberto);
        frequencia = (TextView) findViewById(R.id.frequenciaEventoAberto);
        horaInicio = (TextView) findViewById(R.id.horaInicioEventoAberto);
        horaFim = (TextView) findViewById(R.id.horaFimEventoAberto);
        local = (TextView) findViewById(R.id.localEventoAberto);


        Bundle bundle = getIntent().getExtras();

        String nomeKey = bundle.getString("NOME");
        String apresentadorKey = bundle.getString("APRESENTADOR");
        String categoriaKey = bundle.getString("CATEGORIA");
        String dataKey = bundle.getString("DATA");
        String descricaoKey = bundle.getString("DESCRICAO");
        String frequenciaKey = bundle.getString("FREQUENCIA");
        String horaInicioKey = bundle.getString("HORAINICIO");
        String horaFimKey = bundle.getString("HORAFIM");
        String localKey = bundle.getString("LOCAL");


        nome.setText("Nome: " + nomeKey);
        apresentador.setText("Apresentador: " + apresentadorKey);
        categoria.setText("Categoria: " + categoriaKey);
        data.setText("Data: " + dataKey);
        descricao.setText("Descrição: " + descricaoKey);
        frequencia.setText("Frequênicia: " + frequenciaKey);
        horaInicio.setText("Hora de Início: " + horaInicioKey);
        horaFim.setText("Hora de Término: " + horaFimKey);
        local.setText("Local:        " + localKey);

    }


}
