package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextemail;
    EditText editTextsenha;
    EditText editTextNome;
    EditText editTextMatricula;
    EditText editTextSobrenome;
    EditText editTextCampus;
    EditText editTextDataDeNascimento;
    FirebaseAuth mAuth;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        editTextemail = (EditText)  findViewById(R.id.edtEmailCadastro);
        editTextsenha = (EditText) findViewById(R.id.edtSenhaCadastro);
        editTextNome = (EditText) findViewById(R.id.edtNomeCadastro);
        editTextMatricula = (EditText) findViewById(R.id.edtMatriculaCadastro);
        editTextCampus = (EditText) findViewById(R.id.edtCampus);
        editTextDataDeNascimento = (EditText) findViewById(R.id.edtDataDeNascimento);
        editTextSobrenome = (EditText) findViewById(R.id.edtSobrenomeCadastro);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.edtJaPossuiLogin).setOnClickListener(this);
        findViewById(R.id.botaoCadastar).setOnClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() !=  null) {

        }
    }

    private void registerUser() {

        final String email = editTextemail.getText().toString().trim();
        final String senha = editTextsenha.getText().toString().trim();
        final String nome = editTextNome.getText().toString().trim();
        final String sobrenome = editTextSobrenome.getText().toString().trim();
        final String matricula = editTextMatricula.getText().toString().trim();
        final String campus = editTextCampus.getText().toString().trim();
        final String dataDeNascimento = editTextDataDeNascimento.getText().toString().trim();


        if(email.isEmpty()) {
            editTextemail.setError("Email necessario");
            editTextemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Digite um email válido");
            editTextemail.requestFocus();
            return;

        }

        if(senha.length()<6) {
            editTextsenha.setError("Sua senha deve possuir pelo menos 6 caracteres");
            editTextsenha.requestFocus();
            return;
        }

        if(senha.isEmpty()){
            editTextsenha.setError("Senha necessaria");
            editTextsenha.requestFocus();
            return;
        }

        if(nome.isEmpty()){
            editTextNome.setError("Nome necessario");
            editTextNome.requestFocus();
            return;
        }

        if(matricula.isEmpty()){
            editTextMatricula.setError("Matricula necessaria");
            editTextMatricula.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,senha).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){

                    Usuario usuario = new Usuario(
                            email,
                            senha,
                            nome,
                            sobrenome,
                            matricula,
                            campus,
                            dataDeNascimento
                    );

                    FirebaseDatabase.getInstance().getReference("Usuarios")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()) {
                                Toast.makeText(CadastroActivity.this,"Cadastro criado com sucesso", Toast.LENGTH_LONG).show();
                            }else
                                Toast.makeText(CadastroActivity.this,"Ocorreu um erro ao criar seu cadastro !", Toast.LENGTH_LONG).show();
                        }
                    });


                    Intent intent = new Intent(CadastroActivity.this, EventosActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }else {

                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Este email ja está em uso !", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botaoCadastar:
                registerUser();
                break;

            case R.id.edtJaPossuiLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

}
