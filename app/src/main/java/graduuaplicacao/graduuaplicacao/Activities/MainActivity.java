package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import graduuaplicacao.graduuaplicacao.R;

public class MainActivity extends AppCompatActivity {

    private ImageView mLogoFaculdade;
    private Button mBtnFazerLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogoFaculdade = (ImageView) findViewById(R.id.logoFaculdade);
        mBtnFazerLogin = (Button) findViewById(R.id.btnFazerLogin);

        mBtnFazerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAbrirTelaLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intentAbrirTelaLogin);
            }
        });
    }
}
