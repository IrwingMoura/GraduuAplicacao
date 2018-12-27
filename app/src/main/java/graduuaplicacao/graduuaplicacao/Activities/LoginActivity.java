package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import graduuaplicacao.graduuaplicacao.DAO.ConfiguracaoFirebase;
import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;


public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private TextView txtCriarCadastro;
    private Button btnLogin;
    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        txtCriarCadastro = (TextView) findViewById(R.id.txtCriarCadastro);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")){

                    usuario = new Usuario();
                    usuario.setEmail(edtEmail.getText().toString());
                    usuario.setSenha(edtSenha.getText().toString());

                    validarLogin();

                }else {
                        Toast.makeText(LoginActivity.this, "Preencha os campos de e-mail e senha !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCriarCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });
    }


    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login efetuado com sucesso !", Toast.LENGTH_SHORT).show();
                    abrirTelaPrincipal();
                }else {
                    Toast.makeText(LoginActivity.this, "Usuario e/ou senha inválidos !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal() {
        Intent intentAbrirTelaPrincipal = new Intent(LoginActivity.this, EventosActivity.class);
        startActivity(intentAbrirTelaPrincipal);
        finish();
    }

}
