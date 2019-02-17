package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mNome = findViewById(R.id.txtNomePerfil);
        mSobrenome = findViewById(R.id.txtSobrenomePerfil);
        mMatricula = findViewById(R.id.txtMatriculaPerfil);
        mDataDeNascimento = findViewById(R.id.txtDataDeNascimentoPerfil);
        mCampus = findViewById(R.id.txtCampusPerfil);
        mEmail = findViewById(R.id.txtEmailPerfil);
        mImagePerfil = findViewById(R.id.imagemPerfilTelaPerfil);


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


        StorageReference imagemRef = storageReference.child("Users").child(userID).child("imagem");
        imagemRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println(uri);
                Glide.with(PerfilActivity.this).load(uri).into(mImagePerfil);
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


            mNome.setText(usuario.getNome());
            mMatricula.setText(usuario.getMatricula());
            mEmail.setText(usuario.getEmail());
            mDataDeNascimento.setText(usuario.getDataDeNascimento());
            mCampus.setText(usuario.getCampus());

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
}
