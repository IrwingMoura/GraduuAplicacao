package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;
import graduuaplicacao.graduuaplicacao.Util.Formatador;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CadastroActivity";


    EditText editTextemail;
    EditText editTextsenha;
    EditText editTextNomeCompleto;
    EditText editTextMatricula;
//    EditText editTextSobrenome;
    Spinner editTextCampus;
    EditText editTextDataDeNascimento;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    Button botaoCadastrar;

    String campusSpinner;

    String nomeReg, campusReg, cursoReg, dataNascReg;
    Boolean professorReg;

    private final  int PICK_IMAGE_REQUEST = 71;
    private final static int mLength = 512;

    private Uri filePath;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    private StorageReference mStorageReference ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        //FIREBASE INIT
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        editTextemail = (EditText)  findViewById(R.id.edtEmailCadastro);
        editTextsenha = (EditText) findViewById(R.id.edtSenhaCadastro);
//        editTextNomeCompleto = (EditText) findViewById(R.id.edtNomeCompleto);
        editTextMatricula = (EditText) findViewById(R.id.edtMatriculaCadastro);
//        editTextCampus = (Spinner) findViewById(R.id.edtCampus);
//        editTextDataDeNascimento = (EditText) findViewById(R.id.edtDataDeNascimento);
//        editTextSobrenome = (EditText) findViewById(R.id.edtSobrenomeCadastro);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        botaoCadastrar = (Button) findViewById(R.id.botaoCadastrar);



//        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.campus, android.R.layout.simple_spinner_item);
//        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        editTextCampus.setAdapter(adapterSpinner);
//        editTextCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                campusSpinner = parent.getItemAtPosition(position).toString();
//
////                if(localSpinner.equals("Local")) {
////
////                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();


//        findViewById(R.id.edtJaPossuiLogin).setOnClickListener(this);
        findViewById(R.id.botaoCadastrar).setOnClickListener(this);

//        formatarInputs();
    }

    private void formatarInputs(){
        editTextDataDeNascimento.addTextChangedListener(Formatador.mask(editTextDataDeNascimento, Formatador.FORMAT_DATE));
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
//        final String nome = editTextNomeCompleto.getText().toString().trim();
        final String matricula = editTextMatricula.getText().toString().trim();
//        final String campusSpinner = editTextCampus.getSelectedItem().toString().trim();
//        final String dataDeNascimento = editTextDataDeNascimento.getText().toString().trim();
//
//        Validador validador = new Validador();
//        boolean opt = validador.validarData(dataDeNascimento);


        if (verificacoes(email, senha, matricula)) return;

        progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, senha).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {

                                Usuario usuario = new Usuario(
                                        email,
                                        senha,
                                        nomeReg,
                                        matricula,
                                        campusReg,
                                        dataNascReg,
                                        "",
                                        cursoReg,
                                        professorReg
                                );

                                FirebaseDatabase.getInstance().getReference("Usuarios")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(CadastroActivity.this, "Cadastro criado com sucesso", Toast.LENGTH_LONG).show();
                                        } else
                                            Toast.makeText(CadastroActivity.this, "Ocorreu um erro ao criar seu cadastro !", Toast.LENGTH_LONG).show();
                                    }
                                });


                                Intent intent = new Intent(CadastroActivity.this, EventosActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);


                            } else {

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getApplicationContext(), "Este email ja está em uso !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    }

    private boolean verificacoes(String email, String senha, String matricula) {
        if (email.isEmpty()) {
            editTextemail.setError("Email necessario");
            editTextemail.requestFocus();
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("Digite um email válido");
            editTextemail.requestFocus();
            return true;

        }

        if (senha.length() < 6) {
            Toast.makeText(CadastroActivity.this, "Sua senha deve possuir pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
//            editTextsenha.setError("Sua senha deve possuir pelo menos 6 caracteres");
//            editTextsenha.requestFocus();
            return true;
        }

        if (senha.isEmpty()) {
            editTextsenha.setError("Senha necessaria");
            editTextsenha.requestFocus();
            return true;
        }

//            if (nome.isEmpty()) {
//                editTextNomeCompleto.setError("Nome necessario");
//                editTextNomeCompleto.requestFocus();
//                return;
//            }

        if (matricula.isEmpty()) {
            editTextMatricula.setError("Matricula necessaria");
            editTextMatricula.requestFocus();
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botaoCadastrar:
                DatabaseReference usuariosRegistradosRef = FirebaseDatabase.getInstance().getReference("Users").child(editTextMatricula.getText().toString());
                usuariosRegistradosRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nomeReg = dataSnapshot.child("nome").getValue().toString();
                        campusReg = dataSnapshot.child("campus").getValue().toString();
                        dataNascReg = dataSnapshot.child("dataDeNascimento").getValue().toString();
                        cursoReg = dataSnapshot.child("curso").getValue().toString();
                        professorReg = (Boolean) dataSnapshot.child("professor").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                registerUser();
                break;
        }
    }

}
