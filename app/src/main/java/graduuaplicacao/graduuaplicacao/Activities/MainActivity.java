package graduuaplicacao.graduuaplicacao.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import graduuaplicacao.graduuaplicacao.Adapters.VerticalCardsAdapter;
import graduuaplicacao.graduuaplicacao.R;

public class MainActivity extends AppCompatActivity {

    private ImageView mLogoFaculdade;
    private Button mBtnFazerLogin;
    private FirebaseAnalytics analytics;
    private FirebaseAuth firebaseAuth;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogoFaculdade = (ImageView) findViewById(R.id.logoFaculdade);
//        mBtnFazerLogin = (Button) findViewById(R.id.btnFazerLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

//        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user == null) {
                    Intent intentAbrirTelaLogin = new Intent(MainActivity.this, LoginActivity.class);
                    intentAbrirTelaLogin.putExtra("IDEVENTO", id);
                    startActivity(intentAbrirTelaLogin);
                    finish();
                }else {
                    Intent intentAbrirTelaLogin = new Intent(MainActivity.this, EventosActivity.class);
                    intentAbrirTelaLogin.putExtra("IDEVENTO", id);
                    startActivity(intentAbrirTelaLogin);
                    finish();
                }
            }
        }, 4000);


//        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent());

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                if (pendingDynamicLinkData != null) {
                    analytics = FirebaseAnalytics.getInstance(MainActivity.this);

                    Uri deepLink = pendingDynamicLinkData.getLink();
                    System.out.println(deepLink);

//                    Bundle bundle = new Bundle();
                    id = deepLink.toString().substring(25);
//                    bundle.putString("IDEVENTO", id);
                    Intent intent = new Intent(MainActivity.this, EventosActivity.class);
                    intent.putExtra("IDEVENTO", id);

//                    intent.putExtras(bundle);

//                    FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(pendingDynamicLinkData);
//                    if (invite != null) {
//                        String invitationId = invite.getInvitationId();
//                        if (!TextUtils.isEmpty(invitationId))
//                            System.out.println(invitationId);
//                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failure");
            }
        });


//        FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(getIntent())
//                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
//                    @Override
//                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
//                        Uri deepLink = null;
//                        if (pendingDynamicLinkData != null) {
//                            deepLink = pendingDynamicLinkData.getLink();
//                        }
//                    }
//                })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("", "getDynamicLink:onFailure", e);
//                    }
//                });



    }
}
