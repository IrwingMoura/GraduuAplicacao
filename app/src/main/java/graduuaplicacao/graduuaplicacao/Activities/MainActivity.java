package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import graduuaplicacao.graduuaplicacao.R;

public class MainActivity extends AppCompatActivity {

    private ImageView mLogoFaculdade;
    private FirebaseAuth firebaseAuth;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogoFaculdade = (ImageView) findViewById(R.id.logoFaculdade);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

//        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user == null) {
                    Intent intentAbrirTelaLogin = new Intent(MainActivity.this, LoginActivity.class);
                    intentAbrirTelaLogin.putExtra("IDEVENTO", id);
                    startActivity(intentAbrirTelaLogin);
                    finish();
                }else {
                    Intent intentAbrirTelaLogin = new Intent(MainActivity.this, EventosActivity.class);
                    intentAbrirTelaLogin.putExtra("IDEVENTO", id);
                    startActivity(intentAbrirTelaLogin);
                    finish();
                }
            }
        }, 4000);
    }
}
