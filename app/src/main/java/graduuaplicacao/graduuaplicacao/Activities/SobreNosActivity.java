package graduuaplicacao.graduuaplicacao.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import graduuaplicacao.graduuaplicacao.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class SobreNosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element element = new Element();

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.graduulogo)
                .setDescription("Este aplicativo tem como objetivo, manter os alunos informados sobre os eventos que estão ocorrendo em sua universidade, " +
                        "facilitando também o gestão destes." +
                        " Registrando as horas complementares dos alunos que confirmam presença, através do uso do QR Code, no local.")
                .addItem(new Element().setTitle("Versão 1.0"))
                .addItem(element)
                .addGroup("Contatos")
                .addEmail("irwing.moraes@unigranrio.br")
                .addGitHub("IrwingMoura")
                .addItem(createCopyrigth())
                .create();

        setContentView(aboutPage);
    }

    private Element createCopyrigth() {
        Element copyright = new Element();

        final String copyrightstring = String.format("Copyright %d by Irwing M.", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightstring);
        copyright.setIconDrawable(R.mipmap.ic_logo2);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SobreNosActivity.this, copyrightstring, Toast.LENGTH_SHORT).show();
            }
        });

        return copyright;
    }


}
