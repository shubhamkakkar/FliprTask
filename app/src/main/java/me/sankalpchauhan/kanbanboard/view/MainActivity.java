package me.sankalpchauhan.kanbanboard.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import me.sankalpchauhan.kanbanboard.R;
import me.sankalpchauhan.kanbanboard.model.User;

import static me.sankalpchauhan.kanbanboard.util.Constants.USER;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private GoogleSignInClient googleSignInClient;
    Toolbar toolbar;
    String appVersionName;
    int appVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Boards");
        setSupportActionBar(toolbar);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            appVersionName = pInfo.versionName;
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                // avoid huge version numbers for this to work
                appVersionCode = (int) pInfo.getLongVersionCode();
            } else {
                //noinspection deprecation
                appVersionCode = pInfo.versionCode;
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        initDrawer();
        initGoogleSignInClient();
    }

    private void initDrawer(){
        String email, name;
        if (isAuthenticated() != null) {
            email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String[] nameemail = email.split("@");
            name = nameemail[0];
        } else {
            email = "Log In/Sign Up";
            name = "User";
        }

        if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null) {
            DrawerImageLoader.init(new AbstractDrawerImageLoader() {
                @Override
                public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                    Glide.with(getApplicationContext()).load(firebaseAuth.getCurrentUser().getPhotoUrl()).into(imageView);
                }
            });
        }

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionFirstLine(name)
                .withSelectionSecondLine(email)
                .withHeaderBackground(R.color.colorPrimary)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

//        if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()!=null) {
//            Log.e("PhotoURl", String.valueOf(firebaseAuth.getCurrentUser().getPhotoUrl()));
//            headerResult = new AccountHeaderBuilder()
//                    .withActivity(this)
//                    .withSelectionFirstLine(name)
//                    .withSelectionSecondLine(email)
//                    .withHeaderBackground(R.color.colorPrimary)
//                    .addProfiles(
//                            new ProfileDrawerItem().withName(name).withEmail(email).withIcon(String.valueOf(firebaseAuth.getCurrentUser().getPhotoUrl()))
//                    )
//                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                        @Override
//                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
//                            return false;
//                        }
//                    })
//                    .build();
//        }


        Drawer drawer = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Sign Out"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Build Version: " + appVersionName).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                signOut();
                                return false;
                        }
                        return true;
                    }
                })
                .build();
    }

    private User getUserFromIntent() {
        return (User) getIntent().getSerializableExtra(USER);
    }

    private void initGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }


    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            goToHomePageActivity();
        }
    }

    private void goToHomePageActivity() {
        finishAffinity();
        Intent intent = new Intent(MainActivity.this, HomePage.class);
        startActivity(intent);
    }

    private void signOut() {
        singOutFirebase();
        signOutGoogle();
    }

    private void singOutFirebase() {
        firebaseAuth.signOut();
    }

    private void signOutGoogle() {
        googleSignInClient.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }

    public static FirebaseUser isAuthenticated(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser();
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }
}
