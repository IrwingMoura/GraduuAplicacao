package graduuaplicacao.graduuaplicacao.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import graduuaplicacao.graduuaplicacao.DAO.ConfiguracaoFirebase;
import graduuaplicacao.graduuaplicacao.GlideModule.GlideApp;
import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;

public class EventoAbertoActivity extends AppCompatActivity {

    TextView nome, criadorDoEvento, categoria, data, descricao, frequencia, horaInicio, horaFim, local, txtResult, txtEventoFinalizado, palestrantes, horaTxt;
    ImageView imagem, imgData, imgPalestrante, imgSobre, imgLocal, imgSetor, eventoFinalizadoImg, imgHora;
    FirebaseAnalytics analytics;
    Button btnGerarQrCode, btnLerQrCode;
    DatabaseReference qrCodeAlunosRef, usuarioRef, horasComplementaresRef, getNomeCriadorEvento;
    Usuario usuario = new Usuario();
    Date date = new Date();
    String horaFimKey = null;
    String nomeKey = null;
    String horasComplementaresKey = null;
    Integer valorAtual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_aberto5);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();

        nome = (TextView) findViewById(R.id.nomeEventoAberto);
//        criadorDoEvento = (TextView) findViewById(R.id.criadorDoEvento);
        categoria = (TextView) findViewById(R.id.categoriaEventoAberto);
        data = (TextView) findViewById(R.id.dataEventoAberto);
        descricao = (TextView) findViewById(R.id.descricaoEventoAberto);
//        frequencia = (TextView) findViewById(R.id.frequenciaEventoAberto);
//        horaInicio = (TextView) findViewById(R.id.horaInicioEventoAberto);
       // horaFim = (TextView) findViewById(R.id.horaFimEventoAberto);
        local = (TextView) findViewById(R.id.localEventoAberto);
//        txtResult = (TextView) findViewById(R.id.txtResult);
        imagem = (ImageView) findViewById(R.id.imagemEventoAberto);
        btnGerarQrCode = (Button) findViewById(R.id.btnGerarQrCode);
        btnLerQrCode = (Button) findViewById(R.id.btnLerQrCode);
        /*qrCode = (ImageView) findViewById(R.id.imagemQrCode);*/
        txtEventoFinalizado = (TextView) findViewById(R.id.eventoFinalizado);
        palestrantes = (TextView) findViewById(R.id.palestrantes);

        imgData = (ImageView) findViewById(R.id.imagemData);
        imgLocal = (ImageView) findViewById(R.id.imagemLocal);
        imgPalestrante = (ImageView) findViewById(R.id.imagemPalestrantes);
        imgSetor = (ImageView) findViewById(R.id.imagemSetores);
        imgSobre = (ImageView) findViewById(R.id.imagemSobre);
        imgHora = (ImageView) findViewById(R.id.imagemHora);
        eventoFinalizadoImg = (ImageView) findViewById(R.id.eventoFinalizadoImg);
        horaTxt = (TextView) findViewById(R.id.horaEventoAberto);


        imgData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventoAbertoActivity.this, "Data do evento", Toast.LENGTH_SHORT).show();
            }
        });
        imgLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventoAbertoActivity.this, "Local do evento", Toast.LENGTH_SHORT).show();
            }
        });
        imgPalestrante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventoAbertoActivity.this, "Palestrantes do evento", Toast.LENGTH_SHORT).show();
            }
        });
        imgSetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventoAbertoActivity.this, "Área do evento", Toast.LENGTH_SHORT).show();
            }
        });
        imgSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventoAbertoActivity.this, "Sobre o evento", Toast.LENGTH_SHORT).show();
            }
        });
        imgHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventoAbertoActivity.this, "Hora do evento", Toast.LENGTH_SHORT).show();
            }
        });

        btnLerQrCode.setVisibility(View.GONE);

        horasComplementaresRef = ConfiguracaoFirebase.getFirebase().child("horasComplementares");
        horasComplementaresRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.child(userID).getValue();
                if(dataSnapshot.hasChild(userID)) {
                    valorAtual = Integer.valueOf(dataSnapshot.child(userID).getValue().toString());
                }else{
                    valorAtual = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            nomeKey = bundle.getString("NOME");
            String apresentadorKey = bundle.getString("APRESENTADOR");
            String categoriaKey = bundle.getString("CATEGORIA");
            String dataKey = bundle.getString("DATA");
            String descricaoKey = bundle.getString("DESCRICAO");
//            String frequenciaKey = bundle.getString("FREQUENCIA");
            String horaInicioKey = bundle.getString("HORAINICIO");
            horaFimKey = bundle.getString("HORAFIM");
            String localKey = bundle.getString("LOCAL");
            String idUsuarioLogado = bundle.getString("IDUSUARIOLOGADO");
            horasComplementaresKey = bundle.getString("HORASCOMPLEMENTARES");

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

//            getNomeCriadorEvento = ConfiguracaoFirebase.getFirebase().child("Usuarios").child(idUsuarioLogado);
//            getNomeCriadorEvento.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    criadorDoEvento.setText(dataSnapshot.child("nome").getValue().toString());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });


            nome.setText(nomeKey);
            palestrantes.setText(apresentadorKey);
            categoria.setText(categoriaKey);
            data.setText(dataKey);
            horaTxt.setText(horaInicioKey + " - " + horaFimKey);
            descricao.setText(descricaoKey);
            local.setText(localKey);
//            horaInicio.setText(horaInicioKey);



            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String dataAtual = df.format(date);

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

            Date dataEvento = new Date();
            try {
                dataEvento = formato.parse(dataKey);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String dataEventoFormatada = df.format(dataEvento);

//            btnLerQrCode.setVisibility(View.GONE);
            btnGerarQrCode.setVisibility(View.GONE);
            txtEventoFinalizado.setVisibility(View.GONE);
            eventoFinalizadoImg.setVisibility(View.GONE);
            // TODO: ACERTAR HORA

            if(userID.equals(idUsuarioLogado) && dataAtual.equals(dataEventoFormatada)){
                btnLerQrCode.setVisibility(View.VISIBLE);
                btnGerarQrCode.setVisibility(View.VISIBLE);
            }
            else if(dataAtual.equals(dataEventoFormatada)) {
                btnGerarQrCode.setVisibility(View.VISIBLE);
            }
            else if(dataEvento.after(date)){
//                txtEventoFinalizado.setText("");
//                txtEventoFinalizado.setVisibility(View.VISIBLE);
                eventoFinalizadoImg.setVisibility(View.GONE);
            }
            else{
                txtEventoFinalizado.setVisibility(View.VISIBLE);
                eventoFinalizadoImg.setVisibility(View.VISIBLE);
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

            setImagensFundoCard(categoriaKey, window);
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

    private void setImagensFundoCard(String categoriaKey, Window window) {
        if(categoriaKey.equals("Escola de Ciências da Sáude")) {

            GlideApp.with(this).load(R.drawable.roxo).centerCrop().into(imagem);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.roxoStatus));

        } else if (categoriaKey.equals("Escola de Educação, Ciência, Letras, Artes e Humanidades")) {

            GlideApp.with(this).load(R.drawable.verde).centerCrop().into(imagem);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.verdeStatus));

        } else if (categoriaKey.equals("Escola de Ciência e Tecnologia")) {

            GlideApp.with(this).load(R.drawable.laranja).centerCrop().into(imagem);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.laranjaStatus));

        } else if (categoriaKey.equals("Escola de Ciências Sociais e Aplicadas")) {

            GlideApp.with(this).load(R.drawable.azul).centerCrop().into(imagem);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.azulStatus));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                final String id = result.getContents().substring(0,28);
                qrCodeAlunosRef = ConfiguracaoFirebase.getFirebase().child("eventosCriados").child(result.getContents().substring(29));
                qrCodeAlunosRef.child("AlunosPresentes").child(id).setValue(true);
                horasComplementaresRef.child(id).setValue(Integer.valueOf(horasComplementaresKey) + valorAtual);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getHorasComplementares(){

        Date horaAtual = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        Long hora = Long.valueOf(sdf.format(horaAtual));
        Long horaFim = Long.valueOf(horaFimKey.substring(0,2));


        Date minAtual = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("mm");
        Long min = Long.valueOf(sdf2.format(minAtual));
        Long minFim = Long.valueOf(horaFimKey.substring(3,5));


        Long x = 0L;
        Long resultHora = horaFim - hora;
        if(resultHora < 0){
            x = 1L;
            resultHora = resultHora - x;
        }


        Long y = 0L;
        Long resultMin = minFim - min;
        if(resultMin < 0){
            y = 60L;
            resultMin = resultMin + y;
            resultHora = resultHora - 1;
        }

        String horaString = resultHora.toString();
        String minString = resultMin.toString();

        String resultado = horaString + "h:" + minString + "m";
        return  resultado;

    }
}
