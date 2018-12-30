package graduuaplicacao.graduuaplicacao.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CadastroActivity";


    ImageView imgFotoPerfil;
    EditText editTextemail;
    EditText editTextsenha;
    EditText editTextNome;
    EditText editTextMatricula;
    EditText editTextSobrenome;
    EditText editTextCampus;
    EditText editTextDataDeNascimento;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    Button botaoCadastrar;

    private final  int PICK_IMAGE_REQUEST = 71;
    private final static int mLength = 512;

    private Uri filePath;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    private StorageReference mStorageReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        //FIREBASE INIT
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        imgFotoPerfil = (ImageView) findViewById(R.id.imagemPerfilTelaCadastro);
        editTextemail = (EditText)  findViewById(R.id.edtEmailCadastro);
        editTextsenha = (EditText) findViewById(R.id.edtSenhaCadastro);
        editTextNome = (EditText) findViewById(R.id.edtNomeCadastro);
        editTextMatricula = (EditText) findViewById(R.id.edtMatriculaCadastro);
        editTextCampus = (EditText) findViewById(R.id.edtCampus);
        editTextDataDeNascimento = (EditText) findViewById(R.id.edtDataDeNascimento);
        editTextSobrenome = (EditText) findViewById(R.id.edtSobrenomeCadastro);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        botaoCadastrar = (Button) findViewById(R.id.botaoCadastar);



        mAuth = FirebaseAuth.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();


        findViewById(R.id.edtJaPossuiLogin).setOnClickListener(this);
        findViewById(R.id.botaoCadastar).setOnClickListener(this);

        imgFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherImagem();
            }
        });

//        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadImagem();
//            }
//        });



    }

    private void uploadImagem() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(CadastroActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CadastroActivity.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded"+(int)progress+"%");
                        }
                    });


            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                }
            });
            Log.d(TAG, String.valueOf(ref));
        }
    }

    private void escolherImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imgFotoPerfil.setImageBitmap(bitmap);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
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
                            dataDeNascimento,
                            ""  //TODO: SETAR IMAGEM QUE SERÁ PEGA DO FIREBASE STORAGE (VER NO VIDEO DO HINDU AOS 7:57min) // VER ALGUM MODO DE PEGAR A URL DA IMAGEM NO FIREBASE STORAGE
                    );

                    FirebaseDatabase.getInstance().getReference("Usuarios")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()) {
                                Toast.makeText(CadastroActivity.this,"Cadastro criado com sucesso", Toast.LENGTH_LONG).show();
                                uploadImagem();

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
