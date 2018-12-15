package graduuaplicacao.graduuaplicacao.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import graduuaplicacao.graduuaplicacao.Adapters.EventosAdapter;
import graduuaplicacao.graduuaplicacao.DAO.ConfiguracaoFirebase;
import graduuaplicacao.graduuaplicacao.Model.Evento;
import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;

public class EventosActivity extends AppCompatActivity {

    String TAG = "EventosActivity";

    private ListView listView;
    private ArrayAdapter<Evento> adapter;
    private ArrayList<Evento> eventos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerEventos;
    private Button btnCriarEventoPaginaInicial;
    private Button btnVerPerfil;
    private Button btnConfiguracoes;
    private AlertDialog alertDialog;
    private Evento eventosExcluir;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
//                    toastMessage("Successfully signed in with: " +user.getEmail());
                }else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                    toastMessage("Successfully signed out");
                }
            }
        };

        eventos = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listViewEventos);
        adapter = new EventosAdapter(this, eventos);

        listView.setAdapter(adapter);

        firebase = ConfiguracaoFirebase.getFirebase().child("eventosCriados");

        valueEventListenerEventos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventos.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()) {
                    Evento eventosNovos = dados.getValue(Evento.class);

                    eventos.add(eventosNovos);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        btnCriarEventoPaginaInicial = (Button) findViewById(R.id.btnCriarEventoPaginaInicial);
        btnCriarEventoPaginaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaDeFormularioDeCriacao();
            }
        });


        btnVerPerfil = (Button) findViewById(R.id.btnVerPerfil);
        btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaDePerfil();
            }
        });

        btnConfiguracoes = (Button) findViewById(R.id.btnConfiguracoes);
        btnConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaConfiguracoes();
            }
        });


        //BOTÃO PARA EXCLUIR AO CLICAR NO CARD

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                eventosExcluir = adapter.getItem(position);
//
//                //cria o gerador do alert dialog
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(EventosActivity.this);
//
//                //definir titulo
//
//                builder.setTitle("Excluir");
//
//                //defini uma mensagem
//
//                builder.setMessage("Você realmente deseja excluir o evento " + eventosExcluir.getNome() + "  ?");
//
//                //defini botao sim
//
//                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        firebase = ConfiguracaoFirebase.getFirebase().child("eventosCriados");
//                        firebase.child(eventosExcluir.getNome()).removeValue();
//
//                        Toast.makeText(EventosActivity.this, "Exclusão efetuada!", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(EventosActivity.this, "Exclusão cancelada", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                //criar o alert dialog
//                alertDialog = builder.create();
//
//                //exibe o alert dialog
//                alertDialog.show();
//            }
//        });

    }


    private void abrirTelaDeFormularioDeCriacao() {
        Intent intent = new Intent(EventosActivity.this, RegistroDeEventoActivity.class);
        startActivity(intent);
    }

    private void abrirTelaDePerfil() {
        Intent intent = new Intent(EventosActivity.this, PerfilActivity.class);
        startActivity(intent);
    }

    private void abrirTelaConfiguracoes() {
        Intent intent = new Intent(EventosActivity.this, ConfiguracoesActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerEventos);

        final DatabaseReference teste = myRef.child("Usuarios");
        teste.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren())   {
                    if(data.getKey().equals(userID)){
                        Usuario usuario= data.getValue(Usuario.class);
                        String matricula = usuario.getMatricula().toString();
                            if(matricula.equals("202020")) {            //DESABILITANDO O BOTAO  --- CONSIDERANDO A MATRICULA COMO NAO PERMITIDA
//                                btnCriarEventoPaginaInicial.setEnabled(false);
                                btnCriarEventoPaginaInicial.setVisibility(View.GONE); // TODO: 15/12/2018 TORNAR O BOTAO VISIVEL SOMENTE PARA QUEM TEM PERMISSÃO
                            }

//                        data.getKey().equals("matricula");

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerEventos);
    }

/*
    public String getMatriculaUsuarioLogado(DataSnapshot dataSnapshot, String usuarioLogadoMatricula) {
        dataSnapshot.getChildren();
        usuarioLogadoMatricula = dataSnapshot.child(userID).getValue(Usuario.class).getMatricula();
        if(usuarioLogadoMatricula == "5404451") {
            btnCriarEventoPaginaInicial.setEnabled(false);
        }
        return usuarioLogadoMatricula;
    }*/

}
