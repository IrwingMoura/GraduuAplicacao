package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import graduuaplicacao.graduuaplicacao.GlideModule.GlideApp;
import graduuaplicacao.graduuaplicacao.R;

public class EventoAbertoActivity extends AppCompatActivity {

    TextView nome, apresentador, categoria, data, descricao, frequencia, horaInicio, horaFim, local, txtResult;
    ImageView imagem;
    FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_aberto);

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

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            String nomeKey = bundle.getString("NOME");
            String apresentadorKey = bundle.getString("APRESENTADOR");
            String categoriaKey = bundle.getString("CATEGORIA");
            String dataKey = bundle.getString("DATA");
            String descricaoKey = bundle.getString("DESCRICAO");
//            String frequenciaKey = bundle.getString("FREQUENCIA");
//            String horaInicioKey = bundle.getString("HORAINICIO");
//            String horaFimKey = bundle.getString("HORAFIM");
            String localKey = bundle.getString("LOCAL");


            nome.setText(nomeKey);
            apresentador.setText("por " + apresentadorKey);
            categoria.setText(categoriaKey);
            data.setText(dataKey);
            descricao.setText(descricaoKey);
            local.setText(localKey);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, EventosActivity.class);
        startActivity(intent);
    }

}
