package graduuaplicacao.graduuaplicacao.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import graduuaplicacao.graduuaplicacao.DAO.ConfiguracaoFirebase;
import graduuaplicacao.graduuaplicacao.GlideModule.GlideApp;
import graduuaplicacao.graduuaplicacao.Model.Usuario;
import graduuaplicacao.graduuaplicacao.R;

public class PerfilActivity extends AppCompatActivity {

    String TAG = "PerfilActivity";

    private TextView mNome;
    private TextView mSobrenome;
    private TextView mMatricula;
    private TextView mDataDeNascimento;
    private TextView mCampus;
    private TextView mEmail;
    private ImageView mImagePerfil;
    private TextView mCurso;
    private TextView mHorasComp;
    private ImageView mIconeCamera;
    private TextView txtHorasComp;
    private TextView txtCampus;

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil2);

        mNome = findViewById(R.id.txtNomePerfil);
        mCurso = findViewById(R.id.curso);
//        mSobrenome = findViewById(R.id.txtSobrenomePerfil);
        mMatricula = findViewById(R.id.resultMatricula);
        mDataDeNascimento = findViewById(R.id.resultData);
        mCampus = findViewById(R.id.resultCampus);
        mEmail = findViewById(R.id.resultEmail);
        mImagePerfil = findViewById(R.id.imagemPerfilTelaPerfil);
        mHorasComp = findViewById(R.id.resulHora);
        mIconeCamera = findViewById(R.id.iconeCamera);
        txtCampus = findViewById(R.id.txtCampusPerfil);
        txtHorasComp = findViewById(R.id.txtHorasComp);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        myRef = mFirebaseDatabase.getReference().child("Usuarios");
//        myRef = ConfiguracaoFirebase.getFirebase().child("Usuarios");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());
//                    toastMessage("Successfully signed in with: " +user.getEmail());
                }else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                    toastMessage("Successfully signed out");
                }
            }
        };


        mImagePerfil = (CircleImageView) findViewById(R.id.imagemPerfilTelaPerfil);
        mIconeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherImagem();
            }
        });

        final StorageReference imagemRef = storageReference.child("Users").child(userID).child("imagem");
        imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println(uri);
                if(uri != null || !uri.equals("")) {
                    GlideApp.with(PerfilActivity.this).load(uri).centerCrop().into(mImagePerfil);
                }else{
                    GlideApp.with(PerfilActivity.this).load("https://image.freepik.com/icones-gratis/silhueta-usuario-masculino_318-35708.jpg").centerCrop().into(mImagePerfil);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                GlideApp.with(PerfilActivity.this).load("https://image.freepik.com/icones-gratis/silhueta-usuario-masculino_318-35708.jpg").centerCrop().into(mImagePerfil);
            }
        });

        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference dRHorasComp = ConfiguracaoFirebase.getFirebase().child("horasComplementares");
        dRHorasComp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userID)) {
                    mHorasComp.setText(dataSnapshot.child(userID).getValue().toString() + " de 200");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {

            Usuario usuario = new Usuario();
            usuario.setNome(dataSnapshot.child("nome").getValue().toString());
//            usuario.setSobrenome(dataSnapshot.child("sobrenome").getValue().toString());
            usuario.setMatricula(dataSnapshot.child("matricula").getValue().toString());
            usuario.setEmail(dataSnapshot.child("email").getValue().toString());
            usuario.setDataDeNascimento(dataSnapshot.child("dataDeNascimento").getValue().toString());
            usuario.setCampus(dataSnapshot.child("campus").getValue().toString());
//            usuario.setImagemPerfil(dataSnapshot.child("imagemPerfil").getValue().toString());
            usuario.setCurso(dataSnapshot.child("curso").getValue().toString());
            if(dataSnapshot.hasChild("professor")){
//                txtHorasComp.setText("Formação");
//                mHorasComp.setText(usuario.getCurso());
                txtHorasComp.setVisibility(View.GONE);
                mHorasComp.setVisibility(View.GONE);
                mCampus.setVisibility(View.GONE);
                txtCampus.setVisibility(View.GONE);
            }


//        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
//
//        Date dataa = new Date();
//        try {
//            dataa = df1.parse(usuario.getDataDeNascimento());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        String dataNascimentoStr = df.format(dataa);

            mNome.setText(usuario.getNome());
            mMatricula.setText(usuario.getMatricula());
            mEmail.setText(usuario.getEmail());
            mDataDeNascimento.setText(usuario.getDataDeNascimento());
            mCampus.setText(usuario.getCampus());
            mCurso.setText(usuario.getCurso());

//            Picasso.get()
//                    .load(usuario.getImagemPerfil())
//                    .into(mImagePerfil);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, EventosActivity.class);
        startActivity(intent);
    }

    private void escolherImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST);

    }

    private void uploadImagem() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("Users/").child(userID).child("imagem");  // PASSAR ESCOLHA DE IMAGEM PARA A TELA DE PERFIL, DEVIDO AO CURRENT LOGIN, SUBSTITUIR randomUUID PARA getCurrentUser
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(PerfilActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PerfilActivity.this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                mImagePerfil.setImageBitmap(bitmap);
                uploadImagem();

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
