package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import graduuaplicacao.graduuaplicacao.R;

public class EventoAbertoActivity extends AppCompatActivity {

    TextView nome, apresentador, categoria, data, descricao, frequencia, horaInicio, horaFim, local, txtResult;
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
        frequencia = (TextView) findViewById(R.id.frequenciaEventoAberto);
        horaInicio = (TextView) findViewById(R.id.horaInicioEventoAberto);
        horaFim = (TextView) findViewById(R.id.horaFimEventoAberto);
        local = (TextView) findViewById(R.id.localEventoAberto);
        txtResult = (TextView) findViewById(R.id.txtResult);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

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
            local.setText("Local: " + localKey);


//            String linkDominio = "https://graduu.page.link/?";
//
//            Uri data = this.getIntent().getData();
//            if (data != null && data.isHierarchical()) {
//                String uri = this.getIntent().getDataString();
//                Log.i("MyApp", "Deep link clicked " + uri);
//            }

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
