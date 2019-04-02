package graduuaplicacao.graduuaplicacao.Activities;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(firebaseUser != null) {
                    Intent intentAbrirTelaEventos = new Intent(Home.this, EventosActivity.class);
                    startActivity(intentAbrirTelaEventos);
                }else {

                }
            }
        }, 5000);


    }
}
