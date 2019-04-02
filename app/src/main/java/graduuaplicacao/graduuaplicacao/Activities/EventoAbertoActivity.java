package graduuaplicacao.graduuaplicacao.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import graduuaplicacao.graduuaplicacao.DAO.ConfiguracaoFirebase;
import graduuaplicacao.graduuaplicacao.GlideModule.GlideApp;
import graduuaplicacao.graduuaplicacao.Model.Evento;
import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;

public class EventoAbertoActivity extends AppCompatActivity {

    TextView nome, apresentador, categoria, data, descricao, frequencia, horaInicio, horaFim, local, txtResult;
    ImageView imagem;
    FirebaseAnalytics analytics;
    Button btnGerarQrCode, btnLerQrCode;
    DatabaseReference qrCodeAlunosRef, usuarioRef;
    Usuario usuario = new Usuario();
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_aberto);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();

        nome = (TextView) findViewById(R.id.nomeEventoAberto);
        apresentador = (TextView) findViewById(R.id.apresentadorEventoAberto);
        categoria = (TextView) findViewById(R.id.categoriaEventoAberto);
        data = (TextView) findViewById(R.id.dataEventoAberto);
        descricao = (TextView) findViewById(R.id.descricaoEventoAberto);
//        frequencia = (TextView) findViewById(R.id.frequenciaEventoAberto);
//        horaInicio = (TextView) findViewById(R.id.horaInicioEventoAberto);
//        horaFim = (TextView) findViewById(R.id.horaFimEventoAberto);
        local = (TextView) findViewById(R.id.localEventoAberto);
//        txtResult = (TextView) findViewById(R.id.txtResult);
        imagem = (ImageView) findViewById(R.id.imagemEventoAberto);
        btnGerarQrCode = (Button) findViewById(R.id.btnGerarQrCode);
        btnLerQrCode = (Button) findViewById(R.id.btnLerQrCode);
        /*qrCode = (ImageView) findViewById(R.id.imagemQrCode);*/

        btnLerQrCode.setVisibility(View.GONE);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            String nomeKey = bundle.getString("NOME");
            String apresentadorKey = bundle.getString("APRESENTADOR");
            String categoriaKey = bundle.getString("CATEGORIA");
            String dataKey = bundle.getString("DATA");
            String descricaoKey = bundle.getString("DESCRICAO");
//            String frequenciaKey = bundle.getString("FREQUENCIA");
            String horaInicioKey = bundle.getString("HORAINICIO");
//            String horaFimKey = bundle.getString("HORAFIM");
            String localKey = bundle.getString("LOCAL");
            String idUsuarioLogado = bundle.getString("IDUSUARIOLOGADO");

            usuarioRef = ConfiguracaoFirebase.getFirebase().child("Usuarios").child(userID);
            usuarioRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    usuario.setNome(dataSnapshot.child("nome").getValue().toString());
                    usuario.setMatricula(dataSnapshot.child("matricula").getValue().toString());
                    usuario.setEmail(dataSnapshot.child("email").getValue().toString());
                    usuario.setDataDeNascimento(dataSnapshot.child("dataDeNascimento").getValue().toString());
                    usuario.setCampus(dataSnapshot.child("campus").getValue().toString());
                    usuario.setCurso(dataSnapshot.child("curso").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            nome.setText(nomeKey);
            apresentador.setText("por " + apresentadorKey);
            categoria.setText(categoriaKey);
            data.setText(dataKey + "  ÀS  " + horaInicioKey);
            descricao.setText(descricaoKey);
            local.setText(localKey);
//            horaInicio.setText(horaInicioKey);

            if(userID.equals(idUsuarioLogado)){
                btnLerQrCode.setVisibility(View.VISIBLE);
            }

            SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy");
            String datastr = df.format(date);

            if(!dataKey.equals(datastr)) {
                btnLerQrCode.setVisibility(View.GONE);
            }

//            Glide.with(EventoAbertoActivity.this).load(R.drawable.bot).into(imagem);
//            frequencia.setText(frequenciaKey);
//            horaInicio.setText(horaInicioKey);
//            horaFim.setText(horaFimKey);

//            String linkDominio = "https://graduu.page.link/?";
//
//            Uri data = this.getIntent().getData();
//            if (data != null && data.isHierarchical()) {
//                String uri = this.getIntent().getDataString();
//                Log.i("MyApp", "Deep link clicked " + uri);
//            }

            setImagensFundoCard(categoriaKey);
        }


        btnGerarQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EventoAbertoActivity.this);
                builder.setTitle("QR CODE");

                ImageView qrCode = new ImageView(EventoAbertoActivity.this);
                gerarQrCode(qrCode, userID);
                builder.setView(qrCode);

                builder.show();

            }
        });

        btnLerQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(EventoAbertoActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setPrompt("Camera Scan");
                intentIntegrator.setCameraId(0);
                intentIntegrator.initiateScan();
            }
        });


        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                if (pendingDynamicLinkData != null) {
                    analytics = FirebaseAnalytics.getInstance(EventoAbertoActivity.this);

                    Uri deepLink = pendingDynamicLinkData.getLink();
                    txtResult.append("\nonSuccess Called " + deepLink.toString());

                    FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(pendingDynamicLinkData);
                    if (invite != null) {
                        String invitationId = invite.getInvitationId();
                        if (!TextUtils.isEmpty(invitationId))
                            txtResult.append("\ninvitation Id " + invitationId);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                txtResult.append("\nonFailure");
            }
        });

    }

    private void gerarQrCode(ImageView imagem, String userID) {
        String nomeEvento = nome.getText().toString();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(userID + "/" + nomeEvento, BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imagem.setImageBitmap(bitmap);

        }catch (WriterException e){
            e.printStackTrace();
        }
    }

    private void setImagensFundoCard(String categoriaKey) {
        if(categoriaKey.equals("Escola de Ciências da Sáude")) {

            GlideApp.with(this).load("https://images.unsplash.com/photo-1532187863486-abf9dbad1b69?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80").centerCrop().into(imagem);

        } else if (categoriaKey.equals("Escola de Educação, Ciência, Letras, Artes e Humanidades")) {

            GlideApp.with(this).load("https://images.unsplash.com/photo-1472173148041-00294f0814a2?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80").centerCrop().into(imagem);
        } else if (categoriaKey.equals("Escola de Ciência e Tecnologia")) {

            GlideApp.with(this).load("https://images.unsplash.com/photo-1504164996022-09080787b6b3?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80").centerCrop().into(imagem);
        } else if (categoriaKey.equals("Escola de Ciências Sociais e Aplicadas")) {

            GlideApp.with(this).load("https://images.unsplash.com/photo-1496389361897-383a9afa9afd?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1397&q=80").centerCrop().into(imagem);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_SHORT).show();
                String id = result.getContents().substring(0,28);
                qrCodeAlunosRef = ConfiguracaoFirebase.getFirebase().child("eventosCriados").child(result.getContents().substring(29));
                qrCodeAlunosRef.child("AlunosPresentes").child(id).setValue(usuario);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, EventosActivity.class);
        startActivity(intent);
    }

    private void showData(DataSnapshot datasnapshot) {

    }

}
