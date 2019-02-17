package graduuaplicacao.graduuaplicacao.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import graduuaplicacao.graduuaplicacao.Adapters.HorizontalCardsAdapter;
import graduuaplicacao.graduuaplicacao.Adapters.NossoAdapter;
import graduuaplicacao.graduuaplicacao.DAO.ConfiguracaoFirebase;
import graduuaplicacao.graduuaplicacao.Model.Evento;
import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;
import graduuaplicacao.graduuaplicacao.Util.SpeedyLinearLayoutManager;

public class EventosActivity extends AppCompatActivity implements NossoAdapter.ClickListener {

    String TAG = "EventosActivity";

    private ListView listView;
    private ArrayAdapter<Evento> adapter;
    private HorizontalCardsAdapter adapterHorizontal;
    private ArrayList<Evento> eventos;
    private DatabaseReference firebase;
    private DatabaseReference myRef;
    private ValueEventListener valueEventListenerEventos;
    private ImageButton btnCriarEventoPaginaInicial;
    private TextView btnVerPerfil;
    private TextView nomeUsuarioLogado;
    private ImageButton btnConfiguracoes;
    private CircleImageView imagemPerfil;
    private AlertDialog alertDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ValueEventListener databaseReference;
    private String userID;

    ArrayList<String> nomeHz = new ArrayList<>();
    ArrayList<String> dataHz = new ArrayList<>();
    ArrayList<String> horaHz = new ArrayList<>();

    //RECYCLERVIEW



    private Uri filePath;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private final  int PICK_IMAGE_REQUEST = 71;
    private FirebaseAnalytics analytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
//                    toastMessage("Successfully signed in with: " +user.getEmail());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        eventos = new ArrayList<>();

        swipeRefreshLayout =(SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        swipeRefreshLayout.setColorSchemeColors(Color.rgb(39,190,170));


        final NossoAdapter nossoAdapter = initAdapterCardVertical();

        initAdapterCardHorizontal();



//        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent());

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                if (pendingDynamicLinkData != null) {
                    analytics = FirebaseAnalytics.getInstance(EventosActivity.this);

                    Uri deepLink = pendingDynamicLinkData.getLink();
                    System.out.println(deepLink);

                    FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(pendingDynamicLinkData);
                    if (invite != null) {
                        String invitationId = invite.getInvitationId();
                        if (!TextUtils.isEmpty(invitationId))
                            System.out.println(invitationId);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failure");
            }
        });


        firebase = ConfiguracaoFirebase.getFirebase().child("eventosCriados");
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.child("Usuarios").hasChild("eventosCriados");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        valueEventListenerEventos = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventos.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Evento eventosNovos = dados.getValue(Evento.class);

                    eventos.add(eventosNovos);
                }

                nossoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        imagemPerfil = (CircleImageView) findViewById(R.id.imagemPerfil);
        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherImagem();
            }
        });


        btnCriarEventoPaginaInicial = (ImageButton) findViewById(R.id.btnCriarEventoPaginaInicial);
        btnCriarEventoPaginaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaDeFormularioDeCriacao();
            }
        });


        btnVerPerfil = (TextView) findViewById(R.id.btnVerPerfil);
        btnVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaDePerfil();
            }
        });

        btnConfiguracoes = (ImageButton) findViewById(R.id.btnConfiguracoes);
        btnConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(EventosActivity.this, btnConfiguracoes);
                popupMenu.getMenuInflater().inflate(R.menu.menu_configuracoes, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logout:
                                confirmacaoDeLogout();

                            case R.id.configuracaoes:
                                return false;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        nomeUsuarioLogado = (TextView) findViewById(R.id.nomeUsuarioLogado);

    }

    private void initAdapterCardHorizontal() {
        myRef.child("Likes").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nomeHz.clear();
                dataHz.clear();
                horaHz.clear();
                showData(dataSnapshot);
                adapterHorizontal.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView rcView = findViewById(R.id.listCardHorizontal);
        rcView.setLayoutManager(layoutManager);
        adapterHorizontal = new HorizontalCardsAdapter(this, nomeHz, dataHz, horaHz);
        rcView.setAdapter(adapterHorizontal);
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds: dataSnapshot.getChildren()) {
            Evento evento = new Evento();
            evento.setNome(ds.getValue(Evento.class).getNome());
            evento.setData(ds.getValue(Evento.class).getData());
            evento.setHoraInicio(ds.getValue(Evento.class).getHoraInicio());

            nomeHz.add(evento.getNome());
            dataHz.add(evento.getData());
            horaHz.add(evento.getHoraInicio());

        }
    }

    @NonNull
    private NossoAdapter initAdapterCardVertical() {
        final NossoAdapter nossoAdapter = new NossoAdapter(eventos,this);

        nossoAdapter.setClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listViewEventos);
        recyclerView.setAdapter(nossoAdapter);


        RecyclerView.LayoutManager layout = new SpeedyLinearLayoutManager(this,
                SpeedyLinearLayoutManager.VERTICAL, false);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layout);
        return nossoAdapter;
    }

    private void confirmacaoDeLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EventosActivity.this);
        builder.setTitle("Logout");

        builder.setMessage("Você realmente deseja deslogar? ");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(EventosActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //criar o alert dialog
        alertDialog = builder.create();

        //exibe o alert dialog
        alertDialog.show();
    }


    private void abrirTelaDeFormularioDeCriacao() {
        Intent intent = new Intent(EventosActivity.this, RegistroDeEventoActivity.class);
        startActivity(intent);
    }

    private void abrirTelaDePerfil() {
        Intent intent = new Intent(EventosActivity.this, PerfilActivity.class);
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
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().equals(userID)) {
                        Usuario usuario = data.getValue(Usuario.class);
                        nomeUsuarioLogado.setText(usuario.getNome());
                        String matricula = usuario.getMatricula().toString();
                        if (matricula.equals("202020")) {            //DESABILITANDO O BOTAO  --- CONSIDERANDO A MATRICULA COMO NAO PERMITIDA
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

        StorageReference imagemRef = storageReference.child("Users").child(userID).child("imagem");
        imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println(uri);
                Glide.with(EventosActivity.this).load(uri).into(imagemPerfil);
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerEventos);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void uploadImagem() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("Users/").child(userID).child("imagem");  // PASSAR ESCOLHA DE IMAGEM PARA A TELA DE PERFIL, DEVIDO AO CURRENT LOGIN, SUBSTITUIR randomUUID PARA getCurrentUser
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(EventosActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EventosActivity.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+(int)progress+"%");
                        }
                    });


            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                }
            });
            Log.d(TAG, String.valueOf(ref));
        }
    }

    private void escolherImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imagemPerfil.setImageBitmap(bitmap);
                uploadImagem();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void itemClicked(View view, int position) {
        String nome = eventos.get(position).getNome();
        String apresentador = eventos.get(position).getApresentador();
        String categoria = eventos.get(position).getCategoria();
        String data = eventos.get(position).getData();
        String descricao = eventos.get(position).getDescricao();
        String frequencia = eventos.get(position).getFrequencia();
        String horaInicio = eventos.get(position).getHoraInicio();
        String horaFim= eventos.get(position).getHoraFim();
        String local = eventos.get(position).getLocal();
        String deepLink = eventos.get(position).getDeepLink();

        Bundle bundle = new Bundle();
        bundle.putString("NOME", nome);
        bundle.putString("APRESENTADOR", apresentador);
        bundle.putString("CATEGORIA", categoria);
        bundle.putString("DATA", data);
        bundle.putString("DESCRICAO", descricao);
        bundle.putString("FREQUENCIA", frequencia);
        bundle.putString("HORAINICIO", horaInicio);
        bundle.putString("HORAFIM", horaFim);
        bundle.putString("LOCAL", local);
        bundle.putString("DEEPLINK", deepLink);

        Intent intent = new Intent(EventosActivity.this, EventoAbertoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
