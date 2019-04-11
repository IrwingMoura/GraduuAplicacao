package graduuaplicacao.graduuaplicacao.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import graduuaplicacao.graduuaplicacao.R;

public class EsqueciSenhaActivity extends AppCompatActivity {

    private TextInputEditText email;
    private Button enviar;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        email = (TextInputEditText) findViewById(R.id.emailEsqueciSenha);
        enviar = (Button) findViewById(R.id.enviar);

        firebaseAuth = FirebaseAuth.getInstance();

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EsqueciSenhaActivity.this, "Senha enviado para seu email!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(EsqueciSenhaActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


}
