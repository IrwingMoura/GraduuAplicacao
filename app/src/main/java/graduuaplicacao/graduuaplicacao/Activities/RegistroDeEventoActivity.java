package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import graduuaplicacao.graduuaplicacao.DAO.ConfiguracaoFirebase;
import graduuaplicacao.graduuaplicacao.Model.Evento;
import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;

public class RegistroDeEventoActivity extends AppCompatActivity {

    private EditText titulo;
    private EditText horaInicio;
    private EditText horaFim;
    private EditText data;
    private EditText descricao;
    private EditText apresentador;
    private EditText frequencia;
    private EditText local;
    private EditText categoria;
    private ImageView fotoCard;

    private Usuario usuario;
    private Evento eventos;
    private DatabaseReference firebase;

    private Button criarEvento;

    FirebaseUser firebaseUser;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_de_evento);

        titulo = (EditText) findViewById(R.id.edtTitulo);
        horaInicio = (EditText) findViewById(R.id.edtHoraInicio);
        horaFim = (EditText) findViewById(R.id.edtHoraFim);
        data = (EditText) findViewById(R.id.edtData);
        descricao = (EditText) findViewById(R.id.edtDescricao);
        apresentador = (EditText) findViewById(R.id.edtApresentador);
        frequencia = (EditText) findViewById(R.id.edtFrequencia);
        local = (EditText) findViewById(R.id.edtLocal);
        categoria = (EditText) findViewById(R.id.edtCategoria);
        fotoCard = (ImageView) findViewById(R.id.imgCardView);


        criarEvento = (Button) findViewById(R.id.btnCriarEvento);


        criarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventos = new Evento();
                usuario = new Usuario();

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                uid = firebaseUser.getUid();


//
//                eventos.setIdDoCriador(usuario.getId());
                eventos.setNome(titulo.getText().toString());
                eventos.setHoraInicio(horaInicio.getText().toString());
                eventos.setHoraFim(horaFim.getText().toString());
                eventos.setData(data.getText().toString());
                eventos.setDescricao(descricao.getText().toString());
                eventos.setApresentador(apresentador.getText().toString());
                eventos.setFrequencia(frequencia.getText().toString());
                eventos.setLocal(local.getText().toString());
                eventos.setCategoria(categoria.getText().toString());
                eventos.setIdUsuarioLogado(uid);

                salvarEventos(eventos);

                listarEventos();
            }
        });

    }


    private boolean salvarEventos(Evento eventos) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("eventosCriados");
            firebase.child(eventos.getNome()).setValue(eventos);
            Toast.makeText(RegistroDeEventoActivity.this, "Evento criado com sucesso!", Toast.LENGTH_LONG).show();

            return true ;

        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    private void listarEventos() {
        Intent intent = new Intent(RegistroDeEventoActivity.this, EventosActivity.class);
        startActivity(intent);
        finish();
    }

}
