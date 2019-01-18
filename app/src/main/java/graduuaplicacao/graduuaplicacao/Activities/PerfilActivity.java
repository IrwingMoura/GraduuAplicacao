package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mNome = (TextView) findViewById(R.id.txtNomePerfil);
        mSobrenome = (TextView) findViewById(R.id.txtSobrenomePerfil);
        mMatricula = (TextView) findViewById(R.id.txtMatriculaPerfil);
        mDataDeNascimento = (TextView) findViewById(R.id.txtDataDeNascimentoPerfil);
        mCampus = (TextView) findViewById(R.id.txtCampusPerfil);
        mEmail = (TextView) findViewById(R.id.txtEmailPerfil);
        mImagePerfil = (ImageView) findViewById(R.id.imagemPerfilTelaPerfil);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

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

        myRef.addValueEventListener(new ValueEventListener() {
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
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            Usuario usuario = new Usuario();
            usuario.setNome(ds.child(userID).getValue(Usuario.class).getNome());
            usuario.setSobrenome(ds.child(userID).getValue(Usuario.class).getSobrenome());
            usuario.setMatricula(ds.child(userID).getValue(Usuario.class).getMatricula());
            usuario.setEmail(ds.child(userID).getValue(Usuario.class).getEmail());
            usuario.setDataDeNascimento(ds.child(userID).getValue(Usuario.class).getDataDeNascimento());
            usuario.setCampus(ds.child(userID).getValue(Usuario.class).getCampus());
            usuario.setImagemPerfil(ds.child(userID).getValue(Usuario.class).getImagemPerfil());


            mNome.setText(usuario.getNome());
            mSobrenome.setText(usuario.getSobrenome());
            mMatricula.setText(usuario.getMatricula());
            mEmail.setText(usuario.getEmail());
            mDataDeNascimento.setText(usuario.getDataDeNascimento());
            mCampus.setText(usuario.getCampus());

            Picasso.get()
                    .load(usuario.getImagemPerfil())
                    .into(mImagePerfil);

            break;
        }
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
